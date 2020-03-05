package com.boka2.gesblue.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.boka2.gesblue.GesblueApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.datecs.api.emsr.EMSR;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;
import com.datecs.api.rfid.RC663;
import com.boka2.gesblue.PrintAsyncTask;
import com.boka2.gesblue.R;
import com.boka2.gesblue.Sancio;
import com.boka2.gesblue.activities.passosformulari.Pas1TipusActivity;
import com.boka2.gesblue.activities.passosformulari.Pas2MarcaActivity;
import com.boka2.gesblue.activities.passosformulari.Pas3ModelActivity;
import com.boka2.gesblue.activities.passosformulari.Pas4ColorActivity;
import com.boka2.gesblue.activities.passosformulari.Pas5InfraccioActivity;
import com.boka2.gesblue.activities.passosformulari.Pas6CarrerActivity;
import com.boka2.gesblue.activities.passosformulari.Pas7NumeroActivity;
import com.boka2.gesblue.customstuff.GesblueFragmentActivity;
import com.boka2.gesblue.customstuff.dialogs.DeviceListActivity;
import com.boka2.gesblue.databinding.ActivityFormulariBinding;
import com.boka2.gesblue.datamanager.DatabaseAPI;
import com.boka2.gesblue.datamanager.database.model.Model_Carrer;
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.global.Utils;
import com.boka2.gesblue.network.PrinterServer;
import com.boka2.gesblue.network.PrinterServerListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.boka2.gesblue.global.PreferencesGesblue.getFoto1;
import static com.boka2.gesblue.global.PreferencesGesblue.getFoto2;
import static com.boka2.gesblue.global.PreferencesGesblue.getFoto3;
import static com.boka2.gesblue.global.PreferencesGesblue.getFoto4;
import static com.boka2.gesblue.global.Utils.generateCodiButlleta;

public class FormulariActivity extends GesblueFragmentActivity implements View.OnClickListener {

	public static final String INTENT_RECUPERADA = "recuperada";
	public static final String INTENT_SANCIO = "sancio";
	public static final String INTENT_NUM_DENUNCIA = "numDenuncia";
	public static final String INTENT_DATA_CREACIO = "dataCreacio";


	private static String codiDenuncia;

	private ActivityFormulariBinding mBinding;
	private Sancio sancio;
	private String foto1;
	private String foto2;
	private String foto3;
	private String foto4;

	private Bitmap imageBitmap;
	private Model_Denuncia denuncia = new Model_Denuncia();

	private static Boolean errorDialog=false;

	private Boolean adm = false;
	private Button btn_Enviar;

	public static final int RESULT_FOTO_1 = 1741;
	public static final int RESULT_FOTO_2 = 1742;
	public static final int RESULT_FOTO_3 = 1743;
	public static final int RESULT_FOTO_4 = 1744;
	public static final int RESULT_ESTANDARD = 1730;
	private static final int REQUEST_GET_DEVICE = 1734;
	private static final int REQUEST_MORE_OPTIONS = 1735;
	public static final String KEY_RETURN_PATH = "path";
	public static final String KEY_VINC_DE_MATRICULA = "vincDeMatricula";
	public static final String KEY_FORMULARI_PRIMER_COP = "formPrimerCop";
	public static final String KEY_FORMULARI_CONFIRMAR = "confirmar";
	public static final String KEY_DATA_INFRACCIO = "dataInfraccio";
	public static final String KEY_NUMERO_TIQUET = "numeroTiquet";

	private boolean img1IsActive = false;
	private boolean img2IsActive = false;
	private boolean img3IsActive = false;
	private boolean img4IsActive = false;
	//El primer cop que surti el dialeg d'enviar s'enviarà sol. Els seguents no
	private boolean isFirstEnvia = true;

	private static ProtocolAdapter mProtocolAdapter;
	private static ProtocolAdapter.Channel mPrinterChannel;
	private static Printer mPrinter;
	private static EMSR mEMSR;
	private static PrinterServer mPrinterServer;
	private static BluetoothSocket mBtSocket;
	private static RC663 mRC663;
	private static boolean isLinked = false;
	private boolean denunciaSent = false;
	private String dataInfraccio = "";
	private String numeroTiquet = "";
	private boolean recuperada = false;
	private String numDenuncia = "";
	private Date dataCreacio;

	private ProgressDialog mDialog;

	private ProgressDialog getDialog(String title, String message) {
		if (null == mDialog || !mDialog.isShowing()) {
			mDialog = new ProgressDialog(this);
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(false);
			if (!isFinishing()) mDialog.show();
		}
		if (null != title) {
			mDialog.setTitle(title);
		}
		if (null != message) {
			mDialog.setMessage(message);
		}
		return mDialog;
	}

	private ProgressDialog getDialog(String message) {
		return getDialog(null, message);
	}

	private ProgressDialog getDialog(int message) {
		return getDialog(getString(message));
	}

	private ProgressDialog getDialog(int title, int message) {
		return getDialog(getString(title), getString(message));
	}

	private void dismissDialog() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (null != mDialog) {
					try {
						mDialog.dismiss();
					} catch (Exception ex) {
						ELog(ex);
					}
				}
				mDialog = null;
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.

		savedInstanceState.putString("foto1", foto1);
		savedInstanceState.putString("foto2", foto2);
		savedInstanceState.putString("foto3", foto3);
		savedInstanceState.putString("foto4", foto4);

		// etc.

		super.onSaveInstanceState(savedInstanceState);
	}

