package com.w2.haswants;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
	private WebView webView;
	private Person person;
	private String TAG = "CONNECTOR";
	private Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream);
        
        person = (Person) getIntent().getSerializableExtra("Person");
        Log.d(TAG, "in main, id: " + person.getMyId());
        context = this.getApplicationContext();

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		String url = Constants.BASE_URL + getString(R.string.url_api_v1_stream) + "?auth_token=" + person.getAuthToken();
		webView.loadUrl(url);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public class MyWebViewClient extends WebViewClient{
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		Log.d(TAG,"In url override");

        	    if (url.startsWith("mailto:")) {
        	    	Log.d(TAG, "in email, mailto");
        	        String[] url_values = url.split(":");
        	    	String email = url_values[1];
        	    	Intent intent = new Intent(Intent.ACTION_SEND);
        	    	intent.setType("text/plain");
        	    	String[] recipients = new String[]{email, "",};
        	    	intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        	    	intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.email_subject));
        	    	intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_footer));
        	    	startActivity(Intent.createChooser(intent, "Send contact request via")); 
        	    } else {
        	    	view.loadUrl(url);
        	    }
                return true;
           }		
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