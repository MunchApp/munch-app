package com.example.munch.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        Button searchButton = root.findViewById(R.id.doSearch);

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