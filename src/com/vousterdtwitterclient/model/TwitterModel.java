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
		if(tweets.size() == 0)
		{
			Tweet objTweet = new Tweet();
			objTweet.setText("Geen resultaten gevonden");
			User objUser = new User();
			objUser.setName("Vousterd");
			objUser.setProfile_image_url("https://scontent.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/p280x280/10492499_460021894142446_6754136707238186601_n.jpg?oh=a1a5ba56b9f3e95c9499f5f5f0c31332&oe=55C584B8");
			objTweet.setUser(objUser);
			objTweet.setCreated_at("Thu In 2015 ");
			TweetEntity tweEntity = new TweetEntity();
			objTweet.setTweetEntity(tweEntity);
			
			tweets.add(objTweet);
		}
		return tweets;
	}
	
	public void addTweet(Tweet tweet) {
		tweets.add(tweet);
	}
}
