package com.github.lecho.mobilization.viewmodel;

/**
 * Created by Leszek on 2015-07-29.
 */
public class AgendaItemViewDto {

    public AgendaItemType type;
    public BreakViewDto agendaBreak;
    public TalkViewDto talk;
    public SlotViewDto slot;

    public enum AgendaItemType {
        BREAK, TALK, SLOT,
    }

    @Override
    public String toString() {
        return "AgendaItemViewDto{" +
                "type=" + type +
                ", agendaBreak=" + agendaBreak +
                ", talk=" + talk +
                ", slot=" + slot +
                '}';
    }
}
