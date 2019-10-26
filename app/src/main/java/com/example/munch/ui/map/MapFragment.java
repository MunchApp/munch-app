package com.example.munch.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.HttpRequests;
import com.example.munch.R;
import com.example.munch.SearchListing;
import com.example.munch.SearchListingAdapter;
import com.example.munch.ui.foodTruck.FoodTruckFragment;
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
        HttpRequests foodTruckRequest = new HttpRequests();
        foodTruckRequest.execute("foodtrucks", "GET");
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
                String name = jsonobject.getString("name");

                ArrayList<String> photoURLs = new ArrayList<String>();
                JSONArray jArray = (JSONArray)jsonobject.get("photos");
                if (jArray != null) {
                    for (int k=0; k<jArray.length(); k++){
                        photoURLs.add(jArray.getString(k));
                    }
                }

                if (photoURLs.size() > 3){
                    photoURLs.subList(0,2);
                } else {
                    if (photoURLs.size() == 1){
                        photoURLs.add(1, "https://www.ccms.edu/wp-content/uploads/2018/07/Photo-Not-Available-Image.jpg");
                    }
                    if (photoURLs.size() == 2){
                        photoURLs.add(2, "https://www.ccms.edu/wp-content/uploads/2018/07/Photo-Not-Available-Image.jpg");
                    }
//                    if (photoURLs.get(0).isEmpty()){
//                        photoURLs.set(0, "https://www.ccms.edu/wp-content/uploads/2018/07/Photo-Not-Available-Image.jpg");
//                    }
//                    if (photoURLs.get(1).isEmpty()){
//                        photoURLs.set(1, "https://www.ccms.edu/wp-content/uploads/2018/07/Photo-Not-Available-Image.jpg");
//                    }
//                    if (photoURLs.get(2).isEmpty()){
//                        photoURLs.set(2, "https://www.ccms.edu/wp-content/uploads/2018/07/Photo-Not-Available-Image.jpg");
//                    }
                }


//                String[] actualImages = new String[3];
//                if (images.length >= 3){
//                    for (int j = 0; j < 3; j++){
//                        actualImages[j] = images[j];
//                    }
//                } else {
//                    if (images.length == 1){
//                        actualImages[0] = images[0];
//                        actualImages[1] = "https://www.ccms.edu/wp-content/uploads/2018/07/Photo-Not-Available-Image.jpg";
//                        actualImages[2] = "https://www.ccms.edu/wp-content/uploads/2018/07/Photo-Not-Available-Image.jpg";
//                    }
//                    if (images.length == 2){
//                        actualImages[0] = images[0];
//                        actualImages[1] = images[1];
//                        actualImages[2] = "https://www.ccms.edu/wp-content/uploads/2018/07/Photo-Not-Available-Image.jpg";
//                    }
//                }
                listings.add(new SearchListing(name, photoURLs.get(0),
                        photoURLs.get(1), photoURLs.get(2), 3.5, 409692773, "0.3 miles away"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }





//        String url1 = "https://s3-media4.fl.yelpcdn.com/bphoto/j0m1Ru-GyRSejU0O8jMOQQ/o.jpg";
//        String url2 = "https://s3-media4.fl.yelpcdn.com/bphoto/j0m1Ru-GyRSejU0O8jMOQQ/o.jpg";
//        String url3 = "https://s3-media4.fl.yelpcdn.com/bphoto/j0m1Ru-GyRSejU0O8jMOQQ/o.jpg";
//
//        listings.add(new SearchListing("Cold Cookie Company", url1,
//                url2, url3, 3.5, 409692773, "0.3 miles away"));
////        listings.add(new SearchListing("Cold Cookie Company", R.drawable.cc1,
////                R.drawable.cc2, R.drawable.cc3, 3.5, 409692773, "0.3 miles away"));
        SearchListingAdapter mAdapter = new SearchListingAdapter(getActivity(), listings);
        resultsList.setAdapter(mAdapter);


        //TODO create GET request and get the results needed to populate, store in ArrayList of Map
//        for (int i = 0; i < 5; i++){
//
//        }


    }

    public void goToFoodTruck(View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Fragment myFragment = new FoodTruckFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();
    }



    private void populatePins(View root) {
        //TODO get pins for existing food trucks to populate initial map
    }

}