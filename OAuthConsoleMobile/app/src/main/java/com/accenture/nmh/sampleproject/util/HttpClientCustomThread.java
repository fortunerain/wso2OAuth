package com.accenture.nmh.sampleproject.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;


public class HttpClientCustomThread extends Thread {
	private String TAG = "CustomThread";
	private String API_URL;
	private String jsonStr = "";
	private String headerParams = "";
	private String METHOD = "";
	private Map<String, String> params;

	public HttpClientCustomThread(String method, String url, Map<String, String> params, String headerParams) {
		this.METHOD = method;
		this.API_URL = url;
        this.params = params;
        this.headerParams = headerParams;
	}
	
	public String getResult() { 
		return jsonStr.toString();
	}
	
	@Override
	public void run() {
        HttpClient client = MySSLSocketFactory.getNewHttpClient();
        ResponseHandler<String> rh = new BasicResponseHandler();
        try{
            if("GET".equals(METHOD.toUpperCase())){
                List<NameValuePair> paramList = convertParam(params);
                HttpGet get = new HttpGet(API_URL+"?"+ URLEncodedUtils.format(paramList, "UTF-8"));
                if(!headerParams.isEmpty()) {
                    get.addHeader("Authorization", headerParams);
                }
                System.out.println("GET : " + get.getURI());

                jsonStr = client.execute(get, rh);
            }else if("POST".equals(METHOD.toUpperCase())){
                HttpPost post = new HttpPost(API_URL);
                if(!headerParams.isEmpty()) {
                    post.addHeader("Authorization", headerParams);
                }
                System.out.println("POST : " + post.getURI());

                List<NameValuePair> paramList = convertParam(params);
                post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                jsonStr = client.execute(post, rh);
            }else{
                Log.e(TAG, "METHOD is wrong or null");
            }
        }catch(Exception e){
            Log.e(TAG, "Error in http connection "+e.toString());
        }finally{
            client.getConnectionManager().shutdown();
        }
	}
	
    private static List<NameValuePair> convertParam(Map<String, String> params){
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        Iterator<String> keys = params.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
        }

        return paramList;
    }
}
