package com.jabber;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends Activity
{
	private FirebaseAuth mAuth;
	Button loginbtn;
	EditText username;
	EditText password;
	String user,pass;
	Firebase firebase;
	TextView registerLink;

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
		loginbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				user = username.getText().toString();
				pass = password.getText().toString();
				signIn();
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
		//FirebaseUser currentUser = mAuth.getCurrentUser();
		//updateUI(currentUser);
	}

	public void signIn() {
		mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()) {
					// Sign in success, update UI with the signed-in user's information
					FirebaseUser currentUser = mAuth.getCurrentUser();
					Intent intent = new Intent(getApplicationContext(), NavBar.class);
					Toast.makeText(getApplicationContext(),"Welcome" + currentUser.getEmail().toString(), Toast.LENGTH_LONG).show();
					startActivity(intent);
				}
				else {
					// If sign in fails, display a message to the user.
					Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
