package com.github.lecho.conference.viewmodel;

/**
 * Created by Leszek on 2015-07-29.
 */
public class AgendaItemViewDto {

    public AgendaItemType type;
    public BreakViewDto agendaBreak;
    public TalkViewDto talk;

    public enum AgendaItemType {
        BREAK, TALK
    }

    @Override
    public String toString() {
        return "AgendaItemViewDto{" +
                "type=" + type +
                ", agendaBreak=" + agendaBreak +
                ", talk=" + talk +
                '}';
    }
}
