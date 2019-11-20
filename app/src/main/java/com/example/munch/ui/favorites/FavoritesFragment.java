package com.example.munch.ui.favorites;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.userProfile.UserProfileFragment;
import com.example.munch.ui.userProfile.manageTruck.TruckListingAdapter;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_truck, container, false);
        final TextView title = root.findViewById(R.id.text_list_prompt);
        ListView myTrucksList = (ListView) root.findViewById(R.id.list_of_trucks);
        title.setText("Your Favorites");
        ArrayList<String> listings = new ArrayList<String>();
        listings = UserProfileFragment.currentUser.getFavorites();
        if (listings.size() != 0) {
            ArrayList<FoodTruck> truckListings = new ArrayList<FoodTruck>();
            for (String s: listings){
                truckListings.add(new FoodTruck(s));
            }
            TruckListingAdapter mAdapter = new TruckListingAdapter(getActivity(), truckListings);
            myTrucksList.setAdapter(mAdapter);
        }
        return root;
    }

    //Code to make the first image in favorites list be clickable TODO:Obviously implement so that its dynamic and also for every image
    /*private void clickable(View root){
        imageClick = (ImageView) root.findViewById(R.id.main_image);
        imageClick.bringToFront();
        imageClick.setClickable(true);
        imageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FoodTruckFragment NAME = new FoodTruckFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment, NAME);
                fragmentTransaction.commit();
            }
        });

    }*/
}