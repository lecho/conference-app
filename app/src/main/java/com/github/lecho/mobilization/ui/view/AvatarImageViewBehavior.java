package com.github.lecho.mobilization.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.github.lecho.mobilization.R;

import java.util.List;

/**
 * Created by Leszek on 2015-10-07.
 */
public class AvatarImageViewBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private Rect rect;
    private boolean isHiding;

    public AvatarImageViewBehavior() {
    }

    public AvatarImageViewBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        this.updateVisibility(parent, dependency, child);
        return false;
    }

    private boolean updateVisibility(CoordinatorLayout parent, View dependant, ImageView child) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.getAnchorId() != dependant.getId()) {
            return false;
        } else {
            if (this.rect == null) {
                this.rect = new Rect();
            }

            rect.set(0, 0, dependant.getWidth(), dependant.getHeight());
            parent.offsetDescendantRectToMyCoords(dependant, rect);
            int imageSize = child.getContext().getResources().getDimensionPixelSize(R.dimen.speaker_avatar_big_size);
            if (rect.top <= imageSize / 2) {
                hide(child);
            } else {
                show(child);
            }

            return true;
        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, ImageView child, int layoutDirection) {
        List dependencies = parent.getDependencies(child);
        int i = 0;

        for (int count = dependencies.size(); i < count; ++i) {
            View dependency = (View) dependencies.get(i);
            if (updateVisibility(parent, dependency, child)) {
                break;
            }
        }

        parent.onLayoutChild(child, layoutDirection);
        return true;
    }

    private void hide(final ImageView view) {
        if (!isHiding && view.getVisibility() == View.VISIBLE) {
            if (ViewCompat.isLaidOut(view) && !view.isInEditMode()) {
                view.animate().scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setDuration(200L).setInterpolator(new
                        FastOutSlowInInterpolator()).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                        isHiding = true;
                        view.setVisibility(View.VISIBLE);
                    }

                    public void onAnimationCancel(Animator animation) {
                        isHiding = false;
                    }

                    public void onAnimationEnd(Animator animation) {
                        isHiding = false;
                        view.setVisibility(View.GONE);
                    }
                });
            } else {
                view.setVisibility(View.GONE);
            }

        }
    }

    private void show(final ImageView view) {
        if (view.getVisibility() != View.VISIBLE) {
            if (ViewCompat.isLaidOut(view) && !view.isInEditMode()) {
                view.setAlpha(0.0F);
                view.setScaleY(0.0F);
                view.setScaleX(0.0F);
                view.animate().scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setDuration(200L).setInterpolator
                        (new FastOutSlowInInterpolator()).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                view.setVisibility(View.VISIBLE);
                view.setAlpha(1.0F);
                view.setScaleY(1.0F);
                view.setScaleX(1.0F);
            }
        }
    }
}
