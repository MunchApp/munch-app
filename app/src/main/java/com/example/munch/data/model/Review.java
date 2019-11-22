package com.example.munch.data.model;

import com.example.munch.HttpRequests;
import com.example.munch.ui.userProfile.UserProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Review {

    //fields to hold objects associated with a Review to show >: possibly also post
    private String author;
    private String authorName;
    private String date;
    private String reviewBody;
    private double rating;
    private String authorPicture;
    private String origin;
    private String forFoodTruck;
    private String foodTruckName;
    String serverURL = "https://munch-server.herokuapp.com/";
    //constructor for already existing reviews
    public Review(String reviewId){
        getReview(reviewId);
    }
    //constructor to define all fields but "forFoodtruck", just trying to make lists of these work in
    public Review(String token, String truckId, String review, double rating){
        JSONObject JSONReview = new JSONObject();
        try {
            JSONReview.put("foodTruck", truckId);
            JSONReview.put("comment", review);
            JSONReview.put("rating", rating);
        } catch (JSONException ex) {
            System.out.println("Post Review Failed");
        }
        HttpRequests reviewRequests = new HttpRequests();
        reviewRequests.execute(serverURL + "reviews", "POST", JSONReview.toString(), token);
        String responseLogin = null;
        try {
            responseLogin = reviewRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonReview = new JSONObject(responseLogin);
            jsonToReview(jsonReview);
        } catch (JSONException e){

        }

        int status = reviewRequests.getStatusCode();
        System.out.println(status);
    }

    public String getAuthorName() {

        return authorName;
    }
    public String getAuthorPicture() {

        if (authorPicture != null)
            return authorPicture;
        else
            return "https://www.warnersstellian.com/Content/images/product_image_not_available.png";
    }

    private void getReview(String id){
        HttpRequests reviewRequest = new HttpRequests();
        if (id != null) {

            reviewRequest.execute(serverURL + "reviews/" + id, "GET");
            String responseReview = null;
            try {
                responseReview = reviewRequest.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonReview = new JSONObject(responseReview);
                jsonToReview(jsonReview);
            } catch (JSONException e){

            }
        }

    }

    public String getFoodTruckName() {
        return foodTruckName;
    }

    private void jsonToReview (JSONObject jsonReview){
        try {
            author = jsonReview.getString("reviewer");
            authorName = jsonReview.getString("reviewerName");
            date = jsonReview.getString("date");
            reviewBody = jsonReview.getString("comment");
            rating = jsonReview.getDouble("rating");
            origin = jsonReview.getString("origin");
            forFoodTruck = jsonReview.getString("foodTruck");
        } catch (JSONException e){

        }
        if (!author.equals("")){
            HttpRequests userRequest = new HttpRequests();
            userRequest.execute(serverURL + "users/" + author, "GET");
            String responseReview = null;
            try {
                responseReview = userRequest.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                JSONObject JSONUser= new JSONObject(responseReview);
                authorName = JSONUser.getString("firstName") + " " + JSONUser.getString("lastName");
                authorPicture = JSONUser.getString("picture");
            } catch (JSONException e){

            }
        }
        if (!forFoodTruck.equals("")){
            HttpRequests truckRequest = new HttpRequests();
            String token = UserProfileFragment.currentUser.getAccessToken();
            truckRequest.execute(serverURL + "foodtrucks/" + forFoodTruck, "GET");
            String responseReview = null;
            try {
                responseReview = truckRequest.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                JSONObject JSONUser= new JSONObject(responseReview);
                foodTruckName = JSONUser.getString("name");
            } catch (JSONException e){

            }
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

