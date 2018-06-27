package com.sixtemia.gesbluedroid.activities.passosformulari;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.sixtemia.gesbluedroid.databinding.ActivityPas0ZonaBinding;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Zona;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;

import java.util.ArrayList;

public class Pas0ZonaActivity extends GesblueFragmentActivity {

	ActivityPas0ZonaBinding mBinding;

	private Sancio mSancio;
	private Model_Zona mSelected;
	private ZonaAdapter mAdapter;
	private boolean primerCop;
	private ArrayList<Model_Zona> arrayAux;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pas0_zona);
		setupVisibleToolbar(mBinding.toolbar);

		if(getIntent().getExtras() != null) {
			mSancio = getIntent().getExtras().getParcelable(FormulariActivity.INTENT_SANCIO);
			primerCop = getIntent().getExtras().getBoolean(FormulariActivity.KEY_FORMULARI_PRIMER_COP, true);
		}

		 arrayAux = DatabaseAPI.getZones(mContext);

//		Collections.sort(arrayAux, new Comparator<Model_Zona>() {
//			@Override
//			public int compare(Model_Zona o1, Model_Zona o2) {
//				return o1.getNomzona().compareTo(o2.getNomzona());
//			}
//		});

		if(mAdapter == null) {
			mAdapter = new ZonaAdapter(mContext, arrayAux);
		} else {
			// Posicionem l'scroll a la primera posició
			mBinding.lv.setSelectionAfterHeaderView();
		}
		mBinding.lv.setAdapter(mAdapter);

		if(mSancio != null && mSancio.getModelZona() != null) {
			mSelected = mSancio.getModelZona();
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

				Model_Zona newSelected = arrayAux.get(position);

				if(mSelected!= null && newSelected != mSelected) {
					mSelected = newSelected;
					mAdapter.setSelectedItem(position);
					mAdapter.notifyDataSetChanged();
					PreferencesGesblue.remove(mContext, "numero");
				} else {
					mSelected = newSelected;
					mAdapter.setSelectedItem(position);
					mAdapter.notifyDataSetChanged();
				}

				mSelected = arrayAux.get(position);
				mAdapter.setSelectedItem(position);

				mAdapter.notifyDataSetChanged();
				Log.d("Zona0",mSelected.getNomzona());
			}
		});

		if(primerCop) {
			String id = PreferencesGesblue.getFormulariZona(mContext);
			if(!TextUtils.isEmpty(id)) {
				int index = -1;
				for(int i = 0; i< arrayAux.size(); i++) {
					if(Long.toString(arrayAux.get(i).getCodizona()).equals(id)) {
						index = i;
					}
				}
				mAdapter.setSelectedItem(index);
				mAdapter.notifyDataSetChanged();
			} else {
				String idDefault = PreferencesGesblue.getZonaDefaultValue(mContext);
				if(!TextUtils.isEmpty(idDefault)) {
					int index = -1;
					for(int i = 0; i< arrayAux.size(); i++) {
						if(Long.toString(arrayAux.get(i).getCodizona()).equals(idDefault)) {
							index = i;
						}
					}
					if(index != -1) {
						mAdapter.setSelectedItem(index);
						mAdapter.notifyDataSetChanged();
					}

				}
			}

			if(mSelected == null && mAdapter.getSelected() != null) {
				mSelected = mAdapter.getSelected();
			}

			mBinding.linearInferior.seguent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, Pas7NumeroActivity.class);
					if(mSelected == null) {
						Utils.showDialogNoPotsPassar(mContext);
					} else {
						mSancio.setModelZona(mSelected);
						intent.putExtra(FormulariActivity.INTENT_SANCIO, mSancio);
						startActivity(intent);
					}
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
					//PreferencesGesblue.clearFormulari(mContext);
					//mSancio.setModelZona(mSelected);
					PreferencesGesblue.setCodiZona(mContext,mSelected.getCodizona());
					Log.d("Codizona",""+mSelected.getCodizona());
					PreferencesGesblue.setNomZona(mContext,mSelected.getNomzona());
					PreferencesGesblue.setCodiCarrer(mContext,0);
					PreferencesGesblue.setNomCarrer(mContext,null);
					getIntent().putExtra(FormulariActivity.KEY_FORMULARI_CONFIRMAR, mSelected.getCodizona());
					setResult(RESULT_OK, getIntent());
					finish();
				}
			});
		}

		mBinding.etCerca.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!TextUtils.isEmpty(s) && s.length() > 0) {
					arrayAux = mAdapter.showFiltered(Utils.removeAccents(s.toString()));
				} else {
					arrayAux = mAdapter.showAll();
				}
			}
			@Override
			public void afterTextChanged(Editable s) {}
		});
	}

	private class ZonaAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Model_Zona> zonaArrayList;
		private ArrayList<Model_Zona> zonaArrayListToShow;
		private LayoutInflater inflater;

		private int selectedItem = -1;

		public ZonaAdapter(Context context, ArrayList<Model_Zona> zonaArrayList) {
			this.context = context;
			this.zonaArrayList = zonaArrayList;
			zonaArrayListToShow = zonaArrayList;
			inflater = LayoutInflater.from(this.context);
		}

		public void setSelectedItem(int i) {
			selectedItem = i;
			Log.d("seleccio0",""+i);
			PreferencesGesblue.setFormulariZona(mContext, Long.toString(zonaArrayListToShow.get(i).getCodizona()));
		}

		public Model_Zona getSelected() {
			if(selectedItem != -1) {
				Log.d("seleccio1",""+selectedItem);
				return zonaArrayListToShow.get(selectedItem);
			}
			return null;
		}

		public ArrayList<Model_Zona> showAll() {
			zonaArrayListToShow = new ArrayList<>();

			zonaArrayListToShow = zonaArrayList;
			notifyDataSetChanged();
			return zonaArrayListToShow;
		}

		public ArrayList<Model_Zona> showFiltered(String s) {
			zonaArrayListToShow = new ArrayList<>();

			for(Model_Zona c : zonaArrayList) {
				String nomZona = c.getNomzona().toLowerCase();
				String strFind = s.toLowerCase();
				if(nomZona.contains(strFind)) {
					zonaArrayListToShow.add(c);
				}
			}

			notifyDataSetChanged();
			return zonaArrayListToShow;
		}

		@Override
		public int getCount() {
			return zonaArrayListToShow.size();
		}

		@Override
		public Object getItem(int i) {
			return zonaArrayListToShow.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ZonaAdapter.ZonaViewHolder zonaViewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.item_tipus, viewGroup, false);
				zonaViewHolder = new ZonaAdapter.ZonaViewHolder(view);
				view.setTag(zonaViewHolder);
			} else {
				zonaViewHolder = (ZonaAdapter.ZonaViewHolder) view.getTag();
			}

			Model_Zona zona = (Model_Zona) getItem(i);

			zonaViewHolder.titolTipus.setText(zona.getNomzona());

			if(selectedItem == i) zonaViewHolder.radioButton.setChecked(true);
			else zonaViewHolder.radioButton.setChecked(false);

			return view;
		}

		private class ZonaViewHolder {
			TextView titolTipus;
			AppCompatRadioButton radioButton;

			public ZonaViewHolder(View item) {
				titolTipus = (TextView) item.findViewById(R.id.titolTipus);
				radioButton = (AppCompatRadioButton) item.findViewById(R.id.radioButton);
			}
		}
	}
}
