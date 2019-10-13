package com.example.munch.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.lifecycle.ViewModelProviders;

import com.example.munch.MainActivity;
import com.example.munch.R;
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
        final EditText dobEditText = findViewById(R.id.date_of_birth);
        final EditText genderEditText = findViewById(R.id.gender);
        final EditText phoneNumberEditText = findViewById(R.id.phone_number);
        Button registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {


                        Intent toMainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(toMainIntent);

                        SharedPreferences sp = getSharedPreferences("key", 0);
                        SharedPreferences.Editor ipVals = sp.edit();
                        ipVals.putString("firstName", firstNameEditText.getText().toString());
                        ipVals.putString("lastName", lastNameEditText.getText().toString());
                        ipVals.putString("dob", dobEditText.getText().toString());
                        ipVals.putString("gender", genderEditText.getText().toString());
                        ipVals.putString("phoneNum", phoneNumberEditText.getText().toString());
                        ipVals.putString("email", user);
                        ipVals.commit();





                    }
                });

    }
}
