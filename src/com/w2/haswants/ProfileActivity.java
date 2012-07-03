package com.w2.haswants;


import android.os.Bundle;
import android.util.Log;
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
}
