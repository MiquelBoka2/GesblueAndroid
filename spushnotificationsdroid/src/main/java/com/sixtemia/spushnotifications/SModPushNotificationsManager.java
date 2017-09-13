package com.sixtemia.spushnotifications;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.sixtemia.spushnotifications.classes.GCMIntentService;
import com.sixtemia.spushnotifications.classes.PreferencesSPush;
import com.sixtemia.spushnotifications.classes.SModPushNotificationsManagerListener;
import com.sixtemia.spushnotifications.datamanager.DataManagerSPushNotifications;
import com.sixtemia.spushnotifications.datamanager.SDataManagerListener;
import com.sixtemia.spushnotifications.db.DataBase;
import com.sixtemia.spushnotifications.model.SModPushPendingNotificationsCountResult;
import com.sixtemia.sutils.classes.SDateUtils;

public class SModPushNotificationsManager {

	private static Context mContext;
		
	private static SModPushNotificationsManager mCurrent = null;
	private static DataManagerSPushNotifications dataManager = null;
	
	
	// Inici Singleton
	public static SModPushNotificationsManager Current(Context _context) {
		if (mCurrent == null) {
			mCurrent = new SModPushNotificationsManager(_context);
		} else {
			mCurrent.setContext(_context);
		}
		if (dataManager == null) {
			dataManager = new DataManagerSPushNotifications(_context);
		}
		return mCurrent;
	}
	
	private SModPushNotificationsManager(Context _context) {
		mContext = _context;
	}

	public void setContext(Context _context) {
		mContext = _context;
	}

	public void getNumNovesNotificacions(final TextView _txtResultat, final SModPushNotificationsManagerListener listener) {
		try {
			DataBase.initialize(mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//int intNewestNotificationId = DataBase.Context.NotificacionsSet.getNewestNotificationID();
		String strDataUltimaNotificacio = DataBase.Context.NotificacionsSet.getNewestNotificationDate();

		if (strDataUltimaNotificacio.equals("")) {
			strDataUltimaNotificacio = PreferencesSPush.getAppInstallationDate(mContext);
		}
		String strAppTokenID = PreferencesSPush.getAppTokenID(mContext); //mContext.getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);
		
		
		dataManager.getPendingNotificacionsCount(/*intNewestNotificationId,*/strDataUltimaNotificacio, strAppTokenID, new SDataManagerListener() {
			@Override
			public void onError(Object result) {
				if (_txtResultat != null) {
					_txtResultat.setText(String.valueOf(0));
					_txtResultat.setVisibility(View.GONE);
				}
				listener.onGetNumNovesNotificacionsFinishedListener(0);
			}

			@Override
			public void onCompletion(Object result) {
				SModPushPendingNotificationsCountResult object = (SModPushPendingNotificationsCountResult) result;
				try {
					if (_txtResultat != null) {
						if (object.getIntTotal() > 0) {
							_txtResultat.setText(String.valueOf(object.getIntTotal()));
							_txtResultat.setVisibility(View.VISIBLE);
						} else {
							_txtResultat.setText(String.valueOf(object.getIntTotal()));
							_txtResultat.setVisibility(View.GONE);
						}
					}
					listener.onGetNumNovesNotificacionsFinishedListener(object.getIntTotal());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	public void registerAppForPushNotifications() {
		String APP_TOKEN_ID = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);
		String GCM_SENDER_ID = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_GCM_SENDER_ID);
		String URL_PUSH_REGISTER = mContext.getResources().getString(R.string.smodpushnotifications_url_base_webservices)
				+ mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_1_register);
		String RETURN_ACTIVITY = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_RETURN_ACTIVITY);

		Class receiverClass = SModPushNotificationsListActivity.class;
		// Guardem les classes receiver a les preferences. Ho fem per poder-les recuperar en cas que l'app estigui tancada i siguin classes no estàndards
		PreferencesSPush.setNotificationsListActivity(mContext, receiverClass);

		PreferencesSPush.setAppTokenID(mContext, APP_TOKEN_ID);
		PreferencesSPush.setWSBaseUrl(mContext, mContext.getResources().getString(R.string.smodpushnotifications_url_base_webservices));
		PreferencesSPush.setReturnClassPackage(mContext, RETURN_ACTIVITY);

		// Iniciem el registre de l'app. Aquesta crida s'ha de posar sempre a l'activitat que volem que s'encarregui de registrar les push
		GCMIntentService.registerAppForPushNotifications(mContext, APP_TOKEN_ID, GCM_SENDER_ID, URL_PUSH_REGISTER, false, receiverClass);

		saveDateFirstExecution();
	}

	public void registerAppForPushNotifications(String _strAppTokenID, String _strGCMSenderID, String _strUrlBaseWS, boolean _username_auto) {
		String RETURN_ACTIVITY = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_RETURN_ACTIVITY);

		Class receiverClass = SModPushNotificationsListActivity.class;
		// Guardem les classes receiver a les preferences. Ho fem per poder-les recuperar en cas que l'app estigui tancada i siguin classes no estàndards
		PreferencesSPush.setNotificationsListActivity(mContext, receiverClass);

		PreferencesSPush.setAppTokenID(mContext, _strAppTokenID);
		PreferencesSPush.setWSBaseUrl(mContext, _strUrlBaseWS);
		PreferencesSPush.setReturnClassPackage(mContext, RETURN_ACTIVITY);

		String strUrlRegistre = _strUrlBaseWS + mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_1_register);

		// Iniciem el registre de l'app. Aquesta crida s'ha de posar sempre a l'activitat que volem que s'encarregui de registrar les push
		GCMIntentService.registerAppForPushNotifications(mContext, _strAppTokenID, _strGCMSenderID, strUrlRegistre, _username_auto, receiverClass);

		saveDateFirstExecution();
	}

	public void registerAppForPushNotifications(String _strAppTokenID, String _strGCMSenderID, String _strUrlBaseWS, boolean _username_auto, Class _receiverClass,
												String _strReturnClassPackageName) {
		// Guardem les classes receiver a les preferences. Ho fem per poder-les recuperar en cas que l'app estigui tancada i siguin classes no estàndards
		PreferencesSPush.setNotificationsListActivity(mContext, _receiverClass);

		PreferencesSPush.setAppTokenID(mContext, _strAppTokenID);
		PreferencesSPush.setWSBaseUrl(mContext, _strUrlBaseWS);
		PreferencesSPush.setReturnClassPackage(mContext, _strReturnClassPackageName);

		String strUrlRegistre = _strUrlBaseWS + mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_1_register);

		// Iniciem el registre de l'app. Aquesta crida s'ha de posar sempre a l'activitat que volem que s'encarregui de registrar les push
		GCMIntentService.registerAppForPushNotifications(mContext, _strAppTokenID, _strGCMSenderID, strUrlRegistre,  _username_auto, _receiverClass);

		saveDateFirstExecution();
	}

