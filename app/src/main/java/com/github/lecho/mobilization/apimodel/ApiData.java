package com.github.lecho.mobilization.apimodel;

import java.util.Map;

/**
 * Created by Leszek on 2015-08-05.
 */
public class ApiData {

    public Map<String, AgendaItem> agendaItems;
    public Map<String, BreakApiDto> breaks;
    public Map<String, SlotApiDto> slots;
    public Map<String, SpeakerApiDto> speakers;
    public Map<String, SponsorApiDto> sponsors;
    public Map<String, TalkApiDto> talks;
    public Map<String, VenueApiDto> venues;
}
