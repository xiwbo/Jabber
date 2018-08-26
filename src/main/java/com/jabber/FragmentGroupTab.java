package com.jabber;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FragmentGroupTab extends Fragment
{
	View view;
	ListView listView;
	FloatingActionButton fab;
	DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("GroupChatName");
	private ArrayAdapter<String> arrayAdapter;
	private ArrayList<String> listOfRooms = new ArrayList<>();
	String groupName;
	private FirebaseAuth mAuth;
	FirebaseUser currentUser;
	Firebase firebase;

	public FragmentGroupTab() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_group_tab, container, false);
		listView = view.findViewById(R.id.groupListView);
		Firebase.setAndroidContext(getContext());
		firebase = new Firebase("https://jabber-6ac14.firebaseio.com");
		mAuth = FirebaseAuth.getInstance();
		fab = view.findViewById(R.id.addGroup);
		arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listOfRooms);
		listView.setAdapter(arrayAdapter);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addGroupName();
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				currentUser = mAuth.getCurrentUser();
				Intent intent = new Intent(getContext(), GroupChat.class);
				intent.putExtra("roomName",((TextView)view).getText().toString());
				intent.putExtra("userName", currentUser.getEmail());
				startActivity(intent);
			}
		});
		return(view);
	}

	private void addGroupName() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("Enter group name:");
		final EditText textField = new EditText(getContext());
		builder.setView(textField);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				groupName = textField.getText().toString();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(groupName,"");
				root.updateChildren(map);
				root.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						Set<String> set = new HashSet<String>();
						Iterator i = dataSnapshot.getChildren().iterator();
						while(i.hasNext()) {
							set.add(((DataSnapshot)i.next()).getKey());
						}
						listOfRooms.clear();
						listOfRooms.addAll(set);
					}
					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
					}
				});
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.cancel();
			}
		});
		builder.show();
	}
}
