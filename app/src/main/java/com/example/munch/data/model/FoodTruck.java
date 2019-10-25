package com.example.munch.data.model;

import com.example.munch.HttpRequests;

import org.json.JSONException;
import org.json.JSONObject;

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

    public FoodTruck(String token, String name, String address, String[][] hours, String[] photos ){
        JSONObject truck = new JSONObject();
        try {
            truck.put("name", name);
            truck.put("address", address);
            truck.put("photos", photos);
            truck.put("hours", hours);
        } catch (JSONException ex) {
            System.out.println("Truck Creation Failed");
        }

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
