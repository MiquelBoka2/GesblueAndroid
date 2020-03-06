package com.boka2.sutils.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.util.Log;

/*
 * Methods used to know the network state. 
 * Requires permission "android.permission.ACCESS_NETWORK_STATE" to be specified in the Manifest.
 * 
 * @author jaumebech
 * @version 1.0
 */
public class SNetworkUtils {

	public static String WIFI = "Wifi";
	public static String MOBILE = "Mobile 3G";
	public static String NO_NETWORK = "No Network";

	/*
	 * Checks if there is a connection is available
	 * @param _context The context the method is being called. 
	 * @return true if it's connected to internet. False otherwise.
	 */
	public static boolean isInternetConnectionAvailable(Context _context)
	{
		ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		final android.net.NetworkInfo netInfo = cm.getActiveNetworkInfo();
		
		if(netInfo != null)
			return netInfo.isConnectedOrConnecting();
		else
			return false;
		
		
		/*
		ConnectivityManager connMgr = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
 
		final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		boolean wifiAvailable = wifi.isAvailable();
		boolean mobileAvailable = mobile.isAvailable();
		
		if( wifi.isAvailable() || mobile.isAvailable()) {
			return true;
		}
		else{
			return false;
		}*/
	
	}

	/*
	 * Get the type of the connection available actually
	 * @param _context The context the method is being called. 
	 * @return the type of the connection available
	 */
	public static String getInternetConnectionType(Context _context) {
		ConnectivityManager connMgr = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi != null && wifi.isConnected()) {
			Log.d("RETURNED: ", WIFI);
			return WIFI;
		} else if (mobile != null && mobile.isConnected()) {
			Log.d("RETURNED: ", MOBILE);
			return MOBILE;
		} else {
			Log.d("RETURNED: ", NO_NETWORK);
			return NO_NETWORK;
		}
	}

	/*
	 * Check if the GPS is enabled
	 * @param _context The context the method is being called.
	 * @return true if the GPS is enabled. False otherwise.
	 */
	public static boolean checkGPSAvailableWithAlert(Context _context) {
		final LocationManager manager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return false;
		}

		return true;
	}

	/*
	 * Check if the GPS is enabled and shows an alert to turn on if it's disabled
	 * @param _context The context the method is being called. 
	 * @param _strAlertTitle Alert's title
	 * @param _strAlertText Alert's text
	 * @param _strAlertButtonOkText Alert's positive button text
	 * @param _strAlertButtonCancelText Alert's negative button text
	 */
	public static void checkGPSAvailableWithAlert(Context _context, String _strAlertTitle, String _strAlertText, String _strAlertButtonOkText,
			String _strAlertButtonCancelText) {
		final LocationManager manager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			showAlertMessageNoGps(_context, _strAlertTitle, _strAlertText, _strAlertButtonOkText, _strAlertButtonCancelText);
		}

	}

	/*
	 * 
	 * @param _context The context the method is being called. 
	 * @param _strTitle Alert's title
	 * @param _strText Alert's text
	 * @param _strButtonOkText  Alert's positive button text
	 * @param _strButtonCancelText Alert's negative button text
	 */
	private static void showAlertMessageNoGps(final Context _context, String _strTitle, String _strText, String _strButtonOkText,
			String _strButtonCancelText) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		builder.setMessage(_strText).setTitle(_strTitle).setCancelable(false)
				.setPositiveButton(_strButtonOkText, new DialogInterface.OnClickListener() {
					public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
						_context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				}).setNegativeButton(_strButtonCancelText, new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

}
