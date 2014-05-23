package ntuMHCIlab.linkgaze_client;

import ntuMHCIlab.linkgaze_client.R;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.R.string;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

public class MainActivity extends FragmentActivity {
	private JSONObject userIDJSON;
	private Handler emitHandler;
	private Handler layoutHandler;
	
	private WebSocketOptions mWebSocketOptions = new WebSocketOptions();
	
	private char currentFunction;
	private int isLockedUp;
	private String currentDeivce = "mobile";
	private JSONObject dataForServer;
	
	private Fragment fragmentLockedUp;
	private Fragment fragmentSetting;
	private Fragment fragmentMessenger;
	private Fragment fragmentMessengerInside_idiot;
	private Fragment fragmentMessengerInside_HR;
	private Fragment fragmentMessengerInside_friend;
	private Fragment fragmentPictureTesting;
	private Fragment currentFragment;//存現在在前景的Fragment
	
	private String dataBuffer = "";
	private int dataOffset = 0;
	private int dataSize = 0;
	
	private final WebSocketConnection mConnection = new WebSocketConnection();;
	final int ID_FRAG1 = 0;
	//嚴格來說LOCKED_UP並不算一個function，他由isLockedUp決定是否顯示
	enum fragmentName { LOCKED_UP, SETTING
	}
    enum personName{
		PEOPLE_IDIOT, PEOPLE_HR, PEOPLE_FRIEND
		//花痴			秘書			朋友
}
	
	//主要處理傳入的訊息
	protected void processIncomingMessage(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		if(jsonObject.getString("action").equals("stateChange")){//針對視角的處理
			//這邊還沒有針對function做分流，可能有時候即使沒在看mobile，還是要顯示。
			if(jsonObject.getString("data").equals("lookingGlass")){
				switchToFragment(fragmentName.LOCKED_UP);
			}else if(jsonObject.getString("data").equals("lookingForward")){
				switchToFragment(fragmentName.LOCKED_UP);
			}else if(jsonObject.getString("data").equals("lookingMobile")){
				switchToFragment(fragmentName.SETTING);
			}
		}else if (jsonObject.getString("action").equals("image-download")) {
			Log.e("DOWN", "download");
			sendRequestToFragment("fragmentTesting", jsonObject);
		}else if (jsonObject.getString("action").equals("image-uploading") && jsonObject.getString("data").equals("come")) {
			if(dataOffset + 10000 < dataSize && dataBuffer.length() != 0){
				sendMessageWith("image-uploading", dataBuffer.substring(dataOffset, dataOffset + 10000));
				dataOffset += 10000;
			}else {
				sendMessageWith("image-uploading-done", dataBuffer.substring(dataOffset));
				dataOffset = 0;
				dataBuffer = "";
			}
		}else if(jsonObject.getString("action").equals("image-download-header")){
			Log.e("SIZE", ""+dataSize);
			Log.e("SIZE", "d" + Integer.parseInt(jsonObject.getString("data")));
			dataSize = Integer.parseInt(jsonObject.getString("data"));
			sendMessageWith("image-downloading", "come come");
		}else if(jsonObject.getString("action").equals("image-downloading")){
			dataBuffer += jsonObject.getString("data");
			sendMessageWith("image-downloading", "come come");
		}else if (jsonObject.getString("action").equals("image-download-done")) {
			dataBuffer += jsonObject.getString("data");
			JSONObject obj = new JSONObject();
			obj.put("action", "image-download");
			obj.put("data", dataBuffer);
			sendRequestToFragment("fragmentPictureTesting", obj);
			sendMessageWith("image-done", "fuck");
		}
		
	}
	
	//把request送進去
	public void sendRequestToFragment(String fragmentTag, JSONObject message) throws JSONException{
		Log.e("HPC", "from inside");
		//用名字找
		FragmentManager fm = getFragmentManager();
		Log.e("HPC",fragmentTag.toString());
		Fragment targetFragment = fm.findFragmentByTag(fragmentTag);
		
		//找到了，call。這邊之後用成interface
		
		((InnerReceiver) targetFragment).getRequestFromParent(message);
	}

	

