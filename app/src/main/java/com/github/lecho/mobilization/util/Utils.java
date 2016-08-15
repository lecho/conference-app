package com.github.lecho.mobilization.util;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.ContentUpdateService;
import com.github.lecho.mobilization.realmmodel.RealmFacade;
import com.github.lecho.mobilization.ui.fragment.SlotConflictDialogFragment;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;

/**
 * Created by Leszek on 2015-09-29.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private static final int SMALLEST_WIDTH_DP_FOR_TABLET_LAYOUT = 600;
    private static final int SPAN_COUNT_FOR_TABLET_LAYOUT = 2;

    private static final String ASSETS_JSON_FOLDER = "json";
    private static final String ASSETS_SPEAKERS_IMAGES = "file:///android_asset/images/speakers/";
    private static final String ASSETS_SPONSORS_IMAGES = "file:///android_asset/images/sponsors/";
    private static final String ASSETS_HEADERS_IMAGES = "file:///android_asset/images/headers/";

    private static final String GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps";
    private static final String TWITTER_WWW = "https://twitter.com/";
    private static final String TWITTER_PACKAGE = "com.twitter.android";
    private static final String TWITTER_URI = "twitter://user?screen_name=";
    public static final String PREFS_FILE_NAME = "conference-shared-prefs";
    public static final String PREFS_SCHEMA_VERSION = "schema-version";

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

    public static RecyclerView.LayoutManager getLayoutManager(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration.smallestScreenWidthDp < Utils.SMALLEST_WIDTH_DP_FOR_TABLET_LAYOUT) {
            return new LinearLayoutManager(context);
        } else {
            return new GridLayoutManager(context, SPAN_COUNT_FOR_TABLET_LAYOUT);
        }
    }

    public static void upgradeSchema(Context context) {
        if (Utils.checkIfSchemaUpgradeNeeded(context)) {
            Log.i(TAG, "Upgrading schema");
            Intent serviceIntent = new Intent(context, ContentUpdateService.class);
            context.startService(serviceIntent);
        }
    }

    private static boolean checkIfSchemaUpgradeNeeded(Context context) {
        final long previousSchemaVersion = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).getLong
                (PREFS_SCHEMA_VERSION, 0);
        final long currentSchemaVersion = Realm.getDefaultInstance().getConfiguration().getSchemaVersion();
        if (currentSchemaVersion != previousSchemaVersion) {
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
                    .edit();
            editor.putLong(PREFS_SCHEMA_VERSION, currentSchemaVersion).apply();
            return true;
        }
        return false;
    }

    public static String getJsonFolder() {
        return ASSETS_JSON_FOLDER;
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
        String jsonString = "";
        BufferedInputStream bufferedInputStream = null;
        try {
            File jsonFile = new File(folderName, fileName);
            InputStream inputStream = context.getAssets().open(jsonFile.getPath(), Context.MODE_PRIVATE);
            bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(buffer);
            jsonString = new String(buffer);
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
        return jsonString;
    }

    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);

    }
}
