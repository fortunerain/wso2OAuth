package com.accenture.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthClientResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.commons.codec.binary.Base64;

import com.accenture.test.model.OAuthResponseModel;
import com.accenture.test.util.HttpClientUtil;
import com.accenture.test.util.JsonUtil;

public class MainController {
	private static String accessToken;
	private static String result;
	private static String consumerKey = "G3Jf2p4VERrSAfd66eNdyWgIzeAa";
	private static String consumerSecret = "iH0PVTwSQfVLT4HRJfvFUoMr2pEa";
	
	
	public static void main(String[] args) {
//		Using Oauth library
//		accessToken = getAccessTokenByOauth2Lib();
		
		//Using httpClient
		OAuthResponseModel oAuthResponseModel = getAccessTokenByHttpClient(); 
		accessToken = oAuthResponseModel.getAccess_token();
		String expireTime = oAuthResponseModel.getExpires_in();
		
		//api calls
		String url = "http://54.169.52.124:8280/comment/list/1.0.0";
		Map<String, String> params = new HashMap<String, String>();
		params.put("artworkid", "1");
		result = callAPI(url, params);
		
		System.out.println("accessToken : "+accessToken);
		System.out.println("expireTime : "+expireTime);
		System.out.println("result : "+result);
	}
	
	private static String callAPI(String url, Map<String, String> params) {
		
		String headerParams = "Bearer "+accessToken;
		String response = HttpClientUtil.get(url, params, headerParams);
		return response;
	}
	
	/*
	 * Using httpClient 
	 */
	private static OAuthResponseModel getAccessTokenByHttpClient() {
		//Endpoint for authentication
		String url = "https://54.169.52.124:9443/oauth2/token";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", OAuth2Constants.OAUTH2_GRANT_TYPE_CLIENT_CREDENTIALS);
		
		//Base64 is based on key combinations
		byte[] encodedBytes = Base64.encodeBase64((consumerKey+":"+consumerSecret).getBytes());
		String authorization = new String(encodedBytes);
		String headerParams = "Bearer "+authorization;
		
		String response = HttpClientUtil.post(url, params, headerParams);
		return JsonUtil.parseJsonString(response);
	}
	
	/*
	 * Using Oauth library 
	 */
	private String getAccessTokenByOauth2Lib() {
		//Endpoint for authentication
		String accessEndpoint = "https://localhost:9443/oauth2/token";
		try {
			
			OAuthClientRequest accessRequest = null;
			accessRequest = OAuthClientRequest.tokenLocation(accessEndpoint)
			        .setGrantType(GrantType.CLIENT_CREDENTIALS)
			        .setClientId(consumerKey)
			        .setClientSecret(consumerSecret)
//			        .setScope(scope)
			        .buildBodyMessage();
			
			// Creates OAuth client that uses custom http client under the hood
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			OAuthClientResponse oAuthResponse = oAuthClient.accessToken(accessRequest);
			accessToken = oAuthResponse.getParam(OAuth2Constants.ACCESS_TOKEN);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accessToken;
	}

}
