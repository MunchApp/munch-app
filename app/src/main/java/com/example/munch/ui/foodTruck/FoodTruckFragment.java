package com.example.munch.ui.foodTruck;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.munch.LocationCalculator;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.data.model.Review;
import com.example.munch.ui.foodTruck.reviews.ReviewListingAdapter;
import com.example.munch.ui.userProfile.UserProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

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

    public FoodTruckFragment(FoodTruck foodTruck, Boolean owner) {
        this.foodTruck = foodTruck;
        this.owner = owner;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Get root view
        final View root = inflater.inflate(R.layout.food_truck_activity, container, false);

        //Initialize views that may change values
        image = (ImageView)root.findViewById(R.id.truck_image);
        name = (TextView)root.findViewById(R.id.truck_name);
        statusIcon = (ImageView)root.findViewById(R.id.status);
        status = (TextView)root.findViewById(R.id.status_string);
        address = (TextView)root.findViewById(R.id.truck_address);
        rating = (RatingBar) root.findViewById(R.id.truck_rating_bar);
        website = (TextView)root.findViewById(R.id.truck_website);
        phone = (TextView)root.findViewById(R.id.truck_phone);
        sun = (TextView)root.findViewById(R.id.sun_hours);
        mon = (TextView)root.findViewById(R.id.mon_hours);
        tue = (TextView)root.findViewById(R.id.tue_hours);
        wed = (TextView)root.findViewById(R.id.wed_hours);
        thu = (TextView)root.findViewById(R.id.thu_hours);
        fri = (TextView)root.findViewById(R.id.fri_hours);
        sat = (TextView)root.findViewById(R.id.sat_hours);
        descrip = (TextView)root.findViewById(R.id.truck_descrip);
        hours = (ConstraintLayout)root.findViewById(R.id.hours);
        num_review = (TextView)root.findViewById(R.id.num_reviews);
        distance = (TextView) root.findViewById(R.id.truck_distance);
        allReviews = (RecyclerView) root.findViewById(R.id.truck_reviews);
        gap = root.findViewById(R.id.gap);

        //Initialize views users interact with
        phone_prompt = (TextView)root.findViewById(R.id.phone_prompt);
        website_prompt = (TextView)root.findViewById(R.id.website_prompt);
        hours_prompt = (TextView)root.findViewById(R.id.hours_prompt);
        descrip_prompt = (TextView)root.findViewById(R.id.descrip_prompt);
        edit_name = (ImageView)root.findViewById(R.id.edit_name);
        edit_address = (ImageView)root.findViewById(R.id.edit_address);
        edit_website = (ImageView)root.findViewById(R.id.edit_website);
        edit_phone = (ImageView)root.findViewById(R.id.edit_phone);
        edit_hours = (ImageView)root.findViewById(R.id.edit_hours);
        edit_descrip = (ImageView)root.findViewById(R.id.edit_descrip);
        sw = (Switch) root.findViewById(R.id.switch_status);
        heart = (ImageView) root.findViewById(R.id.favorite_heart);
        postReview = root.findViewById(R.id.add_review);

        //Get View Model
        foodTruckViewModel =
                ViewModelProviders.of(this).get(FoodTruckViewModel.class);


        token = UserProfileFragment.currentUser.getAccessToken();


        fillTruckFragment(foodTruck);
        enableFavorite();
        setInfoButtons(phone_prompt,phone);
        setInfoButtons(website_prompt,website);
        setInfoButtons(hours_prompt,hours);
        setInfoButtons(descrip_prompt,descrip);

        //todo edit hours
        setSimpleEditButtons(edit_hours,null,"HOURS","hours");
        setSimpleEditButtons(edit_name,name,"NAME", "name");
        setSimpleEditButtons(edit_address,address,"ADDRESS", "address");
        setSimpleEditButtons(edit_phone,phone,"PHONE NUMBER","phoneNumber");
        setSimpleEditButtons(edit_website,website,"WEBSITE URL","website");
        setSimpleEditButtons(edit_descrip,descrip, "DESCRIPTION","description");

        Boolean owns = UserProfileFragment.currentUser.getFoodTrucks().contains(foodTruck.getId());

        num_review.setClickable(true);
        num_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                ScrollView scrollView = (ScrollView)root.findViewById(R.id.scrollView);
                TextView review_prompt= (TextView) root.findViewById(R.id.review_prompt);
                scrollView.scrollTo(0, review_prompt.getTop());
            }
        });
        if (owns) {
            sw.setVisibility(View.VISIBLE);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    View gap = root.findViewById(R.id.gap);
                    if (isChecked) {
                        foodTruck.updateTruck(token, null, true, null);
                        statusIcon.setVisibility(View.VISIBLE);
                        gap.setVisibility(View.VISIBLE);
                        status.setText("ONLINE");
                        ImageViewCompat.setImageTintList(statusIcon, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.onlineGreen)));
                    } else {
                        status.setText("OFFLINE");
                        statusIcon.setVisibility(View.GONE);
                        gap.setVisibility(View.GONE);
                        foodTruck.updateTruck(token, null, false, null);
                        ImageViewCompat.setImageTintList(statusIcon, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.offlineRed)));
                    }
                }
            });
        } else {
            sw.setVisibility(View.GONE);
        }
        newReview();
        return root;
    }

    private void enableFavorite () {
        if (UserProfileFragment.currentUser.getLoggedIn()){
            heart.setClickable(true);
            heart.setVisibility(View.VISIBLE);
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    if (UserProfileFragment.currentUser.getFavorites().contains(foodTruck.getId())){
                        //todo make call to delete favorite
                        UserProfileFragment.currentUser.deleteFavorite(foodTruck.getId());

                    } else {
                        //todo make call to add favorite
                        UserProfileFragment.currentUser.addFavorite(foodTruck.getId());
                    }

                    //todo uncomment when call is complete
                    fillHeart(UserProfileFragment.currentUser.getFavorites().contains(foodTruck.getId()));
                }
            });
        } else {
            heart.setVisibility(View.GONE);
        }

    }
    private void setSimpleEditButtons (final View button, final TextView editField, final String prompt, final String jsonField){
        String userId = UserProfileFragment.currentUser.getId();
        String ownerId = foodTruck.getOwner();
        Boolean owns = UserProfileFragment.currentUser.getFoodTrucks().contains(foodTruck.getId());
        if (owns){
           button.setVisibility(View.VISIBLE);
           button.setClickable(true);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    if (prompt.equals("HOURS")){
                        showPopupHours(button, editField,prompt,jsonField);
                    } else
                    showPopup(button, editField, prompt,jsonField);
                }
            });
        } else {
            button.setVisibility(View.GONE);
        }
    }



    private void setInfoButtons (View prompt,final View value){
        value.setVisibility(View.GONE);
        prompt.setClickable(true);
        prompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (value.getVisibility() == View.GONE) {
                    value.setVisibility(View.VISIBLE);
                } else {
                    value.setVisibility(View.GONE);
                }
            }
        });
    }
    private void fillHeart(boolean faved){
        if (faved){
            heart.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.fv_heart_filled));
        } else {
            heart.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.fv_heart));
        }
    }
    private void fillTruckFragment(FoodTruck truck){
        //setValues
        fillHeart(UserProfileFragment.currentUser.getFavorites().contains(foodTruck.getId()));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Picasso.with(getActivity()).load(truck.getPhotos().get(0))
                .resize(width, 650)
                .centerCrop()
                .into(image);
        name.setText(truck.getName());
        if (truck.getStatus()){
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
            //ImageViewCompat.setImageTintList(statusIcon, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.offlineRed)));
        }
        //todo get distance
        LocationCalculator location = new LocationCalculator(getActivity());
        String dist = location.getDistance(truck.getAddress(),"Current Location");
        distance.setText( dist + " away");

        //todo calculate open
        address.setText(truck.getAddress());
        rating.setRating(truck.getAvgRating());
        website.setText(truck.getWebsite());
        phone.setText(truck.getPhoneNumber());
        sun.setText(truck.getRegHours()[0]);
        mon.setText(truck.getRegHours()[1]);
        tue.setText(truck.getRegHours()[2]);
        wed.setText(truck.getRegHours()[3]);
        thu.setText(truck.getRegHours()[4]);
        fri.setText(truck.getRegHours()[5]);
        sat.setText(truck.getRegHours()[6]);
        descrip.setText(truck.getDescription());
        num_review.setText(truck.getReviews().size() + " reviews");
        if (truck.getReviews().size() == 0) {
            num_review.setText("no reviews");
        }
        populateReviews();
    }

    private void showPopup (final View field, final TextView saveEdit, final String prompt, final String jsonField){
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
                if (prompt.equals("ADDRESS")){
                    popupInput2.setVisibility(View.VISIBLE);
                }

                int munchGreen = ContextCompat.getColor(getContext(), R.color.munchGreenDark);
                popupPrompt.setText("ENTER NEW " + prompt);
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
                HashMap<String, String> newVals = new HashMap<String, String>();
                String val;
                if (prompt.equals("ADDRESS")){
                    val = popupInput1.getText().toString() + "\n" +popupInput2.getText().toString();
                } else {
                    val = popupInput1.getText().toString();
                }
                newVals.put(jsonField,val);
                int responseCode = foodTruck.updateTruck(token,newVals,null,null);
                if (responseCode == 200){
                    saveEdit.setText(val);
                }
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
    private void showPopupHours (final View field, final TextView saveEdit, final String prompt, final String jsonField){
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
                        String[][] hours = foodTruck.getHours();
                        int dayInt = 0;
                        switch(dayOfWeek )
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
                        if (!closed.isChecked()){
                            hours[dayInt][0] = "99:99";
                            hours[dayInt][1] = "99:99";
                        } else {
                            hours[dayInt][0] = parseTime(startTime);
                            hours[dayInt][1] = parseTime(endTime);
                        }
                        int responseCode = foodTruck.updateTruck(token,null,null, hours);
                        if (responseCode == 200){
                            fillTruckFragment(foodTruck);
                        }
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
                        fillTruckFragment(foodTruck);
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