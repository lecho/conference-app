package com.github.lecho.mobilization.realmmodel;


import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.realm.RealmObject;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SlotRealm extends RealmObject {

    private static final String TAG = SlotRealm.class.getSimpleName();
    private String key;
    private String from;
    private String to;
    private long fromInMilliseconds;
    private long toInMilliseconds;
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public static void timeToMilliseconds(SlotRealm slotRealm) {
        try {
            slotRealm.setFromInMilliseconds(dateFormat.parse(slotRealm.getFrom()).getTime());
            slotRealm.setToInMilliseconds(dateFormat.parse(slotRealm.getTo()).getTime());
        } catch (ParseException e) {
            Log.e(TAG, "Could not parse Slots From and To values", e);
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getFromInMilliseconds() {
        return fromInMilliseconds;
    }

    public void setFromInMilliseconds(long fromInMilliseconds) {
        this.fromInMilliseconds = fromInMilliseconds;
    }

    public long getToInMilliseconds() {
        return toInMilliseconds;
    }

    public void setToInMilliseconds(long toInMilliseconds) {
        this.toInMilliseconds = toInMilliseconds;
    }
}
