package com.health.assist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ServiceActivity extends Service {

	private Timer timer = new Timer();
	Handler h;
	private boolean flag = true;
	private StringBuilder sb = new StringBuilder();


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Notification notification = new Notification(R.drawable.ic_launcher,
				"Background process started", System.currentTimeMillis());
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, MainScreen.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, "Health Assist","Touch to launch the app", contentIntent);
		startForeground(1, notification);
		timer.scheduleAtFixedRate(new mainTask(), 0, 10000);
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (null);
	}

	private class mainTask extends TimerTask {
		public void run() {
			toastHandler.sendEmptyMessage(0);
		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler toastHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {			
				byte[] readBuf = (byte[]) msg.obj;
				if(BluetoothAdapter.getDefaultAdapter().isEnabled()){
					if(flag){
						flag=false;
						Blue.sendData("a");
						readBuf=Blue.readData();
					}else{
						flag=true;
						Blue.sendData("b");
						readBuf=Blue.readData();
					}	
				}
				String strIncom = new String(readBuf, 0, readBuf.length);
				sb.append(strIncom);
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

		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(timer!=null)timer.cancel();
		Toast.makeText(this, "Background Process Stopped ...", Toast.LENGTH_SHORT).show();
		stopForeground(true);
	}

}