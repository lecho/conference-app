package com.github.lecho.mobilization.viewmodel;

import android.support.annotation.NonNull;

/**
 * Created by Leszek on 2015-07-29.
 */
public class SpeakerViewModel {

    public String key;
    public String firstName;
    public String lastName;
    public String biography;
    public String website;
    public String twitter;
    public String github;
    public String photo;

    @NonNull
    public String getSpeakerNameText() {
        return new StringBuilder(firstName).append(" ").append(lastName).toString();
    }

    @Override
    public String toString() {
        return "SpeakerViewModel{" +
                "key='" + key + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", biography='" + biography + '\'' +
                ", website='" + website + '\'' +
                ", twitter='" + twitter + '\'' +
                ", github='" + github + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
