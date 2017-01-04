package com.example.faizan.notextzone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SlackAuthentication extends AppCompatActivity {

    private static String addressCode = "https://slack.com/oauth/authorize";
    private static String addressToken = "https://slack.com/api/oauth.access";
    private static String scope = "dnd:write";

    private WebView web;
    protected SharedPreferences savedMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slack_authentication);

        savedMessages = getSharedPreferences("savedMessages", Context.MODE_APPEND);
        web = (WebView) findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);
        Log.d("SlackAuthen_onCreate", "LoadUrl: " + addressCode + "?client_id=" + SlackConstants.CLIENT_ID + "&scope=" + scope);
        web.loadUrl(addressCode + "?client_id=" + SlackConstants.CLIENT_ID + "&scope=" + scope);
        Log.d("SlackAuthen_onCreate", "AfterLoadUrl");

        web.setWebViewClient(
                new WebViewClient() {

                    boolean authComplete;

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        Log.d("SlackAuthen_WVC_OPS", "atOnPageStarted");
                        Log.d("SlackAuthen_WVC_OPS", "onStartedUrl: " + url);
                    }

                    String authCode = "";

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        Log.d("SlackAuthen_WVC_OPF", "atOnPageFinished");
                        super.onPageFinished(view, url);
                        Log.d("SlackAuthen_WVC_OPF", "onFinshedUrl: " + url);
                        if (url.contains("?code=") && authComplete!=true) {
                            Uri uri = Uri.parse(url);
                            authCode = uri.getQueryParameter("code");
                            Log.d("SlackAuthen_WVC_OPF", "CODE: " + authCode);
                            authComplete = true;

                            SharedPreferences.Editor smEdit = savedMessages.edit();
                            smEdit.putString("code", authCode);
                            smEdit.apply();
                            new TokenGet().execute();

                            Log.d("SlackAuthen_WVC_OPF", "toMain");
                        } else if(url.contains("error=access_denied")) {
                            Log.d("SlackAuthen_WVC_OPF", "ACCESS DENIED HERE");
                            authComplete = true;
                            Toast.makeText(SlackAuthentication.this, "Error Occured", Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor smEdit = savedMessages.edit();
                            smEdit.putString("access_token", null);
                            smEdit.putString("scope", null);
                            smEdit.putBoolean("slackAuthenticated", true);
                            smEdit.apply();

                            Log.d("SlackAuthen_WVC_OPF", "toMain");
                            Intent returnToMain = new Intent();
                            setResult(Activity.RESULT_CANCELED);
                            SlackAuthentication.this.finish();
                        }
                    }

                }
        );

    }

    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String code;

        @Override
        protected void onPreExecute() {
            Log.d("SlackAuth_TG_OPrE", "onPreExecute");
            super.onPreExecute();
            pDialog = new ProgressDialog(SlackAuthentication.this);
            pDialog.setMessage("Contacting Slack... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            code = savedMessages.getString("code", "");
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            GetSlackAccessToken jParser = new GetSlackAccessToken();
            JSONObject json = jParser.getToken(addressToken, SlackConstants.CLIENT_ID, SlackConstants.CLIENT_SECRET, code);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Log.d("POST EXECUTE", "post exxxx");
            pDialog.dismiss();
            if (json != null){
                try {
                    String token = json.getString("access_token");
                    String scope = json.getString("scope");

                    Log.d("SlackAuth_TG_OPoE", "Token: " + token);
                    Log.d("SlackAuth_TG_OPoE", "scope: " + scope);

                    SharedPreferences.Editor smEdit = savedMessages.edit();
                    smEdit.putString("access_token", token);
                    smEdit.putString("scope", scope);
                    smEdit.putBoolean("slackAuthenticated", true);
                    smEdit.apply();

                    Intent returnToMain = new Intent();
                    setResult(Activity.RESULT_OK, returnToMain);
                    SlackAuthentication.this.finish();
                } catch (JSONException e){
                    Log.e("JSONException", e.getMessage());
                }
            } else {
                Toast.makeText(SlackAuthentication.this, "Network Error", Toast.LENGTH_LONG).show();

                Intent returnToMain = new Intent();
                setResult(Activity.RESULT_CANCELED, returnToMain);
                SlackAuthentication.this.finish();
            }
        }
    }
}
