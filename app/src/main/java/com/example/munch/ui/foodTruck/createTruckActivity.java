package com.example.munch.ui.foodTruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.munch.R;

public class createTruckActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_truck);


        final EditText name = findViewById(R.id.truck_name);
        final EditText address = findViewById(R.id.address);
        final EditText address2 = findViewById(R.id.address2);
        final EditText photo = findViewById(R.id.photo_url);
        final EditText tags = findViewById(R.id.tags);
        final Button next = findViewById(R.id.next_create);

        final String[] truck_photos =  {photo.getText().toString()};

        next.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        Intent toMainIntent = new Intent(createTruckActivity.this, createTruckActivity2.class);
                        toMainIntent.putExtra("name", name.getText().toString());
                        toMainIntent.putExtra("address", address.getText().toString() + "\n" +address2.getText().toString());
                        toMainIntent.putExtra("photo", photo.getText().toString());
                        toMainIntent.putExtra("tags", tags.getText().toString());
                        startActivity(toMainIntent);

                    }
                });
    }
}
