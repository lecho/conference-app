package com.github.lecho.mobilization.viewmodel;

/**
 * Created by Leszek on 2015-07-29.
 */
public class BreakViewModel {

    public String key;
    public String title;
    public String description;
    public SlotViewModel slot;

    @Override
    public String toString() {
        return "BreakViewModel{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", slot=" + slot +
                '}';
    }
}
