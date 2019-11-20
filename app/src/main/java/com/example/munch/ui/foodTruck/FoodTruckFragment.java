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

public class FoodTruckFragment extends Fragment{

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
    /*private ImageView edit_name;
    private ImageView edit_address;
    private ImageView edit_website;
    private ImageView edit_phone;
    private ImageView edit_hours;
    private ImageView edit_descrip;*/
    private String token;
    private Switch sw;
    private TextView distance;
    private ImageView heart;
    private RecyclerView allReviews;
    private View gap;
    private Button postReview;
    private HashMap<ImageView, String> editButtons;
    private HashMap<TextView,View> infoButtons;

    public FoodTruckFragment(FoodTruck foodTruck, Boolean owner) {
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
        ImageView edit_name = (ImageView) root.findViewById(R.id.edit_name);
        ImageView edit_address = (ImageView) root.findViewById(R.id.edit_address);
        ImageView edit_website = (ImageView) root.findViewById(R.id.edit_website);
        ImageView edit_phone = (ImageView) root.findViewById(R.id.edit_phone);
        ImageView edit_hours = (ImageView) root.findViewById(R.id.edit_hours);
        ImageView edit_descrip = (ImageView) root.findViewById(R.id.edit_descrip);
        sw = (Switch) root.findViewById(R.id.switch_status);
        heart = (ImageView) root.findViewById(R.id.favorite_heart);
        postReview = root.findViewById(R.id.add_review);

        editButtons = new HashMap<>();
        editButtons.put(edit_name, "name");
        editButtons.put(edit_address, "address");
        editButtons.put(edit_website, "website");
        editButtons.put(edit_phone, "phoneNumber");
        editButtons.put(edit_hours, "hours");
        editButtons.put(edit_descrip, "description");

        infoButtons = new HashMap<>();
        infoButtons.put(phone_prompt,phone);
        infoButtons.put(website_prompt, website);
        infoButtons.put(hours_prompt,hours);
        infoButtons.put(descrip_prompt,descrip);

        //Get View Model
        foodTruckViewModel =
                ViewModelProviders.of(this,new MyViewModelFactory(foodTruck,getActivity())).get(FoodTruckViewModel.class);

        setObservers();
        populateReviews();
        newReview();

        final FoodTruckController foodTruckController = new FoodTruckController(foodTruckViewModel,foodTruck);

        //set heart
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                foodTruckController.favorite(foodTruck.getId());
            }
        });

        //Set edit buttons
        for (final ImageView key: editButtons.keySet()) {
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editButtons.get(key).equals("hours")) {
                        showPopupHours(key,editButtons.get(key), foodTruckController);
                    } else
                        showPopup(key,editButtons.get(key), foodTruckController);
                    }
            });
        }

        //Set info buttons
        for (final TextView prompt: infoButtons.keySet()){
            infoButtons.get(prompt).setVisibility(View.GONE);
            prompt.setClickable(true);
            prompt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    if (infoButtons.get(prompt).getVisibility() == View.GONE) {
                        infoButtons.get(prompt).setVisibility(View.VISIBLE);
                    } else {
                        infoButtons.get(prompt).setVisibility(View.GONE);
                    }
                }
            });
        }
        return root;
    }


    private void showPopup (final ImageView field, final String jsonField, final FoodTruckController foodTruckController){
        final View popupView = getLayoutInflater().inflate(R.layout.popup_edit_field, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText popupInput1 = (EditText) popupView.findViewById(R.id.input1);
        final EditText popupInput2 = (EditText) popupView.findViewById(R.id.input2);
        final Button save = (Button)popupView.findViewById(R.id.save_info);
        final Button exit = (Button)popupView.findViewById(R.id.exit);

        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Popup window set up
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                int location[] = new int[2];
                view.getLocationOnScreen(location);


                //Styling Popup
                LinearLayout popupLayout = (LinearLayout) popupView.findViewById(R.id.popup_edit);
                TextView popupPrompt= (TextView) popupView.findViewById(R.id.edit_prompt);

                popupInput2.setVisibility(View.GONE);
                if (jsonField.equals("address")){
                    popupInput2.setVisibility(View.VISIBLE);
                }

                int munchGreen = ContextCompat.getColor(getContext(), R.color.munchGreenDark);
                String newField = jsonField;
                if (jsonField.equals("phoneNumber")){
                    newField = "phone number";
                }
                popupPrompt.setText("ENTER NEW " + newField.toUpperCase());
                popupPrompt.setTextColor(Color.WHITE);
                popupInput1.setTextColor(Color.WHITE);
                popupInput2.setTextColor(Color.WHITE);
                popupInput1.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                popupInput2.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                save.setTextColor(Color.WHITE);
                exit.setTextColor(Color.WHITE);
                save.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                exit.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                popupLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.munchGreenDark));
                popupWindow.setBackgroundDrawable(new ColorDrawable(munchGreen));
                popupWindow.setElevation(10);

                //Show popup
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        // Save and Exit buttons
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String input = popupInput1.getText().toString();
                if (popupInput2.getVisibility() == View.VISIBLE){
                    input+=popupInput2.getText();
                }
                foodTruckController.saveEdit(jsonField,input);
                popupWindow.dismiss();

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                popupWindow.dismiss();
            }
        });
    }

    private void showPopupHours (final View field, final String jsonField, final FoodTruckController foodTruckController){
        final View popupView = getLayoutInflater().inflate(R.layout.popup_edit_hours, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Spinner day = (Spinner)popupView.findViewById(R.id.day);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.days, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        day.setAdapter(adapter);
        final Button save = (Button)popupView.findViewById(R.id.save_info);
        final Button exit = (Button)popupView.findViewById(R.id.exit);

        final TimePicker startTime = (TimePicker) popupView.findViewById(R.id.timePicker1);
        startTime.setIs24HourView(false);
        final TimePicker endTime = (TimePicker) popupView.findViewById(R.id.timePicker2);
        endTime.setIs24HourView(false);
        final Switch closed = (Switch) popupView.findViewById(R.id.switch_open);
        final TextView startPrompt = (TextView) popupView.findViewById(R.id.start_prompt);
        final TextView stopPrompt = (TextView) popupView.findViewById(R.id.end_prompt);
        final TextView stringOpen = (TextView) popupView.findViewById(R.id.string_open);

        closed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startPrompt.setVisibility(View.VISIBLE);
                    startTime.setVisibility(View.VISIBLE);
                    endTime.setVisibility(View.VISIBLE);
                    stopPrompt.setVisibility(View.VISIBLE);
                    stringOpen.setText("OPEN");

                } else {
                    startPrompt.setVisibility(View.GONE);
                    startTime.setVisibility(View.GONE);
                    endTime.setVisibility(View.GONE);
                    stopPrompt.setVisibility(View.GONE);
                    stringOpen.setText("CLOSED");
                }
            }
        });

        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Popup window set up
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                int location[] = new int[2];
                view.getLocationOnScreen(location);

                TextView popupPrompt= (TextView) popupView.findViewById(R.id.edit_prompt);
                popupWindow.setElevation(10);

                //Show popup
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        // Save and Exit buttons
        save.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String dayOfWeek = day.getSelectedItem().toString();
                        foodTruckController.saveHours(dayOfWeek,closed.getShowText(),startTime,endTime);
                        popupWindow.dismiss();
                    }
                }
        );
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                popupWindow.dismiss();
            }
        });
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

        final Observer<String> distanceObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newDistance) {
                distance.setText(newDistance + " away");
            }
        };

        final Observer<Boolean> favoriteObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean newFavorite) {
                if (UserProfileFragment.currentUser.getLoggedIn()) {
                    heart.setVisibility(View.VISIBLE);
                    if (newFavorite) {
                        heart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fv_heart_filled));
                    } else {
                        heart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fv_heart));
                    }
                } else {
                    heart.setVisibility(View.GONE);
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

        final Observer<Boolean> editableObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean newEditable) {
                if (newEditable){
                    sw.setVisibility(View.VISIBLE);
                    for (ImageView button: editButtons.keySet()) {
                        button.setVisibility(View.VISIBLE);
                        button.setClickable(true);
                    }
                } else {
                    sw.setVisibility(View.GONE);
                    for (ImageView button: editButtons.keySet()) {
                        button.setVisibility(View.GONE);
                    }
                }
            }
        };

        foodTruckViewModel.getName().observe(this, nameObserver);
        foodTruckViewModel.getAddress().observe(this, addressObserver);
        foodTruckViewModel.getWebsite().observe(this, websiteObserver);
        foodTruckViewModel.getPhoneNumber().observe(this, phoneNumberObserver);
        foodTruckViewModel.getDescription().observe(this, descriptionObserver);
        foodTruckViewModel.getEditable().observe(this, editableObserver);
        foodTruckViewModel.getPhotos().observe(this, photosObserver);
        foodTruckViewModel.getRating().observe(this, ratingObserver);
        foodTruckViewModel.getStatus().observe(this, statusObserver);
        foodTruckViewModel.getFavorite().observe(this, favoriteObserver);
        foodTruckViewModel.getNumReviews().observe(this, numReviewsObserver);
        foodTruckViewModel.getDistance().observe(this, distanceObserver);
    }

    private void populateReviews(){
        ArrayList<String> listings = new ArrayList<String>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        allReviews.setLayoutManager(layoutManager);
        listings = foodTruck.getReviews();
        if (listings.size() != 0) {
            ArrayList<Review> reviewListings = new ArrayList<Review>();
            for (String s: listings){
                reviewListings.add(new Review(s));
            }
            ReviewListingAdapter mAdapter = new ReviewListingAdapter(reviewListings,getContext());
            allReviews.setAdapter(mAdapter);
        }
    }

    private void newReview(){
        final View popupView = getLayoutInflater().inflate(R.layout.popup_add_review, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final Button post = (Button)popupView.findViewById(R.id.post);
        final Button cancel = (Button)popupView.findViewById(R.id.cancel);
        final RatingBar rating = (RatingBar) popupView.findViewById(R.id.ratingbar_on_review);
        final EditText content = (EditText) popupView.findViewById(R.id.reviewContent);

        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Popup window set up
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                int location[] = new int[2];
                view.getLocationOnScreen(location);

                TextView popupPrompt= (TextView) popupView.findViewById(R.id.edit_prompt);
                popupWindow.setElevation(10);

                //Show popup
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        // Save and Exit buttons
        post.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String author = UserProfileFragment.currentUser.getId();
                        Review newReview = new Review(token, author, foodTruck.getId(),content.getText().toString(), rating.getRating());
                        popupWindow.dismiss();
                    }
                }
        );
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                popupWindow.dismiss();
            }
        });
    }
}