package com.sixtemia.spushnotificationsdroid.objects;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

public class FontManager {

	public static final String LOG_TAG = "FONT_MANAGER";
	public static final String TYPEFACE_FOLDER = "fonts";
	public static final String TYPEFACE_EXTENSION = ".ttf";
	public static final String TYPEFACE_EXTENSION_ALT = ".otf";

	private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<String, Typeface>(
			4);

	public static Typeface getTypeFace(Context context, String fileName) {
		Typeface tempTypeface = sTypeFaces.get(fileName);

		if (tempTypeface == null) {
			try{
			String fontPath = new StringBuilder(TYPEFACE_FOLDER).append('/').append(fileName).append(TYPEFACE_EXTENSION).toString();
			tempTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), fontPath);
			}catch(Exception e) {
				String fontPath = new StringBuilder(TYPEFACE_FOLDER).append('/').append(fileName).append(TYPEFACE_EXTENSION_ALT ).toString();
				tempTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), fontPath);
			}
			sTypeFaces.put(fileName, tempTypeface);
			Log.d(LOG_TAG, "Typeface now cached");
		} else {
			Log.d(LOG_TAG, "Typeface returned from cache");
		}

		return tempTypeface;
	}
}