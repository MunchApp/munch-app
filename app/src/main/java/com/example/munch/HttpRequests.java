package com.example.munch;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequests extends AsyncTask<String, Void, String> {

    String serverURL;
    int statusCode;
    public HttpRequests (){
        statusCode = 0;
    }

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
            con.setRequestProperty("Content-Type", "application/json");
            if (strings.length >= 4){
                String token = strings[3];
                con.setRequestProperty ("Authorization", "Bearer " +token);
            }

            //Pass json object with parameters
            if (method.equals("POST") || method.equals("PUT")){
                String json = strings[2];
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            //connect
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