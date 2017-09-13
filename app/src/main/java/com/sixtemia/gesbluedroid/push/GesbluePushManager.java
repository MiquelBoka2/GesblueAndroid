package com.sixtemia.gesbluedroid.push;

import android.content.Context;

import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.spushnotifications.base.GCMIntentServiceBase;
import com.sixtemia.spushnotifications.base.SModPushNotificationsBaseManager;
import com.sixtemia.spushnotifications.classes.PreferencesSPush;

/**
 * Created by rubengonzalez on 21/7/16.
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
		String APP_TOKEN_ID = mContext.getResources().getString(com.sixtemia.spushnotifications.R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);
		String GCM_SENDER_ID = mContext.getResources().getString(com.sixtemia.spushnotifications.R.string.SMODPUSHNOTIFICATIONS_GCM_SENDER_ID);
		String PUSH_URL_WS = mContext.getResources().getString(com.sixtemia.spushnotifications.R.string.smodpushnotifications_url_base_webservices);
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