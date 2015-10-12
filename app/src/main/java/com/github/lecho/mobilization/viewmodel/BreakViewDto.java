package com.github.lecho.mobilization.viewmodel;

/**
 * Created by Leszek on 2015-07-29.
 */
public class BreakViewDto {

    public String key;
    public String title;
    public String description;
    public SlotViewDto slot;

    @Override
    public String toString() {
        return "BreakViewDto{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", slot=" + slot +
                '}';
    }
}
