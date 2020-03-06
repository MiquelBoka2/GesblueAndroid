package com.boka2.gesblue.push;

import android.content.Context;
import android.util.Log;

import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.R;
import com.boka2.spushnotifications.base.GCMIntentServiceBase;

/*
 * Created by Boka2.
 */

public class GCMGesblue extends GCMIntentServiceBase {
	@Override
	public Class getReceiverClass() {
		return GesblueReceiver.class;
	}

	@Override
	protected String getReceiverClassParamComingFromPush() {
		return "com.fhag.HospitalGranollers.push.GesblueReceiver";
	}

	@Override
	protected int getSmallNotificationIcon() {
		return R.mipmap.ic_launcher;
	}

	@Override
	protected int getLargeNotificationIcon() {
		return R.mipmap.ic_launcher;
	}

	@Override
	protected void onSDIDReceived(Context c, String sdid) {
		PreferencesGesblue.setSDID(c, sdid);
		Log.i("GCMGesblue", "SDID: " + sdid);
	}

	@Override
	protected boolean isEnabled(Context c) {
		return PreferencesGesblue.getEnabled(c);
	}
}