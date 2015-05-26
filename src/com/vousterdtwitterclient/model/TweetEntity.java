package com.vousterdtwitterclient.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TweetEntity {

	private List<Url> urls = new ArrayList<Url>();
	private List<User_mention> user_mentions = new ArrayList<User_mention>();
	private List<Hashtag> hashtags = new ArrayList<Hashtag>();
	
	public List<Url> getUrls() {
		return urls;
	}

	public List<User_mention> getUser_mentions() {
		return user_mentions;
	}

	public List<Hashtag> getHashtags() {
		return hashtags;
	}

	public TweetEntity(JSONObject jsonTweetObject) {
		try {
			// URLS
			JSONArray urlArray = jsonTweetObject.getJSONArray("urls");

			for (int i = 0; i < urlArray.length(); i++) {
				Url tempUrl = new Url(urlArray.getJSONObject(i));
				urls.add(tempUrl);

			}

			// User mention
			JSONArray userMentionsArray = jsonTweetObject
					.getJSONArray("user_mentions");

			for (int i = 0; i < userMentionsArray.length(); i++) {
				User_mention tempUserMention = new User_mention(
						userMentionsArray.getJSONObject(i));
				user_mentions.add(tempUserMention);

			}

			// hashtags
			JSONArray hashTagsArray = jsonTweetObject
					.getJSONArray("hashtags");

			for (int i = 0; i < hashTagsArray.length(); i++) {
				Hashtag tempHastag = new Hashtag(
						hashTagsArray.getJSONObject(i));
				hashtags.add(tempHastag);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public TweetEntity()
	{
		
	}

}
