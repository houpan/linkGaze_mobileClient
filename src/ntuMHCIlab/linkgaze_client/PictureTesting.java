package ntuMHCIlab.linkgaze_client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import ntuMHCIlab.linkgaze_client.MessengerFragment.dialogueOnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PictureTesting extends Fragment implements InnerReceiver {

	private static final int IMAGE_PICKER_SELECT = 999;

	private View v;
	private Button chooseBtn;
	private Button uploadBtn;
	private ImageView pictureImageView;
	private ImageView receiveImageView;
	// private TextView base64TextView;
	private String base64ImageString;
	private String resultString;

	@Override
	public void sendRequestToParent(JSONObject request) throws JSONException {
		((MainActivity) getActivity()).receivedRequestFromFragment(this,
				request);

	}

	@Override
	public void getRequestFromParent(JSONObject request) throws JSONException {
		if (request.getString("action").equals("image-download")) {
			Log.e("PICTEST", "DECODE");
			
			resultString = request.getString("data");
			Log.e("DIFF", ""+ resultString.equals(base64ImageString));
			byte receiveImageByteArray[] = Base64.decode(
					request.getString("data"), Base64.DEFAULT);
			Toast.makeText(getActivity(), "DIFF:"+ resultString.equals(base64ImageString), Toast.LENGTH_LONG);
			receiveImageView.setImageBitmap(BitmapFactory.decodeByteArray(
					receiveImageByteArray, 0, receiveImageByteArray.length));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.picture_test, container, false);
		chooseBtn = (Button) v.findViewById(R.id.BTN_CHOOSE_PICTURE);
		chooseBtn.setOnClickListener(chooseListener);
		uploadBtn = (Button) v.findViewById(R.id.BTN_UPLOAD);
		uploadBtn.setOnClickListener(uploadlistener);
		receiveImageView = (ImageView) v.findViewById(R.id.SHOW_RECEIVE);
		pictureImageView = (ImageView) v.findViewById(R.id.image_picture);
		return v;
	}

	Button.OnClickListener chooseListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent galleryIntent = new Intent(Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(galleryIntent, IMAGE_PICKER_SELECT);

		}
	};

	Button.OnClickListener uploadlistener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (base64ImageString != null) {
				Log.e("base64", "" + base64ImageString);
				JSONObject base64Object = new JSONObject();
				try {
					base64Object.put("action", "image-upload");
					base64Object.put("data", base64ImageString);
					Log.e("image length: ",""+base64ImageString.length());
					sendRequestToParent(base64Object);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				try {
					sendRequestToParent(base64Object);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// base64TextView.setText(base64TextView.getText().toString() +
				// base64ImageString);
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGE_PICKER_SELECT
				&& resultCode == Activity.RESULT_OK) {
			MainActivity activity = (MainActivity) getActivity();
			Bitmap bitmap = getBitmapFromCameraData(data, activity);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, stream);
			byte imageArray[] = stream.toByteArray();
			base64ImageString = Base64.encodeToString(imageArray,
					Base64.DEFAULT);
			pictureImageView.setImageBitmap(bitmap);

		}
	}

	private Bitmap getBitmapFromCameraData(Intent data, Context context) {
		Uri selectedImageUri = data.getData();
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImageUri,
				projection, null, null, null);
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
