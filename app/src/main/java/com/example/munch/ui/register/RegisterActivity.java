package com.example.munch.ui.register;

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
        final TextView pifirstAndLast = (TextView)findViewById(R.id.pi_first_and_last_name);
        final TextView firstAndLast = (TextView)findViewById(R.id.first_and_last_name);
        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {


                        Intent toMainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(toMainIntent);
                        /* String first = firstNameEditText.getText().toString();
                        String last = lastNameEditText.getText().toString();
                        String full = first + " " + last;
                        System.out.println("HELLO" + full);
                        pifirstAndLast.setText(full);
                        firstAndLast.setText(full);*/
                        /*TextView dob = findViewById(R.id.pi_dob);
                        dob.setText(dobEditText.getText()toString());
                        TextView gender = findViewById(R.id.pi_gender);
                        gender.setText(genderEditText.getText()toString());
                        TextView phoneNum = findViewById(R.id.pi_phone_num);
                        phoneNum.setText(phoneNumberEditText.getText()toString());
                        TextView email = findViewById(R.id.pi_email);
                        email.setText(user);*/
                    }
                });

    }
}
