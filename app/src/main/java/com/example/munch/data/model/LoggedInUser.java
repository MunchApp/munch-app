package com.example.munch.data.model;

import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    //jwts
    private String email;   //edit
    private String firstName;
    private String lastName;
    private String gender;
    private Date dateOfBirth;
    private String city;
    private String state;
    private String phoneNum; //edit
    private ArrayList<Review> reviews;
    private ArrayList<FoodTruck> foodTrucks;
    private ArrayList<FoodTruck> favorites;

    //, String gender, String city, String state, String phoneNum
    public LoggedInUser(String password, String email, String firstName, String lastName, Date dateOfBirth) {

        //Create JSON Object to be passed through authentication
        try {
            JSONObject user = new JSONObject();
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            user.put("email", email);
            user.put("password", password);
            user.put("dateOfBirth", dateOfBirth);
        } catch (JSONException ex) {
            System.out.println("Login Failed");
        }

        //Send info to User Database


        //Get accessToken?

        //Get - Get info from User Database
        //Set variables





        /*this.email = email;
        this.firstName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.state = state;
        this.phoneNum = phoneNum;*/

    }

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

    public Date getDateOfBirth() {
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
