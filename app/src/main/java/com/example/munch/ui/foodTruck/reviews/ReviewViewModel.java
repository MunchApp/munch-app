package com.example.munch.ui.foodTruck.reviews;

import androidx.lifecycle.MutableLiveData;

public class ReviewViewModel {

    //fields to hold objects associated with a Review to show >: possibly also post
    private MutableLiveData<String> author;
    private MutableLiveData<String> date;
    private MutableLiveData<String> reviewBody;
    private MutableLiveData<Double> rating;
    private MutableLiveData<String> forFoodTruck;

    //constructor to define all fields but "forFoodtruck", just trying to make lists of these work in
    //right now and not necessary
    public ReviewViewModel(String author, String date, String review, double rating){
        this.author.setValue(author);
        this.date.setValue(date);
        reviewBody.setValue(review);
        this.rating.setValue(rating);
    }

    //Just getters and setters for review fields
    public String getAuthor() {
        return author.getValue();
    }

    public void setAuthor(String author) {
        this.author.setValue(author);
    }

    public String getDate() {
        return date.getValue();
    }

    public void setDate(String date) {
        this.date.setValue(date);
    }

    public String getReviewBody() {
        return reviewBody.getValue();
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody.setValue(reviewBody);
    }

    public double getRating() {
        return rating.getValue();
    }

    public void setRating(double rating) {
        this.rating.setValue(rating);
    }
}
