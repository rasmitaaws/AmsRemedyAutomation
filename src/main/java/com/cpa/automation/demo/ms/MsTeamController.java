package com.cpa.automation.demo.ms;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.cpa.automation.demo.remedy.RemedyIncidentHelper;
import com.cpa.automation.demo.util.HttpClientHelper;
import com.cpa.automation.demo.util.JsonUtil;
import com.cpa.automation.demo.util.OAuthConstants;

@Component
public class MsTeamController {

	@Autowired
	HttpClientHelper httpClientHelper;
	
	@Autowired
	RemedyIncidentHelper remedyIncidentHelper;

	public String getChatDetailsPage(Date lastUpdated) throws JSONException, IOException {
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++"+lastUpdated);

		String clienttoken = httpClientHelper
				.getAccessTokenForClientCredential(OAuthConstants.GRANT_TYPE_CLIENT_CREDENTIALS, "");

		String baseurl = "https://graph.microsoft.com/v1.0/users";

		URL url = new URL(baseurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// Set the appropriate header fields in the request header.
		conn.setRequestProperty("Authorization", "Bearer " + clienttoken);
		conn.setRequestProperty("Accept", "application/json");

		String response1 = httpClientHelper.getResponseStringFromConn(conn);

		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException(response1);
		}

		JSONObject responseObject = httpClientHelper.processResponse(responseCode, response1);

		// JSONObject valueTeamObject =responseObject.getJSONObject("value");

		JSONObject jsonObject1 = responseObject.getJSONObject("responseMsg");

		String teamId = "";

		String userId = "";

		if (jsonObject1.has("value")) {
			JSONArray jsonArray = jsonObject1.getJSONArray("value");

			for (int i = 0, size = jsonArray.length(); i < size; i++) {
				JSONObject objectInArray = jsonArray.getJSONObject(i);

				String[] elementNames = JSONObject.getNames(objectInArray);

				for (String elementName : elementNames) {
					if (elementName.equals("mail")
							&& (objectInArray.getString(elementName).equals("rasmiawsact02@gmail.com"))) {
						userId = objectInArray.getString("id");

						System.out.println(userId);
						break;
					}
				}

			}

		}

		String teamUrl = "https://graph.microsoft.com/v1.0/users/" + userId + "/joinedTeams";

		URL teamuri = new URL(teamUrl);
		HttpURLConnection teamconn = (HttpURLConnection) teamuri.openConnection();

		// Set the appropriate header fields in the request header.
		teamconn.setRequestProperty("Authorization", "Bearer " + clienttoken);
		teamconn.setRequestProperty("Accept", "application/json");

		String teamresponse1 = httpClientHelper.getResponseStringFromConn(teamconn);

		int teamresponseCode = teamconn.getResponseCode();
		if (teamresponseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException(teamresponse1);
		}

		JSONObject teamResponseObject = httpClientHelper.processResponse(teamresponseCode, teamresponse1);

		// JSONObject valueTeamObject =responseObject.getJSONObject("value");

		JSONObject teamJsonObject1 = teamResponseObject.getJSONObject("responseMsg");

		if (jsonObject1.has("value")) {
			JSONArray teamjsonArray = teamJsonObject1.getJSONArray("value");

			for (int i = 0, size = teamjsonArray.length(); i < size; i++) {
				JSONObject teamObjectInArray = teamjsonArray.getJSONObject(i);

				String[] elementNames = JSONObject.getNames(teamObjectInArray);

				for (String elementName : elementNames) {

					if (elementName.equals("displayName") && (teamObjectInArray.get(elementName).equals("CPA_POC"))) {

						teamId = teamObjectInArray.getString("id");
						System.out.println(userId);
						break;
					}

				}

			}

		}

		// String clienttoken =
		// httpClientHelper.getAccessTokenForClientCredential(OAuthConstants.GRANT_TYPE_CLIENT_CREDENTIALS,code);
		String channelUrl = "https://graph.microsoft.com/v1.0/teams/" + teamId + "/channels";

		URL channelurl = new URL(channelUrl);
		HttpURLConnection conn1 = (HttpURLConnection) channelurl.openConnection();

		// Set the appropriate header fields in the request header.
		conn1.setRequestProperty("Authorization", "Bearer " + clienttoken);
		conn1.setRequestProperty("Accept", "application/json");

		String channelResponse = httpClientHelper.getResponseStringFromConn(conn1);

		int channelResponseCode = conn1.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException(channelResponse);
		}

		JSONObject channelresponseObject = httpClientHelper.processResponse(channelResponseCode, channelResponse);

		String channelId = "";

		JSONObject jsonObject2 = channelresponseObject.getJSONObject("responseMsg");

		if (jsonObject2.has("value")) {

			JSONArray channelArray1 = jsonObject2.getJSONArray("value");
			for (int i = 0, size = channelArray1.length(); i < size; i++) {
				JSONObject channelObjectInArray = channelArray1.getJSONObject(i);

			
			

			String[] elementNames = JSONObject.getNames(channelObjectInArray);

			for (String elementName : elementNames) {
				
				if (elementName.equals("displayName")&& (channelObjectInArray.get(elementName).equals("Incident_Query"))) {
					
					channelId = channelObjectInArray.getString("id");

					break;
				}

			}

		}
		}

		String msgUrl = "https://graph.microsoft.com/beta/teams/" + teamId + "/channels/" + channelId + "/messages";

		URL msgUri = new URL(msgUrl);

		HttpURLConnection conn2 = (HttpURLConnection) msgUri.openConnection();

		// Set the appropriate header fields in the request header.

		// String authtoken2= httpClientHelper.getAuth(code, redirectUrl);

		conn2.setRequestProperty("Authorization", "Bearer " + clienttoken);
		conn2.setRequestProperty("Accept", "application/json");

		String msgResponse = httpClientHelper.getResponseStringFromConn(conn2);

		int msResponseCode = conn2.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException(msgResponse);
		}

		JSONObject msgresponseObject = httpClientHelper.processResponse(msResponseCode, msgResponse);

		return remedyIncidentHelper.updateIncident(JsonUtil.getMessages(msgresponseObject.toString()));

	}
}
