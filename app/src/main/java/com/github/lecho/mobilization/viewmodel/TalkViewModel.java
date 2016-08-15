package com.github.lecho.mobilization.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.lecho.mobilization.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leszek on 2015-07-29.
 */
public class TalkViewModel {

    private static final String POLISH = "PL";
    private static final String ENGLISH = "EN";
    public String key;
    public String title;
    public String description;
    public String language;
    public boolean isInMyAgenda;
    public SlotViewModel slot;
    public VenueViewModel venue;
    public List<SpeakerViewModel> speakers = new ArrayList<>();

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

    @NonNull
    public String getLanguageInBrackets(){
        return "(" + language + ")";
    }

    @NonNull
    public String getSpeakersText(Context context) {
        String prefix = context.getString(R.string.speakers_prefix);
        StringBuilder speakersText = new StringBuilder(prefix);
        int index = 0;
        for (SpeakerViewModel speakerViewModel : speakers) {
            if (index > 0) {
                speakersText.append(", ");
            }
            ++index;
            speakersText.append(speakerViewModel.firstName).append(" ").append(speakerViewModel.lastName);
        }
        return speakersText.toString();
    }

    @Override
    public String toString() {
        return "TalkViewModel{" +
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
