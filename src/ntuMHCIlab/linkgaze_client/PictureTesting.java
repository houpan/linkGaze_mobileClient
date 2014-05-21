package ntuMHCIlab.linkgaze_client;

import ntuMHCIlab.linkgaze_client.MessengerFragment.dialogueOnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class PictureTesting extends Fragment implements InnerReceiver{
	
	private static final int IMAGE_PICKER_SELECT = 999;
	
	private View v;
	private Button chooseBtn;
	private ImageView pictureImageView;

	@Override
	public void sendRequestToParent(JSONObject request) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRequestFromParent(JSONObject request) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.picture_test, container, false);
		chooseBtn = (Button) v.findViewById(R.id.BTN_CHOOSE_PICTURE);
		chooseBtn.setOnClickListener(clickListener);
		pictureImageView = (ImageView) v.findViewById(R.id.image_picture);
		return v;  
   }

	Button.OnClickListener clickListener = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(galleryIntent, IMAGE_PICKER_SELECT);
			
		}
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK){
			MainActivity activity = (MainActivity) getActivity();
			Bitmap bitmap = getBitmapFromCameraData(data, activity);
			pictureImageView.setImageBitmap(bitmap);
		}
	}
	
	private Bitmap getBitmapFromCameraData(Intent data, Context context){
		Uri selectedImageUri = data.getData();
		String[] projection = {MediaStore.Images.Media.DATA};
		Cursor cursor = context.getContentResolver().query(selectedImageUri, projection, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(projection[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return BitmapFactory.decodeFile(picturePath);
	}

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		  // TODO Auto-generated method stub
		  super.onCreate(savedInstanceState);
		    		
	}
}
