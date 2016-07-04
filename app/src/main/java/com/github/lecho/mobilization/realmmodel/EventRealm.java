package com.github.lecho.mobilization.realmmodel;

import com.github.lecho.mobilization.apimodel.EventApiModel;
import com.github.lecho.mobilization.viewmodel.EventViewModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leszek on 2015-07-24.
 */
public class EventRealm extends RealmObject {

    @PrimaryKey
    private String title;
    private String date;
    private String time;
    private String place;
    private String street;
    private String city;
    private String zip;
    private String country;
    private double latitude;
    private double longitude;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static class EventApiConverter extends RealmFacade.ApiToRealmConverter<EventRealm, EventApiModel> {
        @Override
        public EventRealm convert(String key, EventApiModel apiDto) {
            EventRealm eventRealm = new EventRealm();
            eventRealm.setTitle(apiDto.title);
            eventRealm.setDate(apiDto.data);
            eventRealm.setTime(apiDto.time);
            eventRealm.setCity(apiDto.location.city);
            eventRealm.setCountry(apiDto.location.country);
            eventRealm.setLatitude(apiDto.location.lat);
            eventRealm.setLongitude(apiDto.location.lng);
            eventRealm.setPlace(apiDto.location.place);
            eventRealm.setStreet(apiDto.location.street);
            eventRealm.setZip(apiDto.location.zip);
            return eventRealm;
        }
    }

    public static class EventViewConverter extends RealmFacade.RealmToViewConverter<EventRealm, EventViewModel> {

        @Override
        public EventViewModel convert(EventRealm realmObject) {
            EventViewModel eventViewModel = new EventViewModel();
            eventViewModel.title = realmObject.getTitle();
            eventViewModel.city = realmObject.getCity();
            eventViewModel.country = realmObject.getCountry();
            eventViewModel.date = realmObject.getDate();
            eventViewModel.latitude = realmObject.getLatitude();
            eventViewModel.longitude = realmObject.getLongitude();
            eventViewModel.place = realmObject.getPlace();
            eventViewModel.street = realmObject.getStreet();
            eventViewModel.time = realmObject.getTime();
            eventViewModel.zip = realmObject.getZip();
            return eventViewModel;
        }
    }
}
