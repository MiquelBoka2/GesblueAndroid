package com.boka2.gesblue.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boka2.gesblue.GesblueApplication;
import com.boka2.gesblue.activities.passosformulari.Pas7NumeroActivity;
import com.boka2.gesblue.datamanager.database.model.Model_Zona;
import com.boka2.sbaseobjects.tools.ImageTools;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.boka2.gesblue.R;
import com.boka2.gesblue.Sancio;
import com.boka2.gesblue.activities.passosformulari.Pas0ZonaActivity;
import com.boka2.gesblue.activities.passosformulari.Pas6CarrerActivity;
import com.boka2.gesblue.customstuff.GesblueFragmentActivity;
import com.boka2.gesblue.databinding.ActivityMainBinding;
import com.boka2.gesblue.datamanager.DatabaseAPI;
import com.boka2.gesblue.datamanager.database.model.Model_LlistaAbonats;
import com.boka2.gesblue.datamanager.database.model.Model_LlistaBlanca;
import com.boka2.gesblue.datamanager.webservices.DatamanagerAPI;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.ComprovaMatriculaRequest;
import com.boka2.gesblue.datamanager.webservices.results.operativa.ComprovaMatriculaResponse;
import com.boka2.gesblue.global.Constants;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.global.Utils;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import pt.joaocruz04.lib.misc.JSoapCallback;
import pt.joaocruz04.lib.misc.JsoapError;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static pt.joaocruz04.lib.misc.JsoapError.PARSE_ERROR;

public class MainActivity extends GesblueFragmentActivity {

	private ActivityMainBinding mBinding;
	private Menu menu;
	private String estat="main";
	private ImageView opciones;
	private TextView localitzacio;
	private int RequestCode=0001;


	private String pathS;
	private File path;

	private String foto1;
	private String foto2;
	private String foto3;
	private String foto4;
	private boolean imgAIsActive = false;
	private boolean imgBIsActive = false;
	private boolean imgCIsActive = false;
	private boolean imgDIsActive = false;
	private Bitmap imageBitmap;

	private Boolean adm=false;

	private long dataCaducitat_milisegons=0;

	private Date dataComprovacio;

	private Activity mActivity=this;

	private File root = new File("storage/emulated/0/Boka2/upload/temp/created");
	private File loc_foto1;
	private File loc_foto2;
	private File loc_foto3;
	private File loc_foto4;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FirebaseCrashlytics.getInstance().setUserId(PreferencesGesblue.getConcessio(mContext)+"-"+PreferencesGesblue.getCodiAgent(mContext));
		FirebaseCrashlytics.getInstance().setCustomKey("concessió", PreferencesGesblue.getConcessio(mContext)+"-"+PreferencesGesblue.getConcessioString(mContext));
		FirebaseCrashlytics.getInstance().setCustomKey("UUID", PreferencesGesblue.getPrefEternUUID(mContext));
		FirebaseCrashlytics.getInstance().setCustomKey("codi_agent", PreferencesGesblue.getCodiAgent(mContext));


		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		setupVisibleToolbar(mBinding.toolbar);
		GesblueApplication.DenunciaEnCurs =false;
		Bundle extras = getIntent().getExtras();



		if (root.isDirectory())
		{
			String[] children = root.list();
			for (int i = 0; i < children.length; i++)
			{
				new File(root, children[i]).delete();
			}
		}

		root.mkdirs();


		File low_res=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Boka2" + File.separator + "upload" + File.separator +"low_res" + File.separator);
		if (low_res.isDirectory())
		{
			String[] children = low_res.list();
			for (int i = 0; i < children.length; i++)
			{
				new File(low_res, children[i]).delete();
			}
		}


		File original=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Boka2" + File.separator + "upload" + File.separator +"original" + File.separator);
		if (original.isDirectory())
		{
			String[] children = original.list();
			for (int i = 0; i < children.length; i++)
			{
				new File(original, children[i]).delete();
			}
		}



