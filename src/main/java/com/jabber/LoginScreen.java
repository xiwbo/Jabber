package com.jabber;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.client.Firebase;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

public class LoginScreen extends Activity
{
	private FirebaseAuth mAuth;
	private Firebase firebase;
	private FirebaseUser currentUser;
	private Button loginbtn, fbLogin;
	private EditText username, password;
	private String user, pass;
	private TextView registerLink, forgotPass;
	private Intent HomeMenuIntent, forgotPasswordIntent, registerIntent;
	private LoginButton loginButton;
	private CallbackManager mCallbackManager = CallbackManager.Factory.create();
	private static final String TAG = "FacebookLogin";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCallbackManager = CallbackManager.Factory.create();
		setContentView(R.layout.login_screen);
		Firebase.setAndroidContext(this);
		firebase = new Firebase(getResources().getString(R.string.firebaseDomain));
		mAuth = FirebaseAuth.getInstance();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		loginbtn = findViewById(R.id.btnLogin);
		fbLogin = findViewById(R.id.btnLoginFb);
		username = findViewById(R.id.txtUsername);
		password = findViewById(R.id.txtPassword);
		forgotPass = findViewById(R.id.linkForgotPw);
		registerLink = findViewById(R.id.linkRegister);
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
					signIn();
				}
			}
		});

		fbLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				loginButton.performClick();
			}
		});
		handleFacebookLoginButton();
		forgotPass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				forgotPasswordIntent = new Intent(getApplicationContext(), ForgotPassword.class);
				startActivity(forgotPasswordIntent);
			}
		});

		registerLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				registerIntent = new Intent(getApplicationContext(), RegisterScreen.class);
				startActivity(registerIntent);
				finish();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		//Check user if signed in already
		currentUser = mAuth.getCurrentUser();
		if(currentUser != null) {
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
		HomeMenuIntent = new Intent(getApplicationContext(), HomeMenu.class);
		HomeMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		Toast.makeText(getApplicationContext(),"Welcome! " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
		startActivity(HomeMenuIntent);
		finish();
	}

	public void handleFacebookLoginButton() {
		loginButton = findViewById(R.id.login_button);
		loginButton.setReadPermissions("email", "public_profile");
		loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				Log.d(TAG, "facebook:onSuccess:" + loginResult);
				handleFacebookAccessToken(loginResult.getAccessToken());
				Toast.makeText(getApplicationContext(), "onSuccess",Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onCancel() {
				Log.d(TAG, "facebook:onCancel");
			}
			@Override
			public void onError(FacebookException error) {
				Log.d(TAG, "facebook:onError", error);
			}
		});
	}

	private void handleFacebookAccessToken(AccessToken token) {
		AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
		mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					// Sign in success, update UI with the signed-in user's information
					HomeMenu();
				}
				else {
					// If sign in fails, display a message to the user.
					Log.w(TAG, "signInWithCredential:failure", task.getException());
					Toast.makeText(getApplicationContext(), "Authentication failed.",
							Toast.LENGTH_SHORT).show();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
