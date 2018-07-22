package com.cristianmmuresan.traveltransylvania.ui.main;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class MarginDecoration extends RecyclerView.ItemDecoration {
    private int spacePx;
    private final int spanCount;

    MarginDecoration(int spacePx, int spanCount) {
        this.spacePx = spacePx;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        final int position = parent.getChildLayoutPosition(view);

        // add top margin for first row only
        if (position <= spanCount)
            outRect.top = spacePx;

        // setup left and right margins for left column
        if (position % 2 == 0) {
            outRect.left = spacePx;
            outRect.right = spacePx / 2;
        }
        // setup left and right margins for right column
        else {
            outRect.left = spacePx / 2;
            outRect.right = spacePx;
        }

        outRect.bottom = spacePx;
    }
}
