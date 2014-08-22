package com.travijuu.timer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class MobileConnection {
	private ConnectivityManager dataManager;
	private Method dataMtd = null;
	private boolean connectionStatus = false;
	
	public MobileConnection(Context ctx) {
        try {
        	dataManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            dataMtd.setAccessible(true);
        } 
        catch (NoSuchMethodException e) { e.printStackTrace(); }
	}
	
	public void open() {
		try {
			dataMtd.invoke(dataManager, true);
			connectionStatus = true;
			Log.i("MobileConnection", "3G Opened");
		} 
		catch (IllegalArgumentException e) { e.printStackTrace(); } 
		catch (IllegalAccessException e) { e.printStackTrace(); } 
		catch (InvocationTargetException e) { e.printStackTrace(); }
	}
	
	public void close() {
		try {
			dataMtd.invoke(dataManager, false);
			connectionStatus = false;
			Log.i("MobileConnection", "3G Closed");
		} 
		catch (IllegalArgumentException e) { e.printStackTrace(); } 
		catch (IllegalAccessException e) { e.printStackTrace();} 
		catch (InvocationTargetException e) { e.printStackTrace(); }
	}
	
	public void toggleConnection(boolean status) {
		if (status)
			open();
		else
			close();
	}
	
	public boolean isRunning() { 
		return connectionStatus;
	}
}
