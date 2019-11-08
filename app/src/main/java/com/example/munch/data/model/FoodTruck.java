package com.example.munch.data.model;

import com.example.munch.HttpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
        this.hours = new String[7][2];
        this.photos = new ArrayList<String>();
        this.reviews = new ArrayList<String>();
        this.tags = new ArrayList<String>();
        getTruck(truckId);
    }

    //Constructor for adding new Truck to database
    public FoodTruck(String token, String name, String address, String[][] hours, String[] photos, String owner ){
        this.hours = new String[7][2];
        this.photos = new ArrayList<String>();
        this.reviews = new ArrayList<String>();
        this.tags = new ArrayList<String>();
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

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private void jsonToFoodTruck (JSONObject jsonTruck) {
        try {
            this.name = jsonTruck.get("name").toString();
            this.address = jsonTruck.get("address").toString();
            this.name = jsonTruck.get("name").toString();
            JSONArray JSONlocation = new JSONArray(jsonTruck.get("location").toString());
            this.lng = Float.valueOf(JSONlocation.get(0).toString());
            this.lat = Float.valueOf(JSONlocation.get(1).toString());
            this.owner = jsonTruck.get("owner").toString();
            this.status = Boolean.valueOf(jsonTruck.get("status").toString());
            this.avgRating = Float.valueOf(jsonTruck.get("avgRating").toString());
            JSONArray JSONreviews = new JSONArray(jsonTruck.get("reviews").toString());
            for (int c  = 0; c < JSONreviews.length(); c++){
                this.reviews.add(JSONreviews.get(c).toString());
            }
            JSONArray JSONhours = new JSONArray(jsonTruck.get("hours").toString());
            for (int c  = 0; c < JSONhours.length(); c++) {
                JSONArray JSONhourday = new JSONArray(JSONhours.get(c).toString());
                this.hours[c][0] = JSONhourday.get(0).toString();
                this.hours[c][1] = JSONhourday.get(1).toString();
            }
            JSONArray JSONphotos = new JSONArray(jsonTruck.get("photos").toString());
            for (int c  = 0; c < JSONphotos.length(); c++){
                this.photos.add(JSONphotos.get(c).toString());
            }
            this.phoneNumber = jsonTruck.get("phoneNumber").toString();
            this.website = jsonTruck.get("website").toString();
            this.description = jsonTruck.get("description").toString();
            JSONArray JSONtags = new JSONArray(jsonTruck.get("tags").toString());
            for (int c  = 0; c < JSONtags.length(); c++){
                this.tags.add(JSONtags.get(c).toString());
            }
        } catch (JSONException e){

        }
    }

    public String[][] getHours() {
        return hours;
    }

    public int updateTruck(String token, HashMap<String, String> vals){
        JSONObject truck = new JSONObject();
        for (String key: vals.keySet()){
            try {
                truck.put(key, vals.get(key));
            } catch (JSONException ex) {
            }
        }
        HttpRequests getTruckRequests = new HttpRequests();
        getTruckRequests.execute("foodtrucks/" + id, "PUT", truck.toString(), token);
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
        getTruck(id);
        return getTruckRequests.getStatusCode();
    }
    private JSONObject foodTruckToJson () {
        //todo implement this function and add parameters
        return null;
    }
}
