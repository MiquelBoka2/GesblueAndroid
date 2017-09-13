package com.sixtemia.gesbluedroid.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.cameraview.CameraView;
import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.customstuff.GesblueFragmentActivity;
import com.sixtemia.gesbluedroid.databinding.ActivityControlPresenciaDniBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sixtemia.sbaseobjects.tools.ImageTools.getFileAfterResize;

public class CameraActivity extends GesblueFragmentActivity {
	private ActivityControlPresenciaDniBinding mBinding;
	private Handler mBackgroundHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_control_presencia_dni);
		setTitleWithFont("");
		toggleFullscreen(true);

		initCamera();
	}

	private void initCamera() {
		mBinding.camera.addCallback(new CameraView.Callback() {
			@Override
			public void onCameraOpened(CameraView cameraView) {
				super.onCameraOpened(cameraView);
				DLog("Camera opened");
			}

			@Override
			public void onCameraClosed(CameraView cameraView) {
				super.onCameraClosed(cameraView);
				DLog("Camera closed");
			}

			@Override
			public void onPictureTaken(CameraView cameraView, byte[] data) {
				super.onPictureTaken(cameraView, data);
				DLog("Picture taken: " + data.length + " bytes");
				pictureTaken(data);
			}
		});

		mBinding.camera.setAutoFocus(true);

		mBinding.takePicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBinding.camera.takePicture();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBinding.camera.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mBinding.camera.stop();
	}

	private void pictureTaken(final byte[] data) {
		try {
			savePicture(data);
		} catch (OutOfMemoryError e) {
			Log.e(getTag(), "Out of memory: " + e.getLocalizedMessage(), e);
			Toast.makeText(mContext, "Out of memory", Toast.LENGTH_SHORT).show();
		}
	}

	private void savePicture(final byte[] data) {
		getBackgroundHandler().post(new Runnable() {
			@Override
			public void run() {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				String currentDateString = dateFormat.format(new Date());

				File file = new File(getFilesDir(), currentDateString + ".jpg");
				OutputStream os = null;
				String error = getString(R.string.foto_error_guardar_desconocido);
				try {
					os = new FileOutputStream(file);
					os.write(data);
					os.close();
				} catch (IOException e) {
					Log.w(getTag(), "Cannot write to " + file, e);
					error = e.getLocalizedMessage();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							// Ignore
							error = e.getLocalizedMessage();
						}
					}
				}
				if(file.exists()) {
					file = getFileAfterResize(file);
					confirmIntent(file.getAbsolutePath());
				} else {
					Toast.makeText(mContext, getString(R.string.foto_error_guardar, error), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void confirmIntent(String path) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra(FormulariActivity.KEY_RETURN_PATH, path);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBackgroundHandler != null) {
			mBackgroundHandler.getLooper().quitSafely();
			mBackgroundHandler = null;
		}
	}

	private Handler getBackgroundHandler() {
		if (mBackgroundHandler == null) {
			HandlerThread thread = new HandlerThread("background");
			thread.start();
			mBackgroundHandler = new Handler(thread.getLooper());
		}
		return mBackgroundHandler;
	}
}