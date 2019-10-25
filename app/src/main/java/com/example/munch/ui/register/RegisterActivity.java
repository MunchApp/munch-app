package com.example.munch.ui.register;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.data.model.LoggedInUser;


public class RegisterActivity extends AppCompatActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final String user = getIntent().getStringExtra(USERNAME);
        final String pass = getIntent().getStringExtra(PASSWORD);
        final EditText firstNameEditText = findViewById(R.id.first_name);
        final EditText lastNameEditText = findViewById(R.id.last_name);
        Button registerButton = findViewById(R.id.register);
        final Spinner month = (Spinner) findViewById(R.id.month);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        month.setAdapter(adapter);


        final EditText fName = findViewById(R.id.first_name);
        final EditText lName = findViewById(R.id.last_name);
        final EditText day = findViewById(R.id.day);
        final EditText year = findViewById(R.id.year);


        day.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    day.setHint("");
                }
            }
        });
        year.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    year.setHint("");
                }
            }
        });

        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {







                       /* SharedPreferences sp = getSharedPreferences("key", 0);
                        SharedPreferences.Editor ipVals = sp.edit();
                        ipVals.putString("firstName", firstNameEditText.getText().toString());
                        ipVals.putString("lastName", lastNameEditText.getText().toString());
                        ipVals.putString("email", user);
                        ipVals.commit();*/



                        // Input
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Integer.valueOf(year.getText().toString()), getMonth(month.getSelectedItem().toString()), Integer.valueOf(day.getText().toString()), 00, 00, 00);
                        calendar.set(Calendar.MILLISECOND, 0);
                        Date date = calendar.getTime();
                        SimpleDateFormat sdf;
                        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        String isoDOB = sdf.format(date);
                        System.out.println(isoDOB);

                        Intent toMainIntent = new Intent(RegisterActivity.this, MainActivity.class);

                        toMainIntent.putExtra(USERNAME,user);
                        toMainIntent.putExtra(PASSWORD,pass);
                        toMainIntent.putExtra("firstname", fName.getText().toString());
                        toMainIntent.putExtra("lastname", lName.getText().toString());
                        toMainIntent.putExtra("dateofbirth",isoDOB);

                        startActivity(toMainIntent);



                    }
                });

    }

    private static int getMonth (String month) {
        int monthNum = -1;
        switch (month) {
            case "January": monthNum = 0;
                break;
            case "February": monthNum = 1;
                break;
            case "March": monthNum = 2;
                break;
            case "April": monthNum = 3;
                break;
            case "May": monthNum = 4;
                break;
            case "June": monthNum = 5;
                break;
            case "July": monthNum = 6;
                break;
            case "August": monthNum = 7;
                break;
            case "September": monthNum = 8;
                break;
            case "October": monthNum = 9;
                break;
            case "November": monthNum = 10;
                break;
            case "December": monthNum = 11;
                break;
            default: monthNum = -1;
                break;
        }
        return monthNum;
    }
}
