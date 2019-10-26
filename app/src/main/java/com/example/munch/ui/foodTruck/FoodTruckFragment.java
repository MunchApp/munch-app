package com.example.munch.ui.foodTruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.R;
import com.example.munch.ui.Reviewspageactivity;

public class FoodTruckFragment extends Fragment {

    private FoodTruckViewModel foodTruckViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodTruckViewModel =
                ViewModelProviders.of(this).get(FoodTruckViewModel.class);
        View root = inflater.inflate(R.layout.food_truck_activity, container, false);
        makeReviewClickable(root);
        return root;
    }

    private void makeReviewClickable(View root) {
        TextView reviews = (TextView) root.findViewById(R.id.review_text);
        reviews.setClickable(true);
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Reviewspageactivity.class);
                startActivity(intent);
            }
        });
    }
}


