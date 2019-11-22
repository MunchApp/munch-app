package com.example.munch.data.model;

import com.example.munch.Config;
import com.example.munch.HttpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    // get currentUser : UserProfileFragment.currentUser
    //jwts
    //private HashMap<String,String> userInfo;
    private String id;
    private String accessToken;
    private String refreshToken;
    private boolean loggedIn;
    private String email;   //edit
    private String picture = "https://www.warnersstellian.com/Content/images/product_image_not_available.png";
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth_month;
    private String dateOfBirth_day;
    private String dateOfBirth_year;
    private String city;
    private String state;
    private String phoneNum; //edit
    private ArrayList<String> reviews;
    private ArrayList<String> foodTrucks;
    private ArrayList<String> favorites;
    String serverURL = "https://munch-server.herokuapp.com/";

    //, String gender, String city, String state, String phoneNum
    public LoggedInUser() {
        signOut();
    }

    public int login(String email, String password) {
        JSONObject logUser = new JSONObject();
        try {
            logUser.put("email", email);
            logUser.put("password", password);
        } catch (JSONException ex) {
            System.out.println("Login Failed");
        }
        HttpRequests logRequests = new HttpRequests();
        logRequests.execute(serverURL + "login", "POST", logUser.toString());
        String responseLogin = null;
        try {
            responseLogin = logRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonToken = new JSONObject(responseLogin);
            accessToken = jsonToken.get("token").toString();

        } catch (JSONException e) {

        }

        int statusCode = logRequests.getStatusCode();
        if (statusCode == 200) {
            loggedIn = true;
            HttpRequests proRequests = new HttpRequests();
            proRequests.execute(serverURL + "profile", "GET", null, accessToken);
            String responseProfile = null;
            try {
                responseProfile = proRequests.get();
                JSONObject allVals = new JSONObject(responseProfile);
                jsonToUser(allVals);

            } catch (ExecutionException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }
        return statusCode;
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
            user.put("dateOfBirth", getISOdob(year, day, month));
        } catch (JSONException ex) {
            System.out.println("Login Failed");
        }
        HttpRequests regRequests = new HttpRequests();
        regRequests.execute(serverURL + "register", "POST", user.toString());
        String response = null;
        try {
            response = regRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean getLoggedIn() {
        return loggedIn;
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

    public String getId() {
        return id;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth_month + " " + dateOfBirth_day + ", " + dateOfBirth_year;
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

    public void signOut() {
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
        this.id = "NOT LOGGED IN";
        this.foodTrucks = new ArrayList<String>();
        this.reviews = new ArrayList<String>();
        this.favorites = new ArrayList<String>();
    }

    private static int getMonth(String month) {
        int monthNum = -1;
        switch (month) {
            case "January":
                monthNum = 0;
                break;
            case "February":
                monthNum = 1;
                break;
            case "March":
                monthNum = 2;
                break;
            case "April":
                monthNum = 3;
                break;
            case "May":
                monthNum = 4;
                break;
            case "June":
                monthNum = 5;
                break;
            case "July":
                monthNum = 6;
                break;
            case "August":
                monthNum = 7;
                break;
            case "September":
                monthNum = 8;
                break;
            case "October":
                monthNum = 9;
                break;
            case "November":
                monthNum = 10;
                break;
            case "December":
                monthNum = 11;
                break;
            default:
                monthNum = -1;
                break;
        }
        return monthNum;
    }

    private String getISOdob(String dateOfBirth_year, String dateOfBirth_day, String dateOfBirth_month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(dateOfBirth_year), getMonth(dateOfBirth_month), Integer.valueOf(dateOfBirth_day), 00, 00, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String isoDOB = sdf.format(date);
        return isoDOB;

    }

    public ArrayList<String> getFoodTrucks() {
        return foodTrucks;
    }

    public void addTruck(String truck) {
        foodTrucks.add(truck);
    }

    private void jsonToUser(JSONObject jsonUser) {
        try {
            String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = sdf.parse(jsonUser.get("dateOfBirth").toString());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date); // don't forget this if date is arbitrary e.g. 01-01-2014
            int month = cal.get(Calendar.MONTH); // 6
            int day = cal.get(Calendar.DAY_OF_MONTH); // 17
            int year = cal.get(Calendar.YEAR); //169
            this.email = jsonUser.get("email").toString();
            this.firstName = jsonUser.get("firstName").toString();
            this.lastName = jsonUser.get("lastName").toString();
            this.id = jsonUser.get("id").toString();
            this.picture = jsonUser.get("picture").toString();
            this.dateOfBirth_month = monthNames[month];
            this.dateOfBirth_day = String.valueOf(day);
            this.dateOfBirth_year = String.valueOf(year);
            //this.gender = jsonUser.get("gender").toString();
            this.city = jsonUser.get("city").toString();
            this.state = jsonUser.get("state").toString();
            this.phoneNum = jsonUser.get("phoneNumber").toString();
            JSONArray JSONreviews = new JSONArray(jsonUser.get("reviews").toString());
            JSONArray JSONfoodTrucks = new JSONArray(jsonUser.get("ownedFoodTrucks").toString());
            JSONArray JSONfavorites = new JSONArray(jsonUser.get("favorites").toString());

            for (int c = 0; c < JSONreviews.length(); c++) {
                this.reviews.add(JSONreviews.get(c).toString());
            }

            for (int c = 0; c < JSONfoodTrucks.length(); c++) {
                this.foodTrucks.add(JSONfoodTrucks.get(c).toString());
            }

            for (int c = 0; c < JSONfavorites.length(); c++) {
                this.favorites.add(JSONfavorites.get(c).toString());
            }

        } catch (JSONException | ParseException e) {

        }
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

     publicvoid setPhoneNum(String phoneNum) {
         this.phoneNum = phoneNum;
     }

     public void setLastName(String lastName) {
         this.lastName = lastName;
     }

     public void setAddress(String city) {
         this.city = city;
     }*/
    public int addFavorite(String foodTruckId) {
        HttpRequests reviewRequest = new HttpRequests();
        if (foodTruckId != null) {
            reviewRequest.execute(serverURL + "foodtrucks/" + foodTruckId + "?action=add", "GET");
            String responseReview = null;
            try {
                responseReview = reviewRequest.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonReview = new JSONObject(responseReview);
            } catch (JSONException e) {

            }
        }
        int statusCode = reviewRequest.getStatusCode();
        if (statusCode == 200) {
            favorites.add(foodTruckId);
        }
        return statusCode;
    }

    public int deleteFavorite(String foodTruckId) {
        HttpRequests reviewRequest = new HttpRequests();
        if (foodTruckId != null) {
            reviewRequest.execute(serverURL + "foodtrucks/" + foodTruckId + "?action=delete", "GET");
            String responseReview = null;
            try {
                responseReview = reviewRequest.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonReview = new JSONObject(responseReview);
            } catch (JSONException e) {

            }
        }
        int statusCode = reviewRequest.getStatusCode();
        if (statusCode == 200) {
            favorites.remove(foodTruckId);
        }
        return statusCode;
    }

    public int update(HashMap<String, String> vals) {
        JSONObject user = new JSONObject();
        if (vals != null) {
            for (String key : vals.keySet()) {
                try {
                    user.put(key, vals.get(key));
                } catch (JSONException ex) {
                }
            }
        }
        HttpRequests updateRequest = new HttpRequests();

        updateRequest.execute(serverURL + "profile", "PUT", user.toString(), accessToken);
        String response = null;
        try {
            response = updateRequest.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonUser = new JSONObject(response);
            jsonToUser(jsonUser);
        } catch (JSONException e) {

        }

        int statusCode = updateRequest.getStatusCode();
        return statusCode;
    }

    public int uploadProfilePic() {
        HttpRequests imageRequests = new HttpRequests();
        imageRequests.execute(serverURL + "profile/upload", "PUT", null, accessToken, Config.profileImage);
        String response = "";
        try {
            response = imageRequests.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        HttpRequests userRequest = new HttpRequests();
        userRequest.execute(serverURL + "users/" + this.id, "GET");
        String responseReview = null;
        try {
            responseReview = userRequest.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject JSONUser = new JSONObject(responseReview);
            picture = JSONUser.getString("picture");
        } catch (JSONException e) {

        }

        return imageRequests.getStatusCode();
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public String getPicture() {
        return picture;
    }
}
