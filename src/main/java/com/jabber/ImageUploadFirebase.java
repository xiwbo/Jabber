package com.jabber;

public class ImageUploadFirebase
{
	private String name, ImageURL;

	public ImageUploadFirebase() {
	}

	public ImageUploadFirebase(String name, String imageURL) {
		if(name.trim().equals("")) {
			name = "No name";
		}
		this.name = name;
		ImageURL = imageURL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return ImageURL;
	}

	public void setImageURL(String imageURL) {
		ImageURL = imageURL;
	}
}
