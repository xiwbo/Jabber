package com.jabber;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.widget.Button;
import android.widget.LinearLayout;

public class ManageAccount extends Activity
{
	Dialog myDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout mainLayout = new LinearLayout(this);

		//Dialog for the popup
		myDialog = new Dialog(this);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Register
		Button btnRegister = new Button(this);
		btnRegister.setText("Register Screen");
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Register screen", Toast.LENGTH_SHORT).show();
			}
		});

		//Camera
		Button btnCamera = new Button(this);
		btnCamera.setText("Camera Screen");
		btnCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Camera screen", Toast.LENGTH_SHORT).show();
			}
		});

		//Bio Screen
		Button btnBio = new Button(this);
		btnBio.setText("Bio Screen");
		btnBio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "BIO screen", Toast.LENGTH_SHORT).show();
			}
		});

		//Popup Implementation
		Button btnPopup = new Button(this);
		btnPopup.setText("Popup Sample");
		btnPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "popup clicked", Toast.LENGTH_SHORT).show();
				showPopup("This is a test: sample text in popup.", "OK");
			}
		});
		mainLayout.addView(btnRegister);
		mainLayout.addView(btnCamera);
		mainLayout.addView(btnPopup);
		mainLayout.addView(btnBio);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		setContentView(mainLayout);
	}

	public void showPopup(String promptTxt, String promptBtnTxt) {
		PopupDialog popupDialog = new PopupDialog(myDialog, promptTxt, promptBtnTxt);
		popupDialog.show();
	}
}
