package com.github.lecho.mobilization.async;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        Log.d(TAG, "Downloading json data");
        //// TODO: 25.09.2016 handle exceptions etc
        File assets = new File(getFilesDir(), ASSETS);
        if (!assets.exists()) {
            if (!assets.mkdir()) {
                Log.w(TAG, "Could not create ASSETS folder");
                return;
            }
        }
        File json = new File(assets, JSON);
        if (!json.exists()) {
            if (!json.mkdir()) {
                Log.w(TAG, "Could not create JSON folder");
                return;
            }
        }

        List<FileDownloadTask> tasks = new ArrayList<>();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference jsonRef = firebaseStorage.getReference().child(ASSETS).child(JSON);

        tasks.add(startDownloadTask(jsonRef, json, SCHEDULE_JSON_FILE));
        tasks.add(startDownloadTask(jsonRef, json, EVENT_JSON_FILE));
        tasks.add(startDownloadTask(jsonRef, json, BREAKS_JSON_FILE));
        tasks.add(startDownloadTask(jsonRef, json, SLOTS_JSON_FILE));
        tasks.add(startDownloadTask(jsonRef, json, SPEAKERS_JSON_FILE));
        tasks.add(startDownloadTask(jsonRef, json, SPONSORS_JSON_FILE));
        tasks.add(startDownloadTask(jsonRef, json, TALKS_JSON_FILE));
        tasks.add(startDownloadTask(jsonRef, json, VENUES_JSON_FILE));

        try {
            // TODO: 01.10.2016 show notification with progressbar
            // block until all tasks are completed
            Task task = Tasks.whenAll(tasks);
            Tasks.await(task);
            Log.d(TAG, "Downloaded json data - success!");
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "Could not download json data: ", e);
        }
    }

    private FileDownloadTask startDownloadTask(@NonNull StorageReference remoteDir, @NonNull File localDir,
                                               @NonNull String fileName) {
        File schedule = new File(localDir, fileName);
        StorageReference scheduleRef = remoteDir.child(fileName);
        return scheduleRef.getFile(schedule);
    }
}
