package com.github.lecho.mobilization.apimodel;

import java.util.Arrays;

/**
 * Created by Leszek on 2015-07-24.
 */
public class Talk extends BaseApiModel {

    public String title;
    public String descriptionHtml;
    public String[] speakersKeys;
    public String language;

    @Override
    public String toString() {
        return "Talk{" +
                "title='" + title + '\'' +
                ", descriptionHtml='" + descriptionHtml + '\'' +
                ", speakersKeys=" + Arrays.toString(speakersKeys) +
                ", language='" + language + '\'' +
                '}';
    }
}
