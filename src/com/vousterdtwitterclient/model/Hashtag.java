package com.vousterdtwitterclient.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Hashtag {

	private String text;
	private int[] indices = new int[2];
		
	public Hashtag(JSONObject jsonHastag)
	{
		super();
		try {
			
			String textFromJson = jsonHastag.getString("text");
			this.text = textFromJson;
			
			JSONArray indicesFromJson = jsonHastag.getJSONArray("indices");	
			
			for (int i = 0; i < indicesFromJson.length(); i++) {
				indices[i] = indicesFromJson.getInt(i);
			}
		    
			
		} catch (JSONException e) {
			e.printStackTrace();
		}	
	}

	public String getText() {
		return text;
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
