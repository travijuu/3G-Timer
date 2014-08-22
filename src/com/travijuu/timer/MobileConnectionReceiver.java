package com.travijuu.timer;

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
		Intent actionIntent = new Intent("com.travijuu.timer.broadcast");
		Log.i(this.getClass().getSimpleName(), "Broadcast Received: " + connectionStatus);
		
		mConnection.toggleConnection(connectionStatus);
		settings.setBoolean("Is3G", connectionStatus);
		
		if (!connectionStatus) {
			settings.setString("LastClosed", DateFormat.getDateTimeInstance().format(new Date()));
			if (settings.getBoolean("NotificationEnabled"))
				new TimerNotification(context).post();
		}
		context.sendBroadcast(actionIntent);
	}
}
