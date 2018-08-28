package com.jabber;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.client.Firebase;

public class LoginScreen extends Activity
{
	private FirebaseAuth mAuth;
	Dialog myDialog;
	Button loginbtn;
	EditText username;
	EditText password;
	String user, pass;
	Firebase firebase;
	TextView registerLink, forgotPass;
	FirebaseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myDialog = new Dialog(this);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_screen);
		Firebase.setAndroidContext(this);
		firebase = new Firebase("https://jabber-6ac14.firebaseio.com");
		mAuth = FirebaseAuth.getInstance();
		loginbtn = findViewById(R.id.btnLogin);
		username = findViewById(R.id.txtUsername);
		password = findViewById(R.id.txtPassword);
		forgotPass = findViewById(R.id.linkForgotPw);
		loginbtn.setEnabled(false);
		username.addTextChangedListener(textWatcher);
		password.addTextChangedListener(textWatcher);
		password.setOnEditorActionListener(editorActionListener);
		loginbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String LoginUsername = username.getText().toString();
				if(isOnline()){
					if(!LoginUsername.contains("@") || !LoginUsername.contains(".com")) {
						PopupDialog popup = new PopupDialog(myDialog, "Incorrect username or email and/or password.", "red", "OK");
						popup.showPopup();
					}
					else {
						signIn();
					}
				}
				else {
					PopupDialog popup = new PopupDialog(myDialog, "Please check your internet connection.", "red", "OK");
					popup.showPopup();
				}
			}
		});
		forgotPass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
				startActivity(intent);
			}
		});
		registerLink = findViewById(R.id.linkRegister);
		registerLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), RegisterScreen.class);
				startActivity(intent);
				LoginScreen.this.finish();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		//Check user if signed in already
		currentUser = mAuth.getCurrentUser();
		if(currentUser != null) {
			System.out.println("UID: " + currentUser.getUid());
			HomeMenu();
		}
	}

	public void signIn() {
		user = username.getText().toString();
		pass = password.getText().toString();
		mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()) {
					//Sign in success
					HomeMenu();
				}
			}
		});
	}

	public void HomeMenu() {
		currentUser = mAuth.getCurrentUser();
		Intent intent = new Intent(getApplicationContext(), HomeMenu.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		Toast.makeText(getApplicationContext(),"Welcome! " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
		startActivity(intent);
		LoginScreen.this.finish();
	}

	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}
		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			loginbtn.setEnabled(!username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty());
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
					if(!username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()) {
						signIn();
					}
					break;
			}
			return(false);
		}
	};

	//Check if user is connected to web
	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
			return true;
		}
		else {
			return false;
		}
	}
}
