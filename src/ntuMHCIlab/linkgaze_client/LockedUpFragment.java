package ntuMHCIlab.linkgaze_client;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import ntuMHCIlab.linkgaze_client.R;

public class LockedUpFragment extends Fragment implements InnerReceiver{
    private RelativeLayout layoutParent;
    private RelativeLayout layoutLocked;
    private MainActivity thisReference;
    Handler layoutHandlerReference;
	
    private btnSendMsgOnClickListener _btnSendMsgOnClickListener;
	
    
	class btnSendMsgOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v){
			Message msg = layoutHandlerReference.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putString("message", "HERERERERER");
			msg.setData(bundle);
			layoutHandlerReference.sendMessage(msg);
		}
	}
  
    public RelativeLayout getInstance(){
    		return layoutParent;
    }
    public void setLayoutHandler(Handler layoutHandler){

    		layoutHandler = layoutHandlerReference;
    }
    //test
    public void fuck2(){
    		Log.e("HPC","being fucked by outside");
    }
    //test
    public void hideMySelf(){
		Log.e("HPC","being fucked by outside");
		((MainActivity)getActivity()).showSettinginging(this);
    }
    
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 // TODO Auto-generated method stub
    	
		RelativeLayout.LayoutParams basicRelativeLayoutParams_3 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		basicRelativeLayoutParams_3.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		
	    layoutParent = new RelativeLayout(getActivity());
		layoutParent.setLayoutParams(basicRelativeLayoutParams_3);
	
	
		RelativeLayout.LayoutParams basicRelativeLayoutParams_1= new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		basicRelativeLayoutParams_1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		
	    RelativeLayout layoutLocked;
	    layoutLocked = new RelativeLayout(getActivity());
		layoutLocked.setLayoutParams(basicRelativeLayoutParams_1);
	
		RelativeLayout.LayoutParams basicRelativeLayoutParams_2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		basicRelativeLayoutParams_2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		
		ImageButton imagebuttonLock = new ImageButton(getActivity());
		imagebuttonLock.setImageResource(R.drawable.mobile_lock);
		imagebuttonLock.setLayoutParams(basicRelativeLayoutParams_2);
		imagebuttonLock.setBackgroundColor(Color.TRANSPARENT);
	    
		layoutLocked.addView(imagebuttonLock);
		
		layoutParent.addView(layoutLocked);
    	
    		
//		((MainActivity)getActivity()).fuckyou();
//		View view = inflater.inflate(R.layout.view_word_grid, null);
//		
//		// Here we are fetching the layoutParams from parent activity and setting it to the fragment's view.		
//		view.setLayoutParams(((DroidOpsActivity) activity).fetchLayoutParams());
		return layoutParent;
		
		 
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
    
    //creating layout
//	public LockedLayout_(MainActivity this){
//		_btnSendMsgOnClickListener = new btnSendMsgOnClickListener();
//		imagebuttonLock.setOnClickListener(_btnSendMsgOnClickListener);		
//		
//		
//	}
}
