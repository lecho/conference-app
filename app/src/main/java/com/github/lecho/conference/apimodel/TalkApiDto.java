package com.github.lecho.conference.apimodel;

import java.util.Arrays;

/**
 * Created by Leszek on 2015-07-24.
 */
public class TalkApiDto {

    public String title;
    public String descriptionHtml;
    public String[] speakersKeys;
    public String language;

    @Override
    public String toString() {
        return "TalkRealm{" +
                "title='" + title + '\'' +
                ", descriptionHtml='" + descriptionHtml + '\'' +
                ", speakersKeys=" + Arrays.toString(speakersKeys) +
                ", language='" + language + '\'' +
                '}';
    }
}
