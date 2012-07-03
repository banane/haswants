package com.w2.haswants;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabHost;


public class StreamProfileTabActivity extends TabActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sptab);
        
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
        
        intent = new Intent().setClass(getApplicationContext(), ProfileActivity.class); 
        spec = tabHost.newTabSpec("profile").setIndicator("Profile")
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, StreamActivity.class);
        spec = tabHost.newTabSpec("stream").setIndicator("Stream")
                      .setContent(intent);
        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);
    }
    
  

}