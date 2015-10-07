package com.github.lecho.conference.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Leszek on 2015-10-07.
 */
public class CircleImageViewBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private Rect mTmpRect;
    private boolean isHiding;

    public CircleImageViewBehavior() {
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
            if (rect.bottom <= 120) {
                hide(child);
            } else {
                show(child);
            }

            return true;
        }
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


//public static class Behavior extends android.support.design.widget.CoordinatorLayout.Behavior<FloatingActionButton> {
//    private static final boolean SNACKBAR_BEHAVIOR_ENABLED;
//    private Rect mTmpRect;
//
//    public Behavior() {
//    }
//
//    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
//        return SNACKBAR_BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
//    }
//
//    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
//        if (dependency instanceof Snackbar.SnackbarLayout) {
//            this.updateFabTranslationForSnackbar(parent, child, dependency);
//        } else if (dependency instanceof AppBarLayout) {
//            this.updateFabVisibility(parent, (AppBarLayout) dependency, child);
//        }
//
//        return false;
//    }
//
//    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
//        if (dependency instanceof Snackbar.SnackbarLayout && ViewCompat.getTranslationY(child) != 0.0F) {
//            ViewCompat.animate(child).translationY(0.0F).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator
//                    (AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener((ViewPropertyAnimatorListener) null);
//        }
//
//    }
//
//    private boolean updateFabVisibility(CoordinatorLayout parent, AppBarLayout appBarLayout, FloatingActionButton
//            child) {
//        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
//        if (lp.getAnchorId() != appBarLayout.getId()) {
//            return false;
//        } else {
//            if (this.mTmpRect == null) {
//                this.mTmpRect = new Rect();
//            }
//
//            Rect rect = this.mTmpRect;
//            ViewGroupUtils.getDescendantRect(parent, appBarLayout, rect);
//            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
//                child.hide();
//            } else {
//                child.show();
//            }
//
//            return true;
//        }
//    }
//
//    private void updateFabTranslationForSnackbar(CoordinatorLayout parent, FloatingActionButton fab, View snackbar) {
//        if (fab.getVisibility() == 0) {
//            float translationY = this.getFabTranslationYForSnackbar(parent, fab);
//            ViewCompat.setTranslationY(fab, translationY);
//        }
//    }
//
//    private float getFabTranslationYForSnackbar(CoordinatorLayout parent, FloatingActionButton fab) {
//        float minOffset = 0.0F;
//        List dependencies = parent.getDependencies(fab);
//        int i = 0;
//
//        for (int z = dependencies.size(); i < z; ++i) {
//            View view = (View) dependencies.get(i);
//            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
//                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - (float) view.getHeight());
//            }
//        }
//
//        return minOffset;
//    }
//
//    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
//        List dependencies = parent.getDependencies(child);
//        int i = 0;
//
//        for (int count = dependencies.size(); i < count; ++i) {
//            View dependency = (View) dependencies.get(i);
//            if (dependency instanceof AppBarLayout && this.updateFabVisibility(parent, (AppBarLayout) dependency,
//                    child)) {
//                break;
//            }
//        }
//
//        parent.onLayoutChild(child, layoutDirection);
//        this.offsetIfNeeded(parent, child);
//        return true;
//    }
//
//    private void offsetIfNeeded(CoordinatorLayout parent, FloatingActionButton fab) {
//        Rect padding = fab.mShadowPadding;
//        if (padding != null && padding.centerX() > 0 && padding.centerY() > 0) {
//            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
//            int offsetTB = 0;
//            int offsetLR = 0;
//            if (fab.getRight() >= parent.getWidth() - lp.rightMargin) {
//                offsetLR = padding.right;
//            } else if (fab.getLeft() <= lp.leftMargin) {
//                offsetLR = -padding.left;
//            }
//
//            if (fab.getBottom() >= parent.getBottom() - lp.bottomMargin) {
//                offsetTB = padding.bottom;
//            } else if (fab.getTop() <= lp.topMargin) {
//                offsetTB = -padding.top;
//            }
//
//            fab.offsetTopAndBottom(offsetTB);
//            fab.offsetLeftAndRight(offsetLR);
//        }
//
//    }
//
//    static {
//        SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;
//    }
//}