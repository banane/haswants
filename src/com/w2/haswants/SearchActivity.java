package com.w2.haswants;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.ActionBar;

public class SearchActivity extends Activity {
	private WebView webView;
	private Person person;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream);

        person = (Person) getIntent().getSerializableExtra("Person");
        Log.d("haswants", "in search, id: " + person.getMyId());

        webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		String url = getString(R.string.url_api_v1_search) + "?auth_token=" + person.getAuthToken();
		Log.d("haswants", "search string: " + url);
		webView.loadUrl(url);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_return_stream:
            	openStream();
                
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
   
    
    public void openStream(){
    	Intent mainActivity = new Intent (getApplicationContext(), MainActivity.class);     
    	mainActivity.putExtra("Person",person);
    	startActivity(mainActivity);
    }

}