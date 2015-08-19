package com.github.lecho.mobilization.realmmodel;


import android.util.Log;

import com.github.lecho.mobilization.apimodel.SlotApiDto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SlotRealm extends RealmObject {

    @PrimaryKey
    private String key;
    private String from;
    private String to;
    private long fromInMilliseconds;
    private long toInMilliseconds;

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

    public static class SlotConverter extends RealmFacade.RealmConverter<SlotRealm, SlotApiDto> {
        private static final String TAG = SlotConverter.class.getSimpleName();
        private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        @Override
        public SlotRealm convert(String key, SlotApiDto apiDto) {
            SlotRealm slotRealm = new SlotRealm();
            slotRealm.setKey(key);
            slotRealm.setFrom(apiDto.from);
            slotRealm.setTo(apiDto.to);
            timeToMilliseconds(slotRealm);
            return slotRealm;
        }

        public void timeToMilliseconds(SlotRealm slotRealm) {
            try {
                slotRealm.setFromInMilliseconds(dateFormat.parse(slotRealm.getFrom()).getTime());
                slotRealm.setToInMilliseconds(dateFormat.parse(slotRealm.getTo()).getTime());
            } catch (ParseException e) {
                Log.e(TAG, "Could not parse Slots From and To values", e);
            }
        }
    }
}
