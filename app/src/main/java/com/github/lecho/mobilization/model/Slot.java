package com.github.lecho.mobilization.model;


import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class Slot {

    public static final String TAG = Slot.class.getSimpleName();
    public String key;
    public String from;
    public String to;
    public long fromInMilliseconds;
    public long toInMilliseconds;
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public static List<Slot> fromJson(String json) {
        try {
            Type genericType = new TypeToken<Map<String, Slot>>() {
            }.getType();

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            Map<String, Slot> slotsMap = gson.fromJson(json, genericType);
            for (Map.Entry<String, Slot> entry : slotsMap.entrySet()) {
                Slot slot = entry.getValue();
                slot.key = entry.getKey();
                slot.fromInMilliseconds = dateFormat.parse(slot.from).getTime();
                slot.toInMilliseconds = dateFormat.parse(slot.to).getTime();
            }

            List<Slot> slots = new ArrayList<>(slotsMap.size());
            slots.addAll(slotsMap.values());
            return slots;
        } catch (ParseException e) {
            Log.e(TAG, "Could not parse Slots json", e);
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "Slot{" +
                "key='" + key + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", fromInMilliseconds=" + fromInMilliseconds +
                ", toInMilliseconds=" + toInMilliseconds +
                '}';
    }
}
