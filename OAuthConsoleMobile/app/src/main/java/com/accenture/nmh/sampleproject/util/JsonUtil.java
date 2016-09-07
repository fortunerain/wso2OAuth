package com.accenture.nmh.sampleproject.util;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
	@SuppressWarnings("unchecked")
	public static Object parseJsonString(String jsonString, Class model) {
		Object resultObj = new Object();
		Gson gson = new Gson();
		
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			resultObj = gson.fromJson(jsonObject.toString(), model);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        	
		return resultObj;
	}
}
