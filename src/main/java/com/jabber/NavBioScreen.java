package com.jabber;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class NavBioScreen extends Fragment
{
	View view;
	ImageButton profilePic;
	ImageView avatar;
	private final int CAMERA_REQUEST = 100;
	private Uri imageURI;
	private static final int PICK_IMAGE_REQUEST = 1;

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//title(header) bar
		getActivity().setTitle("Bio");
		//screen orientation of fragments
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_about_screen_option_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
			case R.id.manageAccount:
				Toast.makeText(getContext(), "Manage Account", Toast.LENGTH_SHORT).show();
				break;
			case R.id.logout:
				AlertDialog.Builder logOut = new AlertDialog.Builder(getContext());
				logOut.setCancelable(false);
				logOut.setTitle("Are you sure you want to log out?");
				logOut.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(getContext(), LoginScreen.class);
						startActivity(intent);
						FirebaseAuth.getInstance().signOut();
						getActivity().finish();
					}
				});
				logOut.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.cancel();
					}
				});
				logOut.show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_bio_screen, container, false);
		profilePic = view.findViewById(R.id.addDP);
		avatar = view.findViewById(R.id.imgAvatar);
		profilePic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openFileChooser();
			}
		});
		return(view);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			imageURI = data.getData();
			Picasso.get().load(imageURI).into(avatar);
			if(imageURI != null) {
				avatar.setImageURI(imageURI);
			}
			else  {
				System.out.println("HADASDA");
			}
		}
	}

	public void openFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_IMAGE_REQUEST);
	}
}
