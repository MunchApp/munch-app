package com.example.munch.ui.foodTruck;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.munch.data.model.Review;

public class CreateTruckViewModel extends ViewModel {

    String name;
    String address;
    String[] photos;
    String[][] hours;

    public CreateTruckViewModel() {
        hours = new String[7][2];
    }

    public String getName() {
        return name;
    }

    public String[] getPhotos() {
        return photos;
    }

    public String getAddress() {
        return address;
    }

    public String[][] getHours() {
        return hours;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHours(int day, String start, String end) {
        this.hours[day][0] = start;
        this.hours[day][1] = end;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }
}