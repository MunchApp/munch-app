package com.example.munch.ui.foodTruck.reviews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.munch.R;
import com.google.android.material.textfield.TextInputLayout;

public class AddReviewActivity extends AppCompatActivity {

    Button mSubmitReviewBtn;
    TextInputLayout mReviewContent;
    RatingBar mReviewRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        mReviewRating = findViewById(R.id.ratingBarReview);
        mReviewContent = findViewById(R.id.reviewContent);
        mSubmitReviewBtn = findViewById(R.id.submitNewReviewBtn);
        mSubmitReviewBtn.setOnClickListener(           //action triggered on button click
                new View.OnClickListener() {
                    public void onClick(View view) {

                        String content = mReviewContent.getEditText().getText().toString();
                        String rating = String.valueOf(mReviewRating.getRating());
                        Intent intent = new Intent();
                        intent.putExtra("content", content);
                        intent.putExtra("rating", rating);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

    }

}
