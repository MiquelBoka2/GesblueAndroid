package com.sixtemia.gesbluedroid.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.Sancio;
import com.sixtemia.gesbluedroid.activities.passosformulari.Pas0ZonaActivity;
import com.sixtemia.gesbluedroid.activities.passosformulari.Pas6CarrerActivity;
import com.sixtemia.gesbluedroid.customstuff.GesblueFragmentActivity;
import com.sixtemia.gesbluedroid.databinding.ActivityMainBinding;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_LlistaAbonats;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_LlistaBlanca;
import com.sixtemia.gesbluedroid.datamanager.webservices.DatamanagerAPI;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.ComprovaMatriculaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.ComprovaMatriculaResponse;
import com.sixtemia.gesbluedroid.global.Constants;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;
import com.sixtemia.sbaseobjects.tools.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import pt.joaocruz04.lib.misc.JSoapCallback;
import pt.joaocruz04.lib.misc.JsoapError;

import static android.text.TextUtils.isEmpty;
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


	private String foto1;
	private String foto2;
	private String foto3;
	private String foto4;
	private boolean imgAIsActive = false;
	private boolean imgBIsActive = false;
	private boolean imgCIsActive = false;
	private boolean imgDIsActive = false;

	private Boolean adm=false;

	private Timer contador;

	private long DataCaducitat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		setupVisibleToolbar(mBinding.toolbar);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			adm = extras.getBoolean("adm");

		}

		checkAdmin(adm);
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
			Utils.showCustomDialog2(mContext, R.string.atencio, R.string.errorConcessio, R.string.dacord, new DialogInterface.OnClickListener() {
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

				Intent intent = new Intent(mContext, Pas6CarrerActivity.class);
				intent.putExtra("formPrimerCop", false);
				intent.putExtra("adm",adm);
				startActivityForResult(intent,2);
			}
		});


		//mBinding.tv.setOnClickListener(this);
		mBinding.buttonComprovar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String matricula = mBinding.editTextMatricula.getText().toString();

				if(matricula.equals("") || PreferencesGesblue.getCodiZona(mContext)==0 ||
						PreferencesGesblue.getCodiCarrer(mContext)==0) {
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
				Sancio sancio = new Sancio();
				sancio.setMatricula(mBinding.editTextMatricula.getText().toString());

				Intent intent = new Intent(mContext, FormulariActivity.class);
				PreferencesGesblue.setFoto1(mContext, foto1);
				PreferencesGesblue.setFoto2(mContext, foto2);
				PreferencesGesblue.setFoto3(mContext, foto3);
				PreferencesGesblue.setFoto4(mContext, foto4);
				intent.putExtra(FormulariActivity.INTENT_SANCIO, sancio);
				intent.putExtra(FormulariActivity.KEY_VINC_DE_MATRICULA, true);
                intent.putExtra("adm",adm);
				startActivity(intent);
			}
		});

		mBinding.buttonNoDenunciar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				changeViewComprovarMatricula();
			}
		});





		mBinding.btnCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				intent = new Intent(mContext, CameraActivity.class);

				if (isEmpty(foto1)) {
					intent.putExtra("position", "1");
					startActivityForResult(intent, FormulariActivity.RESULT_FOTO_1);
				} else if (isEmpty(foto2)) {
					intent.putExtra("position", "2");
					startActivityForResult(intent, FormulariActivity.RESULT_FOTO_2);
				} else if (isEmpty(foto3)) {
					intent.putExtra("position", "3");
					startActivityForResult(intent, FormulariActivity.RESULT_FOTO_3);
				} else if (isEmpty(foto4)) {
					intent.putExtra("position", "4");
					startActivityForResult(intent, FormulariActivity.RESULT_FOTO_4);
				} else {
					intent.putExtra("position", "1");
					startActivityForResult(intent, FormulariActivity.RESULT_FOTO_1);
				}
			}
		});


