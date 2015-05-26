package com.vousterdtwitterclient.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import android.util.Base64;

public class ApiConnection {

	private final String CONSUMER_KEY = "nPOmUQZGrLzaWThuod9YJwoBD";
	private final String CONSUMER_SECRET = "AYhS9S4eLfLgp5EhGmSnbmjC587hIZ37tYjjcuGBgHnwMuCPa0";

	private final String ACCESS_TOKEN = "318650666-ulmXnpcfXjVdjpYGK5ULutBWBYXD4zMS8ICfMH7x";
	private final String TOKEN_SECRET = "d2hOWpyJ9HywjRBbkqwkBcoE4oHNcv7YbqdF7IwPwA9Hu";

	private String OAUTH2BEARERTOKENURL = "https://api.twitter.com/oauth2/token";

	public HttpsURLConnection buildBearerHttpsUrlConnection() {
		
		String encodedCredentials = encodeKeys(CONSUMER_KEY, CONSUMER_SECRET);
		URL url;
		try {
			url = new URL(OAUTH2BEARERTOKENURL);
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
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

			return connection;

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

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
		public static String readResponse(HttpsURLConnection connection) {
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
}
