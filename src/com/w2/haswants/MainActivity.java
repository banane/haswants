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

public class MainActivity extends Activity {
	private WebView webView;
	private Person person;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream);

        person = (Person) getIntent().getSerializableExtra("Person");
        Log.d("haswants", "in main, id: " + person.getMyId());

        webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		
		String url = getString(R.string.url_api_v1_stream) + "?auth_token=" + person.getAuthToken();
		webView.loadUrl(url);
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
                openSearch();
                return true;
            case R.id.action_profile:
                openProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
   
    public void openSearch(){
    	Intent searchActivity = new Intent (getApplicationContext(), SearchActivity.class);     
    	searchActivity.putExtra("Person",person);
    	startActivity(searchActivity);
    	
    }
    
    public void openProfile(){
    	
    	Intent profileActivity = new Intent (getApplicationContext(), ProfileActivity.class);     
    	profileActivity.putExtra("Person",person);
    	startActivity(profileActivity);
    	
    }

}