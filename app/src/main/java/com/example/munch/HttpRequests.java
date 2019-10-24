package com.example.munch;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class HttpRequests {
    String serverURL = "";


    public void getRequest(Context context, String email, String[] getFields){

        String fields = "";
        for (String f: getFields){
            fields+= f +",";
        }
        fields = fields.substring(0,fields.length()-1);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = serverURL+"/graphql?query={user(email:\""+ email + "\"){" + fields +"}}" ;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
