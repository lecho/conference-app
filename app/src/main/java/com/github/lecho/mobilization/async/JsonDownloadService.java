package com.github.lecho.mobilization.async;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.util.AnalyticsReporter;
import com.github.lecho.mobilization.util.FileUtils;
import com.github.lecho.mobilization.util.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Created by Leszek on 2015-09-01.
 */
public class JsonDownloadService extends IntentService {

    private static final String TAG = JsonDownloadService.class.getSimpleName();
    private static final int NOTIFICATION_ID = R.drawable.ic_download;
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
    private static final String FIREBASE_STORAGE_URL = "https://firebasestorage.googleapis.com";

    private JsonStorage jsonStorage;
    private Map<String, Uri> urlsMap = new HashMap<>();

    public static void startDownload(Context context) {
        Intent serviceIntent = new Intent(context, JsonDownloadService.class);
        context.startService(serviceIntent);
    }

    public JsonDownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Downloading json data");
        startForeground();

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        Retrofit retrofit = new Retrofit.Builder().baseUrl(FIREBASE_STORAGE_URL).build();
        jsonStorage = retrofit.create(JsonStorage.class);

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

        try {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference jsonRef = firebaseStorage.getReference().child(ASSETS).child(JSON);

            List<Task<Uri>> tasks = new ArrayList<>();
            tasks.add(getDownloadUrl(jsonRef, SCHEDULE_JSON_FILE));
            tasks.add(getDownloadUrl(jsonRef, EVENT_JSON_FILE));
            tasks.add(getDownloadUrl(jsonRef, BREAKS_JSON_FILE));
            tasks.add(getDownloadUrl(jsonRef, SLOTS_JSON_FILE));
            tasks.add(getDownloadUrl(jsonRef, SPEAKERS_JSON_FILE));
            tasks.add(getDownloadUrl(jsonRef, SPONSORS_JSON_FILE));
            tasks.add(getDownloadUrl(jsonRef, TALKS_JSON_FILE));
            tasks.add(getDownloadUrl(jsonRef, VENUES_JSON_FILE));

            //block until all tasks are completed
            Task task = Tasks.whenAll(tasks);
            Tasks.await(task);

            // TODO: 04.10.2016 download asyncronously, try to use storageRef.getStream if getFile doesn't work
            //download synchronously
            boolean result = downloadFile(urlsMap.get(SCHEDULE_JSON_FILE), new File(json, SCHEDULE_JSON_FILE));
            result &= downloadFile(urlsMap.get(EVENT_JSON_FILE), new File(json, EVENT_JSON_FILE));
            result &= downloadFile(urlsMap.get(BREAKS_JSON_FILE), new File(json, BREAKS_JSON_FILE));
            result &= downloadFile(urlsMap.get(SLOTS_JSON_FILE), new File(json, SLOTS_JSON_FILE));
            result &= downloadFile(urlsMap.get(SPEAKERS_JSON_FILE), new File(json, SPEAKERS_JSON_FILE));
            result &= downloadFile(urlsMap.get(SPONSORS_JSON_FILE), new File(json, SPONSORS_JSON_FILE));
            result &= downloadFile(urlsMap.get(TALKS_JSON_FILE), new File(json, TALKS_JSON_FILE));
            result &= downloadFile(urlsMap.get(VENUES_JSON_FILE), new File(json, VENUES_JSON_FILE));

            if (result) {
                Log.d(TAG, "Downloaded json data - success!");
                AnalyticsReporter.logJsonDownloaded(firebaseAnalytics, "");
                DatabaseUpdateService.updateFromInternalMemory(getApplicationContext());
                Utils.saveCurrentJsonDataVersion(getApplicationContext(), Utils.getNextJsonDataVersion
                        (getApplicationContext()));
            } else {
                Log.e(TAG, "Could not download json data");
                AnalyticsReporter.logJsonDownloadFailed(firebaseAnalytics, "");
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not download json data: ", e);
            AnalyticsReporter.logJsonDownloadFailed(firebaseAnalytics, "");
        }
    }

    @NonNull
    private Task<Uri> getDownloadUrl(@NonNull StorageReference remoteDir, @NonNull String fileName) {
        StorageReference scheduleRef = remoteDir.child(fileName);
        return scheduleRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "Retrieved file url for file " + fileName + ": " + uri);
                        urlsMap.put(fileName, uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Could not retrieve uri for file " + fileName, e);
                    }
                });
    }

    private boolean downloadFile(Uri uri, @NonNull File destinationFile) throws IOException {
        if (uri == null) {
            Log.e(TAG, "Null uri for file " + destinationFile.getName());
            return false;
        }
        Log.d(TAG, "Downloading file: " + uri);
        ResponseBody body = jsonStorage.getFile(uri.toString()).execute().body();
        return FileUtils.saveStreamToFile(body.byteStream(), destinationFile);
    }

    private void startForeground() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle(getString(R.string.text_new_agenda))
                .setContentText(getString(R.string.text_downloading_agenda))
                .setProgress(0, 0, true);
        startForeground(NOTIFICATION_ID, builder.build());
    }

}
