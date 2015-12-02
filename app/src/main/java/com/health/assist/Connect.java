package com.health.assist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Connect extends Activity{

	Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        boolean haveWeShownPreferences = prefs.getBoolean("HaveShownPrefs", false);
        if (!haveWeShownPreferences) {
            // launch the preferences activity
			Intent myPrefsIntent = new Intent("com.health.assist.PREFS");
			startActivityForResult(myPrefsIntent, 1); 
        }
            setContentView(R.layout.connect_screen);
            btn = (Button) findViewById(R.id.connectButton);
            btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent myIntent = new Intent("com.health.assist.BLUE");
					startActivity(myIntent);
					finish();
				}
			});
	}

}
