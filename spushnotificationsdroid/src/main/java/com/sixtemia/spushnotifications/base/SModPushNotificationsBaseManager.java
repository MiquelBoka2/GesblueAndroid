package com.sixtemia.spushnotifications.base;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sixtemia.spushnotifications.R;
import com.sixtemia.spushnotifications.classes.PreferencesSPush;
import com.sixtemia.spushnotifications.classes.SModPushNotificationsManagerListener;
import com.sixtemia.spushnotifications.datamanager.DataManagerSPushNotifications;
import com.sixtemia.spushnotifications.datamanager.SDataManagerListener;
import com.sixtemia.spushnotifications.db.DataBase;
import com.sixtemia.spushnotifications.model.SModPushPendingNotificationsCountResult;
import com.sixtemia.sutils.classes.SDateUtils;
import com.sixtemia.sutils.classes.SSystemUtils;

public abstract class SModPushNotificationsBaseManager {

	protected static Context mContext;

	private static DataManagerSPushNotifications dataManager = null;

	protected SModPushNotificationsBaseManager(Context _context) {
		mContext = _context;
		dataManager = new DataManagerSPushNotifications(_context);
	}

	protected SModPushNotificationsBaseManager(Context _context, GCMIntentServiceBase.OnRegisterListener _listener) {
		mContext = _context;
		dataManager = new DataManagerSPushNotifications(_context);
		GCMIntentServiceBase.setRegisterListener(_listener);
	}

	public void setContext(Context _context) {
		mContext = _context;
	}

	public void getNumNovesNotificacions(TextView tv) {
		getNumNovesNotificacions(tv, null);
	}

	public void getNumNovesNotificacions(SModPushNotificationsManagerListener listener) {
		getNumNovesNotificacions(null, listener);
	}

