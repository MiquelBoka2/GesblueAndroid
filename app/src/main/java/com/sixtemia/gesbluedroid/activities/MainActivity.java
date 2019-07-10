package com.sixtemia.gesbluedroid.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.Sancio;
import com.sixtemia.gesbluedroid.activities.passosformulari.Pas0ZonaActivity;
import com.sixtemia.gesbluedroid.activities.passosformulari.Pas6CarrerActivity;
import com.sixtemia.gesbluedroid.customstuff.GesblueFragmentActivity;
import com.sixtemia.gesbluedroid.customstuff.dialogs.DeviceListActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pt.joaocruz04.lib.misc.JSoapCallback;
import pt.joaocruz04.lib.misc.JsoapError;

import static com.sixtemia.gesbluedroid.activities.FormulariActivity.KEY_RETURN_PATH;
import static com.sixtemia.gesbluedroid.activities.FormulariActivity.RESULT_FOTO_1;
import static pt.joaocruz04.lib.misc.JsoapError.PARSE_ERROR;

public class MainActivity extends GesblueFragmentActivity {

	private ActivityMainBinding mBinding;
	private Menu menu;
	private String foto1;


	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.

		savedInstanceState.putString("foto1", foto1);

		// etc.

		super.onSaveInstanceState(savedInstanceState);
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		setupVisibleToolbar(mBinding.toolbar);

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

		mBinding.editTextMatricula.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
		mBinding.textViewLocalitzacioConcessio.setText(concessio);

		mBinding.tvZona.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(mContext, Pas0ZonaActivity.class);
				intent.putExtra("formPrimerCop", false);
				startActivityForResult(intent,1);
			}
		});
		mBinding.tvCarrer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(mContext, Pas6CarrerActivity.class);
				intent.putExtra("formPrimerCop", false);
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
				/*Intent intent = new Intent(mContext, CameraActivity.class);
				intent.putExtra("position", "1");
				startActivityForResult(intent, RESULT_FOTO_1);*/


				Sancio sancio = new Sancio();
				sancio.setMatricula(mBinding.editTextMatricula.getText().toString());

				Intent intent = new Intent(mContext, FormulariActivity.class);
				intent.putExtra(FormulariActivity.INTENT_SANCIO, sancio);
				intent.putExtra(FormulariActivity.KEY_VINC_DE_MATRICULA, true);
				startActivity(intent);
			}
		});

		mBinding.buttonNoDenunciar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				changeViewComprovarMatricula();
			}
		});


