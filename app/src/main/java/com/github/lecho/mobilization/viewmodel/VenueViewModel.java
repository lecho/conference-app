package com.github.lecho.mobilization.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.lecho.mobilization.R;

/**
 * Created by Leszek on 2015-07-29.
 */
public class VenueViewModel {

    public String key;
    public String title;

    @NonNull
    public String getVenueText(Context context) {
        return new StringBuilder(title).append(" ").append(context.getString(R.string.title_activity_track)).toString();
    }

    @Override
    public String toString() {
        return "VenueViewModel{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
