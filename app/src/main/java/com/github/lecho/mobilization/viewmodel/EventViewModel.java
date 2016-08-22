package com.github.lecho.mobilization.viewmodel;

import android.support.annotation.NonNull;

/**
 * Created by Leszek on 2015-09-28.
 */
public class EventViewModel {

    public String title;
    public String date;
    public String time;
    public String place;
    public String street;
    public String city;
    public String zip;
    public String country;
    public double latitude;
    public double longitude;

    @NonNull
    public String getAddress(){
        return  street + ", " + city;
    }

    @NonNull
    public String getDate() {
        return new StringBuilder()
                .append(date)
                .append(", ")
                .append(time)
                .toString();
    }

    @NonNull
    public String getPlace() {
        return new StringBuilder()
                .append(place)
                .append(" ")
                .append(street)
                .append(", ")
                .append(city)
                .toString();
    }
}
