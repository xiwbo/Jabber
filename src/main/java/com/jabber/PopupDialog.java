package com.jabber;

import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

public class PopupDialog extends AppCompatActivity
{
	String txtMessage;
	String txtButton;
	View view;
	Dialog myDialog;

	public PopupDialog(Dialog dialog, String txtMessage, String txtButton) {
		this.view = view;
		this.txtMessage = txtMessage;
		this.txtButton = txtButton;
		this.myDialog = dialog;
	}

	public void showPopup() {
		Button popupButton;
		TextView popupText;
		myDialog.setContentView(R.layout.popup);
		popupText = (TextView)myDialog.findViewById(R.id.popupTxt);
		popupText.setText(txtMessage);
		popupButton = (Button)myDialog.findViewById(R.id.popupBtn);
		popupButton.setText(txtButton);
		popupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
	}

	public void showPromptPopup() {
		Button btnYes;
		TextView popupTextMsg;
		myDialog.setContentView(R.layout.promptpopup);
		popupTextMsg = (TextView)myDialog.findViewById(R.id.popupTxtMsg);
		popupTextMsg.setText(txtMessage);
		btnYes = (Button)myDialog.findViewById(R.id.popupBtnYes);
		btnYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseAuth.getInstance().signOut();
				Intent intent = new Intent(PopupDialog.this, LoginScreen.class);
				startActivity(intent);
				myDialog.dismiss();
			}
		});
		Button btnCancel;
		btnCancel = (Button)myDialog.findViewById(R.id.popupBtnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
	}
}
