package com.jabber;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.firebase.client.Firebase;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends Activity
{
	private FirebaseAuth mAuth;
	private Dialog myDialog;
	private Firebase firebase;
	private FirebaseUser currentUser;
	private Button loginbtn, fbLogin;
	private EditText username, password;
	private String user, pass;
	private TextView registerLink, forgotPass;
	private LoginButton loginButton;
	private CallbackManager mCallbackManager = CallbackManager.Factory.create();
	private static final String TAG = "FacebookLogin";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myDialog = new Dialog(this);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

		fbLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Please check your connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					loginButton.performClick();
				}
			}
		});
		handleFacebookLoginButton();
		forgotPass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Please check your connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
				}
			}
		});

		registerLink = findViewById(R.id.linkRegister);
		registerLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Please check your connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					startActivity(new Intent(getApplicationContext(), RegisterScreen.class));
					finish();
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		userIsLoggedIn();
	}

	private void userIsLoggedIn() {
		currentUser = mAuth.getCurrentUser();
		if(currentUser != null) {
			HomeMenu();
			return;
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
		Toast.makeText(getApplicationContext(),"Welcome! " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
		startActivity(new Intent(getApplicationContext(), HomeMenu.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
				Toast.makeText(getApplicationContext(), "Login Success",Toast.LENGTH_SHORT).show();
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
					PopupDialog popup = new PopupDialog(myDialog, "Authentication failed.", "red", "OK");
					popup.showPopup();
					Log.w(TAG, "signInWithCredential:failure", task.getException());
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
				case EditorInfo.IME_ACTION_GO:
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
			return(true);
		}
		else {
			return(false);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
