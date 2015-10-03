package com.github.lecho.conference.realmmodel;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.github.lecho.conference.R;

/**
 * Created by Leszek on 2015-10-02.
 */
public enum SponsorType {

    DIAMOND(R.string.sponsor_type_diamond, R.color.diamond),
    PLATINUM(R.string.sponsor_type_platinum, R.color.platinum),
    GOLD(R.string.sponsor_type_gold, R.color.gold),
    SILVER(R.string.sponsor_type_silver, R.color.silver),
    COPPER(R.string.sponsor_type_copper, R.color.copper),
    OTHER(R.string.sponsor_type_other, R.color.white);

    private final int textRes;
    private final int colorRes;

    SponsorType(@StringRes int textRes, @ColorRes int colorRes) {
        this.textRes = textRes;
        this.colorRes = colorRes;
    }

    @StringRes
    public int getTextRes() {
        return textRes;
    }

    @ColorRes
    public int getColorRes() {
        return colorRes;
    }
}
