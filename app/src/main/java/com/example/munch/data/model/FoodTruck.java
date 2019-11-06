package com.example.munch.data.model;

import com.example.munch.HttpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class FoodTruck{

    String id;
    String name;
    String address;
    String lat;
    String lng;
    String owner;
    String[][] hours;
    Review[] reviews;
    String phoneNumber;
    String website;
    String description;
    String[] tags;
    Float avgRating;

    public FoodTruck(String token, String name, String address, String[][] hourss, String[] photos ){
        JSONObject truck = new JSONObject();
        try {
            truck.put("name", name);
            truck.put("address", address);
            truck.put("photos", new JSONArray(photos));
            truck.put("hours", new JSONArray(hours));
        } catch (JSONException ex) {
            System.out.println("Truck Creation Failed");
        }
        /*String[] photosT = {"https://images.101cookbooks.com/andrea_nguyen.jpg?w=680&auto=format"};
        String[][] hours = new String[7][2];
        hours[0][0] = "07:00";
        hours[1][0] = hours[0][0];
        hours[2][0] = hours[0][0];
        hours[3][0] = hours[0][0];
        hours[4][0] = hours[0][0];
        hours[5][0] = hours[0][0];
        hours[6][0] = hours[0][0];
        hours[0][1] = "12:00";
        hours[1][1] = hours[0][1];
        hours[2][1] = hours[0][1];
        hours[3][1] = hours[0][1];
        hours[4][1] = hours[0][1];
        hours[5][1] = hours[0][1];
        hours[6][1] = hours[0][1];*/

        try {
            truck.put("name", "Andrea's Test");
            truck.put("address", "an address");
            truck.put("photos", new JSONArray(photos));
            truck.put("hours", new JSONArray(hours));
        } catch (JSONException ex) {
            System.out.println("Truck Creation Failed");
        }

        String temp = truck.toString();
        HttpRequests httpRequests = new HttpRequests();
        httpRequests.execute("foodtrucks", "POST", truck.toString(), token);
        String response = null;
        try {
            response = httpRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        id = response;
        System.out.println(id);
    }
}
