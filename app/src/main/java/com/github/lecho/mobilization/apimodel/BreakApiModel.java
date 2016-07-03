package com.github.lecho.mobilization.apimodel;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class BreakApiModel {

    public String title;
    public String descriptionHtml;

    @Override
    public String toString() {
        return "BreakRealm{" +
                "title='" + title + '\'' +
                ", descriptionHtml='" + descriptionHtml + '\'' +
                '}';
    }

    public static class BreakApiParser extends BaseApiParser<BreakApiModel> {

        @Override
        public Map<String, BreakApiModel> fromJson(String json) {
            Type type = new TypeToken<Map<String, BreakApiModel>>() {
            }.getType();
            return parseJson(json, type);
        }
    }
}
