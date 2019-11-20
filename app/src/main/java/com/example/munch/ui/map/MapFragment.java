package com.example.munch.ui.map;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.munch.HttpRequests;
import com.example.munch.LocationCalculator;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.foodTruck.CustomInfoWindowAdaptor;
import com.example.munch.ui.foodTruck.FoodTruckFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //region global variables
    public ImageView firstIm;
    static GoogleMap munMap;
    String sb;

    private ArrayList<FoodTruck> listing;
    static ArrayList<FoodTruck> forWindow;

    private Marker lastClicked;

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
    public static final CharSequence[] sortList = {"Sort By", "Distance", "Rating", "Number of Reviews"};

//    CheckBox mRating4;
//    CheckBox mRating3;
//    CheckBox mRating2;
    //endregion


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        populatePopularTrucksList(root);
        mapFragment.getMapAsync(this);

        searchText = root.findViewById(R.id.search_explore_pg1);
        locText = root.findViewById(R.id.location_explore_pg1);
        sortBySpinner = (Spinner) root.findViewById(R.id.spinner_sort);

        //region slidingPanel
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
        //endregion

        //region search and filter logic
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch(slideUpPanel,logoText,searchBar,locationBar,background,options);
                locInput = locText.getText().toString();
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
                sb = "";

                if (!(tagInputArray.size() == 0)) {

                    for (int i = 0; i < tagInputArray.size(); i++) {
                        sb += tagInputArray.get(i);
                        sb += "+";
                    }
                    while (sb.charAt(0) == '+') {
                        sb = sb.substring(1);
                    }
                    while (sb.charAt(sb.length() - 1) == '+') {
                        sb = sb.substring(0, sb.length() - 1);
                    }

                    if (!sb.equals("")) {
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

            }
        });
        //endregion

        //region sort by logic
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setPrompt("SORT");
        sortBySpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        getActivity()));
        sortBySpinner.setOnItemSelectedListener(           //action triggered on button click
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0 || position == 1){     //distance
                            LocationCalculator locCal = new LocationCalculator(getContext());
                            String lat = "";
                            String lng = "";
                            if (locText.getText().toString().equals("")){    //no location specified
                                lat = Double.toString(locCal.getLat());
                                lng = Double.toString(locCal.getLng());

                            } else {
                                String address = locText.getText().toString();
                                String[] splitAddress = address.split(" ");
                                String parsedAddress = splitAddress[0];
                                String API_KEY = view.getResources().getString(R.string.GOOGLE_MAPS_API_KEY);
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
                                        lat = location.optString("lat");
                                        lng = location.optString("lng");
                                    }
                                }catch (JSONException e){

                                }

                            }

                            resultsList = (ListView) getActivity().findViewById(R.id.search_results);
                            searchListings = new ArrayList<>();
                            foodTruckRequest = new HttpRequests();
                            String serverURL = "https://munch-server.herokuapp.com/";
                            if (searchText.getText().toString().equals("")){
                                sb = "";
                            }
                            foodTruckRequest.execute(serverURL + "foodtrucks?query=" + sb + "&lat=" + lat + "&lon=" + lng, "GET");
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

                            SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), searchListings);
                            resultsList.setAdapter(mAdapter);

                        }
                        if (position == 2){      //rating

                            searchListings.sort(new Comparator<FoodTruck>() {
                                @Override
                                public int compare(FoodTruck data1, FoodTruck data2) {
                                    if( data1.getAvgRating() < data2.getAvgRating() )
                                        return 1;
                                    else
                                        return -1;
                                }
                            });

                            SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), searchListings);
                            resultsList.setAdapter(mAdapter);

                        }
                        if (position == 3){      //num reviews
                            searchListings.sort(new Comparator<FoodTruck>() {
                                @Override
                                public int compare(FoodTruck data1, FoodTruck data2) {
                                    if( data1.getReviews().size() < data2.getReviews().size() )
                                        return 1;
                                    else
                                        return -1;
                                }
                            });

                            SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), searchListings);
                            resultsList.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        populatePopularTrucksList(getView());
                    }

                });
        //endregion


        return root;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.clear(); //clear old markers
        LocationCalculator locationCalculator = new LocationCalculator(getContext());
        double lat = locationCalculator.getLat();
        double lng = locationCalculator.getLng();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
        munMap = mMap;

        initialePopulation();
        munMap.setInfoWindowAdapter(new CustomInfoWindowAdaptor(getContext()));
        munMap.setOnMarkerClickListener(myMarkerClick);
        munMap.setOnInfoWindowClickListener(myWindowClick);

    }

    private GoogleMap.OnInfoWindowClickListener myWindowClick = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker markey) {
            FragmentActivity activity = (FragmentActivity) getContext();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FoodTruckFragment NAME = new FoodTruckFragment(getTruckFromMarker(markey), false);
            fragmentTransaction.replace(R.id.nav_host_fragment, NAME);
            fragmentTransaction.commit();
        }
    };


    private GoogleMap.OnMarkerClickListener myMarkerClick = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker markey) {
            if(lastClicked != null) {
                lastClicked.setIcon(BitmapDescriptorFromVector(getContext(), R.drawable.ft_dot));

            }
            markey.showInfoWindow();
            markey.setIcon(BitmapDescriptorFromVector(getContext(), R.drawable.ft_truck));;
            lastClicked = markey;

            return true;
        }
    };




    private void initializeCategoryTags(View root) {
        mAmercianCheck = root.findViewById(R.id.catAmerican);
        amerTags = new ArrayList<>();
        amerTags.add("American");
        amerTags.add("Hot+Dogs");
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
        searchListings = new ArrayList<>();
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
//                populateOfFoodTruck(truckListing);
                searchListings.add(truckListing);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), searchListings);
        resultsList.setAdapter(mAdapter);

        while (searchListings.size() < 10) {

        }

        listing = searchListings;
        forWindow = listing;
    }

    private ArrayList<FoodTruck> sampleTrucks() {
        //creates dummy trucks for map pin testing
        ArrayList<FoodTruck> testTrucks = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            FoodTruck tester = new FoodTruck("identification" + i, "testTruck" + i, 30.2672f + i * 0.1f, -97.7431f - i * .1f);
            testTrucks.add(tester);
        }
        return testTrucks;
    }

    private void repopulateFromList(ArrayList<FoodTruck> nearby) {
        //Clears map of pins and populates with new list
        //place pins on map from ArrayList of trucks
        munMap.clear();
        if (nearby != null) {
            for (int i = 0; i < 3; i++) {
                populateFromFT(nearby.get(i));
            }
        }
    }

    private void initialePopulation() {
        //initial Food Truck Population
        if (listing != null) {
            for (int i = 0; i < listing.size(); i++) {
                populateFromFT(listing.get(i));

            }
        }
    }

    private FoodTruck getTruckFromMarker(Marker myMark) {
        //Returns FoodTruck from a marker
        //Assumes FoodTruck is in listing ArrayList
        //The arrayList listing cannot be null or crashes


        for (int i = 0; i < listing.size(); i++) {
            if (listing.get(i).getMarker().equals(myMark)) {
                return listing.get(i);
            }
        }
        return listing.get(0);
    }


    private void populateTrucksFromList(ArrayList<FoodTruck> nearby) {
        //Todo  input list of trucks output pins on map
        //place pins on map from ArrayList of trucks
        if (nearby != null) {
            for (int i = 0; i < 2; i++) {
                populateFromFT(nearby.get(i));
            }
        }
    }


    private void populateFromFT(FoodTruck truck){
        //creates new marker for FoodTruck and displays them on map
        Marker marker = munMap.addMarker(truck.getMarkerOption());
        truck.setMarker(marker);
        truck.setVisibilityOn();
        marker.setIcon(BitmapDescriptorFromVector(getContext(), R.drawable.ft_dot));

        if(truck.getPhotos().size() != 0){
            marker.setSnippet(truck.getAvgRating() + " " + truck.getPhotos().get(0));
        }
        else if(truck.getPhotos().size() == 0){
            marker.setSnippet(truck.getAvgRating() + " " + "None");
        }
    }

    private void hideFTMarker(FoodTruck truck){
        //hides a specific FoodTrucks marker from the map
        truck.getMarker().setVisible(false);
        truck.setVisibilityOff();
    }

    private void displayFTMarker(FoodTruck truck){
        //displays a specific FoodTrucks marker on the map
        truck.getMarker().setVisible(true);
        truck.setVisibilityOn();
    }



    private BitmapDescriptor BitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable= ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }







