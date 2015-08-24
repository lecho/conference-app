package com.github.lecho.mobilization.viewmodel;

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

}
