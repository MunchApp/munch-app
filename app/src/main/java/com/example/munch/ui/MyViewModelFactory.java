package com.example.munch.ui;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.foodTruck.FoodTruckViewModel;
import com.example.munch.ui.userProfile.UserProfileViewModel;

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
        } if (modelClass == UserProfileViewModel.class) {
            return (T) new UserProfileViewModel();
        }
        else {
            return null;
        }
    }
}
