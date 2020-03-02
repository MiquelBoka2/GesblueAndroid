package com.boka2.spushnotificationsdroid.classes;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.boka2.spushnotificationsdroid.R;
import com.boka2.spushnotificationsdroid.SModPushNotificationsListActivity;
import com.boka2.spushnotificationsdroid.SModPushNotificationsWebviewActivity;
import com.boka2.spushnotificationsdroid.datamanager.DataManagerSPushNotifications;
import com.boka2.spushnotificationsdroid.datamanager.SDataManagerListener;
import com.boka2.spushnotificationsdroid.model.SModPushRegisterResult;

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


public class GCMIntentService extends GCMBaseIntentService {
    public static String TAG = "GCMIntentService";

	public static String EXTRA_CODE = "strCode";
	public static String EXTRA_VALUE = "strValue";
	public static String EXTRA_PUSH_WEBVIEW_URL = "webview";

	public static String APP_TOKEN_ID = "";
	public static String GCM_SENDER_ID = "";
	public static String PREFERENCE_ID_PUSH_NOTIF = "PreferencePushNotificationsId";
	public static String URL_PUSH_REGISTER = "";
	public static String USERNAME = "";
	public static Class receiverClass = SModPushNotificationsListActivity.class; // ATENCI�! Inicialitzar sempre pq �s una variable est�tica i si no la inicialitzem petaria quan es rebi una push amb l'app apagada
	public static Class webviewClass = SModPushNotificationsWebviewActivity.class;

	//private String SENDER_ID = "";

	public GCMIntentService() {
		super();
	}
    
    public GCMIntentService(String senderId) {
        super(senderId);
		//SENDER_ID = senderId;
		Log.d("GCMIntentService", "senderID: " + senderId);
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
					webview = webviewClass;
				}

				notificationIntent = new Intent(_context, webview);

				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				notificationIntent.putExtra(ConstantsSPush.EXTRA_COMING_FROM_PUSH, true);

				String strUrlWebview = _intent.getExtras().getString(EXTRA_VALUE);
				notificationIntent.putExtra(SModPushNotificationsWebviewActivity.BUNDLE_WEBVIEW_URL, strUrlWebview);

			} else {
				// Push normal

				// Obtenim la classe que volem obrir pel webview
				Class receiver = PreferencesSPush.getNotificationsListActivity(_context);

				if (receiver == null) {
					receiver = receiverClass;
				}

				notificationIntent = new Intent(_context, receiver);

				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				notificationIntent.putExtra(ConstantsSPush.EXTRA_COMING_FROM_PUSH, true);
			}
			

			PendingIntent contentIntent = PendingIntent.getActivity(_context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

//			String strTitle = _context.getResources().getString(R.string.app_name);
			String strMessage = _intent.getStringExtra("message");

			NotificationCompat.Builder notification = new NotificationCompat.Builder(_context);

			notification.setContentIntent(contentIntent).setSmallIcon(R.drawable.ic_launcher)
					.setLargeIcon(BitmapFactory.decodeResource(_context.getResources(), R.drawable.ic_launcher)).setTicker(strMessage)
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
    
    
    private void sendRegistrationToPushServer(Context context, String registrationID) {
    	
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
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        
				String json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

				try {
					Gson gson = new Gson();
					SModPushRegisterResult result = gson.fromJson(json, SModPushRegisterResult.class);
					if (result != null && !TextUtils.isEmpty(result.getStrSDID())) {
						PreferencesSPush.setSDID(context, result.getStrSDID());
					}
				} catch (Exception e) {

				}
		        
		    } catch (ClientProtocolException e) {
		    	Log.d(TAG, e.getMessage());
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		    	Log.d(TAG, e.getMessage());
		        // TODO Auto-generated catch block
		    }
			
		}	
		
	}
    
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

	private String getDeviceIdentifier(Context _context) {
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
	public static void registerAppForPushNotifications(Context _context, String _strAppTokenId, String _strSenderId, String _strUrlPushRegister,
			Class _receiverClass) {
		
		APP_TOKEN_ID = _strAppTokenId;
		GCM_SENDER_ID = _strSenderId;
		URL_PUSH_REGISTER = _strUrlPushRegister;
		receiverClass = _receiverClass;
		USERNAME = "";

		GCMRegistrar.checkDevice(_context);
		GCMRegistrar.checkManifest(_context);
		final String regId = GCMRegistrar.getRegistrationId(_context);
		GCMRegistrar.register(_context, GCM_SENDER_ID);
		/*if (regId.equals("")) {
		  GCMRegistrar.register(this, SENDER_ID);
		} else {
		  Log.v(TAG, "Already registered");
		}*/
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
	public static void registerAppForPushNotifications(Context _context, String _strAppTokenId, String _strSenderId, String _strUrlPushRegister,
			Class _receiverClass, String _strUsername) {

		APP_TOKEN_ID = _strAppTokenId;
		GCM_SENDER_ID = _strSenderId;
		URL_PUSH_REGISTER = _strUrlPushRegister;
		receiverClass = _receiverClass;
		USERNAME = _strUsername;

		GCMRegistrar.checkDevice(_context);
		GCMRegistrar.checkManifest(_context);
		final String regId = GCMRegistrar.getRegistrationId(_context);
		GCMRegistrar.register(_context, GCM_SENDER_ID);
		/*if (regId.equals("")) {
		  GCMRegistrar.register(this, SENDER_ID);
		} else {
		  Log.v(TAG, "Already registered");
		}*/
	}

	public static void unregisterPushNotifications(Context _context, String _strAppTokenId) {
		DataManagerSPushNotifications dataManager = new DataManagerSPushNotifications(_context);
		
		dataManager.unregisterUserPush(_strAppTokenId, new SDataManagerListener() {

			@Override
			public void onError(Object result) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCompletion(Object result) {
				// TODO Auto-generated method stub

			}
		});
	}
    
    
}