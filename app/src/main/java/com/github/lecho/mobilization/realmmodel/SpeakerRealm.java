package com.github.lecho.mobilization.realmmodel;

import android.net.Uri;

import com.github.lecho.mobilization.apimodel.SpeakerApiModel;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SpeakerRealm extends RealmObject {

    @PrimaryKey
    private String key;
    private String firstName;
    private String lastName;
    private String biography;
    private String website;
    private String github;
    private String twitter;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static class SpeakerApiConverter extends RealmFacade.ApiToRealmConverter<SpeakerRealm, SpeakerApiModel> {

        @Override
        public SpeakerRealm convert(String key, SpeakerApiModel apiDto) {
            SpeakerRealm speakerRealm = new SpeakerRealm();
            speakerRealm.setKey(key);
            speakerRealm.setBiography(apiDto.bioHtml);
            speakerRealm.setFirstName(apiDto.firstname);
            speakerRealm.setLastName(apiDto.lastname);
            speakerRealm.setPhoto(apiDto.photoUrl);
            speakerRealm.setTwitter(apiDto.twitter);
            speakerRealm.setGithub(apiDto.github);
            speakerRealm.setWebsite(apiDto.www);
            return speakerRealm;
        }
    }

    public static class SpeakerViewConverter extends RealmFacade.RealmToViewConverter<SpeakerRealm, SpeakerViewModel> {

        @Override
        public SpeakerViewModel convert(SpeakerRealm realmObject) {
            SpeakerViewModel speakerViewModel = new SpeakerViewModel();
            speakerViewModel.key = realmObject.getKey();
            speakerViewModel.firstName = realmObject.getFirstName();
            speakerViewModel.lastName = realmObject.getLastName();
            speakerViewModel.biography = realmObject.getBiography();
            speakerViewModel.photo = Uri.parse(realmObject.getPhoto()).getLastPathSegment();
            speakerViewModel.twitter = realmObject.getTwitter();
            speakerViewModel.github = realmObject.getGithub();
            speakerViewModel.website = realmObject.getWebsite();
            return speakerViewModel;
        }
    }
}
