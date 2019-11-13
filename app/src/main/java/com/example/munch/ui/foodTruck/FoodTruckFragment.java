package com.example.munch.ui.foodTruck;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.LocationCalculator;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.foodTruck.reviews.ReviewsPageActivity;

import com.example.munch.ui.userProfile.UserProfileFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

public class FoodTruckFragment extends Fragment{

    private FoodTruck foodTruck;
    private Boolean owner;
    private FoodTruckViewModel foodTruckViewModel;
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


    public FoodTruckFragment(FoodTruck foodTruck, Boolean owner) {
        this.foodTruck = foodTruck;
        this.owner = owner;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodTruckViewModel =
                ViewModelProviders.of(this).get(FoodTruckViewModel.class);
        final View root = inflater.inflate(R.layout.food_truck_activity, container, false);
        token = UserProfileFragment.currentUser.getAccessToken();
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
        phone_prompt = (TextView)root.findViewById(R.id.phone_prompt);
        website_prompt = (TextView)root.findViewById(R.id.website_prompt);
        hours_prompt = (TextView)root.findViewById(R.id.hours_prompt);
        descrip_prompt = (TextView)root.findViewById(R.id.descrip_prompt);
        num_review = (TextView)root.findViewById(R.id.num_reviews);
        edit_name = (ImageView)root.findViewById(R.id.edit_name);
        edit_address = (ImageView)root.findViewById(R.id.edit_address);
        edit_website = (ImageView)root.findViewById(R.id.edit_website);
        edit_phone = (ImageView)root.findViewById(R.id.edit_phone);
        edit_hours = (ImageView)root.findViewById(R.id.edit_hours);
        edit_descrip = (ImageView)root.findViewById(R.id.edit_descrip);
        sw = (Switch) root.findViewById(R.id.switch_status);
        distance = (TextView) root.findViewById(R.id.truck_distance);

        fillTruckFragment(foodTruck);
        setInfoButtons(phone_prompt,phone);
        setInfoButtons(website_prompt,website);
        setInfoButtons(hours_prompt,hours);
        setInfoButtons(descrip_prompt,descrip);

        //todo edit hours
        setSimpleEditButtons(edit_name,name,"NAME", "name");
        setSimpleEditButtons(edit_address,address,"ADDRESS", "address");
        setSimpleEditButtons(edit_phone,phone,"PHONE NUMBER","phoneNumber");
        setSimpleEditButtons(edit_website,website,"WEBSITE URL","website");
        setSimpleEditButtons(edit_descrip,descrip, "DESCRIPTION","description");

        Boolean owns = UserProfileFragment.currentUser.getFoodTrucks().contains(foodTruck.getId());
        if (owns) {
            sw.setVisibility(View.VISIBLE);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        foodTruck.updateTruck(token, null, true);
                        status.setText("ONLINE");
                        ImageViewCompat.setImageTintList(statusIcon, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.onlineGreen)));
                    } else {
                        status.setText("OFFLINE");
                        foodTruck.updateTruck(token, null, false);
                        ImageViewCompat.setImageTintList(statusIcon, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.offlineRed)));
                    }
                }
            });
        } else {
            sw.setVisibility(View.GONE);
        }

        return root;
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
                if (value.getVisibility() == View.GONE){
                    value.setVisibility(View.VISIBLE);
                } else {
                    value.setVisibility(View.GONE);
                }
            }
        });
    }

    private void fillTruckFragment(FoodTruck truck){
        //setValues
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
            status.setText("ONLINE");
            sw.setChecked(true);
            ImageViewCompat.setImageTintList(statusIcon, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.onlineGreen)));
        } else {
            status.setText("OFFLINE");
            sw.setChecked(false);
            ImageViewCompat.setImageTintList(statusIcon, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.offlineRed)));
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
    }

    private void showPopup (final View field, final TextView saveEdit, final String prompt, final String jsonField){
        field.setClickable(true);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Popup window set up
                final View popupView = getLayoutInflater().inflate(R.layout.popup_edit_field, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                int location[] = new int[2];
                view.getLocationOnScreen(location);


                //Styling Popup
                Button save = (Button)popupView.findViewById(R.id.save_info);
                Button exit = (Button)popupView.findViewById(R.id.exit);

                LinearLayout popupLayout = (LinearLayout) popupView.findViewById(R.id.popup_edit);
                TextView popupPrompt= (TextView) popupView.findViewById(R.id.edit_prompt);
                final EditText popupInput1 = (EditText) popupView.findViewById(R.id.input1);
                final EditText popupInput2 = (EditText) popupView.findViewById(R.id.input2);
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
                        int responseCode = foodTruck.updateTruck(token,newVals,null);
                        if (responseCode == 200){
                            saveEdit.setText(val);
                        }

                    }
                });
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        popupWindow.dismiss();
                    }
                });

                //Show popup
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }

}