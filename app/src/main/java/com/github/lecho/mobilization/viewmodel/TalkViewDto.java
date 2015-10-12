package com.github.lecho.mobilization.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.lecho.mobilization.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leszek on 2015-07-29.
 */
public class TalkViewDto {

    private static final String POLISH = "PL";
    private static final String ENGLISH = "EN";
    public String key;
    public String title;
    public String description;
    public String language;
    public boolean isInMyAgenda;
    public SlotViewDto slot;
    public VenueViewDto venue;
    public List<SpeakerViewDto> speakers = new ArrayList<>();

    @NonNull
    public String getLanguageLong(Context context) {
        if (language.equalsIgnoreCase(POLISH)) {
            return context.getString(R.string.talk_language_pl);
        } else if (language.equalsIgnoreCase(ENGLISH)) {
            return context.getString(R.string.talk_language_en);
        } else {
            return language;
        }
    }

    @Override
    public String toString() {
        return "TalkViewDto{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", isInMyAgenda=" + isInMyAgenda +
                ", slot=" + slot +
                ", venue=" + venue +
                ", speakers=" + speakers +
                '}';
    }
}
