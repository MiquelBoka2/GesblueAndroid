package com.boka2.spushnotificationsdroid;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.boka2.spushnotificationsdroid.classes.GCMIntentService;
import com.boka2.spushnotificationsdroid.classes.PreferencesSPush;
import com.boka2.spushnotificationsdroid.classes.SModPushNotificationsManagerListener;
import com.boka2.spushnotificationsdroid.datamanager.DataManagerSPushNotifications;
import com.boka2.spushnotificationsdroid.datamanager.SDataManagerListener;
import com.boka2.spushnotificationsdroid.db.DataBase;
import com.boka2.spushnotificationsdroid.model.SModPushPendingNotificationsCountResult;
import com.boka2.sutils.classes.SDateUtils;

public class SModPushNotificationsManager {

	private static Context mContext;
		
	private static SModPushNotificationsManager mCurrent = null;
	private static DataManagerSPushNotifications dataManager = null;
	
	
	// Inici Singleton
	public static SModPushNotificationsManager Current(Context _context) {
				
		if (mCurrent == null) {
			mCurrent = new SModPushNotificationsManager(_context);
		}
		else{ 
			mCurrent.setContext(_context);
		}

		if (dataManager == null) {
			dataManager = new DataManagerSPushNotifications(_context);
		}
				
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
		/*try {
			mCallback = (OnRequestFinishedListener) _context;
		} catch (ClassCastException e) {
			throw new ClassCastException(_context.toString() + " ha d'implementar OnRequestFinishedListener");
		}*/
				
		return mCurrent;
	}
	
	
	private SModPushNotificationsManager(Context _context) {
		mContext = _context;
	}
	// Fi Singleton
	
	
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
		GCMIntentService.registerAppForPushNotifications(mContext, APP_TOKEN_ID, GCM_SENDER_ID, URL_PUSH_REGISTER, receiverClass);

		saveDateFirstExecution();
	}

	public void registerAppForPushNotifications(String _strAppTokenID, String _strGCMSenderID, String _strUrlBaseWS) {
		//String APP_TOKEN_ID = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);
		//String GCM_SENDER_ID = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_GCM_SENDER_ID);
		//String URL_PUSH_REGISTER = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_URL_PUSH_REGISTER);
		String RETURN_ACTIVITY = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_RETURN_ACTIVITY);

		Class receiverClass = SModPushNotificationsListActivity.class;
		// Guardem les classes receiver a les preferences. Ho fem per poder-les recuperar en cas que l'app estigui tancada i siguin classes no estàndards
		PreferencesSPush.setNotificationsListActivity(mContext, receiverClass);

		PreferencesSPush.setAppTokenID(mContext, _strAppTokenID);
		PreferencesSPush.setWSBaseUrl(mContext, _strUrlBaseWS);
		PreferencesSPush.setReturnClassPackage(mContext, RETURN_ACTIVITY);

		String strUrlRegistre = _strUrlBaseWS + mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_1_register);

		// Iniciem el registre de l'app. Aquesta crida s'ha de posar sempre a l'activitat que volem que s'encarregui de registrar les push
		GCMIntentService.registerAppForPushNotifications(mContext, _strAppTokenID, _strGCMSenderID, strUrlRegistre, receiverClass);

		saveDateFirstExecution();
	}

	public void registerAppForPushNotifications(String _strAppTokenID, String _strGCMSenderID, String _strUrlBaseWS, Class _receiverClass,
			String _strReturnClassPackageName) {
		//String APP_TOKEN_ID = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);
		//String GCM_SENDER_ID = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_GCM_SENDER_ID);
		//String URL_PUSH_REGISTER = mContext.getResources().getString(R.string.SMODPUSHNOTIFICATIONS_URL_PUSH_REGISTER);
		//Class receiverClass = SModPushNotificationsListActivity.class;

		// Guardem les classes receiver a les preferences. Ho fem per poder-les recuperar en cas que l'app estigui tancada i siguin classes no estàndards
		PreferencesSPush.setNotificationsListActivity(mContext, _receiverClass);

		PreferencesSPush.setAppTokenID(mContext, _strAppTokenID);
		PreferencesSPush.setWSBaseUrl(mContext, _strUrlBaseWS);
		PreferencesSPush.setReturnClassPackage(mContext, _strReturnClassPackageName);

		String strUrlRegistre = _strUrlBaseWS + mContext.getResources().getString(R.string.smodpushnotifications_url_prefix_ws_1_1_register);

		// Iniciem el registre de l'app. Aquesta crida s'ha de posar sempre a l'activitat que volem que s'encarregui de registrar les push
		GCMIntentService.registerAppForPushNotifications(mContext, _strAppTokenID, _strGCMSenderID, strUrlRegistre, _receiverClass);

		saveDateFirstExecution();
	}

	private void saveDateFirstExecution() {

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
