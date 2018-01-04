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
import com.sixtemia.gesbluedroid.databinding.ActivityPas3ModelBinding;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Model;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Pas3ModelActivity extends GesblueFragmentActivity {

	ActivityPas3ModelBinding mBinding;
	private Sancio mSancio;
	private Model_Model mSelected;
	private Pas3ModelActivity.SimpleAdapter mAdapter;
	private boolean primerCop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pas3_model);
		setupVisibleToolbar(mBinding.toolbar);

		if(getIntent().getExtras() != null) {
			mSancio = getIntent().getExtras().getParcelable(FormulariActivity.INTENT_SANCIO);
			primerCop = getIntent().getExtras().getBoolean(FormulariActivity.KEY_FORMULARI_PRIMER_COP, true);
		}

		final ArrayList<Model_Model> arrayAux = DatabaseAPI.getModelsByMarca(mContext, Integer.parseInt(mSancio.getModelMarca().getCodimarca()));

		Collections.sort(arrayAux, new Comparator<Model_Model>() {
			@Override
			public int compare(Model_Model o1, Model_Model o2) {
				return o1.getNommodel().compareTo(o2.getNommodel());
			}
		});

		if(mAdapter == null) {
			mAdapter = new SimpleAdapter(mContext, arrayAux);
		} else {
			// Posicionem l'scroll a la primera posició
			mBinding.lv.setSelectionAfterHeaderView();
		}
		mBinding.lv.setAdapter(mAdapter);

		if(mSancio != null && mSancio.getModelModel() != null) {
			mSelected = mSancio.getModelModel();
			int index = -1;
			for(int i = 0; i< arrayAux.size(); i++) {
				if(arrayAux.get(i).equals(mSelected)) {
					index = i;
				}
			}
			mAdapter.setSelectedItem(index);
			mAdapter.notifyDataSetChanged();
		}
		else{
			mAdapter.setSelectedItem(0);

		}

		mBinding.lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Model_Model newSelected = arrayAux.get(position);

				if(mSelected!= null && newSelected != mSelected) {
					mSelected = newSelected;
					mAdapter.setSelectedItem(position);
					mAdapter.notifyDataSetChanged();
					PreferencesGesblue.remove(mContext, "color");
					PreferencesGesblue.remove(mContext, "infraccio");
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
			}
		});

		if(primerCop) {
			String id = PreferencesGesblue.getModelDefaultValue(mContext);
			if(!TextUtils.isEmpty(id)) {
				int index = -1;
				for(int i = 0; i< arrayAux.size(); i++) {
					if(arrayAux.get(i).getCodimodel().equals(id)) {
						index = i;
					}
				}
				mAdapter.setSelectedItem(index);
				mAdapter.notifyDataSetChanged();
			}

			if(mSelected == null && mAdapter.getSelected() != null) {
				mSelected = mAdapter.getSelected();
			}

			mBinding.linearInferior.layoutButtons.setVisibility(View.VISIBLE);
			mBinding.linearInferior.seguent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, Pas4ColorActivity.class);
					if(mSelected == null) {
						Utils.showDialogNoPotsPassar(mContext);
					} else {
						mSancio.setModelModel(mSelected);
						PreferencesGesblue.setFormulariModel(mContext, mSelected.getCodimodel());
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
		}  else {
			mBinding.linearConfirmar.layoutConfirmar.setVisibility(View.VISIBLE);
			mBinding.linearInferior.layoutButtons.setVisibility(View.GONE);
			mBinding.linearConfirmar.btnConfirmar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PreferencesGesblue.clearFormulari(mContext);
					mSancio.setModelModel(mSelected);
					getIntent().putExtra(FormulariActivity.KEY_FORMULARI_CONFIRMAR, mSancio);
					setResult(RESULT_OK, getIntent());
					finish();
				}
			});
		}

		mAdapter.showAll();

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

	private class SimpleAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Model_Model> modelArrayList;
		private ArrayList<Model_Model> modelArrayListToShow;
		private LayoutInflater inflater;

		private int selectedItem = -1;

		public SimpleAdapter(Context context, ArrayList<Model_Model> modelArrayList) {
			this.context = context;
			this.modelArrayList = modelArrayList;
			modelArrayListToShow = modelArrayList;
			inflater = LayoutInflater.from(this.context);
		}

		public void setSelectedItem(int i) {
			if(i != -1) {
				selectedItem = i;
				PreferencesGesblue.setFormulariModel(mContext, modelArrayList.get(i).getCodimodel());
			}
		}

		public Model_Model getSelected() {
			if(selectedItem != -1) {
				return modelArrayList.get(selectedItem);
			}
			return null;
		}

		public void showAll() {
			modelArrayListToShow = new ArrayList<>();

			modelArrayListToShow = modelArrayList;
			notifyDataSetChanged();
		}

		public void showFiltered(String s) {
			modelArrayListToShow = new ArrayList<>();

			for(Model_Model m : modelArrayList) {
				String nomMarca = m.getNommodel().toLowerCase();
				String strFind = s.toLowerCase();
				if(nomMarca.contains(strFind)) {
					modelArrayListToShow.add(m);
				}
			}

			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return modelArrayListToShow.size();
		}

		@Override
		public Object getItem(int i) {
			return modelArrayListToShow.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			Pas3ModelActivity.SimpleAdapter.ModelViewHolder modelViewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.item_tipus, viewGroup, false);
				modelViewHolder = new Pas3ModelActivity.SimpleAdapter.ModelViewHolder(view);
				view.setTag(modelViewHolder);
			} else {
				modelViewHolder = (Pas3ModelActivity.SimpleAdapter.ModelViewHolder) view.getTag();
			}

			Model_Model model = (Model_Model) getItem(i);

			modelViewHolder.titolTipus.setText(model.getNommodel());

			if(selectedItem == i) modelViewHolder.radioButton.setChecked(true);
			else modelViewHolder.radioButton.setChecked(false);

			return view;
		}

		private class ModelViewHolder {
			TextView titolTipus;
			AppCompatRadioButton radioButton;

			public ModelViewHolder(View item) {
				titolTipus = (TextView) item.findViewById(R.id.titolTipus);
				radioButton = (AppCompatRadioButton) item.findViewById(R.id.radioButton);
			}
		}
	}
}
