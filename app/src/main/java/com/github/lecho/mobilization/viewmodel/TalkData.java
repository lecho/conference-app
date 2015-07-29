package com.github.lecho.mobilization.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leszek on 2015-07-29.
 */
public class TalkData {

    public String key;
    public String title;
    public String description;
    public String language;
    public VenueData venue;
    public List<SpeakerData> speakers = new ArrayList<>();
}
