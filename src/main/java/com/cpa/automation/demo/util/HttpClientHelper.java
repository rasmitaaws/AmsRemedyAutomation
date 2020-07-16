// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.cpa.automation.demo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


@Component
public class HttpClientHelper {
	
	@Value("${client-id}")
	private String clientId;
	/** process Folder **/
	@Value("${client-secret}")
	private String secretId;
	@Value("${client.scope}")
	private String scope;
	
	@Value("${client.RedirectUri}")
	private String redirectUri;
	
	/** process Folder **/
	@Value("${client.access-token-uri}")
	private String tokenUri;
	
	@Value("${client.user-authorization-uri}")
	private String authorizeUri;
	
	@Value("${user-info-uri}")
	private String userInfoUri;
		
	@Value("${approval_prompt_key=prompt}")
	private String promptKey;
	
	@Value("$approval_prompt_value}")
	private String promptValue;
	
	@Value("${access_type_key}")
	private String  accessKey;
	
	@Value("${access_type_value}")
	private String accessValue;
	
	
	/**
	 * Request OAuth server for authorization code
	 * 
	 * @param oauthDetails
	 * @return
	 */
	public  String getAuthorizationCode() {

		HttpPost post = new HttpPost(authorizeUri);
		String location = null;

		String state = "hello chat";

		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

		parametersBody.add(new BasicNameValuePair(OAuthConstants.RESPONSE_TYPE,
				OAuthConstants.CODE));

		parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,
				clientId));

		parametersBody.add(new BasicNameValuePair(OAuthConstants.REDIRECT_URI,
				redirectUri));

		if (isValid(scope)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE,
					scope));
		}

		/*
		 * if (isValid(promptValue)) { parametersBody.add(new BasicNameValuePair(
		 * promptKey, promptValue)); }
		 * 
		 * if (isValid(accessValue)) { parametersBody.add(new BasicNameValuePair(
		 * accessKey, accessValue)); }
		 */

		if (isValid(state)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.STATE,
					state));
		}

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		String accessToken = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			System.out.println("Code " + code);
			Map<String, String> map = handleURLEncodedResponse(response);

			if (OAuthConstants.HTTP_SEND_REDIRECT == code) {
				location = getHeader(response.getAllHeaders(),
						OAuthConstants.LOCATION_HEADER);
				if (location == null) {
					System.out
							.println("The endpoint did not pass in valid location header for redirect");
					throw new RuntimeException(
							"The endpoint did not pass in valid location header for redirect");
				}
			} else {
				System.out
						.println("Was expecting code 302 from endpoint to indicate redirect. Recieved httpCode "
								+ code);
				throw new RuntimeException(
						"Was expecting code 302 from endpoint to indicate redirect. Recieved httpCode "
								+ code);
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return location;
	}

	
	

	 public String getResponseStringFromConn(HttpURLConnection conn) throws IOException {

		BufferedReader reader;
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
		}

		return stringBuilder.toString();
	}

	 public JSONObject processResponse(int responseCode, String response) throws JSONException {

		JSONObject responseJson = new JSONObject();
		responseJson.put("responseCode", responseCode);

		if (response.equalsIgnoreCase("")) {
			responseJson.put("responseMsg", "");
		} else {
			responseJson.put("responseMsg", new JSONObject(response));
		}
		return responseJson;
	}


	public  String getAccessTokenForAuthGrant(String grantType, String authorizationCode) {
		HttpPost post = new HttpPost(
				"https://login.microsoftonline.com/ac26cf21-c02e-433a-8cca-237e1afccbd1/oauth2/v2.0/token");
		String scope = "https://graph.microsoft.com/.default";

		Map<String, String> map = new HashMap<String, String>();

		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

		parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE, grantType));

		parametersBody.add(new BasicNameValuePair(OAuthConstants.CODE, authorizationCode));

		parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID, "04812a6a-a5cf-4004-94fe-22a4a11c6134"));

		if (isValid("Y0ufVcvSAc4s.8ojYQb2_Ba1R80~2V.C3Z")) {
			parametersBody
					.add(new BasicNameValuePair(OAuthConstants.CLIENT_SECRET, "Y0ufVcvSAc4s.8ojYQb2_Ba1R80~2V.C3Z"));
		}

		parametersBody.add(new BasicNameValuePair(OAuthConstants.REDIRECT_URI,
				"https://localhost:8443/msal4jsample/getChatDetails"));

		if (isValid(scope)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE, scope));
		}

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		String accessToken = null;

		String refreshToken = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();

			map = handleResponse(response);
			accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
			refreshToken = map.get(OAuthConstants.REFRESH_TOKEN);


		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return accessToken;
	}

	public  String getAccessTokenForClientCredential(String grantType, String authorizationCode) {
		HttpPost post = new HttpPost(
				"https://login.microsoftonline.com/ac26cf21-c02e-433a-8cca-237e1afccbd1/oauth2/v2.0/token");
		String scope = "https://graph.microsoft.com/.default";

		Map<String, String> map = new HashMap<String, String>();

		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

		parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE, grantType));

		/*
		 * parametersBody.add(new BasicNameValuePair(OAuthConstants.CODE,
		 * authorizationCode));
		 */

		parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID, "04812a6a-a5cf-4004-94fe-22a4a11c6134"));

		if (isValid("Y0ufVcvSAc4s.8ojYQb2_Ba1R80~2V.C3Z")) {
			parametersBody
					.add(new BasicNameValuePair(OAuthConstants.CLIENT_SECRET, "Y0ufVcvSAc4s.8ojYQb2_Ba1R80~2V.C3Z"));
		}

		/*
		 * parametersBody.add(new BasicNameValuePair(OAuthConstants.REDIRECT_URI,
		 * "https://localhost:8443/msal4jsample/getChatDetails"));
		 */

		if (isValid(scope)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE, scope));
		}

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		String accessToken = null;

		String refreshToken = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();

			map = handleResponse(response);
			accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
			refreshToken = map.get(OAuthConstants.REFRESH_TOKEN);

			// System.out.println(refreshToken+"REFRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");

			/*
			 * if(!isValid(accessToken) && isValid(refreshToken)) { accessToken
			 * =refreshToken; map = refreshAccessToken(refreshToken); accessToken =
			 * map.get(OAuthConstants.ACCESS_TOKEN); }
			 */

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return accessToken;
	}

	public  boolean isValid(String str) {
		return (str != null && str.trim().length() > 0);
	}

	public  Map handleResponse(HttpResponse response) {
		String contentType = OAuthConstants.JSON_CONTENT;
		if (response.getEntity().getContentType() != null) {
			contentType = response.getEntity().getContentType().getValue();
		}
		if (contentType.contains(OAuthConstants.JSON_CONTENT)) {
			return handleJsonResponse(response);
		}
		if (contentType.contains(OAuthConstants.JSON_CONTENT)) {
			return handleJsonResponse(response);
		} else if (contentType.contains(OAuthConstants.URL_ENCODED_CONTENT)) {
			return handleURLEncodedResponse(response);
		} else if (contentType.contains(OAuthConstants.XML_CONTENT)) {
			return handleXMLResponse(response);
		} else {
			// Unsupported Content type
			throw new RuntimeException("Cannot handle " + contentType
					+ " content type. Supported content types include JSON, XML and URLEncoded");
		}

	}

	@SuppressWarnings("unchecked")
	public  Map handleJsonResponse(HttpResponse response) {
		Map<String, String> oauthLoginResponse = null;
		// String contentType =
		// response.getEntity().getContentType().getValue();
		try {
			oauthLoginResponse = (Map<String, String>) new JSONParser()
					.parse(EntityUtils.toString(response.getEntity()));
			// TODO Auto-generated catch block
		} catch (Exception e) {
			System.out.println("Could not parse JSON response");
			throw new RuntimeException(e.getMessage());
		}
		System.out.println();
		System.out.println("********** Response Received **********");
		for (Map.Entry<String, String> entry : oauthLoginResponse.entrySet()) {
			System.out.println(String.format("  %s = %s", entry.getKey(), entry.getValue()));
		}
		return oauthLoginResponse;
	}

	public  Map handleURLEncodedResponse(HttpResponse response) {
		Map<String, Charset> map = Charset.availableCharsets();
		Map<String, String> oauthResponse = new HashMap<String, String>();
		Set<Map.Entry<String, Charset>> set = map.entrySet();
		Charset charset = null;
		HttpEntity entity = response.getEntity();

		System.out.println();
		System.out.println("********** Response Received **********");

		for (Map.Entry<String, Charset> entry : set) {
			System.out.println(String.format("  %s = %s", entry.getKey(), entry.getValue()));
			if (entry.getKey().equalsIgnoreCase(HTTP.UTF_8)) {
				charset = entry.getValue();
			}
		}

		try {
			List<NameValuePair> list = URLEncodedUtils.parse(EntityUtils.toString(entity), Charset.forName(HTTP.UTF_8));
			for (NameValuePair pair : list) {
				System.out.println(String.format("  %s = %s", pair.getName(), pair.getValue()));
				oauthResponse.put(pair.getName(), pair.getValue());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Could not parse URLEncoded Response");
		}

		return oauthResponse;
	}

	public  Map handleXMLResponse(HttpResponse response) {
		Map<String, String> oauthResponse = new HashMap<String, String>();
		try {

			String xmlString = EntityUtils.toString(response.getEntity());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xmlString));
			Document doc = db.parse(inStream);

			System.out.println("********** Response Receieved **********");
			parseXMLDoc(null, doc, oauthResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception occurred while parsing XML response");
		}
		return oauthResponse;
	}

	public  void parseXMLDoc(Element element, Document doc, Map<String, String> oauthResponse) {
		NodeList child = null;
		if (element == null) {
			child = doc.getChildNodes();

		} else {
			child = element.getChildNodes();
		}
		for (int j = 0; j < child.getLength(); j++) {
			if (child.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				org.w3c.dom.Element childElement = (org.w3c.dom.Element) child.item(j);
				if (childElement.hasChildNodes()) {
					System.out.println(childElement.getTagName() + " : " + childElement.getTextContent());
					oauthResponse.put(childElement.getTagName(), childElement.getTextContent());
					parseXMLDoc(childElement, null, oauthResponse);
				}

			}
		}
	}

	/**
	 * Refresh an expired access token by using the refresh token
	 * 
	 * @param oauthDetails
	 * @return
	 */
	public  Map<String, String> refreshAccessToken(String refreshToken) {
		HttpPost post = new HttpPost(
				"https://login.microsoftonline.com/ac26cf21-c02e-433a-8cca-237e1afccbd1/oauth2/v2.0/token");
		String clientId = "04812a6a-a5cf-4004-94fe-22a4a11c6134";
		String clientSecret = "Y0ufVcvSAc4s.8ojYQb2_Ba1R80~2V.C3Z";
		String scope = "https://graph.microsoft.com/.default";
		Map<String, String> map = new HashMap<String, String>();

		if (!isValid(refreshToken)) {
			throw new RuntimeException("Please provide valid refresh token in config file");
		}

		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

		parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN));

		parametersBody.add(new BasicNameValuePair(OAuthConstants.REFRESH_TOKEN, refreshToken));

		if (isValid(clientId)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID, clientId));
		}

		if (isValid(clientSecret)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_SECRET, clientSecret));
		}

		if (isValid(scope)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE, scope));
		}

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();

			map = handleResponse(response);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return map;
	}

	
	
	public  String getHeader(Header[] headers, String name) {

		String header = null;
		if (headers != null) {
			for (Header h : headers) {
				if (h.getName().equalsIgnoreCase(name)) {
					header = h.getValue();
					break;
				}
			}
		}

		return header;

	}
}
