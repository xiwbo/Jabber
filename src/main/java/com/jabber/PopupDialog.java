package com.jabber;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Dialog;

public class PopupDialog extends Activity
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
	
	public void show() {
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
}
