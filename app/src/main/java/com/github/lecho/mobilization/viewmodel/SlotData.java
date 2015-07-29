package com.github.lecho.mobilization.viewmodel;

/**
 * Created by Leszek on 2015-07-29.
 */
public class SlotData {

    public String key;
    public String from;
    public String to;
    public long fromInMilliseconds;
    public long toInMilliseconds;
    public SlotType slotType;
    public TalkData talkData;
    public BreakData breakData;

    public enum SlotType {
        TALK, BREAK
    }
}
