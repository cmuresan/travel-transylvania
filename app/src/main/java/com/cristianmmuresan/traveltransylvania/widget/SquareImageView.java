package com.cristianmmuresan.traveltransylvania.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.cristianmmuresan.traveltransylvania.R;

public class SquareImageView extends AppCompatImageView {

    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView, defStyleAttr, 0);
        mOrientation = a.getInt(R.styleable.SquareImageView_squareLayoutOrientation, ORIENTATION_VERTICAL);
        a.recycle();
    }

    private int mOrientation = ORIENTATION_VERTICAL;

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }
}
