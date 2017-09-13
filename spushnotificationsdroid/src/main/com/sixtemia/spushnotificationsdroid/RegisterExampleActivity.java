package com.sixtemia.spushnotificationsdroid;


import android.content.Intent;
import android.os.Bundle;

import com.sixtemia.spushnotificationsdroid.classes.PreferencesSPush;
import com.sixtemia.spushnotificationsdroid.objects.SPushActivity;
import com.sixtemia.sutils.classes.SDateUtils;

public class RegisterExampleActivity extends SPushActivity {

	/*
	 * Par�metres de configuraci� necess�ris per configurar les push notifications. 
	 * Es poden guardar en un fitxer de recursos o una classes de Constants espec�fics del projecte
	 * 
	 * Aquests par�metres s�n espec�fics de cada aplicaci�.
	 */
	//public static String APP_TOKEN_ID = "e88651ee8452099e42a352f073f7ce52";
	//public static String GCM_SENDER_ID = "73825460411";
	//public static String URL_PUSH_REGISTER = "http://toppo.sixtemialabs.com/ws/smodpushnotifications/notifications_register";
	//public static Class receiverClass = SModPushNotificationsListActivity.class;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*String APP_TOKEN_ID = getResources().getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);
		String GCM_SENDER_ID = getResources().getString(R.string.SMODPUSHNOTIFICATIONS_GCM_SENDER_ID);
		String URL_PUSH_REGISTER = getResources().getString(R.string.SMODPUSHNOTIFICATIONS_URL_PUSH_REGISTER);
		Class receiverClass = SModPushNotificationsListActivity.class;
		
		// Iniciem el registre de l'app. Aquesta crida s'ha de posar sempre a l'activitat que volem que s'encarregui de registrar les push
		GCMIntentService.registerAppForPushNotifications(getBaseContext(), APP_TOKEN_ID, GCM_SENDER_ID, URL_PUSH_REGISTER, receiverClass);
		
		saveDateFirstExecution();*/

		SModPushNotificationsManager.Current(getApplicationContext()).registerAppForPushNotifications();

		Intent intent = new Intent(getApplicationContext(), SModPushNotificationsListActivity.class);
		startActivity(intent);

		finish();
	}

	private void saveDateFirstExecution() {

		//String strDate = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(getResources().getString(R.string.PREFERENCE_INSTALLATION_DATE), "");
		String strDate = PreferencesSPush.getAppInstallationDate(getBaseContext());

		if (strDate.equals("")) {
			try {
				long installed = this.getPackageManager().getPackageInfo("com.sixtemia.spushnotificationsdroid", 0).firstInstallTime;
				//System.out.println(getDate(82233213123L, "dd/MM/yyyy hh:mm:ss.SSS"));
				//Log.d(TAG, "INSTALADA: " + SUtils.getDate(installed, "yyyyMMddHHmmss"));

				PreferencesSPush.setAppInstallationDate(getBaseContext(), SDateUtils.getDateUTC(installed, "yyyyMMddHHmmss"));

			} catch (Exception e) {

				PreferencesSPush.setAppInstallationDate(getBaseContext(), SDateUtils.getDateUTC(System.currentTimeMillis(), "yyyyMMddHHmmss"));

			}
		}

	}

}
