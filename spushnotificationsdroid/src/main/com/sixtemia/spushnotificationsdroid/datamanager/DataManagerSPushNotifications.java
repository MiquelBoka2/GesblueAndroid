package com.boka2.spushnotificationsdroid.datamanager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.boka2.spushnotificationsdroid.R;
import com.boka2.spushnotificationsdroid.classes.PreferencesSPush;
import com.boka2.spushnotificationsdroid.db.DataBase;
import com.boka2.spushnotificationsdroid.model.SModPushNotification;
import com.boka2.spushnotificationsdroid.model.SModPushNotificationsListResult;
import com.boka2.spushnotificationsdroid.model.SModPushPendingNotificationsCountResult;
import com.boka2.spushnotificationsdroid.model.SModPushRegisterResult;
import com.boka2.sutils.classes.SNetworkUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
		private String path = mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_7_unregister_user);

		@Override
		protected SModPushRegisterResult doInBackground(Void... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();

			String localeLanguage = Locale.getDefault().getLanguage();
			String localeParam = "";
			if (localeLanguage.equalsIgnoreCase("ca")) {
				localeParam = "ca";
			} else if (localeLanguage.equalsIgnoreCase("es")) {
				localeParam = "es";
			} else { // per defecte
				localeParam = "en";
			}

			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_UNREGISTER_LANG, localeParam));
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_UNREGISTER_TOKEN, strToken));
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_UNREGISTER_PLATFORM, strPlatform));
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_UNREGISTER_SDID, strSDID));

			//to get json string
			String json = getJSON(pairs, path, HttpMethod.POST, false);

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
		private String path = mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_2_count_new_notif);

		@Override
		protected SModPushPendingNotificationsCountResult doInBackground(Void... arg0) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();

			String localeLanguage = Locale.getDefault().getLanguage();
			String localeParam = "";
			if (localeLanguage.equalsIgnoreCase("ca")) {
				localeParam = "ca";
			} else if (localeLanguage.equalsIgnoreCase("es")) {
				localeParam = "es";
			} else { // per defecte
				localeParam = "en";
			}

			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_LANG, localeParam));

			//to add the rest of parameters
			//			if (intLastNotifId != -1)
			//				pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_NID, Integer.toString(intLastNotifId)));

			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_DATE, strDate));
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_TOKEN, strToken));
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_PLATFORM, strPlatform));
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_SDID, strSDID));

			//to get json string
			String json = getJSON(pairs, path, HttpMethod.POST, false); // No utilitzem la caché per obtenir sempre el número més actualitzat possible
			//String json = getJSONNotificacions(pairs, URL_BASE, path, HttpMethod.POST, false);

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
	// 2.3 LLISTAT NOTIFICACIONS
	// ///////////////////////////////////////////
	DownloadNotificationsList taskDownloadNotificationsList;
	
	public void getLlistatNotificacions(/*int _intLastNotif, */String _strDate, String _strToken, boolean _useCacheData,
			SDataManagerListener listener) {
				
		if (SNetworkUtils.isInternetConnectionAvailable(mContext)) {
			
			if (taskDownloadNotificationsList != null) {
				taskDownloadNotificationsList.cancel(true);
			}
			
			taskDownloadNotificationsList = new DownloadNotificationsList();
			taskDownloadNotificationsList.listener = listener;
			//taskDownloadNotificationsList.intLastNotifId = _intLastNotif;
			taskDownloadNotificationsList.strPlatform = "android";
			taskDownloadNotificationsList.strToken = _strToken;
			taskDownloadNotificationsList.strDate = _strDate;
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
		private String cacheKey = "";
		private String strDate = "";
		private String strSDID = "";
		public boolean useCacheData = true;
		private String path = mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_3_notif_list);
		
		@Override
		protected SModPushNotificationsListResult doInBackground(Void... arg0) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			String localeLanguage = Locale.getDefault().getLanguage();
			String localeParam = "";
			if (localeLanguage.equalsIgnoreCase("ca")) {
				localeParam = "ca";
			} else if (localeLanguage.equalsIgnoreCase("es")) {
				localeParam = "es";
			} else { // per defecte
				localeParam = "en";
			}

			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_LANG, localeParam));

			//to add the rest of parameters
			//			if (intLastNotifId != -1)
			//				pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_NID, Integer.toString(intLastNotifId)));
			//			
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_DATE, strDate));
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_TOKEN, strToken));
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_LIST_PLATFORM, strPlatform));
			pairs.add(new BasicNameValuePair(ParamsWS.PARAM_NOTIFICATIONS_PENDING_SDID, strSDID));
			
			//to get json string
			String json = getJSON(pairs, path, HttpMethod.POST, useCacheData);
			//String json = getJSONNotificacions(pairs, URL_BASE, path, HttpMethod.POST, useCacheData);

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
				
				result.arrayNotificacions = DataBase.Context.NotificacionsSet.getAllNotificacionsFromBDD();
				
				completion(result, listener);
			}
		}
		
	}
	
	
	/*
	 * @param params
	 *            Parametres per la petici�
	 * @param baseUrl
	 *            Url base del webservice
	 * @param urlPath
	 *            Path per afegir al final de la baseUrl o null si no n'hi ha
	 * @param method
	 *            GET o POST
	 * @return Un string amb la resposta al realitzar la connexi� amb el servidor.
	 */
	/*protected String getJSONNotificacions(List<NameValuePair> params, String baseUrl, String urlPath, HttpMethod method, boolean _useCache) {
		try {
			String str = "";
			
			if (SNetworkUtils.isInternetConnectionAvailable(mContext)) {
				//str = getResponse(params, baseUrl, urlPath, method);
				str = getJSON(params, urlPath, method, _useCache);
				//str = getJSON(params, baseUrl + urlPath, method, _useCache);
			} 

			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	
}
