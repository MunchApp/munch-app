package com.example.munch.ui.foodTruck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.munch.R;
import com.example.munch.ReviewListing;
import com.example.munch.ReviewListingAdapter;
import com.example.munch.SearchListing;
import com.example.munch.SearchListingAdapter;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.map.MapViewModel;

import java.util.ArrayList;

public class FoodTruckFragment extends Fragment {

    private FoodTruckViewModel foodTruckViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodTruckViewModel =
                ViewModelProviders.of(this).get(FoodTruckViewModel.class);
        View root = inflater.inflate(R.layout.food_truck_activity, container, false);
        populateReviews(root);
        return root;
    }

    private void populateReviews(View root) {
        ListView resultsList = (ListView) root.findViewById(R.id.review_layout);
        ArrayList<ReviewListing> listings = new ArrayList<>();
        //TODO Make Dynamic

        listings.add(new ReviewListing("SYED NAQVI", "October 25,2019",
                "This is a review! this food truck is so amazing! Please let me see this food truck!\nI want to eat here so bad! Nevermind I hate this place!\nOK just kidding, this place is awesome please come eat here!",4.9));
        listings.add(new ReviewListing("JANINE BARIUAN", "October 25,2019",
                "This is a review! this food truck is so amazing! Please let me see this food truck!\nI want to eat here so bad! Nevermind I hate this place!\nOK just kidding, this place is awesome please come eat here!",4.9));
        ReviewListingAdapter adapter = new ReviewListingAdapter(getActivity(), listings);
        resultsList.setAdapter(adapter);

    }
}
