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
    public HttpRequests (){
        serverURL = "https://munch-server.herokuapp.com/";
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuffer response = new StringBuffer();
        try {
            URL url = new URL(serverURL + strings[0]);
            //Set up connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(strings[1]);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
<<<<<<< Updated upstream

            if (strings.length >= 4){
                con.setRequestProperty ("Authorization", "Bearer " +strings[3]);
            }

=======
            if (strings.length >= 4){
                con.setRequestProperty ("Authorization", "Bearer " +strings[3]);
            }
>>>>>>> Stashed changes
            if (strings[1].equals("POST")){
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = strings[2].getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            con.connect();

            int responseCode = con.getResponseCode();
            System.out.println(responseCode);

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
        //Check to make sure HTTP response is working
        System.out.println(response.toString());
        System.out.println("PRINTED");
        return response.toString();

    }

}