package com.example.munch;

import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.DatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MunchTools {
    public static String callMunchRoute(HttpRequests request) {
        String response = null;
        try {
            response = request.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String getValueFromJson(String value, String response) {
        String returnValue = "";
        try {
            JSONObject jsonToken = new JSONObject(response);
            returnValue = jsonToken.get(value).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public static String toISODate(DatePicker dateInput){
        Calendar calendar = Calendar.getInstance();
        calendar.set(dateInput.getYear(), dateInput.getMonth(), dateInput.getDayOfMonth(), 00, 00, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String isoDOB = sdf.format(date);
        return isoDOB;
    }

    public static String milToReg(String time){
        int hours = Integer.valueOf(time.split(":")[0]);
        String min = time.split(":")[1];
        String ampm = "";
        if (hours < 12) {
            ampm = "AM";
        } else {
            ampm = "PM";
            hours-=12;
        }

        if (hours == 0){
            hours = 12;
        }
        return hours + ":" + min + " " +ampm;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static String ISOtoReg (String string1){
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateFormat df2 = new SimpleDateFormat("MM/dd/yy h:mm a");
        try {
            Date result1 = df1.parse(string1);
            String formattedDate = df2.format(result1);
            return formattedDate;

        } catch (ParseException e) {
        }
        return string1;
    }
}
