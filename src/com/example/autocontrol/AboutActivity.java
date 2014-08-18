package com.example.autocontrol;

import com.example.autocontrol.R;

import android.app.Activity;
import android.os.Bundle;

public class AboutActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		setTitle("About");
	}
}