package com.sixtemia.gesbluedroid.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.sixtemia.gesbluedroid.databinding.ActivityMainBinding;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Agent;
import com.sixtemia.gesbluedroid.datamanager.webservices.DatamanagerAPI;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.ComprovaMatriculaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.ComprovaMatriculaResponse;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;

import java.util.ArrayList;

import pt.joaocruz04.lib.misc.JSoapCallback;
import pt.joaocruz04.lib.misc.JsoapError;

import static pt.joaocruz04.lib.misc.JsoapError.PARSE_ERROR;

public class MainActivity extends GesblueFragmentActivity {

	private ActivityMainBinding mBinding;
	private Menu menu;

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

				if(matricula.equals("")) {
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


		final ArrayList<Model_Agent> listLogs = DatabaseAPI.getAgents(mContext);

		Log.d("Num agent locals",""+listLogs.size());

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
		if (resultCode == RESULT_OK) {
			if(requestCode == 1){

				mBinding.tvZona.setText(PreferencesGesblue.getNomZona(mContext));
			}
			if(requestCode == 2) {

				mBinding.tvCarrer.setText(PreferencesGesblue.getNomCarrer(mContext));

			}

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
			case R.id.buttonRecuperarDenuncia:
				Intent intent = new Intent(mContext, RecuperarDenunciaActivity.class);
				startActivity(intent);

			default:
				return true;

		}
	}
	private void comprovarMatricula(String matricula) {
		amagarTeclat();
		mBinding.viewSwitcherComprovaAnim.showNext();
		mBinding.editTextMatricula.setEnabled(false);

		final boolean multable = true;

		DatamanagerAPI.crida_ComprovaMatricula(new ComprovaMatriculaRequest(PreferencesGesblue.getConcessio(mContext), Utils.getDeviceId(mContext), matricula, Utils.getCurrentTimeLong(mContext),PreferencesGesblue.getCodiCarrer(mContext),0), new JSoapCallback() {
			@Override
			public void onSuccess(String result) {
				final ComprovaMatriculaResponse response;
				try {
					response = DatamanagerAPI.parseJson(result, ComprovaMatriculaResponse.class);
				} catch (Exception ex) {
					ELog(ex);
					onError(PARSE_ERROR);
					return;
				}

				mBinding.viewSwitcherComprovaAnim.showNext();

				switch(response.getResultat()) {
					case 0: //Matricula correcta(no denunciar)
						changeViewNoMultable();
						break;
					case -1: //Matricula no correcta(possibilitat de denunciar)
						changeViewMultable();
						break;
					case -3: //Vehicle ja denunciat
						changeViewJaDenunciat();
						break;
					case -2: //Error de comprovació
					default:
						Utils.showDatamanagerError(mContext, JsoapError.OTHER_ERROR);
						mBinding.editTextMatricula.setEnabled(true);

						break;
				}

			}

			@Override
			public void onError(final int error) {

				mBinding.viewSwitcherComprovaAnim.showNext();
				mBinding.editTextMatricula.setEnabled(true);

				//Utils.showDatamanagerError(mContext, error);

				changeViewNoComprovat();
			}
		});
	}

	private void changeViewNoMultable() {
		mBinding.tvEstacionamentCorrecte.setText(R.string.estacionament_correcte);
		mBinding.tvEstacionamentCorrecte.setVisibility(View.VISIBLE);

		mBinding.separator.setVisibility(View.VISIBLE);

		mBinding.viewSwitcherButton.showNext();
	}

	private void changeViewJaDenunciat() {
		mBinding.tvEstacionamentCorrecte.setText(R.string.vehicle_ja_denunciat);
		mBinding.tvEstacionamentCorrecte.setVisibility(View.VISIBLE);

		mBinding.buttonDenunciar.setVisibility(View.VISIBLE);
		mBinding.buttonNoDenunciar.setVisibility(View.VISIBLE);

		mBinding.separator.setVisibility(View.VISIBLE);

		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);
	}

	private void changeViewMultable() {

		mBinding.textViewEstacionamentIncorrecte.setText(R.string.estacionament_incorrecte);
		mBinding.textViewEstacionamentIncorrecte.setVisibility(View.VISIBLE);

		mBinding.buttonDenunciar.setVisibility(View.VISIBLE);
		mBinding.buttonNoDenunciar.setVisibility(View.VISIBLE);

		mBinding.separator.setVisibility(View.VISIBLE);

		mBinding.textViewMatricula.setEnabled(false);
		mBinding.editTextMatricula.setEnabled(false);
		mBinding.buttonComprovar.setEnabled(false);
	}
	private void changeViewNoComprovat() {
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
