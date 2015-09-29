package com.github.lecho.conference.util;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

/**
 * Created by Leszek on 2015-09-29.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static final String GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps";

    public static boolean launchGMaps(Context context, double latitude, double longitude) {
        final String GMAPS = "geo:";
        final String ZOOM = "?z=17";
        StringBuilder sb = new StringBuilder().append(GMAPS).append(Double.toString(latitude)).append(",")
                .append(Double.toString(longitude)).append(ZOOM);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
            intent.setPackage(GOOGLE_MAPS_PACKAGE);
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
    public static boolean launchWebBrowser(Context context, String url) {
        try {
            url = url.toLowerCase();
            if (!url.startsWith("http://") || !url.startsWith("https://")) {
                url = "http://" + url;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            if (null == resolveInfo) {
                Log.e(TAG, "No activity to handle web intent");
                return false;
            }
            context.startActivity(intent);
            Log.i(TAG, "Launching browser with url: " + url);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Could not start web browser", e);
            return false;
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
}
