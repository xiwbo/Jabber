package com.jabber;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.Firebase;

public class LoginScreen extends Activity
{
	private FirebaseAuth mAuth;
	Button loginbtn;
	EditText username;
	EditText password;
	String user,pass;
	Firebase firebase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Firebase.setAndroidContext(this);
		firebase = new Firebase("https://jabber-6ac14.firebaseio.com");
		mAuth = FirebaseAuth.getInstance();
		loginbtn = findViewById(R.id.btnLogin);
		username = findViewById(R.id.loginUsername);
		password = findViewById(R.id.loginPassword);
		loginbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				user = username.getText().toString();
				pass = password.getText().toString();
				createAccount();
			}
		});
	}

	public void createAccount() {
		mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()) {
					// GO TO HOME SCREEN
					Toast.makeText(getApplicationContext(),"Login success!", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(),"Login fails", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
