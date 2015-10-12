package com.github.lecho.mobilization.apimodel;

/**
 * Created by Leszek on 2015-09-28.
 */
public class LocationApiDto {

    public String place;
    public String street;
    public String city;
    public String zip;
    public String country;
    public double lat;
    public double lng;

    @Override
    public String toString() {
        return "LocationApiDto{" +
                "place='" + place + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
