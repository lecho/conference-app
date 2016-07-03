package com.github.lecho.mobilization.apimodel;


import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SlotApiModel {

    public String from;
    public String to;

    @Override
    public String toString() {
        return "SlotRealm{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    public static class SlotApiParser extends BaseApiParser<SlotApiModel> {

        @Override
        public Map<String, SlotApiModel> fromJson(String json) {
            Type type = new TypeToken<Map<String, SlotApiModel>>() {
            }.getType();
            return parseJson(json, type);
        }
    }
}
