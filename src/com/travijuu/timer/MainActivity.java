package com.travijuu.timer;


import com.travijuu.timer.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter; 
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private PendingIntent startPendingIntent, stopPendingIntent;
	private Intent startIntent, stopIntent;
	private AlarmManager aManager;
	private ToggleButton tbutton ;
	private SeekBar timeIntervalSeekBar, connectionDurationSeekBar;
	private SeekBarListener seekBarListener;
	private TextView timeIntervalText, connectionDurationText, autoControlText, connectionText, lastClosedText;
	private Settings settings;
	private Button saveButton;
	private CheckBox notificationCheckBox;
	private MyBroadcastReceiver myBroadcastReceiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		aManager = (AlarmManager) getSystemService(ALARM_SERVICE);	
		tbutton = (ToggleButton) findViewById(R.id.toggleButton);
		saveButton = (Button) findViewById(R.id.saveButton); 
		timeIntervalSeekBar = (SeekBar) findViewById(R.id.timeIntervalSeekBar);
		connectionDurationSeekBar = (SeekBar) findViewById(R.id.connectionDurationSeekBar);
		timeIntervalText = (TextView) findViewById(R.id.timeIntervalText);
		connectionDurationText = (TextView) findViewById(R.id.connectionDurationText);
		lastClosedText = ((TextView) findViewById(R.id.lastClosedText));
		autoControlText = (TextView) findViewById(R.id.autoControlText);
		connectionText = (TextView) findViewById(R.id.connectionText);
		notificationCheckBox = (CheckBox) findViewById(R.id.notificationCheckBox);
		
		settings = new Settings(getApplicationContext());
		startIntent = new Intent(MainActivity.this, MobileConnectionReceiver.class);
		startIntent.putExtra("ConnectionStatus", true);
		startPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, startIntent, 0);
		stopIntent = new Intent(MainActivity.this, MobileConnectionReceiver.class);
		stopIntent.putExtra("ConnectionStatus", false);
		stopPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, stopIntent, 1);
		myBroadcastReceiver = new MyBroadcastReceiver();
		seekBarListener = new SeekBarListener();
		
		updateMobileConnectionStatus();
		updateAppStatus();
		
		timeIntervalSeekBar.setOnSeekBarChangeListener(seekBarListener);
		timeIntervalSeekBar.setProgress(settings.getInt("TimeInterval"));
		connectionDurationSeekBar.setOnSeekBarChangeListener(seekBarListener);
		connectionDurationSeekBar.setProgress(settings.getInt("ConnectionDuration"));
		notificationCheckBox.setChecked(settings.getBoolean("NotificationEnabled"));
		notificationCheckBox.setOnCheckedChangeListener(new CheckBoxListener());

		registerReceiver(myBroadcastReceiver, new IntentFilter("com.travijuu.timer.broadcast"));
		
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(myBroadcastReceiver);
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.action_about)
			startActivity(new Intent(this, AboutActivity.class));
		
		return super.onOptionsItemSelected(item);
	}
		
	public void toggleTimerApp(View v) {
		if(tbutton.isChecked()) {
			aManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Settings.timeInterval, startPendingIntent);
			aManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + Settings.connectionDuration, Settings.timeInterval, stopPendingIntent);
			settings.setBoolean("isRunning", true);
			Log.v(this.getClass().getSimpleName(), "Mode ON");
		}
		else
		{
			aManager.cancel(startPendingIntent);
			aManager.cancel(stopPendingIntent);
			settings.setBoolean("isRunning",false);
			Log.v(this.getClass().getSimpleName(), "Mode OFF");
		}
		updateAppStatus();
	}
	
	public void saveSettings(View v) {
		settings.setTimeInterval(timeIntervalSeekBar.getProgress());
		settings.setConnectionDuration(connectionDurationSeekBar.getProgress());
		settings.setBoolean("NotificationEnabled", notificationCheckBox.isChecked());
		saveButton.setEnabled(false);
		Log.i(this.getClass().getSimpleName(), "Settings Saved");
	}
	
	public void updateMobileConnectionStatus() {
		if (settings.getBoolean("Is3G"))
		{
			connectionText.setText("ON");
			connectionText.setTextColor(Color.GREEN);
		}
		else
		{
			connectionText.setText("OFF");
			connectionText.setTextColor(Color.RED);
		}
		lastClosedText.setText(settings.getString("LastClosed"));
	}
	
	public void updateAppStatus() {
		if (settings.getBoolean("isRunning"))
		{
			autoControlText.setText("ON");
			autoControlText.setTextColor(Color.GREEN);
			tbutton.setChecked(true);
		}
		else
		{
			autoControlText.setText("OFF");
			autoControlText.setTextColor(Color.RED);
		}	
	}
	
	public void updateToast() {
		if(settings.getBoolean("Is3G"))
			Toast.makeText(getApplicationContext(), "3G Opened: " + Settings.connectionDuration / 1000 + " sec.", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getApplicationContext(), "3G Closed: " + Settings.timeInterval / (1000 * 60) + " min.", Toast.LENGTH_SHORT).show();
	}
	
	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateMobileConnectionStatus();
			updateToast();
		}
	}
	
	private class SeekBarListener implements OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			switch (seekBar.getId()) {
			case R.id.timeIntervalSeekBar:
				timeIntervalText.setText(String.valueOf(progress * settings.getTimeIntervalInMinutes()) + " min.");				
				break;
			case R.id.connectionDurationSeekBar:
				connectionDurationText.setText(String.valueOf(progress * settings.getConnectionDurationInSeconds()) + " sec.");
				break;
			default:
				break;
			}
			if(!saveButton.isEnabled() && fromUser)
				saveButton.setEnabled(true);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) { }

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) { }
	}
	
	private class CheckBoxListener implements android.widget.CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			saveButton.setEnabled(true);
		}
	}
}
