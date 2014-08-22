package com.travijuu.timer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


public class TimerNotification {
	NotificationManager nManager;
	Notification notification;
	
	public TimerNotification(Context context) {
		nManager = (NotificationManager) context.getApplicationContext().getSystemService(context.getApplicationContext().NOTIFICATION_SERVICE);
		Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
		
		notification = new Notification(R.drawable.ic_launcher, "Test Messsage",System.currentTimeMillis());

		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity( context.getApplicationContext(),0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.DEFAULT_VIBRATE;
		
		notification.setLatestEventInfo(context.getApplicationContext(), "3G Timer", "3G closed at " + System.currentTimeMillis(), pendingNotificationIntent);
		notification.tickerText = "Deneme";
		
	}
	
	public void post() {
		nManager.notify(0, notification);
	}
}
