package com.github.lecho.mobilization.realmmodel;

import com.github.lecho.mobilization.apimodel.SpeakerApiDto;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmObject;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SpeakerRealm extends RealmObject {

    private String key;
    private String firstName;
    private String lastName;
    private String biography;
    private String webPage;
    private String twitterProfile;
    private String photo;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public String getTwitterProfile() {
        return twitterProfile;
    }

    public void setTwitterProfile(String twitterProfile) {
        this.twitterProfile = twitterProfile;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static class SpeakerConverter extends RealmFacade.RealmConverter<SpeakerRealm, SpeakerApiDto> {

        @Override
        public SpeakerRealm convert(String key, SpeakerApiDto apiDto) {
            SpeakerRealm speakerRealm = new SpeakerRealm();
            speakerRealm.setKey(key);
            speakerRealm.setBiography(apiDto.bioHtml);
            speakerRealm.setFirstName(apiDto.firstname);
            speakerRealm.setLastName(apiDto.lastname);
            //TODO parse photo name
            speakerRealm.setPhoto(apiDto.photoUrl);
            speakerRealm.setTwitterProfile(apiDto.twitter);
            speakerRealm.setWebPage(apiDto.www);
            return speakerRealm;
        }
    }
}
