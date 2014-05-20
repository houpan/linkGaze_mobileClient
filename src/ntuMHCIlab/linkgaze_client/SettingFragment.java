package ntuMHCIlab.linkgaze_client;


import ntuMHCIlab.linkgaze_client.R;

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
import android.widget.RelativeLayout;

public class SettingFragment extends Fragment implements InnerReceiver{
    private checkboxOnClickListener _checkboxOnClickListener;
    private View fragmentView;
	
    enum checkboxId{
    		PEOPLE_IDIOT, PEOPLE_HR, PEOPLE_FRIEND, 
    		//花痴			秘書			朋友
    		BANK_DATE, BANK_SUMMARY, BANK_WITHDRAW, BANK_DEPOSIT, BANK_BALANCE
    		//交易日期	摘要				提款				存款			餘額
    }
    
	class checkboxOnClickListener implements OnClickListener{
		
		@Override
		public void onClick(View v){
//			Message msg = layoutHandlerReference.obtainMessage();
//			Bundle bundle = new Bundle();
//			bundle.putString("message", "HERERERERER");
//			msg.setData(bundle);
//			layoutHandlerReference.sendMessage(msg);
			try {
				JSONObject jsonChangeSettingRequest  = new JSONObject();
				jsonChangeSettingRequest.put("action", "changeSetting");
				jsonChangeSettingRequest.put("data", gatherSettingToJSON().toString());
				sendRequestToParent(jsonChangeSettingRequest);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
//			debug:可以看傳進來的view是誰
//			if(v.getId() == R.id.CHBX_PEOPLE_IDIOT){
//				Log.e("HPC","GOT IT"+v.getId());
//			}
			
		}
	
	}
	public JSONObject gatherSettingToJSON() throws JSONException{
		JSONObject settingInJson = new JSONObject();
		
		settingInJson.put("PEOPLE_IDIOT", 
				((CheckBox)fragmentView.findViewById(R.id.CHBX_PEOPLE_IDIOT)).isChecked());
		settingInJson.put("PEOPLE_HR", 
				((CheckBox)fragmentView.findViewById(R.id.CHBX_PEOPLE_HR)).isChecked());
		settingInJson.put("PEOPLE_FRIEND", 
				((CheckBox)fragmentView.findViewById(R.id.CHBX_PEOPLE_FRIEND)).isChecked());
		
		settingInJson.put("BANK_DATE", 
				((CheckBox)fragmentView.findViewById(R.id.CHBX_BANK_DATE)).isChecked());
		settingInJson.put("BANK_SUMMARY", 
				((CheckBox)fragmentView.findViewById(R.id.CHBX_BANK_SUMMARY)).isChecked());
		settingInJson.put("BANK_WITHDRAW", 
				((CheckBox)fragmentView.findViewById(R.id.CHBX_BANK_WITHDRAW)).isChecked());
		settingInJson.put("BANK_DEPOSIT", 
				((CheckBox)fragmentView.findViewById(R.id.CHBX_BANK_DEPOSIT)).isChecked());
		settingInJson.put("BANK_BALANCE", 
				((CheckBox)fragmentView.findViewById(R.id.CHBX_BANK_BALANCE)).isChecked());
		
//		Log.e("HPC",settingInJson.toString());
		return settingInJson;
	}
    public void fuck2(){
    		Log.e("HPC","being fucked by outside");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 // TODO Auto-generated method stub
    	
    		fragmentView = inflater.inflate(R.layout.setting_fragment, container, false);
    		_checkboxOnClickListener = new checkboxOnClickListener();
    		
    		fragmentView.findViewById(R.id.CHBX_PEOPLE_IDIOT).setOnClickListener(_checkboxOnClickListener);
    		fragmentView.findViewById(R.id.CHBX_PEOPLE_HR).setOnClickListener(_checkboxOnClickListener);
    		fragmentView.findViewById(R.id.CHBX_PEOPLE_FRIEND).setOnClickListener(_checkboxOnClickListener);
    		fragmentView.findViewById(R.id.CHBX_BANK_DATE).setOnClickListener(_checkboxOnClickListener);
    		fragmentView.findViewById(R.id.CHBX_BANK_SUMMARY).setOnClickListener(_checkboxOnClickListener);
    		fragmentView.findViewById(R.id.CHBX_BANK_WITHDRAW).setOnClickListener(_checkboxOnClickListener);
    		fragmentView.findViewById(R.id.CHBX_BANK_DEPOSIT).setOnClickListener(_checkboxOnClickListener);
    		fragmentView.findViewById(R.id.CHBX_BANK_BALANCE).setOnClickListener(_checkboxOnClickListener);
    		
    		return fragmentView;  
		
		 
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
    		super.onCreate(savedInstanceState);
    		
    }
	@Override
	public void getRequestFromParent(JSONObject request) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	public void sendRequestToParent(JSONObject request) throws JSONException{
		((MainActivity)getActivity()).receivedRequestFromFragment(this, request);
	}    
}
