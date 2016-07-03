package com.github.lecho.mobilization.apimodel;

import java.util.Map;

/**
 * Created by Leszek on 2015-08-05.
 */
public class ApiData {

    public Map<String, EventApiDto> eventsMap;
    public Map<String, AgendaItemApiModel> agendaMap;
    public Map<String, BreakApiModel> breaksMap;
    public Map<String, SlotApiDto> slotsMap;
    public Map<String, SpeakerApiDto> speakersMap;
    public Map<String, SponsorApiDto> sponsorsMap;
    public Map<String, TalkApiDto> talksMap;
    public Map<String, VenueApiDto> venuesMap;
}
