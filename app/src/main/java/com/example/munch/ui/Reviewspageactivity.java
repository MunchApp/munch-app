package com.example.munch.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.munch.R;
import com.example.munch.ReviewListing;
import com.example.munch.ReviewListingAdapter;

import java.util.ArrayList;

public class Reviewspageactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewspageactivity);
        populateReviews(findViewById(R.id.review_page));
    }

    private void populateReviews(View root) {
        ListView resultsList = (ListView) root.findViewById(R.id.review_layout);
        ArrayList<ReviewListing> listings = new ArrayList<>();
        //TODO Make Dynamic

        listings.add(new ReviewListing("SYED NAQVI", "October 25,2019",
                "This is a review! this food truck is so amazing! Please let me see this food truck!\nI want to eat here so bad! Nevermind I hate this place!\nOK just kidding, this place is awesome please come eat here!",4.9));
        listings.add(new ReviewListing("JANINE BARIUAN", "October 25,2019",
                "This is a review! this food truck is so amazing! Please let me see this food truck!\nI want to eat here so bad! Nevermind I hate this place!\nOK just kidding, this place is awesome please come eat here!",4.9));
        ReviewListingAdapter adapter = new ReviewListingAdapter(this, listings);
        resultsList.setAdapter(adapter);

    }
}

