package com.example.munch.data.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.munch.HttpRequests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    //jwts
    //private HashMap<String,String> userInfo;
    private boolean loggedIn;
    private String email;   //edit
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth_month;
    private String dateOfBirth_day;
    private String dateOfBirth_year;
    private String city;
    private String state;
    private String phoneNum; //edit
    private ArrayList<Review> reviews;
    private ArrayList<FoodTruck> foodTrucks;
    private ArrayList<FoodTruck> favorites;

    //, String gender, String city, String state, String phoneNum
    public LoggedInUser () {
        signOut();
    }
    public void register(String password, String email, String firstName, String lastName, String day, String month, String year) {

        //Create JSON Object to be passed through authentication
        //Send Info to Back End
        JSONObject user = new JSONObject();
        try {
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            user.put("email", email);
            user.put("password", password);
            user.put("dateOfBirth", getISOdob(year,day,month));
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
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth_month = month;
        this.dateOfBirth_day = day;
        this.dateOfBirth_year = year;
        this.gender = "Complete your profile";
        this.city = "Complete your profile";
        this.state = "Complete your profile";
        this.phoneNum = "Complete your profile";

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
        return dateOfBirth_month + " " +dateOfBirth_day +", " +dateOfBirth_year;
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

    public void signOut(){
        loggedIn = false;
        this.lastName = "";
        this.firstName = "Guest";
        this.gender = "";
        this.city = "";
        this.state = "";
        this.phoneNum = "";
        this.email = "";
        this.dateOfBirth_month = "";
        this.dateOfBirth_year = "";
        this.dateOfBirth_day = "";
    }

    private static int getMonth (String month) {
        int monthNum = -1;
        switch (month) {
            case "January": monthNum = 0;
                break;
            case "February": monthNum = 1;
                break;
            case "March": monthNum = 2;
                break;
            case "April": monthNum = 3;
                break;
            case "May": monthNum = 4;
                break;
            case "June": monthNum = 5;
                break;
            case "July": monthNum = 6;
                break;
            case "August": monthNum = 7;
                break;
            case "September": monthNum = 8;
                break;
            case "October": monthNum = 9;
                break;
            case "November": monthNum = 10;
                break;
            case "December": monthNum = 11;
                break;
            default: monthNum = -1;
                break;
        }
        return monthNum;
    }

    public String getISOdob (String dateOfBirth_year, String dateOfBirth_day, String dateOfBirth_month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(dateOfBirth_year), getMonth(dateOfBirth_month), Integer.valueOf(dateOfBirth_day), 00, 00, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String isoDOB = sdf.format(date);
        return isoDOB;

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
