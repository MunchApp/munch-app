package com.example.munch.ui.userProfile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.munch.data.model.MunchUser;
import com.example.munch.data.model.Review;

import java.util.ArrayList;
import java.util.List;

public class UserProfileViewModel extends ViewModel {


    private MutableLiveData<Boolean> loggedIn;
    private MutableLiveData<String> firstName;
    private MutableLiveData<String> lastName;
    private MutableLiveData<String> city;
    private MutableLiveData<String> state;
    private MutableLiveData<String> picture;
    private MutableLiveData<String> email;
    private MutableLiveData<String> phoneNumber;
    private MutableLiveData<String> dateOfBirth;
    private MutableLiveData<ArrayList<String>> reviews;
    private MutableLiveData<ArrayList<String>> foodTrucks;
    private MutableLiveData<ArrayList<String>> favorites;
    private MutableLiveData<String> pictureBitmap;

    public UserProfileViewModel(){
        loggedIn = new MutableLiveData<>();
        firstName = new MutableLiveData<>();
        lastName = new MutableLiveData<>();
        city = new MutableLiveData<>();
        state = new MutableLiveData<>();
        picture = new MutableLiveData<>();
        email =new MutableLiveData<>();
        phoneNumber = new MutableLiveData<>();
        dateOfBirth = new MutableLiveData<>();
        reviews = new MutableLiveData<>();
        foodTrucks = new MutableLiveData<>();
        favorites = new MutableLiveData<>();
        pictureBitmap = new MutableLiveData<>();

        MunchUser currentUser = MunchUser.getInstance();
        loggedIn.setValue(currentUser.getLoggedIn());
        if (loggedIn.getValue()){
            firstName.setValue(currentUser.getFirstName());
            lastName.setValue(currentUser.getLastName());
            city.setValue(currentUser.getCity());
            state.setValue(currentUser.getState());
            picture.setValue(currentUser.getPicture());
            dateOfBirth.setValue(currentUser.getDateOfBirth());
            foodTrucks.setValue(currentUser.getFoodTrucks());
            favorites.setValue(currentUser.getFavorites());
            reviews.setValue(currentUser.getReviews());
            email.setValue(currentUser.getEmail());
            phoneNumber.setValue(currentUser.getPhoneNumber());
        } else {
            firstName.setValue("Guest");
            lastName.setValue("");
            city.setValue("Austin");
            state.setValue("Texas");
            picture.setValue("https://www.warnersstellian.com/Content/images/product_image_not_available.png");
            email.setValue("Complete your profile");
            phoneNumber.setValue("Complete your profile");
            dateOfBirth.setValue("Complete your profile");
        }
        pictureBitmap.setValue(null);
    }

    //getters

    public MutableLiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public MutableLiveData<String> getLastName() {
        return lastName;
    }

    public MutableLiveData<String> getFirstName() {
        return firstName;
    }

    public MutableLiveData<String> getCity() {
        return city;
    }

    public MutableLiveData<String> getDateOfBirth() {
        return dateOfBirth;
    }

    public MutableLiveData<ArrayList<String>> getFavorites() {
        return favorites;
    }

    public MutableLiveData<ArrayList<String>> getFoodTrucks() {
        return foodTrucks;
    }

    public MutableLiveData<ArrayList<String>> getReviews() {
        return reviews;
    }

    public MutableLiveData<String> getPicture() {
        return picture;
    }

    public MutableLiveData<String> getState() {
        return state;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getPictureBitmap() {
        return pictureBitmap;
    }

    //setters

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn.setValue(loggedIn);
    }

    public void setLastName(String lastName) {
        this.lastName.setValue(lastName);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    public void setFirstName(String firstName) {
        this.firstName.setValue(firstName);
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.setValue(dateOfBirth);
    }

    public void setCity(String city) {
        this.city.setValue(city);
    }

    public void setPicture(String picture) {
        this.picture.setValue(picture);
    }

    public void setState(String state) {
        this.state.setValue(state);
    }

    public void setFavorites(ArrayList<String> favorites) {
        this.favorites.setValue(favorites);
    }

    public void setFoodTrucks(ArrayList<String> foodTrucks) {
        this.foodTrucks.setValue(foodTrucks);
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews.setValue(reviews);
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public void setPictureBitmap(String pictureBitmap) {
        this.pictureBitmap.setValue(pictureBitmap);
    }
}