package com.jabber;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.app.Dialog;
import android.widget.Button;

public class LoginScreen extends Activity
{
	Dialog myDialog;
	Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myDialog = new Dialog(this);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopup("Please check your internet connection.", "OK");
			}
		});
	}

	public void showPopup(String promptTxt, String promptBtnTxt) {
		PopupDialog popupDialog = new PopupDialog(myDialog, promptTxt, promptBtnTxt);
		popupDialog.show();
	}
}
