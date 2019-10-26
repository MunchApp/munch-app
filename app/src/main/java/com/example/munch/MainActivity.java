package com.example.munch;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.munch.ui.foodTruck.FoodTruckFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //public static LoggedInUser currentUser = new LoggedInUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


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

        String temp = getIntent().getStringExtra("registered");
        if (temp != null) {
            if (temp.equals("true")) {
                navController.navigate(R.id.navigation_user_profile);
            }
        }
    }

    public void goToFoodTruck(View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Fragment myFragment = new FoodTruckFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();


    }

}
