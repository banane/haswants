package com.w2.haswants;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class ProfileActivity extends Activity {
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
		   	super.onCreate(savedInstanceState);
		   	setContentView(R.layout.profile);
	   }
	
	public void postProfile(View v){
		// do nothing for now
		Log.d("haswants", "has clicked update");
		// save/process form
		
		 Intent photoActivity = new Intent (getApplicationContext(), PhotoActivity.class);     
         startActivity(photoActivity);
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
	                
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
	    public void openSearch(){
	    	
	    }
}
