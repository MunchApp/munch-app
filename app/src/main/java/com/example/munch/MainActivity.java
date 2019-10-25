package com.example.munch;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class MainActivity extends AppCompatActivity {
    private Bitmap bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.loadImage("https://s3-media4.fl.yelpcdn.com/bphoto/j0m1Ru-GyRSejU0O8jMOQQ/o.jpg", new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                ImageView im = findViewById(R.id.testImage);
//                im.setImageBitmap(loadedImage);
//                bit = loadedImage;
//            }
//        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_favorites, R.id.navigation_user_profile, R.id.navigation_more)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);


        final TextView textView = (TextView) findViewById(R.id.text);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://munch-server.herokuapp.com/register";

        /*JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("firstName", "Andrea");
            jsonBody.put("lastName", "Nguyen");
            jsonBody.put("email", "ngynandrea@gmail.com");
            jsonBody.put("password", "temp_pass");
            jsonBody.put("dateOfBirth", "1998-03-30");
        }catch (JSONException ex) {
            System.out.println("Login Failed");
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textView.setText("Response:" + response.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                textView.setText("ERROR");
                if (error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;
                    textView.setText(statusCode);
                }
            }
        });*/

        //queue.add(req);



    }

}
