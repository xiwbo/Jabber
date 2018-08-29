package com.jabber;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopupDialog extends Activity
{
	private String txtMessage, colorMessage, txtButton;
	private View view;
	private Dialog myDialog;

	public PopupDialog(Dialog dialog, String txtMessage, String colorMessage, String txtButton) {
		this.colorMessage = colorMessage;
		this.view = view;
		this.txtMessage = txtMessage;
		this.txtButton = txtButton;
		this.myDialog = dialog;
	}

	public void showPopup() {
		Button popupButton;
		TextView popupText;
		myDialog.setContentView(R.layout.popup);
		popupText = myDialog.findViewById(R.id.popupTxt);
		popupText.setText(txtMessage);
		if(colorMessage.equalsIgnoreCase("red")) {
			popupText.setTextColor(Color.rgb(200,0,0));
		}
		popupButton = myDialog.findViewById(R.id.popupBtn);
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
