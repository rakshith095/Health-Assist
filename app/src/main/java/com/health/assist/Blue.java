package com.health.assist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Blue extends Activity {


	private static final int REQUEST_ENABLE_BT = 1;
	static BluetoothAdapter btAdapter;
    static BluetoothSocket btSocket = null;
	static OutputStream outStream = null;
	static InputStream inStream = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static String deviceAddress = "20:13:01:24:02:98";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(btAdapter == null){
        	Toast.makeText(getApplicationContext(), "No bluetooth detected", Toast.LENGTH_LONG).show();
        	finish();
        }else{
        	if(! btAdapter.isEnabled()){
        		turnOnBluetooth();
        	}else{
        		connectToDevice();
        	}
        }
	}
	
	private void turnOnBluetooth() {
		// TODO Auto-generated method stub
	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();
			connectToDevice();
		}
		else if(resultCode == RESULT_CANCELED){
			Toast.makeText(getApplicationContext(), "Bluetooth must be turned on to use the App", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void connectToDevice() {
		// TODO Auto-generated method stub
		if(btAdapter.isDiscovering()){
			btAdapter.cancelDiscovery();
		}
		
	    BluetoothDevice device = btAdapter.getRemoteDevice(deviceAddress);
	    
	    try {
	      btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	  
	    try {
	      btSocket.connect();
			Toast.makeText(getApplicationContext(), "Connection established", Toast.LENGTH_SHORT).show();
	    } catch (IOException e) {
	      try {
	        btSocket.close();
	      } catch (IOException e2) {
	  		Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT).show();
	      }
	    }
	    try {
		      outStream = btSocket.getOutputStream();
		    } catch (IOException e) {
				Toast.makeText(getApplicationContext(), "outputstream creation failed", Toast.LENGTH_SHORT).show();
		    }
        try {
			inStream = btSocket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent myIntent = new Intent("com.health.assist.MAINSCREEN");
		startActivity(myIntent);
	    finish();
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();		
	}

	public static byte[] readData() {
		// TODO Auto-generated method stub
		int bytes = 0;
        byte[] buffer = new byte[4];
        while(true){
            try {
                // Read from the InputStream
                bytes = inStream.read(buffer); 
                return buffer;
            }catch (IOException e) {break;}}
		return buffer;
	}

	public static void sendData(String message) {
	
		byte[] msgBuffer = message.getBytes();
		    try {
		      outStream.write(msgBuffer);
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		  }	

}
