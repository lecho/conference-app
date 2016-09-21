package com.github.lecho.mobilization.async;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by Leszek on 2015-09-01.
 */
public class JsonDownloadService extends IntentService {

    private static final String TAG = JsonDownloadService.class.getSimpleName();
    private static final String ASSETS = "assets";
    private static final String JSON = "json";
    private static final String SCHEDULE_JSON_FILE = "schedule.json";
    private static final String EVENT_JSON_FILE = "event.json";
    private static final String BREAKS_JSON_FILE = "breaks.json";
    private static final String SLOTS_JSON_FILE = "slots.json";
    private static final String SPEAKERS_JSON_FILE = "speakers.json";
    private static final String SPONSORS_JSON_FILE = "sponsors.json";
    private static final String TALKS_JSON_FILE = "talks.json";
    private static final String VENUES_JSON_FILE = "venues.json";

    public JsonDownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO: 21.09.2016 validate dirs creation
        File assets = new File(getFilesDir(), ASSETS);
        assets.mkdir();
        File json = new File(assets, JSON);
        json.mkdir();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference jsonRef = firebaseStorage.getReference().child(ASSETS).child(JSON);

        File schedule = new File(json, SCHEDULE_JSON_FILE);
        StorageReference scheduleRef = jsonRef.child(SCHEDULE_JSON_FILE);
        FileDownloadTask scheduleTask = scheduleRef.getFile(schedule);

        File event = new File(json, EVENT_JSON_FILE);
        StorageReference eventRef = jsonRef.child(EVENT_JSON_FILE);
        FileDownloadTask eventTask = eventRef.getFile(event);

        File breaks = new File(json, BREAKS_JSON_FILE);
        StorageReference breaksRef = jsonRef.child(BREAKS_JSON_FILE);
        FileDownloadTask breaksTask = breaksRef.getFile(breaks);

        File slots = new File(json, SLOTS_JSON_FILE);
        StorageReference slotsRef = jsonRef.child(SLOTS_JSON_FILE);
        FileDownloadTask slotsTask = slotsRef.getFile(slots);

        File speakers = new File(json, SPEAKERS_JSON_FILE);
        StorageReference speakersRef = jsonRef.child(SPEAKERS_JSON_FILE);
        FileDownloadTask speakersTask = speakersRef.getFile(speakers);

        File sponsors = new File(json, SPONSORS_JSON_FILE);
        StorageReference sponsorsRef = jsonRef.child(SPONSORS_JSON_FILE);
        FileDownloadTask sponsorsTask = sponsorsRef.getFile(sponsors);

        File talks = new File(json, TALKS_JSON_FILE);
        StorageReference talksRef = jsonRef.child(TALKS_JSON_FILE);
        FileDownloadTask talksTask = talksRef.getFile(talks);

        File venues = new File(json, VENUES_JSON_FILE);
        StorageReference venuesRef = jsonRef.child(VENUES_JSON_FILE);
        FileDownloadTask venuesTask = venuesRef.getFile(venues);

        try {
            // block until all tasks are completed
            FileDownloadTask.TaskSnapshot scheduleTaskResult = Tasks.await(scheduleTask);
            FileDownloadTask.TaskSnapshot eventTaskResult = Tasks.await(eventTask);
            FileDownloadTask.TaskSnapshot breaksTaskResult = Tasks.await(breaksTask);
            FileDownloadTask.TaskSnapshot slotsTaskResult = Tasks.await(slotsTask);
            FileDownloadTask.TaskSnapshot speakersTaskResult = Tasks.await(speakersTask);
            FileDownloadTask.TaskSnapshot sponsorsTaskResult = Tasks.await(sponsorsTask);
            FileDownloadTask.TaskSnapshot talksTaskResult = Tasks.await(talksTask);
            FileDownloadTask.TaskSnapshot venuesTaskResult = Tasks.await(venuesTask);
        } catch (ExecutionException e) {
            Log.e(TAG, "onHandleIntent ", e);
        } catch (InterruptedException e) {
            Log.e(TAG, "onHandleIntent: ", e);
        }
    }
}
