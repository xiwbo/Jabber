package com.jabber;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.Firebase;

public class ForgotPassword extends AppCompatActivity
{
	private FirebaseAuth mAuth;
	Firebase firebase;
	EditText inputEmail;
	Button sendbtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		Firebase.setAndroidContext(this);
		firebase = new Firebase("https://jabber-6ac14.firebaseio.com");
		mAuth = FirebaseAuth.getInstance();
		inputEmail = findViewById(R.id.txtFEmail);
		inputEmail.addTextChangedListener(textWatcher);
		inputEmail.setOnEditorActionListener(editorActionListener);
		sendbtn = findViewById(R.id.btnSend);
		sendbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				OnClick();
			}
		});
	}

	public void OnClick() {
		String emailAddress = inputEmail.getText().toString().trim();
		mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if(task.isSuccessful()) {
					Toast.makeText(getApplicationContext(), "Your new password has been sent to your email", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(), "Email not found.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}
		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			sendbtn.setEnabled(!inputEmail.getText().toString().trim().isEmpty());
		}
		@Override
		public void afterTextChanged(Editable editable) {
		}
	};

	private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
			switch(i) {
				case EditorInfo.IME_ACTION_SEND:
					if(!inputEmail.getText().toString().trim().isEmpty()) {
						OnClick();
					}
					break;
			}
			return(false);
		}
	};
}
