package com.example.munch.data.model;

import com.example.munch.Config;
import com.example.munch.HttpRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MunchUser {
    private volatile static MunchUser uniqueInstance;

    //From database
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String phoneNumber;
    private String city;
    private String state;
    private String picture;
    private ArrayList<String> reviews;
    private ArrayList<String> foodTrucks;
    private ArrayList<String> favorites;

    //Front End Only
    private String accessToken;
    private Boolean loggedIn;
    private String dobMonth;
    private String dobDay;
    private String dobYear;


    private MunchUser() {
        clear();
    }

    public static MunchUser getInstance() {
        if (uniqueInstance == null) {
            synchronized (MunchUser.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new MunchUser();
                }
            }
        }
        return uniqueInstance;
    }

    public void clear() {
        loggedIn = false;
        this.lastName = null;
        this.firstName = null;
        this.city = null;
        this.state = null;
        this.phoneNumber = null;
        this.email = null;
        this.dobMonth = null;
        this.dobYear = null;
        this.dobDay = null;
        this.id = null;
        this.foodTrucks = new ArrayList<String>();
        this.reviews = new ArrayList<String>();
        this.favorites = new ArrayList<String>();
    }

    public int login(HashMap<String, String> loginInfo) {
        JSONObject jsonLoginInfo = new JSONObject(loginInfo);
        HttpRequests loginRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        loginRequest.execute("login", "POST", jsonLoginInfo.toString());
        String loginResponse = callMunchRoute(loginRequest);
        accessToken = getValueFromJson("token", loginResponse);
        String jsonUser = getValueFromJson("userObject", loginResponse);
        jsonToUser(jsonUser);

        int statusCode = loginRequest.getStatusCode();
        return statusCode;
    }

    public int register(HashMap<String, String> registerInfo) {
        JSONObject jsonRegisterInfo = new JSONObject(registerInfo);
        HttpRequests registerRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        registerRequest.execute("register", "POST", jsonRegisterInfo.toString());
        callMunchRoute(registerRequest);

        int statusCode = registerRequest.getStatusCode();
        return statusCode;
    }

    public int favorite(String foodTruckId, String action) {
        HttpRequests favoriteRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        favoriteRequest.execute("foodtrucks/" + foodTruckId + "?action=" + action, "GET");
        callMunchRoute(favoriteRequest);

        int statusCode = favoriteRequest.getStatusCode();
        if (statusCode == 200 && action.equals("add")) {
            favorites.add(foodTruckId);
        } else if (statusCode == 200 && action.equals("delete")){
            favorites.remove(foodTruckId);
        }
        return statusCode;
    }

    public int updateUserInfo(HashMap<String, String> newUserInfo) {
        JSONObject jsonUser = new JSONObject(newUserInfo);
        HttpRequests updateRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        updateRequest.execute("profile", "PUT", jsonUser.toString(), accessToken);
        String updateResponse = callMunchRoute(updateRequest);
        jsonToUser(updateResponse);

        int statusCode = updateRequest.getStatusCode();
        return statusCode;
    }

    public int uploadProfilePic() {
        HttpRequests imageRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        imageRequest.execute("profile/upload", "PUT", null, accessToken, Config.profileImage);
        callMunchRoute(imageRequest);

        HttpRequests userRequest = new HttpRequests(HttpRequests.Route.MUNCH);
        userRequest.execute("users/" + this.id, "GET");
        String userResponse = callMunchRoute(userRequest);
        this.picture = getValueFromJson("picture", userResponse);

        int statusCode = imageRequest.getStatusCode();
        return statusCode;
    }

    private String callMunchRoute(HttpRequests request) {
        String response = null;
        try {
            response = request.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getValueFromJson(String value, String response) {
        String returnValue = "";
        try {
            JSONObject jsonToken = new JSONObject(response);
            returnValue = jsonToken.get(value).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    private void jsonToUser(String response) {
        try {
            JSONObject jsonUser = new JSONObject(response);
            this.id = jsonUser.get("id").toString();
            this.firstName = jsonUser.get("firstName").toString();
            this.lastName = jsonUser.get("lastName").toString();
            this.email = jsonUser.get("email").toString();
            this.city = jsonUser.get("city").toString();
            this.state = jsonUser.get("state").toString();
            this.dateOfBirth = jsonUser.get("dateOfBirth").toString();
            this.phoneNumber = jsonUser.get("phoneNumber").toString();
            this.picture = jsonUser.get("picture").toString();
            JSONArray JSONreviews = new JSONArray(jsonUser.get("reviews").toString());
            JSONArray JSONfoodTrucks = new JSONArray(jsonUser.get("ownedFoodTrucks").toString());
            JSONArray JSONfavorites = new JSONArray(jsonUser.get("favorites").toString());

            this.reviews = new ArrayList<>();
            for (int c = 0; c < JSONreviews.length(); c++) {
                this.reviews.add(JSONreviews.get(c).toString());
            }

            this.foodTrucks = new ArrayList<>();
            for (int c = 0; c < JSONfoodTrucks.length(); c++) {
                this.foodTrucks.add(JSONfoodTrucks.get(c).toString());
            }

            this.favorites = new ArrayList<>();
            for (int c = 0; c < JSONfavorites.length(); c++) {
                this.favorites.add(JSONfavorites.get(c).toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addTruck(String foodTruckId){
        this.foodTrucks.add(foodTruckId);
    }

    //Getters
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPicture() {
        return picture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public ArrayList<String> getFoodTrucks() {
        return foodTrucks;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public String getDobDay() {
        return dobDay;
    }

    public String getDobMonth() {
        return dobMonth;
    }

    public String getDobYear() {
        return dobYear;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
