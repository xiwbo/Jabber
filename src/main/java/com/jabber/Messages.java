package com.jabber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Messages extends AppCompatActivity
{
	private TextView chatConvo;
	private DatabaseReference root;
	private ImageButton btnSend;
	private EditText txtField;
	private ScrollView scrollView;
	private String userName, tempKey, msgs, chatUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		btnSend = findViewById(R.id.msgButtonSend);
		txtField = findViewById(R.id.msgTextField);
		chatConvo = findViewById(R.id.textView4);
		scrollView = findViewById(R.id.scrollView3);
		userName = getIntent().getExtras().get("id").toString();
		setTitle(userName);
		root = FirebaseDatabase.getInstance().getReference().child("Messages").child(userName);
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Map<String, Object> map = new HashMap<String, Object>();
				tempKey = root.push().getKey();
				root.updateChildren(map);
				DatabaseReference messageRoot = root.child(tempKey);
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("Name", userName);
				map2.put("Message", txtField.getText().toString());
				messageRoot.updateChildren(map2);
				txtField.setText("");
				scrollView.fullScroll(View.FOCUS_DOWN);
			}
		});
		root.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				appendChat(dataSnapshot);
			}
			@Override
			public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				appendChat(dataSnapshot);
			}
			@Override
			public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

			}
			@Override
			public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
			}
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
	}

	private void appendChat(DataSnapshot snapshot) {
		Iterator i = snapshot.getChildren().iterator();
		while(i.hasNext()) {
			msgs = (String) ((DataSnapshot)i.next()).getValue();
			chatUsername = (String) ((DataSnapshot)i.next()).getValue();
			chatConvo.append(chatUsername + " : " + msgs + "\n");
		}
	}
}
