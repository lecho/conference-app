package com.github.lecho.mobilization.viewmodel;

import android.support.annotation.NonNull;

/**
 * Created by Leszek on 2015-07-29.
 */
public class SpeakerViewDto {

    public String key;
    public String firstName;
    public String lastName;
    public String biography;
    public String wwwPage;
    public String twitterProfile;
    public String photo;

    @NonNull
    public String getSpeakerNameText() {
        return new StringBuilder(firstName).append(" ").append(lastName).toString();
    }

    @Override
    public String toString() {
        return "SpeakerViewDto{" +
                "key='" + key + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", biography='" + biography + '\'' +
                ", wwwPage='" + wwwPage + '\'' +
                ", twitterProfile='" + twitterProfile + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
