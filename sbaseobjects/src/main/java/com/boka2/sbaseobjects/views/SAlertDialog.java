package com.boka2.sbaseobjects.views;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

/*
 * Created by Boka2.
 */

public class SAlertDialog extends AlertDialog {
    public static final String TAG = "SAlertDialog";

    public SAlertDialog(Context context) {
        super(context);
    }

    public SAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public SAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception ex) {
            Log.e(TAG, "Error dismissing dialog: " + ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void onAttachedToWindow() {
        try {
            super.onAttachedToWindow();
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
        }
    }


}