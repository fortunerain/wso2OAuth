package com.accenture.test.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.accenture.test.model.OAuthResponseModel;
import com.google.gson.Gson;

public class JsonUtil {
	public static OAuthResponseModel parseJsonString(String jsonString) {
		OAuthResponseModel responseModel = new OAuthResponseModel();
		Gson gson = new Gson();
		
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			responseModel = gson.fromJson(jsonObject.toString(), OAuthResponseModel.class);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        	
		return responseModel;
	}
}
