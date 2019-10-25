package com.example.munch.ui.foodTruck;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoodTruckViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FoodTruckViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}