package com.example.autocontrol;

import java.text.DateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MobileConnectionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean connectionStatus = intent.getExtras().getBoolean("ConnectionStatus");
		Settings settings = new Settings(context);
		MobileConnection mConnection = new MobileConnection(context);
		Intent actionIntent = new Intent("com.example.autocontrol.OPEN");
		Log.i(this.getClass().getSimpleName(), "Broadcast Received: " + connectionStatus);
		
		mConnection.toggleConnection(connectionStatus);
		settings.setBoolean("Is3G", connectionStatus);
		
		if (!connectionStatus)
			settings.setString("LastClosed", DateFormat.getDateTimeInstance().format(new Date()));
		
		context.sendBroadcast(actionIntent);
	}
}
