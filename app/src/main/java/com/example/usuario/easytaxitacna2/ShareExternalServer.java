package com.example.usuario.easytaxitacna2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShareExternalServer {

	public String shareRegIdWithAppServer(final Context context,
			final String regId) {

        JSONArray values = new JSONArray();
        values.put("user1");


        JSONObject json = new JSONObject();

          try {
            json.put("user","user3");
            json.put("type","android");
            json.put("token",regId);

        }catch (Exception e)
        {}



        String result = "";
		//Map<String, String> paramsMap = new HashMap<String, String>();
		//paramsMap.put("regId", regId);
		try {
			URL serverUrl = null;
			try {
				serverUrl = new URL(Config.APP_SERVER_URL);
			} catch (MalformedURLException e) {
				Log.e("AppUtil", "URL Connection Error: "
						+ Config.APP_SERVER_URL, e);
				result = "Invalid URL: " + Config.APP_SERVER_URL;
			}

			//}
			//String body = postBody.toString();
            byte[] bytes =  json.toString().getBytes();


			//byte[] bytes = body.getBytes();
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
							+ regId;
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
