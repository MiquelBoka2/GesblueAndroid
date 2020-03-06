package com.boka2.gesblue.customstuff;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.boka2.gesblue.databinding.ToolbarBinding;
import com.flurry.android.FlurryAgent;
import com.boka2.gesblue.R;
import com.boka2.sbaseobjects.objects.SFragmentActivity;

import java.util.Locale;

import static android.view.View.GONE;


/*
 * Created by Boka2.
 */
public class GesblueFragmentActivity extends SFragmentActivity {

	public static final String LOG_TAG = "Gesblue";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mResources = getResources();
	}

	protected void DLog(String what) {
		Log.i(LOG_TAG, what);
	}

	protected void setupToolbar(ToolbarBinding b, boolean showBack) {
		setupToolbar(b, true, showBack, false);
		boolean trans = setStatusBarTranslucent(true, R.color.colorPrimaryDark);
		if(b != null && trans) {
			setStatusBarPadding(b.toolbarContainer);
		}
	}

	protected void setupToolbar(ToolbarBinding b, boolean visible, boolean showBack, boolean withElevation) {
		if(b != null) {
			setupToolbar(b.toolbarContainer, b.toolbarObject, visible, showBack, withElevation);
		}
	}

	protected void setupToolbar(int title, boolean visible, boolean showBack, boolean withElevation, boolean overStatusbar, @Nullable View needsPadding) {
		ActionBar ab = getSupportActionBar();
		if(ab != null) {
			if(title == 0) {
				ab.setTitle("");
			} else {
				ab.setTitle(title);
			}
			setupToolbar(visible, showBack, withElevation, overStatusbar, needsPadding);
		}
	}

	protected void setupToolbar(View toolbarContainer, Toolbar toolbar, boolean visible, boolean showBack, boolean withElevation) {
		if(toolbar != null) {
			try {
				setSupportActionBar(toolbar);
				ActionBar ab = getSupportActionBar();

				if(ab == null) {
					return;
				}

				setBackIcon(R.drawable.ic_back);

				if (!visible && toolbarContainer != null) {
					toolbarContainer.setVisibility(GONE);
				}
				if (showBack) {
					ab.setHomeButtonEnabled(true);
					ab.setDisplayHomeAsUpEnabled(true);
				}
				if (!withElevation) {
					ab.setElevation(0);
				}

				setTitleWithFont(ab.getTitle().toString());
			} catch (Exception ex) {
				error(ex);
			}
		}
	}

	public void error(Exception e) {
		Log.e(getTag(), "Error: " + e.getLocalizedMessage(), e);
	}

	protected void setupToolbar(boolean visible, boolean showBack, boolean withElevation, boolean overStatusbar, @Nullable View needsPadding) {
		ActionBar ab = getSupportActionBar();
		if(ab != null) {
			if(!visible) {
				ab.hide();
			}
			if(showBack) {
				ab.setDisplayHomeAsUpEnabled(true);
//                ab.setHomeAsUpIndicator(R.drawable.back);
			}
			if(!withElevation) {
				ab.setElevation(0);
			}
			if(overStatusbar) {
				setStatusBarTranslucent(true, R.color.colorPrimaryDark);
				if(needsPadding != null) {
					setStatusBarPadding(needsPadding);
				}
			}
		}
	}

	protected void setupVisibleToolbar(ToolbarBinding b) {
		setSupportActionBar(b.toolbarObject);
		setupToolbar(true, false, false, false, null);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.home:
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public int dpToPx(int dp) {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	}

	protected void setLocale(String languageToLoad) {

	}

	public void configLocale() {

	}

	private void updateResources() {

	}

	protected void updateViews() {

	}

	@Override
	public String getLocale() {
		return Locale.getDefault().getLanguage();
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(mContext);
	}

	@Override
	protected void onStop() {
		FlurryAgent.onEndSession(mContext);
		super.onStop();
	}
}