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
import com.example.munch.data.model.MunchUser;
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
        listings = MunchUser.getInstance().getFavorites();
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
}