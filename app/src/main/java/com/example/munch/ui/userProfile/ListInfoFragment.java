package com.example.munch.ui.userProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.munch.R;
import com.example.munch.data.model.Review;
import com.example.munch.ui.foodTruck.createTruck.createTruckActivity;
import com.example.munch.ui.userProfile.UserProfileFragment;
import com.example.munch.ui.userProfile.myReviews.MyReviewsListingAdapter;

import java.util.ArrayList;

public class ListInfoFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_info, container, false);
        Button create = root.findViewById(R.id.create_truck);
        create.setOnClickListener(           //action triggered on button click
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent toListPage = new Intent(getActivity(), createTruckActivity.class);
                        startActivity(toListPage);
                    }
                });
        return root;
    }
}
