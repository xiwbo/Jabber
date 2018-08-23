package com.jabber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
	Button loginbtn;
	EditText username;
	EditText password;
	String user,pass;
	Firebase firebase;
	TextView registerLink, forgotPass;
	FirebaseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				if(!username.getText().toString().contains("@") || !username.getText().toString().contains(".com")) {
					Toast.makeText(getApplicationContext(), "Please check your email address.", Toast.LENGTH_SHORT).show();
				}
				else {
					OnClick();
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
		// Check if user is signed in (non-null) and update UI accordingly.
		currentUser = mAuth.getCurrentUser();
		if(currentUser != null) {
			HomeMenu();
		}
	}

	public void signIn() {
		mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()) {
					// Sign in success, update UI with the signed-in user's information
					HomeMenu();
				}
				else {
					// If sign in fails, display a message to the user.
					Toast.makeText(getApplicationContext(), "Incorrect username or email and/or password.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void HomeMenu() {
		currentUser = mAuth.getCurrentUser();
		Intent intent = new Intent(getApplicationContext(), HomeMenu.class);
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

	public void OnClick() {
		user = username.getText().toString();
		pass = password.getText().toString();
		signIn();
	}

	private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
			switch(i) {
				case EditorInfo.IME_ACTION_SEND:
					if(!username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()) {
						OnClick();
					}
					break;
			}
			return(false);
		}
	};
}
