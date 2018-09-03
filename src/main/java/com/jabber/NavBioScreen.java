package com.jabber;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import static android.app.Activity.RESULT_OK;

public class NavBioScreen extends Fragment
{
	private View view;
	private ImageButton profilePic;
	private ImageView avatar;
	private Uri imageURI;
	private static final int PICK_IMAGE_REQUEST = 1;
	private Dialog myDialog;
	private Spinner spinner;
	private MaterialEditText birthdate;
	private String myValue;

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle("Bio");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Failed to connect to the server. Please check your connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					Button btnYes;
					myDialog.setContentView(R.layout.popuplogout);
					btnYes = myDialog.findViewById(R.id.popupBtnYes);
					btnYes.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							myDialog.dismiss();
							startActivity(new Intent(getContext(), LoginScreen.class));
							FirebaseAuth.getInstance().signOut();
							getActivity().finish();
						}
					});
					Button btnCancel;
					btnCancel = (Button) myDialog.findViewById(R.id.popupBtnCancel);
					btnCancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							myDialog.dismiss();
						}
					});
					myDialog.setCanceledOnTouchOutside(false);
					myDialog.show();
				}
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_bio_screen, container, false);
		myDialog = new Dialog(getContext());
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		profilePic = view.findViewById(R.id.imgButtonCameraBio);
		spinner = view.findViewById(R.id.spinnerBio);
		avatar = view.findViewById(R.id.profilePicture);
		birthdate = view.findViewById(R.id.txtBirthDateBio);
		String[] gender = new String[] { "Male", "Female" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, gender);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		birthdate.setText(myValue);
		profilePic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, PICK_IMAGE_REQUEST);
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
		}
	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
			return(true);
		}
		else {
			return(false);
		}
	}
}
