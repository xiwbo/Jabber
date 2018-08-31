package com.jabber;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.MenuItem;
import android.view.Window;
import android.view.View;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
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
	private Dialog myDialog;
	private ImageView imageView;
	private ImageButton imgButton, camera, gallery;
	private Uri imageURI;
	private View header;
	private TextView mName;
	private DrawerLayout drawer;
	private Fragment fragment = null;
	private NavigationView navigationView;
	private FirebaseAuth mAuth;
	private StorageReference storageReference;
	private DatabaseReference mUserDatabase, profileDatabase;
	private StorageTask storageTask;
	private String userId, name;
	private Toolbar toolbar;
	private AlertDialog.Builder alertadd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myDialog = new Dialog(this);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
						getPermissions();
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
		storageReference = FirebaseStorage.getInstance().getReference("ProfilePics");
		profileDatabase = FirebaseDatabase.getInstance().getReference("ProfilePics");
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
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Failed to connect to the server. Please check your connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					promptLogout();
				}
				break;
		}
		if(fragment != null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.MyFrameLayout, fragment);
			ft.commit();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		DisplayFragment(id);
		drawer.closeDrawer(GravityCompat.START);
		return(true);
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
			if(storageTask != null && storageTask.isInProgress()) {
				Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_LONG).show();
			}
			else {
				uploadFiletoFirebase();
			}
		}
	}

	private String FileNameExtension(Uri uri) {
		//gets the file extension (.jpeg) etc.
		ContentResolver contentResolver = getContentResolver();
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		return(mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)));
	}

	public void uploadFiletoFirebase() {
		if(imageURI != null) {
			StorageReference sReference = storageReference.child(System.currentTimeMillis() + "." + FileNameExtension(imageURI));
			storageTask = sReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
				@Override
				public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
					ImageUploadFirebase imageUploadFirebase = new ImageUploadFirebase("",taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
					final String uploadId = profileDatabase.push().getKey();
					assert uploadId != null;
					profileDatabase.child(uploadId).setValue(imageUploadFirebase);
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					System.out.println("Error on File Upload: " + e.getMessage());
				}
			});
		}
		else {
			Toast.makeText(getApplicationContext(), "No file selected.", Toast.LENGTH_SHORT).show();
		}
	}

	 private void getPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.CAMERA}, 1);
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
					}
				}
			}
			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}

	public void promptLogout() {
		Button btnYes;
		myDialog.setContentView(R.layout.popuplogout);
		btnYes = myDialog.findViewById(R.id.popupBtnYes);
		btnYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Failed to connect to the server. Please check your connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					myDialog.dismiss();
					startActivity(new Intent(HomeMenu.this, LoginScreen.class));
					FirebaseAuth.getInstance().signOut();
					finish();
				}
			}
		});
		Button btnCancel;
		btnCancel = (Button)myDialog.findViewById(R.id.popupBtnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isOnline()) {
					PopupDialog popup = new PopupDialog(myDialog, "Failed to connect to the server. Please check your connection.", "red", "OK");
					popup.showPopup();
				}
				else {
					myDialog.dismiss();
				}
			}
		});
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.show();
	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
			return(true);
		}
		else {
			return(false);
		}
	}
}