	//test
	public void showSettinginging(Fragment fragTohide){
		getFragmentManager().beginTransaction().hide(fragTohide).commit();
		getFragmentManager().beginTransaction().show(fragmentSetting).commit();
	}
	
	//從Fragment傳上來的request
	public void receivedRequestFromFragment(Fragment senderFragment, JSONObject request) throws JSONException{
		Log.e("JPC", senderFragment.getClass().getName());
		Log.e("JPC", senderFragment.getTag());
		if(senderFragment.getClass().getName().contains("PictureTesting")){
			Log.e("PIC", "IN");
			if(request.getString("action").equals("image-upload")){
				dataBuffer = request.getString("data").toString();
				dataSize = request.getString("data").length();
				sendMessageWith("image-upload-header", "" + dataSize);
			}
			//sendMessageWith(request.getString("action"), request.getString("data"));
			//sendMessageWith("image-upload", "data");
		}
//		if (request.get("action").equals("changeSetting")){
//			
//		}
		
		//debug: 轉到某個Fragment的方法
//		switchToFragment(fragmentName.LOCKED_UP);
	}
	
	//換到某個Fragment
	public void switchToFragment(fragmentName targetFragment){
		//把現在的Fragment隱掉
		getFragmentManager().beginTransaction().hide(currentFragment).commit();
		//顯示目標
		switch (targetFragment){
			case LOCKED_UP:
				currentFragment = fragmentLockedUp;
				break;
			case SETTING:
				currentFragment = fragmentSetting;
				break;
			default:
				break;
		}
		getFragmentManager().beginTransaction().show(currentFragment).commit();

	}
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.layout_null);
		
		fragmentLockedUp = new LockedUpFragment();
		fragmentSetting = new SettingFragment();
		fragmentMessenger = new MessengerFragment();
		fragmentMessengerInside_idiot = new MessengerInsideFragment();
		fragmentMessengerInside_HR = new MessengerInsideFragment();
		fragmentMessengerInside_friend = new MessengerInsideFragment();
		fragmentPictureTesting = new PictureTesting();
		
		
		//用hide跟show，不要用replace，不然會有解不掉的Null ptr excp
		getFragmentManager().beginTransaction().add(R.id.main_layout, fragmentSetting,"fragmentSetting").commit();
		getFragmentManager().beginTransaction().hide(fragmentSetting).commit();
		getFragmentManager().beginTransaction().add(R.id.main_layout, fragmentLockedUp,"fragmentLockedUp").commit();
		getFragmentManager().beginTransaction().hide(fragmentLockedUp).commit();
//		getFragmentManager().beginTransaction().show(fragmentLockedUp).commit();
		getFragmentManager().beginTransaction().add(R.id.main_layout, fragmentMessenger,"fragmentMessenger").commit();
		getFragmentManager().beginTransaction().hide(fragmentMessenger).commit();
		
		getFragmentManager().beginTransaction().add(R.id.main_layout, fragmentMessengerInside_idiot,"fragmentMessengerInside_idiot").commit();
		getFragmentManager().beginTransaction().hide(fragmentMessengerInside_idiot).commit();
		getFragmentManager().beginTransaction().add(R.id.main_layout, fragmentMessengerInside_HR,"fragmentMessengerInside_HR").commit();
		getFragmentManager().beginTransaction().hide(fragmentMessengerInside_HR).commit();
		getFragmentManager().beginTransaction().add(R.id.main_layout, fragmentMessengerInside_friend,"fragmentMessengerInside_friend").commit();
		//getFragmentManager().beginTransaction().show(fragmentMessengerInside_friend).commit();
		getFragmentManager().beginTransaction().hide(fragmentMessengerInside_friend).commit();
		getFragmentManager().beginTransaction().add(R.id.main_layout ,fragmentPictureTesting, "fragmentPictureTesting").commit();
		getFragmentManager().beginTransaction().show(fragmentPictureTesting).commit();
		//朋友的初始化等到onStart()才能進行，不然在create結束前系統都是看不到fragment的，在這個情況下一call sendRequestToFragment就爛(null ptr exp)
		
		
		isLockedUp = 1;
		
		
		
		//initialization: fragment = lock
