package com.github.lecho.mobilization.apimodel;


import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SlotApiDto {

    public String from;
    public String to;

    @Override
    public String toString() {
        return "SlotRealm{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    public static class SlotApiParser extends BaseApiParser<SlotApiDto> {

        @Override
        public Map<String, SlotApiDto> fromJson(String json) {
            Type type = new TypeToken<Map<String, SlotApiDto>>() {
            }.getType();
            return parseJson(json, type);
        }
    }
}
