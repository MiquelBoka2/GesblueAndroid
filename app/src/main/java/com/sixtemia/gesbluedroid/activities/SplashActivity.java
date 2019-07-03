package com.sixtemia.gesbluedroid.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.customstuff.GesblueFragmentActivity;
import com.sixtemia.gesbluedroid.databinding.ActivitySplashBinding;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

import java.lang.ref.WeakReference;

public class SplashActivity extends GesblueFragmentActivity {

	private ActivitySplashBinding mBinding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
		setStatusBarTranslucent(true, R.color.colorPrimaryDark);

		askForPermissions(new String[]{
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.CAMERA
		}, new PermissionListener() {
			@Override
			public void onPermissionsGranted() {
				loadApp();
			}

			@Override
			public void onPermissionsDenied() {
				finish();
			}
		});
	}

	private class TaskOpenAsync extends AsyncTask<Activity, Void, Void> {
		boolean cancelled = false;
		WeakReference<Activity> weakActivity;

		@Override
		protected void onPreExecute() {
			cancelled = false;
		}

		@Override
		protected Void doInBackground(Activity... params) {
			cancelled = false;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!cancelled) {
				fetchInitialData();
			}
		}
	}

	private void fetchInitialData() {
		openApp();
	}

	/**
	 * Es carreguen les dades que facin falta abans d'obrir l'app.
	 */
	private void loadApp() {
		// Obrim l'app normalment
		TaskOpenAsync obrirAppAsync = new TaskOpenAsync();
		obrirAppAsync.weakActivity = new WeakReference<Activity>(this);
		obrirAppAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	/**
	 * Obrim l'app
	 */
	private void openApp() {
		PreferencesGesblue.logout(mContext);
		Intent intent = new Intent(mContext, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
