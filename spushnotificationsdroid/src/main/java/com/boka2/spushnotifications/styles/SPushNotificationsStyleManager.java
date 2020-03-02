package com.boka2.spushnotifications.styles;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class SPushNotificationsStyleManager {

	private static SPushNotificationsStyleManager mCurrent = null;
	private StyleSPushNotifications style;

	public StyleSPushNotifications getStyle() {

		if (style == null) {
			style = new StyleSPushNotifications();
		}

		return style;
	}

	public void setStyle(StyleSPushNotifications style) {
		this.style = style;
	}

	// Inici Singleton
	public static SPushNotificationsStyleManager Current(Context _context) {

		if (mCurrent == null) {
			mCurrent = new SPushNotificationsStyleManager(_context);
		}

		return mCurrent;
	}

	// Fi Singleton

	/**
	 * Mètode utilitzat per inicialitzar l'objecte en cas de voler-lo inicialitzar abans d'utilitzar-lo.
	 * @param _context
	 */
	public static void init(Context _context) {
		Current(_context);
	}

	private SPushNotificationsStyleManager(Context _context) {
		initStyles(_context);
	}

	private void initStyles(Context _context) {
		String json = getJSONStyles(_context);

		style = getStyleObject(json);
	}

	private StyleSPushNotifications getStyleObject(String _json) {
		if (_json != null && !"".equals(_json)) {
			try {
				Gson gson = new Gson();
				return gson.fromJson(_json, StyleSPushNotifications.class);
			} catch (Exception e) {
				e.printStackTrace();
				return new StyleSPushNotifications();
			}
		}

		return null;
	}

	public String getJSONStyles(Context _context) {
		String strFileName = "style/SPushNotificationsStyle.json";
		String jsonString = "";
		try {
			InputStream is = _context.getAssets().open(strFileName); //getResources().openRawResource(R.raw.json_file);
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			jsonString = writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}


}
