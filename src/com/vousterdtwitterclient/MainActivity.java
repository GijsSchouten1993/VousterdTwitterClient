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

import com.vousterdtwitterclient.model.Tweet;
import com.vousterdtwitterclient.model.TwitterModel;
import com.vousterdtwitterclient.view.TwitterAdapter;

public class MainActivity extends ActionBarActivity {
	private TwitterModel model;
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

		model = ((VousterdApplication) getApplication()).getModel();
		// Koppel xml-elementen aan variabelen
		twitterImg = (ImageView) findViewById(R.id.twitterImg);
		vousterdImg = (ImageView) findViewById(R.id.vousterdImg);
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchHeader = (EditText) findViewById(R.id.searchHeader);
		searchBtn.setOnClickListener(new searchClickListener());

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

	// Voeg de consumerkey en consumerSecret samen
	private static String encodeKeys(String consumerKey, String consumerSecret) {

		try {
			String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
			String encodedConsumerSecret;
			encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");

			String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;

			return (Base64.encodeToString(fullKey.toString().getBytes(),
					Base64.NO_WRAP));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	// Schrijf body in het request
	private static boolean writeRequest(HttpsURLConnection connection,
			String textBody) {
		try {
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
					connection.getOutputStream()));
			wr.write(textBody);
			wr.flush();
			wr.close();

			return true;
		} catch (IOException e) {
			return false;
		}
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

	@SuppressLint("NewApi")
	private void WriteToApiProperties(String content) {

		ClassLoader classLoader = getClass().getClassLoader();
		URL lol = classLoader.getResource("apiconfig.properties");
		Log.d("VOUSTERD", lol.toString());
		File configFile = new File(lol.getFile());

		try {
			Properties props = new Properties();
			props.setProperty("bearertoken", content);
			FileOutputStream fileOut = new FileOutputStream(configFile);
			props.store(fileOut, "Aangepast");
			fileOut.close();
		} catch (FileNotFoundException ex) {
			Log.d("VOUSTERD", ex.getMessage());
		} catch (IOException ex) {
			// I/O error
		}

	}

	@SuppressLint("NewApi")
	private String ReadFromApiProperties() {

		Properties prop = new Properties();
		String propFileName = "apiconfig.properties";

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propFileName);

		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				return "";
			}
		} else {
			try {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");

			} catch (FileNotFoundException e) {
				return "";
			}
		}

		// get the property value and print it out
		String token = prop.getProperty("bearertoken");

		return token;
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

		private String CONSUMER_KEY = "nPOmUQZGrLzaWThuod9YJwoBD";
		private String CONSUMER_SECRET = "AYhS9S4eLfLgp5EhGmSnbmjC587hIZ37tYjjcuGBgHnwMuCPa0";

		private String ACCESS_TOKEN = "318650666-ulmXnpcfXjVdjpYGK5ULutBWBYXD4zMS8ICfMH7x";
		private String TOKEN_SECRET = "d2hOWpyJ9HywjRBbkqwkBcoE4oHNcv7YbqdF7IwPwA9Hu";

		private String OAUTH2BEARERTOKENURL = "https://api.twitter.com/oauth2/token";

		@Override
		protected String doInBackground(String... params) {

			String urlText = params[0];
			if (urlText.equals("")) {
				return "";
			}
			URL url;
			String encodedCredentials = encodeKeys(CONSUMER_KEY,
					CONSUMER_SECRET);
			Log.d("VOUSTERD", "Begin met request ");

			HttpsURLConnection connection = null;
			try {

				// Haal de bearer token op uit het tekstbestand
				String bearer = ReadFromApiProperties();
				Log.d("VOUSTERD", bearer);
				// Bearer token bestaat nog niet haal een nieuwe bearer op en
				// sla deze op in het tekstbestand
				if (bearer == null || bearer.length() == 0) {
					// Voeg de public key en private samen

					url = new URL(OAUTH2BEARERTOKENURL);
					connection = (HttpsURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Host", "api.twitter.com");
					connection.setRequestProperty("User-Agent", "Vousterd");
					connection.setRequestProperty("Authorization", "Basic "
							+ encodedCredentials);
					connection.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded;charset=UTF-8");
					connection.setUseCaches(false);

					// Schrijf weg naar body
					writeRequest(connection, "grant_type=client_credentials");

					// Haal de json op van het request
					String jsonFromRequest = readResponse(connection);

					JSONObject obj = new JSONObject(jsonFromRequest);

					if (obj != null) {
						String tokenType = (String) obj.get("token_type");
						String token = (String) obj.get("access_token");

						String bearerText = ((tokenType.equals("bearer")) && (token != null)) ? token
								: "";
						WriteToApiProperties(bearerText);
					}
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
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
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
