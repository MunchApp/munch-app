package com.example.munch.ui.foodTruck;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.Rating;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.munch.HttpRequests;
import com.example.munch.LocationCalculator;
import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.data.model.Review;
import com.example.munch.ui.foodTruck.reviews.ReviewListingAdapter;
import com.example.munch.ui.map.SearchListingAdapter;
import com.example.munch.ui.userProfile.UserProfileFragment;
import com.example.munch.ui.userProfile.manageTruck.TruckListingAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FoodTruckFragmentRefactor extends Fragment{

    private FoodTruckViewModel foodTruckViewModel;
    private FoodTruck foodTruck;
    private Boolean owner;
    private ImageView image;
    private TextView name;
    private ImageView statusIcon;
    private TextView status;
    private TextView address;
    private RatingBar rating;
    private TextView website;
    private TextView phone;
    private TextView sun;
    private TextView mon;
    private TextView tue;
    private TextView wed;
    private TextView thu;
    private TextView fri;
    private TextView sat;
    private TextView descrip;
    private TextView num_review;
    private ConstraintLayout hours;
    private TextView website_prompt;
    private TextView phone_prompt;
    private TextView hours_prompt;
    private TextView descrip_prompt;
    private ImageView edit_name;
    private ImageView edit_address;
    private ImageView edit_website;
    private ImageView edit_phone;
    private ImageView edit_hours;
    private ImageView edit_descrip;
    private String token;
    private Switch sw;
    private TextView distance;
    private ImageView heart;
    private RecyclerView allReviews;
    private View gap;
    private Button postReview;

    public FoodTruckFragmentRefactor(FoodTruck foodTruck, Boolean owner) {
        this.foodTruck = foodTruck;
        this.owner = owner;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Get root view
        final View root = inflater.inflate(R.layout.food_truck_activity, container, false);

        //Initialize views that may change values
        image = (ImageView) root.findViewById(R.id.truck_image);
        name = (TextView) root.findViewById(R.id.truck_name);
        statusIcon = (ImageView) root.findViewById(R.id.status);
        status = (TextView) root.findViewById(R.id.status_string);
        address = (TextView) root.findViewById(R.id.truck_address);
        rating = (RatingBar) root.findViewById(R.id.truck_rating_bar);
        website = (TextView) root.findViewById(R.id.truck_website);
        phone = (TextView) root.findViewById(R.id.truck_phone);
        sun = (TextView) root.findViewById(R.id.sun_hours);
        mon = (TextView) root.findViewById(R.id.mon_hours);
        tue = (TextView) root.findViewById(R.id.tue_hours);
        wed = (TextView) root.findViewById(R.id.wed_hours);
        thu = (TextView) root.findViewById(R.id.thu_hours);
        fri = (TextView) root.findViewById(R.id.fri_hours);
        sat = (TextView) root.findViewById(R.id.sat_hours);
        descrip = (TextView) root.findViewById(R.id.truck_descrip);
        hours = (ConstraintLayout) root.findViewById(R.id.hours);
        num_review = (TextView) root.findViewById(R.id.num_reviews);
        distance = (TextView) root.findViewById(R.id.truck_distance);
        allReviews = (RecyclerView) root.findViewById(R.id.truck_reviews);
        gap = root.findViewById(R.id.gap);

        //Initialize views users interact with
        phone_prompt = (TextView) root.findViewById(R.id.phone_prompt);
        website_prompt = (TextView) root.findViewById(R.id.website_prompt);
        hours_prompt = (TextView) root.findViewById(R.id.hours_prompt);
        descrip_prompt = (TextView) root.findViewById(R.id.descrip_prompt);
        edit_name = (ImageView) root.findViewById(R.id.edit_name);
        edit_address = (ImageView) root.findViewById(R.id.edit_address);
        edit_website = (ImageView) root.findViewById(R.id.edit_website);
        edit_phone = (ImageView) root.findViewById(R.id.edit_phone);
        edit_hours = (ImageView) root.findViewById(R.id.edit_hours);
        edit_descrip = (ImageView) root.findViewById(R.id.edit_descrip);
        sw = (Switch) root.findViewById(R.id.switch_status);
        heart = (ImageView) root.findViewById(R.id.favorite_heart);
        postReview = root.findViewById(R.id.add_review);

        //Get View Model
        foodTruckViewModel =
                ViewModelProviders.of(this,new MyViewModelFactory(foodTruck,getActivity())).get(FoodTruckViewModel.class);

        setObservers();

    }

    private void setObservers(){
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                name.setText(newName);
            }
        };

        final Observer<String> addressObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newAddress) {
                address.setText(newAddress);
            }
        };

        final Observer<String> websiteObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newWebsite) {
                website.setText(newWebsite);
            }
        };

        final Observer<String> descriptionObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newDescription) {
                descrip.setText(newDescription);
            }
        };

        final Observer<String> phoneNumberObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newPhoneNumber) {
                phone.setText(newPhoneNumber);
            }
        };

        final Observer<String> Observer = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newWebsite) {
                name.setText(newWebsite);
            }
        };

        final Observer<String> distanceObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newDistance) {
                distance.setText(newDistance + " away");
            }
        };

        final Observer<Boolean> favoriteObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean newFavorite) {
                if (newFavorite){
                    heart.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.fv_heart_filled));
                } else {
                    heart.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.fv_heart));
                }
            }
        };

        final Observer<Float> ratingObserver = new Observer<Float>() {
            @Override
            public void onChanged(@Nullable final Float newRating) {
                rating.setRating(newRating);

            }
        };

        final Observer<Boolean> statusObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean newStatus) {
                if (newStatus){
                    statusIcon.setVisibility(View.VISIBLE);
                    status.setText("ONLINE");
                    sw.setChecked(true);
                    gap.setVisibility(View.VISIBLE);
                    ImageViewCompat.setImageTintList(statusIcon, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.onlineGreen)));
                } else {
                    statusIcon.setVisibility(View.GONE);
                    gap.setVisibility(View.GONE);
                    status.setText("OFFLINE");
                    sw.setChecked(false);
                }
            }
        };

        final Observer<Integer> numReviewsObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer newNumReviews) {
                num_review.setText(newNumReviews + " reviews");
                if (newNumReviews == 0) {
                    num_review.setText("no reviews");
                }
            }
        };

        final Observer<List<String>> photosObserver = new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> newPhotos) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                Picasso.with(getActivity()).load(newPhotos.get(0))
                        .resize(width, 650)
                        .centerCrop()
                        .into(image);
            }
        };


    }

}