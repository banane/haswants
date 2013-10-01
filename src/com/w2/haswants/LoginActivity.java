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

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText emailET;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
    }
    
    public void loginUser(View v){
    	emailET = (EditText) findViewById(R.id.emailET);;
    	String emailString = emailET.getText().toString();
    	Log.d(getClass().getName(), "emailstring: " + emailString);
    	String urlString = getString(R.string.url_api_v1_login);
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
    private class LoginUserTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... values) {
          Log.i("LOGGER", "Starting...");
          haswants appState = ((haswants)getApplicationContext());
          String resultString = appState.postLogin(values[0], values[1]);
          return resultString;
        }

        @Override
        protected void onPostExecute(String result) {
        	try{
	            JSONObject jObject = new JSONObject(result);
	            Boolean successLong = jObject.getBoolean("success");
	            if(successLong){
	            	// set person object
	            	JSONObject personObj = jObject.getJSONObject("person");
	            	
	            	Person person = new Person(personObj.getString("first_name"), personObj.getString("id"));
		        	Intent mainActivity = new Intent (getApplicationContext(), MainActivity.class);     
		            startActivity(mainActivity);
	            } else {
	            	errorLogin();
	            	
	            }
        	} catch (JSONException e){
        		Log.i("LOGGER", "exception: " + e.getMessage());
        		errorLogin();
        	}
            Log.i("LOGGER", "...Done");
 
        }

    }

}

