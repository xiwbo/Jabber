package com.jabber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class PrivacyPolicy extends AppCompatActivity {

	WebView webView;
	Button btnOkay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_terms_of_use);
		webView = findViewById(R.id.policy_webView);
		btnOkay = findViewById(R.id.btnOkay);
		webView.loadUrl("https://drive.google.com/file/d/1i6Qg6Oubcd8cteO9uSPSBhan6r1F_JMh/view");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		btnOkay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), RegisterScreen.class);
				startActivity(intent);
				finish();
			}
		});
	}
}