//    private void populateTrucksFromTag(ArrayList<FoodTruck> nearby, String tag){
//        //place pins on map from ArrayList of trucks
//        for(int i = 0; i < listing.size(); i++){
//            if(listing.get(i).getTags() == null){
//                boolean flag = false;
//                for(int j = 0; j <  listing.get(i).getTags().size(); j++){
//                      if(listing.get(i).getTags().get(j) == tag){
//                            flag = true;
//                      }
//                if(flag = true){
//                      displayFTMarker(listing.get(i);
//                  }
//                }
//
//            }
//        }
//            }
//        }
//    }

///////////////TRASH    Below
//    private void populateOfFoodTruck(FoodTruck truck){
//        populateTruckPin(truck.getLatitude(), truck.getLongitude(), truck.getName(), truck.getId());
//    }

    private void popFoodTruck(MarkerOptions markO){
        //May delete
        munMap.addMarker(markO);
    }

    public void populateTruckPin(float lat, float lng, String name, String id){
        //Todo set create pin from truck info
        //helper function for populateNearbyTrucks

        Marker marker = munMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name));

//        marker.set
//        MarkerOptions markeres = new MarkerOptions().position(new LatLng(30.3152, (-97.0726))).title("TestTruck2");
////        munMap.addMarker(markeres);
//        munMap.addMarker(markeres);

//        MarkerOptions markeres = new MarkerOptions().position(new LatLng(lat, lng)).title("TestTruck2");
////        munMap.addMarker(markeres);
//        Marker market = munMap.addMarker(markeres);

//        if i set the title as the id, i can find the name, or i can find the info in the text window

//
    }

    private void populateSearchedTruckPin(float lat, float lng, String name, String id){
        //Todo set pin for single truck found through searchbar
        //sets pin for truck found from searching
//        munMap.clear();
//        Marker markered =
                munMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name));
//        markered.showInfoWindow();
//        munMap.setOnMarkerClickListener(onMarkerClickedListener);
    }


    private void populatePins() {
        //TODO get pins for existing food trucks to populate initial map
        munMap.clear();
        munMap.addMarker(new MarkerOptions().position(new LatLng(37.415229, -122.17265)).title("TestTruck"));
        munMap.addMarker(new MarkerOptions().position(new LatLng(37.415229, -122.07265)).title("TestTruck2"));
    }

}