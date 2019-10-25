package com.example.munch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.munch.data.model.LoggedInUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    public LoggedInUser currentUser = new LoggedInUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String email = getIntent().getStringExtra("username");
        final String pass = getIntent().getStringExtra("password");
        final String firstname = getIntent().getStringExtra("firstname");
        final String lastname = getIntent().getStringExtra("lastname");
        final String dob = getIntent().getStringExtra("dateofbirth");
        SharedPreferences sp = getSharedPreferences("key", 0);
        SharedPreferences.Editor ipVals = sp.edit();
        if (pass != null && email != null && firstname !=null && dob != null) {
            currentUser.register(pass, email, firstname, lastname, dob);

            ipVals.putString("firstName", firstname);
            ipVals.putString("lastName", lastname);
            ipVals.putString("email", email);
            ipVals.putString("dob", dob);

        }
        ipVals.putString("loggedIn", Boolean.toString(currentUser.getLoggedIn()));
        ipVals.commit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_favorites, R.id.navigation_user_profile, R.id.navigation_more)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

    }

}
