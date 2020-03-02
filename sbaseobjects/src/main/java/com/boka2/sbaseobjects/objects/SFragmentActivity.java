package com.boka2.sbaseobjects.objects;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.boka2.sbaseobjects.tools.Preferences;
import com.boka2.sbaseobjects.tools.TypefaceSpan;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Boka2.
 */
public class SFragmentActivity extends AppCompatActivity {
	public static final String PREF_LOCALE = "appLocale";

	public Context mContext;
	public Resources mResources;
	private boolean mRunning = false;
	private boolean mLanguageChanged = false;

	protected String getTag() {
		return "SFragment";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
		} catch (Exception ex) {
			Log.e(getClass().getSimpleName(), "Error OnCreate: " + ex.getLocalizedMessage(), ex);
		}
		mContext = this;
		configLocale();
		mResources = getResources();
		mRunning = true;
	}

	public static Locale getSelectedLocale(Context c) {
		return new Locale(Preferences.getString(c, PREF_LOCALE, "es"));
	}

	public void removeStatusBarPadding(View v) {
		v.setPadding(0, 0, 0, 0);
	}

	public void setBackIcon(int icon) {
		try {
			if (getSupportActionBar() != null) {
				getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(mContext, icon));
			}
		} catch (Exception ex) {
			ELog(ex);
		}
	}

	protected void ELog(Exception e) {
		Log.e(getTag(), "Error: " + e.getLocalizedMessage(), e);
	}


	public void toggleFullscreen(boolean fullscreen)
	{
		WindowManager.LayoutParams attrs = getWindow().getAttributes();
		if (fullscreen)
		{
			attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		}
		else
		{
			attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
		}
		getWindow().setAttributes(attrs);
	}

	@Override
	protected void onResume() {
		try {
			mRunning = true;
			mContext = this;
			super.onResume();
		} catch (Exception ex) {
			Log.e(getClass().getSimpleName(), "Error OnResume: " + ex.getLocalizedMessage());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mRunning = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mRunning = false;
	}

	public boolean isRunning() {
		return mRunning;
	}

	public void setRunning(boolean Estat) {
		mRunning=Estat;
	}

	protected void DLog(String what) {
		if(isDebugging()) {
			Log.i(getTag(), what);
		}
	}

	private boolean isDebugging() {
		boolean debuggable = false;
		PackageManager pm = getPackageManager();

		try {
			ApplicationInfo appinfo = pm.getApplicationInfo(getPackageName(), 0);
			debuggable = (appinfo.flags &= 2) != 0;
		} catch (PackageManager.NameNotFoundException var4) {
			;
		}

		return debuggable;
	}

	/**
	 * Intenta fer la status bar transparent o treu la transparència
	 * @param makeTranslucent
	 * @return true si s'ha fet transparent
	 */
	protected boolean setStatusBarTranslucent(boolean makeTranslucent, int colorPrimaryDark) {
		boolean done = false;
		if (makeTranslucent) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				done = true;
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				getWindow().setStatusBarColor(Color.TRANSPARENT);
				done = true;
			}
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				getWindow().setStatusBarColor(ContextCompat.getColor(mContext, colorPrimaryDark));
			}
		}
		return done;
	}

	protected void setActionBarTransparent(boolean _set, int colorPrimary, int colorPrimaryDark) {
		final ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(mContext, colorPrimary));
		if(_set) {
			setStatusBarTranslucent(true, colorPrimaryDark);
			cd.setAlpha(0);
			if(getSupportActionBar() != null) {
				getSupportActionBar().setDisplayShowTitleEnabled(false);
			}
		} else {
			setStatusBarTranslucent(false, colorPrimaryDark);
			cd.setAlpha(255);
			if(getSupportActionBar() != null) {
				getSupportActionBar().setDisplayShowTitleEnabled(true);
				getSupportActionBar().setBackgroundDrawable(cd);
			}
		}
	}

	protected void setTitleWithFont(String title) {
		ActionBar ab = getSupportActionBar();
		if(ab != null) {
			ab.setTitle(title);
		} else {
			DLog("Null actionbar");
		}
	}

	protected void setTitleWithFont(String title, String font) {
		ActionBar ab = getSupportActionBar();
		if(ab != null) {
			ab.setTitle(applyFont(title, font));
		} else {
			DLog("Null actionbar");
		}
	}

	public void setTitleWithFont(int title) {
		setTitle(getString(title));
	}

	public void setTitleWithFont(int title, String font) {
		setTitle(getString(title, font));
	}

	public SpannableString applyFont(String s) {
		DLog("Applying font to: " + s);
		SpannableString sp = new SpannableString(s);
		sp.setSpan(new TypefaceSpan(mContext, getDefaultFont()), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	public SpannableString applyFont(String string, String font) {
		DLog("Applying font to: " + string);
		SpannableString sp = new SpannableString(string);
		sp.setSpan(new TypefaceSpan(mContext, font), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	protected String getDefaultFont() {
		return "roboto";
	}

	protected int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public void setStatusBarPadding(View v) {
		v.setPadding(0, getStatusBarHeight(), 0, 0);
	}

	protected void setToolbarMargin(Toolbar toolbar) {
		AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, getStatusBarHeight(), 0, 0);
		toolbar.setLayoutParams(params);
	}

	protected void unsetToolbarMargin(Toolbar toolbar) {
		AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 0);
		toolbar.setLayoutParams(params);
	}

	public void amagarTeclat() {
		if (getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	public void amagarTeclat(View focusedView) {
		if (focusedView != null) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
		}
	}

	public void obrirTeclat(View view) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
			view.requestFocus();
		} catch (Exception ex) {
			Log.e(getTag(), "Error: " + ex.getLocalizedMessage(), ex);
		}
	}

	public void enableRotation() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}

	public void disableRotation() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	protected float pixelsToSp(float px)
	{
		float scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
		return px/scaledDensity;
	}

	protected float dimenToSp(int dimen) {
		return pixelsToSp(getResources().getDimension(dimen));
	}

	public abstract class PermissionListener {
		public abstract void onPermissionsGranted();
		public abstract void onPermissionsDenied();
	}

	private PermissionListener mPermissionListener;
	private static final int MULTIPLE_PERMISSIONS = 123;

	protected void askForPermissions(@NonNull String[] permissions, @NonNull PermissionListener _listener) {
		ArrayList<String> neededPermissions = new ArrayList<>();
		for(String s : permissions) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
				neededPermissions.add(s);
			}
		}

		if(neededPermissions.size() == 0) {
			_listener.onPermissionsGranted();
		} else {
			permissions = new String[neededPermissions.size()];
			for(int i = 0; i < permissions.length; i++) {
				permissions[i] = neededPermissions.get(i);
			}
			mPermissionListener = _listener;
			ActivityCompat.requestPermissions(this, permissions, MULTIPLE_PERMISSIONS);
		}
	}

	protected void askForPermissions(@NonNull String permission, @NonNull PermissionListener _listener) {
		askForPermissions(new String[] {permission}, _listener);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case MULTIPLE_PERMISSIONS:
				// If request is cancelled, the result arrays are empty.
				boolean granted = true;
				int i = 0;
				int max = permissions.length;
				if(grantResults.length == 0) {
					if(mPermissionListener != null) {
						mPermissionListener.onPermissionsDenied();
					}
					return;
				}

				while(granted && i < max) {
					if (!(grantResults.length > i && grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
						granted = false;
					}
					i++;
				}
				if(mPermissionListener != null) {
					if (granted) {
						mPermissionListener.onPermissionsGranted();
					} else {
						mPermissionListener.onPermissionsDenied();
					}
				}
				break;
		}
	}

	@SuppressLint("WrongConstant")
	public Intent clearTask(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		return intent;
	}

	protected void setLocale(String languageToLoad) {
		try {
			if(languageToLoad.equals(Preferences.getString(mContext, PREF_LOCALE, "es"))) {
				return;
			}
		} catch (Exception ex) {
			Log.e(getTag(), "Error: " + ex.getLocalizedMessage(), ex);
		}
		Preferences.put(mContext, PREF_LOCALE, languageToLoad);
		updateResources();
		mLanguageChanged = true;
	}

	public String getLocale() {
		return Preferences.getString(mContext, PREF_LOCALE, "es");
	}

	public static String getLocale(Context c) {
		return Preferences.getString(c, PREF_LOCALE, "es");
	}

	public void configLocale() {
		String loc = Preferences.getString(mContext, PREF_LOCALE, "es");
		Locale locale = new Locale(loc);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getResources().updateConfiguration(config, getResources().getDisplayMetrics());
		onConfigurationChanged(config);
	}

	private void updateResources() {
		String loc = Locale.getDefault().getLanguage();
		loc = Preferences.getString(mContext, PREF_LOCALE, loc);
		Locale locale = new Locale(loc);
		Locale.setDefault(locale);

		Resources resources = mContext.getResources();

		Configuration configuration = resources.getConfiguration();
		configuration.locale = locale;

		resources.updateConfiguration(configuration, resources.getDisplayMetrics());
		updateViews();
	}

	protected void updateViews() {
		Log.i(getTag(), "UpdateViews");
	}

	public int dpToPx(int dp) {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	}

	public static void anima(View v) {
		anima(v, 250);
	}

	public static void anima(View v, long duration) {
		if(v != null && v instanceof ViewGroup) {
			anima((ViewGroup)v, duration);
		}
	}

	public static void anima(ViewGroup g, long duration) {
		if(g != null) {
			Transition t = TransitionManager.getDefaultTransition();
			t.setDuration(duration);
			TransitionManager.beginDelayedTransition(g, t);
		}
	}

	public boolean hasLanguageChanged() {
		return mLanguageChanged;
	}
}