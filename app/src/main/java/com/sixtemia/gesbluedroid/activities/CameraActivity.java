package com.sixtemia.gesbluedroid.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.cameraview.CameraView;
import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.customstuff.GesblueFragmentActivity;
import com.sixtemia.gesbluedroid.databinding.ActivityControlPresenciaDniBinding;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.text.TextUtils.isEmpty;
import static com.sixtemia.gesbluedroid.global.PreferencesGesblue.getCodiAgent;
import static com.sixtemia.gesbluedroid.global.PreferencesGesblue.getControl;
import static com.sixtemia.gesbluedroid.global.PreferencesGesblue.getPrefCodiExportadora;
import static com.sixtemia.gesbluedroid.global.PreferencesGesblue.getPrefCodiInstitucio;
import static com.sixtemia.gesbluedroid.global.PreferencesGesblue.getPrefCodiTipusButlleta;
import static com.sixtemia.gesbluedroid.global.PreferencesGesblue.getTerminal;
import static com.sixtemia.sbaseobjects.tools.ImageTools.getFileAfterResize;

public class CameraActivity extends GesblueFragmentActivity {
	private ActivityControlPresenciaDniBinding mBinding;
	private Handler mBackgroundHandler;
	String position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_control_presencia_dni);
		setTitleWithFont("");
		toggleFullscreen(true);
		if(PreferencesGesblue.getPrefFlash(mContext)==CameraView.FLASH_ON){
			mBinding.flash.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_flash));
			mBinding.camera.setFlash(CameraView.FLASH_ON);
		}
		else{
			mBinding.flash.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_flash_off));
			mBinding.camera.setFlash(CameraView.FLASH_OFF);
		}

		position = getIntent().getStringExtra("position");

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

		mBinding.flash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mBinding.camera.getFlash()==CameraView.FLASH_OFF) {
					mBinding.camera.setFlash(CameraView.FLASH_ON);
					mBinding.flash.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_flash));
					PreferencesGesblue.setPrefFlash(mContext,CameraView.FLASH_ON);

				}
				else{
					mBinding.camera.setFlash(CameraView.FLASH_OFF);
					mBinding.flash.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_flash_off));
					PreferencesGesblue.setPrefFlash(mContext,CameraView.FLASH_OFF);
				}

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
	private String generateCodiButlleta() {
		String numeroTiquet="";
		if(!isEmpty(numeroTiquet)) {
		    Log.d("Camera","No empty");
			return numeroTiquet;
		} else {
			long codiAgent = PreferencesGesblue.getCodiAgent(mContext);
			String terminal = PreferencesGesblue.getTerminal(mContext);

			int comptadorDenuncia = PreferencesGesblue.getComptadorDenuncia(getApplicationContext())+1;
			//PreferencesGesblue.saveComptadorDenuncia(mContext, comptadorDenuncia);

			int control = getControl(mContext);

			DLog("comptadorDenuncia: "+comptadorDenuncia);
			int codiexportadora = getPrefCodiExportadora(mContext);
			String coditipusbutlleta = getPrefCodiTipusButlleta(mContext);
			String codiinstitucio = getPrefCodiInstitucio(mContext);
			StringBuilder sb = new StringBuilder();
			//sb.append("1");
			int padding = 0;
			switch(codiexportadora) {
				case 1://Consell Comarcal de la Selva
					sb.append(coditipusbutlleta);
					sb.append(codiinstitucio);
					if (terminal.length() < 2) {
						sb.append("0");
					}
					sb.append(terminal);
					padding = 5 - String.valueOf(comptadorDenuncia).length();
					for (int i = 0; i < padding; i++) {
						sb.append("0");
					}
					sb.append(comptadorDenuncia);
					break;
				case 2://Consell Comarcal del Baix Empordà
					sb.append(coditipusbutlleta);
					sb.append(codiinstitucio);
					if (terminal.length() < 2) {
						sb.append("0");
					}
					sb.append(terminal);
					padding = 6 - String.valueOf(comptadorDenuncia).length();
					for (int i = 0; i < padding; i++) {
						sb.append("0");
					}
					sb.append(comptadorDenuncia);
					break;
				case 3://Xaloc
					sb.append(coditipusbutlleta);
					sb.append(codiinstitucio);
					if (terminal.length() < 2) {
						sb.append("0");
					}
					sb.append(terminal);
					padding = 5 - String.valueOf(comptadorDenuncia).length();
					for (int i = 0; i < padding; i++) {
						sb.append("0");
					}
					sb.append(comptadorDenuncia);
					break;
				case 4://Somintec
					sb.append(coditipusbutlleta);
					sb.append(codiinstitucio);
					if (terminal.length() < 2) {
						sb.append("0");
					}
					sb.append(terminal);
					padding = 5 - String.valueOf(comptadorDenuncia).length();
					for (int i = 0; i < padding; i++) {
						sb.append("0");
					}
					sb.append(comptadorDenuncia);
					break;
				case 5://Policia Local de Calonge
					sb.append(coditipusbutlleta);
					sb.append(codiinstitucio);
					if (terminal.length() < 2) {
						sb.append("0");
					}
					sb.append(terminal);
					padding = 5 - String.valueOf(comptadorDenuncia).length();
					for (int i = 0; i < padding; i++) {
						sb.append("0");
					}
					sb.append(comptadorDenuncia);
					break;
			}
			numeroTiquet = sb.toString();

			return numeroTiquet;
		}
	}
	private void savePicture(final byte[] data) {
		getBackgroundHandler().post(new Runnable() {
			@Override
			public void run() {

				String concessio = Long.toString(PreferencesGesblue.getConcessio(mContext));
				String numDenuncia = generateCodiButlleta();
				while(numDenuncia == ""){
					numDenuncia = generateCodiButlleta();
					Log.d("Camera","Error calculant Codi Butlleta");
				}
				Log.d ("Camera",numDenuncia);
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				String currentDateString = dateFormat.format(new Date());
                File direct = new File("storage/emulated/0/Sixtemia/upload/temp");

                if (!direct.exists()) {
                    File wallpaperDirectory = new File("storage/emulated/0/Sixtemia/upload/temp");
                    wallpaperDirectory.mkdirs();
                }
				File file = new File(direct, currentDateString  + "-" + concessio + "-" + numDenuncia + position + ".jpg");
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