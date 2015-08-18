package com.github.lecho.mobilization.apimodel;

import java.util.Map;

/**
 * Created by Leszek on 2015-08-05.
 */
public class ApiData {

    public Map<String, AgendaItemApiDto> agendaItemApiDtoMap;
    public Map<String, BreakApiDto> breakApiDtoMap;
    public Map<String, SlotApiDto> slotApiDtoMap;
    public Map<String, SpeakerApiDto> speakerApiDtoMap;
    public Map<String, SponsorApiDto> sponsors;
    public Map<String, TalkApiDto> talkApiDtoMap;
    public Map<String, VenueApiDto> venueApiDtoMap;
}
