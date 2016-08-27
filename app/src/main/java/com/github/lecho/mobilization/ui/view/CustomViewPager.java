package com.github.lecho.mobilization.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Leszek on 27.08.2016.
 */
public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int pagerHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int childHeight = child.getMeasuredHeight();
            if (childHeight > pagerHeight) {
                pagerHeight = childHeight;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(pagerHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}