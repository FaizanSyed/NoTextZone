package com.example.faizan.notextzone;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SlackSnooze {

    public static void set() {
        new SetSnooze().execute();
    }

    public static void end() {
        new EndSnooze().execute();
    }

    private static class SetSnooze extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // Check if Slack API Token has been generated
            if (SlackConstants.SLACK_TOKEN != null) {
                Log.d("Slack_token", SlackConstants.SLACK_TOKEN);
                // Create HTTP Client and Post
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://slack.com/api/dnd.setSnooze");

                try {
                    // Add token and minutes
                    List<NameValuePair> values = new ArrayList<NameValuePair>(2);
                    values.add(new BasicNameValuePair("token", SlackConstants.SLACK_TOKEN));
                    values.add(new BasicNameValuePair("num_minutes", "720"));
                    httppost.setEntity(new UrlEncodedFormEntity(values));

                    // Execute HTTP Post
                    HttpResponse httpResponse = httpclient.execute(httppost);
                    Log.d("Slack_HTTPRes", httpResponse.toString());

                } catch (ClientProtocolException e) {
                    Log.d("ClientProtocolException", e.getMessage());
                } catch (IOException e) {
                    Log.d("IOException", e.getMessage());
                }
            }
            return null;
        }
    }

    private static class EndSnooze extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            // Check if Slack API Token has been generated
            if (SlackConstants.SLACK_TOKEN != null) {
                // Create HTTP Client and Post
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://slack.com/api/dnd.endSnooze");

                try {
                    // Add token
                    List<NameValuePair> values = new ArrayList<NameValuePair>(1);
                    values.add(new BasicNameValuePair("token", SlackConstants.SLACK_TOKEN));
                    httppost.setEntity(new UrlEncodedFormEntity(values));

                    // Execute HTTP Post
                    HttpResponse httpResponse = httpclient.execute(httppost);

                } catch (ClientProtocolException e) {
                    Log.d("ClientProtocolException", e.getMessage());
                } catch (IOException e) {
                    Log.d("IOException", e.getMessage());
                }
            }
            return null;
        }
    }
}
