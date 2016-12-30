package com.example.faizan.notextzone;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GetSlackAccessToken {
    static InputStream is;
    static JSONObject jOdj;
    static String json;
    List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
    DefaultHttpClient httpClient;
    HttpPost httpPost;

    public GetSlackAccessToken(){
        Log.i("GetSlackAccessToken", "Constructor");
    }

    public JSONObject getToken(String address, String client_id, String client_secret, String code){
        try {
            httpClient = new DefaultHttpClient();
            httpPost =  new HttpPost(address);

            queryParams.add(new BasicNameValuePair("client_id", client_id));
            queryParams.add(new BasicNameValuePair("client_secret", client_secret));
            queryParams.add(new BasicNameValuePair("code", code));


            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(queryParams));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e){
            Log.e("UnsupportedEncodingE", e.getMessage());
        } catch (IOException e){
            Log.e("IOException", e.getMessage());
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            json = sb.toString();
            Log.i("JSONStr", json);
        } catch(IOException e){
            Log.e("IOException", e.getMessage());
        }

        try{
            jOdj = new JSONObject(json);
        } catch (JSONException e){
            Log.e("IOException", e.getMessage());
        }
        return jOdj;
    }
}
