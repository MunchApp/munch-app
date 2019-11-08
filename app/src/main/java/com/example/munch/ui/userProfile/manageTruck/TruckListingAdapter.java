package com.example.munch.ui.userProfile.manageTruck;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TruckListingAdapter extends ArrayAdapter<FoodTruck> {
    private Context mContext;
    private List<FoodTruck> truckList;
//    private ImageView image1;

    public TruckListingAdapter(Context context, ArrayList<FoodTruck> list) {
        super(context, 0, list);
        mContext = context;
        truckList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_truck,parent,false);

        FoodTruck currentResult = truckList.get(position);

        ImageView image1 = (ImageView)listItem.findViewById(R.id.main_image);
        Picasso.with(mContext).load(currentResult.getPhotos().get(0))
                .resize(390, 260)
                .centerCrop()
                .into(image1);


        TextView truckName = (TextView)listItem.findViewById(R.id.truck_name);
        truckName.setText(currentResult.getName());

        TextView truckAddress = (TextView)listItem.findViewById(R.id.truck_address);
        truckAddress.setText(currentResult.getAddress());

        TextView truckStatus= (TextView)listItem.findViewById(R.id.status_string);
        ImageView truckStatusIcon= (ImageView)listItem.findViewById(R.id.status);
        if (currentResult.getStatus()){
            ImageViewCompat.setImageTintList(truckStatusIcon, ColorStateList.valueOf(ContextCompat.getColor(this.getContext(), R.color.onlineGreen)));
            truckAddress.setText("ONLINE");
        } else {
            ImageViewCompat.setImageTintList(truckStatusIcon, ColorStateList.valueOf(ContextCompat.getColor(this.getContext(), R.color.offlineRed)));
            truckAddress.setText("OFFLINE");
        }

        return listItem;
    }
}
