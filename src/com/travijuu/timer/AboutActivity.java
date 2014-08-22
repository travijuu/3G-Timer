package com.travijuu.timer;

import com.travijuu.timer.R;

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
