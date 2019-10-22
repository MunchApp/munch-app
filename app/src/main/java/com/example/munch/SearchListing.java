package com.example.munch;

public class SearchListing {

    private String truckName;
    private int mPic1;
    private int mPic2;
    private int mPic3;
    private double rating;
    private int phoneNumber;
    private String distance;


    public SearchListing(String truckName, int mPic1, int mPic2, int mPic3,
                         double rating, int phoneNumber, String distance){
        this.truckName = truckName;
        this.mPic1 = mPic1;
        this.mPic2 = mPic2;
        this.mPic3 = mPic3;
        this.truckName = truckName;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.distance = distance;
    }


    public String getTruckName() {
        return truckName;
    }

    public void setTruckName(String truckName) {
        this.truckName = truckName;
    }

    public int getmPic1() {
        return mPic1;
    }

    public void setmPic1(int mPic1) {
        this.mPic1 = mPic1;
    }

    public int getmPic2() {
        return mPic2;
    }

    public void setmPic2(int mPic2) {
        this.mPic2 = mPic2;
    }

    public int getmPic3() {
        return mPic3;
    }

    public void setmPic3(int mPic3) {
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
