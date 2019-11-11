package com.example.munch.ui.foodTruck.reviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.munch.HttpRequests;
import com.example.munch.R;
import com.example.munch.ui.userProfile.UserProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ReviewsPageActivity extends AppCompatActivity {

    Button mAddReviewBtn;
    ListView resultsList;
    ArrayList<ReviewListing> listings;
    ReviewListingAdapter adapter;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_page);
        id = getIntent().getStringExtra("id");
        populateReviews(findViewById(R.id.review_page));
        mAddReviewBtn = findViewById(R.id.reviewButton);
        if(!UserProfileFragment.currentUser.getLoggedIn()){
            mAddReviewBtn.setEnabled(false);
        } else {
            mAddReviewBtn.setEnabled(true);
            mAddReviewBtn.setOnClickListener(           //action triggered on button click
                    new View.OnClickListener() {
                        public void onClick(View view) {

                            Intent addReviewIntent = new Intent(ReviewsPageActivity.this, AddReviewActivity.class);
                            startActivityForResult(addReviewIntent, 1);
                        }
                    });
        }

    }

    private void populateReviews(View root) {
        resultsList = (ListView) root.findViewById(R.id.review_layout);
        listings = new ArrayList<>();
        HttpRequests reviewRequest = new HttpRequests();
        if (id != null) {
            reviewRequest.execute("reviews/foodtruck/" + id, "GET");

            String responseReview = null;
            try {
                responseReview = reviewRequest.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }


            try {
                JSONArray reviewData = new JSONArray(responseReview);
                if (reviewData.length() == 0) {
                    FrameLayout frameLayout = (FrameLayout) findViewById(R.id.review_page);
                    TextView textView = new TextView(this);
                    textView.setText("No reviews to display!");
                    textView.setPadding(10, 10, 10, 10);
                    textView.setGravity(Gravity.CENTER);
                    frameLayout.addView(textView);
                } else {
                    for (int i = 0; i < reviewData.length(); i++) {
                        JSONObject jsonobject = reviewData.getJSONObject(i);
                        String author = jsonobject.getString("reviewer");
                        String date = jsonobject.getString("date");
                        String reviewBody = jsonobject.getString("comment");
                        Double rating = jsonobject.getDouble("rating");
                        listings.add(new ReviewListing(author, date, reviewBody, rating));
                    }
                    adapter = new ReviewListingAdapter(this, listings);
                    resultsList.setAdapter(adapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ReviewsPageActivity.this.invalidateOptionsMenu();
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
//                ReviewListing newReview = new ReviewListing("anon", (new Date().toString()), data.getStringExtra("content"), Double.valueOf(data.getStringExtra("rating")));
//                listings.add(newReview);
//                adapter.add(newReview);
//                adapter.notifyDataSetChanged();
                JSONObject review = new JSONObject();
                try {
                    review.put("foodTruck", id);
                    review.put("comment", data.getStringExtra("content"));
                    review.put("rating", Float.valueOf(data.getStringExtra("rating")));
                } catch (JSONException ex) {
                    System.out.println("Post Review Failed");
                }
                String token = UserProfileFragment.currentUser.getAccessToken();
                HttpRequests reviewRequests = new HttpRequests();
                reviewRequests.execute("reviews", "POST", review.toString(), token);
                String responseLogin = null;
                try {
                    responseLogin = reviewRequests.get();
                    String responseCode = reviewRequests.getStatus().toString();
                    System.out.println(responseCode);
                    populateReviews(findViewById(R.id.review_page));

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}


