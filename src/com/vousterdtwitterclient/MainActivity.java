package com.vousterdtwitterclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.vousterdtwitterclient.model.Tweet;
import com.vousterdtwitterclient.model.User;
import com.vousterdtwitterclient.view.TwitterAdapter;

public class MainActivity extends ActionBarActivity {
	private ImageView twitterImg, vousterdImg;
	private EditText searchHeader;
	private Button searchBtn, tweetBtn;
	private ListView lvTweets;
	InputStream is = null;
	String result = null;
	String line = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Koppel xml-elementen aan variabelen
		twitterImg = (ImageView) findViewById(R.id.twitterImg);
		vousterdImg = (ImageView) findViewById(R.id.vousterdImg);
		lvTweets = (ListView) findViewById(R.id.lvTweets);

		String jsonText = null;
		try {
			jsonText = readAssetIntoString("test.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<Tweet> convertedJsonTweets = getListOfJsonString(jsonText);

		// Setup cursor adapter using cursor from last step
		TwitterAdapter todoAdapter = new TwitterAdapter(this,
				convertedJsonTweets);
		// Attach cursor adapter to the ListView
		lvTweets.setAdapter(todoAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Reads an asset file and returns a string with the full contents.
	 *
	 * @param filename
	 *            The filename of the file to read.
	 * @return The contents of the file.
	 * @throws IOException
	 *             If file could not be found or not read.
	 */
	private String readAssetIntoString(String filename) throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			InputStream is = getAssets().open(filename,
					AssetManager.ACCESS_BUFFER);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	private ArrayList<Tweet> getListOfJsonString(String json) {
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

		return (tempArray);

	}

}