		try {
			loc_foto1=root.createTempFile("foto_temp_1_",
					".jpg",root);
			loc_foto2=root.createTempFile("foto_temp_2_",
					".jpg",root);
			loc_foto3=root.createTempFile("foto_temp_3_",
					".jpg",root);
			loc_foto4=root.createTempFile("foto_temp_4_",
					".jpg",root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (extras != null) {
			adm = extras.getBoolean("adm");

		}

		checkEstat(adm,PreferencesGesblue.getOffline(mContext));
		localitzacio=mBinding.toolbar.txtLocalitzacioEstat;
		opciones=mBinding.toolbar.icOpciones;



		opciones.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, Opcions.class);
				intent.putExtra("estat",estat);
				intent.putExtra("adm",adm);
				startActivityForResult(intent,RequestCode);
			}
		});

		Runnable runnable = new Runnable()
		{
			@Override
			public void run() {
				Glide.get(mContext).clearDiskCache();
			}
		};
		new Thread(runnable).start();
		Glide.get(mContext).clearMemory();

		String concessio = PreferencesGesblue.getConcessioString(mContext);
		localitzacio=mBinding.toolbar.txtLocalitzacioEstat;
		localitzacio.setText(concessio);
		Log.e("La conzezió", concessio+"*****");

		if (concessio == null || concessio.equals("")){
			Utils.showCustomDialog2(mContext, R.string.atencio, R.string.errorConcessio, R.string.dacord,-1, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					PreferencesGesblue.setUserName(mContext,"");
					PreferencesGesblue.setPassword(mContext,"");
					PreferencesGesblue.setConcessioString(mContext,"");

					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);

					finish();
				}
			}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			}, false);

		}



		mBinding.editTextMatricula.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
		mBinding.toolbar.txtLocalitzacioEstat.setText(concessio);

		mBinding.tvZona.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(mContext, Pas0ZonaActivity.class);
				intent.putExtra("formPrimerCop", false);
				intent.putExtra("adm",adm);
				startActivityForResult(intent,1);
			}
		});
		mBinding.tvCarrer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(mBinding.tvZona.getText().toString()!=getResources().getString(R.string.zona) & mBinding.tvZona.getText().toString()!="" & mBinding.tvZona.getText().toString()!=null){
					Intent intent = new Intent(mContext, Pas6CarrerActivity.class);
					intent.putExtra("formPrimerCop", false);
					intent.putExtra("adm",adm);
					startActivityForResult(intent,2);
				}
				else{
					Toast.makeText(mContext,getResources().getString(R.string.seleciona_zona_primer), Toast.LENGTH_LONG).show();
				}

			}
		});
		mBinding.tvNum.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(mBinding.tvCarrer.getText().toString()!=getResources().getString(R.string.carrer) & mBinding.tvCarrer.getText().toString()!="" & mBinding.tvCarrer.getText().toString()!=null){
					Intent intent = new Intent(mContext, Pas7NumeroActivity.class);
					intent.putExtra("formPrimerCop", false);
					intent.putExtra("adm",adm);
					startActivityForResult(intent,3);
					mBinding.tvNum.clearFocus();
				}
				else{
					Toast.makeText(mContext,getResources().getString(R.string.seleciona_carrer_primer), Toast.LENGTH_LONG).show();
					mBinding.tvNum.clearFocus();
				}
			}
		});
		mBinding.tvNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				if(b){
					mBinding.tvNum.callOnClick();
				}
			}
		});





		//mBinding.tv.setOnClickListener(this);
		mBinding.buttonComprovar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String matricula = mBinding.editTextMatricula.getText().toString();

				if(matricula.equals("") || PreferencesGesblue.getCodiZona(mContext)==0 ||PreferencesGesblue.getCodiCarrer(mContext)==0) {
					Utils.showFaltenDadesError(mContext);
				} else {
					comprovarMatricula(matricula);
				}
			}
		});

		mBinding.buttonAcceptar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				changeViewComprovarMatricula();
			}
		});

		mBinding.buttonDenunciar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				if(PreferencesGesblue.getEstatComprovacio(mContext)==1 || PreferencesGesblue.getEstatComprovacio(mContext)==3|| PreferencesGesblue.getEstatComprovacio(mContext)==4){

				}

				if(Check_Null_Data(dataComprovacio,mContext)) {//DATA NO NULL
					//DATA NO NULL
					Sancio sancio = new Sancio();
					sancio.setMatricula(mBinding.editTextMatricula.getText().toString());
					sancio.setNumero(mBinding.tvNum.getText().toString());
					Model_Zona zona = new Model_Zona();
					zona.setCodizona(PreferencesGesblue.getCodiZona(mContext));
					zona.setNomzona(PreferencesGesblue.getNomZona(mContext));
					sancio.setModelZona(zona);

					Intent intent = new Intent(mContext, FormulariActivity.class);
					PreferencesGesblue.setFoto1(mContext, foto1);
					PreferencesGesblue.setFoto2(mContext, foto2);
					PreferencesGesblue.setFoto3(mContext, foto3);
					PreferencesGesblue.setFoto4(mContext, foto4);
					intent.putExtra(FormulariActivity.INTENT_SANCIO, sancio);
					intent.putExtra(FormulariActivity.KEY_VINC_DE_MATRICULA, true);
					intent.putExtra("adm", adm);
					FormulariActivity.dataComprovacio = dataComprovacio;
					intent.putExtra("dataComprovacio", dataComprovacio);
					GesblueApplication.DenunciaEnCurs = true;
					startActivity(intent);
				}
				else{
					//DATA NULL
				}

			}
		});

		mBinding.buttonNoDenunciar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(PreferencesGesblue.getEstatComprovacio(mContext)==1 || PreferencesGesblue.getEstatComprovacio(mContext)==3|| PreferencesGesblue.getEstatComprovacio(mContext)==4){

				}
				foto1=null;
				foto2=null;
				foto3=null;
				foto4=null;
				imgAIsActive=false;
				imgBIsActive=false;
				imgCIsActive=false;
				imgDIsActive=false;
				mBinding.imageViewA.setImageDrawable(null);
				mBinding.imageViewB.setImageDrawable(null);
				mBinding.imageViewC.setImageDrawable(null);
				mBinding.imageViewD.setImageDrawable(null);

				if (root.isDirectory())
				{
					String[] children = root.list();
					for (int i = 0; i < children.length; i++)
					{
						new File(root, children[i]).delete();
					}
				}

				changeViewComprovarMatricula();
			}
		});





		mBinding.btnCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String model= Build.MANUFACTURER
						+ " " + Build.MODEL;


				if (isEmpty(foto1)) {
					if(model.equals("bq Aquaris U")){

						Intent intent = new Intent(mContext, CameraActivity.class);
						intent.putExtra("position", "1");
						startActivityForResult(intent, Utils.RESULT_FOTO_1);

					}
					else{
						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							Uri photoURI =FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", loc_foto1);
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_1);
						}
					}


				} else if (isEmpty(foto2)) {
					if(model.equals("bq Aquaris U")){

						Intent intent = new Intent(mContext, CameraActivity.class);
						intent.putExtra("position", "2");
						startActivityForResult(intent, Utils.RESULT_FOTO_2);

					}
					else{
						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							Uri photoURI =FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", loc_foto2);
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_2);
						}
					}
				} else if (isEmpty(foto3)) {
					if(model.equals("bq Aquaris U")){

						Intent intent = new Intent(mContext, CameraActivity.class);
						intent.putExtra("position", "3");
						startActivityForResult(intent, Utils.RESULT_FOTO_3);

					}
					else{
						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							Uri photoURI =FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", loc_foto3);
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_3);
						}
					}
				} else if (isEmpty(foto4)) {
					if(model.equals("bq Aquaris U")){

						Intent intent = new Intent(mContext, CameraActivity.class);
						intent.putExtra("position", "4");
						startActivityForResult(intent, Utils.RESULT_FOTO_4);

					}
					else{
						Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
							Uri photoURI =FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", loc_foto4);
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
							startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE_4);
						}
					}
				} else {
					AlertDialog.Builder builder= new AlertDialog.Builder(mContext)
							.setTitle(getResources().getString(R.string.no_mes_fotos))
							.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();

								}
							});


					AlertDialog alert = builder.create();
					alert.show();
					Button ybutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
					ybutton.setTextColor(Color.BLACK);
				}
			}
		});



		mBinding.imageViewA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(imgAIsActive){
					confirmPicture(mBinding.imageViewA, foto1, new Runnable() {
						@Override
						public void run() {

							borra(foto1);
							foto1 = null;
							imgAIsActive = false;
							checkBotoCamera();
						}
					});
				}
			}
		});

		mBinding.imageViewB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(imgBIsActive){
					confirmPicture(mBinding.imageViewB, foto2, new Runnable() {
						@Override
						public void run() {

							borra(foto2);
							foto2 = null;
							imgBIsActive = false;
							checkBotoCamera();
						}
					});
				}
			}
		});

		mBinding.imageViewC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(imgCIsActive){
					confirmPicture(mBinding.imageViewC, foto3, new Runnable() {
						@Override
						public void run() {

							borra(foto3);
							foto3 = null;
							imgCIsActive = false;
							checkBotoCamera();
						}
					});
				}
			}
		});

		mBinding.imageViewD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(imgDIsActive){
					confirmPicture(mBinding.imageViewD, foto4, new Runnable() {
						@Override
						public void run() {

							borra(foto4);
							foto4 = null;
							imgDIsActive = false;
							checkBotoCamera();
						}
					});
				}
			}
		});


