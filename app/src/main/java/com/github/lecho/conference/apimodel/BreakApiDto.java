package com.github.lecho.conference.apimodel;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class BreakApiDto {

    public String title;
    public String descriptionHtml;

    @Override
    public String toString() {
        return "BreakRealm{" +
                "title='" + title + '\'' +
                ", descriptionHtml='" + descriptionHtml + '\'' +
                '}';
    }

    public static class BreakApiParser extends BaseApiParser<BreakApiDto> {

        @Override
        public Map<String, BreakApiDto> fromJson(String json) {
            Type type = new TypeToken<Map<String, BreakApiDto>>() {
            }.getType();
            return parseJson(json, type);
        }
    }
}
