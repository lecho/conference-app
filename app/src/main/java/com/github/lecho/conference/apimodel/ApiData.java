package com.github.lecho.conference.apimodel;

import com.github.lecho.conference.viewmodel.EventViewDto;

import java.util.Map;

/**
 * Created by Leszek on 2015-08-05.
 */
public class ApiData {

    public Map<String, EventApiDto> eventsMap;
    public Map<String, AgendaItemApiDto> agendaMap;
    public Map<String, BreakApiDto> breaksMap;
    public Map<String, SlotApiDto> slotsMap;
    public Map<String, SpeakerApiDto> speakersMap;
    public Map<String, SponsorApiDto> sponsors;
    public Map<String, TalkApiDto> talksMap;
    public Map<String, VenueApiDto> venuesMap;
}
