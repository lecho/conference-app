package com.github.lecho.mobilization.realmmodel;

import com.github.lecho.mobilization.viewmodel.AgendaViewDto;
import com.github.lecho.mobilization.viewmodel.SpeakerViewDto;
import com.github.lecho.mobilization.viewmodel.TalkViewDto;
import com.github.lecho.mobilization.viewmodel.VenueViewDto;

import java.util.List;

/**
 * Created by Leszek on 2015-07-29.
 */
public interface DataFacade {

    AgendaViewDto loadMyAgenda();

    AgendaViewDto loadAgendaForVenue();

    AgendaViewDto loadWholeAgenda();

    List<TalkViewDto> loadAllTalks();

    List<TalkViewDto> loadTalksForVenue(String venueKey);

    TalkViewDto loadTalk(String talkKey);

    SpeakerViewDto loadSpeaker(String speakerKey);

    VenueViewDto loadVenue(String venueKey);


}
