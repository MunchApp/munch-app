package com.example.munch.ui.foodTruck;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.lifecycle.ViewModel;

import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.data.model.MunchUser;
import com.example.munch.data.model.Review;
import com.example.munch.ui.userProfile.UserProfileFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodTruckController {

    private FoodTruckViewModel foodTruckViewModel;
    private FoodTruck foodTruck;
    private String token;

    public FoodTruckController(FoodTruckViewModel foodTruckViewModel, FoodTruck foodTruck){
        this.foodTruckViewModel = foodTruckViewModel;
        this.foodTruck = foodTruck;
        token = MunchUser.getInstance().getAccessToken();
    }

    public void addReview (String content, double rating){
        String author = MunchUser.getInstance().getId();
        Review newReview = new Review(token, foodTruck.getId(),content, rating);
        ArrayList<Review> allReviews = foodTruckViewModel.getReviews().getValue();
        if (!newReview.getDate().equals("")){
            int statusCode = foodTruck.getTruck(foodTruck.getId());
            if (statusCode == 200) {
                allReviews.add(newReview);
                foodTruckViewModel.setReviews(allReviews);
                foodTruckViewModel.setRating(foodTruck.getAvgRating());
                foodTruckViewModel.setNumReviews(allReviews.size());
            }
        }
    }

    public void favorite(String id){
        int statusCode = 0;
        if (MunchUser.getInstance().getFavorites().contains(id)){
            statusCode = MunchUser.getInstance().favorite(id,"delete");
            if (statusCode == 200) {
                foodTruckViewModel.setFavorite(false);
            }
        } else {
            statusCode = MunchUser.getInstance().favorite(id, "add");
            if (statusCode == 200) {
                foodTruckViewModel.setFavorite(true);
            }
        }
    }
    public void setStatus(boolean status){
        int statusCode = foodTruck.updateTruck(token,null,status,null);
        if (statusCode == 200)
            foodTruckViewModel.setStatus(status);
    }
    public void saveEdit(String jsonField, String input){
        int statusCode = 0;
        HashMap<String, String> newVals = new HashMap<>();
        newVals.put(jsonField,input);
        statusCode = foodTruck.updateTruck(token,newVals,null,null);
        if (statusCode == 200) {
            switch (jsonField){
                case "name":
                    foodTruckViewModel.setName(input);
                    break;
                case "address":
                    foodTruckViewModel.setAddress(input);
                    break;
                case "phoneNumber":
                    foodTruckViewModel.setPhoneNumber(input);
                    break;
                case "website":
                    foodTruckViewModel.setWebsite(input);
                    break;
                case "description":
                    foodTruckViewModel.setDescription(input);
                    break;
            }
        }
    }

    public void saveHours(String dayOfWeek, boolean open, TimePicker startTime, TimePicker endTime){
        int statusCode = 0;
        String[][] hours = foodTruck.getHours();
        int dayInt = 0;
        switch(dayOfWeek)
        {
            case "Sunday":
                dayInt =0;
                break;
            case "Monday":
                dayInt =1;
                break;
            case "Tuesday":
                dayInt =2;
                break;
            case "Wednesday":
                dayInt =3;
                break;
            case "Thursday":
                dayInt =4;
                break;
            case "Friday":
                dayInt =5;
                break;
            case "SATURDAY":
                dayInt =6;
                break;
            default:
                System.out.println("error");
        }
        if (!open){
            hours[dayInt][0] = "99:99";
            hours[dayInt][1] = "99:99";
        } else {
            hours[dayInt][0] = parseTime(startTime);
            hours[dayInt][1] = parseTime(endTime);
        }
        statusCode = foodTruck.updateTruck(token,null,null, hours);
        if (statusCode == 200){
            foodTruckViewModel.setHours(foodTruck.getRegHours());
        }
    }

    //todo check this method
    private String parseTime(TimePicker start){
        String time = "";
        if (start.getHour() < 10){
            time += "0" + start.getHour() + ":";
        } else {
            time += start.getHour() + ":";
        }
        if (start.getMinute() < 10){
            time += "0" + start.getMinute();
        } else {
            time += start.getMinute();
        }
        return time;
    }

}
