package com.example.munch.ui.foodTruck;


import android.content.Context;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.munch.HttpRequests;
import com.example.munch.R;
import com.example.munch.data.model.Review;
import com.example.munch.HttpRequests;
import com.example.munch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class CreateTruckViewModel extends ViewModel {

    String name;
    String address;
    String[] photos;
    String[][] hours;
    float[] location;
    ArrayList<String> tags;

    public CreateTruckViewModel() {
        hours = new String[7][2];
        location = new float[2];
        tags = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public String[] getPhotos() {
        return photos;
    }

    public String getAddress() {
        return address;
    }

    public String[][] getHours() {
        return hours;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHours(int day, String start, String end) {
        this.hours[day][0] = start;
        this.hours[day][1] = end;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTag(String tag){
        String[] allTags = tag.split(", ");
        for (String s: allTags){
            tags.add(s);
        }
    }

    public ArrayList getTag(){
        return tags;
    }


    public void setPhotos(String[] photos) {
        this.photos = photos;
    }

    public float[] getLocation(Context context){
        String[] splitAddress = address.split(" ");
        String parsedAddress = splitAddress[0];
        String API_KEY = context.getResources().getString(R.string.GOOGLE_MAPS_API_KEY);
        for (int s = 1; s < splitAddress.length; s++){
            parsedAddress += "+" + splitAddress[s];
        }

        parsedAddress = parsedAddress.split("\n")[0] + parsedAddress.split("\n")[1];
        String response = null;
        HttpRequests getTruckRequests = new HttpRequests();
        getTruckRequests.execute("https://maps.googleapis.com/maps/api/geocode/json?address=" + parsedAddress +
                "&key=" + API_KEY, "GET");
        try {
            response = getTruckRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try{
            JSONObject myResponse = new JSONObject(response);
            JSONArray results = myResponse.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject location = results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                this.location[1] = Float.valueOf(location.optString("lat"));
                this.location[0] = Float.valueOf(location.optString("lng"));
            }
        }catch (JSONException e){

        }
        return location;
    }
}