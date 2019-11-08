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

public class createTruckActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_truck);


        final EditText name = findViewById(R.id.truck_name);
        final EditText address = findViewById(R.id.address);
        final EditText photo = findViewById(R.id.photo_url);
        final Button next = findViewById(R.id.next_create);

        final String[] truck_photos =  {photo.getText().toString()};

        next.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        Intent toMainIntent = new Intent(createTruckActivity.this, createTruckActivity2.class);
                        toMainIntent.putExtra("name", name.getText().toString());
                        toMainIntent.putExtra("address", address.getText().toString());
                        toMainIntent.putExtra("photo", photo.getText().toString());
                        startActivity(toMainIntent);

                    }
                });
    }
}
