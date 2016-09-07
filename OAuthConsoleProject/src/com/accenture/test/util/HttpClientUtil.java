package com.accenture.test.util;

import static org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientUtil{
	private static String result;
	public static String post(String url, Map<String, String> params, String headerParams){
        return post(url, params, headerParams, "UTF-8");
    }
 
    public static String get(String url, Map<String, String> params, String headerParams){
        return get(url, params, headerParams, "UTF-8");
    }
    
	/**
     * POST 요청
     * @param url       요청할 url
     * @param params    파라메터
     * @param encoding  파라메터 Encoding
     * @return 서버 응답과 문자열
     */
    public static String post(String url, Map<String, String> params, String headerParams, String encoding){
        HttpClient client = new DefaultHttpClient();
         
        try{
            HttpPost post = new HttpPost(url);
            if(!headerParams.isEmpty()) {
            	post.addHeader("Authorization", headerParams);
            }
            System.out.println("POST : " + post.getURI());
             
            List<NameValuePair> paramList = convertParam(params);
            post.setEntity(new UrlEncodedFormEntity(paramList, encoding));
             
            ResponseHandler<String> rh = new BasicResponseHandler();
    		
            //ssl 서버 인증 무시
            TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                @Override
                    public boolean isTrusted(X509Certificate[] certificate, String authType) {
                        return true;
                    }
                };
            SSLSocketFactory sf = new SSLSocketFactory(acceptingTrustStrategy, ALLOW_ALL_HOSTNAME_VERIFIER);
            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 9443, sf));
                
            return client.execute(post, rh);
        }catch(Exception e){
        	result = e.toString();
        }finally{
            client.getConnectionManager().shutdown();
        }
         
        return result;
    }
     
    
    /**
     * GET 요청
     * POST 와 동일
     */
    public static String get(String url, Map<String, String> params, String headerParams, String encoding){
        HttpClient client = new DefaultHttpClient();
 
        try{
            List<NameValuePair> paramList = convertParam(params);
            HttpGet get = new HttpGet(url+"?"+URLEncodedUtils.format(paramList, encoding));
            if(!headerParams.isEmpty()) {
            	get.addHeader("Authorization", headerParams);
            }
          
            System.out.println("GET : " + get.getURI());
             
            ResponseHandler<String> rh = new BasicResponseHandler();
             
            //ssl 서버 인증 무시
            TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
                public boolean isTrusted(X509Certificate[] certificate, String authType) {
                    return true;
                }
            };
            SSLSocketFactory sf = new SSLSocketFactory(acceptingTrustStrategy, ALLOW_ALL_HOSTNAME_VERIFIER);
            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 9443, sf));
         
            return client.execute(get, rh);
        }catch(Exception e){
        	result = e.toString();
        }finally{
            client.getConnectionManager().shutdown();
        }
         
        return result;
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
