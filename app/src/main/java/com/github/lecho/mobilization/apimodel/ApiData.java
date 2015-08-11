package com.github.lecho.mobilization.apimodel;

import java.util.Map;

/**
 * Created by Leszek on 2015-08-05.
 */
public class ApiData {

    public Map<String, AgendaItem> agendaItems;
    public Map<String, Break> breaks;
    public Map<String, Slot> slots;
    public Map<String, Speaker> speakers;
    public Map<String, Sponsor> sponsors;
    public Map<String, Talk> talks;
    public Map<String, Venue> venues;
}
