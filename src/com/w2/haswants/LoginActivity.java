package com.w2.haswants;


import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;


public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
    }
    
    public void loginUser(View v){
    	// validate that they are attending, then redir to main
    	
    	 Intent mainActivity = new Intent (getApplicationContext(), MainActivity.class);     
         startActivity(mainActivity);
    	
    }
    
    public void linkEvent(View v){
    	
    }

}