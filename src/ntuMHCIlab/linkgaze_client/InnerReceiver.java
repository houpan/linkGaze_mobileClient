package ntuMHCIlab.linkgaze_client;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;

public interface InnerReceiver {
	public void sendRequestToParent(JSONObject request) throws JSONException;
	public abstract void getRequestFromParent(JSONObject request) throws JSONException;
}
