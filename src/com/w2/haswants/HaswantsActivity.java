package com.w2.haswants;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;


public class HaswantsActivity extends Activity {
    /** Called when the activity is first created. */
	String[] listContent = {"Austin", "Bogota", "Mexico City", "New York", "Palo Alto"};
	ListView myList;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        myList = (ListView)findViewById(R.id.myList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.my_row,listContent);
      
        myList.setAdapter(adapter);
    }
  
}