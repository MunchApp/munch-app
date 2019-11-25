package com.example.munch.ui.userProfile.myReviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.munch.R;
import com.example.munch.data.model.MunchUser;
import com.example.munch.data.model.Review;
import com.example.munch.ui.userProfile.UserProfileFragment;

import java.util.ArrayList;

public class MyReviewsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_truck, container, false);
        ListView reviewList = (ListView) root.findViewById(R.id.list_of_trucks);
        TextView title = (TextView) root.findViewById(R.id.text_list_prompt);
        title.setText("Your Reviews");
        ArrayList<String> listings = new ArrayList<String>();
        listings = MunchUser.getInstance().getReviews();
        if (listings.size() != 0) {
            ArrayList<Review> reviewsListings = new ArrayList<Review>();
            for (String s: listings){
                reviewsListings.add(new Review(s));
            }
            MyReviewsListingAdapter mAdapter = new MyReviewsListingAdapter(getActivity(), reviewsListings);
            reviewList.setAdapter(mAdapter);
        }

        return root;
    }
}
