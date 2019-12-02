package com.example.munch.ui.map;

import android.view.View;

import com.example.munch.HttpRequests;
import com.example.munch.LocationCalculator;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class SearchLogic{


    private static SearchLogic search_instance = new SearchLogic();

    private String searchFormatted = "";
    private ArrayList<String> tagInputArray;
    private ArrayList<FoodTruck> searchListings;

    private HttpRequests foodTruckRequest;


    // private constructor restricted to this class itself
    private SearchLogic() {
        tagInputArray = new ArrayList<>();
        searchListings = new ArrayList<>();

    }

    // static method to create instance of Singleton class
    public static SearchLogic getInstance() {
        if (search_instance == null)
            search_instance = new SearchLogic();

        return search_instance;
    }


    ArrayList<FoodTruck> sortByDistance(String locInput) {
        String userLat = locInput.split("&")[0];
        String userLng = locInput.split("&")[1];
//                            if (searchText.getText().toString().equals("")){
//                                searchFormatted = "";
//                            }
        return getResultsList(searchFormatted, userLat, userLng);
    }

    ArrayList<FoodTruck> sortByRating() {
        searchListings.sort(new Comparator<FoodTruck>() {
            @Override
            public int compare(FoodTruck data1, FoodTruck data2) {
                return data2.getAvgRating().compareTo(data1.getAvgRating());
            }
        });

        return searchListings;
    }

    ArrayList<FoodTruck> sortByNumReviews() {
        searchListings.sort(new Comparator<FoodTruck>() {
            @Override
            public int compare(FoodTruck data1, FoodTruck data2) {
                return Integer.compare(data2.getReviews().size(), data1.getReviews().size());
            }
        });

        return searchListings;
    }

    ArrayList<FoodTruck> getResultsList(String searchFormatted, String userLat, String userLng) {
        searchListings = new ArrayList<>();
        foodTruckRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        foodTruckRequest.execute("foodtrucks?query=" + searchFormatted + "&lat=" + userLat + "&lon=" + userLng, "GET");
        String responseTruck = null;
        try {
            responseTruck = foodTruckRequest.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONArray truckData = new JSONArray(responseTruck);
            for (int i = 0; i < truckData.length(); i++) {
                JSONObject jsonobject = truckData.getJSONObject(i);
                String truckID = jsonobject.getString("id");

                FoodTruck truckListing = new FoodTruck(truckID);
                searchListings.add(truckListing);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchListings;
    }


    String formatLocationInput(View v, String locInput) {
        LocationCalculator locCal = new LocationCalculator(v.getContext());
        String lat = "";
        String lng = "";
        if (locInput.equals("")) {    //no location specified
            lat = Double.toString(locCal.getLat());
            lng = Double.toString(locCal.getLng());

        } else {
            String[] splitAddress = locInput.split(" ");
            String parsedAddress = splitAddress[0];
            String API_KEY = v.getResources().getString(R.string.GOOGLE_MAPS_API_KEY);
            for (int s = 1; s < splitAddress.length; s++) {
                parsedAddress += "+" + splitAddress[s];
            }

            String response = null;
            HttpRequests getTruckRequests = new HttpRequests(HttpRequests.Route.GOOGLE);
            getTruckRequests.execute(parsedAddress +
                    "&key=" + API_KEY, "GET");
            try {
                response = getTruckRequests.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                JSONObject myResponse = new JSONObject(response);
                JSONArray results = myResponse.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject location = results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                    lat = location.optString("lat");
                    lng = location.optString("lng");
                }
            } catch (JSONException e) {

            }


        }

        return lat + "&" + lng;
    }

   String formatSearchInput(String userInput, ArrayList<String> tagInputArray) {
        String formattedInput = userInput.replaceAll(" ", "+");
        if (!formattedInput.equals("")) {
            tagInputArray.add(formattedInput);
        }
        searchFormatted = "";

        if (!(tagInputArray.size() == 0)) {

            for (int i = 0; i < tagInputArray.size(); i++) {
                searchFormatted += tagInputArray.get(i);
                searchFormatted += "+";
            }


            if (!searchFormatted.equals("")) {

                while (searchFormatted.charAt(0) == '+') {
                    searchFormatted = searchFormatted.substring(1);
                }
                while (searchFormatted.charAt(searchFormatted.length() - 1) == '+') {
                    searchFormatted = searchFormatted.substring(0, searchFormatted.length() - 1);
                }

            }
        }
        return searchFormatted;

    }




}
