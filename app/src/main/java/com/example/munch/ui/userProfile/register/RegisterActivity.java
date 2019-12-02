package com.example.munch.ui.userProfile.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import com.example.munch.MainActivity;
import com.example.munch.MunchTools;
import com.example.munch.R;
import com.example.munch.data.model.MunchUser;

import java.util.ArrayList;
import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private EditText emailInput;
    private EditText passwordInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private DatePicker dobInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameInput = findViewById(R.id.first_name);
        lastNameInput = findViewById(R.id.last_name);
        emailInput = findViewById(R.id.email_name);
        passwordInput = findViewById(R.id.pass_name);
        dobInput = (DatePicker) findViewById(R.id.dob);

        final ArrayList<EditText> inputs = new ArrayList<>();
        inputs.add(firstNameInput);
        inputs.add(lastNameInput);
        inputs.add(emailInput);
        inputs.add(passwordInput);

        String user = getIntent().getStringExtra(USERNAME);
        String pass = getIntent().getStringExtra(PASSWORD);


        emailInput.setText(user);
        passwordInput.setText(pass);


        final Button registerButton = findViewById(R.id.register);



        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        boolean registerValid = true;
                        for (EditText v: inputs){
                            if (v.getText().toString().equals(""))
                                registerValid = false;
                        }

                        if (registerValid) {
                            HashMap<String, String> registerInfo = new HashMap<>();
                            registerInfo.put("firstName", firstNameInput.getText().toString());
                            registerInfo.put("lastName", lastNameInput.getText().toString());
                            registerInfo.put("password", passwordInput.getText().toString());
                            registerInfo.put("dateOfBirth", MunchTools.toISODate(dobInput));
                            registerInfo.put("email", emailInput.getText().toString());

                            int statusCode = MunchUser.getInstance().register(registerInfo);

                            Intent toMainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            toMainIntent.putExtra("registered", "true");
                            startActivity(toMainIntent);
                        }

                    }
                });

    }


}
