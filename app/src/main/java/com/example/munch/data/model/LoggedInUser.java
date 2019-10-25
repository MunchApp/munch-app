package com.example.munch.data.model;

import android.content.SharedPreferences;

import com.example.munch.HttpRequests;

import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    //jwts
    private boolean loggedIn;
    private String email;   //edit
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String city;
    private String state;
    private String phoneNum; //edit
    private ArrayList<Review> reviews;
    private ArrayList<FoodTruck> foodTrucks;
    private ArrayList<FoodTruck> favorites;

    //, String gender, String city, String state, String phoneNum
    public LoggedInUser () {
        loggedIn = false;
    }
    public void register(String password, String email, String firstName, String lastName, String dateOfBirth) {

        //Create JSON Object to be passed through authentication
        //Send Info to Back End
        JSONObject user = new JSONObject();
        try {
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            user.put("email", email);
            user.put("password", password);
            user.put("dateOfBirth", dateOfBirth);
        } catch (JSONException ex) {
            System.out.println("Login Failed");
        }
        HttpRequests httpRequests = new HttpRequests();
        httpRequests.execute("register", "POST", user.toString());
        String response = null;
        try {
            response = httpRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        loggedIn = true;


        //Get accessToken?
        //Get - Get info from User Database
        //Set variables





        this.email = email;
        this.firstName = lastName;
        this.dateOfBirth = dateOfBirth;



        //this.gender = gender;
        //this.city = city;
        //this.state = state;
        //this.phoneNum = phoneNum;

    }
    public boolean getLoggedIn() { return loggedIn;}

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return city + "," + state;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

   /* public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String city) {
        this.city = city;
    }*/
}
