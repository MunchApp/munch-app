package com.example.munch.ui.foodTruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
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

public class createTruckActivity2 extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_truck_2);

        final String truck_name = getIntent().getStringExtra("name");
        final String truck_address = getIntent().getStringExtra("address");
        final String[] truck_photos = getIntent().getStringArrayExtra("photos");
        final EditText hoursSrtSun = findViewById(R.id.hoursSrtSun);
        final EditText hoursSrtMon = findViewById(R.id.hoursSrtMon);
        final EditText hoursSrtTue = findViewById(R.id.hoursSrtTue);
        final EditText hoursSrtWed = findViewById(R.id.hoursSrtWed);
        final EditText hoursSrtThu = findViewById(R.id.hoursSrtThu);
        final EditText hoursSrtFri = findViewById(R.id.hoursSrtFri);
        final EditText hoursSrtSat = findViewById(R.id.hoursSrtSat);
        final EditText hoursEndSun = findViewById(R.id.hoursEndSun);
        final EditText hoursEndMon = findViewById(R.id.hoursEndMon);
        final EditText hoursEndTue = findViewById(R.id.hoursEndTue);
        final EditText hoursEndWed = findViewById(R.id.hoursEndWed);
        final EditText hoursEndThu = findViewById(R.id.hoursEndThu);
        final EditText hoursEndFri = findViewById(R.id.hoursEndFri);
        final EditText hoursEndSat = findViewById(R.id.hoursEndSat);

        String[][] hours = new String[7][2];
        hours[0][0] = hoursSrtSun.getText().toString();
        hours[1][0] = hoursSrtMon.getText().toString();
        hours[2][0] = hoursSrtTue.getText().toString();
        hours[3][0] = hoursSrtWed.getText().toString();
        hours[4][0] = hoursSrtThu.getText().toString();
        hours[5][0] = hoursSrtFri.getText().toString();
        hours[6][0] = hoursSrtSat.getText().toString();
        hours[0][1] = hoursEndSun.getText().toString();
        hours[1][1] = hoursEndMon.getText().toString();
        hours[2][1] = hoursEndTue.getText().toString();
        hours[3][1] = hoursEndWed.getText().toString();
        hours[4][1] = hoursEndThu.getText().toString();
        hours[5][1] = hoursEndFri.getText().toString();
        hours[6][1] = hoursEndSat.getText().toString();



        if (truck_name != null && truck_address != null && hours !=null && truck_photos != null) {
            String token = UserProfileFragment.currentUser.getAccessToken();
            FoodTruck foodTruck = new FoodTruck(token,truck_name,truck_address,hours,truck_photos);
        }


    }
}
