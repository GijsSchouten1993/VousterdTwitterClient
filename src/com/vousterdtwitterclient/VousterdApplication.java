package com.vousterdtwitterclient;

import android.app.Application;

import com.vousterdtwitterclient.model.TwitterModel;

public class VousterdApplication extends Application{
	private TwitterModel model;
	
	public VousterdApplication () {
		model = new TwitterModel();
	}
	
	public TwitterModel getModel() {
		return model;
	}

}
