/*
 * Copyright 2010-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.w2.haswants;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import com.w2.haswants.R;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PhotoActivity extends Activity {

	private AmazonS3Client s3Client = new AmazonS3Client(
			new BasicAWSCredentials(Constants.ACCESS_KEY_ID,
					Constants.SECRET_KEY));
	private  String pictureName;
	private Person person;
	final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 123;
	private String TAG = "CONNECTOR";
	private String fileNameStr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 person = (Person) getIntent().getSerializableExtra("Person");
	     Log.d(TAG, "photo activity: " + person.getMyId());
		
	//	s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
	    int  randomNumer = 3 + (int)(Math.random()*100); 
		pictureName = "profile_" + person.getMyId() + "_" + randomNumer + ".png";
		
		setContentView(R.layout.photo);

		
	}
	
	public void clickCamera(View v){
		
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "resultCode: " + resultCode);
		Log.d(TAG, "requestCode: "+ requestCode);
		
		if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
			if ((resultCode == RESULT_OK) && (data != null)){
				Bitmap imageData = (Bitmap) data.getExtras().get("data");
	
				Log.d(TAG, "resultcode: camera pic request, about to kick off s3 put object task");
				new S3PutObjectTask().execute(imageData);
			} else {
				Log.e(TAG, "Error: no data or result error");
			}
		} else {
			Log.e(TAG, "resultcode was not activity result");
		}
	}


	private class S3PutObjectTask extends AsyncTask<Bitmap, Void, S3TaskResult> {

		ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = new ProgressDialog(PhotoActivity.this);
			dialog.setMessage(PhotoActivity.this
					.getString(R.string.uploading));
			dialog.setCancelable(false);
			dialog.show();
		}

		protected S3TaskResult doInBackground(Bitmap... uris) {

			if (uris == null || uris.length != 1) {
				return null;
			}

			// The file location of the image selected.
			Bitmap imageData = uris[0];
         	/*Log.d(TAG,"assigned uri selectedimage from arguments");

			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String filePath = cursor.getString(columnIndex);
			cursor.close();
         	Log.d(TAG,"put image data into filepath");*/
			
			try {
				String root = Environment.getExternalStorageDirectory().toString();
				fileNameStr = root + "/haswants" + pictureName;
				FileOutputStream out = new FileOutputStream(fileNameStr);
				imageData.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.close();
				Log.d(TAG, "Saved: " + fileNameStr);
			} catch (FileNotFoundException e) {
				Log.e(TAG,"filenotfound: " + e.toString());
			} catch (IOException e) {
				Log.e(TAG, "ioexcpetion:" + e.toString());
			} catch (Exception e) {
				Log.e(TAG, "general exception: " + e.toString());
			} 


			S3TaskResult result = new S3TaskResult();

				Log.d(TAG, "starting upload");


		         try {
		         	
		         	PutObjectRequest por = new PutObjectRequest( Constants.PICTURE_BUCKET, pictureName, new java.io.File(fileNameStr) );  
		         	por.setCannedAcl(CannedAccessControlList.PublicRead);
		         	s3Client.putObject( por );
		         	Log.d(TAG,"********* file uploaded: "+ pictureName);

		         }
		         catch ( Exception exception ) {
		         	Log.e(TAG, "Upload Failure:"+ exception.getMessage() );
		         }
		         // now post to server

			return result;
		}

		protected void onPostExecute(S3TaskResult result) {

			dialog.dismiss();
			

			if (result.getErrorMessage() != null) {

				displayErrorAlert(
						PhotoActivity.this
								.getString(R.string.upload_failure_title),
						result.getErrorMessage());
			} else {
				ResponseHeaderOverrides override = new ResponseHeaderOverrides();
				override.setContentType( "image/png" );
//				String pictureUrl = "https://mobilephotos.s3.amazonaws.com/" + pictureName;
				GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( Constants.getPictureBucket(), pictureName );
				urlRequest.setExpiration( new Date( System.currentTimeMillis() + 3600000 ) );  // Added an hour's worth of milliseconds to the current time.
				urlRequest.setResponseHeaders( override );
				URL url = s3Client.generatePresignedUrl( urlRequest );
				String pictureUrl = url.toString();

				String putString = Constants.BASE_URL +  getString(R.string.url_api_v1_profile) + "/" + person.getMyId()  + ".json";
				new PostPhotoTask().execute(putString, pictureUrl, person.getAuthToken() );
			}
		}
	}
	

	

	private class S3TaskResult {
		String errorMessage = null;
		Uri uri = null;

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public Uri getUri() {
			return uri;
		}

		public void setUri(Uri uri) {
			this.uri = uri;
		}
	}
	
	
	private class PostPhotoTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... values) {
          Log.i("haswants", "Starting... to upload post photo");
          haswants appState = ((haswants)getApplicationContext());
          String resultString = appState.postPhoto(values[0], values[1], values[2]);
          return resultString;
        }

        @Override
        protected void onPostExecute(String result) {
        	 Log.d(TAG, "finished updating profile photo");
        	Intent profileActivity = new Intent (getApplicationContext(), ProfileActivity.class);     
	    	profileActivity.putExtra("Person",person);
	    	startActivity(profileActivity);
        	
        }
	}
	
	// Display an Alert message for an error or failure.
	protected void displayAlert(String title, String message) {

		AlertDialog.Builder confirm = new AlertDialog.Builder(this);
		confirm.setTitle(title);
		confirm.setMessage(message);

		confirm.setNegativeButton(
				PhotoActivity.this.getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		confirm.show().show();
	}

	protected void displayErrorAlert(String title, String message) {

		AlertDialog.Builder confirm = new AlertDialog.Builder(this);
		confirm.setTitle(title);
		confirm.setMessage(message);

		confirm.setNegativeButton(
				PhotoActivity.this.getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						PhotoActivity.this.finish();
					}
				});

		confirm.show().show();
	}

}
