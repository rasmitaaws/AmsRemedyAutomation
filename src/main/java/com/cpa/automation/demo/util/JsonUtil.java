package com.cpa.automation.demo.util;

import java.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {

	String jsonString = "";

	public static String getMessages(String jsonString) {

		JSONObject jsonObject = new JSONObject(jsonString);

		
		  JSONObject jsonObject2 = jsonObject.getJSONObject("responseMsg");
		// JSONObject jsonObject1=jsonObject.getJSONObject("value");

		JSONArray jsonArray = jsonObject2.getJSONArray("value");

		
		StringBuilder stringBuilder=new StringBuilder();
		for (int i = 0; i < jsonArray.length(); i++)

		{
			JSONObject objectInArray = jsonArray.getJSONObject(i);

			String[] elementNames = JSONObject.getNames(objectInArray);
			System.out.printf("%d ELEMENTS IN CURRENT OBJECT:\n", elementNames.length);

			for (String elementName : elementNames) {
				if (elementName.equals("createdDateTime")) {

					if (DateUtil.getDateValueFromStringJson(
							objectInArray.getString("createdDateTime")) == (LocalDateTime.now().getDayOfYear()))
						;
					{
						
						
						Object objectInArray2 = objectInArray.get("from");

						JSONObject fromjsonObject3=new JSONObject(objectInArray2.toString());
						Object userObject3=fromjsonObject3.get("user");

						String displayName="";
						JSONObject usersonObject=new JSONObject(userObject3.toString());

						displayName=usersonObject.getString("displayName");

						
						Object objectInArray1 = objectInArray.get("body");
						
						//Object objectInArray1 = objectInArray.get("user");

						JSONObject jsonObject3 = new JSONObject(objectInArray1.toString());
						
						String contentType="";
						String jsonString1="";
						if (jsonObject3.has("contentType")) {
							contentType=jsonObject3.getString("contentType");
						}

						if (jsonObject3.has("content") && "text".equals(contentType)) {
							jsonString1 = jsonObject3.getString("content");
							stringBuilder.append(displayName+":"); //for user name 
							stringBuilder.append("\\n");
							stringBuilder.append(jsonString1);
							stringBuilder.append("\\n");
							
						}
					}

				}

			}
		}

		return stringBuilder.toString();
	}
}
