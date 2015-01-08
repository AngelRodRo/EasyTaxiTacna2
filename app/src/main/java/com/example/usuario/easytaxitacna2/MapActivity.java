package com.example.usuario.easytaxitacna2;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MapActivity extends ActionBarActivity {

    AsyncTask<Void, Void, String> shareRegidTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        shareRegidTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = SendServerMessage();
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                shareRegidTask = null;
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        };
        shareRegidTask.execute(null, null, null);
    }


    public String SendServerMessage(){

        String SERVER_URL = "https://aqueous-escarpment-1930.herokuapp.com/SEND";


        JSONArray values = new JSONArray();
        values.put("user1");
        //JSON PRINCIPAL (MENSAJE)
        JSONObject princjs = new JSONObject();

        //Partes del JSON
        JSONObject androidjs = new JSONObject();
        JSONObject datajs = new JSONObject();
        JSONObject iosjs = new JSONObject();

        try {
            datajs.put("message", "Este es mi mensaje :D");

            androidjs.put("collapseKey", "optional");
            androidjs.put("data", datajs);

            iosjs.put("badge", 0);
            iosjs.put("alert", "Your message here");
            iosjs.put("sound", "soundName");


            princjs.put("users", values);
            princjs.put("android", androidjs);
            princjs.put("ios", iosjs);
        }
        catch (Exception e){

        }



        String result = "";

        try {
            URL serverUrl = null;
            try {
                serverUrl = new URL(SERVER_URL);
            } catch (MalformedURLException e) {
                Log.e("AppUtil", "URL Connection Error: "
                        + SERVER_URL, e);
                result = "Invalid URL: " + SERVER_URL;
            }


            byte[] bytes =  princjs.toString().getBytes();

            HttpURLConnection httpCon = null;
            try {
                httpCon = (HttpURLConnection) serverUrl.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setUseCaches(false);
                httpCon.setFixedLengthStreamingMode(bytes.length);
                httpCon.setRequestMethod("POST");
                httpCon.setRequestProperty("Content-Type","application/json");
                httpCon.connect();

                OutputStream out = httpCon.getOutputStream();
                out.write(bytes);
                out.close();

                int status = httpCon.getResponseCode();
                if (status == httpCon.HTTP_OK) {
                    result = "RegId shared with Application Server. RegId: "
                           ;
                } else {
                    result = "Post Failure." + " Status: " + status;
                }
            } finally {
                if (httpCon != null) {
                    httpCon.disconnect();
                }
            }

        } catch (IOException e) {
            result = "Post Failure. Error in sharing with App Server.";
            Log.e("AppUtil", "Error in sharing with App Server: " + e);
        }
        return result;
    }


}
