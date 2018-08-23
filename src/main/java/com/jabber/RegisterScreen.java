package com.jabber;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
	private FirebaseAuth mAuth;
	DatabaseReference databaseReference;
	Button registerbtn;
	TextView loginLink;
	EditText email,username,password,confirmPassword;
	CheckBox tickbox;
	String user,mail,pass;
	Firebase firebase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);
		Firebase.setAndroidContext(this);
		firebase = new Firebase("https://jabber-6ac14.firebaseio.com");
		mAuth = FirebaseAuth.getInstance();
		registerbtn = findViewById(R.id.btnRegister);
		email = findViewById(R.id.txtFEmail);
		username = findViewById(R.id.txtUsername);
		password = findViewById(R.id.txtPassword);
		confirmPassword = findViewById(R.id.txtConfirmPw);
		tickbox = findViewById(R.id.tickbox);
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
				Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
				startActivity(intent);
				RegisterScreen.this.finish();
			}
		});
		registerbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!email.getText().toString().contains("@") || !email.getText().toString().contains(".com")) {
					Toast.makeText(getApplicationContext(), "Please check your email address.", Toast.LENGTH_SHORT).show();
				}
				else if(password.length() < 6) {
					Toast.makeText(getApplicationContext(), "Please check password, make sure your password is atleast 6 characters.", Toast.LENGTH_SHORT).show();
				}
				else {
					OnClick();
				}
			}
		});
	}
	public void createAccount(final String userName, final String userEmail, final String userPassword) {
		mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()) {
					// GO TO HOME SCREEN
					FirebaseUser firebaseUser = mAuth.getCurrentUser();
					String userId = firebaseUser.getUid();
					databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Email");
					HashMap<String, String> hashMap = new HashMap<>();
					hashMap.put("id", userId);
					hashMap.put("email", userEmail);
					hashMap.put("username",userName);
					hashMap.put("password",userPassword);
					databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if(task.isSuccessful()) {
								Toast.makeText(getApplicationContext(),"Register Success!", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
								RegisterScreen.this.finish();
							}
						}
					});
				}
				else {
					Toast.makeText(getApplicationContext(),"Register fail", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void OnClick() {
		if(password.getText().toString().equals(confirmPassword.getText().toString())) {
			user = username.getText().toString();
			mail = email.getText().toString();
			pass = password.getText().toString();
			createAccount(user,mail,pass);
		}
		else {
			Toast.makeText(getApplicationContext(), "Password does not match.", Toast.LENGTH_SHORT).show();
		}
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
				case EditorInfo.IME_ACTION_SEND:
					if(!email.getText().toString().trim().isEmpty() && !username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty() && !confirmPassword.getText().toString().trim().isEmpty() && tickbox.isChecked()) {
						OnClick();
					}
				break;
			}
			return(false);
		}
	};
}
