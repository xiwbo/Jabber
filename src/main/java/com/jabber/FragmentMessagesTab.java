package com.jabber;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentMessagesTab extends Fragment
{
	private ImageButton addContact;
	private RecyclerView recyclerView;
	private List<Users> listOfUsers;
	private UserAdapter userAdapter;

	public FragmentMessagesTab() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_messages_tab, container, false);
		addContact = view.findViewById(R.id.addContacts);
		recyclerView = view.findViewById(R.id.userRecyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		listOfUsers = new ArrayList<>();
		readUsers();
		addContact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getContext(), AddContacts.class);
				startActivity(intent);
			}
		});
		return(view);
	}

	private void readUsers() {
		final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
						Toast.makeText(getContext(), users.toString(), Toast.LENGTH_SHORT).show();
					}
				}
				userAdapter = new UserAdapter(getContext(), listOfUsers);
				recyclerView.setAdapter(userAdapter);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}
}