/*		final ArrayList<Model_LlistaBlanca> listLogs = DatabaseAPI.getLlistaBlanca(mContext);

		Log.d("Num llistaBlanca local",""+listLogs.size());

		final ArrayList<Model_LlistaAbonats> listLogs2 = DatabaseAPI.getLlistaAbonats(mContext);

		Log.d("Num llistaAbonats local",""+listLogs2.size());*/


	}


	/** Actualitza el UI en funcio de ADMIN**/
	private void checkAdmin(Boolean adm) {

		if (adm){
			mBinding.toolbar.imgUnlock.setVisibility(View.VISIBLE);
			mBinding.toolbar.txtLocalitzacioEstat.setBackgroundColor(getResources().getColor(R.color.admin));
		}
		else{
			mBinding.toolbar.imgUnlock.setVisibility(View.GONE);
			mBinding.toolbar.txtLocalitzacioEstat.setBackgroundColor(getResources().getColor(R.color.barra_estat));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBinding.tvZona.setText(PreferencesGesblue.getNomZona(mContext));
		mBinding.tvCarrer.setText(PreferencesGesblue.getNomCarrer(mContext));
		checkAdmin(adm);



	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {


			switch (requestCode) {

				/**ESPEREM EL RESULTAT DE LES FOTOS**/
				case FormulariActivity.RESULT_FOTO_1:
					foto1 = data.getExtras().getString(FormulariActivity.KEY_RETURN_PATH);
					pinta(foto1, mBinding.imageViewA);
					imgAIsActive = true;
					checkBotoCamera();
					break;
				case FormulariActivity.RESULT_FOTO_2:
					foto2 = data.getExtras().getString(FormulariActivity.KEY_RETURN_PATH);
					pinta(foto2, mBinding.imageViewB);
					imgBIsActive = true;
					checkBotoCamera();
					break;
				case FormulariActivity.RESULT_FOTO_3:
					foto3 = data.getExtras().getString(FormulariActivity.KEY_RETURN_PATH);
					pinta(foto3, mBinding.imageViewC);
					imgCIsActive = true;
					checkBotoCamera();
					break;

				case FormulariActivity.RESULT_FOTO_4:
					foto4 = data.getExtras().getString(FormulariActivity.KEY_RETURN_PATH);
					pinta(foto4, mBinding.imageViewD);
					imgCIsActive = true;
					checkBotoCamera();
					break;




				/**ESPEREM EL RESULTAT DE LA ZONA i EL CARRER**/
				case 1: {

					mBinding.tvZona.setText(PreferencesGesblue.getNomZona(mContext));
				}
				case 2: {

					mBinding.tvCarrer.setText(PreferencesGesblue.getNomCarrer(mContext));

				}

			}
			/** CHECK ADMIN**/
			Boolean result = data.getExtras().getBoolean("adm");
			if (result != null) {
				if (result) {
					adm = result;
				}
			}
			checkAdmin(adm);

		}
	}

	private void comprovarMatricula(String matricula) {
		final String mat = matricula;
		amagarTeclat();


		final boolean multable = true;
		Log.d("crida0", Constants.OPERATIVA_COMPROVAMATRICULA_METHOD);
		Log.d("params",PreferencesGesblue.getConcessio(mContext)+" - "+Utils.getDeviceId(mContext)+" - "+matricula+" - "+ Utils.getCurrentTimeLong(mContext)+" - "+PreferencesGesblue.getCodiCarrer(mContext)+" - "+PreferencesGesblue.getCodiZona(mContext)+" - "+PreferencesGesblue.getCodiAgent(mContext));

		DatamanagerAPI.crida_ComprovaMatricula(new ComprovaMatriculaRequest(PreferencesGesblue.getConcessio(mContext), Utils.getDeviceId(mContext), matricula, Utils.getCurrentTimeLong(mContext),PreferencesGesblue.getCodiCarrer(mContext),PreferencesGesblue.getCodiZona(mContext)), new JSoapCallback() {
			@Override
			public void onSuccess(String result) {
				final ComprovaMatriculaResponse response;
				try {
					response = DatamanagerAPI.parseJson(result, ComprovaMatriculaResponse.class);
					Log.d("Resultat",""+response.getResultat());
				} catch (Exception ex) {
					ELog(ex);
					onError(PARSE_ERROR);
					return;
				}

				Long data =Utils.getCurrentTimeLong(mContext);

				Long temps = response.getTemps();
				DataCaducitat=data;


				//Dies
				if(temps/60/60/24>0){
					DataCaducitat=DataCaducitat+Math.round((temps/60/60/24))*1000000;
				}

				//Hores
				if(temps/60/60>0){
					DataCaducitat=DataCaducitat+Math.round((temps/60/60))*10000;
				}

				//Minuts
				if(temps/60>0){
					DataCaducitat=DataCaducitat+Math.round((temps/60))*100;
				}
				//Segons
				else{
					DataCaducitat=DataCaducitat+Math.round((temps));
				}








				Log.d("EstatComprovacio",""+ PreferencesGesblue.getEstatComprovacio(mContext));

				int estatComprovacio = 0;


				switch(response.getResultat()) {
					case 0: //Matricula correcta(no denunciar)
						if((temps>0)) {

						    estatComprovacio = 1;

							mBinding.txtTemps.setText(System.getProperty("line.separator") + Math.round(temps/60));
							CridarThreadContador();
							mBinding.txtInfo.setText(getResources().getString(R.string.estacionament_correcte2));
							changeViewNoMultable(getResources().getString(R.string.estacionament_correcte));
						}else{
							mBinding.txtInfo.setText(getResources().getString(R.string.estacionament_correcte));
							estatComprovacio = 2;
						}

						break;
					case -1: //Matricula no correcta(possibilitat de denunciar)
						Log.d("Temps:",""+response.getTemps());
						if((temps<0)&&(temps>-50400)){
							if(temps>-3600) {

								mBinding.txtTemps.setText(System.getProperty("line.separator") + Math.round(temps / -60));
								mBinding.txtInfo.setText(getResources().getString(R.string.estacionament_incorrecte2));


                                estatComprovacio = 3;
							}
							else{
								mBinding.txtTemps.setText(System.getProperty("line.separator") + Math.round(temps / -60 / 60));
								mBinding.txtInfo.setText(getResources().getString(R.string.estacionament_incorrecte3));


                                estatComprovacio = 4;
							}
						}else{
							changeViewMultable(getResources().getString(R.string.estacionament_incorrecte));
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
                        estatComprovacio = 7;

						break;
				}

                PreferencesGesblue.saveEstatComprovacio(mContext,estatComprovacio);

			}

			@Override
			public void onError(final int error) {
                int estatComprovacio = 0;

				mBinding.viewSwitcherComprovaAnim.showNext();
				mBinding.editTextMatricula.setEnabled(true);

				//Utils.showDatamanagerError(mContext, error);

				Model_LlistaBlanca llistaBlanca = DatabaseAPI.findLlistaBlanca(mContext, mat);

				if(llistaBlanca==null) {


                    Model_LlistaAbonats llistaAbonats = DatabaseAPI.findLlistaAbonats(mContext, mat);

                    if(llistaAbonats==null) {

                        estatComprovacio = 8;
                        changeViewNoComprovat();

                    }
                    else{
						try {
							SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							Date datainici = fmt.parse(llistaAbonats.getDatainici());
							Date datafi = fmt.parse(llistaAbonats.getDatafi());
							Date date = new Date();
							if( date.after(datainici) && date.before(datafi)){
                                estatComprovacio = 9;
								changeViewNoMultable(getResources().getString(R.string.estacionament_correcte));
							}
							else{
                                estatComprovacio = 8;
								changeViewNoComprovat();
							}
						}catch(ParseException pex){

						}

                        Log.d("comprovacio","abonat");
                        estatComprovacio = 9;
                        changeViewNoMultable(getResources().getString(R.string.estacionament_correcte));

                    }
				}
				else{
                    Log.d("comprovacio","blanca");
                    estatComprovacio = 10;
					changeViewNoMultable(getResources().getString(R.string.estacionament_correcte));

				}

                PreferencesGesblue.saveEstatComprovacio(mContext,estatComprovacio);
			}
		});
	}

	private void changeViewNoMultable(String text) {
		//Amaguem el nom de l'ajuntament per qüestió estètica.

		mBinding.layDades.setBackgroundColor(getResources().getColor(R.color.verdOK));
		mBinding.txtEstatEstacionament.setText(text);
		mBinding.txtEstatEstacionament.setVisibility(View.VISIBLE);

		mBinding.llBtnDenuncies.setVisibility(View.VISIBLE);

		mBinding.viewBtnComprobar.setVisibility(View.GONE);

	}

	private void changeViewJaDenunciat() {
		//Amaguem el nom de l'ajuntament per qüestió estètica.

		mBinding.layDades.setBackgroundColor(getResources().getColor(R.color.admin));

		mBinding.txtInfo.setText(R.string.vehicle_ja_denunciat);
		mBinding.txtInfo.setVisibility(View.VISIBLE);


		mBinding.llBtnDenuncies.setVisibility(View.VISIBLE);

		//Recomana NO Denunciar
		mBinding.buttonDenunciar.setBackground(getResources().getDrawable(R.drawable.button_white_selector));
		mBinding.buttonNoDenunciar.setBackground(getResources().getDrawable(R.drawable.button_selector));

		mBinding.buttonDenunciar.setTextColor(getResources().getColor(R.color.text_no_recomenat));
		mBinding.buttonNoDenunciar.setTextColor(getResources().getColor(R.color.text_recomenat));



		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);
		mBinding.buttonComprovar.setVisibility(View.GONE);
	}

	private void changeViewMultable(String text) {
		//Amaguem el nom de l'ajuntament per qüestió estètica.

		mBinding.layDades.setBackgroundColor(getResources().getColor(R.color.vermellKO));

		mBinding.layImatges.setVisibility(View.VISIBLE);
		mBinding.txtEstatEstacionament.setText(text);
		mBinding.txtEstatEstacionament.setVisibility(View.VISIBLE);



		mBinding.llBtnDenuncies.setVisibility(View.VISIBLE);
		//Recomana Denunciar
		mBinding.buttonNoDenunciar.setBackground(getResources().getDrawable(R.drawable.button_white_selector));
		mBinding.buttonDenunciar.setBackground(getResources().getDrawable(R.drawable.button_selector));

		mBinding.buttonDenunciar.setTextColor(getResources().getColor(R.color.text_recomenat));
		mBinding.buttonNoDenunciar.setTextColor(getResources().getColor(R.color.text_no_recomenat));



		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);

		mBinding.buttonComprovar.setVisibility(View.GONE);
	}
	private void changeViewNoComprovat() {
		//Amaguem el nom de l'ajuntament per qüestió estètica.


		mBinding.txtEstatEstacionament.setText(R.string.estacionament_sense_internet);
		mBinding.layImatges.setVisibility(View.GONE);
		mBinding.txtEstatEstacionament.setVisibility(View.VISIBLE);


		mBinding.llBtnDenuncies.setVisibility(View.VISIBLE);

		//Recomana NO Denunciar
		mBinding.buttonDenunciar.setBackground(getResources().getDrawable(R.drawable.button_white_selector));
		mBinding.buttonNoDenunciar.setBackground(getResources().getDrawable(R.drawable.button_selector));

		mBinding.buttonDenunciar.setTextColor(getResources().getColor(R.color.text_no_recomenat));
		mBinding.buttonNoDenunciar.setTextColor(getResources().getColor(R.color.text_recomenat));


		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);

		mBinding.buttonComprovar.setVisibility(View.GONE);
	}

	private void changeViewComprovarMatricula() {
		//Mostrem el nom de l'ajuntament per qüestió estètica.
		mBinding.layDades.setBackgroundColor(getResources().getColor(R.color.barra_estat));

		mBinding.txtEstatEstacionament.setVisibility(View.INVISIBLE);
		mBinding.txtTemps.setVisibility(View.INVISIBLE);
		mBinding.txtInfo.setVisibility(View.INVISIBLE);
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
			mBinding.btnCamera.setVisibility(View.GONE);
		}
		else{
			mBinding.btnCamera.setVisibility(VISIBLE);
		}

	}


	private void CridarThreadContador(){


		contador = new Timer();
		contador.schedule(new TimerTask() {
			@Override
			public void run() {
				TimerMethod();
			}

		}, 0, 500);
	}

	private void TimerMethod()
	{

		this.runOnUiThread(Timer_Tick);
	}


	private Runnable Timer_Tick = new Runnable() {
		public void run() {

			long TempsResultant= DataCaducitat -Utils.getCurrentTimeLong(mContext);
			mBinding.txtTemps.setText(TempsResultant+"");
			//This method runs in the same thread as the UI.

			//Do something to the UI thread here

		}
	};

}
