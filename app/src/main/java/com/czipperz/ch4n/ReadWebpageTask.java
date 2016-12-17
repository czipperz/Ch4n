package com.czipperz.ch4n;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ReadWebpageTask extends AsyncTask<String, Void, JSONObject> {
    private Callback<JSONObject> activity;
    public ReadWebpageTask(Callback<JSONObject> activity) { this.activity = activity; }

    @Override
    protected JSONObject doInBackground(String... urls) {
        try {
            return new JSONObject(download(urls[0]));
        } catch (OutOfMemoryError e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String download(String url) throws Exception {
        String total = "";
        URL website = new URL(url);
        URLConnection con = website.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        try {
            char[] buffer = new char[1024];
            int num;
            while ((num = reader.read(buffer)) != -1) {
                total = total.concat(new String(buffer, 0, num));
            }
        } catch(Exception e) {
            reader.close();
            throw e;
        }
        reader.close();
        return total;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        activity.useInput(result);
    }
}
