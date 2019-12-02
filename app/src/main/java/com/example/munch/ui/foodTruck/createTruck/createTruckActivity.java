package com.example.munch.ui.foodTruck.createTruck;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.munch.Config;
import com.example.munch.MunchTools;
import com.example.munch.R;
import com.example.munch.data.model.MunchUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class createTruckActivity extends AppCompatActivity {

    private final int SELECT_PHOTO = 1;
    private Bitmap selectedImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_truck);

        final EditText name = findViewById(R.id.truck_name);
        final EditText address = findViewById(R.id.address);
        final EditText address2 = findViewById(R.id.address2);
        /*final EditText photo = findViewById(R.id.photo_url);*/
        final Button pickImage = findViewById(R.id.photo_url);
        final EditText tags = findViewById(R.id.tags);
        final Button next = findViewById(R.id.next_create);

        //final String[] truck_photos =  {photo.getText().toString()};

        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        next.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        Intent toMainIntent = new Intent(createTruckActivity.this, createTruckActivity2.class);
                        toMainIntent.putExtra("name", name.getText().toString());
                        toMainIntent.putExtra("address", address.getText().toString() + "\n" +address2.getText().toString());
                        toMainIntent.putExtra("photo", "");
                        toMainIntent.putExtra("tags", tags.getText().toString());
                        //toMainIntent.putExtra("bitmap", encodeTobase64(selectedImage));
                        if (selectedImage !=null) {
                            String bitmap = MunchTools.encodeTobase64(selectedImage);
                            MunchUser.getInstance().addTempPhoto(bitmap);
                        }
                        startActivity(toMainIntent);

                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        //imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



}
