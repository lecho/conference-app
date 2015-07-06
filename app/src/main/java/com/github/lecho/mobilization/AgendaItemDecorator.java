package com.github.lecho.mobilization;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * @see {http://gist.github.com/lapastillaroja/858caf1a82791b6c1a36}
 */
public class AgendaItemDecorator extends RecyclerView.ItemDecoration {

    private Drawable divider;
    private boolean showFirstDivider = false;
    private boolean showLastDivider = false;

    public AgendaItemDecorator(Context context, AttributeSet attrs) {
        final TypedArray a = context
                .obtainStyledAttributes(attrs, new int[]{android.R.attr.listDivider});
        divider = a.getDrawable(0);
        a.recycle();
    }

    public AgendaItemDecorator(Context context, AttributeSet attrs, boolean showFirstDivider,
                               boolean showLastDivider) {
        this(context, attrs);
        this.showFirstDivider = showFirstDivider;
        this.showLastDivider = showLastDivider;
    }

    public AgendaItemDecorator(Drawable divider) {
        this.divider = divider;
    }

    public AgendaItemDecorator(Drawable divider, boolean showFirstDivider,
                               boolean showLastDivider) {
        this(divider);
        this.showFirstDivider = showFirstDivider;
        this.showLastDivider = showLastDivider;
    }

    public AgendaItemDecorator(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            this.divider = context.getResources().getDrawable(R.drawable.divider_agenda_item);
        } else {
            this.divider = context.getResources().getDrawable(R.drawable.divider_agenda_item, null);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (divider == null) {
            return;
        }
        if (parent.getChildAdapterPosition(view) < 1) {
            return;
        }

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.top = divider.getIntrinsicHeight();
        } else {
            outRect.left = divider.getIntrinsicWidth();
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (divider == null) {
            super.onDrawOver(c, parent, state);
            return;
        }

        // Initialization needed to avoid compiler warning
        int left = 0, right = 0, top = 0, bottom = 0, size;
        int orientation = getOrientation(parent);
        int childCount = parent.getChildCount();

        if (orientation == LinearLayoutManager.VERTICAL) {
            size = divider.getIntrinsicHeight();
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else { //horizontal
            size = divider.getIntrinsicWidth();
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
        }

        for (int i = showFirstDivider ? 0 : 1; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (orientation == LinearLayoutManager.VERTICAL) {
                if (isReverseLayout(parent)) {
                    bottom = child.getBottom() - params.bottomMargin;
                    top = bottom - size;
                } else {
                    top = child.getTop() - params.topMargin;
                    bottom = top + size;
                }
            } else { //horizontal
                if (isReverseLayout(parent)) {
                    right = child.getRight() - params.rightMargin;
                    left = right - size;
                } else {
                    left = child.getLeft() - params.leftMargin;
                    right = left + size;
                }
            }
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }

        // show last divider
        if (showLastDivider && childCount > 0) {
            View child = parent.getChildAt(childCount - 1);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (orientation == LinearLayoutManager.VERTICAL) {
                top = child.getBottom() + params.bottomMargin;
                bottom = top + size;
            } else { // horizontal
                left = child.getRight() + params.rightMargin;
                right = left + size;
            }
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    private int getOrientation(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            return layoutManager.getOrientation();
        } else {
            throw new IllegalStateException(
                    "DividerItemDecoration can only be used with a LinearLayoutManager.");
        }
    }

    private boolean isReverseLayout(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            return layoutManager.getReverseLayout();
        } else {
            throw new IllegalStateException(
                    "DividerItemDecoration can only be used with a LinearLayoutManager.");
        }
    }
}
