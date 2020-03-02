package com.boka2.gesblue.customstuff.views;

import android.content.Context;
import android.util.AttributeSet;

import com.boka2.sbaseobjects.views.SButton;

import com.boka2.gesblue.R;

/**
 * Created by Boka2.
 */

public class SGBButton extends SButton {
    public SGBButton(Context context) {
        super(context);
    }

    @Override
    protected String getDefaultFont(Context context) {
        return context.getString(R.string.font_regular);
    }

    public SGBButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SGBButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
