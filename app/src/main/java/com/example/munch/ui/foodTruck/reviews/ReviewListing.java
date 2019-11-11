package com.example.munch.ui.foodTruck.reviews;

public class ReviewListing {

    //fields to hold objects associated with a Review to show >: possibly also post
    private String author;
    private String date;
    private String reviewBody;
    private double rating;
    private String forFoodTruck;

    //constructor to define all fields but "forFoodtruck", just trying to make lists of these work in
    //right now and not neccesary
    public ReviewListing(String author, String date, String review, double rating){
        this.author=author;
        this.date=date;
        reviewBody=review;
        this.rating=rating;
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
