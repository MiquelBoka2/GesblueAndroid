package com.sixtemia.sbaseobjects.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.sixtemia.sbaseobjects.R;

/**
 * Created by rubengonzalez on 14/6/16.
 */

public class SProgressBar extends ProgressBar {

    private static final int DEFAULT_COLOR = Color.BLUE;

    public SProgressBar(Context context) {
        super(context);
    }

    public SProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttrs(context, attrs);
    }

    public SProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(context, attrs);
    }

    public SProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    public void setProgressColor(int color) {
        if(getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        if(getProgressDrawable() != null) {
            final LayerDrawable layers = (LayerDrawable) getProgressDrawable();
            layers.getDrawable(2).setColorFilter(color,PorterDuff.Mode.SRC_IN);;
        }
    }

    private void readAttrs(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            // Ignore if within editor
            return;
        }
        if (attrs != null) {
            // Look up any layout-defined attributes
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SProgressBar, 0, 0);
            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.SProgressBar_progressColor) {
                    setProgressColor(a.getColor(R.styleable.SProgressBar_progressColor, DEFAULT_COLOR));
                }
            }
            a.recycle();
        }
    }
}