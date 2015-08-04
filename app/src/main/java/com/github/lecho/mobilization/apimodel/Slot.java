package com.github.lecho.mobilization.apimodel;


/**
 * Created by Leszek on 2015-07-24.
 */
public class Slot extends BaseApiModel {

    public String from;
    public String to;

    @Override
    public String toString() {
        return "Slot{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
