package com.example.munch.ui.foodTruck.createTruck;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.data.model.MunchUser;
import com.example.munch.ui.userProfile.UserProfileFragment;

import android.widget.TimePicker;

public class createTruckActivity2 extends AppCompatActivity {


    private TimePicker openTime;
    private TimePicker closeTime;
    private Switch openSwitch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_truck_2);
        final CreateTruckViewModel createTruckViewModel = new CreateTruckViewModel();


        String[] photos = {getIntent().getStringExtra("photo")};
        String tags = getIntent().getStringExtra("tags");
        createTruckViewModel.setName(getIntent().getStringExtra("name"));
        createTruckViewModel.setAddress(getIntent().getStringExtra("address"));
        createTruckViewModel.setTag(tags);
        createTruckViewModel.setPhotos(photos);

        final Context context = this;


        Button sunButton = (Button) findViewById(R.id.sun_prompt);
        Button monButton = (Button) findViewById(R.id.mon_prompt);
        Button tueButton = (Button) findViewById(R.id.tue_prompt);
        Button wedButton = (Button) findViewById(R.id.wed_prompt);
        Button thuButton = (Button) findViewById(R.id.thu_prompt);
        Button friButton = (Button) findViewById(R.id.fri_prompt);
        Button satButton = (Button) findViewById(R.id.sat_prompt);

        ImageView pointer = (ImageView) findViewById(R.id.pointer);

        //LinearLayout numPad = (LinearLayout) findViewById(R.id.num_pad);

        //numPad.setVisibility(View.GONE);

        setButtonDays(createTruckViewModel, sunButton, pointer, 0, "Sunday", 0);
        setButtonDays(createTruckViewModel, monButton, pointer, 130, "Monday", 1);
        setButtonDays(createTruckViewModel, tueButton, pointer, 260, "Tuesday", 2);
        setButtonDays(createTruckViewModel, wedButton, pointer, 390, "Wednesday", 3);
        setButtonDays(createTruckViewModel, thuButton, pointer, 520, "Thursday", 4);
        setButtonDays(createTruckViewModel, friButton, pointer, 650, "Friday", 5);
        setButtonDays(createTruckViewModel, satButton, pointer, 780, "Saturday", 6);

        openSwitch = (Switch) findViewById(R.id.switch_open);
        final TextView openText = (TextView) findViewById(R.id.switch_text);
        final LinearLayout timeDiv = (LinearLayout) findViewById(R.id.time_div);
        openTime = (TimePicker) findViewById((R.id.timePicker1));
        closeTime = (TimePicker) findViewById((R.id.timePicker2));

        final Button create = (Button) findViewById(R.id.create_truck);

        openTime.setHour(12);
        openTime.setMinute(0);
        closeTime.setHour(12);
        closeTime.setMinute(0);

        openSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openText.setText("OPEN");
                    timeDiv.setVisibility(View.VISIBLE);
                } else {
                    openText.setText("CLOSED");
                    timeDiv.setVisibility(View.INVISIBLE);
                }
            }
        });
        create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {


                        String[][] finalHours = new String[7][2];
                        int c = 0;
                        for (Integer[] s: createTruckViewModel.getHours()){
                            finalHours[c][0] = parseTime(s[0], s[1]);
                            finalHours[c][1] = parseTime(s[2], s[3]);
                            c++;
                        }
                        String token = MunchUser.getInstance().getAccessToken();
                        String ownerId = MunchUser.getInstance().getId();

                        FoodTruck foodTruck = new FoodTruck(token, createTruckViewModel.getName(), createTruckViewModel.getAddress(), finalHours, ownerId, createTruckViewModel.getTag(), createTruckViewModel.getLocation(context));
                       //todo only add if response is 200
                        MunchUser.getInstance().addTruck(foodTruck.getId());
                        Intent toMainIntent = new Intent(createTruckActivity2.this, MainActivity.class);
                        startActivity(toMainIntent);
                    }
                });

    }

    private void setButtonDays(final CreateTruckViewModel createTruckViewModel, Button button, final ImageView pointer, final float pos, final String day, final int intDay) {

        button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        int currentDay = createTruckViewModel.getIntDay().getValue();
                        if(openSwitch.isChecked()) {
                            createTruckViewModel.setHours(currentDay, openTime.getHour(), openTime.getMinute(), closeTime.getHour(), closeTime.getMinute());
                        } else {
                            createTruckViewModel.setHours(currentDay, 99,99,99,99);
                        }
                        TextView dayPrompt = findViewById(R.id.day_prompt);
                        dayPrompt.setText(day);
                        ObjectAnimator animation = ObjectAnimator.ofFloat(pointer, "translationX", pos);
                        animation.setDuration(500);
                        animation.start();

                        Integer[] time = createTruckViewModel.getHours()[intDay];
                        if (time[0] <= 24) {
                            openSwitch.setChecked(true);
                            openTime.setHour(time[0]);
                            openTime.setMinute(time[1]);
                            closeTime.setHour(time[2]);
                            closeTime.setMinute(time[3]);
                        } else {
                            openSwitch.setChecked(false);
                        }

                        createTruckViewModel.setIntDay(intDay);
                    }
                });
    }

    private String parseTime(int hour, int min) {
        String returnedTime = "";
        if (hour < 10){
            returnedTime += "0";
        }
        returnedTime+= hour + ":";
        if (min < 10){
            returnedTime += "0";
        }
        returnedTime+= min;
        return  returnedTime;
    }

}



