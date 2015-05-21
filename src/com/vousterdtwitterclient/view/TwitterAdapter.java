package com.vousterdtwitterclient.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vousterdtwitterclient.R;
import com.vousterdtwitterclient.model.Hashtag;
import com.vousterdtwitterclient.model.Tweet;

public class TwitterAdapter extends ArrayAdapter<Tweet> {
	private TextView tweeterName, tweetTxt, dateTxt;
	private List<Tweet> tweets;

	public TwitterAdapter(Context context, ArrayList<Tweet> tweets) {
		super(context, 0, tweets);
		this.tweets = tweets;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.tweetview, parent, false);
		}

		tweeterName = (TextView) convertView.findViewById(R.id.tweeterNameTxt);
		tweetTxt = (TextView) convertView.findViewById(R.id.tweetTxt);
		dateTxt = (TextView) convertView.findViewById(R.id.dateTxt);
		
		Tweet tempTweet = tweets.get(position);
		
		tweeterName.setText(tempTweet.getUser().getName());
		
		List<Hashtag> tags = tempTweet.getTweetEntity().getHashtags();
		
		SpannableString spannablecontent = new SpannableString(tempTweet.getText());
		
		//Loop door alle hashtags in een tweet en geef deze een opmaak.
		for (int i = 0; i < tags.size(); i++) {
			Hashtag tag = tags.get(i);
			spannablecontent.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 
                    tag.getStartPositionIndice(),tag.getEndPositionIndice(), 0);
		}
		
		// set Text here
		tweetTxt.setText(spannablecontent);	
		dateTxt.setText(tempTweet.getCreated_at());

		new DownloadImageTask(
				(ImageView) convertView.findViewById(R.id.tweeterImg))
				.execute(tempTweet.getUser().getProfile_image_url());

		return convertView;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

}
