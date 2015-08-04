package com.github.lecho.mobilization.apimodel;

/**
 * Created by Leszek on 2015-07-24.
 */
public class Speaker extends BaseApiModel {

    public String firstname;
    public String lastname;
    public String bioHtml;
    public String www;
    public String twitter;
    public String photoUrl;

    @Override
    public String toString() {
        return "Speaker{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", bioHtml='" + bioHtml + '\'' +
                ", www='" + www + '\'' +
                ", twitter='" + twitter + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
