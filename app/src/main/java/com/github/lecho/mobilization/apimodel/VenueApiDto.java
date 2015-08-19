package com.github.lecho.mobilization.apimodel;

/**
 * Created by Leszek on 2015-07-24.
 */
public class VenueApiDto extends BaseApiDto {

    public String key;
    public String title;

    @Override
    public String toString() {
        return "VenueRealm{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
