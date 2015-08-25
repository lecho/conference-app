package com.github.lecho.mobilization.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leszek on 2015-07-29.
 */
public class TalkViewDto {

    public String key;
    public String title;
    public String description;
    public String language;
    public SlotViewDto slot;
    public VenueViewDto venue;
    public List<SpeakerViewDto> speakers = new ArrayList<>();

    @Override
    public String toString() {
        return "TalkViewDto{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", slot=" + slot +
                ", venue=" + venue +
                ", speakers=" + speakers +
                '}';
    }
}
