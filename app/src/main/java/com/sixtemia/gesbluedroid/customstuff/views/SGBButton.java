package com.sixtemia.gesbluedroid.customstuff.views;

import android.content.Context;
import android.util.AttributeSet;

import com.sixtemia.sbaseobjects.views.SButton;

import com.sixtemia.gesbluedroid.R;

/**
 * Created by joelabello on 20/7/16.
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
