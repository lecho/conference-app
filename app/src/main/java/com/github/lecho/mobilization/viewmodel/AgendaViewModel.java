package com.github.lecho.mobilization.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leszek on 2015-07-29.
 */
public class AgendaViewModel {

    public List<AgendaItemViewModel> agendaItems = new ArrayList<>();

    @Override
    public String toString() {
        return "AgendaViewModel{" +
                "agendaItems=" + agendaItems +
                '}';
    }
}
