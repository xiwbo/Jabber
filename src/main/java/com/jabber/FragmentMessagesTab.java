package com.jabber;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class FragmentMessagesTab extends Fragment
{
	private View view;
	private ImageButton addContact;

	public FragmentMessagesTab() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_messages_tab, container, false);
		addContact = view.findViewById(R.id.addContacts);
		addContact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getContext(), AddContacts.class);
				startActivity(intent);
			}
		});
		return(view);
	}
}