/*		final ArrayList<Model_LlistaBlanca> listLogs = DatabaseAPI.getLlistaBlanca(mContext);

		Log.d("Num llistaBlanca local",""+listLogs.size());

		final ArrayList<Model_LlistaAbonats> listLogs2 = DatabaseAPI.getLlistaAbonats(mContext);

		Log.d("Num llistaAbonats local",""+listLogs2.size());*/



		/*ESTAT PER DEFECTE DE LES DADES*/{
			if(PreferencesGesblue.getPrefLastConcessio(mContext).equals(concessio)){
				PreferencesGesblue.setCodiCarrer(mContext, PreferencesGesblue.getPrefLastCodiCarrer(mContext));
				PreferencesGesblue.setNomCarrer(mContext, PreferencesGesblue.getPrefLastNomCarrer(mContext));
				PreferencesGesblue.setCodiZona(mContext, PreferencesGesblue.getPrefLastCodiZona(mContext));
				PreferencesGesblue.setNomZona(mContext, PreferencesGesblue.getPrefLastNomZona(mContext));


				mBinding.tvZona.setText(PreferencesGesblue.getPrefLastNomZona(mContext));
				mBinding.tvCarrer.setText(PreferencesGesblue.getPrefLastNomCarrer(mContext));


			}
			else{
				PreferencesGesblue.setPrefLastConcessio(mContext,concessio);
				PreferencesGesblue.setCodiCarrer(mContext, 0);
				PreferencesGesblue.setNomCarrer(mContext, null);
				PreferencesGesblue.setCodiZona(mContext, 0);
				PreferencesGesblue.setNomZona(mContext, null);
				mBinding.tvZona.setText(PreferencesGesblue.getZonaDefaultValue(mContext));
				mBinding.tvCarrer.setText(PreferencesGesblue.getCarrerDefaultValue(mContext));
			}
		}
	}


	/* Actualitza el UI en funcio de ADMIN**/
	private void checkEstat(Boolean adm,Boolean offline) {

		if (adm){
			mBinding.toolbar.imgUnlock.setVisibility(View.VISIBLE);
			mBinding.toolbar.toolbarBackground.setBackgroundColor(getResources().getColor(R.color.admin));
			mBinding.toolbar.txtLocalitzacioEstat.setBackgroundColor(getResources().getColor(R.color.admin));
		}
		else{
			mBinding.toolbar.imgUnlock.setVisibility(View.GONE);
			mBinding.toolbar.toolbarBackground.setBackgroundColor(getResources().getColor(R.color.barra_estat));
			mBinding.toolbar.txtLocalitzacioEstat.setBackgroundColor(getResources().getColor(R.color.barra_estat));
		}

		if(offline){
			mBinding.toolbar.toolbarBackground.setBackgroundColor(getResources().getColor(R.color.vermellKO));
			mBinding.toolbar.txtLocalitzacioEstat.setBackgroundColor(getResources().getColor(R.color.vermellKO));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		GesblueApplication.DenunciaEnCurs =false;
		mBinding.tvZona.setText(PreferencesGesblue.getNomZona(mContext));
		mBinding.tvCarrer.setText(PreferencesGesblue.getNomCarrer(mContext));
		//mBinding.tvNum.setText(PreferencesGesblue.getFormulariNumero(mContext));
		checkEstat(adm,PreferencesGesblue.getOffline(mContext));



	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle extras=null;
			Boolean result=null;
			if(data!=null&&data.getExtras()!=null) {
				extras = data.getExtras();
				result = extras.getBoolean("adm");
			}
			imageBitmap=null;
			switch (requestCode) {

				/*ESPEREM EL RESULTAT DE LES FOTOS**/
				case Utils.REQUEST_IMAGE_CAPTURE_1:


					if(imageBitmap==null && data!=null&& data.getExtras()!=null ){
						imageBitmap = (Bitmap) extras.get("data");
					}
					else{
						imageBitmap= ImageTools.getBitmapAfterResize(loc_foto1);
					}
					foto1=Utils.savePicture(imageBitmap,mContext,"1");
					pinta(foto1, mBinding.imageViewA);
					imgAIsActive = true;
					checkBotoCamera();
					break;



				case Utils.REQUEST_IMAGE_CAPTURE_2:

					if(imageBitmap==null && data!=null&& data.getExtras()!=null ){
						imageBitmap = (Bitmap) extras.get("data");
					}
					else{

						imageBitmap= ImageTools.getBitmapAfterResize(loc_foto2);
					}
					foto2=Utils.savePicture(imageBitmap,mContext,"2");
					pinta(foto2, mBinding.imageViewB);
					imgBIsActive = true;
					checkBotoCamera();
					break;



				case Utils.REQUEST_IMAGE_CAPTURE_3:

					if(imageBitmap==null && data!=null&& data.getExtras()!=null ){
						imageBitmap = (Bitmap) extras.get("data");
					}
					else{
						imageBitmap= ImageTools.getBitmapAfterResize(loc_foto3);
					}
					foto3=Utils.savePicture(imageBitmap,mContext,"3");
					pinta(foto3, mBinding.imageViewC);
					imgCIsActive = true;
					checkBotoCamera();
					break;


				case Utils.REQUEST_IMAGE_CAPTURE_4:

					if(imageBitmap==null && data!=null&& data.getExtras()!=null ){
						imageBitmap = (Bitmap) extras.get("data");
					}
					else{
						imageBitmap= ImageTools.getBitmapAfterResize(loc_foto4);
					}
					foto4=Utils.savePicture(imageBitmap,mContext,"4");
					pinta(foto4, mBinding.imageViewD);
					imgDIsActive = true;
					checkBotoCamera();
					break;


				/*ESPEREM EL RESULTAT DE LES FOTOS BQ Aquaris U**/
				case Utils.RESULT_FOTO_1:

					String pathS = data.getExtras().getString(Utils.KEY_RETURN_PATH);
					File path= new File(pathS);
					imageBitmap= ImageTools.getBitmapAfterResize(path).copy(Bitmap.Config.RGB_565,true);
					foto1=Utils.savePicture(imageBitmap,mContext,"1");
					pinta(foto1, mBinding.imageViewA);
					imgAIsActive = true;
					checkBotoCamera();
					break;
				case Utils.RESULT_FOTO_2:
					pathS = data.getExtras().getString(Utils.KEY_RETURN_PATH);
					path= new File(pathS);
					imageBitmap= ImageTools.getBitmapAfterResize(path).copy(Bitmap.Config.RGB_565,true);
					foto2=Utils.savePicture(imageBitmap,mContext,"2");
					pinta(foto2, mBinding.imageViewB);
					imgBIsActive = true;
					checkBotoCamera();
					break;
				case Utils.RESULT_FOTO_3:
					pathS = data.getExtras().getString(Utils.KEY_RETURN_PATH);
					path= new File(pathS);
					imageBitmap= ImageTools.getBitmapAfterResize(path).copy(Bitmap.Config.RGB_565,true);
					foto3=Utils.savePicture(imageBitmap,mContext,"3");
					pinta(foto3, mBinding.imageViewC);
					imgCIsActive = true;
					checkBotoCamera();
					break;
				case Utils.RESULT_FOTO_4:
					pathS = data.getExtras().getString(Utils.KEY_RETURN_PATH);
					path= new File(pathS);
					imageBitmap= ImageTools.getBitmapAfterResize(path).copy(Bitmap.Config.RGB_565,true);
					foto4=Utils.savePicture(imageBitmap,mContext,"4");
					pinta(foto4, mBinding.imageViewD);
					imgDIsActive = true;
					checkBotoCamera();
					break;


				/*ESPEREM EL RESULTAT DE LA ZONA i EL CARRER**/
				case 1: {

					mBinding.tvZona.setText(PreferencesGesblue.getNomZona(mContext));
				}
				case 2: {

					mBinding.tvCarrer.setText(PreferencesGesblue.getNomCarrer(mContext));

				}
				case 3:{
					mBinding.tvNum.setText(PreferencesGesblue.getFormulariNumero(mContext));
				}



			}
			/* CHECK ADMIN**/

			if (result != null) {
				adm = result;


			}
			checkEstat(adm,PreferencesGesblue.getOffline(mContext));

		}

	}

	private void comprovarMatricula(String matricula) {
		final String mat = matricula;
		amagarTeclat();


		final boolean multable = true;
		Log.d("crida0", Constants.OPERATIVA_COMPROVAMATRICULA_METHOD);
		Log.d("params",PreferencesGesblue.getConcessio(mContext)+" - "+Utils.getDeviceId(mContext)+" - "+matricula+" - "+ Utils.getCurrentTimeLong(mContext)+" - "+PreferencesGesblue.getCodiCarrer(mContext)+" - "+PreferencesGesblue.getCodiZona(mContext)+" - "+PreferencesGesblue.getCodiAgent(mContext));


		//GUARDEM DE FORMA TEMPORAL LA ACTUAL DATA(per sobre escriure la vella)
		dataComprovacio= new Date();
		Check_Null_Data(dataComprovacio, mContext);//DATA NO NULL

		mBinding.txtEstatEstacionament.setText("");
		mBinding.txtTemps.setText("");
		mBinding.txtInfo.setText("");


		if(!PreferencesGesblue.getOffline(mContext)) {
			DatamanagerAPI.crida_ComprovaMatricula(new ComprovaMatriculaRequest(PreferencesGesblue.getConcessio(mContext), Utils.getDeviceId(mContext), matricula, Utils.getCurrentTimeLong(mContext), PreferencesGesblue.getCodiCarrer(mContext), PreferencesGesblue.getCodiZona(mContext)), PreferencesGesblue.getTimeOut(mContext), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					final ComprovaMatriculaResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, ComprovaMatriculaResponse.class);
						Log.d("Resultat", "" + response.getResultat());
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						Log.e("ERROR COMPROVACIO:", "");
						return;
					}

					//GUADEM LA DATA EN CAS DE CONEXXIO
					dataComprovacio = new Date();
					Check_Null_Data(dataComprovacio, mContext);//DATA NO NULL


					Long data = Utils.getCurrentTimeLong(mContext);

					Long temps = response.getTemps();


					dataCaducitat_milisegons = (System.currentTimeMillis()) + temps * 1000;


					Log.d("EstatComprovacio", "" + PreferencesGesblue.getEstatComprovacio(mContext));

					int estatComprovacio = 0;

					mBinding.txtTemps.setVisibility(INVISIBLE);
					mBinding.txtInfo.setVisibility(INVISIBLE);

					switch (response.getResultat()) {
						case 0: //Matricula correcta(no denunciar)
							if ((temps > 0)) {

								estatComprovacio = 1;


								mBinding.txtInfo.setVisibility(VISIBLE);
								mBinding.txtTemps.setVisibility(VISIBLE);
								mBinding.txtTemps.setText(Check_Temps(dataCaducitat_milisegons));
								mBinding.txtEstatEstacionament.setText(getResources().getString(R.string.estacionament_correcte));

								changeViewNoMultable();
							} else {
								mBinding.txtInfo.setVisibility(VISIBLE);
								mBinding.txtTemps.setVisibility(VISIBLE);
								mBinding.txtInfo.setText("");
								mBinding.txtTemps.setText("");
								mBinding.txtEstatEstacionament.setText(getResources().getString(R.string.estacionament_correcte));

								changeViewNoMultable();
								estatComprovacio = 2;
							}

							break;
						case -1: //Matricula no correcta(possibilitat de denunciar)
							Log.d("Temps:", "" + response.getTemps());
							if ((temps < 0) && (temps > -50400)) {
								if (temps > -3600) {

									mBinding.txtInfo.setVisibility(VISIBLE);
									mBinding.txtTemps.setVisibility(VISIBLE);
									mBinding.txtTemps.setText(Check_Temps(dataCaducitat_milisegons));
									changeViewMultable();


									estatComprovacio = 3;
								} else {


									mBinding.txtInfo.setVisibility(VISIBLE);
									mBinding.txtTemps.setVisibility(VISIBLE);
									mBinding.txtTemps.setText(Check_Temps(dataCaducitat_milisegons));
									changeViewMultable();

									estatComprovacio = 4;
								}
							} else {
								mBinding.txtInfo.setVisibility(VISIBLE);
								mBinding.txtInfo.setText("");
								mBinding.txtTemps.setText("");
								mBinding.txtInfo.setText(getResources().getString(R.string.sense_tiquet));
								changeViewMultable();
								estatComprovacio = 5;
							}
							break;
						case -3: //Vehicle ja denunciat
							changeViewJaDenunciat();
							estatComprovacio = 6;
							break;
						case -2: //Error de comprovació
						default:
							Utils.showDatamanagerError(mContext, JsoapError.OTHER_ERROR);
							mBinding.editTextMatricula.setEnabled(true);
							mBinding.tvZona.setEnabled(true);
							mBinding.tvCarrer.setEnabled(true);
							mBinding.tvNum.setEnabled(true);
							estatComprovacio = 7;

							break;
					}

					PreferencesGesblue.saveEstatComprovacio(mContext, estatComprovacio);

				}

				@Override
				public void onError(final int error) {
					int estatComprovacio = 0;

					//GUADEM LA DATA EN CAS DE NO CONEXXIO
					dataComprovacio = new Date();
					Check_Null_Data(dataComprovacio, mContext);//DATA NO NULL

					mBinding.viewSwitcherComprovaAnim.showNext();
					mBinding.editTextMatricula.setEnabled(true);
					mBinding.tvZona.setEnabled(true);
					mBinding.tvCarrer.setEnabled(true);
					mBinding.tvNum.setEnabled(true);

					//Utils.showDatamanagerError(mContext, error);

					Model_LlistaBlanca llistaBlanca = DatabaseAPI.findLlistaBlanca(mContext, mat);

					if (llistaBlanca == null) {


						Model_LlistaAbonats llistaAbonats = DatabaseAPI.findLlistaAbonats(mContext, mat);

						if (llistaAbonats == null) {

							estatComprovacio = 8;
							changeViewNoComprovat();

						} else {
							try {
								SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
								Date datainici = fmt.parse(llistaAbonats.getDatainici());
								Date datafi = fmt.parse(llistaAbonats.getDatafi());
								Date date = new Date();
								Long Zona_selct=PreferencesGesblue.getCodiZona(mContext);
								Long zona1= llistaAbonats.getZona1();
								Long zona2= llistaAbonats.getZona2();
								Long zona3= llistaAbonats.getZona3();
								Long zona4= llistaAbonats.getZona4();
								Long zona5= llistaAbonats.getZona5();

								boolean zonna_correcte=Zona_selct.equals(zona1) ||Zona_selct.equals(zona2) ||Zona_selct.equals(zona3) || Zona_selct.equals(zona4) ||Zona_selct.equals(zona5);

								if (date.after(datainici) && date.before(datafi) &&	zonna_correcte){
									estatComprovacio = 9;
									changeViewNoMultable();
								} else {
									estatComprovacio = 8;
									changeViewNoComprovat();
								}
							} catch (ParseException pex) {
								estatComprovacio = 8;
								changeViewNoComprovat();
							}

							Log.d("comprovacio", "abonat");


						}
					} else {
						Log.d("comprovacio", "blanca");
						estatComprovacio = 10;
						changeViewNoMultable();

					}

					PreferencesGesblue.saveEstatComprovacio(mContext, estatComprovacio);
				}
			});
		}
		else{
			int estatComprovacio = 0;

			//GUADEM LA DATA EN CAS DE NO CONEXXIO
			dataComprovacio = new Date();
			Check_Null_Data(dataComprovacio, mContext);//DATA NO NULL

			mBinding.viewSwitcherComprovaAnim.showNext();
			mBinding.editTextMatricula.setEnabled(true);
			mBinding.tvZona.setEnabled(true);
			mBinding.tvCarrer.setEnabled(true);
			mBinding.tvNum.setEnabled(true);

			//Utils.showDatamanagerError(mContext, error);

			Model_LlistaBlanca llistaBlanca = DatabaseAPI.findLlistaBlanca(mContext, mat);

			if (llistaBlanca == null) {


				Model_LlistaAbonats llistaAbonats = DatabaseAPI.findLlistaAbonats(mContext, mat);

				if (llistaAbonats == null) {

					estatComprovacio = 8;
					changeViewNoComprovat();

				} else {
					try {
						SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						Date datainici = fmt.parse(llistaAbonats.getDatainici());
						Date datafi = fmt.parse(llistaAbonats.getDatafi());
						Date date = new Date();
						Long Zona_selct=PreferencesGesblue.getCodiZona(mContext);
						Long zona1= llistaAbonats.getZona1();
						Long zona2= llistaAbonats.getZona2();
						Long zona3= llistaAbonats.getZona3();
						Long zona4= llistaAbonats.getZona4();
						Long zona5= llistaAbonats.getZona5();

						boolean zonna_correcte=Zona_selct.equals(zona1) ||Zona_selct.equals(zona2) ||Zona_selct.equals(zona3) || Zona_selct.equals(zona4) ||Zona_selct.equals(zona5);

						if (date.after(datainici) && date.before(datafi) &&	zonna_correcte){
							estatComprovacio = 9;
							changeViewNoMultable();
						} else {
							estatComprovacio = 8;
							changeViewNoComprovat();
						}
					} catch (ParseException pex) {
						estatComprovacio = 8;
						changeViewNoComprovat();
					}

					Log.d("comprovacio", "abonat");


				}
			} else {
				Log.d("comprovacio", "blanca");
				estatComprovacio = 10;
				changeViewNoMultable();

			}

			PreferencesGesblue.saveEstatComprovacio(mContext, estatComprovacio);
		}
	}

	private void changeViewNoMultable() {
		//Amaguem el nom de l'ajuntament per qüestió estètica.

		mBinding.txtEstatEstacionament.setVisibility(View.VISIBLE);
		mBinding.txtTemps.setVisibility(View.VISIBLE);
		mBinding.txtInfo.setVisibility(View.VISIBLE);

		mBinding.layDades.setBackgroundColor(getResources().getColor(R.color.verdOK));
		mBinding.txtEstatEstacionament.setText(getResources().getString(R.string.estacionament_correcte));
		mBinding.txtEstatEstacionament.setVisibility(View.VISIBLE);

		//Recomana NO Denunciar
		mBinding.buttonDenunciar.setBackground(getResources().getDrawable(R.drawable.button_white_selector));
		mBinding.buttonNoDenunciar.setBackground(getResources().getDrawable(R.drawable.button_selector));

		mBinding.buttonDenunciar.setTextColor(getResources().getColor(R.color.text_no_recomenat));
		mBinding.buttonNoDenunciar.setTextColor(getResources().getColor(R.color.text_recomenat));


		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.tvZona.setEnabled(false);
		mBinding.tvCarrer.setEnabled(false);
		mBinding.tvNum.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);

		mBinding.llBtnDenuncies.setVisibility(View.VISIBLE);


		mBinding.buttonComprovar.setVisibility(View.GONE);

	}

	private void changeViewJaDenunciat() {
		//Amaguem el nom de l'ajuntament per qüestió estètica.

		mBinding.txtEstatEstacionament.setVisibility(View.GONE);
		mBinding.txtTemps.setVisibility(View.GONE);
		mBinding.txtInfo.setVisibility(View.VISIBLE);

		mBinding.layDades.setBackgroundColor(getResources().getColor(R.color.ja_denunciat));

		mBinding.txtInfo.setText(R.string.vehicle_ja_denunciat);

		mBinding.layImatges.setVisibility(View.VISIBLE);

		mBinding.llBtnDenuncies.setVisibility(View.VISIBLE);

		//Recomana NO Denunciar
		mBinding.buttonDenunciar.setBackground(getResources().getDrawable(R.drawable.button_white_selector));
		mBinding.buttonNoDenunciar.setBackground(getResources().getDrawable(R.drawable.button_selector));

		mBinding.buttonDenunciar.setTextColor(getResources().getColor(R.color.text_no_recomenat));
		mBinding.buttonNoDenunciar.setTextColor(getResources().getColor(R.color.text_recomenat));



		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.tvZona.setEnabled(false);
		mBinding.tvCarrer.setEnabled(false);
		mBinding.tvNum.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);
		mBinding.buttonComprovar.setVisibility(View.GONE);
	}

	private void changeViewMultable() {
		//Amaguem el nom de l'ajuntament per qüestió estètica.

		mBinding.txtEstatEstacionament.setVisibility(View.VISIBLE);
		mBinding.txtTemps.setVisibility(View.VISIBLE);
		mBinding.txtInfo.setVisibility(View.VISIBLE);

		mBinding.layDades.setBackgroundColor(getResources().getColor(R.color.vermellKO));

		mBinding.layImatges.setVisibility(View.VISIBLE);

		mBinding.txtEstatEstacionament.setText(getResources().getString(R.string.estacionament_incorrecte));


		mBinding.llBtnDenuncies.setVisibility(View.VISIBLE);
		//Recomana Denunciar
		mBinding.buttonNoDenunciar.setBackground(getResources().getDrawable(R.drawable.button_white_selector));
		mBinding.buttonDenunciar.setBackground(getResources().getDrawable(R.drawable.button_selector));

		mBinding.buttonDenunciar.setTextColor(getResources().getColor(R.color.text_recomenat));
		mBinding.buttonNoDenunciar.setTextColor(getResources().getColor(R.color.text_no_recomenat));



		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.tvZona.setEnabled(false);
		mBinding.tvCarrer.setEnabled(false);
		mBinding.tvNum.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);

		mBinding.buttonComprovar.setVisibility(View.GONE);
	}

	private void changeViewNoComprovat() {
		//Amaguem el nom de l'ajuntament per qüestió estètica.



		mBinding.txtEstatEstacionament.setVisibility(View.GONE);
		mBinding.txtTemps.setVisibility(View.GONE);
		mBinding.txtInfo.setVisibility(View.VISIBLE);

		mBinding.layDades.setBackgroundColor(getResources().getColor(R.color.ja_denunciat));


		mBinding.txtEstatEstacionament.setText("");
		mBinding.txtEstatEstacionament.setVisibility(INVISIBLE);
		mBinding.txtInfo.setText(R.string.estacionament_sense_internet);
		mBinding.layImatges.setVisibility(View.GONE);

		if(mBinding.viewSwitcherComprovaAnim.getCurrentView() != mBinding.buttonComprovar) {
			mBinding.viewSwitcherComprovaAnim.showNext();
		}


		mBinding.llBtnDenuncies.setVisibility(View.VISIBLE);

		//Recomana NO Denunciar
		mBinding.buttonDenunciar.setBackground(getResources().getDrawable(R.drawable.button_white_selector));
		mBinding.buttonNoDenunciar.setBackground(getResources().getDrawable(R.drawable.button_selector));

		mBinding.buttonDenunciar.setTextColor(getResources().getColor(R.color.text_no_recomenat));
		mBinding.buttonNoDenunciar.setTextColor(getResources().getColor(R.color.text_recomenat));


		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.tvZona.setEnabled(false);
		mBinding.tvCarrer.setEnabled(false);
		mBinding.tvNum.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);

		mBinding.buttonComprovar.setVisibility(View.GONE);
	}

	private void changeViewComprovarMatricula() {
		//Mostrem el nom de l'ajuntament per qüestió estètica.
		mBinding.txtEstatEstacionament.setVisibility(View.INVISIBLE);
		mBinding.txtTemps.setVisibility(View.INVISIBLE);
		mBinding.txtInfo.setVisibility(View.INVISIBLE);

		mBinding.layDades.setBackgroundColor(getResources().getColor(R.color.barra_estat));


		mBinding.layImatges.setVisibility(View.GONE);
		mBinding.editTextMatricula.setText("");

		mBinding.llBtnDenuncies.setVisibility(View.INVISIBLE);

		//Recomana Denunciar
		mBinding.buttonNoDenunciar.setBackground(getResources().getDrawable(R.drawable.button_white_selector));
		mBinding.buttonDenunciar.setBackground(getResources().getDrawable(R.drawable.button_selector));


		if(mBinding.viewBtnComprobar.getCurrentView() == mBinding.buttonAcceptar) {
			mBinding.viewBtnComprobar.showNext();
		}
		if(mBinding.viewSwitcherComprovaAnim.getCurrentView() != mBinding.buttonComprovar) {
			mBinding.viewSwitcherComprovaAnim.showNext();
		}

		mBinding.textViewMatricula.setEnabled(true);
		mBinding.editTextMatricula.setEnabled(true);
		mBinding.tvZona.setEnabled(true);
		mBinding.tvCarrer.setEnabled(true);
		mBinding.tvNum.setEnabled(true);
		mBinding.buttonComprovar.setEnabled(true);
		mBinding.buttonComprovar.setVisibility(View.VISIBLE);

	}






	private void pinta(String path, ImageView imgView) {
		if(!isEmpty(path)) {
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

	@SuppressLint("RestrictedApi")
	private void checkBotoCamera() {
		if(!isEmpty(foto1) & !isEmpty(foto2) & !isEmpty(foto3) & !isEmpty(foto4)){
			mBinding.btnCamera.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.btn_desectivat));
		}
		else{
			mBinding.btnCamera.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.btn_activat));
		}

	}



	private String Check_Temps(Long dataCaducitat_milisegons_long){
		String dateString;

		SimpleDateFormat formatTempsH=new SimpleDateFormat("HH");
		SimpleDateFormat formatTempsM=new SimpleDateFormat("mm");
		SimpleDateFormat formatTempsS=new SimpleDateFormat("ss");


		formatTempsH.setTimeZone(TimeZone.getTimeZone("GMT"));
		formatTempsM.setTimeZone(TimeZone.getTimeZone("GMT"));
		formatTempsS.setTimeZone(TimeZone.getTimeZone("GMT"));
		/* TEMPS POSITIU**/

		if (dataCaducitat_milisegons_long>System.currentTimeMillis()){

			long TempsResultant=  dataCaducitat_milisegons_long-System.currentTimeMillis();

			/* + DE 2 DIES**/
			if(TempsResultant>=172800000){


				dateString = (((((TempsResultant)/1000)/60)/60)/24)+" "+getResources().getString(R.string.Dies);




			}/* 1 a 2 DIES**/
			else if(TempsResultant>=86400000){

				dateString =
						(((((TempsResultant)/1000)/60)/60)/24)+"D : "+
								formatTempsH.format(TempsResultant)+"H : "+
								formatTempsM.format(TempsResultant)+"M : "+
								formatTempsS.format(TempsResultant)+"S";






			}/* NORMA GENERAL**/
			else{


				dateString =
						formatTempsH.format(TempsResultant)+"H : "+
								formatTempsM.format(TempsResultant)+"M : "+
								formatTempsS.format(TempsResultant)+"S";


			}
			/* GENERAL */


			mBinding.txtInfo.setText(getResources().getString(R.string.temps_Restant));

		}
		/* TEMPS NEGATIU**/
		else{
			/* GENERAL */
			long TempsResultant=  System.currentTimeMillis()-dataCaducitat_milisegons;

			dateString =
					formatTempsH.format(TempsResultant)+"H : "+
							formatTempsM.format(TempsResultant)+"M : "+
							formatTempsS.format(TempsResultant)+"S";




			mBinding.txtInfo.setText(getResources().getString(R.string.temps_Excedit));

		}




		return dateString;
	}



	private void confirmPicture(final ImageView iv, String path, final Runnable onDelete) {
		try {





			Glide.with(mContext)
					.load(path)
					.asBitmap()
					.into(mBinding.preview);
			mBinding.repetir.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					anima(mBinding.getRoot());
					iv.setImageDrawable(null);
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


	private boolean Check_Null_Data(Date data, Context mContext){

		if(data==null) {
			androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
			alertDialogBuilder.setTitle(mContext.getString(R.string.atencio))
					.setMessage(mContext.getString(R.string.reiniciar_terminal))
					.setCancelable(false);

			androidx.appcompat.app.AlertDialog alert = alertDialogBuilder.create();
			alert.show();

			return false;
		}
		else {
			return true;
		}


	}





}
