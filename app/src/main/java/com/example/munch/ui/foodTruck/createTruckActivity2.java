package com.example.munch.ui.foodTruck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSet;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.login.LoginActivity;
import com.example.munch.ui.map.MapViewModel;
import com.example.munch.ui.register.RegisterActivity;
import com.example.munch.ui.userProfile.UserProfileFragment;

import android.widget.TimePicker;

import java.sql.Time;

public class createTruckActivity2 extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_hours);
        final CreateTruckViewModel newTruck = new CreateTruckViewModel();

        newTruck.setName(getIntent().getStringExtra("name"));
        newTruck.setAddress(getIntent().getStringExtra("address"));
        String[] photos = {getIntent().getStringExtra("photo")};
        String tags = getIntent().getStringExtra("tags");
        Bitmap bmp;
        //final String image = getIntent().getStringExtra("bitmap");

        newTruck.setPhotos(photos);

        final TimePicker startTime = (TimePicker) findViewById(R.id.timePicker1);
        startTime.setIs24HourView(false);
        final TimePicker endTime = (TimePicker) findViewById(R.id.timePicker2);
        endTime.setIs24HourView(false);
        final TextView prompt = (TextView) findViewById(R.id.day_prompt);
        final Button next = findViewById(R.id.submit_time);
        final String[] days = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
        final Switch closed = (Switch) findViewById(R.id.switch_open);
        final TextView startPrompt = (TextView) findViewById(R.id.start_prompt);
        final TextView stopPrompt = (TextView) findViewById(R.id.end_prompt);
        final TextView stringOpen = (TextView) findViewById(R.id.string_open);
        newTruck.setTag(tags);

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
        int day = 0;
        prompt.setText(days[0] + " HOURS");
        String[][] hours = new String[7][2];
        final Context context = this;
        next.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String day = prompt.getText().toString().split(" ")[0];
                        switch(day)
                        {
                            case "SUNDAY":
                                prompt.setText(days[1]+ " HOURS");
                                if (!closed.isChecked()){
                                    newTruck.setHours(0,"99:99","99:99");
                                } else
                                newTruck.setHours(0,parseTime(startTime),parseTime(endTime));
                                break;
                            case "MONDAY":
                                prompt.setText(days[2]+ " HOURS");
                                if (!closed.isChecked()){
                                    newTruck.setHours(0,"99:99","99:99");
                                } else
                                newTruck.setHours(1,parseTime(startTime),parseTime(endTime));
                                break;
                            case "TUESDAY":
                                prompt.setText(days[3]+ " HOURS");
                                if (!closed.isChecked()){
                                    newTruck.setHours(0,"99:99","99:99");
                                } else
                                newTruck.setHours(2,parseTime(startTime),parseTime(endTime));
                                break;
                            case "WEDNESDAY":
                                prompt.setText(days[4]+ " HOURS");
                                if (!closed.isChecked()){
                                    newTruck.setHours(0,"99:99","99:99");
                                } else
                                newTruck.setHours(3,parseTime(startTime),parseTime(endTime));
                                break;
                            case "THURSDAY":
                                prompt.setText(days[5]+ " HOURS");
                                if (!closed.isChecked()){
                                    newTruck.setHours(0,"99:99","99:99");
                                } else
                                newTruck.setHours(4,parseTime(startTime),parseTime(endTime));
                                break;
                            case "FRIDAY":
                                prompt.setText(days[6]+ " HOURS");
                                next.setText("CREATE TRUCK");
                                if (!closed.isChecked()){
                                    newTruck.setHours(0,"99:99","99:99");
                                } else
                                newTruck.setHours(5,parseTime(startTime),parseTime(endTime));
                                break;
                            case "SATURDAY":
                                if (!closed.isChecked()){
                                    newTruck.setHours(0,"99:99","99:99");
                                } else
                                newTruck.setHours(6,parseTime(startTime),parseTime(endTime));
                                String token = UserProfileFragment.currentUser.getAccessToken();
                                String ownerId = UserProfileFragment.currentUser.getId();

                                FoodTruck foodTruck = new FoodTruck(token, newTruck.getName(), newTruck.getAddress(), newTruck.getHours(),ownerId, newTruck.getTag(), newTruck.getLocation(context));
                                UserProfileFragment.currentUser.addTruck(foodTruck.getId());
                                Intent toMainIntent = new Intent(createTruckActivity2.this, MainActivity.class);
                                startActivity(toMainIntent);
                                break;
                            default:
                                System.out.println("error");
                        }
                    }
                }
        );
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
}