package com.sixtemia.gesbluedroid.activities;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.Sancio;
import com.sixtemia.gesbluedroid.customstuff.GBData;
import com.sixtemia.gesbluedroid.customstuff.GesblueFragmentActivity;
import com.sixtemia.gesbluedroid.databinding.ActivityMoreInfoToPrintBinding;
import com.sixtemia.gesbluedroid.global.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MoreInfoToPrintActivity extends GesblueFragmentActivity implements View.OnClickListener {

	public static final String KEY_TIQUET = "numero";
	public static final String KEY_DATAIMPORT = "dataImport";

	Sancio mSancio;
	private boolean tiquet;
	private boolean dataImport;
	private String strTiquet;
	private String strData;
	private String strImport;
	private GBData dataAEscollir;

	ActivityMoreInfoToPrintBinding mBinding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_more_info_to_print);
		setupVisibleToolbar(mBinding.toolbar);

		dataAEscollir = new GBData(mContext);

		if(getIntent().getExtras() != null) {
			mSancio = getIntent().getExtras().getParcelable(FormulariActivity.INTENT_SANCIO);
			tiquet = getIntent().getExtras().getBoolean(KEY_TIQUET,false);
			dataImport = getIntent().getExtras().getBoolean(KEY_DATAIMPORT,false);
		}

		mBinding.etNumeroTiquet.setVisibility(tiquet ? VISIBLE : GONE);
		mBinding.etData.setVisibility(dataImport ? VISIBLE : GONE);
		mBinding.etImport.setVisibility(dataImport ? VISIBLE : GONE);

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnAcceptar:
				if(!TextUtils.isEmpty(mBinding.etNumeroTiquet.getText().toString())) {
					strTiquet = mBinding.etNumeroTiquet.getText().toString();
				} else if(tiquet) {
					Utils.showCustomDialog(mContext, R.string.atencio, R.string.campsObligatoris);
				}
				if(!TextUtils.isEmpty(mBinding.etData.getText().toString())) {
					strData = convertData(mBinding.etData.getText().toString());
				} else if(dataImport) {
					Utils.showCustomDialog(mContext, R.string.atencio, R.string.campsObligatoris);
				}
				if(!TextUtils.isEmpty(mBinding.etImport.getText().toString())) {
					strImport = mBinding.etImport.getText().toString();

					mSancio.setImportInfraccio(strImport);
				} else if(dataImport) {
					Utils.showCustomDialog(mContext, R.string.atencio, R.string.campsObligatoris);
				}

				sendAll();

				break;
			case R.id.etData:
				showDatePickerDialog();
				break;
		}
	}

	public void showDatePickerDialog() {
		DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				dataAEscollir.setData(dayOfMonth, month, year);
				if(dataAEscollir.getData().after(new GBData().getData())) {
					dataAEscollir = null;
				} else {
					mBinding.etData.setText(dataAEscollir.getStringData("dd/MMMM/yyyy"));
				}
			}
		};
		dataAEscollir = new GBData(mContext);
		DatePickerDialog dpd = new DatePickerDialog(mContext, R.style.AppTheme_Dialog, listener, dataAEscollir.getAny(), dataAEscollir.getMes(), dataAEscollir.getDia());
		Calendar calendar = Calendar.getInstance();
		dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
		dpd.show();
	}

	private String convertData(String data) {
		//-- INPUT = dd/MM/yyyy
		//-- OUTPUT = yyyyMMdd

		String newDateString = "";

		try {
			final String OLD_FORMAT = "dd/MM/yyyy";
			final String NEW_FORMAT = "yyyyMMdd";

			SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
			Date d = null;

			d = sdf.parse(data);
			sdf.applyPattern(NEW_FORMAT);
			newDateString = sdf.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}


		return newDateString;
	}

	private void sendAll() {
		getIntent().putExtra(FormulariActivity.INTENT_SANCIO, mSancio);
		getIntent().putExtra(FormulariActivity.KEY_DATA_INFRACCIO, strData);
		getIntent().putExtra(FormulariActivity.KEY_NUMERO_TIQUET, strTiquet);
		setResult(RESULT_OK, getIntent());
		finish();
	}
}
