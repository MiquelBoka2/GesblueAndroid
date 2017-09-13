package com.sixtemia.spushnotifications.classes;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

/*
 * the default implementation of getGCMIntentServiceClassName assumes that the intent service class is located in the package of the application, so it's expecting com.example.pushtest.GCMIntentService instead of com.test.pushlibrary.GCMIntentService
 */
public class SModPushNotificationsReceiver extends GCMBroadcastReceiver {
	/**
	 * Gets the class name of the intent service that will handle GCM messages.
	 */
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return "com.sixtemia.spushnotifications.classes.GCMIntentService";
	}
}
