package com.example.munch.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.foodTruck.FoodTruckFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchListingAdapter extends ArrayAdapter<FoodTruck> {

    private Context mContext;
    private List<FoodTruck> searchList;
//    private ImageView image1;

    SearchListingAdapter(Context context, ArrayList<FoodTruck> list) {
        super(context, 0, list);
        mContext = context;
        searchList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.search_results,parent,false);

        final FoodTruck currentResult = searchList.get(position);

        ImageView image1 = (ImageView)listItem.findViewById(R.id.image1);
        Picasso.with(mContext).load(currentResult.getPhotos().get(0))
                .resize(110, 110)
                .centerCrop()
                .into(image1);

        ImageView image2 = (ImageView)listItem.findViewById(R.id.image2);
        Picasso.with(mContext).load(currentResult.getPhotos().get(1))
                .resize(110, 110)
                .centerCrop()
                .into(image2);

        ImageView image3 = (ImageView)listItem.findViewById(R.id.image3);
        Picasso.with(mContext).load(currentResult.getPhotos().get(2))
                .resize(110, 110)
                .centerCrop()
                .into(image3);

        TextView truckName = (TextView)listItem.findViewById(R.id.truckName);
        truckName.setText(currentResult.getName());

        //todo distance is calculated incorrectly
        TextView truckDistance = (TextView)listItem.findViewById(R.id.truckDistance);
        truckDistance.setText("");

        RatingBar rating = (RatingBar)listItem.findViewById(R.id.results_rating_bar);
        rating.setIsIndicator(true);
        rating.setRating(currentResult.getAvgRating());

        Button goToTruckView = (Button) listItem.findViewById(R.id.button);
        goToTruckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity)mContext;
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FoodTruckFragment NAME = new FoodTruckFragment(currentResult, false);
                fragmentTransaction.replace(R.id.nav_host_fragment, NAME);
                fragmentTransaction.addToBackStack("map");
                fragmentTransaction.commit();
            }
        });



        return listItem;
    }

}
