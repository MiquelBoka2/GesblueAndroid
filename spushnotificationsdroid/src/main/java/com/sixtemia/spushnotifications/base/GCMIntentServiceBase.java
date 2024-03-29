package com.sixtemia.spushnotifications.base;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.sixtemia.sbaseobjects.SWebViewActivity;
import com.sixtemia.sbaseobjects.SWebViewFragment;
import com.sixtemia.spushnotifications.classes.ConstantsSPush;
import com.sixtemia.spushnotifications.classes.PreferencesSPush;
import com.sixtemia.spushnotifications.datamanager.DataManagerSPushNotifications;
import com.sixtemia.spushnotifications.datamanager.SDataManagerListener;
import com.sixtemia.spushnotifications.model.SModPushRegisterResult;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.sixtemia.spushnotifications.classes.GCMIntentService.mHandlerPostRegister;
import static com.sixtemia.spushnotifications.classes.GCMIntentService.receiverClass;


public abstract class GCMIntentServiceBase extends GCMBaseIntentService {
	public static String TAG = "GCMIntentServiceBase";

	public static String EXTRA_CODE = "strCode";
	public static String EXTRA_VALUE = "strValue";
	public static String EXTRA_PUSH_WEBVIEW_URL = "webview";

	public static String APP_TOKEN_ID = "";
	public static String GCM_SENDER_ID = "";
	public static String PREFERENCE_ID_PUSH_NOTIF = "PreferencePushNotificationsId";
	public static String URL_PUSH_REGISTER = "";
	public static String USERNAME = "";
	public static boolean username_auto = true;
	private static OnRegisterListener mListener;
	//private String SENDER_ID = "";

	public GCMIntentServiceBase() {
		super();
	}

	public GCMIntentServiceBase(String senderId) {
		super(senderId);
		//SENDER_ID = senderId;
		Log.d("GCMIntentService", "senderID: " + senderId);
	}

	public abstract Class getReceiverClass();
	protected abstract String getReceiverClassParamComingFromPush();

	protected Class getWebviewClass() {
		return SWebViewActivity.class;
	}
	protected String getWebviewParamUrl() {
		return SWebViewActivity.KEY_URL;
	}
	protected String getWebviewParamConfig() {
		return SWebViewActivity.KEY_CONFIG;
	}
	protected Parcelable getWebviewConfig() {
		return new SWebViewFragment.Config(getApplicationContext());
	}

	protected abstract int getSmallNotificationIcon();
	protected abstract int getLargeNotificationIcon();

	public static void setRegisterListener(OnRegisterListener mListener) {
		GCMIntentServiceBase.mListener = mListener;
	}

	protected void onRegisterDone(String sixtemiaDID, String googleDID) {
		if(mListener != null) {
			mListener.onRegisterDone(sixtemiaDID, googleDID);
		}
	}

	protected void onRegisterFailed(Exception e) {
		if(mListener != null) {
			mListener.onRegisterFailed(e);
		}
	}

	@Override
	protected void onError(Context _context, String _errorId) {
		//Log.d("onError", _errorId);
	}

	@Override
	protected boolean onRecoverableError(Context _context, String _errorId) {
		//Log.d("onRecoverableError", _errorId);
		return false;
	}

	@Override
	protected void onMessage(Context _context, Intent _intent) {
		//Log.d(TAG, "Received message: " + String.valueOf(_intent));
		handleMessage(_context, _intent);
	}

	@Override
	protected void onRegistered(Context _context, String _regId) {
		//Log.d("onRegistered", _regId);
		handleRegistration(_context, _regId);
	}

	@Override
	protected void onUnregistered(Context _context, String _regId) {
		//Log.d("onUnregistered", _regId);
	}

	/**
	 * Gets the sender ids.
	 *
	 *
	 By default, it returns the sender ids passed in
	 * the constructor, but it could be overridden to
	 * provide a dynamic sender id.
	 *
	 * @throws IllegalStateException if sender id was
	 * not set on constructor.
	 */
	@Override
	protected String[] getSenderIds(Context context) {
		String[] ids = new String[1];
		ids[0] = GCM_SENDER_ID;
		return ids;
	}

	/*
	 * M�todes corresponents a accions
	 */
	@SuppressLint("NewApi")
	private void handleMessage(Context _context, Intent _intent) {

		//Log.d("GCM", "Received message: " + String.valueOf(_intent));
		createNotification(_context, _intent);
	}

