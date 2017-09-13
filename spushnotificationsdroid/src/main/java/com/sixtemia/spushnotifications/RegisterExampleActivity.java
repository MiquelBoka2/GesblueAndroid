package com.sixtemia.spushnotifications;


import android.content.Intent;
import android.os.Bundle;

import com.sixtemia.spushnotifications.classes.PreferencesSPush;
import com.sixtemia.spushnotifications.objects.SPushActivity;
import com.sixtemia.sutils.classes.SDateUtils;

public class RegisterExampleActivity extends SPushActivity {

	/*
	 * Par�metres de configuraci� necess�ris per configurar les push notifications. 
	 * Es poden guardar en un fitxer de recursos o una classes de Constants espec�fics del projecte
	 * 
	 * Aquests par�metres s�n espec�fics de cada aplicaci�.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
