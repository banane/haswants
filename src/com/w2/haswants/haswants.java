package com.w2.haswants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import android.app.Application;
import android.util.Log;

public class haswants extends Application {	
	private HttpPost httpPost;
	private String responseString;
	private HttpEntity resEntity;	
	public String postLogin(String URL, String email) {
		
		   try {
			   Log.d(getClass().getName(), "starting http call");
		        HttpClient client = new DefaultHttpClient();  
		        String postURL = URL;
		        HttpPost post = new HttpPost(postURL); 
		            List<NameValuePair> params = new ArrayList<NameValuePair>();
		            params.add(new BasicNameValuePair("email", email));
		            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
		            post.setEntity(ent);
		            HttpResponse responsePOST = client.execute(post);  
		            resEntity = responsePOST.getEntity();  
		            if (resEntity != null) {    
		            	responseString = EntityUtils.toString(resEntity);
		                Log.i("RESPONSE",responseString);
		            }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return responseString;
    }
	
	public String postPhoto(String URL, String profile_photo_url, String auth_token) {
		
		   try {
			   Log.d(getClass().getName(), "starting http call to post photo");
		        HttpClient client = new DefaultHttpClient();  
		        String postURL = URL;
		        HttpPut put = new HttpPut(postURL); 
		        put.addHeader("Content-Type", "application/json");
		        put.addHeader("Accept", "application/json");
	            
//		        List<NameValuePair> params = new ArrayList<NameValuePair>();
//	            params.add(new BasicNameValuePair("profile_photo", profile_photo_url));
//	            params.add(new BasicNameValuePair("auth_token", auth_token));
//	            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);

	            JSONObject params = new JSONObject();
	            params.put("profile_photo", profile_photo_url);
	            params.put("auth_token", auth_token);

	            StringEntity ent = new StringEntity(params.toString());  
	            put.setEntity(ent);
	            HttpResponse responsePUT = client.execute(put);  
	            resEntity = responsePUT.getEntity();  
	            if (resEntity != null) {    
	            	responseString = EntityUtils.toString(resEntity);
	                Log.i("RESPONSE",responseString);
	                Log.d("haswants", "the response string: " + responseString);
	            } else {
	                Log.d("haswants", "response string from photo upload empty.");

	            }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return responseString;
	}
}
