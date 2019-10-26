package com.example.munch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchListingAdapter extends ArrayAdapter<SearchListing> {

    private Context mContext;
    private List<SearchListing> searchList;
//    private ImageView image1;

    public SearchListingAdapter(Context context, ArrayList<SearchListing> list) {
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

        SearchListing currentResult = searchList.get(position);

        ImageView image1 = (ImageView)listItem.findViewById(R.id.image1);
        Picasso.with(mContext).load(currentResult.getmPic1())
                .resize(110, 110)
                .centerCrop()
                .into(image1);

        ImageView image2 = (ImageView)listItem.findViewById(R.id.image2);
        Picasso.with(mContext).load(currentResult.getmPic2())
                .resize(110, 110)
                .centerCrop()
                .into(image2);

        ImageView image3 = (ImageView)listItem.findViewById(R.id.image3);
        Picasso.with(mContext).load(currentResult.getmPic3())
                .resize(110, 110)
                .centerCrop()
                .into(image3);

        TextView truckName = (TextView)listItem.findViewById(R.id.truckName);
        truckName.setText(currentResult.getTruckName());

        TextView truckDistance = (TextView)listItem.findViewById(R.id.truckDistance);
        truckDistance.setText(currentResult.getDistance());

//        Button goToTruckView = (Button) listItem.findViewById(R.id.button);
//        goToTruckView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                Fragment myFragment = new FoodTruckFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();
//
////                Fragment newFragment = new FoodTruckFragment();
////                FragmentTransaction transaction = getFragmentManager().beginTransaction();
////
////                transaction.replace(R.id.m, newFragment);
////                transaction.addToBackStack(null);
//
//// Commit the transaction
////                transaction.commit();
//            }
//        });

        //TODO implement getting phone number and rating in the right format

        return listItem;
    }


//    public void getBitmapFromURL(String url, int id) throws IOException {
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.loadImage(url, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                ImageView im = view.findViewById(id);
//                im.setImageBitmap(loadedImage);
//            }
//        });
//    }

}
