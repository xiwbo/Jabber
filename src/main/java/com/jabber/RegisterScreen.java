package com.jabber;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterScreen extends AppCompatActivity
{
	private Dialog myDialog;
	private FirebaseAuth mAuth;
	private Firebase firebase;
	private FirebaseUser firebaseUser;
	private DatabaseReference databaseReference;
	private DatabaseReference mUserDatabase;
	private Button registerbtn;
	private TextView loginLink;
	private EditText email,username,password,confirmPassword;
	private CheckBox tickbox;
	private String user, mail, pass, userId;
	private TextView termsOfUse, privacyPolicy;
	private Intent loginIntent, termsOfUseIntent, policyIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myDialog = new Dialog(this);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_screen);
		Firebase.setAndroidContext(this);
		firebase = new Firebase(getResources().getString(R.string.firebaseDomain));
		mAuth = FirebaseAuth.getInstance();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		registerbtn = findViewById(R.id.btnRegister);
		email = findViewById(R.id.txtFEmail);
		username = findViewById(R.id.txtUsername);
		password = findViewById(R.id.txtPassword);
		confirmPassword = findViewById(R.id.txtConfirmPw);
		tickbox = findViewById(R.id.tickbox);
		termsOfUse = findViewById(R.id.linkTermsOfUse);
		privacyPolicy = findViewById(R.id.linkPrivacyPolicy);
		registerbtn.setEnabled(false);
		email.addTextChangedListener(textWatcher);
		username.addTextChangedListener(textWatcher);
		password.addTextChangedListener(textWatcher);
		confirmPassword.addTextChangedListener(textWatcher);
		confirmPassword.setOnEditorActionListener(editorActionListener);
		loginLink = findViewById(R.id.linkLogin);
		loginLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Failed to connect to the server. Please check your internet connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					loginIntent = new Intent(getApplicationContext(), LoginScreen.class);
					startActivity(loginIntent);
					finish();
				}
			}
		});
		registerbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!email.getText().toString().contains("@") || !email.getText().toString().contains(".com")) {
					PopupDialog popup = new PopupDialog(myDialog, "Please check your email address.", "red", "OK");
					popup.showPopup();
				}
				else if(username.length() <6 ) {
					PopupDialog popup = new PopupDialog(myDialog, "Please check username, make sure your username is atleast 6 characters, alphanumeric and has no special characters.", "red", "OK");
					popup.showPopup();
				}
				else if(password.length() < 6) {
					PopupDialog popup = new PopupDialog(myDialog, "Please check password, make sure your password is atleast 6 characters.", "red", "OK");
					popup.showPopup();
				}
				else if(!tickbox.isChecked()) {
					PopupDialog popup = new PopupDialog(myDialog, "Please check the below the box below, indicated that you have read and agree to the Terms Of Use and Privacy Policy.", "red", "OK");
					popup.showPopup();
				}
				else if(!password.getText().toString().equals(confirmPassword.getText().toString()) || !confirmPassword.getText().toString().equals(password.getText().toString())) {
					PopupDialog popup = new PopupDialog(myDialog, "Password does not match.", "red", "OK");
					popup.showPopup();
				}
				else if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Failed to connect to the server. Please check your internet connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					OnClick();
				}
			}
		});
		termsOfUse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Failed to connect to the server. Please check your connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					termsOfUseIntent = new Intent(getApplicationContext(), TermsOfUse.class);
					startActivity(termsOfUseIntent);
				}
			}
		});
		privacyPolicy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Failed to connect to the server. Please check your connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					policyIntent = new Intent(getApplicationContext(), PrivacyPolicy.class);
					startActivity(policyIntent);
				}
			}
		});
	}

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

	public void OnClick() {
		user = username.getText().toString();
		mail = email.getText().toString();
		pass = password.getText().toString();
		createAccount(user,mail,pass);
	}

	public void createAccount(final String userName, final String userEmail, final String userPassword) {
		mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				//If Successfully registered
				if(task.isSuccessful()) {
					// GO TO HOME SCREEN
					firebaseUser = mAuth.getCurrentUser();
					userId = firebaseUser.getUid();
					databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
					HashMap<String, String> hashMap = new HashMap<>();
					hashMap.put("id", userId);
					hashMap.put("profilePicture", "default");
					hashMap.put("email", userEmail);
					hashMap.put("username", userName);
					hashMap.put("password", userPassword);
					databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if(task.isSuccessful()) {
								startActivity(new Intent(getApplicationContext(), LoginScreen.class));
								finish();
							}
						}
					});
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
			registerbtn.setEnabled(!email.getText().toString().trim().isEmpty() && !username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty() && !confirmPassword.getText().toString().trim().isEmpty());
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
					if(!email.getText().toString().trim().isEmpty() && !username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty() && !confirmPassword.getText().toString().trim().isEmpty() && tickbox.isChecked()) {
						OnClick();
					}
				break;
			}
			return(false);
		}
	};
}
