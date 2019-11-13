package com.example.munch.ui.foodTruck.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.munch.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewListingAdapter extends ArrayAdapter<ReviewListing>{

    //Just need a context() and will make a list of reviews each styled by review_layout
    private Context Context;
    private List<ReviewListing> Reviews;

    //Constructor
    public ReviewListingAdapter(Context context, ArrayList<ReviewListing> list) {
        super(context, 0, list);
        Context = context;
        Reviews = list;
    }

    //This method will return the view
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View aReview = convertView;
        if(aReview == null)
            aReview = LayoutInflater.from(Context).inflate(R.layout.review_layout,parent,false);

        ReviewListing currentReview = Reviews.get(position);

        //Set the author in view
        TextView author = (TextView)aReview.findViewById(R.id.review_author);
        author.setText(currentReview.getAuthor());

        //Set the body of review in view
        TextView review = (TextView)aReview.findViewById(R.id.reviewbod);
        review.setText(currentReview.getReviewBody());

        //Set the date in view
        TextView date = (TextView)aReview.findViewById(R.id.dateofreview);
        date.setText(currentReview.getDate());

        RatingBar rating = (RatingBar)aReview.findViewById(R.id.ratingbar_on_review);
        rating.setRating((float)currentReview.getRating());

        return aReview;
    }
}
