package com.github.lecho.mobilization.ui.snackbar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.lecho.mobilization.rx.RxBus;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

/**
 * Created by Leszek on 2015-10-04.
 */
public class SnackbarHelper {

    private final Context context;
    private final View parentView;
    private Map<Class, Subscription> subscriptions = new HashMap<>();

    public SnackbarHelper(Context context, View parentView) {
        this.context = context;
        this.parentView = parentView;
    }

    public void showSnackbar(@StringRes int text) {
        Snackbar.make(parentView, text, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackbar(@StringRes int text, @StringRes int actionText, @NonNull View.OnClickListener listener) {
        Snackbar.make(parentView, text, Snackbar.LENGTH_SHORT).setAction(actionText, listener).show();
    }

    public void subscribe(@NonNull Class<?> clazz, @StringRes int textRes) {
        subscriptions.put(clazz, RxBus.subscribe(clazz, e -> showSnackbar(textRes)));
    }

    public void subscribe(@NonNull Class clazz, @StringRes int textRes, @StringRes int actionText,
                          @NonNull View.OnClickListener listener) {
        subscriptions.put(clazz, RxBus.subscribe(clazz, e -> showSnackbar(textRes, actionText, listener)));
    }

    public void unsubscribe(@NonNull Class clazz) {
        Subscription subscription = subscriptions.get(clazz);
        if (subscription != null) {
            subscription.unsubscribe();
            subscriptions.put(clazz, null);
        }
    }

}
