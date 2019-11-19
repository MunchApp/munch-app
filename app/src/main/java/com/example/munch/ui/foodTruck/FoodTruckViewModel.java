package com.example.munch.ui.foodTruck;


import android.app.Activity;
import android.media.Rating;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.munch.LocationCalculator;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.userProfile.UserProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class FoodTruckViewModel extends ViewModel {


    private MutableLiveData<String> phoneNumber;
    private MutableLiveData<String> description;
    private MutableLiveData<String[][]> hours;
    private MutableLiveData<String> website;
    private MutableLiveData<Boolean> status;
    private MutableLiveData<String> address;
    private MutableLiveData<Float> rating;
    private MutableLiveData<String> name;
    private MutableLiveData<List<String>> photos;
    private MutableLiveData<Integer> numReviews;
    private MutableLiveData<String> distance;
    private MutableLiveData<Boolean> favorite;

    public FoodTruckViewModel(FoodTruck truck, Activity activity) {
        name.setValue(truck.getName());
        phoneNumber.setValue(truck.getPhoneNumber());
        description.setValue(truck.getDescription());
        hours.setValue(truck.getHours());
        website.setValue(truck.getWebsite());
        status.setValue(truck.getStatus());
        address.setValue(truck.getAddress());
        rating.setValue(truck.getAvgRating());
        photos.setValue(truck.getPhotos());
        numReviews.setValue(truck.getReviews().size());
        LocationCalculator location = new LocationCalculator(activity);
        String dist = location.getDistance(truck.getAddress(),"Current Location");
        distance.setValue(dist);
        if (UserProfileFragment.currentUser.getFavorites().contains(truck.getId())){
            favorite.setValue(true);
        } else {
            favorite.setValue(false);
        }
    }

    //getters
    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public MutableLiveData<String[][]> getHours() {
        return hours;
    }

    public MutableLiveData<String> getWebsite() {
        return website;
    }

    public MutableLiveData<Boolean> getStatus() {
        return status;
    }

    public MutableLiveData<Float> getRating() {
        return rating;
    }

    public MutableLiveData<List<String>> getPhotos() {
        return photos;
    }

    public MutableLiveData<String> getAddress() {
        return address;
    }

    public MutableLiveData<Integer> getNumReviews() {
        return numReviews;
    }

    public MutableLiveData<String> getDistance() {
        return distance;
    }

    public MutableLiveData<Boolean> getFavorite() {
        return favorite;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    //setters

    public void setPhotos(ArrayList<String> photos) {
        this.photos.setValue(photos);
    }

    public void setHours(String[][] hours) {
        this.hours.setValue(hours);
    }

    public void setAddress(String address) {
        this.address.setValue(address);
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    public void setWebsite(String website) {
        this.website.setValue(website);
    }

    public void setRating(float rating) {
        this.rating.setValue(rating);
    }

    public void setStatus(boolean status) {
        this.status.setValue(status);
    }

    public void setNumReviews(int numReviews) {
        this.numReviews.setValue(numReviews);
    }

    public void setDistance(String distance) {
        this.distance.setValue(distance);
    }

    public void setFavorite(boolean favorite) {
        this.favorite.setValue(favorite);
    }

    public void setName(String name) {
        this.name.setValue(name);
    }
}