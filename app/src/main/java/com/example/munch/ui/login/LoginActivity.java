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

import com.example.munch.R;
import com.example.munch.ui.map.MapViewModel;
import com.example.munch.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);


        loginButton.setOnClickListener(           //action triggered on button click
                new View.OnClickListener() {
                    public void onClick(View view) {

                        Intent toRegPage = new Intent(LoginActivity.this, RegisterActivity.class);
                        toRegPage.putExtra(USERNAME,usernameEditText.getText().toString());
                        toRegPage.putExtra(PASSWORD,passwordEditText.getText().toString());
                        startActivity(toRegPage);
                    }
                });

    }
}
