package com.github.lecho.mobilization.apimodel;

import java.util.Arrays;

/**
 * Created by Leszek on 2015-07-24.
 */
public class TalkApiDto extends BaseApiDto{

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
