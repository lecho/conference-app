package com.github.lecho.conference.viewmodel;

/**
 * Created by Leszek on 2015-07-29.
 */
public class SlotViewDto {

    public String key;
    public String from;
    public String to;
    public long fromInMilliseconds;
    public long toInMilliseconds;

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
}
