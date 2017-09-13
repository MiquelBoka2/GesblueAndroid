package com.sixtemia.spushnotifications.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.sixtemia.spushnotifications.R;

public class PreferencesSPush {

	public static String PREFS_NAME = "smodpushnotificationsprefs";

	private static String PREFERENCE_APP_INSTALLATION_DATE = "pushnotificationsInstallationDate";
	private static String PREFERENCE_PUSH_NOTIFICATIONS_APP_TOKEN_ID = "pushnotificationsAppTokenId";
	private static String PREFERENCE_PUSH_NOTIFICATIONS_SDID = "pushnotificationsSDID";
	private static String PREFERENCE_PUSH_NOTIFICATIONS_RETURN_CLASS_PACKAGE = "pushnotificationsReturnClassPackage";
	private static String PREFERENCE_PUSH_NOTIFICATIONS_LIST_ACTIVITY = "pushnotificationsListActivity";
	private static String PREFERENCE_PUSH_NOTIFICATIONS_WEBVIEW_ACTIVITY = "pushnotificationsWebViewActivity";
	private static String PREFERENCE_PUSH_NOTIFICATIONS_USER_TOKEN = "pushnotificationsUserToken";

	private static String PREF_PUSH_WS_BASE_URL = "spushnotificationsWSBaseUrl";

	public static String getAppInstallationDate(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(PREFS_NAME, 0);
		return preferences.getString(PREFERENCE_APP_INSTALLATION_DATE, "");
	}
	
	public static void setAppInstallationDate(Context _context, String _strDate) {
		SharedPreferences.Editor editor = _context.getSharedPreferences(PREFS_NAME, 0).edit().putString(PREFERENCE_APP_INSTALLATION_DATE, _strDate);
		editor.apply();
	}

	public static String getAppTokenID(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(PREFS_NAME, 0);
		return preferences.getString(PREFERENCE_PUSH_NOTIFICATIONS_APP_TOKEN_ID, "");
	}

	public static void setAppTokenID(Context _context, String _strToken) {
		SharedPreferences.Editor editor = _context.getSharedPreferences(PREFS_NAME, 0).edit()
				.putString(PREFERENCE_PUSH_NOTIFICATIONS_APP_TOKEN_ID, _strToken);
		editor.apply();
	}

	public static String getUserTokenID(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(PREFS_NAME, 0);
		return preferences.getString(PREFERENCE_PUSH_NOTIFICATIONS_USER_TOKEN, "");
	}

	public static void setUserTokenID(Context _context, String _strToken) {
		SharedPreferences.Editor editor = _context.getSharedPreferences(PREFS_NAME, 0).edit()
				.putString(PREFERENCE_PUSH_NOTIFICATIONS_USER_TOKEN, _strToken);
		editor.apply();
	}

	public static String getWSBaseUrl(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(PREFS_NAME, 0);
		return preferences.getString(PREF_PUSH_WS_BASE_URL, _context.getResources().getString(R.string.smodpushnotifications_url_base_webservices));
	}

	public static void setWSBaseUrl(Context _context, String _strUrlPushRegister) {
		SharedPreferences.Editor editor = _context.getSharedPreferences(PREFS_NAME, 0).edit().putString(PREF_PUSH_WS_BASE_URL, _strUrlPushRegister);
		editor.apply();
	}

	public static String getSDID(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(PREFS_NAME, 0);
		return preferences.getString(PREFERENCE_PUSH_NOTIFICATIONS_SDID, "");
	}

	public static void setSDID(Context _context, String _strSDID) {
		SharedPreferences.Editor editor = _context.getSharedPreferences(PREFS_NAME, 0).edit().putString(PREFERENCE_PUSH_NOTIFICATIONS_SDID, _strSDID);
		editor.apply();
	}

	/**
	 * Defineix el path complert de la classe que s'ha d'obrir al fer back des del llistat de notificacions
	 * Exemple: com.sixtemia.sauledacatalegs.CategoriesListActivity
	 *
	 * @param _context
	 * @return
	 */
	public static String getReturnClassPackage(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(PREFS_NAME, 0);
		return preferences.getString(PREFERENCE_PUSH_NOTIFICATIONS_RETURN_CLASS_PACKAGE, "");
	}

	/**
	 * Retorna el path complert de la classe que s'ha d'obrir al fer back des del llistat de notificacions
	 * Exemple: com.sixtemia.sauledacatalegs.CategoriesListActivity
	 *
	 * @param _context
	 * @param _strReturnClassPackage
	 */
	public static void setReturnClassPackage(Context _context, String _strReturnClassPackage) {
		SharedPreferences.Editor editor = _context.getSharedPreferences(PREFS_NAME, 0).edit()
				.putString(PREFERENCE_PUSH_NOTIFICATIONS_RETURN_CLASS_PACKAGE, _strReturnClassPackage);
		editor.apply();
	}

	public static Class getNotificationsListActivity(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(PREFS_NAME, 0);

		String strClass = preferences.getString(PREFERENCE_PUSH_NOTIFICATIONS_LIST_ACTIVITY, "");

		if (!TextUtils.isEmpty(strClass)) {
			Class returnClass;
			try {
				returnClass = Class.forName(strClass);
			} catch (ClassNotFoundException e) {
				returnClass = null;
			}
			return returnClass;
		} else {
			return null;
		}

	}

	public static void setNotificationsListActivity(Context _context, Class _listActivityClass) {
		String strClassName = _listActivityClass.getName();

		SharedPreferences.Editor editor = _context.getSharedPreferences(PREFS_NAME, 0).edit()
				.putString(PREFERENCE_PUSH_NOTIFICATIONS_LIST_ACTIVITY, strClassName);
		editor.apply();
	}

	public static Class getNotificationsWebviewActivity(Context _context) {
		SharedPreferences preferences = _context.getSharedPreferences(PREFS_NAME, 0);

		String strClass = preferences.getString(PREFERENCE_PUSH_NOTIFICATIONS_WEBVIEW_ACTIVITY, "");

		if (!TextUtils.isEmpty(strClass)) {
			Class returnClass;
			try {
				returnClass = Class.forName(strClass);
			} catch (ClassNotFoundException e) {
				returnClass = null;
			}
			return returnClass;
		} else {
			return null;
		}

	}

	public static void setNotificationsWebviewActivity(Context _context, Class _webviewActivityClass) {
		String strClassName = _webviewActivityClass.getName();

		SharedPreferences.Editor editor = _context.getSharedPreferences(PREFS_NAME, 0).edit()
				.putString(PREFERENCE_PUSH_NOTIFICATIONS_WEBVIEW_ACTIVITY, strClassName);
		editor.apply();
	}

}
