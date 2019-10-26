package com.example.munch.data.model;

import com.example.munch.HttpRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    // get currentUser : UserProfileFragment.currentUser
    //jwts
    //private HashMap<String,String> userInfo;
    private String accessToken;
    private String refreshToken;
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
    public void login(String email, String password){
        JSONObject logUser = new JSONObject();
        try {
            logUser.put("email", email);
            logUser.put("password", password);
        } catch (JSONException ex) {
            System.out.println("Login Failed");
        }
        HttpRequests logRequests = new HttpRequests();
        logRequests.execute("login", "POST", logUser.toString());
        String responseLogin = null;
        try {
            responseLogin = logRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        accessToken = responseLogin;
        if ( accessToken != null && !accessToken.equals(""))
            loggedIn = true;

//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.dateOfBirth_month = month;
//        this.dateOfBirth_day = day;
//        this.dateOfBirth_year = year;
//        this.gender = "Complete your profile";
//        this.city = "Complete your profile";
//        this.state = "Complete your profile";
//        this.phoneNum = "Complete your profile";
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
        HttpRequests regRequests = new HttpRequests();
        regRequests.execute("register", "POST", user.toString());
        String response = null;
        try {
            response = regRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        login(email,password);


        //This is wrong but keep until GET routes are established
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

    public String getAccessToken() { return accessToken;}
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
