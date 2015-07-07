package com.github.lecho.mobilization;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VenueAgendaFragment extends Fragment {

    public static final String TAG = "VenueAgendaFragment";
    public static final String[] DATASET = new String[]{"ALA", "OLA", "ELA", "EWA", "JULA"};
    private static final String ARG_AGENDA_TYPE = "arg-agenda-type";

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    public static VenueAgendaFragment newInstance(AgendaType agendaType) {
        VenueAgendaFragment fragment = new VenueAgendaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_AGENDA_TYPE, agendaType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new VenueAgendaAdapter(DATASET));
        recyclerView.addItemDecoration(new AgendaItemDecorator(getActivity()));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public enum AgendaType {
        VENUE_SPECIFIC, VENUE_GENERIC
    }
}
