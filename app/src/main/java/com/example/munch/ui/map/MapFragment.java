package com.example.munch.ui.map;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.R;
import com.example.munch.SearchListing;
import com.example.munch.SearchListingAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MapFragment extends Fragment{

    private MapViewModel mapViewModel;

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

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(30.267153, -97.743057)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
            }
        });

        populatePins(root);
        populatePopularTrucksList(root);


        return root;
    }

    private void populatePopularTrucksList(View root) {
        ListView resultsList = (ListView) root.findViewById(R.id.search_results);
        ArrayList<SearchListing> listings = new ArrayList<>();
        listings.add(new SearchListing("Cold Cookie Company", R.drawable.cc1,
                R.drawable.cc2, R.drawable.cc3, 3.5, 409692773, "0.3 miles away"));
        listings.add(new SearchListing("Cold Cookie Company", R.drawable.cc1,
                R.drawable.cc2, R.drawable.cc3, 3.5, 409692773, "0.3 miles away"));
        SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), listings);
        resultsList.setAdapter(mAdapter);


        //TODO create GET request and get the results needed to populate, store in ArrayList of Map
//        for (int i = 0; i < 5; i++){
//
//        }


    }


    //this method gets a drawable object from image links for the food truck
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private void populatePins(View root) {
        //TODO get pins for existing food trucks to populate initial map
    }

}