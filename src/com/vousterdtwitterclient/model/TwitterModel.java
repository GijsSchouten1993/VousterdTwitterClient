package com.vousterdtwitterclient.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterModel {
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	
	/**
	 * Gets the full user list.
	 * @return
	 */
	public ArrayList<User> getUsers() {
		return users;
	}
	
	public void addUser(User user) {
		users.add(user);
	}
	
	public boolean userDoesExist(String id) {
		for (User user : users) {
			if (user.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * get a specific user using his id.
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		for (User user : users) {
			if (user.getId().equals(id)) {
				return user;
			}
		}
		return null;
	}
	
	public void setListOfJsonString(String json) {
		ArrayList<Tweet> tempArray = new ArrayList<Tweet>();
		try {

			JSONObject jObj = new JSONObject(json);

			JSONArray jArr = jObj.getJSONArray("statuses");

			for (int i = 0; i < jArr.length(); i++) {

				JSONObject objTweet = jArr.getJSONObject(i);

				Tweet dbTweet = new Tweet(objTweet);

				tempArray.add(dbTweet);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		tweets = tempArray;

	}
	
	public ArrayList<Tweet> getTweets() {
		return tweets;
	}
	
	public void addTweet(Tweet tweet) {
		tweets.add(tweet);
	}
}
