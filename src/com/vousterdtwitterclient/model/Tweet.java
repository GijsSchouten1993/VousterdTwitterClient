package com.vousterdtwitterclient.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

	private String text;
	private String created_at;
	private User user;
	private TweetEntity tweetEntity;
	
	public void setText(String text) {
		this.text = text;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getText() {
		return text.replaceAll("\n\n", "\n");
	}

	public String getCreated_at() {
		String[] date = this.created_at.split(" ");
		String shortedDate = "";
		
		for (int i = 1; i < 3; i++) {
			shortedDate += date[i];
			shortedDate += "\n";
		}
		return shortedDate.trim();
	}

	public User getUser() {
		return user;
	}

	public TweetEntity getTweetEntity() {
		return tweetEntity;
	}
	
	public Tweet(JSONObject jsonTweetObject) {
		super();
		try {
			JSONObject objUser = jsonTweetObject.getJSONObject("user");

			User dbUser = new User(objUser);

			String text = jsonTweetObject.getString("text");
			String created_at = jsonTweetObject.getString("created_at");

			this.text = text;
			this.created_at = created_at;
			this.user = dbUser;
			this.tweetEntity = new TweetEntity(jsonTweetObject.getJSONObject("entities"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public Tweet()
	{
		
	}

	public void setTweetEntity(TweetEntity tweetEntity) {
		this.tweetEntity = tweetEntity;
	}
	
}