//		currentFragment = fragmentLockedUp;
		//currentFragment = fragmentMessengerInside_friend;
		currentFragment = fragmentPictureTesting;
		
//        final Button button = (Button) findViewById(R.id.btnSendMsg);
//        button.setOnClickListener(new View.OnClickListener() {
//        		int i = 0;
//            public void onClick(View v) {
////            	switchToFragment(fragmentName.SETTING);
////    			FragmentManager fm = getFragmentManager();
////    			LockedUpFragment fragment1 = (LockedUpFragment)fm.findFragmentByTag("FUCK1");
////    			fragment1.hideMySelf();
//            	
//
//            }
//        });
		
		

		
		//websocketSender
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				websocketInitialization();
				Looper.prepare(); 
				
				emitHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						Bundle bundle = msg.getData();
						emitMessageThroughWebsocket(bundle.getString("action"), 
														bundle.getString("data"));
					}
				};
				Looper.loop();
			}
			
		}).start();
		
		
	}
	
	

	@Override
	protected void onStart(){
		super.onStart();
		try {
			sendRequestToFragment("fragmentMessengerInside_idiot",new JSONObject("{\"action\":\"setUser\", \"friendName\":\"PEOPLE_IDIOT\"}"));
			sendRequestToFragment("fragmentMessengerInside_HR",new JSONObject("{\"action\":\"setUser\", \"friendName\":\"PEOPLE_HR\"}"));
			sendRequestToFragment("fragmentMessengerInside_friend",new JSONObject("{\"action\":\"setUser\", \"friendName\":\"PEOPLE_FRIEND\"}"));
			//sendMessageWith("action", "data");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
//	sender: mobile/ glass/ eyeTracker/ webController
//	isActived:0/1
//	0: locked, show "lock" picture, 1: active, show the content 
//	action: handshake/setting
//	data: [in JSON] 
		//A~H and its data, refer to the app specification.
	
	private void sendMessageWith(String action, String data){
		
		Message msg = emitHandler.obtainMessage();
		Bundle dataBundle = new Bundle();
		dataBundle.putString("action", action);
		dataBundle.putString("data", data);
		
		msg.setData(dataBundle);
		emitHandler.sendMessage(msg);
	}
	private void emitMessageThroughWebsocket(String action, String data){
		dataForServer = new JSONObject();
		try {
			dataForServer.put("sender", currentDeivce );
			dataForServer.put("action", action );
			dataForServer.put("data", data );
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mConnection.sendTextMessage(dataForServer.toString());
		//mConnection.sendTextMessage(payload);
	}
	
	private void websocketInitialization(){
		final String wsuri = "ws://140.112.30.33:25566";
		mWebSocketOptions.setMaxMessagePayloadSize(mWebSocketOptions.getMaxMessagePayloadSize() * 10);
		try {
			mConnection.connect(wsuri, new WebSocketHandler() {
			
				@Override
			
			public void onOpen() {
				sendMessageWith("handshake","N/A");
				Log.e("SocketIO", "Status: Connected to " + wsuri);
//				mConnection.sendTextMessage(dataForServer.toString());
					
			}
		
			@Override
			public void onTextMessage(String payload) {
				Log.e("SocketIO", "Got echo: " + payload);
				try {
					JSONObject incommingRequest = new JSONObject(payload);
					if( incommingRequest.getString("receiver").equals(currentDeivce) 
						||incommingRequest.getString("receiver").equals("broadcast")){//所有人都要處理						
						Log.e("SocketIO", "it's me!");
						processIncomingMessage(incommingRequest);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		
			@Override
			public void onClose(int code, String reason) {
			Log.e("SocketIO", "Connection lost.");
				}
			}, mWebSocketOptions);
		} catch (WebSocketException e) {
		
			Log.e("SocketIO", e.toString());
		}
		
	}
	



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		return;
	}
	
}
