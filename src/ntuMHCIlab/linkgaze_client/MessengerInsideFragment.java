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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessengerInsideFragment extends Fragment implements InnerReceiver{
    private dialogueOnClickListener _dialogueOnClickListener;
    private View fragmentView;
    private MessengerDatabase messengerDatabase;
	private int idSerial_item;
	private int idSerial_threeComponents;
	private String personName;
	private int selfieResourceId;
    
    
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
	//send request在abstract class中做掉了
	
    public void getRequestFromParent(JSONObject request) throws JSONException{
    		Log.e("HPC","內部："+request.toString());
    		if(request.getString("action").equals("newMessage")){
    			newMessageInbound(request.getString("data"));
    		}else if(request.getString("action").equals("setUser")){
    			setUser(request.getString("friendName"));
    		}
    		
    		Log.e("HPC","being fucked by outside");
    }
    
    public void setUser(String person){
    		
    		Log.e("HPC",person);
    		Log.e("HPC",fragmentView.toString());
    		idSerial_item = 10000;
    		idSerial_threeComponents = 3333;
    		TextView textView_nameBar = (TextView) fragmentView.findViewById(R.id.TXT_NAME_BAR);

    		if(person.equals("PEOPLE_IDIOT")){
	    		textView_nameBar.setText("补泰熟");
	    		selfieResourceId = R.drawable.people_idiot;
	    		personName = "PEOPLE_IDIOT";
	    		
    		}else if(person.equals("PEOPLE_HR")){
    			textView_nameBar.setText("花旗銀行");
    			selfieResourceId = R.drawable.people_hr;
    			idSerial_item += 10000;
    			idSerial_threeComponents += 10000;
    			personName = "PEOPLE_HR";
    			
    		}else if(person.equals("PEOPLE_FRIEND")){
    			textView_nameBar.setText("羅詩婷");
    			selfieResourceId = R.drawable.people_friend;
    			idSerial_item += 20000;
    			idSerial_threeComponents += 20000;
    			personName = "PEOPLE_FRIEND";
    		}
    		
    		insertDefaultMessage();
    	
    }
    //處理送進來的資料
    public void newMessageInbound(String message) throws JSONException{

    		JSONObject messageParsed = new JSONObject(message);
        	if(!messageParsed.getString("msgSender").equals(personName)){//假設不是自己，就不用處理了
        		return;
        	}
        	
    		//msgSender/ serialNumber
    		JSONObject resultToDisplay = messengerDatabase.queryByTarget_AndSN_(messageParsed.getString("msgSender"), messageParsed.getInt("serialNumber"));
    		Boolean isSentBySelf;//是不是自己傳的
    		if(resultToDisplay.getString("sender").equals("SELF")){
    			isSentBySelf = true;
    		}else{
    			isSentBySelf = false;
    		}
    		

    		
    		
    		RelativeLayout.LayoutParams itemLayoutParams = new RelativeLayout.LayoutParams(
    				RelativeLayout.LayoutParams.MATCH_PARENT,
    				RelativeLayout.LayoutParams.WRAP_CONTENT);
    		
    	    RelativeLayout newItemLayout;
    	    newItemLayout = new RelativeLayout(getActivity());
    	    if(idSerial_item != 10000){//非第一個，就要連到上一個人
    	    		itemLayoutParams.addRule(RelativeLayout.BELOW, idSerial_item);
    	    }    	    
    	    newItemLayout.setId( ++ idSerial_item);
    	    newItemLayout.setLayoutParams(itemLayoutParams);
    	    
    	    
//    	    ---- 對方的狀態
    	    
    	    RelativeLayout.LayoutParams imagePeopleParams = new RelativeLayout.LayoutParams(
    				RelativeLayout.LayoutParams.WRAP_CONTENT,
    				RelativeLayout.LayoutParams.WRAP_CONTENT);
    	    if(isSentBySelf){
    	    		imagePeopleParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
    	    }else{
    	    		imagePeopleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
    	    }    	    
    	    imagePeopleParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
    		
    		ImageView imagePeople = new ImageView(getActivity());
    		
    	    if(isSentBySelf){
    	    		imagePeople.setImageResource(R.drawable.people_self);
	    }else{
	    		imagePeople.setImageResource(selfieResourceId);
	    }    	    
    		imagePeople.setLayoutParams(imagePeopleParams);
    		imagePeople.setId(++ idSerial_threeComponents);

    		newItemLayout.addView(imagePeople);
    		
//    		--Text
    	    RelativeLayout.LayoutParams textPeopleParams = new RelativeLayout.LayoutParams(
    				RelativeLayout.LayoutParams.WRAP_CONTENT,
    				RelativeLayout.LayoutParams.WRAP_CONTENT);
    	    
    	    if(isSentBySelf){
    	    		textPeopleParams.addRule(RelativeLayout.LEFT_OF, idSerial_threeComponents);
	    }else{
	    		textPeopleParams.addRule(RelativeLayout.RIGHT_OF, idSerial_threeComponents);
	    }
    	    
    	    textPeopleParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
    		
    		TextView textPeople = new TextView(getActivity());
    		textPeople.setLayoutParams(textPeopleParams);
    		textPeople.setText(resultToDisplay.getString("dialogue"));
    		textPeople.setId(++ idSerial_threeComponents);
    		
    		newItemLayout.addView(textPeople);
    		
//    		--link
    		
    	    RelativeLayout.LayoutParams imageLinkParams = new RelativeLayout.LayoutParams(
    				RelativeLayout.LayoutParams.WRAP_CONTENT,
    				RelativeLayout.LayoutParams.WRAP_CONTENT);
    	    if(isSentBySelf){
    	    		imageLinkParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
	    }else{
	    		imageLinkParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
	    }
    	    imageLinkParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
    		
    		ImageView imageLink = new ImageView(getActivity());
    		imageLink.setImageResource(R.drawable.link);
    		imageLink.setLayoutParams(imageLinkParams);
    		imageLink.setId(++ idSerial_threeComponents);
    		if(!resultToDisplay.getString("link").equals("N/A")){//有link才加
    			newItemLayout.addView(imageLink);
    			//要listener
    		}
    	    RelativeLayout layoutDialogueSection = (RelativeLayout)fragmentView.findViewById(R.id.LAYOUT_DIALOGUE_SECTION);
    	    layoutDialogueSection.addView(newItemLayout);

	}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 // TODO Auto-generated method stub
    		messengerDatabase = new MessengerDatabase();
    		fragmentView = inflater.inflate(R.layout.messenger_inside, container, false);
    		_dialogueOnClickListener = new dialogueOnClickListener();
    		//Button choose_picButton = findViewById(R.id.BTN_SEND_MESSAGE);
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