package com.travijuu.timer;

import java.text.DateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


public class TimerNotification {
	NotificationManager nManager;
	Notification notification;
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public TimerNotification(Context context) {
		nManager = (NotificationManager) context.getApplicationContext().getSystemService(context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context.getApplicationContext(),0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification = new Notification.Builder(context)
			.setContentTitle("3G Timer")
			.setContentText("Closed Notification")
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(pendingNotificationIntent)
			.setAutoCancel(true)
			.setTicker("3G Closed: " + DateFormat.getDateTimeInstance().format(new Date()))
			.build();
		
		nManager.cancel(0);		
	}
	
	public void post() {
		nManager.notify(0, notification);
	}
}
