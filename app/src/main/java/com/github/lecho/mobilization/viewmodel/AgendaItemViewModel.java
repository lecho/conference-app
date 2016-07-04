package com.github.lecho.mobilization.viewmodel;

/**
 * Created by Leszek on 2015-07-29.
 */
public class AgendaItemViewModel {

    public AgendaItemType type;
    public BreakViewModel agendaBreak;
    public TalkViewModel talk;
    public SlotViewModel slot;

    public enum AgendaItemType {
        BREAK, TALK, SLOT,
    }

    @Override
    public String toString() {
        return "AgendaItemViewModel{" +
                "type=" + type +
                ", agendaBreak=" + agendaBreak +
                ", talk=" + talk +
                ", slot=" + slot +
                '}';
    }
}
