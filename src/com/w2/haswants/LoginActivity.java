package com.w2.haswants;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;



public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText emailET;
	private TextView introHeadTV;
	private TextView introTV;
	private Person person;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Lato-Bol.ttf");
        
        introTV = (TextView) findViewById(R.id.introTV)
        		;
        introTV.setTypeface(typeFace);
        
    }
    
    public void loginUser(View v){
    	emailET = (EditText) findViewById(R.id.emailET);
    	String emailString = emailET.getText().toString();
    	Log.d(getClass().getName(), "emailstring: " + emailString);
    	String urlString = Constants.BASE_URL +  getString(R.string.url_api_v1_login);
    	Log.d("haswants", "Login, http call: " + urlString);
    	
    	new LoginUserTask().execute(urlString, emailString );
    	
    }
    
    public void linkEvent(View v){
    	
    }
    
    public void errorLogin(){
    	TextView introTV = (TextView)findViewById(R.id.introTV);
    	introTV.setText(getString(R.string.emailError));
    	introTV.setTextColor(Color.parseColor("#FF0000"));
    	introTV.setHeight(60);
    }
    
    private void DetermineWizard(){
    	if(person.getProfilePhoto().length() == 0){
    		Intent wizActivity = new Intent (getApplicationContext(), WizardActivity.class);     
    		wizActivity.putExtra("Person",person);
	    	startActivity(wizActivity);
    	}else{
	    	Intent mainActivity = new Intent (getApplicationContext(), MainActivity.class);     
	    	mainActivity.putExtra("Person",person);
	    	startActivity(mainActivity);
    		
    	}
    }
    
    private class LoginUserTask extends AsyncTask<String, Void, String> {
    	ProgressDialog dialog;

        protected String doInBackground(String... values) {
          Log.i("LOGGER", "Starting...");
          haswants appState = ((haswants)getApplicationContext());
          String resultString = appState.postLogin(values[0], values[1]);
          return resultString;
        }
        
     	protected void onPreExecute() {
    			dialog = new ProgressDialog(LoginActivity.this);
    			dialog.setMessage(LoginActivity.this
    					.getString(R.string.logging_in));
    			dialog.setCancelable(false);
    			dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
        	try{
	            JSONObject jObject = new JSONObject(result);
	            Boolean successLong = jObject.getBoolean("success");
	            if(successLong){
	            	// set person object
	            	//JSONObject personObj = jObject.getJSONObject("person");
	            	String MyId = jObject.getString("id");
	            	String firstName = jObject.getString("first_name");
	            	String profilePhoto = jObject.getString("profile_photo");
	            	String authToken = jObject.getString("token");
	            	
	            	person = new Person(firstName, MyId, profilePhoto, authToken);
	            	Log.d("haswants", "in loginactivity, person id: " + person.getMyId());
		        	DetermineWizard();
	            } else {
	            	errorLogin();
	            	
	            }
        	} catch (JSONException e){
        		Log.i("LOGGER", "exception: " + e.getMessage());
        		errorLogin();
        	}
            Log.i("LOGGER", "...Done");
            dialog.dismiss();
 
        }

    }

}