/*		final ArrayList<Model_LlistaBlanca> listLogs = DatabaseAPI.getLlistaBlanca(mContext);

		Log.d("Num llistaBlanca local",""+listLogs.size());

		final ArrayList<Model_LlistaAbonats> listLogs2 = DatabaseAPI.getLlistaAbonats(mContext);

		Log.d("Num llistaAbonats local",""+listLogs2.size());*/


	}

	@Override
	protected void onResume(){
		super.onResume();
		mBinding.tvZona.setText(PreferencesGesblue.getNomZona(mContext));
		mBinding.tvCarrer.setText(PreferencesGesblue.getNomCarrer(mContext));
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 1:
				mBinding.tvZona.setText(PreferencesGesblue.getNomZona(mContext));
				break;

			case 2:
				mBinding.tvCarrer.setText(PreferencesGesblue.getNomCarrer(mContext));
				break;

/*			case RESULT_FOTO_1:

				//foto1 = data.getExtras().getString(KEY_RETURN_PATH);

				PreferencesGesblue.setFoto1(mContext, data.getExtras().getString(KEY_RETURN_PATH));

				Sancio sancio = new Sancio();
				sancio.setMatricula(mBinding.editTextMatricula.getText().toString());

				Intent intent = new Intent(mContext, FormulariActivity.class);
				intent.putExtra(FormulariActivity.INTENT_SANCIO, sancio);
				intent.putExtra(FormulariActivity.KEY_VINC_DE_MATRICULA, true);
				startActivity(intent);



				break;*/

		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		this.menu = menu;

		String menuTitle = "";
		try {
			PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
			menuTitle = pInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		menu.findItem(R.id.versionNumber).setTitle(menuTitle);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.btnLogout:
				Utils.showCustomDialog(mContext, R.string.atencio, R.string.deslog, R.string.dacord, R.string.enrere, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						PreferencesGesblue.setUserName(mContext,"");
						PreferencesGesblue.setPassword(mContext,"");
						PreferencesGesblue.setConcessioString(mContext,"");
						finish();
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}, false);
				return true;

			case R.id.buttonRecuperarDenuncia:
				Intent intent = new Intent(mContext, RecuperarDenunciaActivity.class);
				startActivity(intent);

			default:
				return true;

		}
	}
	private void comprovarMatricula(String matricula) {
		final String mat = matricula;
		amagarTeclat();
		mBinding.viewSwitcherComprovaAnim.showNext();
		mBinding.editTextMatricula.setEnabled(false);

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

				mBinding.viewSwitcherComprovaAnim.showNext();

				Long temps = response.getTemps();


				Log.d("EstatComprovacio",""+ PreferencesGesblue.getEstatComprovacio(mContext));

				int estatComprovacio = 0;


				switch(response.getResultat()) {
					case 0: //Matricula correcta(no denunciar)
						if((temps>0)) {

						    estatComprovacio = 1;

							changeViewNoMultable(getResources().getString(R.string.estacionament_correcte) + System.getProperty("line.separator") + Math.round(temps/60) + " " + getResources().getString(R.string.estacionament_correcte2));
						}else{
							changeViewNoMultable(getResources().getString(R.string.estacionament_correcte));
							estatComprovacio = 2;
						}

						break;
					case -1: //Matricula no correcta(possibilitat de denunciar)
						Log.d("Temps:",""+response.getTemps());
						if((temps<0)&&(temps>-50400)){
							if(temps>-3600) {
								changeViewMultable(getResources().getString(R.string.estacionament_incorrecte) + System.getProperty("line.separator") + Math.round(temps / -60) + " " + getResources().getString(R.string.estacionament_incorrecte2));
                                estatComprovacio = 3;
							}
							else{
								changeViewMultable(getResources().getString(R.string.estacionament_incorrecte) + System.getProperty("line.separator") + Math.round(temps / -60 / 60) + " " + getResources().getString(R.string.estacionament_incorrecte3));
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
		mBinding.textViewLocalitzacioConcessio.setVisibility(View.INVISIBLE);

		mBinding.tvEstacionamentCorrecte.setText(text);
		mBinding.tvEstacionamentCorrecte.setVisibility(View.VISIBLE);

		mBinding.buttonDenunciar.setVisibility(View.VISIBLE);
		mBinding.buttonNoDenunciar.setVisibility(View.VISIBLE);

		mBinding.separator.setVisibility(View.VISIBLE);

		mBinding.viewSwitcherButton.showNext();
	}

	private void changeViewJaDenunciat() {
		//Amaguem el nom de l'ajuntament per qüestió estètica.
		mBinding.textViewLocalitzacioConcessio.setVisibility(View.INVISIBLE);

		mBinding.tvEstacionamentCorrecte.setText(R.string.vehicle_ja_denunciat);
		mBinding.tvEstacionamentCorrecte.setVisibility(View.VISIBLE);

		mBinding.buttonDenunciar.setVisibility(View.VISIBLE);
		mBinding.buttonNoDenunciar.setVisibility(View.VISIBLE);

		mBinding.separator.setVisibility(View.VISIBLE);

		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);
	}

	private void changeViewMultable(String text) {
		//Amaguem el nom de l'ajuntament per qüestió estètica.
		mBinding.textViewLocalitzacioConcessio.setVisibility(View.INVISIBLE);

		mBinding.textViewEstacionamentIncorrecte.setText(text);
		mBinding.textViewEstacionamentIncorrecte.setVisibility(View.VISIBLE);

		mBinding.buttonDenunciar.setVisibility(View.VISIBLE);
		mBinding.buttonNoDenunciar.setVisibility(View.VISIBLE);

		mBinding.separator.setVisibility(View.VISIBLE);

		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);
	}
	private void changeViewNoComprovat() {
		//Amaguem el nom de l'ajuntament per qüestió estètica.
		mBinding.textViewLocalitzacioConcessio.setVisibility(View.INVISIBLE);

		mBinding.textViewEstacionamentIncorrecte.setText(R.string.estacionament_sense_internet);

		mBinding.textViewEstacionamentIncorrecte.setVisibility(View.VISIBLE);

		mBinding.buttonDenunciar.setVisibility(View.VISIBLE);
		mBinding.buttonNoDenunciar.setVisibility(View.VISIBLE);

		mBinding.separator.setVisibility(View.VISIBLE);

		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);
	}

	private void changeViewComprovarMatricula() {
		//Mostrem el nom de l'ajuntament per qüestió estètica.
		mBinding.textViewLocalitzacioConcessio.setVisibility(View.VISIBLE);

		mBinding.editTextMatricula.setText("");

		mBinding.textViewEstacionamentIncorrecte.setVisibility(View.INVISIBLE);
		mBinding.tvEstacionamentCorrecte.setVisibility(View.INVISIBLE);

		mBinding.buttonDenunciar.setVisibility(View.INVISIBLE);
		mBinding.buttonNoDenunciar.setVisibility(View.INVISIBLE);

		mBinding.separator.setVisibility(View.INVISIBLE);

		if(mBinding.viewSwitcherButton.getCurrentView() == mBinding.buttonAcceptar) {
			mBinding.viewSwitcherButton.showNext();
		}

		mBinding.textViewMatricula.setEnabled(true);
		mBinding.editTextMatricula.setEnabled(true);
		mBinding.buttonComprovar.setEnabled(true);
	}
}
