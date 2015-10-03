package com.github.lecho.conference.viewmodel;

import com.github.lecho.conference.realmmodel.SponsorType;

/**
 * Created by Leszek on 2015-07-29.
 */
public class SponsorViewDto {

    public String name;
    public String wwwPage;
    public String logo;
    public SponsorType type;

    @Override
    public String toString() {
        return "SponsorViewDto{" +
                "name='" + name + '\'' +
                ", wwwPage='" + wwwPage + '\'' +
                ", logo='" + logo + '\'' +
                ", type=" + type +
                '}';
    }
}
