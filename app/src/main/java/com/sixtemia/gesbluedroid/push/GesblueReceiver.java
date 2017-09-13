package com.sixtemia.gesbluedroid.push;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

/**
 * Created by rubengonzalez on 21/7/16.
 */

public class GesblueReceiver extends GCMBroadcastReceiver {
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return "com.sixtemia.gesblue.push.GCMGesblue";
	}
}