package com.example.munch.ui.userProfile.myReviews;


import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.data.model.Review;
import com.example.munch.ui.foodTruck.FoodTruckFragment;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyReviewsListingAdapter extends ArrayAdapter<Review> {
    private Context mContext;
    private List<Review> reviewList;
//    private ImageView image1;

    public MyReviewsListingAdapter(Context context, ArrayList<Review> list) {
        super(context, 0, list);
        reviewList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.review_layout,parent,false);

        final Review currentReview = reviewList.get(position);

        //Set the author in view
        TextView author = (TextView)listItem.findViewById(R.id.review_author);
        author.setText(currentReview.getFoodTruckName());

        //Set the body of review in view
        TextView review = (TextView)listItem.findViewById(R.id.reviewbod);
        review.setText(currentReview.getReviewBody());

        //Set the date in view
        TextView date = (TextView)listItem.findViewById(R.id.dateofreview);
        ImageView proPic = listItem.findViewById(R.id.reviewerpic);
        date.setText(currentReview.getDate());

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateFormat df2 = new SimpleDateFormat("MM/dd/yy h:mm a");
        String string1 = currentReview.getDate();
        try {
            Date result1 = df1.parse(string1);
            String formattedDate = df2.format(result1);
            date.setText(formattedDate);

        } catch (ParseException e) {
        }

        RatingBar rating = (RatingBar)listItem.findViewById(R.id.ratingbar_on_review);
        rating.setIsIndicator(true);
        rating.setRating((float)currentReview.getRating());

        Picasso.with(mContext).load(currentReview.getAuthorPicture())
                .resize(100, 100)
                .centerCrop()
                .into(proPic);

        return listItem;
    }
}
