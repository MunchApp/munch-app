package com.example.munch.ui.map;

import android.widget.Button;

public class SearchListing {

    private String truckName;
    private String mPic1;
    private String mPic2;
    private String mPic3;
    private double rating;
    private int phoneNumber;
    private String distance;
    Button mTruckButton;


    public SearchListing(String truckName, String mPic1, String mPic2, String mPic3,
                         double rating, int phoneNumber, String distance, Button mTruckButton){
        this.truckName = truckName;
        this.mPic1 = mPic1;
        this.mPic2 = mPic2;
        this.mPic3 = mPic3;
        this.truckName = truckName;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.distance = distance;
        this.mTruckButton = mTruckButton;
    }


    public String getTruckName() {
        return truckName;
    }

    public void setTruckName(String truckName) {
        this.truckName = truckName;
    }


    public String getmPic1() {
        return mPic1;
    }

    public void setmPic1(String mPic1) {
        this.mPic1 = mPic1;
    }


    public String getmPic2() {
        return mPic2;
    }

    public void setmPic2(String mPic2) {
        this.mPic2 = mPic2;
    }


    public String getmPic3() {
        return mPic3;
    }

    public void setmPic3(String mPic3) {
        this.mPic3 = mPic3;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
