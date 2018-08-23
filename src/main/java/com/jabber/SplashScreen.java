package com.jabber;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

public class SplashScreen extends Activity
{
	private static int splashScreenTime = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
				startActivity(intent);
				SplashScreen.this.finish();
			}
		}, splashScreenTime);
	}
}
