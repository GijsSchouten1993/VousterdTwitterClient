package com.vousterdtwitterclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Url {
	private String url;

	public String getUrl() {
		return url;
	}

	public int[] getIndices() {
		return indices;
	}

	private int[] indices = new int[2];

	public Url(JSONObject jsonUrl) {
		super();
		try {

			String urlFromJson = jsonUrl.getString("url");
			this.url = urlFromJson;

			JSONArray indicesFromJson = jsonUrl.getJSONArray("indices");

			for (int i = 0; i < indicesFromJson.length(); i++) {
				indices[i] = indicesFromJson.getInt(i);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
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
