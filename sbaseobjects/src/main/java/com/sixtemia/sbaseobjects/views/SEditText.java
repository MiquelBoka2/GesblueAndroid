package com.sixtemia.sbaseobjects.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.widget.AppCompatEditText;

import com.sixtemia.sbaseobjects.R;

public class SEditText extends AppCompatEditText {

    //The image we are going to use for the Clear button
    private Drawable imgCloseButton;
    private boolean showClearButton = true;
    private int clearButton = R.drawable.clear_white;

    public SEditText(Context context, Drawable imgCloseButton, boolean showClearButton, int clearButton) {
        super(context);
        this.imgCloseButton = imgCloseButton;
        this.showClearButton = showClearButton;
        this.clearButton = clearButton;
    }

    public SEditText(Context context, AttributeSet attrs, Drawable imgCloseButton, boolean showClearButton, int clearButton) {
        super(context, attrs);
        this.imgCloseButton = imgCloseButton;
        this.showClearButton = showClearButton;
        this.clearButton = clearButton;
    }

    public SEditText(Context context, AttributeSet attrs, int defStyleAttr, Drawable imgCloseButton, boolean showClearButton, int clearButton) {
        super(context, attrs, defStyleAttr);
        this.imgCloseButton = imgCloseButton;
        this.showClearButton = showClearButton;
        this.clearButton = clearButton;
    }

    public SEditText(Context context) {
        super(context);
        init(context);
    }

    public SEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        setCustomFont(context, attrs);
    }

    public SEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setCustomFont(context, attrs);
    }

    private void configButton(Context _context, Drawable custom) {
        if(custom == null) {
            imgCloseButton = ContextCompat.getDrawable(_context, clearButton);
        }
        // Set bounds of the Clear button so it will look ok
        imgCloseButton.setBounds(0, 0, imgCloseButton.getIntrinsicWidth(), imgCloseButton.getIntrinsicHeight());

        // There may be initial text in the field, so we may need to display the button
        handleClearButton();
    }

    void init(Context _context) {
        if (isInEditMode()) {
            // Ignore if within editor
            return;
        }
        configButton(_context, null);

        //remove clear button
        SEditText.this.removeClearButton();

        //if the Close image is displayed and the user remove his finger from the button, clear it. Otherwise do nothing
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                SEditText et = SEditText.this;

                if (et.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;

                if (!isEnabled())
                    return false;

                if(!isFocusable())
                    return false;

                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgCloseButton.getIntrinsicWidth()) {
                    et.setText("");

                    try {
                        // et.onEditorAction(EditorInfo.IME_ACTION_SEARCH); // Forcem l'execuci� de la cerca
                    } catch (Exception e) {

                    }

                    SEditText.this.handleClearButton();
                }
                return false;
            }
        });

        //if text changes, take care of the button
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(showClearButton) {
                    if (getText().toString().equals("")) {
                        removeClearButton();
                    } else {
                        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], imgCloseButton, getCompoundDrawables()[3]);
                    }
                } else {
                    removeClearButton();
                }
//				EditTextCustom.this.handleClearButton();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        });
    }

    //intercept Typeface change and set it with our custom font
	/*public void setTypeface(Typeface tf, int style) {
	    if (style == Typeface.BOLD) {
	        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Vegur-B 0.602.otf"));
	    } else {
	        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Vegur-R 0.602.otf"));
	    }
	}*/

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

    void handleClearButton() {
        if(showClearButton) {
            if (this.getText().toString().equals("")) {
                // remove the clear button
                removeClearButton();
            } else {
                // add clear button
                this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], imgCloseButton, this.getCompoundDrawables()[3]);
            }
        } else {
            removeClearButton();
        }
    }

    public void removeClearButton() {
        //remove clear button
        this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
    }

    @Override
    public void setError(CharSequence error) {
        super.setError(error);
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
        boolean updated = false;
        if (attrs != null) {
            // Look up any layout-defined attributes
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomFontStyleable, 0, 0);
            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.CustomFontStyleable_customFont) {
                    font = a.getString(R.styleable.CustomFontStyleable_customFont);
                    updated = true;
                } else if(attr == R.styleable.CustomFontStyleable_clearDrawable) {
                    try {
                        Drawable d = a.getDrawable(R.styleable.CustomFontStyleable_clearDrawable);
                        if (d != null) {
                            imgCloseButton = d;
                        }
                        updated = true;
                    } catch (Exception ex) {
                        Log.e(getClass().getSimpleName(), "Error: " + ex.getLocalizedMessage(), ex);
                    }
                }
            }
            a.recycle();
        }
        if(updated) {
            if (font != null) {
                setCustomFontByFontFile(font, context);
            }
            configButton(context, imgCloseButton);
        }
    }

    public void setShowClearButton(boolean showClearButton) {
        this.showClearButton = showClearButton;
        handleClearButton();
    }
}