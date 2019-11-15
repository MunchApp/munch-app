package com.example.munch.data.model;

import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.munch.HttpRequests;
import com.example.munch.R;
import com.example.munch.ui.foodTruck.reviews.ReviewListing;
import com.example.munch.ui.foodTruck.reviews.ReviewListingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Review {

    //fields to hold objects associated with a Review to show >: possibly also post
    private String author;
    private String date;
    private String reviewBody;
    private double rating;
    private String forFoodTruck;
    String serverURL = "https://munch-server.herokuapp.com/";
    //constructor for already existing reviews
    public Review(String reviewId){
        getReview(reviewId);
    }
    //constructor to define all fields but "forFoodtruck", just trying to make lists of these work in
    public Review(String author, String date, String review, double rating){
        this.author=author;
        this.date=date;
        reviewBody=review;
        this.rating=rating;
    }

    private void getReview(String id){
        HttpRequests reviewRequest = new HttpRequests();
        if (id != null) {
            reviewRequest.execute(serverURL + "reviews/foodtruck/" + id, "GET");
            String responseReview = null;
            try {
                responseReview = reviewRequest.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonReview = new JSONObject(responseReview);
            } catch (JSONException e){

            }
        }
    }

    private void jsonToReview (JSONObject jsonReview){
        try {
            author = jsonReview.getString("reviewer");
            date = jsonReview.getString("date");
            reviewBody = jsonReview.getString("comment");
            rating = jsonReview.getDouble("rating");
        } catch (JSONException e){

        }

    }

    //Just getters and setters for review fields
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}

