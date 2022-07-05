package com.antizon.uit_android.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyMapUtils {


    public static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable, int width, int height) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, width, width);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static String getAddress(Context context, LatLng latLng) {

        if (latLng == null) {
            return "";
        }
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty())
            return addresses.get(0).getAddressLine(0);
        else
            return "";
    }


    public static String getCityCountry(Context context, LatLng latLng) {

        if (latLng == null) {
            return "";
        }
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()) {
            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();
            return city + ", " + country;
        } else
            return "";
    }

    public static String getCityCountry(Context context, double latitude, double longitude) {

        LatLng latLng = new LatLng(latitude, longitude);
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()) {
            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();
            return city + ", " + country;
        } else
            return "";
    }
}
