package com.github.lecho.mobilization.apimodel;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

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

    public static class TalkApiParser extends BaseApiParser<TalkApiDto> {

        @Override
        public Map<String, TalkApiDto> fromJson(String json) {
            Type type = new TypeToken<Map<String, TalkApiDto>>() {
            }.getType();
            return parseJson(json, type);
        }
    }
}
