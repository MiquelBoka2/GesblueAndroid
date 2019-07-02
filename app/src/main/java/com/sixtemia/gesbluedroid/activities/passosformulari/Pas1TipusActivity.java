package com.sixtemia.gesbluedroid.activities.passosformulari;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.Sancio;
import com.sixtemia.gesbluedroid.activities.FormulariActivity;
import com.sixtemia.gesbluedroid.customstuff.GesblueFragmentActivity;
import com.sixtemia.gesbluedroid.databinding.ActivityPas1TipusBinding;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_TipusVehicle;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Pas1TipusActivity extends GesblueFragmentActivity {

	private ActivityPas1TipusBinding mBinding;
	private Sancio mSancio;
	private Model_TipusVehicle mSelected;
	private SimpleAdapter mAdapter;
	private boolean primerCop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pas1_tipus);
		setupVisibleToolbar(mBinding.toolbar);

		PreferencesGesblue.clearFormulari(mContext);

		if(getIntent().getExtras() != null) {
			mSancio = getIntent().getExtras().getParcelable(FormulariActivity.INTENT_SANCIO);
			primerCop = getIntent().getExtras().getBoolean(FormulariActivity.KEY_FORMULARI_PRIMER_COP, true);
		}

		final ArrayList<Model_TipusVehicle> arrayAux = DatabaseAPI.getTipusVehicles(mContext);

		if(arrayAux != null ) {
			Collections.sort(arrayAux, new Comparator<Model_TipusVehicle>() {
				@Override
				public int compare(Model_TipusVehicle o1, Model_TipusVehicle o2) {

					return Long.compare(o1.getCoditipusvehicle(), o2.getCoditipusvehicle());
				}
			});
		}

		if(mAdapter == null) {
			mAdapter = new SimpleAdapter(mContext, arrayAux);
		} else {
			// Posicionem l'scroll a la primera posició
			mBinding.lv.setSelectionAfterHeaderView();
		}
		mBinding.lv.setAdapter(mAdapter);

		if(mSancio != null && mSancio.getModelTipusVehicle() != null) {
			mSelected = mSancio.getModelTipusVehicle();
			int index = -1;
			for(int i = 0; i< arrayAux.size(); i++) {
				if(arrayAux.get(i).equals(mSelected)) {
					index = i;
				}
			}

			mAdapter.setSelectedItem(index);
			mAdapter.notifyDataSetChanged();
		}

		mBinding.lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Model_TipusVehicle newSelected = arrayAux.get(position);

				if(mSelected!= null && newSelected != mSelected) {
					mSelected = newSelected;
					mAdapter.setSelectedItem(position);
					mAdapter.notifyDataSetChanged();
					PreferencesGesblue.remove(mContext, "marca");
					PreferencesGesblue.remove(mContext, "model");
					PreferencesGesblue.remove(mContext, "color");
					PreferencesGesblue.remove(mContext, "infraccio");
					//PreferencesGesblue.remove(mContext, "carrer");
					PreferencesGesblue.remove(mContext, "numero");
				} else {
					mSelected = newSelected;
					mAdapter.setSelectedItem(position);
					mAdapter.notifyDataSetChanged();
				}
			}
		});

		if(primerCop) {
			mBinding.layoutButtons.setVisibility(View.VISIBLE);
			mBinding.seguent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, Pas2MarcaActivity.class);
					if(mSelected == null) {
						Utils.showDialogNoPotsPassar(mContext);
					} else {
						mSancio.setModelTipusVehicle(mSelected);
						PreferencesGesblue.setFormulariTipus(mContext, Long.toString(mSelected.getCoditipusvehicle()));
						intent.putExtra(FormulariActivity.INTENT_SANCIO, mSancio);
						startActivity(intent);
					}
				}
			});
			String idDefault = PreferencesGesblue.getTipusVehicleDefaultValue(mContext);
			if(!TextUtils.isEmpty(idDefault)) {
				mSelected = DatabaseAPI.getTipusVehicle(mContext, idDefault);
				if(mAdapter != null && mSelected != null) { //S'ha inicialitzat l'adapter i l'id que ens han passat de default existeix a la bbdd

					for(int i = 0; i < mAdapter.tipus_vehicleArrayList.size(); i++) {
						Model_TipusVehicle model = mAdapter.tipus_vehicleArrayList.get(i);
						if(model.getCoditipusvehicle() == mSelected.getCoditipusvehicle()) {
							mAdapter.setSelectedItem(i);
						}
					}
				}
			}
		} else {
			mBinding.linearConfirmar.layoutConfirmar.setVisibility(View.VISIBLE);
			mBinding.layoutButtons.setVisibility(View.GONE);
			mBinding.linearConfirmar.btnConfirmar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PreferencesGesblue.clearFormulari(mContext);
					mSancio.setModelTipusVehicle(mSelected);
					getIntent().putExtra(FormulariActivity.KEY_FORMULARI_CONFIRMAR, mSancio);
					setResult(RESULT_OK, getIntent());
					finish();
				}
			});
		}
	}

	private class SimpleAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Model_TipusVehicle> tipus_vehicleArrayList;
		private LayoutInflater inflater;

		private int selectedItem = -1;
		private long selectedItemId = -1;

		public SimpleAdapter(Context context, ArrayList<Model_TipusVehicle> tipus_vehicleArrayList) {
			this.context = context;
			this.tipus_vehicleArrayList = tipus_vehicleArrayList;
			inflater = LayoutInflater.from(this.context);
		}

		public void setSelectedItem(int i) {
			selectedItem = i;
		}

		@Override
		public int getCount() {
			return tipus_vehicleArrayList.size();
		}

		@Override
		public Object getItem(int i) {
			return tipus_vehicleArrayList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			Tipus_VehicleViewHolder tipus_VehicleViewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.item_tipus, viewGroup, false);
				tipus_VehicleViewHolder = new Tipus_VehicleViewHolder(view);
				view.setTag(tipus_VehicleViewHolder);
			} else {
				tipus_VehicleViewHolder = (Tipus_VehicleViewHolder) view.getTag();
			}

			Model_TipusVehicle tipus_vehicle = (Model_TipusVehicle) getItem(i);

			tipus_VehicleViewHolder.titolTipus.setText(Utils.languageMultiplexer(tipus_vehicle.getNomtipusvehiclees(), tipus_vehicle.getNomtipusvehiclecat()));

			tipus_VehicleViewHolder.radioButton.setChecked(selectedItem == i);

			return view;
		}

		private class Tipus_VehicleViewHolder {
			TextView titolTipus;
			AppCompatRadioButton radioButton;

			public Tipus_VehicleViewHolder(View item) {
				titolTipus = (TextView) item.findViewById(R.id.titolTipus);
				radioButton = (AppCompatRadioButton) item.findViewById(R.id.radioButton);
			}
		}
	}
}
