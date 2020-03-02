package com.boka2.sbaseobjects.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.boka2.sbaseobjects.R;

public class SButton extends Button {

    public SButton(Context context) {
        super(context);
    }

    public SButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public SButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCustomFontByFontFile(String _strFontNameAndExtension, Context _context) {

        try {
            Typeface tf = Typeface.createFromAsset(_context.getAssets(), "fonts/" + _strFontNameAndExtension);

            if (tf != null) {
                setTypeface(tf);
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Could not get typeface: " + e.getMessage());
        }

    }

    protected String getDefaultFont(Context context) {
        return null;
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            // Ignore if within editor
            return;
        }
        String font = getDefaultFont(context);
        if (attrs != null) {
            // Look up any layout-defined attributes
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomFontStyleable, 0, 0);
            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.CustomFontStyleable_customFont) {
                    font = a.getString(0);

                }
            }
            a.recycle();
        }
        if(font != null) {
            setCustomFontByFontFile(font, context);
        }
    }

}