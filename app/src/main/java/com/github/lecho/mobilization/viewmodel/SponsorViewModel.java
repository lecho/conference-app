package com.github.lecho.mobilization.viewmodel;

import com.github.lecho.mobilization.realmmodel.SponsorType;

/**
 * Created by Leszek on 2015-07-29.
 */
public class SponsorViewModel {

    public String name;
    public String wwwPage;
    public String logo;
    public SponsorType type;

    @Override
    public String toString() {
        return "SponsorViewModel{" +
                "name='" + name + '\'' +
                ", wwwPage='" + wwwPage + '\'' +
                ", logo='" + logo + '\'' +
                ", type=" + type +
                '}';
    }
}
