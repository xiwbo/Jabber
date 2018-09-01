package com.jabber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddContacts extends AppCompatActivity
{
	private RecyclerView recyclerView;
	private List<Users> listOfUsers;
	private UserAdapter userAdapter;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayList<String> lists = new ArrayList<>();
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contacts);
		recyclerView = findViewById(R.id.userRecyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		listOfUsers = new ArrayList<>();
		listView = findViewById(R.id.addContactsListView);
		arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,lists);
		listView.setAdapter(arrayAdapter);
		lists.add("Invite friends");
		lists.add("Contacts help");
		arrayAdapter.notifyDataSetChanged();
		readUsers();
	}

	private void readUsers() {
		final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				listOfUsers.clear();
				for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
					Users users = snapshot.getValue(Users.class);
					assert users != null;
					assert firebaseUser != null;
					if(!users.getId().equals(firebaseUser.getUid())) {
						//populate the recyclerview with username of users
						listOfUsers.add(users);
					}
				}
				userAdapter = new UserAdapter(getApplicationContext(), listOfUsers);
				recyclerView.setAdapter(userAdapter);
			}
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
	}
}
