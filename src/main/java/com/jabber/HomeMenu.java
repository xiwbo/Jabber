package com.jabber;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HomeMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	Dialog myDialog;
	TextView mName;
	PopupDialog logoutPopup;
	private static final int PICK_IMAGE_REQUEST = 1;
	private final int CAMERA_REQUEST = 100;
	private final int RESULT_GALLERY = 100;
	ImageView imageView;
	ImageButton imgButton;
	private Uri imageURI;
	View header;

	private FirebaseAuth mAuth;
	private DatabaseReference mUserDatabase;
	private String userId, name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		//Fragments on nav
		DisplayFragment(R.id.navHome);
		navigationView.setCheckedItem(R.id.navHome);
		header = navigationView.getHeaderView(0);
		imageView = header.findViewById(R.id.userPhoto);
		imgButton = header.findViewById(R.id.addProfilePhoto);
		mName = header.findViewById(R.id.mName);
		imgButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder alertadd = new AlertDialog.Builder(HomeMenu.this);
				LayoutInflater factory = LayoutInflater.from(HomeMenu.this);
				final View aView = factory.inflate(R.layout.camera_pop_up, null);
				ImageButton camera = aView.findViewById(R.id.imgCamera);
				ImageButton gallery = aView.findViewById(R.id.imgGallery);
				camera.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(getApplicationContext(), "Camera", Toast.LENGTH_SHORT).show();
						openCamera();
					}
				});
				gallery.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(getApplicationContext(), "Gallery", Toast.LENGTH_SHORT).show();
						openFileChooser();
					}
				});
				alertadd.setView(aView);
				alertadd.show();
			}
		});
		myDialog = new Dialog(this);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mAuth = FirebaseAuth.getInstance();
		userId = mAuth.getCurrentUser().getUid();
		mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
		getUserInfo();
	}

	public void openFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_IMAGE_REQUEST);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// noinspection SimplifiableIfStatement
		// if (id == R.id.action_settings) {
		//	return true;
		// }
		// 3 dots code will go here
		return(super.onOptionsItemSelected(item));
	}

	private void DisplayFragment(int id) {
		Fragment fragment = null;
		switch(id) {
			case R.id.navHome:
				fragment = new NavHomeScreen();
				break;
			case R.id.navBio:
				Toast.makeText(this, "BIO SCREEN", Toast.LENGTH_SHORT).show();
				break;
			case R.id.navInterest:
				Toast.makeText(this, "INTEREST SCREEN", Toast.LENGTH_SHORT).show();
				break;
			case R.id.navAnonChat:
				Toast.makeText(this, "ANON SCREEN", Toast.LENGTH_SHORT).show();
				break;
			case R.id.navAboutUs:
				fragment = new NavAboutScreen();
				break;
			case R.id.navLogout:
					logoutPopup = new PopupDialog(myDialog, "Are you sure you want to logout?", "OK");
					logoutPopup.showPromptPopup();
				break;
		}
		if(fragment != null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.MyFrameLayout, fragment);
			ft.commit();
		}
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		DisplayFragment(id);
		return(true);
	}

	public void openCamera() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, CAMERA_REQUEST);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			imageURI = data.getData();
			Picasso.get().load(imageURI).into(imageView);
			imageView.setImageURI(imageURI);
		}
		if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			imageURI = data.getData();
			Picasso.get().load(imageURI).into(imageView);
			imageView.setImageURI(imageURI);
		}
	}

	private void getUserInfo() {
		mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
					Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
					if(map.get("username")!= null) {
						name = map.get("username").toString();
						mName.setText(name);
						Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}
}
