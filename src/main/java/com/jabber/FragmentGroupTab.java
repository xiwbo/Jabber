package com.jabber;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
	private View view;
	private ListView listView;
	private Dialog myDialog;
	private ImageButton imgButton;
	private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("GroupChatName");
	private ArrayAdapter<String> arrayAdapter;
	private ArrayList<String> listOfRooms = new ArrayList<>();
	private String groupName;
	private FirebaseAuth mAuth;
	private FirebaseUser currentUser;
	private Firebase firebase;

	public FragmentGroupTab() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		myDialog = new Dialog(getContext());
		view = inflater.inflate(R.layout.fragment_group_tab, container, false);
		Firebase.setAndroidContext(getContext());
		firebase = new Firebase(getResources().getString(R.string.firebaseDomain));
		mAuth = FirebaseAuth.getInstance();
		listView = view.findViewById(R.id.groupListView);
		imgButton = view.findViewById(R.id.addGroup);
		arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listOfRooms);
		listView.setAdapter(arrayAdapter);
		DisplayGroupName();
		imgButton.setOnClickListener(new View.OnClickListener() {
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
				intent.putExtra("roomName", ((TextView)view).getText().toString());
				intent.putExtra("userName", currentUser.getEmail());
				startActivity(intent);
			}
		});
		return(view);
	}

	private void addGroupName() {
		Button btnCreateGroup;
		myDialog.setContentView(R.layout.popupaddgroup);
		btnCreateGroup = myDialog.findViewById(R.id.popupBtnCreate);
		btnCreateGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText newGroup = myDialog.findViewById(R.id.popupTxtGroupName);
				groupName = newGroup.getText().toString();
				if(groupName.length() > 0) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(groupName,"");
					root.updateChildren(map);
					Toast.makeText(getContext(), "Group added", Toast.LENGTH_SHORT).show();
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
					myDialog.dismiss();
				}
				else {
					Toast.makeText(getContext(), "Enter your group name", Toast.LENGTH_SHORT).show();
				}
			}
		});
		Button btnCancel;
		btnCancel = (Button)myDialog.findViewById(R.id.popupBtnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
	}

	private void DisplayGroupName() {
		root.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Iterator i = dataSnapshot.getChildren().iterator();
				listOfRooms.clear();
				while(i.hasNext()) {
					//this will do the trick
					listOfRooms.add(((DataSnapshot)i.next()).getKey());
					arrayAdapter.notifyDataSetChanged();
				}
			}
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
	}
}
