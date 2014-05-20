package ntuMHCIlab.linkgaze_client;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class MessengerFragment extends Fragment implements InnerReceiver{
    private dialogueOnClickListener _dialogueOnClickListener;
    private View fragmentView;
    private MessengerDatabase messengerDatabase;
	
    enum personName{
    		PEOPLE_IDIOT, PEOPLE_HR, PEOPLE_FRIEND
    		//花痴			秘書			朋友
    }
    
	class dialogueOnClickListener implements OnClickListener{
		
		@Override
		public void onClick(View v){
//			try {
//				JSONObject jsonChangeSettingRequest  = new JSONObject();
////				jsonChangeSettingRequest.put("action", "changeSetting");
////				jsonChangeSettingRequest.put("data", gatherSettingToJSON().toString());
////				sendRequestToParent(jsonChangeSettingRequest);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
			
		}
	
	}
    public void getRequestFromParent(JSONObject request) throws JSONException{
    		if(request.getString("action").equals("newMessage")){
    			newMessageInbound(request.getString("data"));
    		}
    		
    		Log.e("HPC","being fucked by outside");
    }
    
    //處理送進來的資料
    public void newMessageInbound(String message) throws JSONException{
    		JSONObject messageParsed = new JSONObject(message);
    		TextView targetText = null;
    		//msgSender/ serialNumber
    		if(messageParsed.getString("msgSender").equals("PEOPLE_IDIOT")){
    			targetText = (TextView)(fragmentView.findViewById(R.id.TEXT_IDIOT));
    		}else if(messageParsed.getString("msgSender").equals("PEOPLE_HR")){
    			targetText = (TextView)(fragmentView.findViewById(R.id.TEXT_HR));
    		}else if(messageParsed.getString("msgSender").equals("PEOPLE_FRIEND")){
    			targetText = (TextView)(fragmentView.findViewById(R.id.TEXT_FRIEND));
    		}
		JSONObject resultToDisplay = messengerDatabase.queryByTarget_AndSN_(messageParsed.getString("msgSender"), messageParsed.getInt("serialNumber"));
		targetText.setText(resultToDisplay.getString("dialogue"));
	}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 // TODO Auto-generated method stub
    		messengerDatabase = new MessengerDatabase();
    		fragmentView = inflater.inflate(R.layout.messenger, container, false);
    		_dialogueOnClickListener = new dialogueOnClickListener();
    		
    		fragmentView.findViewById(R.id.LAYOUT_IDIOT).setOnClickListener(_dialogueOnClickListener);
    		fragmentView.findViewById(R.id.LAYOUT_HR).setOnClickListener(_dialogueOnClickListener);
    		fragmentView.findViewById(R.id.LAYOUT_FRIEND).setOnClickListener(_dialogueOnClickListener);

    		insertDefaultMessage();
    		
    		return fragmentView;  
		
		 
    }
    public void insertDefaultMessage(){
    		int i;
		try {
	    		JSONObject fakeRequest = new JSONObject();
	    		JSONObject fakeData = new JSONObject();
	    		for(i = -1 ; i<1;i++){
	    			fakeData.put("msgSender","PEOPLE_IDIOT");
		    		fakeData.put("serialNumber",i);
		    		fakeRequest.put("action", "newMessage");
		    		fakeRequest.put("data",fakeData.toString());
		    		getRequestFromParent(fakeRequest);
	    		}
	    		for(i = -1 ; i<1;i++){
	    			fakeData.put("msgSender","PEOPLE_HR");
		    		fakeData.put("serialNumber",i);
		    		fakeRequest.put("action", "newMessage");
		    		fakeRequest.put("data",fakeData.toString());
		    		getRequestFromParent(fakeRequest);
	    		}
	    		for(i = 0 ; i<7 ; i++){
	    			fakeData.put("msgSender","PEOPLE_FRIEND");
		    		fakeData.put("serialNumber",i);
		    		fakeRequest.put("action", "newMessage");
		    		fakeRequest.put("data",fakeData.toString());
		    		getRequestFromParent(fakeRequest);
	    		}

	    		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
    		super.onCreate(savedInstanceState);
    		
    }
	public void sendRequestToParent(JSONObject request) throws JSONException{
		((MainActivity)getActivity()).receivedRequestFromFragment(this, request);
	}
    
}