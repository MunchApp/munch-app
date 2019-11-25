package com.example.munch.ui.userProfile.manageTruck;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.data.model.MunchUser;
import com.example.munch.ui.userProfile.UserProfileFragment;

import java.util.ArrayList;

public class ManageTruckFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_truck, container, false);
        ListView myTrucksList = (ListView) root.findViewById(R.id.list_of_trucks);
        TextView title = (TextView) root.findViewById(R.id.text_list_prompt);
        title.setText("Your Food Trucks");
        ArrayList<String> listings = new ArrayList<String>();
        listings = MunchUser.getInstance().getFoodTrucks();
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
