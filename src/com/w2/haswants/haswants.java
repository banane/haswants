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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


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
}
