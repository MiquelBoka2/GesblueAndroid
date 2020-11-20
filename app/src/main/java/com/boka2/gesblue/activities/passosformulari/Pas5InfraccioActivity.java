package com.boka2.gesblue.activities.passosformulari;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.text.TextUtils;
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
import com.boka2.gesblue.databinding.ActivityPas5InfraccioBinding;
import com.boka2.gesblue.datamanager.DatabaseAPI;
import com.boka2.gesblue.datamanager.database.model.Model_Infraccio;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.global.Utils;

import java.util.ArrayList;

import static android.view.View.GONE;

public class Pas5InfraccioActivity extends GesblueFragmentActivity {

	ActivityPas5InfraccioBinding mBinding;
	private Sancio mSancio;
	private Model_Infraccio mSelected;
	private InfraccioAdapter mAdapter;
	private boolean primerCop;
	private Boolean adm=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pas5_infraccio);
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

		final ArrayList<Model_Infraccio> arrayAux = DatabaseAPI.getInfraccionsZona(mContext);

//		Collections.sort(arrayAux, new Comparator<Model_Infraccio>() {
//			@Override
//			public int compare(Model_Infraccio o1, Model_Infraccio o2) {
//				return o1.getNom().compareTo(o2.getNom());
//			}
//		});

		if(mAdapter == null) {
			mAdapter = new InfraccioAdapter(mContext, arrayAux);
		} else {
			// Posicionem l'scroll a la primera posició
			mBinding.lv.setSelectionAfterHeaderView();
		}
		mBinding.lv.setAdapter(mAdapter);

		if(mSancio != null && mSancio.getModelInfraccio() != null) {
			mSelected = mSancio.getModelInfraccio();
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

				Model_Infraccio newSelected = arrayAux.get(position);

				if(mSelected!= null && newSelected != mSelected) {
					mSelected = newSelected;
					mAdapter.setSelectedItem(position);
					mAdapter.notifyDataSetChanged();
					PreferencesGesblue.remove(mContext, "carrer");
					PreferencesGesblue.remove(mContext, "numero");
				} else {
					mSelected = newSelected;
					mAdapter.setSelectedItem(position);
					mAdapter.notifyDataSetChanged();
				}

				mSelected = arrayAux.get(position);
				mAdapter.setSelectedItem(position);

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
			String id = PreferencesGesblue.getInfraccioDefaultValue(mContext);
			if(!TextUtils.isEmpty(id)) {
				int index = -1;
				for(int i = 0; i< arrayAux.size(); i++) {
					if(Long.toString(arrayAux.get(i).getCodi()).equals(id)) {
						index = i;
					}
				}
				mAdapter.setSelectedItem(index);
				mAdapter.notifyDataSetChanged();
			}

			if(mSelected == null && mAdapter.getSelected() != null) {
				mSelected = mAdapter.getSelected();
			}

			mBinding.linearInferior.seguent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					//PAS FINAL
					Intent intent = new Intent(mContext, FormulariActivity.class);
					if(mSelected == null) {
						Utils.showDialogNoPotsPassar(mContext);
					} else {
						mSancio.setModelInfraccio(mSelected);
						intent.putExtra(FormulariActivity.INTENT_SANCIO, mSancio);
						intent.putExtra("adm",adm);
						startActivity(intent);
					}


					/** PAS NUMERO
					Intent intent = new Intent(mContext, Pas7NumeroActivity.class);
					if(mSelected == null) {
						Utils.showDialogNoPotsPassar(mContext);
					} else {
						mSancio.setModelInfraccio(mSelected);
						intent.putExtra(FormulariActivity.INTENT_SANCIO, mSancio);
						intent.putExtra("formPrimerCop", true);
						intent.putExtra("adm",adm);
						startActivity(intent);
					}
					 **/
				}
			});

			mBinding.linearInferior.anterior.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});

			String idDefault = PreferencesGesblue.getInfraccioDefaultValue(mContext);
			if(!TextUtils.isEmpty(idDefault)) {
				mSelected = DatabaseAPI.getInfraccio(mContext, idDefault);
				if(mAdapter != null && mSelected != null) { //S'ha inicialitzat l'adapter i l'id que ens han passat de default existeix a la bbdd
					for(int i = 0; i < mAdapter.infraccioArrayList.size(); i++) {
						Model_Infraccio model = mAdapter.infraccioArrayList.get(i);
						if(model.getCodi() == mSelected.getCodi()) {
							mAdapter.setSelectedItem(i);
						}
					}
				}
			}

		} else {
			mBinding.linearConfirmar.layoutConfirmar.setVisibility(View.VISIBLE);
			mBinding.linearInferior.layoutButtons.setVisibility(View.GONE);
			mBinding.linearConfirmar.btnConfirmar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PreferencesGesblue.clearFormulari(mContext);
					mSancio.setModelInfraccio(mSelected);
					getIntent().putExtra(FormulariActivity.KEY_FORMULARI_CONFIRMAR, mSancio);
					setResult(RESULT_OK, getIntent());
					finish();
				}
			});
		}
	}

	private class InfraccioAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Model_Infraccio> infraccioArrayList;
		private LayoutInflater inflater;

		private int selectedItem = -1;
		private long selectedItemId = -1;

		public InfraccioAdapter(Context context, ArrayList<Model_Infraccio> _infraccioArrayList) {
			this.context = context;
			this.infraccioArrayList = _infraccioArrayList;
			inflater = LayoutInflater.from(this.context);
		}

		public void setSelectedItem(int i) {
			selectedItem = i;
			PreferencesGesblue.setFormulariInfraccio(mContext, Long.toString(infraccioArrayList.get(i).getCodi()));
		}

		public void setSelectedItemId(long _id) {
			selectedItemId = _id;
		}

		public Model_Infraccio getSelected() {
			if(selectedItem != -1) {
				return infraccioArrayList.get(selectedItem);
			}
			return null;
		}

		@Override
		public int getCount() {
			return infraccioArrayList.size();
		}

		@Override
		public Object getItem(int i) {
			return infraccioArrayList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			InfraccioAdapter.InfraccioViewHolder infraccioViewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.item_llistat_infraccio, viewGroup, false);
				infraccioViewHolder = new InfraccioAdapter.InfraccioViewHolder(view);
				view.setTag(infraccioViewHolder);
			} else {
				infraccioViewHolder = (InfraccioAdapter.InfraccioViewHolder) view.getTag();
			}

			Model_Infraccio infraccio = (Model_Infraccio) getItem(i);

			infraccioViewHolder.tvTitol.setVisibility(GONE);
			infraccioViewHolder.tvCos.setText(infraccio.getNom());

			infraccioViewHolder.radioButton.setChecked(selectedItem == i);


			return view;
		}

		private class InfraccioViewHolder {
			TextView tvTitol;
			TextView tvCos;
			AppCompatRadioButton radioButton;

			public InfraccioViewHolder(View item) {
				tvTitol = (TextView) item.findViewById(R.id.tvTitol);
				tvCos = (TextView) item.findViewById(R.id.tvCos);
				radioButton = (AppCompatRadioButton) item.findViewById(R.id.radioButton);
			}
		}
	}
}
