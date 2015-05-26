package com.vousterdtwitterclient.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class User {
	private String name;
	private String id;
	private String screen_name;
	private String profile_image_url;
	private ArrayList<Tweet> tweets;
	private Bitmap image;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public String getId() {
		return id;
	}
	
	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public ArrayList<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(ArrayList<Tweet> tweets) {
		this.tweets = tweets;
	}

	public User(JSONObject jsonUserObject) {
		super();
		try {

			String name = jsonUserObject.getString("name");
			String profile_image_url = jsonUserObject
					.getString("profile_image_url");
			String screen_name = jsonUserObject.getString("screen_name");
			String id = jsonUserObject.getString("id_str");

			this.name = name;
			this.screen_name = screen_name;
			this.profile_image_url = profile_image_url;
			this.id = id;

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public User()
	{
		
	}
}
