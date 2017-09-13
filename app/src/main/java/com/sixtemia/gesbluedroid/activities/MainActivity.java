package com.sixtemia.gesbluedroid.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.Sancio;
import com.sixtemia.gesbluedroid.customstuff.GesblueFragmentActivity;
import com.sixtemia.gesbluedroid.databinding.ActivityMainBinding;
import com.sixtemia.gesbluedroid.datamanager.webservices.DatamanagerAPI;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.ComprovaMatriculaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.ComprovaMatriculaResponse;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;

import pt.joaocruz04.lib.misc.JSoapCallback;
import pt.joaocruz04.lib.misc.JsoapError;

import static pt.joaocruz04.lib.misc.JsoapError.PARSE_ERROR;

public class MainActivity extends GesblueFragmentActivity {

	private ActivityMainBinding mBinding;

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

		mBinding.textViewLocalitzacioConcessio.setText(PreferencesGesblue.getConcessioString(mContext));

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
	}

	private void comprovarMatricula(String matricula) {
		amagarTeclat();
		mBinding.viewSwitcherComprovaAnim.showNext();
		mBinding.editTextMatricula.setEnabled(false);

		final boolean multable = true;

		DatamanagerAPI.crida_ComprovaMatricula(new ComprovaMatriculaRequest(PreferencesGesblue.getConcessio(mContext), Utils.getDeviceId(mContext), matricula, Utils.getCurrentTimeLong(mContext)), new JSoapCallback() {
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

				Utils.showDatamanagerError(mContext, error);

				changeViewMultable();
			}
		});
	}

	private void changeViewNoMultable() {
		mBinding.tvEstacionamentCorrecte.setVisibility(View.VISIBLE);

		mBinding.separator.setVisibility(View.VISIBLE);

		mBinding.viewSwitcherButton.showNext();
	}

	private void changeViewMultable() {
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
