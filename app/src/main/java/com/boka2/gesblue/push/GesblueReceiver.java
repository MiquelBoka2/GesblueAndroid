package com.boka2.gesblue.push;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

/*
 * Created by Boka2.
 */

public class GesblueReceiver extends GCMBroadcastReceiver {
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return "com.sixtemia.gesblue.push.GCMGesblue";
	}
}