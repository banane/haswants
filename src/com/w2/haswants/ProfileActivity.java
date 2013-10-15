package com.w2.haswants;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.Activity;
import android.content.Intent;

public class ProfileActivity extends Activity {
	 private WebView webView;
	 private Person person;
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
		   	super.onCreate(savedInstanceState);
		   	Log.d("haswants", "in profile oncreate");
		    setContentView(R.layout.profile);

	        person = (Person) getIntent().getSerializableExtra("Person");
	        Log.d("haswants", person.getMyId());

	        webView = (WebView) findViewById(R.id.webview);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebViewClient(new WebViewClient());
			String url = Constants.BASE_URL + getString(R.string.url_api_v1_profile) + "/" + person.getMyId() + "/edit?auth_token="+ person.getAuthToken() ;
			Log.d("haswants", "url api v1 profile wth id: " + url);
			webView.loadUrl(url);
	   }
	
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.profilemenu, menu);
	        return super.onCreateOptionsMenu(menu);
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle presses on the action bar items
	        switch (item.getItemId()) {
	        	case R.id.action_photo:
	        		openPhoto();
	        		return true;
	        	
	        	case R.id.action_return_stream:
	        		returnStream();
	        		return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
	    public void returnStream(){
	    	Intent mainActivity = new Intent (getApplicationContext(), MainActivity.class);     
	    	mainActivity.putExtra("Person",person);
	    	startActivity(mainActivity);
	    }
	    public void openPhoto(){

	    	Intent photoActivity = new Intent (getApplicationContext(), PhotoActivity.class);     
	    	photoActivity.putExtra("Person",person);
	    	startActivity(photoActivity);
	    }
}
