package com.w2.haswants;

import android.os.Bundle;
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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream);

        webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(getString(R.string.url_api_v1_search));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                
                return true;
            case R.id.action_profile:
                openProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
   
    public void openSearch(){
    	//startActivity(new Intent(this, Search.class));
    	
    }
    
    public void openProfile(){
    	startActivity(new Intent(this, ProfileActivity.class));
    }

}