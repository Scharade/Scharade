package com.example.scharade_andiestirnundlos_raten;


/*
 * Anker
 * To DO - Überspringen und Richtig sowie falsch einbauen
 * Farben ändern bei Aktion
 * Passiert alles im Thread
 * Bei Backbutton Thread killen
 * 
 */


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Question extends Activity implements SensorEventListener {

	
	Sensor accelerometer;
    SensorManager sm;
    MediaPlayer mediaPlayer;
	Handler mHandler = new Handler();
	
	private TextView showTime;
	private TextView questions;
	private Button back;


	
	private int onlyOneTime;
	
	//Zeit bis die Fragen starten
	private int timeToQuestionBegin=5;
	
	//Zeit fuer die Fragen
	private int timeForQuestions = 5;
	
	private boolean runTimeToQuestion = true;
	
	private boolean finish = false;
	
	boolean readyForQuestions = false; 
	 
	private boolean onlyonce = true;
	
	private int count = 0;
	
	
	private TimeForQuestions timeQuestions;
	private Thread timeQuestionsThread;

	private TimeToQuestionBegin timeQuestionBegin;
	private Thread timeToQuestionBeginThread;
	
	private QuestionsAbfrage questionAbfrageInstanz;
	private Thread questionAbfrageThread;
	
	
	
	String cat;
	
	boolean frageNurEinmalBearbeiten = true;
	boolean newQuestion = false;
	 
	//MediaPlayer für Spielbeginn 5,4,3
    MediaPlayer mp;
    MediaPlayer rightFlag;
    MediaPlayer wrongFlag;
    MediaPlayer finishSound;
    
    boolean load = false;
   // SoundPool soundPool;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		//Nur Landscape möglich
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
		
	  
		
		
		System.out.println("ZWEIMAL");
		
		initialisiereThread();
		
		//zeigt die Zeit an
		showTime = (TextView)findViewById(R.id.showTime);
		
		
	    initialisiereBeschleunigungssensor();
		
		
		
			
		//zeigt die Frage an
		questions = (TextView)findViewById(R.id.questions);
	
//		//Back Button zum Menu
		back = (Button)findViewById(R.id.back);

		
		//Reicht die Kategorie bis zum Ende durch
		Bundle zielkorb = getIntent().getExtras();
		cat = zielkorb.getString("Kategorie");
		
		
		back.setOnClickListener(new View.OnClickListener()
		{
		    	@Override
		    	public void onClick(View v) {   		
		    		onPause();
		    		
		    		/*
		    		 * Funktioniert noch nicht: das false greift nicht, evtl. einfach auch einen Thread nehmen wie unten oder versuchen runnable zu unterbrechen
		    		 * 
		    		 */
//		    		runTimeToQuestion = false;
//		    		System.out.println("WURDE AUF GESETZT" + runTimeToQuestion);
//		    		timeToQuestionBegin = 5;
		    		mp.stop(); 
		    		
					Intent in = new Intent(Question.this,MainActivity.class);
					startActivity(in);
		    		}
		});
		
	}
	
	private void initialisiereBeschleunigungssensor() {
		
		//Beschleunigungssensor wird initalisiert um Fragen zu überspringen oder weiter zu machen
				sm = (SensorManager)getSystemService(SENSOR_SERVICE);
				accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	
	

	private void initialisiereThread() {
		
		//Zeit für die Frage-Thread wird instanziiert
		timeQuestionBegin = new TimeToQuestionBegin();
		timeToQuestionBeginThread = new Thread(timeQuestionBegin);
		timeToQuestionBeginThread.start();
		System.out.println("#################Er startet hier auch öfter");
		}
	
	
	//Fragen werden hier gestellt		
	
	public void startQuestion(boolean finish) {
		
		//gut dafür http://wuselsnej.wordpress.com/coding/android/android-beispiel-fur-die-verwendung-von-threads/
     	//TimeForQuestions
     
		questionAbfrageInstanz = new QuestionsAbfrage();
		questionAbfrageThread = new Thread(questionAbfrageInstanz);
		
		 timeQuestions = new TimeForQuestions();
	     timeQuestionsThread = new Thread(timeQuestions);
	     
		 //Zeit für die Fragen wird gestartet - gibt es gar nicht mehr
		// timeForQuestions();
     	
		 if(!finish) {
			 //finish = true;
			 
			 timeQuestions.setResult(20);
			 timeQuestions.setRun(true);
			 timeQuestions.setPause(false);
			 
		    
		     System.out.println("THREAD WIRD ÖFTER GESTARTET");

			 readyForQuestions = true;
			 //Fragen stellen
			 questions.setText("Leonardo di Caprio");
			 timeQuestionsThread.start();
			 
			 
			 System.out.println("Starte RICHTIG FALSCH TRHEAD");
			//FrageThread, der anzeigt ob Richtig oder Übersprungen wird instanziiert, ist aber nicht aktiv da run = false;
			questionAbfrageInstanz = new QuestionsAbfrage();
			questionAbfrageThread = new Thread(questionAbfrageInstanz);
//			
//			questionAbfrageInstanz.setRun(false);
//			questionAbfrageInstanz.setRight(true);
			
			
			questionAbfrageThread.start();
			 
		 } else {
			 
			 System.out.println("STOP");
			 timeQuestionsThread.interrupt();
			 readyForQuestions = false;
			 
			 Bundle schickeKategorieWeiter = new Bundle();
			 schickeKategorieWeiter.putString("Kategorie", cat);
			
    		 Intent in = new Intent(Question.this,End.class);
			 in.putExtras(schickeKategorieWeiter);
			 startActivity(in);
			 
			
		 }
		
     }
	 
  
	 
	 private class TimeToQuestionBegin implements Runnable {
		 
	        private int beginTime = 5;
	        private boolean pause = false;
	    
	 
	        @Override
	        public void run() {
	           
	        	mp = MediaPlayer.create(getApplicationContext(), R.raw.race);  
	        	pause = false;
	            beginTime = 5;
	       
	            mp.start();
	            while (true) {


	            /*
	             * Evtl. mit dem SOund gucken, mal schauen was passiert, wenn ich den direkt hier immer neu initalisiere
	             * 
	             */
	            	
	            	
	            	
	               //hier Musik asyncrhon
	              
	            	
	                if(beginTime == 0) {
	                	
	                	 mHandler.post(new Runnable() {
	 	                    @Override
	 	                    public void run() {
	 	                    	
	 	                     mp.stop();
	 	                     startQuestion(false);
	 	                     System.out.println("ENDE");
	 	                    }
	 	                });
	                	
	                	break;
	                }
	                
	                mHandler.post(new Runnable() {
	                    @Override
	                    public void run() {
	                        showTime.setText("" + beginTime);
	                    }
	                });
	                
	                try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                }
	            
	            	
	                
	                beginTime--;
	               
	            }
	        }
	 
	    }
	 
	
	 
	 
	 


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	
    
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		
		//frageNurEinmalBearbeiten verhindert, dass man das Handy gekippt lassen kann und so immer Fragen weiterspringt, das Handy muss immer in die Ausgangsposition
		
		
		//Testwerte
		int[] myIntArray = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		
		
		//readyForQuestions ist notwendig, damit der Beschleunigungssensor erst aktiv ist, sobald der Timer von 5 Sekunden runtergelaufen ist
		if(readyForQuestions) {

			if(newQuestion) {

				questions.setText("" + myIntArray[count]);
				
				newQuestion = false;
			}
			
			
			//Wenn richtig, dann nach vorne
			if(event.values[2] < -3 && frageNurEinmalBearbeiten) { //Evtl. noch kalibrierbar
			    
				
				//Vielleicht hier eine Methode von schreiben CHECKMethode
				if(frageNurEinmalBearbeiten) {
			    	 count = count + 1;
					
			    	 questionAbfrageInstanz.setRun(true);
			    	 questionAbfrageInstanz.setRight(true);
			    
			    //Alter Code, der gelöscht werden kann	 
			    //	 timeQuestions.setRun(false);
			    //	 timeQuestions.setRight(true);
			    	 
					 frageNurEinmalBearbeiten = false;
					 } 
			    	 
			}
			
			
			//Verhindert, dass das Handy gekippt gehalten werden kann
			if(event.values[2] > -3 && event.values[2] < 5) {
				 frageNurEinmalBearbeiten = true;
			}
			
			
			//Wenn Handy nach hinten gekippt wird
			if(event.values[2] > 5) { 
				
				//HIer die CHECKMethode einfügen und false/true anpassen
				 if(frageNurEinmalBearbeiten) {
				
				 count = count + 1;
				 
				 questionAbfrageInstanz.setRun(true);
		    	 questionAbfrageInstanz.setRight(false);
			   
		    	//Alter Code, der gelöscht werden kann
		       // timeQuestions.setRun(false);
		       // timeQuestions.setRight(false);	
				 frageNurEinmalBearbeiten = false;
				 }
				 
			} 
			
		}
				
	}
	
	@Override
	  protected void onPause() {
	    // unregister listener
	    super.onPause();
	    sm.unregisterListener(this);
	  }
	
	
	/*
	 * QuestionsAbfrage zeigt nur an ob der Begriff richtig war oder übersprungen wurde.
	 * Wenn er richtig ist, stoppt der Timer für eine Sekunde
	 * Wenn man überspringt, läuft der Timer weiter	   
	 */
    private class QuestionsAbfrage implements Runnable {
    	
    	  private boolean run;
    	  private boolean right;
    	  
       	  RelativeLayout lLayout = (RelativeLayout) findViewById(R.id.relativeLa);
    	  
    	 @Override
	        public void run() {
    		 
    		 rightFlag = MediaPlayer.create(getApplicationContext(), R.raw.soundright); 
    		 wrongFlag = MediaPlayer.create(getApplicationContext(), R.raw.wrong); 
    		 
    		 run = false;
    		 //right = true;
    		
    		 
    		 while (true) {
	                
    			//Beenden des Threads
    			 if(timeQuestions.getResult() == 0) {
    				 System.out.println("Beendet!!!");
    				 break;
    			 }
    			
    			 if(run) {
    				
	    			 if(right) {
	    				
	    				 mHandler.post(new Runnable() {
			                	
			                	public void run() {
			                		rightFlag.start();
			                		questions.setText("Richtig - Gut!"); 
			                		
			                		
			                		//Grün
			                		lLayout.setBackgroundColor(Color.parseColor("#458B00"));
			                		
			                	}
			                
	    				 });
	    				 
	    				 timeQuestions.setRun(false);
	    				 
	    				 try {
	  	                	Thread.sleep(1000);
	  	                 } catch (InterruptedException e) {
	  	                 }
	    				 
	    				 mHandler.post(new Runnable() {
	    					 public void run() {
	    				 
	    						 //normal hellblau
	    						 lLayout.setBackgroundColor(Color.parseColor("#1E90FF"));
	    					 }
	    				 });
	    				 
	    				 run = false;
	    				 newQuestion = true;
	    				 
	    			 } else {
	    				 
	    				 //Hier Hintergrund ändern + Sound einfügen
	    				 mHandler.post(new Runnable() {
			                	
			                	public void run() {
			                		wrongFlag.start();
			                		questions.setText("Überspringen!"); 
			                		
			                		//rot
			                		lLayout.setBackgroundColor(Color.parseColor("#EE3B3B"));
			 	    				
			                		
			                	}
	 				 });
	    				 
	    				 try {
	   	                	Thread.sleep(1000);
	   	                } catch (InterruptedException e) {
	   	                }
	    				 
	    				 mHandler.post(new Runnable() {
	    					 public void run() {
	    				 
	    						 //normal hellblau
	    						 lLayout.setBackgroundColor(Color.parseColor("#1E90FF"));
	    					 }
	    				 });
	    				 
	    				run = false; 
	     				newQuestion = true;
	    			 } 
    			 
    			 }
	    	 }
	    }
    	 
    	  
	   public void setRun(boolean run) {
				this.run = run; 
			}
	   
       public void setRight(boolean right) {
			this.right = right; 		
		}
    }
	
	//Timer - Zeit die runterläuft
	 private class TimeForQuestions implements Runnable {
		 
	        private int result;
	        private boolean pause;
	        private boolean run;
	        private boolean right;
	 
	        @Override
	        public void run() {
	        	        	
//	            pause = false;
//	            run = true;
	            result = 40;
//	            right = false;
	            
	        	finishSound = MediaPlayer.create(getApplicationContext(), R.raw.beep); 
	        	 
	            while (true) {
	                
	            	if (run) {
	 	
	                mHandler.post(new Runnable() {
	                    @Override
	                    public void run() {
	                    	showTime.setText("" + result);  
	                    }
	                });
	                
	                try {
	                	Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                }
	                
	                if(result <= 6 && result >= 2) {
	                	finishSound.start();
	                }
	               
	                
	                //Wenn result 1 erreicht wurde, beende das Spiel
	                if(result == 1) {
	                	finish = true;
	                	mHandler.post(new Runnable() {
	                	
	                	public void run() {
	                			System.out.println("ER GEHT HIER EIN!!!!");
	                			setRun(false);
			                	startQuestion(true);    
	                	}
	                });
	                }
	                
	                //Zählt Zeit runter
	                result = getResult() - 1;
	                
	               //Beendet Dauerschleife im Thread
	                if(result < 0) {
	                System.out.println("RESULT " + result);
	                break;
	                }
	                
	                
	            	} else {
	            		//Geht ins Else, wenn die Frage Richtig beantwortet wurde, pausiert den Timer als "Belohnung"
	            		try {
							Thread.sleep(1000);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		run = true;
	            	}	
	            }
	            
	            
	        }
	        
	        
	        public void setRun(boolean run) {
				this.run = run; 
				
			}
	        
//	        public boolean getRun() {
//	        	return this.run;
//	        }
	        
	        public void setResult(int result) {
				this.result = result; 
				
			}
	        
	        public int getResult() {
	        	return this.result;
	        }

//			public void setRight(boolean right) {
//	        	this.right = right;
//				// TODO Auto-generated method stub
//			}

	        public void setPause(boolean pause) {
	            this.pause = pause;
	        }   
	    }	
}


/*
 * TimeForQuestions Auslagerung
 *  else  {
	            	
	            	
	            		
	            		mHandler.post(new Runnable() {
		                	
		                	public void run() {
		                		
		                		if(right) {
		                		questions.setText("Richtig - Gut!");  
		                		} else {
		                		questions.setText("Überspringen!");  	
		                		}
		                			
		                	}
	            		});
	            		
	            		
	            		 try {
	            			 
            				//Wenn richtig, dann wird der Spieler belohnt und die Zeit geht nicht runter
	            			 if(right) {
	                            Thread.sleep(1000);
	            			 } else {
                				            				 
	            				Thread.sleep(1000);
	            				result--;
	            			 }

	            			 //Setzt Counter wieder in Gang
	                         run = true;
	                            
	                         //Schaltet neue Frage frei
	                         newQuestion = true;
	                            
	                            
	                        } catch (InterruptedException e) {
	                        }
	            		 
	            	}*/
