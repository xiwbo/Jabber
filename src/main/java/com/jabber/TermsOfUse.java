package com.jabber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class TermsOfUse extends AppCompatActivity
{
	private WebView webView;
	private Button btnOkay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_terms_of_use);
		webView = findViewById(R.id.policy_webView);
		btnOkay = findViewById(R.id.btnOkay);
		webView.loadUrl(getResources().getString(R.string.termsOfUseDomain));
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		btnOkay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(), RegisterScreen.class));
				finish();
			}
		});
	}
}
