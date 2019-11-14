package com.example.munch.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.HttpRequests;
import com.example.munch.LocationCalculator;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.more.AboutPageActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    private MapViewModel mapViewModel;
    public ImageView firstIm;
    static GoogleMap munMap;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment

        mapFragment.getMapAsync(this);

//        populateNearbyTrucks(sampleTrucks());

//                populateSearchedTruckPin(37.415229, -122.06265, "Testing", "Truck122");

//                populatePins();

        populatePopularTrucksList(root);
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        munMap = mMap;
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(30.2672, -97.7431)).title("Init"));
//        marker.showInfoWindow();
//        munMap.setOnMarkerClickListener(onMarkerClickedListener);
    }


    private GoogleMap.OnMarkerClickListener onMarkerClickedListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
            }
            return true;
        }
    };

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
//                populateOfFoodTruck(truckListing);
                listings.add(truckListing);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), listings);
        resultsList.setAdapter(mAdapter);

        while(listings.size() < 10){

        }
        populateNearbyTrucks(listings);

    }

    private ArrayList<FoodTruck> sampleTrucks(){
        //creates dummy trucks for map pin testing
        ArrayList<FoodTruck> testTrucks = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            FoodTruck tester = new FoodTruck("identification" + i, "testTruck" + i, 30.2672f + i* 0.1f, -97.7431f - i*.1f);
            testTrucks.add(tester);
        }
        return testTrucks;
    }

    private void populateNearbyTrucks(ArrayList<FoodTruck> nearby){
        //Todo  input list of trucks output pins on map
        //place pins from ArrayList of trucks
        if(nearby != null){
            for(int i = 0; i < 10; i++){
                populateTruckPin(
                        nearby.get(i).getLatitude(),
                        nearby.get(i).getLongitude(),
                        nearby.get(i).getName(),
                        nearby.get(i).getId()
                );
            }
        }
    }

//    private void populateOfFoodTruck(FoodTruck truck){
//        populateTruckPin(truck.getLatitude(), truck.getLongitude(), truck.getName(), truck.getId());
//    }

    public void populateTruckPin(float lat, float lng, String name, String id){
        //Todo set create pin from truck info
        //helper function for populateNearbyTrucks
        Marker marker = munMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name));
    }

    private void populateSearchedTruckPin(float lat, float lng, String name, String id){
        //Todo set pin for single truck found through searchbar
        //sets pin for truck found from searching
        munMap.clear();
        Marker markered = munMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name));
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