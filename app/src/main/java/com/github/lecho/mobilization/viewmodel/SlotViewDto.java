package com.github.lecho.mobilization.viewmodel;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Leszek on 2015-07-29.
 */
public class SlotViewDto {

    public String key;
    public String from;
    public String to;
    public long fromInMilliseconds;
    public long toInMilliseconds;
    public boolean isEmpty;

    @Override
    public String toString() {
        return "SlotViewDto{" +
                "key='" + key + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", fromInMilliseconds=" + fromInMilliseconds +
                ", toInMilliseconds=" + toInMilliseconds +
                '}';
    }

    public static class SlotInTimeZone {

        public final int fromHour;
        public final int fromMinute;
        public final int toHour;
        public final int toMinute;
        public final int currentHour;
        public final int currentMinute;

        private SlotInTimeZone(int fromHour, int fromMinute, int toHour, int toMinute, int currentHour, int
                currentMinute) {
            this.fromHour = fromHour;
            this.fromMinute = fromMinute;
            this.toHour = toHour;
            this.toMinute = toMinute;
            this.currentHour = currentHour;
            this.currentMinute = currentMinute;
        }

        public static SlotInTimeZone getSlotInTimezone(SlotViewDto slotViewDto) {
            Calendar calendar = Calendar.getInstance();
            TimeZone defaultTZ = TimeZone.getDefault();
            if (!defaultTZ.getID().equals(calendar.getTimeZone().getID())) {
                calendar.setTimeZone(defaultTZ);
            }
            calendar.setTimeZone(TimeZone.getDefault());
            calendar.setTimeInMillis(slotViewDto.fromInMilliseconds);
            final int fromHour = calendar.get(Calendar.HOUR_OF_DAY);
            final int fromMinute = calendar.get(Calendar.MINUTE);
            calendar.setTimeInMillis(slotViewDto.toInMilliseconds);
            final int toHour = calendar.get(Calendar.HOUR_OF_DAY);
            final int toMinute = calendar.get(Calendar.MINUTE);
            calendar.setTime(new Date());
            final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            final int currentMinute = calendar.get(Calendar.MINUTE);

            return new SlotInTimeZone(fromHour, fromMinute, toHour, toMinute, currentHour, currentMinute);
        }

        @NonNull
        public String getTimeSlotText() {
            StringBuilder slotTextBuilder = new StringBuilder();
            slotTextBuilder = appendValueInXXFormat(slotTextBuilder, fromHour);
            slotTextBuilder.append(":");
            slotTextBuilder = appendValueInXXFormat(slotTextBuilder, fromMinute);
            slotTextBuilder.append("-");
            slotTextBuilder = appendValueInXXFormat(slotTextBuilder, toHour);
            slotTextBuilder.append(":");
            slotTextBuilder = appendValueInXXFormat(slotTextBuilder, toMinute);
            return slotTextBuilder.toString();
        }

        @NonNull
        private static StringBuilder appendValueInXXFormat(StringBuilder stringBuilder, int value) {
            if (value < 10) {
                stringBuilder.append("0").append(value);
            } else {
                stringBuilder.append(value);
            }
            return stringBuilder;
        }

        public boolean isInCurrentSlot() {
            final int fromTime = 60 * fromHour + fromMinute;
            final int toTime = 60 * toHour + toMinute;
            final int currentTime = 60 * currentHour + currentMinute;
            return currentTime >= fromTime && currentTime < toTime;
        }
    }
}
