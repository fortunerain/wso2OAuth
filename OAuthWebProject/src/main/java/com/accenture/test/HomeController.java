package com.accenture.test;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthClientResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.accenture.test.model.OAuthResponseModel;
import com.accenture.test.util.HttpClientUtil;
import com.accenture.test.util.JsonUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static String accessToken;
	private static String result;
	private static String consumerKey = "G3Jf2p4VERrSAfd66eNdyWgIzeAa";
	private static String consumerSecret = "iH0PVTwSQfVLT4HRJfvFUoMr2pEa";
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate );
		
//		Using Oauth library
//		accessToken = getAccessTokenByOauth2Lib();
		
		//Using httpClient
		OAuthResponseModel oAuthResponseModel = getAccessTokenByHttpClient(); 
		accessToken = oAuthResponseModel.getAccess_token();
		String expireTime = oAuthResponseModel.getExpires_in();
		
		//api calls
//		String url = "http://localhost:8280/weather/1.0.0";
		String url = "http://54.169.52.124:8280/comment/list/1.0.0";
//		String url = "http://localhost:8280/weather";
		Map<String, String> params = new HashMap<String, String>();
//		params.put("q", "London");
		params.put("artworkid", "1");
		result = callAPI(url, params);
		
		model.addAttribute("accessToken", accessToken);
		model.addAttribute("expireTime", expireTime);
		model.addAttribute("result", result);
		
		return "home";
	}
	
	private String callAPI(String url, Map<String, String> params) {
		
		String headerParams = "Bearer "+accessToken;
		String response = HttpClientUtil.get(url, params, headerParams);
		return response;
	}
	
	/*
	 * Using httpClient 
	 */
	private OAuthResponseModel getAccessTokenByHttpClient() {
		//Endpoint for authentication
		String url = "https://54.169.52.124:9443/oauth2/token";
//		String url = "https://localhost:9443/oauth2/token";
		
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
