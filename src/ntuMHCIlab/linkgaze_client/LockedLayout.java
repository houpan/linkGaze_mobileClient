package ntuMHCIlab.linkgaze_client;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import ntuMHCIlab.linkgaze_client.R;

public class LockedLayout {
    private RelativeLayout layoutParent;
    private RelativeLayout layoutLocked;
    private MainActivity rootActivityReference;
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
    //creating layout
	public LockedLayout(MainActivity rootActivity){
		RelativeLayout.LayoutParams basicRelativeLayoutParams_3 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		basicRelativeLayoutParams_3.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		
	    layoutParent = new RelativeLayout(rootActivity);
		layoutParent.setLayoutParams(basicRelativeLayoutParams_3);
	
	
		RelativeLayout.LayoutParams basicRelativeLayoutParams_1= new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		basicRelativeLayoutParams_1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		
	    RelativeLayout layoutLocked;
	    layoutLocked = new RelativeLayout(rootActivity);
		layoutLocked.setLayoutParams(basicRelativeLayoutParams_1);
	
		RelativeLayout.LayoutParams basicRelativeLayoutParams_2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		basicRelativeLayoutParams_2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		
		ImageButton imagebuttonLock = new ImageButton(rootActivity);
		imagebuttonLock.setImageResource(R.drawable.mobile_lock);
		imagebuttonLock.setLayoutParams(basicRelativeLayoutParams_2);
		imagebuttonLock.setBackgroundColor(Color.TRANSPARENT);
	    
		layoutLocked.addView(imagebuttonLock);
		
		layoutParent.addView(layoutLocked);
		
		_btnSendMsgOnClickListener = new btnSendMsgOnClickListener();
		imagebuttonLock.setOnClickListener(_btnSendMsgOnClickListener);		
		
		
	}
}
