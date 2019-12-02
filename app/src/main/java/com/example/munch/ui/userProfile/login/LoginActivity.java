package com.example.munch.ui.userProfile.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.data.model.MunchUser;
import com.example.munch.ui.userProfile.UserProfileController;
import com.example.munch.ui.userProfile.register.RegisterActivity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button login = findViewById(R.id.login);
        final TextView newAccount = findViewById(R.id.new_account);


        newAccount.setOnClickListener(           //action triggered on button click
                new View.OnClickListener() {
                    public void onClick(View view) {

                        Intent toRegPage = new Intent(LoginActivity.this, RegisterActivity.class);
                        toRegPage.putExtra(USERNAME, emailEditText.getText().toString());
                        toRegPage.putExtra(PASSWORD, passwordEditText.getText().toString());
                        startActivity(toRegPage);
                    }
                });

        login.setOnClickListener(           //action triggered on button click
                new View.OnClickListener() {
                    public void onClick(View view) {
                        HashMap<String, String> loginInfo = new HashMap<>();
                        loginInfo.put("email", emailEditText.getText().toString());
                        loginInfo.put("password", passwordEditText.getText().toString());
                        int statusCode = MunchUser.getInstance().login(loginInfo);
                        if (statusCode != 200) {
                            CharSequence text = "Invalid email and/or password! Try Again!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(LoginActivity.this, text, duration);
                            toast.show();

                        } else {
                            Intent toMainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            toMainIntent.putExtra("registered", "true");
                            startActivity(toMainIntent);
                        }
                    }
                    });
                }
    }
