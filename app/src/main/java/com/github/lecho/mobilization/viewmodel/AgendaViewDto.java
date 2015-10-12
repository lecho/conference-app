package com.github.lecho.mobilization.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leszek on 2015-07-29.
 */
public class AgendaViewDto {

    public List<AgendaItemViewDto> agendaItems = new ArrayList<>();

    @Override
    public String toString() {
        return "AgendaViewDto{" +
                "agendaItems=" + agendaItems +
                '}';
    }
}
