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
import android.net.Uri;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import java.io.FileNotFoundException;

public class GalleryScreen extends AppCompatActivity
{
	private final int CAMERA_REQUEST = 100;
	private final int RESULT_LOAD_IMG = 100;
	ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_screen);
		Button camBtn = (Button)findViewById(R.id.camBtn);
		Button galleryBtn = (Button)findViewById(R.id.galleryBtn);
		galleryBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openGalleryIntent();
			}
		});
		imageView = (ImageView)this.findViewById(R.id.imageView);
	}

	private void openGalleryIntent() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
	}

	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			try {
				final Uri imageUri = data.getData();
				final InputStream imageStream = getContentResolver().openInputStream(imageUri);
				final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
				imageView.setImageBitmap(selectedImage);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
