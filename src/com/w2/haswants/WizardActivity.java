package com.w2.haswants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class WizardActivity extends Activity {
	private Person person;
	private TextView wizardPara1, wizardPara2, wizardPara3;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard1);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Lato-Bol.ttf");
        
        wizardPara1 = (TextView) findViewById(R.id.wizardPara1);
        wizardPara2 = (TextView) findViewById(R.id.wizardPara2);
        wizardPara3 = (TextView) findViewById(R.id.wizardPara3);
        
        wizardPara1.setTypeface(typeFace);
        wizardPara2.setTypeface(typeFace);
        wizardPara3.setTypeface(typeFace);

        person = (Person) getIntent().getSerializableExtra("Person");
        Log.d("haswants", "In wizard: person is: " + person.getMyId());
	}
	
	public void viewPhoto(View v){
		Intent photoActivity = new Intent (getApplicationContext(), PhotoActivity.class);     
		photoActivity.putExtra("Person",person);
    	startActivity(photoActivity);
	}

}
