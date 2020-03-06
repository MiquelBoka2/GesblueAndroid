package com.boka2.spushnotifications.datamanager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.boka2.spushnotifications.R;
import com.boka2.spushnotifications.classes.ConstantsSPush;
import com.boka2.spushnotifications.classes.PreferencesSPush;
import com.boka2.spushnotifications.classes.SNameValuePair;
import com.boka2.spushnotifications.db.DataBase;
import com.boka2.spushnotifications.model.SModPushNotification;
import com.boka2.spushnotifications.model.SModPushNotificationsListResult;
import com.boka2.spushnotifications.model.SModPushPendingNotificationsCountResult;
import com.boka2.spushnotifications.model.SModPushRegisterResult;
import com.boka2.sutils.classes.SNetworkUtils;
import com.boka2.sutils.classes.SSystemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataManagerSPushNotifications extends SDataManager {
	
	Context mContext;

	public DataManagerSPushNotifications(Context _context) {
		super(_context);
		mContext = _context;
		// Fixem la url base del webservice
		//URL_BASE = context.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_URL_BASE_WS);
		URL_BASE = PreferencesSPush.getWSBaseUrl(_context);
	}

	/*
	 * Mètode per obtenir el número de versió que utilitzarem per enviar als ws
	 * @return el número de versió actual
	 */
	private String getVersion() {
		String strVersion = ConstantsSPush.WS_VERSION;
		return strVersion;
	}
	// ////////////////////////////////////////////
	// 1.7 UNREGISTER USER
	// ///////////////////////////////////////////
	UnregisterUserTask taskUnregisterUser;

	public void unregisterUserPush(String _strAppTokenId, SDataManagerListener listener) {
		String strSDID = PreferencesSPush.getSDID(mContext);
		// Eliminem el SDID guardat a preferences.
		PreferencesSPush.setSDID(mContext, "");
		if (SNetworkUtils.isInternetConnectionAvailable(mContext)) {
			if (taskUnregisterUser != null) {
				taskUnregisterUser.cancel(true);
			}

			taskUnregisterUser = new UnregisterUserTask();
			taskUnregisterUser.listener = listener;
			taskUnregisterUser.strPlatform = "android";
			taskUnregisterUser.strToken = _strAppTokenId;
			taskUnregisterUser.strSDID = strSDID;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				taskUnregisterUser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				taskUnregisterUser.execute();
			}
		} else {
			error(new SModPushRegisterResult(), listener, "");
		}
	}

	private class UnregisterUserTask extends AsyncTask<Void, Void, SModPushRegisterResult> {
		private SDataManagerListener listener;
		private boolean error = false;
		private String strPlatform = "";
		private String strToken = "";
		private String strSDID = "";
		private String ws_path = mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_7_unregister_user);
		private String base_path = mContext.getResources().getString(R.string.smodpushnotifications_url_base_webservices);

		@Override
		protected SModPushRegisterResult doInBackground(Void... params) {
			List<SNameValuePair> pairs = new ArrayList<SNameValuePair>();

			String localeLanguage = Locale.getDefault().getLanguage();
			String localeParam = "";
			if (localeLanguage.equalsIgnoreCase("ca")) {
				localeParam = "ca";
			} else if (localeLanguage.equalsIgnoreCase("es")) {
				localeParam = "es";
			} else { // per defecte
				localeParam = "en";
			}

			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_UNREGISTER_LANG, localeParam));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_UNREGISTER_TOKEN, strToken));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_UNREGISTER_PLATFORM, strPlatform));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_UNREGISTER_SDID, strSDID));
			//to get json string
			String json = getJSON(pairs, base_path, ws_path, HttpMethod.POST, false);
			try {
				Gson gson = new Gson();
				SModPushRegisterResult result = gson.fromJson(json, SModPushRegisterResult.class);
				if (result.strResult.equals("KO")) {
					error = true;
				}
				return result;
			} catch (Exception e) {
				error = true;
				return new SModPushRegisterResult();
			}
		}

		@Override
		protected void onPostExecute(SModPushRegisterResult result) {
			if (!isCancelled()) {
				if (error) {
					error(result, listener, null);
					return;
				}
				completion(result, listener);
			}
		};
	}

	// ////////////////////////////////////////////
	// 2.2 NOTIFICACIONS PENDENTS DE DESCARREGAR
	// ///////////////////////////////////////////
	DownloadPendingNotificationsCount taskDownloadPendingNotificationsCount;

	public void getPendingNotificacionsCount(/*int _intLastNotif,*/String _strDate, String _strToken, SDataManagerListener listener) {
		if (SNetworkUtils.isInternetConnectionAvailable(mContext)) {
			if (taskDownloadPendingNotificationsCount != null) {
				taskDownloadPendingNotificationsCount.cancel(true);
			}
			taskDownloadPendingNotificationsCount = new DownloadPendingNotificationsCount();
			taskDownloadPendingNotificationsCount.listener = listener;
			//taskDownloadPendingNotificationsCount.intLastNotifId = _intLastNotif;
			taskDownloadPendingNotificationsCount.strPlatform = "android";
			taskDownloadPendingNotificationsCount.strToken = _strToken;
			taskDownloadPendingNotificationsCount.strDate = _strDate;
			taskDownloadPendingNotificationsCount.strSDID = PreferencesSPush.getSDID(mContext);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				taskDownloadPendingNotificationsCount.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				taskDownloadPendingNotificationsCount.execute();
			}
		} else {
			// No hi ha internet. Retornem error
			error(new SModPushPendingNotificationsCountResult(), listener, "");
		}
	}

	private class DownloadPendingNotificationsCount extends AsyncTask<Void, Void, SModPushPendingNotificationsCountResult> {

		private SDataManagerListener listener;
		private boolean error = false;
		//private int intLastNotifId = -1;
		private String strPlatform = "";
		private String strToken = "";
		private String cacheKey = "";
		private String strDate = "";
		private String strSDID = "";
		private String ws_path = mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_4_2_count_new_notif);
		private String base_path = mContext.getResources().getString(R.string.smodpushnotifications_url_base_webservices);

		@Override
		protected SModPushPendingNotificationsCountResult doInBackground(Void... arg0) {
			List<SNameValuePair> pairs = new ArrayList<SNameValuePair>();
			String localeLanguage = Locale.getDefault().getLanguage();
			String localeParam = "";
			if (localeLanguage.equalsIgnoreCase("ca")) {
				localeParam = "ca";
			} else if (localeLanguage.equalsIgnoreCase("es")) {
				localeParam = "es";
			} else { // per defecte
				localeParam = "en";
			}
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_LANG, localeParam));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_DATE, strDate));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_TOKEN, strToken));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_PLATFORM, strPlatform));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_SDID, strSDID));

			//to get json string
			String json = getJSON(pairs, base_path, ws_path, HttpMethod.POST, false); // No utilitzem la caché per obtenir sempre el número més actualitzat possible
			//String json = getJSONNotificacions(pairs, URL_BASE, ws_path, HttpMethod.POST, false);

			if(!TextUtils.isEmpty(json)) {
				try {
					Gson gson = new Gson();
					SModPushPendingNotificationsCountResult result = gson.fromJson(json, SModPushPendingNotificationsCountResult.class);
					if (result.strResult.equals("KO")) {
						error = true;
					}
					return result;
				} catch (Exception e) {
					error = true;
					return new SModPushPendingNotificationsCountResult();
				}
			} else {
				error = true;
				return new SModPushPendingNotificationsCountResult();
			}

		}

		@Override
		protected void onPostExecute(SModPushPendingNotificationsCountResult result) {
			super.onPostExecute(result);
			if (!isCancelled()) {
				if (error) {
					error(result, listener, cacheKey);
					return;
				}
				completion(result, listener);
			}
		}
	}

	// ////////////////////////////////////////////
	// 4.1 LLISTAT NOTIFICACIONS
	// ///////////////////////////////////////////
	DownloadNotificationsList taskDownloadNotificationsList;
	
	public void getLlistatNotificacions(String _strDate, String _strToken, boolean _useCacheData, SDataManagerListener listener) {
				
		if (SNetworkUtils.isInternetConnectionAvailable(mContext)) {
			if (taskDownloadNotificationsList != null) {
				taskDownloadNotificationsList.cancel(true);
			}
			taskDownloadNotificationsList = new DownloadNotificationsList();
			taskDownloadNotificationsList.listener = listener;
			taskDownloadNotificationsList.strPlatform = "android";
			taskDownloadNotificationsList.strToken = _strToken;
			taskDownloadNotificationsList.strDate = _strDate;
			taskDownloadNotificationsList.strVer = getVersion();
			taskDownloadNotificationsList.strScreen = SSystemUtils.getDeviceDensityDisplay(mContext);
			taskDownloadNotificationsList.useCacheData = _useCacheData;
			taskDownloadNotificationsList.strSDID = PreferencesSPush.getSDID(mContext);
					
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				taskDownloadNotificationsList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				taskDownloadNotificationsList.execute();
			}
		} else {
			// No hi ha internet. Retornem el que tenim a la base de dades
			SModPushNotificationsListResult result = new SModPushNotificationsListResult();
			ArrayList<SModPushNotification> arrayNotificacions = DataBase.Context.NotificacionsSet.getAllNotificacionsFromBDD();
			if (arrayNotificacions != null) {
				result.setArrayNotificacions(arrayNotificacions);
				result.setStrResult("OK");
				result.setIntMaxCache(arrayNotificacions.size());
				completion(result, listener);
			} else {
				error(new SModPushNotificationsListResult("KO", "KO_INTERNET"), listener, "");
			}
		}
	}

	private class DownloadNotificationsList extends AsyncTask<Void, Void, SModPushNotificationsListResult> {

		private SDataManagerListener listener;
		private boolean error = false;
		//private int intLastNotifId = -1;
		private String strPlatform = "";
		private String strToken = "";
		private String strVer = "";
		private String cacheKey = "";
		private String strDate = "";
		private String strSDID = "";
		private String strScreen = "";
		public boolean useCacheData = false;
		private String ws_path = mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_4_1_notif_list);
		private String base_path = mContext.getResources().getString(R.string.smodpushnotifications_url_base_webservices);
		
		@Override
		protected SModPushNotificationsListResult doInBackground(Void... arg0) {
			List<SNameValuePair> pairs = new ArrayList<>();
			
			String localeLanguage = Locale.getDefault().getLanguage();
			String localeParam = "";
			if (localeLanguage.equalsIgnoreCase("ca")) {
				localeParam = "ca";
			} else if (localeLanguage.equalsIgnoreCase("es")) {
				localeParam = "es";
			} else { // per defecte
				localeParam = "en";
			}

			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_LANG, localeParam));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_DATE, strDate));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_VER, strVer));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_TOKEN, strToken));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_SCREEN, strScreen));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_PLATFORM, strPlatform));
			pairs.add(new SNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_SDID, strSDID));
			//to get json string
			String json = getJSON(pairs, base_path, ws_path, HttpMethod.POST, useCacheData);
			try {
				Gson gson = new Gson();
				SModPushNotificationsListResult result = gson.fromJson(json, SModPushNotificationsListResult.class);

				if (result.strResult.equals("KO")) {
					error = true;
				}
				return result;
			} catch(Exception e) {
				error = true;
				return new SModPushNotificationsListResult();
			}
		}
		
		@Override
		protected void onPostExecute(SModPushNotificationsListResult result) {
			super.onPostExecute(result);
			if (!isCancelled()) {
				if (error) {
					error(result, listener, cacheKey);
					return;
				}
				ArrayList<SModPushNotification> clone = new ArrayList<SModPushNotification>();
				for (SModPushNotification item : result.arrayNotificacions) {
					clone.add(new SModPushNotification(item));
			    }
				// MARQUEM TOTES LES NOTIFICACIONS DE LA BDD COM A LLEGIDES
				DataBase.Context.NotificacionsSet.markAllNotificationsAsRead();
				// GUARDEM LES NOVES NOTIFICACIONS A LA BDD
				DataBase.Context.NotificacionsSet.updateWithNotificacions(clone);
				// ELIMINEM LES NOTIFICACIONS QUE PASSEN DEL LIMIT MARCAT
				DataBase.Context.NotificacionsSet.deleteOldNotifications(result.getIntMaxCache());
				result.setArrayNotificacions(DataBase.Context.NotificacionsSet.getAllNotificacionsFromBDD());
				completion(result, listener);
			}
		}
	}
}
