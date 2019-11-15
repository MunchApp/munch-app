package com.example.munch.ui.map;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.munch.HttpRequests;
import com.example.munch.LocationCalculator;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapFragment extends Fragment{

    public ImageView firstIm;
    EditText searchText;
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

    TextView sortByText;
    Spinner sortBySpinner;
    public static final CharSequence[] sortList = {"Sort By", "Distance", "Rating", "Number of Reviews"};

//    CheckBox mRating4;
//    CheckBox mRating3;
//    CheckBox mRating2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers
                LocationCalculator locationCalculator = new LocationCalculator(getContext());
                double lat = locationCalculator.getLat();
                double lng = locationCalculator.getLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
            }
        });

        populatePins(root);
        populatePopularTrucksList(root);

        searchText = root.findViewById(R.id.search_explore_pg1);

        final SlidingUpPanelLayout slideUpPanel = (SlidingUpPanelLayout) root.findViewById(R.id.sliding_layout);
        final View searchBar = (View) root.findViewById(R.id.search_map);
        final View locationBar = (View) root.findViewById(R.id.location_map);
        final View options = (View) root.findViewById(R.id.options);
        final View background = (View) root.findViewById(R.id.search_bg);
        background.setVisibility(View.GONE);
        locationBar.setVisibility(View.GONE);
        options.setVisibility(View.GONE);
        final TextView logoText = (TextView) root.findViewById(R.id.logo_text);
        slideUpPanel.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                if(previousState == PanelState.COLLAPSED && newState == PanelState.DRAGGING){
                    ObjectAnimator animation = ObjectAnimator.ofFloat(searchBar, "translationY", -230f);
                    animation.setDuration(1000);
                    animation.start();
                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(locationBar, "translationY", 0f);
                    animation2.setDuration(1000);
                    animation2.start();
                    AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
                    fadeOut.setDuration(1000);
                    logoText.setVisibility(View.INVISIBLE);
                    logoText.startAnimation(fadeOut);
                }
                if(previousState == PanelState.DRAGGING && newState == PanelState.COLLAPSED){
                    ObjectAnimator animation = ObjectAnimator.ofFloat(searchBar, "translationY", 0f);
                    animation.setDuration(1000);
                    animation.start();
                    logoText.setVisibility(View.VISIBLE);
                    AlphaAnimation fadeIn = new AlphaAnimation( 0.0f , 1.0f ) ;
                    fadeIn.setDuration(1000);
                    fadeIn.setFillAfter(true);
                    logoText.startAnimation(fadeIn);
                }
            }
        });
        searchText.setClickable(true);
        searchText.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
               fadeOut.setDuration(1000);
               AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
               fadeIn.setDuration(1000);
               fadeIn.setFillAfter(true);
               ObjectAnimator animation = ObjectAnimator.ofFloat(searchBar, "translationY", -170f);
               animation.setDuration(1000);
               animation.start();
               if(logoText.getVisibility() == View.VISIBLE) {
                   logoText.setVisibility(View.INVISIBLE);
                   logoText.startAnimation(fadeOut);
               }
               if (locationBar.getVisibility() == View.GONE) {
                   locationBar.setVisibility(View.VISIBLE);
                   locationBar.startAnimation(fadeIn);
               }
               background.setVisibility(View.VISIBLE);
               int negHeight = background.getHeight() * -1;
               TranslateAnimation animate = new TranslateAnimation(
                       0,
                       0,
                       negHeight,
                       0);
               animate.setDuration(1000);
               animate.setFillAfter(true);
               background.startAnimation(animate);
               options.setVisibility(View.VISIBLE);
               options.startAnimation(fadeIn);
            }
        });

        TextView close = (TextView)root.findViewById(R.id.close_button);
        TextView search = (TextView)root.findViewById(R.id.search_button);
        tagInputArray = new ArrayList<>();

        initializeCategoryTags(root);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch(slideUpPanel,logoText,searchBar,locationBar,background,options);
            }
        });



        //region search and filter logic
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch(slideUpPanel,logoText,searchBar,locationBar,background,options);
                String userInput = searchText.getText().toString();
                String formattedInput = userInput.replaceAll(" ", "+");
                tagInputArray.add(formattedInput);
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

                String sb = "";
                for (String s : tagInputArray){
                    sb += s;
                    sb += "+";
                }
                if (!sb.equals("")){
                    HttpRequests searchRequest = new HttpRequests();
                    String serverURL = "https://munch-server.herokuapp.com/";
                    searchRequest.execute(serverURL + "foodtrucks?query=" + sb, "GET");
                    searchListings = new ArrayList<>();
                    String responseSearch = null;
                    try {
                        responseSearch = searchRequest.get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray searchData = new JSONArray(responseSearch);
                        for (int i = 0; i < searchData.length(); i++) {
                            JSONObject jsonobject = searchData.getJSONObject(i);
                            String id = jsonobject.getString("id");

                            FoodTruck truckListing = new FoodTruck(id);
                            searchListings.add(truckListing);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), searchListings);
                    resultsList.setAdapter(mAdapter);
                }

            }
        });
        //endregion

