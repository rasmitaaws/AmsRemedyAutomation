package com.cpa.automation.demo.remedy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class BMCRestTestController {

	public static String login(String baseURL, String userName, String password) {
		String output = "";

		if (!baseURL.endsWith("/")) {
			baseURL = baseURL + "/";
		}

		HttpURLConnection urlConnection = null;

		try {
			URL urltorequest = new URL(baseURL + "api/jwt/login");
			urlConnection = (HttpURLConnection) urltorequest.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);

			String str = "username=" + userName + "&password=" + password;
			byte[] outputInBytes = str.getBytes("UTF-8");
			OutputStream os = urlConnection.getOutputStream();
			os.write(outputInBytes);
			os.close();

			// int statusCode = urlConnection.getResponseCode();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			output = "AR-JWT " + getResponseText(in);

		} catch (MalformedURLException ex) {

		} catch (IOException ex) {

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return output;
	}

	public static int logout(String baseURL, String token) {
		HttpURLConnection urlConnection = null;
		int statusCode = 0;

		if (!baseURL.endsWith("/")) {
			baseURL = baseURL + "/";
		}

		try {
			URL urltorequest = new URL(baseURL + "api/jwt/logout");
			urlConnection = (HttpURLConnection) urltorequest.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Authorization", token);
			statusCode = urlConnection.getResponseCode();

		} catch (MalformedURLException ex) {

		} catch (IOException ex) {

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return statusCode;
	}

	public static void getEntry(String baseURL, String token, String entryID) {
		HttpURLConnection urlConnection = null;

		if (!baseURL.endsWith("/")) {
			baseURL = baseURL + "/";
		}

		try {

			URL urltorequest = new URL(
					baseURL + "api/arsys/v1/entry/" + "HPD:IncidentInterface_Create" + "/" + entryID);
			urlConnection = (HttpURLConnection) urltorequest.openConnection();
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Authorization", token);
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			System.out.println("Response Message: " + urlConnection.getResponseMessage() + " Response Code : "
					+ urlConnection.getResponseCode() + "  ");
		} catch (MalformedURLException ex) {
			System.out.println(ex.getMessage() + ex);

		} catch (IOException ex) {
			System.out.println(ex.getMessage() + ex);

			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}

	private static String getResponseText(InputStream inStream) {
		return new Scanner(inStream).useDelimiter("\\A").next();
	}

	public static void updateIncident(String token, String msg) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(
				"http://VTRVITSTP-03:8008/api/arsys/v1/entry/HPD:IncidentInterface/INC000000002106%7CINC000000002106");

		// build the JSON entry
		String json = "{ \"values\" : {\n ";
		json += "\"z1D_Details\" : \"" + msg + "\",\n";
		json += "\"z1D_WorklogDetails\" : \"updated via REST\",\n";
		json += "\"z1D Action\" : \"MODIFY\",\n";
		json += "\"z1D_View_Access\" : \"Internal\",\n";
		json += "\"z1D_Secure_Log\" : \"Yes\",\n";
		json += "\"z1D_Activity_Type\" : \"Incident Task/Action\",\n";
	    json += "\"Resolution\" : \"User Request has been serviced\",\n";
	    json += "\"Urgency\" : \"3-Medium\",\n";
		json += "\"Detailed Decription\" : \"Updated description\"";
		json += " } }";
		System.out.println(json);
		httpPut.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
		httpPut.addHeader("Authorization", token);

		// make the call and print the status
		try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
			StatusLine status = response.getStatusLine();
			System.out.println("update");
			System.out.println("update status:" + status);
		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage() + e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage() + e);
			e.printStackTrace();
		}

	}


}
