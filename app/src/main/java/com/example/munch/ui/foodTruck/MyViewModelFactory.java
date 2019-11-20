package com.example.munch.ui.foodTruck;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.munch.data.model.FoodTruck;

public class MyViewModelFactory implements ViewModelProvider.Factory {

    private FoodTruck foodTruck;
    private Activity activity;


    public MyViewModelFactory(FoodTruck foodTruck, Activity activity) {
        this.foodTruck = foodTruck;
        this.activity = activity;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass == FoodTruckViewModel.class) {
            return (T) new FoodTruckViewModel(foodTruck, activity);
        }else {
            return null;
        }
    }
}
