package com.sixtemia.sbaseobjects.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"ConstantConditions", "SameParameterValue", "SameReturnValue", "unused"})
public class Preferences {
	static final String TAG = "Prefs";

	private static String getPrefName() {
		return "preferences";
	}

	private static SharedPreferences get(Context context) {
		if (context == null) {
			Log.e(TAG, "Null Context en getSharedPreferences");
		}
		try {
			return context.getSharedPreferences(getPrefName(), 0);
		} catch (Exception ex) {
			Log.e(TAG, "Error: " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		return null;
	}

	public static boolean contains(Context context, String key) {
		SharedPreferences preferences = get(context);
		return preferences != null && preferences.contains(key);
	}

	public static void put(Context context, String key, int value) {
		SharedPreferences settings = context.getSharedPreferences(getPrefName(), 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.apply();
	}

	public static void put(Context context, String key, boolean value) {
		int intVal = 0;
		if (value) {
			intVal = 1;
		}
		put(context, key, intVal);
	}

	public static void put(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(getPrefName(), 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public static void put(Context context, String key, long value) {
		SharedPreferences settings = context.getSharedPreferences(getPrefName(), 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		editor.apply();
	}

	@SuppressLint("NewApi")
	public static void put(Context context, String key, Set<String> value) {
		if (Build.VERSION.SDK_INT >= 11) {
			SharedPreferences settings = context.getSharedPreferences(getPrefName(), 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putStringSet(key, value);
			editor.apply();
		} else {
			String val = "";
			for (String s : value) {
				if (val.length() != 0) {
					val += ";";
				}
				val += s;
			}
			SharedPreferences settings = context.getSharedPreferences(getPrefName(), 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(key, val);
			editor.apply();
		}
	}

	public static String getString(Context context, String key, String defaultValue) {
		try {
			return get(context).getString(key, defaultValue);
		} catch (Exception ex) {
			return defaultValue;
		}
	}

	@SuppressLint("NewApi")
	public static HashSet<String> getStringSet(Context context, String key, HashSet<String> defaultValue) {
		if (Build.VERSION.SDK_INT >= 11) {
			try {
				return (HashSet<String>)get(context).getStringSet(key, defaultValue);
			} catch (Exception ex) {
				return defaultValue;
			}
		} else {
			try {
				String tmp = get(context).getString(key, "");
				String[] tmpa = tmp.split(";");
				HashSet<String> res = new HashSet<>();
				Collections.addAll(res, tmpa);
				return res;
			} catch (Exception ex) {
				return defaultValue;
			}
		}
	}

	public static int getInt(Context context, String key, int defaultValue) {
		try {
			return get(context).getInt(key, defaultValue);
		} catch (Exception ex) {
			return defaultValue;
		}
	}

	public static long getLong(Context context, String key, long defaultValue) {
		try {
			return get(context).getLong(key, defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static boolean getBool(Context context, String key, boolean defaultValue) {
		try {
			int defValue = 0;
			if (defaultValue) {
				defValue = 1;
			}
			return getInt(context, key, defValue) == 1;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static void putJson(Context context, String key, Object o) {
		String s = new Gson().toJson(o);
		put(context, key, s);
	}

	public static <T> T getJson(Context context, String key, Class<T> c) {
		String s = getString(context, key, null);
		if(TextUtils.isEmpty(s)) {
			return null;
		} else {
			try {
				return new Gson().fromJson(s, c);
			} catch (Exception ex) {
				Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
				return null;
			}
		}
	}

	public static void remove(Context context, String string) {
		SharedPreferences settings = context.getSharedPreferences(getPrefName(), 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(string);
		editor.apply();
	}
}
