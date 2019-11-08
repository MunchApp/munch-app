package com.example.munch.ui.foodTruck;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.Reviewspageactivity;
import com.example.munch.ui.userProfile.UserProfileFragment;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class FoodTruckFragment extends Fragment {

    private FoodTruck foodTruck;
    private Boolean owner;
    private FoodTruckViewModel foodTruckViewModel;
    public  FoodTruckFragment (FoodTruck foodTruck, Boolean owner){
        this.foodTruck = foodTruck;
        this.owner = owner;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodTruckViewModel =
                ViewModelProviders.of(this).get(FoodTruckViewModel.class);
        final View root = inflater.inflate(R.layout.food_truck_activity, container, false);
        fillListing(root);
        ImageView edit = (ImageView)root.findViewById(R.id.edit);
        final EditText name = (EditText)root.findViewById(R.id.truck_name);
        final EditText address = (EditText)root.findViewById(R.id.truck_address);
        final EditText phone_number = (EditText)root.findViewById(R.id.phone_number);
        final EditText description = (EditText)root.findViewById(R.id.descrip_body);
        final Button save = (Button) root.findViewById(R.id.save_info);
        final Button exit = (Button) root.findViewById(R.id.exit);

        name.setEnabled(false);
        address.setEnabled(false);
        phone_number.setEnabled(false);
        description.setEnabled(false);
        edit.setClickable(true);
        save.setVisibility(View.INVISIBLE);
        exit.setVisibility(View.INVISIBLE);
        if (!owner){
            edit.setVisibility(View.INVISIBLE);
        } else {
            edit.setVisibility(View.VISIBLE);
        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setEnabled(true);
                address.setEnabled(true);
                phone_number.setEnabled(true);
                description.setEnabled(true);
                save.setVisibility(View.VISIBLE);
                exit.setVisibility(View.VISIBLE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> vals = new HashMap<String, String>();
                vals.put("name",name.getText().toString());
                vals.put("address",address.getText().toString());
                vals.put("phoneNumber",phone_number.getText().toString());
                vals.put("description",description.getText().toString());
                foodTruck.updateTruck(UserProfileFragment.currentUser.getAccessToken(),vals);
                name.setEnabled(false);
                address.setEnabled(false);
                phone_number.setEnabled(false);
                description.setEnabled(false);
                save.setVisibility(View.INVISIBLE);
                exit.setVisibility(View.INVISIBLE);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillListing(root);
                name.setEnabled(false);
                address.setEnabled(false);
                phone_number.setEnabled(false);
                description.setEnabled(false);
                save.setVisibility(View.INVISIBLE);
                exit.setVisibility(View.INVISIBLE);
            }
        });
        return root;
    }

    private void fillListing(View root){
        final EditText name = (EditText)root.findViewById(R.id.truck_name);
        final EditText address = (EditText)root.findViewById(R.id.truck_address);
        final EditText phone_number = (EditText)root.findViewById(R.id.phone_number);
        final EditText description = (EditText)root.findViewById(R.id.descrip_body);
        name.setText(foodTruck.getName());
        address.setText(foodTruck.getAddress());
        phone_number.setText(foodTruck.getPhoneNumber());
        description.setText(foodTruck.getDescription());

        final ImageView main_img = (ImageView)root.findViewById(R.id.main_image);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = 700;
        Picasso.with(getActivity()).load(foodTruck.getPhotos().get(0))
                .resize(width, height)
                .centerCrop()
                .into(main_img);

        final TextView sun = (TextView) root.findViewById(R.id.sun_hours);
        final TextView mon = (TextView) root.findViewById(R.id.mon_hours);
        final TextView tue = (TextView) root.findViewById(R.id.tue_hours);
        final TextView wed = (TextView) root.findViewById(R.id.wed_hours);
        final TextView thu = (TextView) root.findViewById(R.id.thu_hours);
        final TextView fri = (TextView) root.findViewById(R.id.fri_hours);
        final TextView sat = (TextView) root.findViewById(R.id.sat_hours);
        sun.setText(militaryToReg(foodTruck.getHours()[0][0]) + " - " + militaryToReg(foodTruck.getHours()[0][1]));
        mon.setText(militaryToReg(foodTruck.getHours()[1][0]) + " - " + militaryToReg(foodTruck.getHours()[1][1]));
        tue.setText(militaryToReg(foodTruck.getHours()[2][0]) + " - " + militaryToReg(foodTruck.getHours()[2][1]));
        wed.setText(militaryToReg(foodTruck.getHours()[3][0]) + " - " + militaryToReg(foodTruck.getHours()[3][1]));
        thu.setText(militaryToReg(foodTruck.getHours()[4][0]) + " - " + militaryToReg(foodTruck.getHours()[4][1]));
        fri.setText(militaryToReg(foodTruck.getHours()[5][0]) + " - " + militaryToReg(foodTruck.getHours()[5][1]));
        sat.setText(militaryToReg(foodTruck.getHours()[6][0]) + " - " + militaryToReg(foodTruck.getHours()[6][1]));
    }

    private String militaryToReg (String time) {
        int hour = Integer.valueOf(time.split(":")[0]);
        int min = Integer.valueOf(time.split(":")[1]);
        String ampm = "";
        if (hour < 12){
            ampm = "AM";
        } else {
            ampm = "PM";
            hour -=12;
        }
        if (hour == 0){
            hour = 12;
        }
        return hour+":"+min+ " "+ampm;
    }
    /*private void makeReviewClickable(View root) {
        TextView reviews = (TextView) root.findViewById(R.id.review_text);
        reviews.setClickable(true);
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Reviewspageactivity.class);
                startActivity(intent);
            }
        });
    }*/
     /* final View popupView =  inflater.inflate(R.layout.popup_edit_field, container, false);
        final PopupWindow pw = new PopupWindow(popupView, popupView.getWidth(),popupView.getHeight(), true);
        pw.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        pw.update();


        final HashMap<String,ImageView> editable = new HashMap<String,ImageView>();
        editable.put("name", (ImageView)root.findViewById(R.id.edit_name));
        editable.put("address", (ImageView)root.findViewById(R.id.edit_address));
        editable.put("sun_hours", (ImageView)root.findViewById(R.id.edit_sun));
        editable.put("mon_hours", (ImageView)root.findViewById(R.id.edit_mon));
        editable.put("tue_hours", (ImageView)root.findViewById(R.id.edit_tue));
        editable.put("wed_hours", (ImageView)root.findViewById(R.id.edit_wed));
        editable.put("thu_hours", (ImageView)root.findViewById(R.id.edit_thu));
        editable.put("fri_hours", (ImageView)root.findViewById(R.id.edit_fri));
        editable.put("sat_hours", (ImageView)root.findViewById(R.id.edit_sat));
        editable.put("phone_num", (ImageView)root.findViewById(R.id.edit_phone));
        editable.put("description", (ImageView)root.findViewById(R.id.edit_descrip));

        for (String i: editable.keySet()){
            if (owner) {
                final String index = i;
                editable.get(i).setClickable(true);
                editable.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pw.showAtLocation(editable.get(index), Gravity.CENTER, 0, 0);
                        Button save = (Button)popupView.findViewById(R.id.save_info);
                        Button close = (Button) popupView.findViewById(R.id.exit);
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pw.dismiss();
                            }
                        });
                    }
                });

            } else {
                editable.get(i).setVisibility(View.INVISIBLE);
            }
        }*/




    // makeReviewClickable(root);
}


