package com.example.munch.ui.foodTruck;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
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

import com.example.munch.Config;
import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.login.LoginActivity;
import com.example.munch.ui.map.MapViewModel;
import com.example.munch.ui.register.RegisterActivity;
import com.example.munch.ui.userProfile.UserProfileFragment;

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
                        Config.bitmapImage = encodeTobase64(selectedImage);
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
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }


}
