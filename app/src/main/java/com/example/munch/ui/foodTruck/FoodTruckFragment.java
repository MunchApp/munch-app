package com.example.munch.ui.foodTruck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.map.MapViewModel;

public class FoodTruckFragment extends Fragment {

    private FoodTruckViewModel foodTruckViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodTruckViewModel =
                ViewModelProviders.of(this).get(FoodTruckViewModel.class);
        View root = inflater.inflate(R.layout.food_truck_activity, container, false);
        return root;
    }
}
