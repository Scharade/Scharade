/*
 * Eine App von Michael Rothkegel
 * 
 * Sound anpassen 
 * Farben anpassen beim Shaking
 * MainDesign anpassen
 * Kernstück Fragen durch Datenbank aus Kategorie 
 * 
 * 
 */


package com.example.scharade_andiestirnundlos_raten;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	String cat = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		ImageButton imgOne = (ImageButton)findViewById(R.id.imageButton1);
		
		imgOne.setOnClickListener(new View.OnClickListener()
		{
		    	@Override
		    	public void onClick(View v) {
		    		
		    		 cat = "ImageButton1";
		    		
		    		 Bundle korb = new Bundle();
					 korb.putString("Kategorie", cat);
					
		    		
		    		
		    		Intent in = new Intent(MainActivity.this,StartGame.class);
		    		 in.putExtras(korb);
		    		startActivity(in);
		    		}
		});
		
		
	}
	
	@Override
	public void onBackPressed() {
	
    
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
