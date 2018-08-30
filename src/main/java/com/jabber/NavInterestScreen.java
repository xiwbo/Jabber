package com.jabber;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NavInterestScreen extends Fragment
{
	private View view;
	private FirebaseAuth mAuth;
	private Firebase firebase;
	private Dialog myDialog;
	private FirebaseUser currentUser;
	private ImageButton addInterest;
	private EditText txtInterest;
	private String userId;
	private DatabaseReference mUserDatabase;
	private ListView listView;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayList<String> listOfInterests = new ArrayList<>();

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
		getActivity().setTitle("Interest");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_interest_screen, container, false);
		myDialog = new Dialog(getContext());
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Firebase.setAndroidContext(getContext());
		firebase = new Firebase(getResources().getString(R.string.firebaseDomain));
		mAuth = FirebaseAuth.getInstance();
		userId = mAuth.getCurrentUser().getUid();
		listView = view.findViewById(R.id.added_interest);
		arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listOfInterests);
		listView.setAdapter(arrayAdapter);
		txtInterest = view.findViewById(R.id.input_interest);
		addInterest = view.findViewById(R.id.btnAddInterest);
		addInterest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(txtInterest.getText().toString().isEmpty()) {
					PopupDialog popup = new PopupDialog(myDialog, "Please enter your interest.", "red", "OK");
					popup.showPopup();
				}
				else {
					mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Interests");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(txtInterest.getText().toString(), "");
					mUserDatabase.updateChildren(map);
					PopupDialog popup = new PopupDialog(myDialog, "Your interests is successfully added.", "red", "OK");
					popup.showPopup();
					txtInterest.setText("");
					mUserDatabase.addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							Set<String> set = new HashSet<String>();
							Iterator i = dataSnapshot.getChildren().iterator();
							while(i.hasNext()) {
								set.add(((DataSnapshot) i.next()).getKey());
							}
							listOfInterests.clear();
							listOfInterests.addAll(set);
						}

						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
						}
					});
				}
			}
		});
		return(view);
	}
}
