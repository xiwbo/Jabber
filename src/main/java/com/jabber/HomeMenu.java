package com.jabber;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class HomeMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	private static final int PICK_IMAGE_REQUEST = 1;
	private final int CAMERA_REQUEST = 100;
	private ImageView imageView;
	private ImageButton imgButton, camera, gallery;
	private Uri imageURI;
	private View header;
	private TextView mName;
	private DrawerLayout drawer;
	private Fragment fragment = null;
	private NavigationView navigationView;
	private FirebaseAuth mAuth;
	private DatabaseReference mUserDatabase;
	private String userId, name;
	private Toolbar toolbar;
	private AlertDialog.Builder logOut, alertadd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		navigationView = findViewById(R.id.nav_view);
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
				alertadd = new AlertDialog.Builder(HomeMenu.this);
				LayoutInflater factory = LayoutInflater.from(HomeMenu.this);
				final View aView = factory.inflate(R.layout.camera_pop_up, null);
				camera = aView.findViewById(R.id.imgCamera);
				gallery = aView.findViewById(R.id.imgGallery);
				camera.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, CAMERA_REQUEST);
						}
					}
				});
				gallery.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(intent, PICK_IMAGE_REQUEST);
					}
				});
				alertadd.setView(aView);
				alertadd.show();
			}
		});
		mAuth = FirebaseAuth.getInstance();
		userId = mAuth.getCurrentUser().getUid();
		mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
		getUserInfo();
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		}
		else {
			super.onBackPressed();
		}
	}

	private void DisplayFragment(int id) {
		switch(id) {
			case R.id.navHome:
				fragment = new NavHomeScreen();
				break;
			case R.id.navBio:
				fragment = new NavBioScreen();
				break;
			case R.id.navInterest:
				fragment = new NavInterestScreen();
				break;
			case R.id.navAnonChat:
				fragment = new NavAnonymousChatScreen();
				break;
			case R.id.navAboutUs:
				fragment = new NavAboutScreen();
				break;
			case R.id.navLogout:
				logOut = new AlertDialog.Builder(this);
				logOut.setCancelable(false);
				logOut.setTitle("Are you sure you want to log out?");
				logOut.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(HomeMenu.this, LoginScreen.class);
						startActivity(intent);
						FirebaseAuth.getInstance().signOut();
						finish();
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
		if(fragment != null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.MyFrameLayout, fragment);
			ft.commit();
		}
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
		getPermissions();
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

	 private void getPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.CAMERA}, 1);
		}
	}

	public void logout(){
		AlertDialog.Builder logOut = new AlertDialog.Builder(this);
		logOut.setCancelable(false);
		logOut.setTitle("Are you sure you want to log out?");
		logOut.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				Intent intent = new Intent(HomeMenu.this, LoginScreen.class);
				startActivity(intent);
				FirebaseAuth.getInstance().signOut();
				finish();
			}
		});
		logOut.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.cancel();
			}
		});
		logOut.show();
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
