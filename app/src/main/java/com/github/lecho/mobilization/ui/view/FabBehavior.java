package com.github.lecho.mobilization.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Leszek on 2015-10-07.
 */
public class FabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private Rect tmpRect;

    public FabBehavior() {
    }

    public FabBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        updateFabVisibility(parent, fab, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        return updateFabVisibility(parent, fab, dependency);
    }

    private boolean updateFabVisibility(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (tmpRect == null) {
            tmpRect = new Rect();
        }
        final Rect rect = tmpRect;

        final CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.getAnchorId() != dependency.getId()) {
            //Workaround for FAB behaviour on orientation change:(
            final View anchor = dependency.findViewById(lp.getAnchorId());
            if (anchor == null) {
                return false;
            }
            dependency = anchor;
        }

        calculateDependencyRect(parent, dependency, rect);
        setChildVisibility(child, rect);
        return true;
    }

    private void calculateDependencyRect(CoordinatorLayout parent, View dependency, Rect rect) {
        // View#offsetDescendantRectToMyCoords includes scroll offsets of the last child.
        // We need to reverse it here so that we get the rect of the view itself rather
        // than its content.
        rect.set(0, 0, dependency.getWidth(), dependency.getHeight());
        parent.offsetDescendantRectToMyCoords(dependency, rect);
        rect.offset(dependency.getScrollX(), dependency.getScrollY());

    }

    private void setChildVisibility(FloatingActionButton child, Rect rect) {
        if (rect.top <= child.getHeight() / 2) {
            child.hide();
        } else {
            child.show();
        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton fab,
                                 int layoutDirection) {
        // First, let's make sure that the visibility of the FAB is consistent
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, count = dependencies.size(); i < count; i++) {
            final View dependency = dependencies.get(i);
            if (updateFabVisibility(parent, fab, dependency)) {
                break;
            }
        }
        // Now let the CoordinatorLayout lay out the FAB
        parent.onLayoutChild(fab, layoutDirection);
        return true;
    }
}
