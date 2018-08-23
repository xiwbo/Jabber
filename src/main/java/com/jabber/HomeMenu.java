package com.jabber;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	NavigationView navigationView;

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

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		//Shows the first item in the nav drawer
		DisplayFragment(R.id.navHome);
		navigationView.setCheckedItem(R.id.navHome);

		View header = navigationView.getHeaderView(0);
		ImageButton imgButton = header.findViewById(R.id.addImgButton);
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
					}
				});
				gallery.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(getApplicationContext(), "Gallery", Toast.LENGTH_SHORT).show();
					}
				});
				alertadd.setView(aView);
				/*alertadd.setNeutralButton("Here!", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
					}
				});*/
				alertadd.show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if(drawer.isDrawerOpen(GravityCompat.START)) {
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
					FirebaseAuth.getInstance().signOut();
					Toast.makeText(getApplicationContext(), "Good Bye! ",Toast.LENGTH_LONG).show();
					Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
					startActivity(intent);
					finish();
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
}
