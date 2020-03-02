package com.boka2.gesblue.push;

import android.content.Context;

import com.boka2.gesblue.R;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.spushnotifications.base.GCMIntentServiceBase;
import com.boka2.spushnotifications.base.SModPushNotificationsBaseManager;
import com.boka2.spushnotifications.classes.PreferencesSPush;

/**
 * Created by Boka2.
 */

public class GesbluePushManager extends SModPushNotificationsBaseManager {
	public GesbluePushManager(Context _context) {
		super(_context);
	}

	public GesbluePushManager(Context _context, GCMIntentServiceBase.OnRegisterListener _listener) {
		super(_context, _listener);
	}

	@Override
	protected GCMIntentServiceBase getGCM() {
		return new GCMGesblue();
	}

	/**
	 * Fa el registre amb les dades correctes de XML
	 *  - UsernameAuto: Sempre fals
	 *  - Username: res si no està conectat, el nom de usuari si ho està
	 */
	public void registerAppForPushNotifications() {
		String APP_TOKEN_ID = mContext.getResources().getString(com.boka2.spushnotifications.R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);
		String GCM_SENDER_ID = mContext.getResources().getString(com.boka2.spushnotifications.R.string.SMODPUSHNOTIFICATIONS_GCM_SENDER_ID);
		String PUSH_URL_WS = mContext.getResources().getString(com.boka2.spushnotifications.R.string.smodpushnotifications_url_base_webservices);
		PreferencesSPush.setReturnClassPackage(mContext, mContext.getString(R.string.SMODPUSHNOTIFICATIONS_RETURN_ACTIVITY));

		registerAppForPushNotifications(
				APP_TOKEN_ID,
				GCM_SENDER_ID,
				PUSH_URL_WS,
				false,
				PreferencesGesblue.getUserName(mContext)
		);
	}
}