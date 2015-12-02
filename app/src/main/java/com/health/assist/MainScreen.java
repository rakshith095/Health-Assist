package com.health.assist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainScreen extends Activity implements View.OnClickListener{

	Button curr,rec,wrec,settin,exi,emer;
	Timer t;
	Handler h;
	private boolean flag = true;
	final int RECIEVE_MESSAGE = 1;
	private StringBuilder sb = new StringBuilder();
	private ConnectedThread cT;
	SharedPreferences shared;
    String num1,num2,num3,num4,num5,personName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_screen);
		initializeEverything();
		stopService(new Intent(this, ServiceActivity.class));
	}

	private void initializeEverything() {
		// TODO Auto-generated method stub
		curr = (Button) findViewById(R.id.currButton);
		rec = (Button) findViewById(R.id.recButton);
		wrec = (Button) findViewById(R.id.wRecButton);
		emer = (Button) findViewById(R.id.emergButton);
		settin = (Button) findViewById(R.id.settingButton);
		exi = (Button) findViewById(R.id.exitButton);
		curr.setOnClickListener(this);
		rec.setOnClickListener(this);
		wrec.setOnClickListener(this);
		emer.setOnClickListener(this);
		settin.setOnClickListener(this);
		exi.setOnClickListener(this);		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.currButton:
			currentHealth();
			break;
		case R.id.recButton:
			record();
			break;
		case R.id.wRecButton:
			withoutRecord();
			break;
		case R.id.emergButton:
			shared = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			personName = shared.getString("name", "No name");
			num1 = shared.getString("number1", "DEFAULT_PHONE_NUMBER");
			num2 = shared.getString("number2", "DEFAULT_PHONE_NUMBER");
			num3 = shared.getString("number3", "DEFAULT_PHONE_NUMBER");
			num4 = shared.getString("number4", "DEFAULT_PHONE_NUMBER");
			num5 = shared.getString("number5", "DEFAULT_PHONE_NUMBER");
			String sent ="SMS_SENT";
	        PendingIntent sentPI = PendingIntent.getBroadcast(MainScreen.this, 0,new Intent(sent), 0);
	        registerReceiver(new BroadcastReceiver(){
	        	@Override
	        	public void onReceive(Context arg0, Intent arg1) {
	        		if(getResultCode() == Activity.RESULT_OK){
	        			Toast.makeText(getApplicationContext(), "Alert message sent",Toast.LENGTH_SHORT).show();
	        			}else{
	        				Toast.makeText(getApplicationContext(), "Alert message couldnt be sent",Toast.LENGTH_SHORT).show();
	        				}
	        		}}, new IntentFilter(sent));
	        SmsManager.getDefault().sendTextMessage("COUNTRY_CODE"+num1, null, "ALERT!!! "+personName+" is under serious health emergency. Reach him as early as possible", null, null);
	        SmsManager.getDefault().sendTextMessage("COUNTRY_CODE"+num2, null, "ALERT!!! "+personName+" is under serious health emergency. Reach him as early as possible", null, null);
	        SmsManager.getDefault().sendTextMessage("COUNTRY_CODE"+num3, null, "ALERT!!! "+personName+" is under serious health emergency. Reach him as early as possible", null, null);
	        SmsManager.getDefault().sendTextMessage("COUNTRY_CODE"+num4, null, "ALERT!!! "+personName+" is under serious health emergency. Reach him as early as possible", null, null);
	        SmsManager.getDefault().sendTextMessage("COUNTRY_CODE"+num5, null, "ALERT!!! "+personName+" is under serious health emergency. Reach him as early as possible", null, null);
			break;
		case R.id.settingButton:
			if(t!=null)t.cancel();
			rec.setEnabled(true);
			Intent myPrefsIntent = new Intent("com.health.assist.PREFS");
			startActivityForResult(myPrefsIntent, 1); 
			break;
		case R.id.exitButton:
			exitApp();
			break;	
		}		
	}

	private void currentHealth() {
		// TODO Auto-generated method stub
		rec.setEnabled(true);
		if(t!=null)t.cancel();
		Intent myCurrIntent = new Intent("com.health.assist.CURRENTHEALTH");
		startActivity(myCurrIntent); 		
	}

	@SuppressLint("HandlerLeak")
	private void record() {
		// TODO Auto-generated method stub
		rec.setEnabled(false);
		if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			cT = new ConnectedThread(Blue.btSocket);
			cT.start();
		}
		h = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case RECIEVE_MESSAGE:
					byte[] readBuf = (byte[]) msg.obj;
					String strIncom = new String(readBuf, 0, msg.arg1);
					sb.append(strIncom);
					int endOfLineIndex = sb.indexOf("\r\n");
					    try
					    {
					     File root = new File(Environment.getExternalStorageDirectory(), "Health Assist");
					     String FinalString;
					     if (!root.exists()) root.mkdirs();
					     File gpxfile = new File(root, "MyFile.txt");
					     BufferedWriter writer;
					     writer = new BufferedWriter(new FileWriter(gpxfile, true));
					     String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
					     if(flag) FinalString = currentDateTimeString+" "+sb+" Degree celsius ";
					     else  FinalString = currentDateTimeString+" "+sb+" pulse/min ";  
					     writer.append(FinalString);
					     writer.newLine();
					     writer.flush();
					     writer.close();
					     Toast.makeText(getApplicationContext(), "Recording..", Toast.LENGTH_SHORT).show();
					    }
					    catch(IOException e){e.printStackTrace();}					
					break;
				}
			};
		};
        t = new Timer();        
        t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
							if(BluetoothAdapter.getDefaultAdapter().isEnabled()){
								if(flag){
									flag=false;
									Blue.sendData("a");
								}else{
									flag=true;
									Blue.sendData("b");
								}	
							}
					}});
			}}, 0, 10000);        
	}

	private void withoutRecord() {
		// TODO Auto-generated method stub
		if(t!=null)t.cancel();
		rec.setEnabled(true);
    	Intent intnt=new Intent(this,ServiceActivity.class);
	    startService(intnt);			
        finish();			
	}

	private void exitApp() {
		// TODO Auto-generated method stub
		stopService(new Intent(this, ServiceActivity.class));
		if(t!=null)t.cancel();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("EXIT")
            .setMessage("Do you want to run your App in the background?")
            .setCancelable(false)
            .setIcon(R.drawable.ic_launcher)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	Toast.makeText(getApplicationContext(), "App will run in the background", Toast.LENGTH_SHORT).show();
           			Intent intnt=new Intent(MainScreen.this,ServiceActivity.class);
           			startService(intnt);			
           	        finish();			
                    }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
        			if(BluetoothAdapter.getDefaultAdapter().isEnabled()){
        			    if (Blue.outStream != null) {
        			        try {
        			          Blue.outStream.flush();
        			        } catch (IOException e) {
        			        	e.printStackTrace();
        			        }
        			      }

        			      try     {
        			        Blue.btSocket.close();
        			      } catch (IOException e2) {
        			    	  e2.printStackTrace();
        			      }				
        			}
                	MainScreen.this.finish();
                }
            });
        builder.create().show();		// create and show the alert dialog
	}

	private class ConnectedThread extends Thread {
		@SuppressWarnings("unused")
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			try {
				tmpIn = socket.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mmInStream = tmpIn;
		}

		public void run() {
			byte[] buffer = new byte[256];
			int bytes;

			while (true) {
				try {
					bytes = mmInStream.read(buffer);
					h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer)
							.sendToTarget();
				} catch (IOException e) {
					break;
				}
			}
		}

	}

}
