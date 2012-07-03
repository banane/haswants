package com.w2.haswants;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;


public class HaswantsActivity extends Activity implements OnItemClickListener{
    /** Called when the activity is first created. */
	String[] listContent = {"Austin", "Bogota", "Mexico City", "New York", "Palo Alto"};
	private ListView myList;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        myList = (ListView)findViewById(R.id.myList);
        myList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , listContent));
        myList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				// carry value through to profile page, display on top textview, store in stream
				
				 switch( position )
				    {
				       case 0:  Intent profileActivity = new Intent (getApplicationContext(), ProfileActivity.class);     
				                startActivity(profileActivity);
				                break;
				      
				    }
				}
			});
    }


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}