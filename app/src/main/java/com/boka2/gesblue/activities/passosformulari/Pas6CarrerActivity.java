package com.boka2.gesblue.activities.passosformulari;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boka2.gesblue.R;
import com.boka2.gesblue.Sancio;
import com.boka2.gesblue.activities.FormulariActivity;
import com.boka2.gesblue.customstuff.GesblueFragmentActivity;
import com.boka2.gesblue.databinding.ActivityPas6CarrerBinding;
import com.boka2.gesblue.datamanager.DatabaseAPI;
import com.boka2.gesblue.datamanager.database.model.Model_Carrer;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.global.Utils;

import java.util.ArrayList;

public class Pas6CarrerActivity extends GesblueFragmentActivity {

	ActivityPas6CarrerBinding mBinding;

	private Sancio mSancio;
	private Model_Carrer mSelected;
	private CarrerAdapter mAdapter;
	private boolean primerCop;
	private ArrayList<Model_Carrer> arrayAux;
	private Boolean adm=false;

	private Boolean checked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pas6_carrer);
		setupVisibleToolbar(mBinding.toolbar);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		mBinding.toolbar.txtLocalitzacioEstat.setVisibility(View.GONE);
		mBinding.toolbar.icOpciones.setVisibility(View.GONE);
		mBinding.toolbar.txtGesBlue.setVisibility(View.GONE);
		mBinding.toolbar.txtAny.setVisibility(View.GONE);
		mBinding.toolbar.imgUnlock.setVisibility(View.GONE);

		if(getIntent().getExtras() != null) {
			adm=getIntent().getExtras().getBoolean("adm");

			mSancio = getIntent().getExtras().getParcelable(FormulariActivity.INTENT_SANCIO);
			primerCop = getIntent().getExtras().getBoolean(FormulariActivity.KEY_FORMULARI_PRIMER_COP, true);
		}

		arrayAux = DatabaseAPI.getCarrersZona(mContext);

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

			if(index != -1){
				mAdapter.setSelectedItem(index);
				mAdapter.notifyDataSetChanged();
			}
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

				//Variable checked que ens diu si hi ha algun element clicat.
				checked = true;

				mAdapter.notifyDataSetChanged();

				if(primerCop){
					mBinding.linearInferior.seguent.callOnClick();
				}
				else{
					mBinding.linearConfirmar.btnConfirmar.callOnClick();
				}
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

				if(index != -1) {
					mAdapter.setSelectedItem(index);
					mAdapter.notifyDataSetChanged();
				}
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
						intent.putExtra("adm",adm);
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

					//Comprovem si hi ha alguna opció clicada, en cas negatiu doncs mostrem un missatge d'error, en cas a firmatiu, proseguim amb el codi.
					if(checked){
						PreferencesGesblue.setCodiCarrer(mContext,mSelected.getCodicarrer());
						PreferencesGesblue.setNomCarrer(mContext,mSelected.getNomcarrer());
						PreferencesGesblue.setPrefLastCodiCarrer(mContext,mSelected.getCodicarrer());
						PreferencesGesblue.setPrefLastNomCarrer(mContext,mSelected.getNomcarrer());
						//Per evitar el penjament degut al parsejament de les dades a sancio, desem als extres un boleà per dir al programa que ho porcessi de forma diferent.

						if(mSancio!=null){
							PreferencesGesblue.clearFormulari(mContext);
							mSancio.setNumero("");
							mSancio.setModelCarrer(mSelected);
							getIntent().putExtra(FormulariActivity.KEY_FORMULARI_CONFIRMAR, mSancio);
							setResult(RESULT_OK, getIntent());
							finish();
						}
						else{
							getIntent().putExtra("noSancio", true);
							getIntent().putExtra(FormulariActivity.KEY_FORMULARI_CONFIRMAR, mSelected.getCodicarrer());
							setResult(RESULT_OK, getIntent());
							finish();
						}

					}else{
						Utils.showCustomDatamanagerError(mContext, getString(R.string.mancaCarrer));
					}
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

		public ArrayList<Model_Carrer> showAll() {
			carrerArrayListToShow = new ArrayList<>();

			carrerArrayListToShow = carrerArrayList;
			notifyDataSetChanged();
			return carrerArrayListToShow;
		}

		public ArrayList<Model_Carrer> showFiltered(String s) {
			carrerArrayListToShow = new ArrayList<>();

			for(Model_Carrer c : carrerArrayList) {
				String nomCarrer = c.getNomcarrer().toLowerCase();
				String strFind = s.toLowerCase();
				if(nomCarrer.contains(strFind)) {
					carrerArrayListToShow.add(c);
				}
			}

			notifyDataSetChanged();

			return carrerArrayListToShow;
		}

		@Override
		public int getCount() {
			if(carrerArrayListToShow != null){
				return carrerArrayListToShow.size();
			}else{
				return 0;
			}
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
