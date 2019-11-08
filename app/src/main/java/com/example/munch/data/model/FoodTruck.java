package com.example.munch.data.model;

import com.example.munch.HttpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FoodTruck{

    String id;
    Boolean status;
    String name;
    String address;
    float lat;
    float lng;
    String owner;
    String[][] hours;
    ArrayList<String> photos;
    ArrayList<String> reviews;
    String phoneNumber;
    String website;
    String description;
    ArrayList<String> tags;
    Float avgRating;

    //Constructor for already existing Trucks in database
    public FoodTruck(String truckId){
        getTruck(truckId);
    }

    //Constructor for adding new Truck to database
    public FoodTruck(String token, String name, String address, String[][] hours, String[] photos, String owner ){

        //Creating JSON Object to pass in Request
        JSONObject truck = new JSONObject();
        try {
            truck.put("name", name);
            truck.put("address", address);
            truck.put("photos", new JSONArray(photos));
            truck.put("hours", new JSONArray(hours));
            //truck.put("owner", owner);
        } catch (JSONException ex) {
            System.out.println("Truck Creation Failed");
        }


        //Make httpRequest
        HttpRequests httpRequests = new HttpRequests();
        httpRequests.execute("foodtrucks", "POST", truck.toString(), token);
        String response = null;
        try {
            response = httpRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        //Return response
        id = response;
        int statusCode = httpRequests.getStatusCode();
        getTruck(id);
    }

    //Get existing truck from database
    private void getTruck (String truckId){
        HttpRequests getTruckRequests = new HttpRequests();
        getTruckRequests.execute("foodtrucks/" + truckId, "GET");
        String response = null;
        try {
            response = getTruckRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try{
            JSONObject jsonTruck = new JSONObject(response);
            jsonToFoodTruck(jsonTruck);
        }catch (JSONException e){

        }
    }
    //Setters
    public void setAllValues (String id,Boolean status, String name, String address, float lat, float lng, String owner, String[][] hours,
                              ArrayList<String> photos, ArrayList<String> reviews, String phoneNumber, String website, String description,
                              ArrayList<String> tags, float avgRating){
        // todo edit values
        this.id = id;
        this.status = status;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.owner = owner;
        this.hours = hours;
        this.photos = photos;
        this.reviews = reviews;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.description = description;
        this.tags = tags;
        this.avgRating = avgRating;
    }


    //Getters
    public String getId() {
        return id;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    private void jsonToFoodTruck (JSONObject jsonTruck) {
        //todo implement this function
    }

    private JSONObject foodTruckToJson () {
        //todo implement this function and add parameters
        return null;
    }
}
