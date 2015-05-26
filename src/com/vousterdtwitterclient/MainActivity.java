package com.vousterdtwitterclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.vousterdtwitterclient.model.ApiConnection;
import com.vousterdtwitterclient.model.Tweet;
import com.vousterdtwitterclient.model.TwitterModel;
import com.vousterdtwitterclient.view.TwitterAdapter;

public class MainActivity extends ActionBarActivity {
	private TwitterModel model;
	private ImageView twitterImg;
	private EditText searchHeader;
	private Button searchBtn, tweetBtn, profileBtn;
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

		model = ((VousterdApplication) getApplication()).getModel();
		// Koppel xml-elementen aan variabelen
		twitterImg = (ImageView) findViewById(R.id.twitterImg);
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		tweetBtn = (Button)findViewById(R.id.tweetBtn);
		profileBtn = (Button)findViewById(R.id.profileBtn);
		searchHeader = (EditText) findViewById(R.id.searchHeader);
		
		//Events
		searchBtn.setOnClickListener(new searchClickListener());
		profileBtn.setOnClickListener(new ProfileClickListener());
		tweetBtn.setOnClickListener(new TweetClickListener());
		
		String jsonText = null;
		try {
			jsonText = readAssetIntoString("test.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setListOfJsonString(jsonText);
		// Setup cursor adapter using cursor from last step
		TwitterAdapter todoAdapter = new TwitterAdapter(this, model.getTweets());
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

	private class searchClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			// Bouw de url op
			Uri.Builder builder = new Uri.Builder();
			builder.scheme("https")
					.authority("api.twitter.com")
					.appendPath("1.1")
					.appendPath("search")
					.appendPath("tweets.json")
					.appendQueryParameter("q",
							"%23" + searchHeader.getText().toString());

			String url = builder.build().toString();

			asyncTweets tweetTask = new asyncTweets();
			tweetTask.execute(url);
		}

	}
	
	private class ProfileClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
		//Ga naar de profiel pagina
			
		}

	}
	
	private class TweetClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			//Ga naar een nieuwe tweet pagina
			
		}

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

	// Haal de response van een request op en return de body als string
	private static String readResponse(HttpsURLConnection connection) {
		try {
			StringBuilder str = new StringBuilder();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = "";
			while ((line = br.readLine()) != null) {
				str.append(line + System.getProperty("line.separator"));
			}
			return str.toString();
		} catch (IOException e) {
			return new String();
		}
	}

	private String getResponseBody(HttpRequestBase request) {
		StringBuilder sb = new StringBuilder();
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpResponse response = (HttpResponse) httpClient.execute(request);
			int statusCode = ((org.apache.http.HttpResponse) response)
					.getStatusLine().getStatusCode();
			String reason = ((org.apache.http.HttpResponse) response)
					.getStatusLine().getReasonPhrase();

			if (statusCode == 200) {

				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();

				BufferedReader bReader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"), 8);
				String line = null;
				while ((line = bReader.readLine()) != null) {
					sb.append(line);
				}
			} else {
				sb.append(reason);
			}
		} catch (UnsupportedEncodingException ex) {
		} catch (ClientProtocolException ex1) {
		} catch (IOException ex2) {
		}
		return sb.toString();
	}

	private class asyncTweets extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			String urlText = params[0];
			if (urlText.equals("")) {
				return "";
			}

			ApiConnection conn = new ApiConnection();

			Log.d("VOUSTERD", "Begin met request ");

			try {

				String bearer = "";

				// Bearer token bestaat nog niet haal een nieuwe bearer op
				HttpsURLConnection bearerConn = conn
						.buildBearerHttpsUrlConnection();

				// Haal de json op van het request
				String jsonFromRequest = readResponse(bearerConn);

				JSONObject obj = new JSONObject(jsonFromRequest);

				if (obj != null) {
					String tokenType = (String) obj.get("token_type");
					String token = (String) obj.get("access_token");

					bearer = ((tokenType.equals("bearer")) && (token != null)) ? token
							: "";
				}

				// Maak http request aan
				HttpGet httpGet = new HttpGet(urlText);

				// Voeg bearer token aan het request toe
				httpGet.setHeader("Authorization", "Bearer " + bearer);
				httpGet.setHeader("Content-Type", "application/json");

				return getResponseBody(httpGet);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			 finally {
				
			}
			return null;
		}

		// Als backgroundworker klaar is bind de lijst
		@Override
		protected void onPostExecute(String result) {

			if (result != null && result.length() > 0) {
				// hier binden we straks de lijst in het model
				model.setListOfJsonString(result);
				// Setup cursor adapter using cursor from last step
				TwitterAdapter todoAdapter = new TwitterAdapter(
						MainActivity.this, model.getTweets());
				// Attach cursor adapter to the ListView
				lvTweets.setAdapter(todoAdapter);
			}

		}

	}

}