//onRestoreInstanceState

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.


		boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
		double myDouble = savedInstanceState.getDouble("myDouble");
		int myInt = savedInstanceState.getInt("MyInt");
		foto1 = savedInstanceState.getString("foto1");
		foto2 = savedInstanceState.getString("foto2");
		foto3 = savedInstanceState.getString("foto3");
		foto4 = savedInstanceState.getString("foto4");
		if (foto1 != "") {
			pinta(foto1, mBinding.imageViewA);
			img1IsActive = true;
		}
		if (foto2 != "") {
			pinta(foto2, mBinding.imageViewB);
			img2IsActive = true;
		}
		if (foto3 != "") {
			pinta(foto3, mBinding.imageViewC);
			img3IsActive = true;
		}
		if (foto4 != "") {
			pinta(foto4, mBinding.imageViewD);
			img4IsActive = true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_formulari);
		setupVisibleToolbar(mBinding.toolbar);
		mBinding.toolbar.txtLocalitzacioEstat.setVisibility(View.GONE);
		mBinding.toolbar.icOpciones.setVisibility(View.GONE);
		mBinding.toolbar.txtGesBlue.setVisibility(View.GONE);
		mBinding.toolbar.txtAny.setVisibility(View.GONE);


		if (!getFoto1(mContext).equals("")) {
			foto1 = getFoto1(mContext);
			pinta(foto1, mBinding.imageViewA);
			img1IsActive = true;
		}
		if (!getFoto2(mContext).equals("")) {
			foto2 = getFoto2(mContext);
			pinta(foto2, mBinding.imageViewB);
			img2IsActive = true;
		}
		if (!getFoto3(mContext).equals("")) {
			foto3 = getFoto3(mContext);
			pinta(foto3, mBinding.imageViewC);
			img3IsActive = true;
		}
		if (!getFoto4(mContext).equals("")) {
			foto4 = getFoto4(mContext);
			pinta(foto4, mBinding.imageViewD);
			img4IsActive = true;
		}


		getFromIntent();
		fillAll();
		disableViews();
		initOnClicks();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {


		/*	borra(foto1);

			borra(foto2);

			borra(foto3);*/

			Log.e("onKeyDown pulsado:", "" + keyCode);

		}
		return super.onKeyDown(keyCode, event);
	}

	private void getFromIntent() {
		Intent intent = getIntent();

		sancio = intent.getParcelableExtra(INTENT_SANCIO);

		if (intent.getBooleanExtra(INTENT_RECUPERADA, false)) {
			recuperada = true;
		}
		if (!isEmpty(intent.getStringExtra(INTENT_NUM_DENUNCIA))) {
			numDenuncia = intent.getStringExtra(INTENT_NUM_DENUNCIA);
		}
		if (recuperada == true) {
			dataCreacio = (Date) intent.getSerializableExtra(INTENT_DATA_CREACIO);
		} else {

			Model_Carrer carrer = DatabaseAPI.getCarrer(mContext, String.valueOf(PreferencesGesblue.getCodiCarrer(mContext)));

			sancio.setModelCarrer(carrer);
		}
		if (!isEmpty(intent.getStringExtra(KEY_DATA_INFRACCIO))) {
			dataInfraccio = intent.getStringExtra(KEY_DATA_INFRACCIO);
		}
		if (!isEmpty(intent.getStringExtra(KEY_NUMERO_TIQUET))) {
			numeroTiquet = intent.getStringExtra(KEY_NUMERO_TIQUET);
		}
		if (intent.getExtras().getBoolean("adm")) {
			adm = intent.getExtras().getBoolean("adm");

			if (adm) {
				btn_Enviar = findViewById(R.id.btn_Enviar);
				btn_Enviar.setVisibility(VISIBLE);
			}
		}

		if (intent.getBooleanExtra(KEY_VINC_DE_MATRICULA, false)) {
			intent = new Intent(mContext, Pas1TipusActivity.class);
			intent.putExtra(INTENT_SANCIO, sancio);
			intent.putExtra(KEY_FORMULARI_PRIMER_COP, true);
			intent.putExtra("adm", adm);
			startActivity(intent);
		}

	}

	private void fillAll() {
		fillMatricula();
		fillTipus();
		fillMarca();
		fillModel();
		fillColor();
		fillSancio();
		fillCarrer();
		fillNum();

		Log.e("Recuperada?:", "" + recuperada);

		/*if(recuperada==true) {
			File f = new File("storage/emulated/0/Boka2/upload/temp");
			if (f.exists() && f.isDirectory()){
				final Pattern p = Pattern.compile(".*-"+numDenuncia+"1.jpg"); // I know I really have a stupid mistake on the regex;

				File[] flists = f.listFiles(new FileFilter(){
					@Override
					public boolean accept(File file) {
						Log.e("Matched?:","-"+file);
						return p.matcher(file.getName()).matches();
					}
				});
				if(flists.length>0){
					File f1 = flists[0];

					Log.e("Ruta foto1:",f1.getName());

					pinta("storage/emulated/0/Boka2/upload/temp/"+f1.getName(), mBinding.imageViewA);
					img1IsActive = true;
					foto1 = "storage/emulated/0/Boka2/upload/temp/"+f1.getName();
				}

				final Pattern p2 = Pattern.compile(".*-"+numDenuncia+"2.jpg"); // I know I really have a stupid mistake on the regex;

				File[] flists2 = f.listFiles(new FileFilter(){
					@Override
					public boolean accept(File file) {
						return p2.matcher(file.getName()).matches();
					}
				});
				if(flists2.length>0){
					File f2 = flists2[0];

					Log.e("Ruta foto2:",f2.getName());


					pinta("storage/emulated/0/Boka2/upload/temp/"+f2.getName(), mBinding.imageViewB);
					img2IsActive = true;
					foto2 = "storage/emulated/0/Boka2/upload/temp/"+f2.getName();
				}


				final Pattern p3 = Pattern.compile(".*-"+numDenuncia+"3.jpg"); // I know I really have a stupid mistake on the regex;

				File[] flists3 = f.listFiles(new FileFilter(){
					@Override
					public boolean accept(File file) {
						return p3.matcher(file.getName()).matches();
					}
				});
				if(flists3.length>0){
					File f3 = flists3[0];


					Log.e("Ruta foto3:",f3.getName());


					pinta("storage/emulated/0/Boka2/upload/temp/"+f3.getName(), mBinding.imageViewC);
					img3IsActive = true;
					foto3 = "storage/emulated/0/Boka2/upload/temp/"+f3.getName();
				}

				final Pattern p4 = Pattern.compile(".*-"+numDenuncia+"4.jpg"); // I know I really have a stupid mistake on the regex;

				File[] flists4 = f.listFiles(new FileFilter(){
					@Override
					public boolean accept(File file) {
						return p4.matcher(file.getName()).matches();
					}
				});
				if(flists4.length>0){
					File f4 = flists4[0];


					Log.e("Ruta foto4:",f4.getName());


					pinta("storage/emulated/0/Boka2/upload/temp/"+f4.getName(), mBinding.imageViewD);
					img4IsActive = true;
					foto4 = "storage/emulated/0/Boka2/upload/temp/"+f4.getName();
				}

			}
		}*/

	}

	private void fillMatricula() {
		if (!isEmpty(sancio.getMatricula())) {
			mBinding.tvMatricula.setText(sancio.getMatricula());
		} else {
			finish();
		}
	}

	private void fillTipus() {
		if (sancio.getModelTipusVehicle() != null) {

			String aux = mBinding.tvTipus.getText().toString();

			if (isEmpty(aux)) {
				mBinding.tvTipus.setText(Utils.languageMultiplexer(sancio.getModelTipusVehicle().getNomtipusvehiclees(), sancio.getModelTipusVehicle().getNomtipusvehiclecat()));
				mBinding.tvMarca.setEnabled(true);
			} else {
				if (!(aux.equals(Utils.languageMultiplexer(sancio.getModelTipusVehicle().getNomtipusvehiclees(), sancio.getModelTipusVehicle().getNomtipusvehiclecat())))) {
					mBinding.tvTipus.setText(Utils.languageMultiplexer(sancio.getModelTipusVehicle().getNomtipusvehiclees(), sancio.getModelTipusVehicle().getNomtipusvehiclecat()));

					mBinding.tvMarca.setText("");
					mBinding.tvMarca.setEnabled(true);
					mBinding.imageViewMarca.setImageResource(0);
					sancio.setModelMarca(null);

					mBinding.tvModel.setText("");
					mBinding.tvModel.setEnabled(false);
					sancio.setModelModel(null);
				}
			}
		}
	}

	private void fillMarca() {
		if (mBinding.tvMarca.isEnabled()) {
			if (sancio.getModelMarca() != null) {

				String aux = mBinding.tvMarca.getText().toString();

				if (isEmpty(aux)) {
					Glide.with(mContext).load(sancio.getModelMarca().getImatgemarca()).into(mBinding.imageViewMarca);
					mBinding.tvMarca.setText(sancio.getModelMarca().getNommarca());
					mBinding.tvModel.setEnabled(true);
				} else {
					if (!(aux.equals(sancio.getModelMarca().getNommarca()))) {
						Glide.with(mContext).load(sancio.getModelMarca().getImatgemarca()).into(mBinding.imageViewMarca);
						mBinding.tvMarca.setText(sancio.getModelMarca().getNommarca());
						mBinding.tvModel.setText("");
						mBinding.tvModel.setEnabled(true);
						sancio.setModelModel(null);

					}
				}
			}
		}
	}

	private void fillModel() {
		if (mBinding.tvModel.isEnabled()) {
			if (sancio.getModelModel() != null) {

				String aux = mBinding.tvModel.getText().toString();

				if (isEmpty(aux)) {
					mBinding.tvModel.setText(sancio.getModelModel().getNommodel());
				} else {
					if (!(aux.equals(sancio.getModelModel().getNommodel()))) {
						mBinding.tvModel.setText(sancio.getModelModel().getNommodel());

					}
				}
			}
		}
	}

	private void fillColor() {
		if (mBinding.tvColor.isEnabled()) {
			if (sancio.getModelColor() != null) {
				mBinding.tvColor.setText(Utils.languageMultiplexer(sancio.getModelColor().getNomcolores(), sancio.getModelColor().getNomcolorcat()));
				mBinding.viewColor.setBackgroundColor(Color.parseColor("#" + sancio.getModelColor().getHexcolor()));
			}
		}
	}

	private void fillSancio() {
		if (mBinding.tvInfraccio.isEnabled()) {
			if (sancio.getModelInfraccio() != null) {
				mBinding.tvInfraccio.setText(sancio.getModelInfraccio().getNom());
			}
		}
	}

	private void fillCarrer() {
		if (mBinding.tvCarrer.isEnabled()) {
			if (sancio.getModelCarrer() != null) {
				mBinding.tvCarrer.setText(sancio.getModelCarrer().getNomcarrer());
/*				String aux = mBinding.tvCarrer.getText().toString();

				//Actualitzem la sanció amb els nous valors.
				sancio.getModelCarrer().setNomcarrer(PreferencesGesblue.getNomCarrer(mContext));
				sancio.getModelCarrer().setCodicarrer(PreferencesGesblue.getCodiCarrer(mContext));

				if(isEmpty(aux)) {
					mBinding.tvCarrer.setText(sancio.getModelCarrer().getNomcarrer());
					mBinding.tvNum.setEnabled(true);
				} else {
					if(!(aux.equals(sancio.getModelCarrer().getNomcarrer()))) {
						mBinding.tvCarrer.setText(sancio.getModelCarrer().getNomcarrer());
						mBinding.tvNum.setText("");
						mBinding.tvNum.setEnabled(true);
						sancio.setNumero(null);
					}
				}*/
			}
		}
	}

	private void fillNum() {
		if (mBinding.tvNum.isEnabled()) {
			if (!isEmpty(sancio.getNumero())) {
				mBinding.tvNum.setText(sancio.getNumero());
			}
		}
	}

	private void initOnClicks() {
		mBinding.tvTipus.setOnClickListener(this);
		mBinding.tvMarca.setOnClickListener(this);
		mBinding.tvModel.setOnClickListener(this);
		mBinding.tvColor.setOnClickListener(this);
		mBinding.tvInfraccio.setOnClickListener(this);
		mBinding.tvCarrer.setOnClickListener(this);
		mBinding.tvNum.setOnClickListener(this);

		mBinding.btnCamera.setOnClickListener(this);
		mBinding.buttonAccepta.setOnClickListener(this);
		mBinding.buttonPrint.setOnClickListener(this);
		mBinding.buttonEnvia.setOnClickListener(this);
		mBinding.imageViewA.setOnClickListener(this);
		mBinding.imageViewB.setOnClickListener(this);
		mBinding.imageViewC.setOnClickListener(this);
		mBinding.imageViewD.setOnClickListener(this);
		mBinding.btnEnviar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		int totalfotos;
		switch (v.getId()) {
			case R.id.tvTipus:
				if (!recuperada) {
					startActivityFromIntent(new Intent(mContext, Pas1TipusActivity.class));
				}
				break;
			case R.id.tvMarca:
				if (!recuperada) {
					startActivityFromIntent(new Intent(mContext, Pas2MarcaActivity.class));
				}
				break;
			case R.id.tvModel:
				if (!recuperada) {
					startActivityFromIntent(new Intent(mContext, Pas3ModelActivity.class));
				}
				break;
			case R.id.tvColor:
				if (!recuperada) {
					startActivityFromIntent(new Intent(mContext, Pas4ColorActivity.class));
				}
				break;
			case R.id.tvInfraccio:
				if (!recuperada) {
					startActivityFromIntent(new Intent(mContext, Pas5InfraccioActivity.class));
				}
				break;
			case R.id.tvCarrer:
				if (!recuperada) {
					startActivityFromIntent(new Intent(mContext, Pas6CarrerActivity.class));
				}
				break;
			case R.id.tvNum:
				if (!recuperada) {
					startActivityFromIntent(new Intent(mContext, Pas7NumeroActivity.class));
				}
				break;
			case R.id.btnCamera:
				if (!recuperada) {
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					if (isEmpty(foto1)) {
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_1);
						} else {
						}


					} else if (isEmpty(foto2)) {
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_2);
						} else {
						}


					} else if (isEmpty(foto3)) {
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_3);
						} else {
						}


					} else if (isEmpty(foto4)) {
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_4);
						} else {
						}


					} else {
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_1);
						} else {
						}
					}
				}
				break;
			case R.id.buttonAccepta:
			case R.id.buttonPrint:
				totalfotos = 0;
				if ((foto1 != null) && (foto1 != "")) {
					totalfotos++;
				}
				if ((foto2 != null) && (foto2 != "")) {
					totalfotos++;
				}
				if ((foto3 != null) && (foto3 != "")) {
					totalfotos++;
				}

				if ((totalfotos < 2) && (recuperada == false)) {
					Utils.showCustomDialog(mContext, R.string.atencio, R.string.fotosObligatories);
				} else {
					if (checkCamps()) {
						errorDialog=false;
						print();
					} else {
						Utils.showCustomDialog(mContext, R.string.atencio, R.string.campsObligatoris);
					}
				}
				break;

			case R.id.imageViewA:

				if (img1IsActive) {
					confirmPicture(mBinding.imageViewA, foto1, new Runnable() {
						@Override
						public void run() {

							borra(foto1);
							foto1 = null;
							img1IsActive = false;
							checkBotoCamera();

						}
					});
				} else {

					if (!recuperada) {
						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_1);
						} else {
						}
					}
				}

				break;
			case R.id.imageViewB:

				if (img2IsActive) {
					confirmPicture(mBinding.imageViewB, foto2, new Runnable() {
						@Override
						public void run() {
							borra(foto2);
							foto2 = null;
							img2IsActive = false;
							checkBotoCamera();
						}
					});
				} else {
					if (!recuperada) {
						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_2);
						} else {
						}
					}
				}

				break;
			case R.id.imageViewC:
				if (img3IsActive) {
					confirmPicture(mBinding.imageViewC, foto3, new Runnable() {
						@Override
						public void run() {
							borra(foto3);
							foto3 = null;
							img3IsActive = false;
							checkBotoCamera();
						}
					});
				} else {
					if (!recuperada) {
						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_3);
						} else {
						}
					}
				}

				break;

			case R.id.imageViewD:
				if (img3IsActive) {
					confirmPicture(mBinding.imageViewD, foto4, new Runnable() {
						@Override
						public void run() {
							borra(foto4);
							foto4 = null;
							img4IsActive = false;
							checkBotoCamera();
						}
					});
				} else {
					if (!recuperada) {
						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_4);
						} else {
						}
					}
				}

				break;
			/**ENVIAR ADMIN***/
			case R.id.btn_Enviar:

				totalfotos = 0;
				if ((foto1 != null) && (foto1 != "")) {
					totalfotos++;
				}
				if ((foto2 != null) && (foto2 != "")) {
					totalfotos++;
				}
				if ((foto3 != null) && (foto3 != "")) {
					totalfotos++;
				}
				if ((foto4 != null) && (foto4 != "")) {
					totalfotos++;
				}

				if (checkCamps()) {
					if (isFirstEnvia) {
						isFirstEnvia = false;
						send();
						DatabaseAPI.updateADenunciaImpresa(mContext, denuncia.getCodidenuncia());
						goToMain();
					}
				} else {
					Utils.showCustomDialog(mContext, R.string.atencio, R.string.campsObligatoris);
				}


				break;
		}
	}

	private boolean checkCamps() {
		return (
				sancio != null &&
						sancio.getModelCarrer() != null &&
						sancio.getModelCarrer().getCodicarrer() != 0 &&
						!isEmpty(sancio.getNumero()) &&
						!isEmpty(sancio.getMatricula()) &&
						sancio.getModelTipusVehicle() != null &&
						sancio.getModelMarca() != null &&
						sancio.getModelModel() != null &&
						sancio.getModelColor() != null &&
						sancio.getModelInfraccio() != null &&
						(sancio.getModelTipusVehicle().getCoditipusvehicle() != 0) &&
						!isEmpty(sancio.getModelMarca().getCodimarca()) &&
						!isEmpty(sancio.getModelModel().getCodimodel()) &&
						!isEmpty(sancio.getModelColor().getCodicolor()) &&
						(sancio.getModelInfraccio().getCodi() != 0) &&
						!isEmpty(sancio.getModelInfraccio().getImporte())
		);
	}

	private void print() {
		if (isLinked) {
			printFinal(true);
		} else {
			getDialog(R.string.msg_connecting);
			link();
		}
	}

	private void link() {
		if (!isEmpty(PreferencesGesblue.getAddressBluetoothPrinter(mContext))) {
			establishBluetoothConnection(PreferencesGesblue.getAddressBluetoothPrinter(mContext));

		} else {
			waitForConnection();
		}
	}

	private void startActivityFromIntent(Intent intent) {
		intent.putExtra(INTENT_SANCIO, sancio);
		intent.putExtra(KEY_FORMULARI_PRIMER_COP, false);
		startActivityForResult(intent, RESULT_ESTANDARD);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();

			switch (requestCode) {

				/**ESPEREM EL RESULTAT DE LES FOTOS**/
				case Utils.REQUEST_IMAGE_CAPTURE_1:

					imageBitmap = (Bitmap) extras.get("data");
					foto1 = Utils.savePicture(imageBitmap, mContext, "1");
					pinta(foto1, mBinding.imageViewA);
					img1IsActive = true;
					checkBotoCamera();
					break;


				case Utils.REQUEST_IMAGE_CAPTURE_2:

					imageBitmap = (Bitmap) extras.get("data");
					foto2 = Utils.savePicture(imageBitmap, mContext, "2");
					pinta(foto2, mBinding.imageViewB);
					img2IsActive = true;
					checkBotoCamera();
					break;


				case Utils.REQUEST_IMAGE_CAPTURE_3:

					imageBitmap = (Bitmap) extras.get("data");
					foto3 = Utils.savePicture(imageBitmap, mContext, "3");
					pinta(foto3, mBinding.imageViewC);
					img3IsActive = true;
					checkBotoCamera();
					break;


				case Utils.REQUEST_IMAGE_CAPTURE_4:

					imageBitmap = (Bitmap) extras.get("data");
					foto4 = Utils.savePicture(imageBitmap, mContext, "4");
					pinta(foto4, mBinding.imageViewD);
					img4IsActive = true;
					checkBotoCamera();
					break;

				case RESULT_ESTANDARD:
					Boolean semafor = (Boolean) data.getExtras().get("noSancio");

					if (semafor == null) {
						sancio = (Sancio) data.getExtras().get(KEY_FORMULARI_CONFIRMAR);
					}
					disableViews();
					fillAll();
					break;

				case REQUEST_MORE_OPTIONS:
					sancio = (Sancio) data.getExtras().get(INTENT_SANCIO);
					dataInfraccio = (String) data.getExtras().get(KEY_DATA_INFRACCIO);
					numeroTiquet = (String) data.getExtras().get(KEY_NUMERO_TIQUET);
					break;


				case REQUEST_GET_DEVICE:
					String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					if (BluetoothAdapter.checkBluetoothAddress(address)) {
						PreferencesGesblue.saveAdressBluetoothPrinter(mContext, address);
						establishBluetoothConnection(address);
					}
					break;
				default:
					break;
			}
		} else if (resultCode == RESULT_CANCELED || resultCode == 1) {
			switch (requestCode) {
				case REQUEST_GET_DEVICE:
					dismissDialog();
					break;
			}
		}
	}

	@SuppressLint("RestrictedApi")
	private void checkBotoCamera() {
		mBinding.btnCamera.setVisibility((isEmpty(foto1) || isEmpty(foto2) || isEmpty(foto3) || isEmpty(foto4)) ? VISIBLE : GONE);
	}

	private void pinta(String path, ImageView imgView) {
		if (!isEmpty(path)) {
			Glide.with(mContext)
					.load(path)
					.asBitmap()
					.into(new BitmapImageViewTarget(imgView) {
						@Override
						protected void setResource(Bitmap resource) {
							//Play with bitmap
							super.setResource(resource);
						}
					});


			anima(mBinding.getRoot());
			imgView.setVisibility(VISIBLE);
		}
	}

	private boolean borra(String path) {
		try {
			File f = new File(path);
			if (f.exists()) {
				return f.delete();
			}
		} catch (Exception ex) {
			ELog(ex);
		}
		return false;
	}

	private void confirmPicture(final ImageView iv, String path, final Runnable onDelete) {
		try {
			if (!recuperada) {
				mBinding.repetir.setVisibility(VISIBLE);
			} else {
				mBinding.repetir.setVisibility(INVISIBLE);

			}

			Glide.with(mContext).load(path).asBitmap().into(mBinding.preview);
			mBinding.repetir.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					anima(mBinding.getRoot());
					iv.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
					mBinding.linearPreview.setVisibility(GONE);
					mBinding.preview.setImageResource(R.mipmap.ic_launcher); //Ens assegurem de lliberar memòria
					onDelete.run();
				}
			});
			mBinding.confirmar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					anima(mBinding.getRoot());
					mBinding.linearPreview.setVisibility(GONE);
					mBinding.preview.setImageResource(R.mipmap.ic_launcher); //Ens assegurem de lliberar memòria
				}
			});

			anima(mBinding.getRoot());
			mBinding.linearPreview.setVisibility(VISIBLE);
		} catch (OutOfMemoryError e) {
			Log.e(getTag(), "Out of memory: " + e.getLocalizedMessage(), e);
			Toast.makeText(mContext, "Out of memory", Toast.LENGTH_SHORT).show();
		}
	}

	public void disableViews() {
		mBinding.tvMarca.setEnabled(comprovarDependenciesMarca());
		mBinding.tvModel.setEnabled(comprovarDependenciesModel());
		mBinding.tvNum.setEnabled(comprovarDependenciesNumero());
	}

	private boolean comprovarDependenciesMarca() {
		return sancio.getModelTipusVehicle() != null;
	}

	private boolean comprovarDependenciesModel() {
		return sancio.getModelMarca() != null && comprovarDependenciesMarca();
	}

	private boolean comprovarDependenciesNumero() {
		return sancio.getModelCarrer() != null;
	}

	@Override
	public void onDestroy() {
		dismissDialog();
		super.onDestroy();
	}

	private void send() {
		DLog("Començo a fer la crida!!!");
		//getDialog(R.string.enviantDenuncia);

		Model_Denuncia denuncia = new Model_Denuncia();
		Date date;
		if (recuperada == false) {
			date = new Date();
			denuncia.setCodidenuncia(generateCodiButlleta(mContext));
			codiDenuncia=denuncia.getCodidenuncia();
		} else {
			date = dataCreacio;
			denuncia.setCodidenuncia(numDenuncia);

		}


		denuncia.setFechacreacio(date);
		long NUM = PreferencesGesblue.getIdAgent(mContext);
		denuncia.setAgent(PreferencesGesblue.getIdAgent(mContext));
		denuncia.setAdrecacarrer(sancio.getModelCarrer().getCodicarrer());
		if (sancio.getNumero().equals("S/N")) {
			denuncia.setAdrecanum(0);
		} else {
			denuncia.setAdrecanum(Double.parseDouble(sancio.getNumero()));
		}

		denuncia.setPosicio("");
		denuncia.setMatricula(sancio.getMatricula());
		denuncia.setTipusvehicle(sancio.getModelTipusVehicle().getCoditipusvehicle());
		denuncia.setMarca(Long.parseLong(sancio.getModelMarca().getCodimarca()));
		denuncia.setModel(Long.parseLong(sancio.getModelModel().getCodimodel()));
		denuncia.setColor(Long.parseLong(sancio.getModelColor().getCodicolor()));
		denuncia.setInfraccio(sancio.getModelInfraccio().getCodi());
		denuncia.setTerminal(Long.parseLong(PreferencesGesblue.getTerminal(mContext)));
		denuncia.setEstatcomprovacio(PreferencesGesblue.getEstatComprovacio(mContext));
		if (img1IsActive) {
			denuncia.setFoto1(foto1);
		}
		if (img2IsActive) {
			denuncia.setFoto2(foto2);
		}
		if (img3IsActive) {
			denuncia.setFoto3(foto3);
		}
		if (img4IsActive) {
			denuncia.setFoto4(foto4);
		}

		final ArrayList<Model_Denuncia> arrayDenuncies = new ArrayList<Model_Denuncia>();

		arrayDenuncies.add(denuncia);

		if (!recuperada && isFirstEnvia) {

			DatabaseAPI.insertDenuncies(mContext, arrayDenuncies);
			sendPhotos();
			int augmentar_Contador_Denuncia = PreferencesGesblue.getComptadorDenuncia(mContext) + 1;
			PreferencesGesblue.saveComptadorDenuncia(mContext, augmentar_Contador_Denuncia);
			isFirstEnvia = false;
		}
	}

	private void sendPhotos() {
		String concessio = Long.toString(PreferencesGesblue.getConcessio(mContext));
		String numDenuncia = generateCodiButlleta(mContext);
		if (img1IsActive) {
			File photo = new File(foto1);

			String fileName = photo.getName();
			String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			File newFile = new File("storage/emulated/0/Boka2/upload/" + date + "-" + concessio + "-" + numDenuncia + "1.jpg");
			photo.renameTo(newFile);
		}
		if (img2IsActive) {
			File photo = new File(foto2);

			String fileName = photo.getName();
			String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			File newFile = new File("storage/emulated/0/Boka2/upload/" + date + "-" + concessio + "-" + numDenuncia + "2.jpg");
			photo.renameTo(newFile);
		}
		if (img3IsActive) {
			File photo = new File(foto3);

			String fileName = photo.getName();
			String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			File newFile = new File("storage/emulated/0/Boka2/upload/" + date + "-" + concessio + "-" + numDenuncia + "3.jpg");
			photo.renameTo(newFile);
		}

		if (img4IsActive) {
			File photo = new File(foto4);

			String fileName = photo.getName();
			String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			File newFile = new File("storage/emulated/0/Boka2/upload/" + date + "-" + concessio + "-" + numDenuncia + "4.jpg");
			photo.renameTo(newFile);
		}
	}

	private void goToMain() {
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		GesblueApplication.DenunciaEnCurs = false;
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (mBinding.linearPreview.getVisibility() == VISIBLE) {
			anima(mBinding.getRoot(), 150);
			mBinding.linearPreview.setVisibility(GONE);
			mBinding.preview.setImageResource(R.mipmap.ic_launcher);
		} else {
			goToMain();
		}
	}

	private void printFinal(boolean isFirstTime) {
		if (isFirstTime) {
			send();
		}
		if (PreferencesGesblue.getTiquetUsuari(mContext) || PreferencesGesblue.getDataImportTiquet(mContext)) {
			Intent intent = new Intent(mContext, MoreInfoToPrintActivity.class);
			intent.putExtra(MoreInfoToPrintActivity.KEY_TIQUET, PreferencesGesblue.getTiquetUsuari(mContext));
			intent.putExtra(MoreInfoToPrintActivity.KEY_DATAIMPORT, PreferencesGesblue.getDataImportTiquet(mContext));
			intent.putExtra(INTENT_SANCIO, sancio);
			startActivityForResult(intent, REQUEST_MORE_OPTIONS);
		} else {
			try {
				runOnUiThread(new Runnable() {
					public void run() {
						getDialog(R.string.dialogImprimint);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			final String codibutlleta;
			Date fecha;
			if (recuperada) {
				codibutlleta = numDenuncia;
				fecha = dataCreacio;
			} else {
				codibutlleta = generateCodiButlleta(mContext);
				fecha = new Date();
			}
			PrintAsyncTask p = new PrintAsyncTask(mPrinter, mContext, sancio, codibutlleta, fecha, isFirstTime, new PrintAsyncTask.PrintListener() {
				@Override
				public void onFinish(Exception ex, boolean isFirstTime) {
					if (null == ex) {
						dismissDialog();
						if(errorDialog==false) {
							String test=codiDenuncia;
							CustomDialogClass ccd = new CustomDialogClass(FormulariActivity.this,codiDenuncia);
							ccd.show();
						}



					} else {
						ELog(ex);
						if (isFirstTime) {
							printFinal(false);
						} else {
							Utils.showCustomDialog(mContext, R.string.atencio, R.string.printerConnection_failedConnect, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dismissDialog();
								}
							});
						}

					}
				}

				@Override
				public void onError() {
					link();
				}
			});
			p.execute();
		}
	}

//	private void disableAllCamps() {
//		mBinding.tvTipus.setEnabled(false);
//		mBinding.tvMarca.setEnabled(false);
//		mBinding.tvModel.setEnabled(false);
//		mBinding.tvColor.setEnabled(false);
//		mBinding.tvInfraccio.setEnabled(false);
//		mBinding.tvCarrer.setEnabled(false);
//		mBinding.tvNum.setEnabled(false);
//	}
//
//	private void setBoto(final int value) {
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				mBinding.buttonAccepta.setVisibility(value == 0 ? VISIBLE : GONE);
//				mBinding.rlButtonPrint.setVisibility(value == 1 ? VISIBLE : GONE);
//				mBinding.rlButtonEnvia.setVisibility(value == 2 ? VISIBLE : GONE);
//			}
//		});
//
//	}

	private class CustomDialogClass extends Dialog implements
			android.view.View.OnClickListener {

		public Activity c;
		public Dialog d;
		public ConstraintLayout yesR, noR, yes;
		public String codiDenun;

		public CustomDialogClass(Activity a,String codiDenuncia) {
			super(a);
			// TODO Auto-generated constructor stub
			this.c = a;
			this.codiDenun=codiDenuncia;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.custom_dialog_impressio);
			yes = (ConstraintLayout) findViewById(R.id.lay_Yes);
			noR = (ConstraintLayout) findViewById(R.id.lay_NoR);
			yesR = (ConstraintLayout) findViewById(R.id.lay_YesR);
			yes.setOnClickListener(this);
			noR.setOnClickListener(this);
			yesR.setOnClickListener(this);
			this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.lay_Yes:
					String temp=codiDenun;

					DatabaseAPI.updateADenunciaImpresa(mContext, codiDenun);

					goToMain();
					break;

				case R.id.lay_YesR:
					DatabaseAPI.updateADenunciaImpresa(mContext, codiDenun);
					printFinal(isFirstEnvia);
					break;


				case R.id.lay_NoR:
					printFinal(isFirstEnvia);
					break;

			}
			dismiss();
		}
	}


	public static Calendar createCalendar(int aSumar) {
		if (aSumar == -1) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();

			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, 1); //Com van de 0 a 11, hi afegim 1 per tenir el correcte.
			calendar.add(Calendar.DATE, aSumar);

			return calendar;
		}
	}

	public static Calendar createCalendarCount(int aSumar) {
		if (aSumar == -1) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();

			calendar.setTime(new Date());
			//calendar.add(Calendar.MONTH, 1); //Com van de 0 a 11, hi afegim 1 per tenir el correcte.
			calendar.add(Calendar.DATE, aSumar);

			return calendar;
		}
	}

	private synchronized void waitForConnection() {
//		closeActiveConnection();

		// Show dialog to select a Bluetooth device.
		Intent intent = new Intent(mContext, DeviceListActivity.class);
		intent.putExtra(DeviceListActivity.KEY_VALID, true);
		startActivityForResult(intent, REQUEST_GET_DEVICE);

		// Start server to listen for network connection.
		try {
			mPrinterServer = new PrinterServer(new PrinterServerListener() {
				@Override
				public void onConnect(Socket socket) {
					Log.i(LOG_TAG, "Accept connection from " + socket.getRemoteSocketAddress().toString());

					// Close Bluetooth selection dialog
					finishActivity(REQUEST_GET_DEVICE);

					try {
						InputStream in = socket.getInputStream();
						OutputStream out = socket.getOutputStream();
						initPrinter(in, out);
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(mContext, "FAILED to initialize: " + e.getMessage(), Toast.LENGTH_LONG).show();
						waitForConnection();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void closeActiveConnection() {
		closePrinterConnection();
		closeBluetoothConnection();
		closePrinterServer();
	}

	private synchronized void closeBluetoothConnection() {
		// Close Bluetooth connection
		BluetoothSocket s = mBtSocket;
		mBtSocket = null;
		if (s != null) {
			Log.i(LOG_TAG, "Close Bluetooth socket");
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void closePrinterServer() {
		// Close network server
		PrinterServer ps = mPrinterServer;
		mPrinterServer = null;
		if (ps != null) {
			Log.i(LOG_TAG, "Close Network server");
			try {
				ps.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void closePrinterConnection() {
		if (mRC663 != null) {
			try {
				mRC663.disable();
			} catch (IOException e) {
				ELog(e);
			}

			mRC663.close();
		}

		if (mEMSR != null) {
			mEMSR.close();
		}

		if (mPrinter != null) {
			mPrinter.close();
		}

		if (mProtocolAdapter != null) {
			mProtocolAdapter.close();
		}
	}

	protected void initPrinter(InputStream inputStream, OutputStream outputStream) throws IOException {
		// Check if printer is into protocol mode. Ones the object is created it can not be released
		// without closing base streams.
		mProtocolAdapter = new ProtocolAdapter(inputStream, outputStream);
		if (mProtocolAdapter.isProtocolEnabled()) {
			Log.i(LOG_TAG, "Protocol mode is enabled");

			isLinked = true;


			// Into protocol mode we can callbacks to receive printer notifications
			mProtocolAdapter.setPrinterListener(new ProtocolAdapter.PrinterListener() {
				@Override
				public void onThermalHeadStateChanged(boolean overheated) {
					if (overheated) {
						Log.i(LOG_TAG, "Thermal head is overheated");
						showErrorImpresora(0);
					}
				}

				@Override
				public void onPaperStateChanged(boolean hasPaper) {
					if (hasPaper) {
						Log.i(LOG_TAG, "Event: Paper out");
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showErrorImpresora(1);
							}
						});
					}
				}

				@Override
				public void onBatteryStateChanged(boolean lowBattery) {
					if (lowBattery) {
						Log.i(LOG_TAG, "Low battery");
						showErrorImpresora(2);
					}
				}
			});

			// Get printer instance
			mPrinterChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
			mPrinter = new com.datecs.api.printer.Printer(mPrinterChannel.getInputStream(), mPrinterChannel.getOutputStream());
			isLinked = true;

		} else {
			Log.i(LOG_TAG, "Protocol mode is disabled");

			// Protocol mode it not enables, so we should use the row streams.
			mPrinter = new com.datecs.api.printer.Printer(mProtocolAdapter.getRawInputStream(), mProtocolAdapter.getRawOutputStream());
		}

		mPrinter.setConnectionListener(new com.datecs.api.printer.Printer.ConnectionListener() {
			@Override
			public void onDisconnect() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mContext, getString(R.string.printer_disconnected), Toast.LENGTH_SHORT).show();
						isLinked = false;
						if (!isFinishing()) {
							waitForConnection();
						}
					}
				});
			}
		});

	}

	private void showErrorImpresora(int code) {

		switch (code) {
			case 0:
				Utils.showCustomDialog(mContext, R.string.errorAtencio, R.string.errorTemperatura);
				errorDialog=true;
				break;
			case 1:
				Utils.showCustomDialog(mContext, R.string.errorAtencio, R.string.errorPaper);
				errorDialog=true;
				break;
			case 2:
				Utils.showCustomDialog(mContext, R.string.errorAtencio, R.string.errorBateria);
				errorDialog=true;
				break;
		}
	}

	private void establishBluetoothConnection(final String address) {
		getDialog(R.string.title_please_wait, R.string.msg_connecting);

		closeActiveConnection();

		final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(LOG_TAG, "Connecting to " + address + "...");

				btAdapter.cancelDiscovery();

				try {
					UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
					BluetoothDevice btDevice = btAdapter.getRemoteDevice(address);

					InputStream in;
					OutputStream out;

					try {
						BluetoothSocket btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(uuid);
						btSocket.connect();

						mBtSocket = btSocket;
						in = mBtSocket.getInputStream();
						out = mBtSocket.getOutputStream();
					} catch (final IOException e) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (btAdapter.getState() == BluetoothAdapter.STATE_OFF) {
									Utils.showCustomDialog(mContext, R.string.printerConnection_failedTitle, R.string.printerConnection_failedEngegaBluetooth);
									errorDialog=true;
								} else {
									Utils.showCustomDialog(mContext, R.string.printerConnection_failedTitle, R.string.printerConnection_failedConnect);
									errorDialog=true;
									PreferencesGesblue.saveAdressBluetoothPrinter(mContext, "");
								}
								DLog("FAILED to connect: " + e.getMessage());
								dismissDialog();
//								waitForConnection();
							}
						});
						return;
					}

					try {
						initPrinter(in, out);
					} catch (final IOException e) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (btAdapter.getState() == BluetoothAdapter.STATE_ON) {
									Utils.showCustomDialog(mContext, R.string.printerConnection_failedTitle, R.string.printerConnection_failedInitialize);
									errorDialog=true;
								}
								DLog("FAILED to initiallize: " + e.getMessage());
							}
						});
					}
				} finally {
					DLog("Tancant dialeg a establishBluetoothConnection");
					dismissDialog();

					printFinal(true);
				}
			}
		});
		t.start();
	}

}