package com.jabber;

import android.content.Intent;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.app.Activity;
import android.widget.Toast;
import android.content.pm.PackageManager;

public class CameraScreen extends AppCompatActivity
{
	private final int CAMERA_REQUEST = 100;
	private final int RESULT_LOAD_IMG = 100;
	ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_screen);
		Button camBtn = (Button)findViewById(R.id.camBtn);
		camBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dispatchTakePictureIntent();
			}
		});
		final GalleryScreen gs = new GalleryScreen();
		Button galleryBtn = (Button)findViewById(R.id.galleryBtn);
		galleryBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), GalleryScreen.class);
				startActivity(intent);
			}
		});
		imageView = (ImageView)this.findViewById(R.id.imageView);
	}

	public void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, CAMERA_REQUEST);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
			Bitmap photo = (Bitmap)data.getExtras().get("data");
			imageView.setImageBitmap(photo);
		}
	}
}
