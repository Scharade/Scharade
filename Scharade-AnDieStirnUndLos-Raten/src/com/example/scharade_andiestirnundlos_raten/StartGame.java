package com.example.scharade_andiestirnundlos_raten;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartGame extends Activity implements SensorEventListener{
	
	/*
	 * Du hast Kategorie XX gewählt per Übergabe
	 * 
	 * Hier ein Bild einbauen, dass erklärt wie es funktioniert (hebe es an deine Stirn und das Spiel startet automatisch)
	 * 
	 * Zurück-Button einbauen 
	 * 
	 * Auf Backbutton vom Androidpfeil App immer beenden
	 */
	
	Sensor accelerometer;
	SensorManager sm;
	String cat;
	
	TextView showCategory;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_game);
		
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
		Bundle zielkorb = getIntent().getExtras();
		String cat = zielkorb.getString("Kategorie");
		
		
		showCategory = (TextView)findViewById(R.id.categoryName);
		
		showCategory.setText(cat);
		
		sm = (SensorManager)getSystemService(SENSOR_SERVICE);
		accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		
		Button back = (Button)findViewById(R.id.back);
		
		back.setOnClickListener(new View.OnClickListener()
		{
		    	@Override
		    	public void onClick(View v) {
		    		
		    		
		    		onPause();
		    		
					Intent in = new Intent(StartGame.this,MainActivity.class);
					startActivity(in);
		    		}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_game, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


	public void onSensorChanged(SensorEvent event) {
	
		/*
		if(set) {

		x =  event.values[0];
		y = event.values[1];
		z =  event.values[2];
		set = false;
		}*/
		
		//System.out.println("x OLD " + x + "Y: old "+ y + " Z old : " + z);
		
		
		//System.out.println("X: "+ event.values[0] + "\nY: "+ event.values[1] + "\n Z: "+event.values[2]);
	     
	//	System.out.println("Z: "+event.values[2]);
		
		if( (event.values[2] < -1)) {

			 onPause(); //Starte Thread und pausiere Sensor, da Sensor nicht mehr benötigt wird
			 
			 cat = (String) showCategory.getText();
			 
			 Bundle schickeKategorieWeiter = new Bundle();
			 schickeKategorieWeiter.putString("Kategorie", cat);
			 
			 
		     Intent in = new Intent(StartGame.this,Question.class);
		     in.putExtras(schickeKategorieWeiter);
		     
		     startActivity(in);
		     	
		} 
		
		// TODO Auto-generated method stub
		//System.out.println("X: "+ event.values[0] + "\nY: "+ event.values[1] + "\n Z: "+event.values[2]);
		//acceleration.setText("X: "+event.values[0] + "\nY: "+ event.values[1] + "\n Z: "+event.values[2]);
		
		
	}


	//Nicht benötigt evtl auch falscher Code, findet trotzdem möglicherweise Verwendung
	 @Override
	  protected void onStop() {
	    // unregister listener
	    super.onStop();
	    sm.unregisterListener(this);
	//    Log.v("DEINE KLASSE", "onStop()");
	  }
	 
	 @Override
	  protected void onPause() {
	    // unregister listener
	    super.onPause();
	    sm.unregisterListener(this);

	 //   Log.v("DEINE KLASSE", "onPause()");
	  }
	 
	 @Override
	 public void onBackPressed() {
	 }
	 
	 
	
}