	public void getNumNovesNotificacions(final TextView _txtResultat, final SModPushNotificationsManagerListener listener) {
		try {
			DataBase.initialize(mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String strDataUltimaNotificacio = DataBase.Context.NotificacionsSet.getNewestNotificationDate();

		if (strDataUltimaNotificacio.equals("")) {
			strDataUltimaNotificacio = PreferencesSPush.getAppInstallationDate(mContext);
		}
		String strAppTokenID = PreferencesSPush.getAppTokenID(mContext);


		dataManager.getPendingNotificacionsCount(strDataUltimaNotificacio, strAppTokenID, new SDataManagerListener() {
			@Override
			public void onError(Object result) {
				if (_txtResultat != null) {
					_txtResultat.setText(String.valueOf(0));
					_txtResultat.setVisibility(View.GONE);
				}
				if(listener != null) {
					listener.onGetNumNovesNotificacionsFinishedListener(0);
				}
			}

			@Override
			public void onCompletion(Object result) {
				SModPushPendingNotificationsCountResult object = (SModPushPendingNotificationsCountResult) result;
				try {
					if(SSystemUtils.isDebugging(mContext)) {
						Log.i("PushNotificationManager", "Count: " + object.getIntTotal());
					}

					//TODO: Treure això
					if(Math.random() * 10 > 5) {
						object.setIntTotal((int) Math.round(Math.random() * 10));
					}

					if (_txtResultat != null) {
						if (object.getIntTotal() > 0) {
							_txtResultat.setText(String.valueOf(object.getIntTotal()));
							_txtResultat.setVisibility(View.VISIBLE);
						} else {
							_txtResultat.setText(String.valueOf(object.getIntTotal()));
							_txtResultat.setVisibility(View.GONE);
						}
					}
					if(listener != null) {
						listener.onGetNumNovesNotificacionsFinishedListener(object.getIntTotal());
					}
				} catch (Exception e) {
					Log.e("PushNotificationManager", "Error: " + e.getLocalizedMessage(), e);
				}
			}
		});
	}

	protected abstract GCMIntentServiceBase getGCM();

	public void registerAppForPushNotifications() {
		String APP_TOKEN_ID = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);
		String GCM_SENDER_ID = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_GCM_SENDER_ID);
		String URL_PUSH_REGISTER = mContext.getResources().getString(R.string.smodpushnotifications_url_base_webservices)
				+ mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_1_register);
		String RETURN_ACTIVITY = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_RETURN_ACTIVITY);

		Class receiverClass = getGCM().getReceiverClass();
		// Guardem les classes receiver a les preferences. Ho fem per poder-les recuperar en cas que l'app estigui tancada i siguin classes no estàndards
		PreferencesSPush.setNotificationsListActivity(mContext, receiverClass);

		PreferencesSPush.setAppTokenID(mContext, APP_TOKEN_ID);
		PreferencesSPush.setWSBaseUrl(mContext, mContext.getResources().getString(R.string.smodpushnotifications_url_base_webservices));
		PreferencesSPush.setReturnClassPackage(mContext, RETURN_ACTIVITY);

		// Iniciem el registre de l'app. Aquesta crida s'ha de posar sempre a l'activitat que volem que s'encarregui de registrar les push
		getGCM().registerAppForPushNotifications(mContext, APP_TOKEN_ID, GCM_SENDER_ID, URL_PUSH_REGISTER, false, receiverClass);

		saveDateFirstExecution();
	}

	public void registerAppForPushNotifications(String _strAppTokenID, String _strGCMSenderID, String _strUrlBaseWS, boolean _username_auto) {
		registerAppForPushNotifications(_strAppTokenID, _strGCMSenderID, _strUrlBaseWS, _username_auto, "");
	}

	public void registerAppForPushNotifications(String _strAppTokenID, String _strGCMSenderID, String _strUrlBaseWS, boolean _username_auto, String username) {
//		String RETURN_ACTIVITY = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_RETURN_ACTIVITY);

		Class receiverClass = getGCM().getReceiverClass();
		String RETURN_ACTIVITY = receiverClass.getCanonicalName();

		// Guardem les classes receiver a les preferences. Ho fem per poder-les recuperar en cas que l'app estigui tancada i siguin classes no estàndards
		PreferencesSPush.setNotificationsListActivity(mContext, receiverClass);

		PreferencesSPush.setAppTokenID(mContext, _strAppTokenID);
		PreferencesSPush.setWSBaseUrl(mContext, _strUrlBaseWS);
//		PreferencesSPush.setReturnClassPackage(mContext, RETURN_ACTIVITY);
		PreferencesSPush.setReturnClassPackage(mContext, mContext.getString(R.string.SMODPUSHNOTIFICATIONS_RETURN_ACTIVITY));

		String strUrlRegistre = _strUrlBaseWS + mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_1_register);

		// Iniciem el registre de l'app. Aquesta crida s'ha de posar sempre a l'activitat que volem que s'encarregui de registrar les push
		getGCM().registerAppForPushNotifications(mContext, _strAppTokenID, _strGCMSenderID, strUrlRegistre, receiverClass, _username_auto, username, null);

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
		getGCM().registerAppForPushNotifications(mContext, _strAppTokenID, _strGCMSenderID, strUrlRegistre,  _username_auto, _receiverClass);

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
		getGCM().registerAppForPushNotifications(mContext, _strAppTokenID, _strGCMSenderID, strUrlRegistre, _receiverClass, _username_auto, "",_mHandler);

		saveDateFirstExecution();
	}


	protected void saveDateFirstExecution() {
		//String strDate = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(getResources().getString(R.string.PREFERENCE_INSTALLATION_DATE), "");
		String strDate = PreferencesSPush.getAppInstallationDate(mContext);
		if (strDate.equals("")) {
			try {
				long installed = mContext.getPackageManager().getPackageInfo("com.sixtemia.spushnotificationsdroid", 0).firstInstallTime;
				//System.out.println(getDate(82233213123L, "dd/MM/yyyy hh:mm:ss.SSS"));
				//Log.d(TAG, "INSTALADA: " + SUtils.getDate(installed, "yyyyMMddHHmmss"));
				PreferencesSPush.setAppInstallationDate(mContext, SDateUtils.getDateUTC(installed, "yyyyMMddHHmmss"));
			} catch (Exception e) {
				PreferencesSPush.setAppInstallationDate(mContext, SDateUtils.getDateUTC(System.currentTimeMillis(), "yyyyMMddHHmmss"));
			}
		}
	}

}