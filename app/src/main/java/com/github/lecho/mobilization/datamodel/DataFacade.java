package com.github.lecho.mobilization.datamodel;

import com.github.lecho.mobilization.viewmodel.AgendaData;
import com.github.lecho.mobilization.viewmodel.SpeakerData;
import com.github.lecho.mobilization.viewmodel.TalkData;
import com.github.lecho.mobilization.viewmodel.VenueData;

import java.util.List;

/**
 * Created by Leszek on 2015-07-29.
 */
public interface DataFacade {

    AgendaData loadMyAgenda();

    AgendaData loadAgendaForVenue();

    AgendaData loadWholeAgenda();

    List<TalkData> loadAllTalks();

    List<TalkData> loadTalksForVenue(String venueKey);

    TalkData loadTalk(String talkKey);

    SpeakerData loadSpeaker(String speakerKey);

    VenueData loadVenue(String venueKey);
}
