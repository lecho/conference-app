package com.github.lecho.mobilization.async;

import android.os.AsyncTask;

import com.github.lecho.mobilization.realmmodel.RealmFacade;

/**
 * Created by Leszek on 2015-10-05.
 */
public class TalkAsyncHelper {

    public static void addTalk(String talkKey) {
        new AddTalkTask(talkKey).execute();
    }

    public static void removeTalk(String talkKey) {
        new RemoveTalkTask(talkKey).execute();
    }

    public static void removeTalkSilent(String talkKey) {
        new RemoveTalkTaskSilent(talkKey).execute();
    }

    public static void replaceTalk(String oldTalkKey, String newTalkKey) {
        new ReplaceTalkTask(oldTalkKey, newTalkKey).execute();
    }

    private static class AddTalkTask extends AsyncTask<Void, Void, Void> {

        private final String talkKey;

        private AddTalkTask(String talkKey) {
            this.talkKey = talkKey;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade();
            realmFacade.addTalkToMyAgenda(talkKey);
            return null;
        }
    }

    private static class RemoveTalkTask extends AsyncTask<Void, Void, Void> {

        private final String talkKey;

        private RemoveTalkTask(String talkKey) {
            this.talkKey = talkKey;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade();
            realmFacade.removeTalkFromMyAgenda(talkKey);
            return null;
        }
    }

    private static class RemoveTalkTaskSilent extends AsyncTask<Void, Void, Void> {

        private final String talkKey;

        private RemoveTalkTaskSilent(String talkKey) {
            this.talkKey = talkKey;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade();
            realmFacade.removeTalkFromMyAgendaSilent(talkKey);
            return null;
        }
    }

    private static class ReplaceTalkTask extends AsyncTask<Void, Void, Void> {

        private final String oldTalkKey;
        private final String newTalkKey;

        private ReplaceTalkTask(String oldTalkKey, String newTalkKey) {
            this.oldTalkKey = oldTalkKey;
            this.newTalkKey = newTalkKey;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade();
            realmFacade.replaceTalk(oldTalkKey, newTalkKey);
            return null;
        }
    }
}
