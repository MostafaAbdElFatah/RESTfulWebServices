package com.mostafa.fci.restfulwebservices;

import android.net.Proxy;
import android.net.wifi.hotspot2.pps.Credential;
import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Challenge;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * Created by FCI on 2018-06-25.
 */

public class URLManager {


    /**
     * get Data by AsyncTask with HttpURLConnection without Authentication
     * */
    public static String  getDatabyHttpURLConnection(String uri){

        BufferedReader reader = null;
        try {

            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder stringBuilder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line ;
            while ( (line = reader.readLine() ) != null ){
                stringBuilder.append(line+"\n");
            }

            return stringBuilder.toString();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

        }
    }

    /**
     * get Data by AsyncTask with HttpURLConnection with Authentication
     * */
    public static String  getDatabyHttpURLConnection(String uri,String userName ,String pass) {
        BufferedReader reader = null;
        HttpURLConnection con = null;
        try {

            URL url = new URL(uri);

            byte[] loginBytes = (userName+":"+pass).getBytes();
            StringBuilder loginBuilder = new StringBuilder()
                    .append("Basic ")
                    .append(Base64.encodeToString(loginBytes,Base64.DEFAULT));

            con = (HttpURLConnection) url.openConnection();
            StringBuilder stringBuilder = new StringBuilder();

            con.addRequestProperty("Authorization",loginBuilder.toString());

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line ;
            while ( (line = reader.readLine() ) != null ){
                stringBuilder.append(line+"\n");
            }

            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            try {
                int state = con.getResponseCode();
                con.disconnect();
            }catch (Exception ex){
                e.printStackTrace();
            }
            return null;
        }finally {

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

        }
    }
    /**
     * get Data by AsyncTask with HttpURLConnection with Authentication
     * */
    public static String  getDatabyHttpURLConnection(RequestPackage params) {
        BufferedReader reader = null;
        HttpURLConnection con = null;
        String stringURI = params.getUri();

        if (params.getMethod().equals("GET")){
            stringURI += "?" + params.getEncodeParams();
        }
        try {

            URL url = new URL(stringURI);
            con = (HttpURLConnection) url.openConnection();
            StringBuilder stringBuilder = new StringBuilder();

            con.setRequestMethod(params.getMethod());

            /**
            // if you want send params as json format
            JSONObject jsonObject = new JSONObject(params.getParams());
            String jsonParams = "params="+ jsonObject.toString();
            if (params.getMethod().equals("POST")){
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(jsonParams);
                writer.flush();
            }
            */
            if (params.getMethod().equals("POST")){

                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(params.getEncodeParams());
                writer.flush();

            }

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line ;
            while ( (line = reader.readLine() ) != null ){
                stringBuilder.append(line+"\n");
            }

            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

        }
    }


    /**
     * call without params
     * */
    public static String  getDatabyOKHttpClient(String uri){

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(uri);
        final Request request = builder.build();

        /**
         * or Synchronous Call
         * */

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        /**
         * or Asynchronous Call
         * */
        /*
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String myResponse = response.body().string();

                 MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // do update in UI
                        txtString.setText(myResponse);
                    }
                });
            }
        });*/
        return null;
    }
    /**
     * call with Authentication
     * */
    public static String  getDatabyOKHttpClient(String uri,String username , String pass) {

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(uri);
        String credential = Credentials.basic(username , pass);
        final Request request = builder
                .header("Authorization",credential).build();

        /**
         * or Synchronous Call
         * */

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * call with post params
     * */
    public static String  getDatabyOKHttpClient(RequestPackage requestPackage) {

        OkHttpClient client = new OkHttpClient();

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        JSONObject postdata = new JSONObject(requestPackage.getParams());
        /* or
        try {
            postdata.put("UserName", "Abhay Anand");
            postdata.put("Email", "anand.abhay1910@gmail.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
        Request request = new Request.Builder()
                .url(requestPackage.getUri())
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        /**
         * or Synchronous Call
         * */

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*************************************/
}
