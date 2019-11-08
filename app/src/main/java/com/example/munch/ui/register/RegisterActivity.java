package com.example.munch.ui.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.ui.userProfile.UserProfileFragment;


public class RegisterActivity extends AppCompatActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final String user = getIntent().getStringExtra(USERNAME);
        final String pass = getIntent().getStringExtra(PASSWORD);
        final EditText emailEditText = findViewById(R.id.email_name);
        final EditText passEditText = findViewById(R.id.pass_name);
        emailEditText.setText(user);
        passEditText.setText(pass);
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

                        String firstname = fName.getText().toString();
                        String lastname = lName.getText().toString();
                        String dateofbirth_day = day.getText().toString();
                        String dateofbirth_month= month.getSelectedItem().toString();
                        String dateofbirth_year= year.getText().toString();
                        String password = passEditText.getText().toString();
                        String username = emailEditText.getText().toString();
                        int statusCode = 0;
                        if (password != null && username != null && firstname !=null && dateofbirth_day != null && dateofbirth_month != null && dateofbirth_year != null) {
                            UserProfileFragment.currentUser.register(pass, user, firstname, lastname, dateofbirth_day, dateofbirth_month, dateofbirth_year);
                            statusCode = UserProfileFragment.currentUser.login(user,pass);
                        }
                        if (statusCode != 200) {
                            CharSequence text = "Invalid email and/or password! Try Again!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(RegisterActivity.this, text, duration);
                            toast.show();

                        } else {
                            Intent toMainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            toMainIntent.putExtra("registered", "true");
                            startActivity(toMainIntent);
                        }
                    }
                });

    }


}
