package com.boka2.spushnotificationsdroid.objects;

import android.app.ActionBar;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.boka2.classes.SPushTypefaceSpan;
import com.boka2.spushnotificationsdroid.R;
import com.boka2.spushnotificationsdroid.styles.SPushNotificationsStyleManager;

public class SPushFragmentActivity extends FragmentActivity {

	public Context mContext;

	protected static final String TAG = "spush";

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;

		// Si ho fem aquí no funciona bé sempre. S'ha de fer al onCreate de cada activity després d'assignar el text al títol
		//applyStylesToActionBar();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	
	protected void applyStylesToActionBar() {
		// Apliquem la typo
		actionbarStyle(SPushNotificationsStyleManager.Current(mContext).getStyle().getNavigationStyle().getStrFontFamily(),
				SPushNotificationsStyleManager.Current(mContext).getStyle().getNavigationStyle().getTitleColor(), SPushNotificationsStyleManager
						.Current(mContext).getStyle().getNavigationStyle().getFontSize());
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(SPushNotificationsStyleManager.Current(mContext).getStyle().getNavigationStyle().getBackgroundColor()));
	}


	public void actionbarStyle(String _strFontName, int _intFontColor, float _fontSize) {
		try {
			ActionBar actionBar = getActionBar();

			String strTitle = actionBar.getTitle().toString();
			SpannableString s = new SpannableString(strTitle);

			String strFont;
			if (_strFontName != null && !_strFontName.equals("")) {
				strFont = SPushTypefaceSpan.getTypefaceExtension(mContext, _strFontName);
				s.setSpan(new SPushTypefaceSpan(this, strFont, _intFontColor, _fontSize), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

			// Update the action bar title with the TypefaceSpan instance
			actionBar.setTitle(s);
		} catch (Exception e) {
			Log.d(TAG, "SNewsActivity actionbarStylized() exception: " + e.getMessage());
		}

	}

	//	public void actionbarStyle(String _strFontName, int _intFontColor, float _fontSize) {
	//		try {
	//			ActionBar actionBar = getSupportActionBar();
	//
	//			String strTitle = actionBar.getTitle().toString();
	//			SpannableString s = new SpannableString(strTitle);
	//
	//			String strFont;
	//			if (_strFontName != null && !_strFontName.equals("")) {
	//				strFont = "fonts/" + _strFontName + ".ttf";
	//			} else {
	//				strFont = getString(R.string.stoppo_font_actionbar_title);
	//			}
	//
	//			s.setSpan(new SPushTypefaceSpan(this, strFont, _intFontColor, _fontSize), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	//
	//			// Update the action bar title with the TypefaceSpan instance
	//			actionBar.setTitle(s);
	//		} catch (Exception e) {
	//			Log.d(TAG, "SPushFragmentActivity actionbarStylized() exception: " + e.getMessage());
	//		}
	//
	//	}

	public void onStart() {
		super.onStart();

		if (!isDebugging()) {
			// Release mode
			FlurryAgent.onStartSession(mContext, getResources().getString(R.string.STOPPO_FLURRY_API_KEY));
		} else {
			// Debug mode
			FlurryAgent.onStartSession(mContext, getResources().getString(R.string.STOPPO_FLURRY_API_KEY_DEBUG));
		}

	}

	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(mContext);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void Logd(String log) {
		Log.d(TAG, log);
	}

	/*
	 * Retorna true si l'app est� en mode debug o false si est� en mode release. 
	 * Ho utilitzarem per saber la key de flurry i maps que ha d'utilitzar
	 */
	public boolean isDebugging() {
		boolean debuggable = false;

		PackageManager pm = this.getPackageManager();
		try {
			ApplicationInfo appinfo = pm.getApplicationInfo(this.getPackageName(), 0);
			debuggable = (0 != (appinfo.flags &= ApplicationInfo.FLAG_DEBUGGABLE));
		} catch (NameNotFoundException e) {
			/*debuggable variable will remain false*/
		}

		return debuggable;
	}


}
