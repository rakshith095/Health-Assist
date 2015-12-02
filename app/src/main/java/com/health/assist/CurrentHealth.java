package com.health.assist;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CurrentHealth extends Activity {

	TextView tv;
	Button heart,temp;
	Handler h;
	private boolean flag = false;
	final int RECIEVE_MESSAGE = 1;
	private StringBuilder sb = new StringBuilder();
	private ConnectedThread cT;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.current_health);
		tv = (TextView) findViewById(R.id.tvHeartRate);
		heart = (Button) findViewById(R.id.buttonHeart);
		temp = (Button) findViewById(R.id.buttonTemp);

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
						tv.setVisibility(View.VISIBLE);
						if(flag)
							tv.setText(sb+"   Pulse/min");
						else
							tv.setText(sb+"   Degree celsius");
					
				sb.delete(0, sb.length());						
					break;
				}
			};
		};

		heart.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				flag = true;
				// TODO Auto-generated method stub
				if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
					Blue.sendData("a");
			}
		}});
		
		temp.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				flag = false;
				// TODO Auto-generated method stub
				if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
					Blue.sendData("b");
			}
		}});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
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
