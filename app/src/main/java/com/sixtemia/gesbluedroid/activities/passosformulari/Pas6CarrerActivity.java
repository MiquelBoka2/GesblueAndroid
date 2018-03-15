package com.sixtemia.gesbluedroid.activities.passosformulari;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.sixtemia.gesbluedroid.databinding.ActivityPas6CarrerBinding;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Carrer;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;

import java.util.ArrayList;

public class Pas6CarrerActivity extends GesblueFragmentActivity {

	ActivityPas6CarrerBinding mBinding;

	private Sancio mSancio;
	private Model_Carrer mSelected;
	private CarrerAdapter mAdapter;
	private boolean primerCop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pas6_carrer);
		setupVisibleToolbar(mBinding.toolbar);

		if(getIntent().getExtras() != null) {
			mSancio = getIntent().getExtras().getParcelable(FormulariActivity.INTENT_SANCIO);
			primerCop = getIntent().getExtras().getBoolean(FormulariActivity.KEY_FORMULARI_PRIMER_COP, true);
		}

		final ArrayList<Model_Carrer> arrayAux = DatabaseAPI.getCarrersZona(mContext);

//		Collections.sort(arrayAux, new Comparator<Model_Carrer>() {
//			@Override
//			public int compare(Model_Carrer o1, Model_Carrer o2) {
//				return o1.getNomcarrer().compareTo(o2.getNomcarrer());
//			}
//		});

		if(mAdapter == null) {
			mAdapter = new CarrerAdapter(mContext, arrayAux);
		} else {
			// Posicionem l'scroll a la primera posició
			mBinding.lv.setSelectionAfterHeaderView();
		}
		mBinding.lv.setAdapter(mAdapter);

		if(mSancio != null && mSancio.getModelCarrer() != null) {
			mSelected = mSancio.getModelCarrer();
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

				Model_Carrer newSelected = arrayAux.get(position);

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
			}
		});

		if(primerCop) {
			String id = PreferencesGesblue.getFormulariCarrer(mContext);
			if(!TextUtils.isEmpty(id)) {
				int index = -1;
				for(int i = 0; i< arrayAux.size(); i++) {
					if(Long.toString(arrayAux.get(i).getCodicarrer()).equals(id)) {
						index = i;
					}
				}
				mAdapter.setSelectedItem(index);
				mAdapter.notifyDataSetChanged();
			} else {
				String idDefault = PreferencesGesblue.getCarrerDefaultValue(mContext);
				if(!TextUtils.isEmpty(idDefault)) {
					int index = -1;
					for(int i = 0; i< arrayAux.size(); i++) {
						if(Long.toString(arrayAux.get(i).getCodicarrer()).equals(idDefault)) {
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
						mSancio.setModelCarrer(mSelected);
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
					//mSancio.setModelCarrer(mSelected);
					PreferencesGesblue.setCodiCarrer(mContext,mSelected.getCodicarrer());
					PreferencesGesblue.setNomCarrer(mContext,mSelected.getNomcarrer());
					getIntent().putExtra(FormulariActivity.KEY_FORMULARI_CONFIRMAR, mSelected.getCodicarrer());
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
					mAdapter.showFiltered(Utils.removeAccents(s.toString()));
				} else {
					mAdapter.showAll();
				}
			}
			@Override
			public void afterTextChanged(Editable s) {}
		});
	}

	private class CarrerAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Model_Carrer> carrerArrayList;
		private ArrayList<Model_Carrer> carrerArrayListToShow;
		private LayoutInflater inflater;

		private int selectedItem = -1;

		public CarrerAdapter(Context context, ArrayList<Model_Carrer> carrerArrayList) {
			this.context = context;
			this.carrerArrayList = carrerArrayList;
			carrerArrayListToShow = carrerArrayList;
			inflater = LayoutInflater.from(this.context);
		}

		public void setSelectedItem(int i) {
			selectedItem = i;
			PreferencesGesblue.setFormulariCarrer(mContext, Long.toString(carrerArrayList.get(i).getCodicarrer()));
		}

		public Model_Carrer getSelected() {
			if(selectedItem != -1) {
				return carrerArrayList.get(selectedItem);
			}
			return null;
		}

		public void showAll() {
			carrerArrayListToShow = new ArrayList<>();

			carrerArrayListToShow = carrerArrayList;
			notifyDataSetChanged();
		}

		public void showFiltered(String s) {
			carrerArrayListToShow = new ArrayList<>();

			for(Model_Carrer c : carrerArrayList) {
				String nomCarrer = c.getNomcarrer().toLowerCase();
				String strFind = s.toLowerCase();
				if(nomCarrer.contains(strFind)) {
					carrerArrayListToShow.add(c);
				}
			}

			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return carrerArrayListToShow.size();
		}

		@Override
		public Object getItem(int i) {
			return carrerArrayListToShow.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			CarrerAdapter.CarrerViewHolder carrerViewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.item_tipus, viewGroup, false);
				carrerViewHolder = new CarrerAdapter.CarrerViewHolder(view);
				view.setTag(carrerViewHolder);
			} else {
				carrerViewHolder = (CarrerAdapter.CarrerViewHolder) view.getTag();
			}

			Model_Carrer carrer = (Model_Carrer) getItem(i);

			carrerViewHolder.titolTipus.setText(carrer.getNomcarrer());

			if(selectedItem == i) carrerViewHolder.radioButton.setChecked(true);
			else carrerViewHolder.radioButton.setChecked(false);

			return view;
		}

		private class CarrerViewHolder {
			TextView titolTipus;
			AppCompatRadioButton radioButton;

			public CarrerViewHolder(View item) {
				titolTipus = (TextView) item.findViewById(R.id.titolTipus);
				radioButton = (AppCompatRadioButton) item.findViewById(R.id.radioButton);
			}
		}
	}
}
