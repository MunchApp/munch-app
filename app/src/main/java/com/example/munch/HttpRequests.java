package com.example.munch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.android.internal.http.multipart.MultipartEntity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpRequests extends AsyncTask<String, Void, String> {

    int statusCode;
    public HttpRequests (){
        statusCode = 0;
    }
    String boundary = "***" + System.currentTimeMillis() + "***";
    String twoHyphens = "--";
    String crlf = "\r\n";
    String output = "";

    @Override
    protected String doInBackground(String... strings) {
        StringBuffer response = new StringBuffer();
        try {

            String method = strings[1];
            String route = strings[0];

            URL url = new URL(route);

            //Set up connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setDoInput(true);

            //Pass token

            if ( strings.length != 5) {
                con.setRequestProperty("Content-Type", "application/json");
            }
            else {
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" +boundary);
            }

            if (strings.length >= 4){
                String token = strings[3];
                con.setRequestProperty ("Authorization", "Bearer " +token);
            }

            //Pass json object with parameters
            if ((method.equals("POST") || method.equals("PUT"))&&strings.length !=5){
                String json = strings[2];
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            if(strings.length == 5){
                byte[] decodedByte = Base64.decode(strings[4], 0);
                DataOutputStream request = new DataOutputStream(con.getOutputStream());
                request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"image\"" + crlf);
                request.writeBytes(crlf);
                request.write(decodedByte);
                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary + crlf);

            }

            //connect
            System.out.println(con.getOutputStream().toString());
            con.connect();

            //get error code
            int responseCode = con.getResponseCode();
            statusCode = responseCode;

            //get response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();

    }

    public int getStatusCode(){
        return statusCode;
    }



}