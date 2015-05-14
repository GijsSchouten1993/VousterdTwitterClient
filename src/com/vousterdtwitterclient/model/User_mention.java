package com.vousterdtwitterclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User_mention {

	private String screen_name, name;
	private int id;
	private int[] indices = new int[2];

	public User_mention(JSONObject jsonMention) {
		super();
		try {

			String screen_name_json = jsonMention.getString("screen_name");
			this.screen_name = screen_name_json;

			String name_json = jsonMention.getString("name");
			this.name = name_json;

			int id_json = jsonMention.getInt("id");
			this.id = id_json;

			JSONArray indicesFromJson = jsonMention.getJSONArray("indices");

			for (int i = 0; i < indicesFromJson.length(); i++) {
				indices[i] = indicesFromJson.getInt(i);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getScreen_name() {
		return screen_name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int[] getIndices() {
		return indices;
	}
	
	public int getStartPositionIndice()
	{
		return this.indices[0];
	}
	
	public int getEndPositionIndice()
	{
		return this.indices[1];
	}
}
