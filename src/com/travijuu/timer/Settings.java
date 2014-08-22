package com.travijuu.timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	private int tibase = 1000 * 60 * 5; // 5 minutes.
	private int cdbase = 1000 * 15; // 15 seconds.
	public static int timeInterval;
	public static int connectionDuration;
	
	public Settings(Context ctx) {
		preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
		editor = preferences.edit();
		setTimeInterval(getInt("TimeInterval"));
		setConnectionDuration(getInt("ConnectionDuration"));
	}
	
	public void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}
	
	public int getInt(String key){
		return preferences.getInt(key, 1);
	}
	
	public void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}
	
	public String getString(String key) {
		return preferences.getString(key, "-");
	}
	
	public void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public Boolean getBoolean(String key) {
		return preferences.getBoolean(key, false);
	}
	
	public void setTimeInterval(int value) {
		timeInterval = value * tibase;
		setInt("TimeInterval", value);
	}
	
	public void setConnectionDuration(int value) {
		connectionDuration = value * cdbase;
		setInt("ConnectionDuration", value);
	}
	
	public int getTimeIntervalInMinutes() {
		return tibase / (1000 * 60) ;
	}
	
	public int getConnectionDurationInSeconds() {
		return cdbase / 1000;
	}
}
