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

import java.net.URL;
import java.util.Date;

import com.w2.haswants.R;


import com.amazonaws.auth.BasicAWSCredentials;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class PhotoActivity extends Activity {

	private AmazonS3Client s3Client = new AmazonS3Client(
			new BasicAWSCredentials(Constants.ACCESS_KEY_ID,
					Constants.SECRET_KEY));

	private Button selectPhoto = null;
	private Button showInBrowser = null;

	private static final int PHOTO_SELECTED = 1;
	
	private  String pictureName;
	private Person person;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 person = (Person) getIntent().getSerializableExtra("Person");
	     Log.d("haswants", "photo activity: " + person.getMyId());
		
	//	s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
	    int  randomNumer = 3 + (int)(Math.random()*100); 
		pictureName = "profile_" + person.getMyId() + "_" + randomNumer + ".png";
		
		setContentView(R.layout.photo);

		selectPhoto = (Button) findViewById(R.id.select_photo_button);
		selectPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Start the image picker.
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, PHOTO_SELECTED);
			}
		});

		
	}

	// This method is automatically called by the image picker when an image is
	// selected.
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case PHOTO_SELECTED:
			if (resultCode == RESULT_OK) {

				Uri selectedImage = imageReturnedIntent.getData();
				new S3PutObjectTask().execute(selectedImage);
			}
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

	private class S3PutObjectTask extends AsyncTask<Uri, Void, S3TaskResult> {

		ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = new ProgressDialog(PhotoActivity.this);
			dialog.setMessage(PhotoActivity.this
					.getString(R.string.uploading));
			dialog.setCancelable(false);
			dialog.show();
		}

		protected S3TaskResult doInBackground(Uri... uris) {

			if (uris == null || uris.length != 1) {
				return null;
			}

			// The file location of the image selected.
			Uri selectedImage = uris[0];

			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String filePath = cursor.getString(columnIndex);
			cursor.close();

			S3TaskResult result = new S3TaskResult();

				Log.d("haswants", "starting uplaod");


		         try {
		         	//s3Client.createBucket( Constants.PICTURE_BUCKET );
		         	
		         	PutObjectRequest por = new PutObjectRequest( Constants.PICTURE_BUCKET, pictureName, new java.io.File(filePath) );  
		         	por.setCannedAcl(CannedAccessControlList.PublicRead);
		         	s3Client.putObject( por );
		         	Log.d("haswants","********* file uploaded: "+ pictureName);

		         }
		         catch ( Exception exception ) {
		         	Log.e("haswants", "Upload Failure:"+ exception.getMessage() );
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
        	 Log.d("haswants", "finished updating profile photo");
        	Intent profileActivity = new Intent (getApplicationContext(), ProfileActivity.class);     
	    	profileActivity.putExtra("Person",person);
	    	startActivity(profileActivity);
        	
        }
	}
}
