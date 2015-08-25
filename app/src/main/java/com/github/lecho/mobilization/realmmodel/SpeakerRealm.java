package com.github.lecho.mobilization.realmmodel;

import com.github.lecho.mobilization.apimodel.SpeakerApiDto;
import com.github.lecho.mobilization.viewmodel.SpeakerViewDto;

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

    public static class SpeakerApiConverter extends RealmFacade.ApiToRealmConverter<SpeakerRealm, SpeakerApiDto> {

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

    public static class SpeakerViewConverter extends RealmFacade.RealmToViewConverter<SpeakerRealm, SpeakerViewDto> {

        @Override
        public SpeakerViewDto convert(SpeakerRealm realmObject) {
            SpeakerViewDto speakerViewDto = new SpeakerViewDto();
            speakerViewDto.key = realmObject.getKey();
            speakerViewDto.firstName = realmObject.getFirstName();
            speakerViewDto.lastName = realmObject.getLastName();
            speakerViewDto.biography = realmObject.getBiography();
            speakerViewDto.photo = realmObject.getPhoto();
            speakerViewDto.twitterProfile = realmObject.getTwitterProfile();
            speakerViewDto.wwwPage = realmObject.getWebPage();
            return speakerViewDto;
        }
    }
}
