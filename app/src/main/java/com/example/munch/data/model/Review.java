package com.example.munch.data.model;

import com.example.munch.HttpRequests;
import com.example.munch.MunchTools;
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
        HttpRequests reviewRequests = new HttpRequests(HttpRequests.Route.MUNCH);
        reviewRequests.execute("reviews", "POST", JSONReview.toString(), token);
        String responseLogin = MunchTools.callMunchRoute(reviewRequests);
        jsonToReview(responseLogin);

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

    private int getReview(String id){
        HttpRequests reviewRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        if (id != null) {
            reviewRequest.execute("reviews/" + id, "GET");
            String responseReview = MunchTools.callMunchRoute(reviewRequest);
            jsonToReview(responseReview);
        }

        int statusCode = reviewRequest.getStatusCode();
        return statusCode;
    }

    public String getFoodTruckName() {
        return foodTruckName;
    }

    private void jsonToReview (String response){
        try {
            JSONObject jsonReview = new JSONObject(response);
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
            HttpRequests userRequest = new HttpRequests(HttpRequests.Route.MUNCH);
            userRequest.execute("users/" + author, "GET");
            String responseReview = MunchTools.callMunchRoute(userRequest);
            authorName = MunchTools.getValueFromJson("firstName",responseReview)+ " " + MunchTools.getValueFromJson("lastName", responseReview);
            authorPicture = MunchTools.getValueFromJson("lastName", responseReview);
        }

        if (!forFoodTruck.equals("")){
            HttpRequests truckRequest = new HttpRequests(HttpRequests.Route.MUNCH);
            String token = MunchUser.getInstance().getAccessToken();
            truckRequest.execute("foodtrucks/" + forFoodTruck, "GET");
            String responseReview = MunchTools.callMunchRoute(truckRequest);
            foodTruckName = MunchTools.getValueFromJson("name", responseReview);
        }

    }

    //Just getters and setters for review fields
    public String getAuthor() {
        return author;
    }

    public String getForFoodTruck() {return getForFoodTruck();}

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

