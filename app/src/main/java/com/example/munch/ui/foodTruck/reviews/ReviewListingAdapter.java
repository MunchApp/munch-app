package com.example.munch.ui.foodTruck.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.munch.MunchTools;
import com.example.munch.R;
import com.example.munch.data.model.Review;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class ReviewListingAdapter extends RecyclerView.Adapter<ReviewListingAdapter.MyViewHolder> {
    private List<Review> reviews;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReviewListingAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReviewListingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View root = LayoutInflater.from(context).inflate(R.layout.review_layout, parent,false);

        MyViewHolder vh = new MyViewHolder(root);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        final Review currentReview = reviews.get(position);

        //Set the author in view
        TextView author = (TextView)holder.view.findViewById(R.id.review_author);
        author.setText(currentReview.getAuthorName());

        //Set the body of review in view
        TextView review = (TextView)holder.view.findViewById(R.id.reviewbod);
        review.setText(currentReview.getReviewBody());

        //Set the date in view
        TextView date = (TextView)holder.view.findViewById(R.id.dateofreview);
        ImageView proPic = holder.view.findViewById(R.id.reviewerpic);
        date.setText(MunchTools.ISOtoReg(currentReview.getDate()));


        RatingBar rating = (RatingBar)holder.view.findViewById(R.id.ratingbar_on_review);
        rating.setIsIndicator(true);
        rating.setRating((float)currentReview.getRating());


        String picture = currentReview.getAuthorPicture();
        if (picture==null || picture.equals("")){
            picture = "https://www.warnersstellian.com/Content/images/product_image_not_available.png";
        }
        Picasso.with(context).load(picture)
                .resize(100, 100)
                .centerCrop()
                .into(proPic);



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
