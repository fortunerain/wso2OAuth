package com.accenture.nmh.sampleproject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.accenture.nmh.sampleproject.model.OAuthResponseModel;
import com.accenture.nmh.sampleproject.util.HttpClientCustomThread;
import com.accenture.nmh.sampleproject.util.JsonUtil;

import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    private static String accessToken;
    private static String result;
    private static String consumerKey = "G3Jf2p4VERrSAfd66eNdyWgIzeAa";
    private static String consumerSecret = "iH0PVTwSQfVLT4HRJfvFUoMr2pEa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OAuthResponseModel model = getAccessTokenByHttpClient();
        String accesstoken = model.getAccess_token();
        System.out.println("accesstoken : "+accesstoken);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static OAuthResponseModel getAccessTokenByHttpClient() {
        //Endpoint for authentication
        String url = "https://54.169.52.124:9443/oauth2/token";

        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "client_credentials");

        //Base64 is based on key combinations
        byte[] encodedBytes = Base64.encodeBase64((consumerKey + ":" + consumerSecret).getBytes());
        String authorization = new String(encodedBytes);
        String headerParams = "Bearer "+authorization;


        HttpClientCustomThread thread = new HttpClientCustomThread("post", url, params, headerParams);

        thread.start();

        try {
            thread.join();
        } catch(Exception e) {
            e.printStackTrace();
        }

        String jsonString = thread.getResult();
        Log.i("===API URL : ", url);
        Log.i("===API RESPONSE : ", jsonString);
        return (OAuthResponseModel) JsonUtil.parseJsonString(jsonString, OAuthResponseModel.class);
    }
}