//        sortByText = root.findViewById(R.id.sort_by_text);
//        sortByText.setOnClickListener(           //action triggered on button click
//                new View.OnClickListener() {
//                    public void onClick(View view) {
//
//                    }
//                });

//        sortBySpinner = root.findViewById(R.id.spinner_sort_by);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(), R.layout.support_simple_spinner_dropdown_item, sortList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sortBySpinner.setAdapter(adapter);






        return root;
    }


//    public void sortByNumReviews() {
//
//        Collections.sort(data, new Comparator<FoodTruck>() {
//            @Override
//            public int compare(YourDataModel data1, YourDataModel data2) {
//                if (data1.getDistance() < data2.getDistance())
//                    return 1;
//                else
//                    return 0;
//            }
//        });
//    }

    private void initializeCategoryTags(View root) {
        mAmercianCheck = root.findViewById(R.id.catAmerican);
        amerTags = new ArrayList<>();
        amerTags.add("American");
        amerTags.add("Hot Dogs");
        amerTags.add("Burgers");
        amerTags.add("Pizza");
        amerTags.add("Sandwiches");
        amerTags.add("Hawaiian");
        amerTags.add("Steak");

        mAsianCheck = root.findViewById(R.id.catAsian);
        asianTags = new ArrayList<>();
        asianTags.add("Asian");
        asianTags.add("Thai");
        asianTags.add("Korean");
        asianTags.add("Japanese");
        asianTags.add("Asian Fusion");
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
        barbTags.add("Korean Barbeque");
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
        dessTags.add("Ice Cream");
        dessTags.add("Crepes");
        dessTags.add("Sweet");
        dessTags.add("Bakery");
        dessTags.add("Cakes");
        dessTags.add("Donuts");


    }

    private void closeSearch (SlidingUpPanelLayout slideUpPanel, TextView logoText,View searchBar,View locationBar, View background, View options){
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(1000);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);
        float move = 0f;
        if (slideUpPanel.getPanelState() == PanelState.EXPANDED){
            move = -230f;
        } else {
            logoText.setVisibility(View.VISIBLE);
            logoText.startAnimation(fadeIn);
        }
        ObjectAnimator animation = ObjectAnimator.ofFloat(searchBar, "translationY", move);
        animation.setDuration(1000);
        animation.start();
        if (locationBar.getVisibility() == View.VISIBLE) {
            locationBar.startAnimation(fadeOut);
            locationBar.setVisibility(View.GONE);
        }
        int negHeight = background.getHeight() * -1;
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                negHeight);
        animate.setDuration(1000);
        animate.setFillAfter(true);
        background.setVisibility(View.GONE);
        background.startAnimation(animate);
        options.startAnimation(fadeOut);
        options.setVisibility(View.GONE);
    }

    //sort by distance, rating, and most reviewed
    //filter by rating, food tags and categories
        /*
        Hawaiian    Seafood Southern
        Brazilian
        Burgers
        Korean
        Barbeque
        Chinese
        Sandwiches
         */

    private void populatePopularTrucksList(View root) {
        resultsList = (ListView) root.findViewById(R.id.search_results);
        ArrayList<FoodTruck> listings = new ArrayList<>();
        HttpRequests foodTruckRequest = new HttpRequests();
        String serverURL = "https://munch-server.herokuapp.com/";
        foodTruckRequest.execute(serverURL + "foodtrucks", "GET");
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
                String id = jsonobject.getString("id");

                FoodTruck truckListing = new FoodTruck(id);
                listings.add(truckListing);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), listings);
        resultsList.setAdapter(mAdapter);
    }



    private void populatePins(View root) {
        //TODO get pins for existing food trucks to populate initial map
    }

}