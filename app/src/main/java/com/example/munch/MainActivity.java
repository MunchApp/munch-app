package com.example.munch;


import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //public static LoggedInUser currentUser = new LoggedInUser();
    int MY_PERMISSIONS_ACCESS_FINE_LOCATION;
    int MY_PERMISSIONS_ACCESS_COARSE_LOCATION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int testPermission1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION );
        int testPermission2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION );

        if(testPermission1 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION} ,
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }
        if(testPermission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION} ,
                    MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.munchOrangeDark)));
        navView.setItemTextColor(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.munchOrangeDark)));
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


}
