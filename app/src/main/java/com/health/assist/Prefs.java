package com.health.assist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.WindowManager;


public class Prefs extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.putBoolean("HaveShownPrefs", true);
		ed.commit();
		addPreferencesFromResource(R.xml.prefs);
		setNumberTypes();
		setPreviousValues();
 }
	
    private void setPreviousValues() {
		// TODO Auto-generated method stub
    	String val = "";
    	EditTextPreference etp;
    	SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	val= getPrefs.getString("name", "Enter your name");
		etp = (EditTextPreference) findPreference("name");
		etp.setSummary(val);
    	val= getPrefs.getString("number1", "Enter the number of the 1st person");
		etp = (EditTextPreference) findPreference("number1");
		etp.setSummary(val);
    	val= getPrefs.getString("number2", "Enter the number of the 2nd person");
		etp = (EditTextPreference) findPreference("number2");
		etp.setSummary(val);
    	val= getPrefs.getString("number3", "Enter the number of the 3rd person");
		etp = (EditTextPreference) findPreference("number3");
		etp.setSummary(val);
    	val= getPrefs.getString("number4", "Enter the number of the 4th person");
		etp = (EditTextPreference) findPreference("number4");
		etp.setSummary(val);
    	val= getPrefs.getString("number5", "Enter the number of the 5th person");
		etp = (EditTextPreference) findPreference("number5");
		etp.setSummary(val);

	}

	private void setNumberTypes() {
		// TODO Auto-generated method stub
    	EditTextPreference etp = (EditTextPreference) findPreference("number1");
		etp.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
		etp.getEditText().setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
		etp.getEditText().setKeyListener(DigitsKeyListener.getInstance());
		etp = (EditTextPreference) findPreference("number2");
		etp.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
		etp.getEditText().setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
		etp.getEditText().setKeyListener(DigitsKeyListener.getInstance());
		etp = (EditTextPreference) findPreference("number3");
		etp.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
		etp.getEditText().setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
		etp.getEditText().setKeyListener(DigitsKeyListener.getInstance());
		etp = (EditTextPreference) findPreference("number4");
		etp.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
		etp.getEditText().setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
		etp.getEditText().setKeyListener(DigitsKeyListener.getInstance());
		etp = (EditTextPreference) findPreference("number5");
		etp.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
		etp.getEditText().setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
		etp.getEditText().setKeyListener(DigitsKeyListener.getInstance());		
	}
    

	@Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	String val = "";
    	EditTextPreference etp;
    	if(key.equals("name")){
    		val=sharedPreferences.getString(key, "Enter your name");
    		etp = (EditTextPreference) findPreference(key);
    		etp.setSummary(val);
    	}else if(key.equals("number1")){
    		val=sharedPreferences.getString(key, "Enter the number of the 1st person");
    		etp = (EditTextPreference) findPreference(key);
    		etp.setSummary(val);    		
    	}else if(key.equals("number2")){
    		val=sharedPreferences.getString(key, "Enter the number of the 2nd person");
    		etp = (EditTextPreference) findPreference(key);
    		etp.setSummary(val);    		
    	}else if(key.equals("number3")){
    		val=sharedPreferences.getString(key, "Enter the number of the 3rd person");
    		etp = (EditTextPreference) findPreference(key);
    		etp.setSummary(val);    		
    	}else if(key.equals("number4")){
    		val=sharedPreferences.getString(key, "Enter the number of the 4th person");
    		etp = (EditTextPreference) findPreference(key);
    		etp.setSummary(val);    		
    	}else if(key.equals("number5")){
    		val=sharedPreferences.getString(key, "Enter the number of the 5th person");
    		etp = (EditTextPreference) findPreference(key);
    		etp.setSummary(val);    		
    	}
    }

}