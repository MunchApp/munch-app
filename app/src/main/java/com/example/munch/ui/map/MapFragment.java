package com.example.munch.ui.map;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapFragment extends Fragment{

    private MapViewModel mapViewModel;
    public ImageView firstIm;
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


        //TODO create GET request and get the results needed to populate, store in ArrayList of Map
//        for (int i = 0; i < 5; i++){
//
//        }


    }




    private void populatePins(View root) {
        //TODO get pins for existing food trucks to populate initial map
    }

}