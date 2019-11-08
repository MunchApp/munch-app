package com.example.munch.ui.login;

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
import com.example.munch.ui.map.MapViewModel;
import com.example.munch.ui.register.RegisterActivity;
import com.example.munch.ui.userProfile.UserProfileFragment;

public class LoginActivity extends AppCompatActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button login = findViewById(R.id.login);
        final TextView newAccount = findViewById(R.id.new_account);


        newAccount.setOnClickListener(           //action triggered on button click
                new View.OnClickListener() {
                    public void onClick(View view) {

                        Intent toRegPage = new Intent(LoginActivity.this, RegisterActivity.class);
                        toRegPage.putExtra(USERNAME, usernameEditText.getText().toString());
                        toRegPage.putExtra(PASSWORD, passwordEditText.getText().toString());
                        startActivity(toRegPage);
                    }
                });

        login.setOnClickListener(           //action triggered on button click
                new View.OnClickListener() {
                    public void onClick(View view) {
                        int statusCode = UserProfileFragment.currentUser.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
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
