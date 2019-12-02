package com.example.munch.ui.foodTruck;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.munch.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class CustomInfoWindowAdaptor implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdaptor(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendowWindowText(Marker marker, View view){

        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String[] splited = marker.getSnippet().split(" ");
        Float rating = Float.parseFloat(splited[0]);
        String image = splited[1];

        ImageView tvImage = (ImageView) view.findViewById(R.id.windowpic);

        if(image != "None"){

            Bitmap logo = getBitmapFromURL(image);
            tvImage.setImageBitmap(logo);
        }

        String newSnippet = "Rating: " + rating + "\n";

        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);

        tvSnippet.setText(newSnippet);
    }

    private void rendoWindowText(Marker marker, View view, String rating){

        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = rating;
//                marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            tvSnippet.setText(rating);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
//        rendoWindowText(marker, mWindow, "Rating: 5\n picture");
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
//        rendoWindowText(marker, mWindow, "Rating: 5/n picture");
        return mWindow;

    }

    public static Bitmap getBitmapFromURL(String src)  {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e){
            // Log exception
            return null;
        }
        catch (NetworkOnMainThreadException e){
            // Log exception
            return null;
        }
    }

}
