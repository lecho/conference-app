package com.github.lecho.conference.ui;

import android.content.Context;
import android.os.AsyncTask;

import com.github.lecho.conference.loader.ContentChangeObserver;
import com.github.lecho.conference.realmmodel.RealmFacade;

/**
 * Created by Leszek on 2015-09-22.
 */
public abstract class TalkFavoriteTask extends AsyncTask<Void, Void, Void> {

    protected final Context context;
    protected final String talkKey;
    protected final boolean shouldEmitBroadcast;

    private TalkFavoriteTask(Context context, String talkKey, boolean shouldEmitBroadcast) {
        this.context = context;
        this.talkKey = talkKey;
        this.shouldEmitBroadcast = shouldEmitBroadcast;
    }

    public static void addToMyAgenda(Context context, String talkKey, boolean shouldEmitBroadcast) {
        new AddTalkToMyAgendaTask(context, talkKey, shouldEmitBroadcast).execute();
    }

    public static void removeFromMyAgenda(Context context, String talkKey, boolean shouldEmitBroadcast) {
        new RemoveTalkToMyAgendaTask(context, talkKey, shouldEmitBroadcast).execute();
    }

    private static class AddTalkToMyAgendaTask extends TalkFavoriteTask {

        private AddTalkToMyAgendaTask(Context context, String talkKey, boolean shouldEmitBroadcast) {
            super(context, talkKey, shouldEmitBroadcast);
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade(context);
            realmFacade.addTalkToMyAgenda(talkKey, shouldEmitBroadcast);
            return null;
        }
    }

    private static class RemoveTalkToMyAgendaTask extends TalkFavoriteTask {

        private RemoveTalkToMyAgendaTask(Context context, String talkKey, boolean shouldEmitBroadcast) {
            super(context, talkKey, shouldEmitBroadcast);
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade(context);
            realmFacade.removeTalkFromMyAgenda(talkKey, shouldEmitBroadcast);
            return null;
        }
    }
}
