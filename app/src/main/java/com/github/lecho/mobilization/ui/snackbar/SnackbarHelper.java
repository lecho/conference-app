package com.github.lecho.mobilization.ui.snackbar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.lecho.mobilization.rx.RxBus;

import rx.Subscription;

/**
 * Created by Leszek on 2015-10-04.
 */
public class SnackbarHelper {

    private final Context context;
    private final View parentView;
    private Subscription subscription;

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

    public void onResume(@NonNull SnackbarEvent event, @StringRes int text) {
        subscription = RxBus.subscribe(event.getClass(), e -> showSnackbar(text));
    }

    public void onResume(@NonNull SnackbarEvent event, @StringRes int text, @StringRes int actionText,
                         @NonNull View.OnClickListener listener) {
        subscription = RxBus.subscribe(event.getClass(), e -> showSnackbar(text, actionText, listener));
    }

    public void onPause() {
        subscription.unsubscribe();
    }

}
