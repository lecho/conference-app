package com.github.lecho.mobilization.realmmodel;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.github.lecho.mobilization.R;

/**
 * Created by Leszek on 2015-10-02.
 */
public enum SponsorType {

    DIAMOND(R.string.sponsor_type_diamond, R.drawable.shape_sponsor_diamond, 1),
    PLATINUM(R.string.sponsor_type_platinum, R.drawable.shape_sponsor_platinum, 2),
    GOLD(R.string.sponsor_type_gold, R.drawable.shape_sponsor_gold, 3),
    SILVER(R.string.sponsor_type_silver, R.drawable.shape_sponsor_silver, 4),
    COPPER(R.string.sponsor_type_copper, R.drawable.shape_sponsor_copper, 5),
    OTHER(R.string.sponsor_type_other, R.drawable.shape_sponsor_other, 6);

    private final int textRes;
    private final int drawable;
    private final int priority;

    SponsorType(@StringRes int textRes, @DrawableRes int drawable, int priority) {
        this.textRes = textRes;
        this.drawable = drawable;
        this.priority = priority;
    }

    @StringRes
    public int getTextRes() {
        return textRes;
    }

    @DrawableRes
    public int getDrawable() {
        return drawable;
    }

    public int getPriority() {
        return priority;
    }
}
