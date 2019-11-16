package com.example.munch.ui.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.HttpRequests;
import com.example.munch.LocationCalculator;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.foodTruck.CustomInfoWindowAdaptor;
import com.example.munch.ui.foodTruck.FoodTruckFragment;
import com.example.munch.ui.more.AboutPageActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mapViewModel;
    public ImageView firstIm;
    static GoogleMap munMap;
    private ArrayList<FoodTruck> listing;
    static ArrayList<FoodTruck> forWindow;

    private Marker lastClicked;

    EditText searchText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
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
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
                munMap = mMap;

                initialePopulation();
                munMap.setInfoWindowAdapter(new CustomInfoWindowAdaptor(getContext()));
                munMap.setOnMarkerClickListener(myMarkerClick);
                munMap.setOnInfoWindowClickListener(myWindowClick);
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
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch(slideUpPanel,logoText,searchBar,locationBar,background,options);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch(slideUpPanel,logoText,searchBar,locationBar,background,options);
                //todo put call search route and search functionality here
            }
        });

        String name;
        String address;
        ArrayList<String> tags;
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userInput = searchText.getText().toString();
//
//
//                HttpRequests searchRequest = new HttpRequests();
//                String serverURL = "https://munch-server.herokuapp.com/";
//                searchRequest.execute(serverURL + "foodtrucks?name=" + name + "&tags=" + tags + "&address=" + address);
//            }
//        });




        return root;
    }

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

    private void populatePopularTrucksList(View root) {
        ListView resultsList = (ListView) root.findViewById(R.id.search_results);
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

        while (listings.size() < 10) {

        }

        listing = listings;
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

}