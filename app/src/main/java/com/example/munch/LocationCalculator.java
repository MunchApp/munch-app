package com.example.munch;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class LocationCalculator implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    double lat = 30.2672;
    double lng = -97.7431;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    int MY_PERMISSIONS_ACCESS_FINE_LOCATION;

    public LocationCalculator (Context context){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        int testPermission1 = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION );
        int testPermission2 = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION );
        int test3 = PackageManager.PERMISSION_GRANTED;

        if (testPermission1 == PackageManager.PERMISSION_GRANTED
                || testPermission2 == PackageManager.PERMISSION_GRANTED) {

            Criteria criteria = new Criteria();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String getLocation() {
        return lng + ", " + lat;
    }

    public String getDistance(String destAddress, String orginAddress){

        if (orginAddress.equals("Current Location"))
            orginAddress = lat+ "," +lng;
        String[] dAddress = destAddress.split(" ");
        String fAddress = dAddress[0];
        for (int c = 1; c < dAddress.length; c++){
            fAddress += "+" + dAddress[c];
        }
        dAddress = fAddress.split("\n");
        if (dAddress.length > 1)
            fAddress = dAddress[0]+",+" + dAddress[1];
        //todo hide this
        String api_key = "AIzaSyDGG8qjvXja7BIa_2lm2MIkmFTmN-lnV68";
        String response = null;

        String distance= "";
        if (fAddress != null) {
            String getDistance =orginAddress + "&destinations=" + fAddress + "&key=" + api_key;
            HttpRequests distRequests = new HttpRequests(HttpRequests.Route.DISTANCE);
            distRequests.execute(getDistance, "GET");
            try {
                response = distRequests.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                JSONObject myResponse = new JSONObject(response);
                JSONObject element = new JSONObject(myResponse.getJSONArray("rows").get(0).toString());
                JSONObject results = new JSONObject(element.getJSONArray("elements").get(0).toString());
                JSONObject distanceObject= new JSONObject(results.get("distance").toString());
                distance = distanceObject.get("text").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return distance;
    }
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLatitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
