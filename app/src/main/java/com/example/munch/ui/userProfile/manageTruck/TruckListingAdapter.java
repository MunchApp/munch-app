package com.example.munch.ui.userProfile.manageTruck;

import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.munch.MainActivity;
import com.example.munch.R;
import com.example.munch.data.model.FoodTruck;
import com.example.munch.ui.foodTruck.FoodTruckFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TruckListingAdapter extends ArrayAdapter<FoodTruck> {
    private Context mContext;
    private List<FoodTruck> truckList;
//    private ImageView image1;

    public TruckListingAdapter(Context context, ArrayList<FoodTruck> list) {
        super(context, 0, list);truckList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_truck,parent,false);

        final FoodTruck currentResult = truckList.get(position);

        ImageView image1 = (ImageView)listItem.findViewById(R.id.main_image);
        image1.setClickable(true);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity)mContext;
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FoodTruckFragment NAME = new FoodTruckFragment(currentResult, true);
                fragmentTransaction.replace(R.id.nav_host_fragment, NAME);
                fragmentTransaction.commit();
            }
        });
        if (currentResult.getPhotos().size() > 0) {
            Picasso.with(mContext).load(currentResult.getPhotos().get(0))
                    .resize(390, 260)
                    .centerCrop()
                    .into(image1);
        }

        TextView truckName = (TextView)listItem.findViewById(R.id.truck_name);
        truckName.setText(currentResult.getName());

        TextView truckAddress = (TextView)listItem.findViewById(R.id.truck_address);
        truckAddress.setText(currentResult.getAddress());

        TextView truckStatus= (TextView)listItem.findViewById(R.id.status_string);
        ImageView truckStatusIcon= (ImageView)listItem.findViewById(R.id.status);
        View gap = listItem.findViewById(R.id.gap);
        if (currentResult.getStatus()){
            truckStatusIcon.setVisibility(View.VISIBLE);
            gap.setVisibility(View.VISIBLE);
            ImageViewCompat.setImageTintList(truckStatusIcon, ColorStateList.valueOf(ContextCompat.getColor(this.getContext(), R.color.onlineGreen)));
            truckStatus.setText("ONLINE");
        } else {
            truckStatusIcon.setVisibility(View.GONE);
            gap.setVisibility(View.GONE);
            /*ImageViewCompat.setImageTintList(truckStatusIcon, ColorStateList.valueOf(ContextCompat.getColor(this.getContext(), R.color.offlineRed)));
            truckStatus.setText("OFFLINE");*/
        }

        return listItem;
    }
}
