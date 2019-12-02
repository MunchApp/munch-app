package com.example.munch.ui.map;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class Search {

    private static Search search_instance = null;

    private String searchFormatted = "";

    EditText searchText;
    EditText locText;
    ArrayList<String> tagInputArray;
    ListView resultsList;

    CheckBox mAmercianCheck;
    ArrayList<String> amerTags;
    CheckBox mAsianCheck;
    ArrayList<String> asianTags;
    CheckBox mBarbequeCheck;
    ArrayList<String> barbTags;
    CheckBox mSouthernCheck;
    ArrayList<String> southTags;
    CheckBox mBreakfastCheck;
    ArrayList<String> breakTags;
    CheckBox mMexicanCheck;
    ArrayList<String> mexiTags;
    CheckBox mSeafoodCheck;
    ArrayList<String> seaTags;
    CheckBox mDessertCheck;
    ArrayList<String> dessTags;
    ArrayList<FoodTruck> searchListings;
    String locInput;
    String responseTruck;

    HttpRequests foodTruckRequest;

    TextView sortByText;
    Spinner sortBySpinner;
    TextView noResultsText;
    Context activity;
    View view;

    // private constructor restricted to this class itself
    private Search(View root) {
        view = root;

        searchText = root.findViewById(R.id.search_explore_pg1);
        locText = root.findViewById(R.id.location_explore_pg1);
        sortBySpinner = (Spinner) root.findViewById(R.id.spinner_sort);
        noResultsText = root.findViewById(R.id.no_results_text);
        resultsList = root.findViewById(R.id.search_results);
        activity = root.getContext();
        tagInputArray = new ArrayList<>();

        initializeCategoryTags(root);
    }

    // static method to create instance of Singleton class
    public static Search getInstance(View root)
    {
        if (search_instance == null)
            search_instance = new Search(root);

        return search_instance;
    }

    public void makeSearch() {
        searchFormatted = formatSearchInput(view);
        locInput = formatLocationInput(view);
        String userLat = locInput.split("&")[0];
        String userLng = locInput.split("&")[1];

        if (!searchFormatted.equals("") || !locInput.equals("")) {

            performSearch(searchFormatted, userLat, userLng);

        } else {
            performSearch("", userLat, userLng);
        }
    }

    public void sortByDistance() {
        locInput = formatLocationInput(view);
        String userLat = locInput.split("&")[0];
        String userLng = locInput.split("&")[1];
//                            if (searchText.getText().toString().equals("")){
//                                searchFormatted = "";
//                            }
        performSearch(searchFormatted, userLat, userLng);
    }

    public void sortByRating() {
        searchListings.sort(new Comparator<FoodTruck>() {
            @Override
            public int compare(FoodTruck data1, FoodTruck data2) {
                return data2.getAvgRating().compareTo(data1.getAvgRating());
            }
        });

        updateListings();
    }

    public void sortByNumReviews() {
        searchListings.sort(new Comparator<FoodTruck>() {
            @Override
            public int compare(FoodTruck data1, FoodTruck data2) {
                return Integer.compare(data2.getReviews().size(), data1.getReviews().size());
            }
        });

        updateListings();
    }

    public void replaceMethod() {
        populateClosestTrucksList();
    }


    public void populateClosestTrucksList() {
//        resultsList = (ListView) view.findViewById(R.id.search_results);
        searchListings = new ArrayList<>();
        HttpRequests foodTruckRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        foodTruckRequest.execute("foodtrucks", "GET");
        String responseTruck = null;
        try {
            responseTruck = foodTruckRequest.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONArray truckData = new JSONArray(responseTruck);
            for (int i = 0; i < 10; i++) {
                JSONObject jsonobject = truckData.getJSONObject(i);
                String id = jsonobject.getString("id");

                FoodTruck truckListing = new FoodTruck(id);
//                populateOfFoodTruck(truckListing);
                searchListings.add(truckListing);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SearchListingAdapter mAdapter = new SearchListingAdapter(activity, searchListings);
        resultsList.setAdapter(mAdapter);
        MapFragment.forWindow = searchListings;
        MapFragment.listing = searchListings;

//        listing = searchListings;
//        forWindow = listing;
    }

    public void updateListings() {
        SearchListingAdapter mAdapter = new SearchListingAdapter(activity, searchListings);
        resultsList.setAdapter(mAdapter);
        MapFragment.listing = searchListings;
        MapFragment.forWindow = searchListings;
//        listing = searchListings;
//        forWindow = listing;
//        initialePopulation();
    }

    public void performSearch(String searchFormatted, String userLat, String userLng) {
//        resultsList = (ListView) getActivity().findViewById(R.id.search_results);
        searchListings = new ArrayList<>();
        foodTruckRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        foodTruckRequest.execute("foodtrucks?query=" + searchFormatted + "&lat=" + userLat + "&lon=" + userLng, "GET");
        responseTruck = null;
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

//        SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), searchListings);
//        resultsList.setAdapter(mAdapter);
        if (searchListings.size() == 0){
            noResultsText.setVisibility(View.VISIBLE);
            sortBySpinner.setEnabled(false);
        } else {
            noResultsText.setVisibility(View.GONE);
            sortBySpinner.setEnabled(true);
        }
//        listing = searchListings;
//        forWindow = listing;
//        initialePopulation();

        updateListings();

    }

    public String formatLocationInput(View v) {
        locInput = locText.getText().toString();
        LocationCalculator locCal = new LocationCalculator(activity);
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

    public String formatSearchInput(View v) {
        String userInput = searchText.getText().toString();
        tagInputArray.clear();
        String formattedInput = userInput.replaceAll(" ", "+");
        if (!formattedInput.equals("")) {
            tagInputArray.add(formattedInput);
        }
        if (mAmercianCheck.isChecked()){
            tagInputArray.addAll(amerTags);
        }
        if (mAsianCheck.isChecked()){
            tagInputArray.addAll(asianTags);
        }
        if (mBarbequeCheck.isChecked()){
            tagInputArray.addAll(barbTags);
        }
        if (mSouthernCheck.isChecked()){
            tagInputArray.addAll(southTags);
        }
        if (mBreakfastCheck.isChecked()){
            tagInputArray.addAll(breakTags);
        }
        if (mMexicanCheck.isChecked()){
            tagInputArray.addAll(mexiTags);
        }
        if (mSeafoodCheck.isChecked()){
            tagInputArray.addAll(seaTags);
        }
        if (mDessertCheck.isChecked()){
            tagInputArray.addAll(dessTags);
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

    private void initializeCategoryTags(View root) {
        mAmercianCheck = root.findViewById(R.id.catAmerican);
        amerTags = new ArrayList<>();
        amerTags.add("American");
        amerTags.add("Hot+Dogs");
        amerTags.add("Burgers");
        amerTags.add("Pizza");
        amerTags.add("Hawaiian");
        amerTags.add("Steak");
//        amerTags.add("");

        mAsianCheck = root.findViewById(R.id.catAsian);
        asianTags = new ArrayList<>();
        asianTags.add("Asian");
        asianTags.add("Thai");
        asianTags.add("Korean");
        asianTags.add("Japanese");
        asianTags.add("Asian+Fusion");
        asianTags.add("Vietnamese");
        asianTags.add("Chinese");
        asianTags.add("Indian");
        asianTags.add("Filipino");
//        asianTags.add("");
//        asianTags.add("");

        mBarbequeCheck = root.findViewById(R.id.catBarbeque);
        barbTags = new ArrayList<>();
        barbTags.add("Barbeque");
        barbTags.add("Barbecue");
        barbTags.add("Korean+Barbeque");
        barbTags.add("Brisket");

        mSouthernCheck = root.findViewById(R.id.catSouthern);
        southTags = new ArrayList<>();
        southTags.add("Southern");
        southTags.add("Potatoes");
//        southTags.add("");
//        southTags.add("");


        mBreakfastCheck = root.findViewById(R.id.catBreakfast);
        breakTags = new ArrayList<>();
        breakTags.add("Breakfast");
        breakTags.add("Brunch");
        breakTags.add("Eggs");
        breakTags.add("Pancakes");
//        breakTags.add("");

        mMexicanCheck = root.findViewById(R.id.catMexican);
        mexiTags = new ArrayList<>();
        mexiTags.add("Mexican");
        mexiTags.add("Latin");
        mexiTags.add("Tacos");
        mexiTags.add("Burritos");
        mexiTags.add("Quesadillas");
//        mexiTags.add("");
//        mexiTags.add("");

        mSeafoodCheck = root.findViewById(R.id.catSeafood);
        seaTags = new ArrayList<>();
        seaTags.add("Seafood");
        seaTags.add("Fish");
        seaTags.add("Shrimp");
        seaTags.add("Sushi");
//        seaTags.add("");

        mDessertCheck = root.findViewById(R.id.catDessert);
        dessTags = new ArrayList<>();
        dessTags.add("Dessert");
        dessTags.add("Ice+Cream");
        dessTags.add("Crepes");
        dessTags.add("Sweet");
        dessTags.add("Bakery");
        dessTags.add("Cakes");
        dessTags.add("Donuts");
    }



}
