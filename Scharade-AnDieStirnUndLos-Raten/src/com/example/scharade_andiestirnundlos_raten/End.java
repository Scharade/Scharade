package com.example.scharade_andiestirnundlos_raten;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class End extends Activity {
	
	MediaPlayer endSound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
		
		endSound = MediaPlayer.create(getApplicationContext(), R.raw.chord); 
		
		endSound.start();
		//Nur Landscape möglich
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Button back = (Button)findViewById(R.id.back);
		TextView category = (TextView)findViewById(R.id.categoryName);
		
		back.setOnClickListener(new View.OnClickListener()
		{
		    	@Override
		    	public void onClick(View v) {   		
		    		onPause();
		    		
					Intent in = new Intent(End.this,MainActivity.class);
					startActivity(in);
		    		}
		});
		
		
		Bundle zielkorb = getIntent().getExtras();
		String cat = zielkorb.getString("Kategorie");
		
		
		
		category.setText(cat);
		//System.out.println("HIER ENDE KATEGORIE" + cat);
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.end, menu);
		return true;
	}
	
	@Override
	 public void onBackPressed() {
	 }
	

}
