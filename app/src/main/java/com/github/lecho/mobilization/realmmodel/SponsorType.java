package com.github.lecho.mobilization.realmmodel;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.github.lecho.mobilization.R;

/**
 * Created by Leszek on 2015-10-02.
 */
public enum SponsorType {

    DIAMOND(R.string.sponsor_type_diamond, R.color.diamond, 1),
    PLATINUM(R.string.sponsor_type_platinum, R.color.platinum, 2),
    GOLD(R.string.sponsor_type_gold, R.color.gold, 3),
    SILVER(R.string.sponsor_type_silver, R.color.silver, 4),
    COPPER(R.string.sponsor_type_copper, R.color.copper, 5),
    OTHER(R.string.sponsor_type_other, R.color.white, 6);

    private final int textRes;
    private final int colorRes;
    private final int priority;

    SponsorType(@StringRes int textRes, @ColorRes int colorRes, int priority) {
        this.textRes = textRes;
        this.colorRes = colorRes;
        this.priority = priority;
    }

    @StringRes
    public int getTextRes() {
        return textRes;
    }

    @ColorRes
    public int getColorRes() {
        return colorRes;
    }

    public int getPriority() {
        return priority;
    }
}
