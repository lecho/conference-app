package com.github.lecho.mobilization.util;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.DatabaseUpdateService;
import com.github.lecho.mobilization.async.JsonDataVersion;
import com.github.lecho.mobilization.realmmodel.RealmFacade;
import com.github.lecho.mobilization.ui.dialog.SlotConflictDialogFragment;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.realm.Realm;

/**
 * Created by Leszek on 2015-09-29.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static final int DEFAULT_JSON_DATA_VERSION = 1;

    private static final String ASSETS_JSON_FOLDER = "json";
    private static final String ASSETS_SPEAKERS_IMAGES = "file:///android_asset/images/speakers/";
    private static final String ASSETS_SPONSORS_IMAGES = "file:///android_asset/images/sponsors/";
    private static final String ASSETS_HEADERS_IMAGES = "file:///android_asset/images/headers/";

    private static final String JSON_FOLDER = "assets/json";

    private static final String GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps";
    private static final String TWITTER_WWW = "https://twitter.com/";
    private static final String TWITTER_PACKAGE = "com.twitter.android";
    private static final String TWITTER_URI = "twitter://user?screen_name=";
    private static final String PREFS_FILE_NAME = "conference-shared-prefs";
    private static final String PREFS_SCHEMA_VERSION = "schema-version";
    private static final String PREFS_JSON_DATA_CURRENT_VERSION = "json-data-current-version";
    private static final String PREFS_JSON_DATA_NEXT_VERSION = "json-data-next-version";
    private static final String PREFS_JSON_UPDATE_DIALOG_WAS_SHOWN = "json-update-dialog-was-shown";

    public static final String MAP_IMAGE = "map.jpg";

    private static Transformation PICASSO_CIRCLE_TRANSFORMATION;

    private static Transformation getTransformation(Context context) {
        if (PICASSO_CIRCLE_TRANSFORMATION == null) {
            final int color;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                color = context.getResources().getColor(R.color.black);
            } else {
                color = context.getColor(R.color.black);
            }
            PICASSO_CIRCLE_TRANSFORMATION = new RoundedTransformationBuilder()
                    .borderColor(color)
                    .borderWidthDp(2)
                    .scaleType(ImageView.ScaleType.CENTER_CROP)
                    .oval(true)
                    .build();
        }
        return PICASSO_CIRCLE_TRANSFORMATION;
    }

    public static void upgradeSchema(Context context) {
        if (Utils.checkIfSchemaUpgradeNeeded(context)) {
            Log.i(TAG, "Upgrading schema");
            DatabaseUpdateService.updateFromAssets(context);
        }
    }

    private static boolean checkIfSchemaUpgradeNeeded(Context context) {
        final long currentSchemaVersion = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .getLong(PREFS_SCHEMA_VERSION, 0);
        final long newSchemaVersion = Realm.getDefaultInstance().getConfiguration().getSchemaVersion();
        if (currentSchemaVersion < newSchemaVersion) {
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
                    .edit();
            editor.putLong(PREFS_SCHEMA_VERSION, newSchemaVersion).apply();
            return true;
        }
        return false;
    }

    // TODO: 09.10.2016 Move to separated class for example PrefsHelper
    public static boolean checkIfJsonUpdateNeeded(Context context) {
        final long currentVersion = getCurrentJsonDataVersion(context);
        final long nextVersion = getNextJsonDataVersion(context);
        return currentVersion < nextVersion;
    }

    /**
     * Check if update event should be skipped. Event should be skipped when nextVersion from update is the same as
     * nextVersion from shared preferences.
     *
     * @param context
     * @param versionFromUpdate
     * @return
     */
    public static boolean checkIfEventShouldBeSkipped(Context context, long versionFromUpdate) {
        final long nextVersion = getNextJsonDataVersion(context);
        return nextVersion == versionFromUpdate;
    }

    public static void saveCurrentJsonDataVersion(Context context, long currentVersion) {
        if (currentVersion < JsonDataVersion.DEFAULT_VERSION) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(PREFS_JSON_DATA_CURRENT_VERSION, currentVersion).apply();
    }

    public static long getCurrentJsonDataVersion(Context context) {
        return context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).getLong
                (PREFS_JSON_DATA_CURRENT_VERSION, DEFAULT_JSON_DATA_VERSION);
    }

    public static void saveNextJsonDataVersion(Context context, long nextVersion) {
        if (nextVersion < JsonDataVersion.DEFAULT_VERSION) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(PREFS_JSON_DATA_NEXT_VERSION, nextVersion).apply();
    }

    public static long getNextJsonDataVersion(Context context) {
        return context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).getLong
                (PREFS_JSON_DATA_NEXT_VERSION, DEFAULT_JSON_DATA_VERSION);
    }

    public static String getJsonAssetsFolder() {
        return ASSETS_JSON_FOLDER;
    }

    public static String getJsonInternalMemoryFolder(Context context) {
        return context.getFilesDir() + File.separator + JSON_FOLDER;
    }

    public static void loadSpeakerImageBig(Context context, String fileName, ImageView imageView) {
        loadSpeakerImage(context, fileName, imageView, R.dimen.speaker_avatar_big_size);
    }

    public static void loadSpeakerImageMedium(Context context, String fileName, ImageView imageView) {
        loadSpeakerImage(context, fileName, imageView, R.dimen.speaker_avatar_medium_size);
    }

    public static void loadSpeakerImageSmall(Context context, String fileName, ImageView imageView) {
        loadSpeakerImage(context, fileName, imageView, R.dimen.speaker_avatar_small_size);
    }

    private static void loadSpeakerImage(Context context, String fileName, ImageView imageView, @DimenRes int dimen) {
        //TODO Because I use transformation there is a problem with rounding placeholder and error image so it always
        // should be some image to load. Don't use placeholder or error image. Transformation is used because
        // RoundImageView doesn't support Picasso fade in animation and that's looks bad.
        //TODO Maybe use Glide library
        Picasso.with(context.getApplicationContext())
                .load(ASSETS_SPEAKERS_IMAGES + fileName)
                .resizeDimen(dimen, dimen)
                .transform(getTransformation(context))
                .into(imageView);
    }

    public static void loadSponsorImage(Context context, String fileName, ImageView imageView) {
        Picasso.with(context.getApplicationContext())
                .load(ASSETS_SPONSORS_IMAGES + fileName)
                .fit()
                .centerInside()
                .into(imageView);
    }

    public static void loadHeaderImage(Context context, String fileName, ImageView imageView) {
        Picasso.with(context.getApplicationContext())
                .load(ASSETS_HEADERS_IMAGES + fileName)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    /**
     * Checks if there is slot conflict for talk with given key. If yes shows conflict dialog.
     *
     * @param activity
     * @param talkKey
     * @return true if there is slot conflict
     */
    public static boolean checkSlotConflict(AppCompatActivity activity, String talkKey) {
        RealmFacade realmFacade = new RealmFacade();
        Optional<TalkViewModel> optionalConflictedTalk = realmFacade.getConflictedTalk(talkKey);
        if (optionalConflictedTalk.isPresent()) {
            TalkViewModel conflictedTalk = optionalConflictedTalk.get();
            SlotConflictDialogFragment.show(activity, conflictedTalk.key, conflictedTalk.title, talkKey);
            return true;
        }
        return false;
    }

    public static boolean launchGMaps(Context context, double latitude, double longitude) {
        final String GMAPS = "geo:";
        final String ZOOM = "?z=17";
        StringBuilder sb = new StringBuilder().append(GMAPS).append(Double.toString(latitude)).append(",")
                .append(Double.toString(longitude)).append(ZOOM);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
            intent.setPackage(GOOGLE_MAPS_PACKAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            if (null == componentName) {
                Log.e(TAG, "No activity to handle geo intent");
                return false;
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Could not start google navigation", e);
            return false;
        }
    }

    public static boolean launchGMaps(Context context, double latitude, double longitude, String address) {
        final String GMAPS = "geo:";
        final String ZOOM = "?z=17&q=";
        StringBuilder sb = new StringBuilder().append(GMAPS).append(Double.toString(latitude)).append(",")
                .append(Double.toString(longitude)).append(ZOOM).append(address);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
            intent.setPackage(GOOGLE_MAPS_PACKAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            if (null == componentName) {
                Log.e(TAG, "No activity to handle geo intent");
                return false;
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Could not start google navigation", e);
            return false;
        }
    }

    @SuppressLint("DefaultLocale")
    public static boolean openWebBrowser(Context context, final String url) {
        try {
            StringBuilder targetUrl = new StringBuilder(url.toLowerCase());
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                targetUrl.insert(0, "http://");
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl.toString()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            if (null == resolveInfo) {
                Log.e(TAG, "No activity to handle web intent");
                return false;
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Could not start web browser", e);
            return false;
        }
    }

    public static boolean openTwitter(Context context, String username) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_URI + username));
        intent.setPackage(TWITTER_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        if (null == componentName) {
            Log.e(TAG, "No activity to handle twitter intent");
            return openWebBrowser(context, TWITTER_WWW + username);
        } else {
            context.startActivity(intent);
            return true;
        }
    }

    public static Pair<String, Integer> getAppVersionAndBuild(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return new Pair<>(pInfo.versionName, pInfo.versionCode);
        } catch (Exception e) {
            Log.e(TAG, "Could not get version number");
            return new Pair<>("", 0);
        }
    }

    public static String readFileFromAssets(Context context, String folderName, String fileName) {
        BufferedInputStream bufferedInputStream = null;
        try {
            File jsonFile = new File(folderName, fileName);
            InputStream inputStream = context.getAssets().open(jsonFile.getPath(), Context.MODE_PRIVATE);
            bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Could not read file from assets", e);
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close input stream", e);
                }
            }
        }
        return "";
    }

    public static String readFileFromInternalStorage(Context context, String folderName, String fileName) {
        BufferedReader bufferedReader = null;
        try {
            FileInputStream inputStream = new FileInputStream(new File(folderName, fileName));
            InputStreamReader reader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            Log.e(TAG, "Could not read file from internal memory", e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close output stream", e);
                }
            }
        }
        return "";
    }

    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);

    }

    public static boolean isOnline(Context context) {
        // TODO check timeout
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
