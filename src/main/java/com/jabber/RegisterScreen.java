package com.jabber;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.content.Intent;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.Firebase;

public class RegisterScreen extends AppCompatActivity
{

	private FirebaseAuth mAuth;
	Button registerbtn;
	TextView loginLink;
	EditText email,username,password,confirmPassword;
	CheckBox tickbox;
	String user,pass;
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

	public void createAccount() {
		mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()) {
					// GO TO HOME SCREEN
					Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
					startActivity(intent);
					RegisterScreen.this.finish();
					Toast.makeText(getApplicationContext(),"register success!", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(),"register fail", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void OnClick() {
		if(password.getText().toString().equals(confirmPassword.getText().toString())) {
			user = email.getText().toString();
			pass = password.getText().toString();
			createAccount();
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
			registerbtn.setEnabled(!email.getText().toString().trim().isEmpty() && !username.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty() && !confirmPassword.getText().toString().trim().isEmpty() && tickbox.isChecked());
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
