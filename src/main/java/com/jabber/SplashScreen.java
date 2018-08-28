package com.jabber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent loginIntent = new Intent(getApplicationContext(), LoginScreen.class);
				startActivity(loginIntent);
				finish();
			}
		}, 2000);
	}
}