	private void createNotification(Context _context, Intent _intent) {
		try {
			NotificationManager nm = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);

			Intent notificationIntent;

			if (_intent.getExtras().containsKey(EXTRA_CODE) && _intent.getExtras().containsKey(EXTRA_VALUE)
					&& !TextUtils.isEmpty(_intent.getExtras().getString(EXTRA_CODE))
					&& !TextUtils.isEmpty(_intent.getExtras().getString(EXTRA_VALUE))
					&& _intent.getExtras().getString(EXTRA_CODE).equals(EXTRA_PUSH_WEBVIEW_URL)) {
				// Quan toquem la push s'obrirà un webview

				// Obtenim la classe que volem obrir pel webview
				Class webview = PreferencesSPush.getNotificationsWebviewActivity(_context);

				if (webview == null) {
					webview = getWebviewClass();
				}

				notificationIntent = new Intent(_context, webview);

				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				notificationIntent.putExtra(ConstantsSPush.EXTRA_COMING_FROM_PUSH, true);

				if(getWebviewConfig() != null) {
					notificationIntent.putExtra(getWebviewParamConfig(), getWebviewConfig());
				}

				String strUrlWebview = _intent.getExtras().getString(EXTRA_VALUE);
				notificationIntent.putExtra(getWebviewParamUrl(), strUrlWebview);

			} else {
				// Push normal

				// Obtenim la classe que volem obrir pel webview
				Class receiver = PreferencesSPush.getNotificationsListActivity(_context);

				if (receiver == null) {
					receiver = getReceiverClass();
				}

				notificationIntent = new Intent(_context, receiver);

				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				notificationIntent.putExtra(getReceiverClassParamComingFromPush(), true);
			}


			PendingIntent contentIntent = PendingIntent.getActivity(_context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

//			String strTitle = _context.getResources().getString(R.string.app_name);
			String strMessage = _intent.getStringExtra("message");

			NotificationCompat.Builder notification = new NotificationCompat.Builder(_context);

			notification.setContentIntent(contentIntent).setSmallIcon(getSmallNotificationIcon())
					.setLargeIcon(BitmapFactory.decodeResource(_context.getResources(), getLargeNotificationIcon())).setTicker(strMessage)
					.setWhen(System.currentTimeMillis()).setAutoCancel(true)/*.setContentTitle(strTitle)*/.setContentText(strMessage)
					.setVibrate(new long[] { 100, 500 });

			try {
				Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(_context, RingtoneManager.TYPE_NOTIFICATION);
				notification.setSound(alarmSound);
			} catch (Exception e) {
			}

			notification.setStyle(new NotificationCompat.BigTextStyle().bigText(strMessage));

			Notification n = notification.build();

			nm.notify(1, n);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void handleRegistration(Context _context, String _registrationID) {

		//Log.d(TAG, "Received registration ID");

		PreferenceManager.getDefaultSharedPreferences(_context).edit().putString(PREFERENCE_ID_PUSH_NOTIF, _registrationID).commit();

		// Send the registration ID to the 3rd party site that is sending the messages.
		// When done, remember that all registration is done.

		//http://www.sixtemialabs.com/push/register_device.php?deviceid=123&devicetoken=48886&deviceos=android&bundleid=com.sixtemia.com
		sendRegistrationToPushServer(_context, _registrationID);
	}

	protected abstract boolean isEnabled(Context c);

	protected void sendRegistrationToPushServer(Context context, String registrationID) {

		// Enviem per POST les dades per registrar l'app al servidor de push

		String _url = URL_PUSH_REGISTER;

		if(!_url.equals("")) {

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(_url);

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				//nameValuePairs.add(new BasicNameValuePair("deviceid", getPhoneIMEI(context)));
				nameValuePairs.add(new BasicNameValuePair("sdid", getDeviceIdentifier(context)));
				nameValuePairs.add(new BasicNameValuePair("did", registrationID));
				nameValuePairs.add(new BasicNameValuePair("platform", "android"));
				nameValuePairs.add(new BasicNameValuePair("token", APP_TOKEN_ID));
				//nameValuePairs.add(new BasicNameValuePair("bundleid", getAppPackageName(context)));
				nameValuePairs.add(new BasicNameValuePair("lang", Locale.getDefault().getLanguage()));
				nameValuePairs.add(new BasicNameValuePair("username", USERNAME));
				nameValuePairs.add(new BasicNameValuePair("username_auto", String.valueOf(username_auto)));
				nameValuePairs.add(new BasicNameValuePair("enabled", isEnabled(context) ? "1" : "0"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

				try {
					Gson gson = new Gson();
					SModPushRegisterResult result = gson.fromJson(json, SModPushRegisterResult.class);
					if (result != null && !TextUtils.isEmpty(result.getStrSDID())) {
						PreferencesSPush.setSDID(context, result.getStrSDID());
						onSDIDReceived(context, result.getStrSDID());
						onRegisterDone(result.getStrSDID(), registrationID);
						return;
					}
				} catch (Exception e) {
					onRegisterFailed(e);
				}

			} catch (ClientProtocolException e) {
				Log.d(TAG, e.getMessage(), e);
				onRegisterFailed(e);
			} catch (IOException e) {
				Log.d(TAG, e.getMessage(), e);
				onRegisterFailed(e);
			}
			onRegisterFailed(null);
		}

	}

	/**
	 * Cridat quan es rep un SDID.
	 *
	 * @param c
	 * @param sdid
	 */
	protected abstract void onSDIDReceived(Context c, String sdid);
    
	/*private String getAppPackageName(Context context) {
		
		String strResultat = "";
		
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			strResultat = pinfo.packageName;
						
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return strResultat;
	}
	
	
	private String getPhoneIMEI(Context context) {
		
		String strResultat = "";
		
		try{
			
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			strResultat = telephonyManager.getDeviceId();
			
		} catch(Exception e) {
			// TODO: handle exception
		}
		
		return strResultat;
	}*/

	protected String getDeviceIdentifier(Context _context) {
		//String strResultat = "";
		// 		20140902 JBD Deixem d'utilitzar l'udid i comencem a utilitzar identificadors propis de sixtemia
		//		try {
		//
		//			TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		//
		//			if (telephonyManager.getDeviceId() != null) {
		//				strResultat = telephonyManager.getDeviceId(); // *** use for mobiles
		//			} else {
		//				strResultat = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); // *** use for tablets
		//			}
		//
		//		} catch (Exception e) {
		//			Log.d(TAG, e.getMessage());
		//		}
		// return strResultat;

		return PreferencesSPush.getSDID(_context);
	}

	/**
	 * S'ha de cridar des de l'activitat que registra les push notifications al server.
	 *
	 * @param _context
	 * @param _strAppTokenId Token de l'app facilitat per Google per poder regitrar per la recepci� de push
	 * @param _strSenderId  Identificaci� de l'emissor de push que podr� enviar a trav�s del sistema GCM de Google
	 * @param _strUrlPushRegister URL del servidor de push de Sixtemia
	 * @param _receiverClass Activity que s'ha d'obrir quan es premi la push rebuda
	 *
	 */
	public static void registerAppForPushNotifications(Context _context, String _strAppTokenId, String _strSenderId, String _strUrlPushRegister, boolean _username_auto,
	                                                   Class _receiverClass) {
//		APP_TOKEN_ID = _strAppTokenId;
//		GCM_SENDER_ID = _strSenderId;
//		URL_PUSH_REGISTER = _strUrlPushRegister;
//		receiverClass = _receiverClass;
//		USERNAME = "";
//		username_auto = _username_auto;
//
//		Log.i(TAG, "Registering to " + URL_PUSH_REGISTER);
//
//		GCMRegistrar.checkDevice(_context);
//		GCMRegistrar.checkManifest(_context);
//		final String regId = GCMRegistrar.getRegistrationId(_context);
//		GCMRegistrar.register(_context, GCM_SENDER_ID);

		registerAppForPushNotifications(_context, _strAppTokenId, _strSenderId, _strUrlPushRegister, _receiverClass, _username_auto, "", null);
	}

	/**
	 * S'ha de cridar des de l'activitat que registra les push notifications al server.
	 *
	 * @param _context
	 * @param _strAppTokenId Token de l'app facilitat per Google per poder regitrar per la recepci� de push
	 * @param _strSenderId  Identificaci� de l'emissor de push que podr� enviar a trav�s del sistema GCM de Google
	 * @param _strUrlPushRegister URL del servidor de push de Sixtemia
	 * @param _receiverClass Activity que s'ha d'obrir quan es premi la push rebuda
	 * @param _strUsername Nom d'usuari en cas que a l'app es faci login
	 *
	 */
	public static void registerAppForPushNotifications(Context _context, String _strAppTokenId, String _strSenderId, String _strUrlPushRegister, Class _receiverClass, boolean _username_auto, String _strUsername, Handler _mHandler) {
		APP_TOKEN_ID = _strAppTokenId;
		GCM_SENDER_ID = _strSenderId;
		URL_PUSH_REGISTER = _strUrlPushRegister;
		receiverClass = _receiverClass;
		USERNAME = _strUsername;
		username_auto = _username_auto;
		if(_mHandler != null) {
			mHandlerPostRegister = _mHandler;
		}

		Log.i(TAG, "Registering to " + URL_PUSH_REGISTER);

		GCMRegistrar.checkDevice(_context);
		GCMRegistrar.checkManifest(_context);
		final String regId = GCMRegistrar.getRegistrationId(_context);
		GCMRegistrar.register(_context, GCM_SENDER_ID);
	}

	public static void unregisterPushNotifications(Context _context, String _strAppTokenId) {
		DataManagerSPushNotifications dataManager = new DataManagerSPushNotifications(_context);

		dataManager.unregisterUserPush(_strAppTokenId, new SDataManagerListener() {

			@Override
			public void onError(Object result) {
				Log.e("SModPush", "- Error on unregisterPushNotifications");
			}

			@Override
			public void onCompletion(Object result) {
				SModPushRegisterResult push_result = (SModPushRegisterResult)result;
				Log.e("SModPush", "- Complete ");
			}
		});
	}

	public static abstract class OnRegisterListener {
		public abstract void onRegisterDone(String sixtemiaDID, String googleDID);
		public abstract void onRegisterFailed(@Nullable Exception e);
	}

}