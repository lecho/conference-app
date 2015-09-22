package com.github.lecho.conference.ui;

import android.content.Context;
import android.os.AsyncTask;

import com.github.lecho.conference.realmmodel.RealmFacade;

/**
 * Created by Leszek on 2015-09-22.
 */
class AddTalkInMyAgendaTask extends AsyncTask<Void, Void, Void> {

    private final Context context;
    private final String talkKey;
    private final boolean isInMyAgenda;

    private AddTalkInMyAgendaTask(Context context, String talkKey, boolean isInMyAgenda) {
        this.context = context;
        this.talkKey = talkKey;
        this.isInMyAgenda = isInMyAgenda;
    }

    public static AddTalkInMyAgendaTask getAddTask(Context context, String talkKey) {
        return new AddTalkInMyAgendaTask(context, talkKey, false);
    }

    public static AddTalkInMyAgendaTask getRemoveTask(Context context, String talkKey) {
        return new AddTalkInMyAgendaTask(context, talkKey, true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        RealmFacade realmFacade = new RealmFacade(context);
        if (isInMyAgenda) {
            realmFacade.removeTalkFromMyAgenda(talkKey);
        } else {
            realmFacade.addTalkToMyAgenda(talkKey);
        }
        return null;
    }
}
