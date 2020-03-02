package com.boka2.gesblue.activities.passosformulari;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.boka2.gesblue.R;
import com.boka2.gesblue.Sancio;
import com.boka2.gesblue.activities.FormulariActivity;
import com.boka2.gesblue.customstuff.GesblueFragmentActivity;
import com.boka2.gesblue.databinding.ActivityPas7NumeroBinding;
import com.boka2.gesblue.global.PreferencesGesblue;

public class Pas7NumeroActivity extends GesblueFragmentActivity {

	ActivityPas7NumeroBinding mBinding;
	private Sancio mSancio;
	private String mSelected;
	private boolean primerCop;
	private Boolean adm=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pas7_numero);
		setupVisibleToolbar(mBinding.toolbar);

		mBinding.toolbar.txtLocalitzacioEstat.setVisibility(View.GONE);
		mBinding.toolbar.icOpciones.setVisibility(View.GONE);
		mBinding.toolbar.txtGesBlue.setVisibility(View.GONE);
		mBinding.toolbar.txtAny.setVisibility(View.GONE);

		mBinding.etNum.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mBinding.etNum, InputMethodManager.SHOW_IMPLICIT);

		if(getIntent().getExtras() != null) {
			adm=getIntent().getExtras().getBoolean("adm");

			mSancio = getIntent().getExtras().getParcelable(FormulariActivity.INTENT_SANCIO);
			primerCop = getIntent().getExtras().getBoolean(FormulariActivity.KEY_FORMULARI_PRIMER_COP, true);
		}

		if(mSancio != null && !TextUtils.isEmpty(mSancio.getNumero())) {
			mSelected = mSancio.getNumero();
			mBinding.etNum.setText(mSelected);
		}
		if(primerCop) {

			String value = PreferencesGesblue.getFormulariNumero(mContext);
			if(!TextUtils.isEmpty(value)) {
				mSelected = value;
				mBinding.etNum.setText(value);
			}

			mBinding.linearInferior.seguent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PreferencesGesblue.clearFormulari(mContext);
					Intent intent = new Intent(mContext, FormulariActivity.class);

					mSelected = (TextUtils.isEmpty(mBinding.etNum.getText().toString()) ? "S/N" : mBinding.etNum.getText().toString());
					mSancio.setNumero(mSelected);
					intent.putExtra(FormulariActivity.INTENT_SANCIO, mSancio);
					intent.putExtra("adm",adm);
					startActivity(intent);
				}
			});

			mBinding.linearInferior.anterior.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		} else {
			mBinding.linearConfirmar.layoutConfirmar.setVisibility(View.VISIBLE);
			mBinding.linearInferior.layoutButtons.setVisibility(View.GONE);
			mBinding.linearConfirmar.btnConfirmar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mSelected = (TextUtils.isEmpty(mBinding.etNum.getText().toString()) ? "" : mBinding.etNum.getText().toString());
					mSancio.setNumero(mSelected);
					getIntent().putExtra(FormulariActivity.KEY_FORMULARI_CONFIRMAR, mSancio);
					setResult(RESULT_OK, getIntent());
					finish();
				}
			});
		}
	}
}
