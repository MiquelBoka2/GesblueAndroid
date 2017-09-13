package com.sixtemia.sutils.classes;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * Commonly used methods related to System information.
 * 
 * @author jaumebech
 * @version 1.0
 */
public class SSystemUtils {

	public static String DENSITAT_LDPI = "ldpi";
	public static String DENSITAT_MDPI = "mdpi";
	public static String DENSITAT_HDPI = "hdpi";
	public static String DENSITAT_XHDPI = "xhdpi";
	public static String DENSITAT_XXHDPI = "xxhdpi";
	public static String DENSITAT_XXXHDPI = "xxxhdpi";
	public static String DENSITAT_TV = "tv";

	/**
	 * Checks if the app is in debug mode or in release.
	 * 
	 * @param _context
	 *            The context the method is being called.
	 * @return true if the app is in debugging mode or false if it's in release
	 *         mode.
	 */
	public static boolean isDebugging(Context _context) {
		boolean debuggable = false;

		PackageManager pm = _context.getPackageManager();
		try {
			ApplicationInfo appinfo = pm.getApplicationInfo(_context.getPackageName(), 0);
			debuggable = (0 != (appinfo.flags &= ApplicationInfo.FLAG_DEBUGGABLE));
		} catch (NameNotFoundException e) {
			/* debuggable variable will remain false */
		}

		return debuggable;
	}

	/**
	 * Gets the Version Code of the app
	 * 
	 * @param _context
	 *            The context the method is being called.
	 * @return the Version Code of the app
	 */
	public static String getAppVersionCode(Context _context) {

		String strResultat = "";

		try {
			PackageInfo pinfo = _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0);
			strResultat = Integer.toString(pinfo.versionCode);

		} catch (Exception e) {
			//Do nothing.
		}

		return strResultat;
	}

	/**
	 * Gets the Version Name of the app
	 * 
	 * @param _context
	 *            The context the method is being called.
	 * @return the Version Name of the app
	 */
	public static String getAppVersionName(Context _context) {

		String strResultat = "";

		try {
			PackageInfo pinfo = _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0);
			strResultat = pinfo.versionName;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return strResultat;
	}

	/**
	 * Gets the Version Code of the app
	 * 
	 * @param _context
	 *            The context the method is being called.
	 * @return the Version Code of the app
	 */
	public static String getDeviceUniqueIdentifier(Context _context) {
		String strResultat = "";

		try {

			TelephonyManager telephonyManager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);

			if (telephonyManager.getDeviceId() != null) {
				strResultat = telephonyManager.getDeviceId(); // *** use for
																// mobiles
			} else {
				strResultat = Secure.getString(_context.getContentResolver(), Secure.ANDROID_ID); // ***
																									// use
																									// for
																									// tablets
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResultat;
	}

	/**
	 * Gets the screen's density of the device **DEPRECATED** Use
	 * getDeviceDensityDisplay(Context _context) instead.
	 * 
	 * @param _activity
	 *            The activity the method is being called.
	 * @return A string identifier of the screen density
	 */
	public static String getDeviceDensityDisplay(Activity _activity) {
		return getDeviceDensityDisplay((Context) _activity);
	}

	/**
	 * Gets the screen's density of the device
	 * 
	 * @param _context
	 *            A context of the app.
	 * @return A string identifier of the screen density
	 */
	public static String getDeviceDensityDisplay(Context _context) {
		String strResultat = "";

		DisplayMetrics metrics = _context.getResources().getDisplayMetrics();

		int dpi = metrics.densityDpi;
		if (dpi <= 140) {
			strResultat = DENSITAT_LDPI;
		} else if (dpi <= 199) {
			strResultat = DENSITAT_MDPI;
		} else if (dpi == DisplayMetrics.DENSITY_TV) {
			strResultat = DENSITAT_TV;
		} else if (dpi <= 319) {
			strResultat = DENSITAT_HDPI;
		} else if (dpi <= 340) {
			strResultat = DENSITAT_XHDPI;
		} else if (dpi <= 520) {
			strResultat = DENSITAT_XXHDPI;
		} else {
			strResultat = DENSITAT_XXXHDPI;
		}

		return strResultat;
	}

	/**
	 * Gets the screen's density of the device **DEPRECATED** Use
	 * getDeviceDensityDisplay(Context _context) instead.
	 * 
	 * @param _activity
	 *            The activity the method is being called.
	 * @return A string identifier of the screen density
	 */
	public static String getDeviceDensityDisplay(Activity _activity, String _defaultValue) {
		return getDeviceDensityDisplay((Context) _activity);
	}

	/**
	 * Gets the screen's density of the device
	 * 
	 * @param _context
	 *            A context of the app.
	 * @return A string identifier of the screen density
	 */
	public static String getDeviceDensityDisplay(Context _context, String _defaultValue) {
		return getDeviceDensityDisplay(_context);
	}

	/**
	 * Gets the screen's density identifier
	 * 
	 * @param _activity
	 *            The activity the method is being called.
	 * @return The screen density expressed as dots-per-inch. May be either
	 *         DisplayMetrics.DENSITY_LOW, DisplayMetrics.DENSITY_MEDIUM,
	 *         DisplayMetrics.DENSITY_HIGH, DisplayMetrics.DENSITY_XHIGH,
	 *         DisplayMetrics.DENSITY_XXHIGH or DisplayMetrics.DENSITY_TV
	 */
	public static int getDeviceDensityDisplayID(Activity _activity) {

		DisplayMetrics metrics = new DisplayMetrics();
		_activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		return metrics.densityDpi;
	}

	/**
	 * Checks if the current device is a tablet.
	 * 
	 * @param _context
	 *            The context the method is being called.
	 * @return true if the device is a tablet. False otherwise.
	 */
	public static boolean isTablet(Context _context) {
		boolean result = (_context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
		return result;
	}

}