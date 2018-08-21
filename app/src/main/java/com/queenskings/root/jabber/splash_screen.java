package com.queenskings.root.jabber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_screen extends AppCompatActivity {

	private static int splashScreenTime = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
			    Intent intent = new Intent(getApplicationContext(), login_screen.class);
			    startActivity(intent);
			}
		}, splashScreenTime);

	}

}
