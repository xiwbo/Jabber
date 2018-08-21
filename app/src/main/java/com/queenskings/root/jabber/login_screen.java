package com.queenskings.root.jabber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class login_screen extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
        Button loginbtn = findViewById(R.id.register_button);
        Button facebookbtn = findViewById(R.id.facebookButton);
        TextView registerTxt = findViewById(R.id.register_link);
        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), register_screen.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Login Button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainChat.class);
                startActivity(intent);
            }
        });
        facebookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Facebook Button", Toast.LENGTH_SHORT).show();
            }
        });
	}
}
