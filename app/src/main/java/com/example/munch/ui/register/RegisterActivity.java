package com.example.munch.ui.register;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.ui.explore.ExploreFragment;
import com.example.munch.ui.login.LoginActivity;
import com.example.munch.ui.login.LoginViewModel;

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


                        Intent toMainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(toMainIntent);




                        SharedPreferences sp = getSharedPreferences("key", 0);
                        SharedPreferences.Editor ipVals = sp.edit();
                        ipVals.putString("firstName", firstNameEditText.getText().toString());
                        ipVals.putString("lastName", lastNameEditText.getText().toString());
                        ipVals.putString("email", user);
                        ipVals.putString("month",month.getSelectedItem().toString());
                        ipVals.putString("day",day.getText().toString());
                        ipVals.putString("year",year.getText().toString());
                        ipVals.commit();





                    }
                });

    }
}