	public void registerAppForPushNotifications(String _strAppTokenID, String _strGCMSenderID, String _strUrlBaseWS, boolean _username_auto, Class _receiverClass,
												String _strReturnClassPackageName, Handler _mHandler) {
		// Guardem les classes receiver a les preferences. Ho fem per poder-les recuperar en cas que l'app estigui tancada i siguin classes no estàndards
		PreferencesSPush.setNotificationsListActivity(mContext, _receiverClass);

		PreferencesSPush.setAppTokenID(mContext, _strAppTokenID);
		PreferencesSPush.setWSBaseUrl(mContext, _strUrlBaseWS);
		PreferencesSPush.setReturnClassPackage(mContext, _strReturnClassPackageName);

		String strUrlRegistre = _strUrlBaseWS + mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_1_register);

		// Iniciem el registre de l'app. Aquesta crida s'ha de posar sempre a l'activitat que volem que s'encarregui de registrar les push
		GCMIntentService.registerAppForPushNotifications(mContext, _strAppTokenID, _strGCMSenderID, strUrlRegistre, _receiverClass, _username_auto, "",_mHandler);

		saveDateFirstExecution();
	}


	private void saveDateFirstExecution() {
		//String strDate = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(getResources().getString(R.string.PREFERENCE_INSTALLATION_DATE), "");
		String strDate = PreferencesSPush.getAppInstallationDate(mContext);
		if (strDate.equals("")) {
			try {
				long installed = mContext.getPackageManager().getPackageInfo("com.sixtemia.spushnotifications", 0).firstInstallTime;
				//System.out.println(getDate(82233213123L, "dd/MM/yyyy hh:mm:ss.SSS"));
				//Log.d(TAG, "INSTALADA: " + SUtils.getDate(installed, "yyyyMMddHHmmss"));
				PreferencesSPush.setAppInstallationDate(mContext, SDateUtils.getDateUTC(installed, "yyyyMMddHHmmss"));
			} catch (Exception e) {
				PreferencesSPush.setAppInstallationDate(mContext, SDateUtils.getDateUTC(System.currentTimeMillis(), "yyyyMMddHHmmss"));
			}
		}
	}
	
}
