package com.github.lecho.conference.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.github.lecho.conference.R;
import com.github.lecho.conference.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Leszek on 2015-10-07.
 */
public class CircleImageViewBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private Rect mTmpRect;
    private boolean isHiding;

    public CircleImageViewBehavior() {
    }

    public CircleImageViewBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            this.updateVisibility(parent, (AppBarLayout) dependency, child);
        }
        return false;
    }


    private boolean updateVisibility(CoordinatorLayout parent, AppBarLayout appBarLayout, CircleImageView child) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.getAnchorId() != appBarLayout.getId()) {
            return false;
        } else {
            if (this.mTmpRect == null) {
                this.mTmpRect = new Rect();
            }

            Rect rect = this.mTmpRect;
            ViewGroupUtilsHoneycomb.getDescendantRect(parent, appBarLayout, rect);
            if (rect.bottom <= child.getContext().getResources().getDimensionPixelSize(R.dimen
                    .main_avatar_min_distance_from_toolbar)) {
                hide(child);
            } else {
                show(child);
            }

            return true;
        }
    }

    public boolean onLayoutChild(CoordinatorLayout parent, CircleImageView child, int layoutDirection) {
        List dependencies = parent.getDependencies(child);
        int i = 0;

        for (int count = dependencies.size(); i < count; ++i) {
            View dependency = (View) dependencies.get(i);
            if (dependency instanceof AppBarLayout && updateVisibility(parent, (AppBarLayout) dependency, child)) {
                break;
            }
        }

        parent.onLayoutChild(child, layoutDirection);
        return true;
    }

    private void hide(final CircleImageView view) {
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

    private void show(final CircleImageView view) {
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

    private static class ViewGroupUtilsHoneycomb {
        private static final ThreadLocal<Matrix> sMatrix = new ThreadLocal();
        private static final ThreadLocal<RectF> sRectF = new ThreadLocal();
        private static final Matrix IDENTITY = new Matrix();

        ViewGroupUtilsHoneycomb() {
        }

        static void getDescendantRect(ViewGroup parent, View descendant, Rect out) {
            out.set(0, 0, descendant.getWidth(), descendant.getHeight());
            offsetDescendantRect(parent, descendant, out);
        }

        public static void offsetDescendantRect(ViewGroup group, View child, Rect rect) {
            Matrix m = sMatrix.get();
            if (m == null) {
                m = new Matrix();
                sMatrix.set(m);
            } else {
                m.set(IDENTITY);
            }

            offsetDescendantMatrix(group, child, m);
            RectF rectF = sRectF.get();
            if (rectF == null) {
                rectF = new RectF();
            }

            rectF.set(rect);
            m.mapRect(rectF);
            rect.set((int) (rectF.left + 0.5F), (int) (rectF.top + 0.5F), (int) (rectF.right + 0.5F), (int) (rectF
                    .bottom + 0.5F));
        }

        static void offsetDescendantMatrix(ViewParent target, View view, Matrix m) {
            ViewParent parent = view.getParent();
            if (parent instanceof View && parent != target) {
                View vp = (View) parent;
                offsetDescendantMatrix(target, vp, m);
                m.preTranslate((float) (-vp.getScrollX()), (float) (-vp.getScrollY()));
            }

            m.preTranslate((float) view.getLeft(), (float) view.getTop());
            if (!view.getMatrix().isIdentity()) {
                m.preConcat(view.getMatrix());
            }
        }
    }
}
