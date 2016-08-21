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
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        updateFabVisibility(parent, dependency, child);
        return false;
    }

    private boolean updateFabVisibility(CoordinatorLayout parent,
                                        View dependency, FloatingActionButton child) {
        final CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.getAnchorId() != dependency.getId()) {
            return false;
        }

        if (tmpRect == null) {
            tmpRect = new Rect();
        }

        // First, let's get the visible rect of the dependency
        final Rect rect = tmpRect;
        // View#offsetDescendantRectToMyCoords includes scroll offsets of the last child.
        // We need to reverse it here so that we get the rect of the view itself rather
        // than its content.
        rect.set(0, 0, dependency.getWidth(), dependency.getHeight());
        parent.offsetDescendantRectToMyCoords(dependency, rect);
        rect.offset(dependency.getScrollX(), dependency.getScrollY());

        if (rect.top <= child.getHeight() / 2) {
            child.hide();
        } else {
            child.show();
        }
        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child,
                                 int layoutDirection) {
        // First, let's make sure that the visibility of the FAB is consistent
        final List<View> dependencies = parent.getDependencies(child);
        for (int i = 0, count = dependencies.size(); i < count; i++) {
            final View dependency = dependencies.get(i);
            if (updateFabVisibility(parent, dependency, child)) {
                break;
            }
        }
        // Now let the CoordinatorLayout lay out the FAB
        parent.onLayoutChild(child, layoutDirection);
        return true;
    }
}
