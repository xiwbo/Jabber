package com.jabber;

import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterScreen extends AppCompatActivity {

	private FirebaseAuth mAuth;
	Button registerbtn;
	TextView loginLink;
	EditText email;
	EditText password;
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
		email = findViewById(R.id.navEmail);
		password = findViewById(R.id.txtPassword);
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
				user = email.getText().toString();
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
}
