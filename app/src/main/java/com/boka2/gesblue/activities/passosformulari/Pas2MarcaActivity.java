package com.boka2.gesblue.activities.passosformulari;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.boka2.gesblue.R;
import com.boka2.gesblue.Sancio;
import com.boka2.gesblue.activities.FormulariActivity;
import com.boka2.gesblue.customstuff.GesblueFragmentActivity;
import com.boka2.gesblue.customstuff.views.SGBTextView;
import com.boka2.gesblue.databinding.ActivityPas2MarcaBinding;
import com.boka2.gesblue.datamanager.DatabaseAPI;
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;
import com.boka2.gesblue.datamanager.database.model.Model_Marca;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.global.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Pas2MarcaActivity extends GesblueFragmentActivity {

	ActivityPas2MarcaBinding mBinding;
	Sancio mSancio;
	Model_Marca mSelected;
	SimpleAdapter mAdapter;
	private boolean primerCop;
	private Boolean adm=false;

	private ArrayList<Model_Marca> arrayAux;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pas2_marca);
		setupVisibleToolbar(mBinding.toolbar);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		mBinding.toolbar.txtLocalitzacioEstat.setVisibility(View.GONE);
		mBinding.toolbar.icOpciones.setVisibility(View.GONE);
		mBinding.toolbar.txtGesBlue.setVisibility(View.GONE);
		mBinding.toolbar.txtAny.setVisibility(View.GONE);

		if(getIntent().getExtras() != null) {
			adm=getIntent().getExtras().getBoolean("adm");
			mSancio = getIntent().getExtras().getParcelable(FormulariActivity.INTENT_SANCIO);
			primerCop = getIntent().getExtras().getBoolean(FormulariActivity.KEY_FORMULARI_PRIMER_COP, true);
		}

		arrayAux = DatabaseAPI.getMarques(mContext);


		final ArrayList<Model_Denuncia> arrayDenuncies = DatabaseAPI.getDenuncies(mContext);

		Log.d("Num denuncies locals",""+arrayDenuncies.size());

		Collections.sort(arrayAux, new Comparator<Model_Marca>() {
			@Override
			public int compare(Model_Marca o1, Model_Marca o2) {
				return o1.getNommarca().compareTo(o2.getNommarca());
			}
		});


		if(mAdapter == null) {
			mAdapter = new SimpleAdapter(mContext, arrayAux);
		} else {
			// Posicionem l'scroll a la primera posició
			mBinding.lv.setSelectionAfterHeaderView();
		}
		mBinding.lv.setAdapter(mAdapter);

		if(mSancio != null && mSancio.getModelMarca() != null) {
			mSelected = mSancio.getModelMarca();
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

				Model_Marca newSelected = arrayAux.get(position);

				if(mSelected!= null && newSelected != mSelected) {
					mSelected = newSelected;
					mAdapter.setSelectedItem(position);
					mAdapter.notifyDataSetChanged();
					PreferencesGesblue.remove(mContext, "model");
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

				if(primerCop){
					mBinding.linearInferior.seguent.callOnClick();
				}
				else{
					mBinding.linearConfirmar.btnConfirmar.callOnClick();
				}
			}
		});

		if(primerCop) {
			String id = PreferencesGesblue.getMarcaDefaultValue(mContext);
			if(!TextUtils.isEmpty(id)) {
				int index = -1;
				for(int i = 0; i< arrayAux.size(); i++) {
					if(arrayAux.get(i).getCodimarca().equals(id)) {
						index = i;
					}
				}

				if(index != 0) {
					mAdapter.setSelectedItem(index);
					mAdapter.notifyDataSetChanged();
				}
			}

			if(mSelected == null && mAdapter.getSelected() != null) {
				mSelected = mAdapter.getSelected();
			}

			mBinding.linearInferior.layoutButtons.setVisibility(View.VISIBLE);
			mBinding.linearInferior.seguent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, Pas3ModelActivity.class);
					if(mSelected == null) {
						Utils.showDialogNoPotsPassar(mContext);
					} else {
						mSancio.setModelMarca(mSelected);
						PreferencesGesblue.setFormulariMarca(mContext, mSelected.getCodimarca());
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
		}  else {
			mBinding.linearConfirmar.layoutConfirmar.setVisibility(View.VISIBLE);
			mBinding.linearInferior.layoutButtons.setVisibility(View.GONE);
			mBinding.linearConfirmar.btnConfirmar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PreferencesGesblue.clearFormulari(mContext);
					mSancio.setModelMarca(mSelected);
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
					arrayAux = mAdapter.showFiltered(Utils.removeAccents(s.toString()));
					if (arrayAux.size() == 1 ){
						Model_Marca newSelected = arrayAux.get(0);

						if(mSelected!= null && newSelected != mSelected) {
							mSelected = newSelected;
							mAdapter.setSelectedItem(0);
							mAdapter.notifyDataSetChanged();
							PreferencesGesblue.remove(mContext, "model");
							PreferencesGesblue.remove(mContext, "color");
							PreferencesGesblue.remove(mContext, "infraccio");
							//PreferencesGesblue.remove(mContext, "carrer");
							PreferencesGesblue.remove(mContext, "numero");
						} else {
							mSelected = newSelected;
							mAdapter.setSelectedItem(0);
							mAdapter.notifyDataSetChanged();
						}

						mSelected = arrayAux.get(0);
						mAdapter.setSelectedItem(0);

						mAdapter.notifyDataSetChanged();

					}
				} else {
					arrayAux = mAdapter.showAll();
				}
			}
			@Override
			public void afterTextChanged(Editable s) {}
		});
	}

	private class SimpleAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Model_Marca> marcaArrayList;
		private ArrayList<Model_Marca> marcaArrayListToShow;
		private LayoutInflater inflater;

		private int selectedItem = -1;

		public SimpleAdapter(Context context, ArrayList<Model_Marca> marcaArrayList) {
			this.context = context;
			this.marcaArrayList = marcaArrayList;
			marcaArrayListToShow = marcaArrayList;
			inflater = LayoutInflater.from(this.context);
		}

		public void setSelectedItem(int i) {
			selectedItem = i;
			PreferencesGesblue.setFormulariMarca(mContext, marcaArrayList.get(i).getCodimarca());
		}

		public Model_Marca getSelected() {
			if(selectedItem != -1) {
				return marcaArrayList.get(selectedItem);
			}
			return null;
		}

		public ArrayList<Model_Marca> showAll() {
			marcaArrayListToShow = new ArrayList<>();

			marcaArrayListToShow = marcaArrayList;
			notifyDataSetChanged();

			return  marcaArrayListToShow;
		}

		public ArrayList<Model_Marca> showFiltered(String s) {
			marcaArrayListToShow = new ArrayList<>();

			for(Model_Marca m : marcaArrayList) {
				String nomMarca = m.getNommarca().toLowerCase();
				String strFind = s.toLowerCase();
				if(nomMarca.contains(strFind)) {
					marcaArrayListToShow.add(m);
				}
			}

			notifyDataSetChanged();
			return  marcaArrayListToShow;
		}

		@Override
		public int getCount() {
			return marcaArrayListToShow.size();
		}

		@Override
		public Object getItem(int i) {
			return marcaArrayListToShow.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			MarcaViewHolder marcaViewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.item_llistat_marca, viewGroup, false);
				marcaViewHolder = new MarcaViewHolder(view);
				view.setTag(marcaViewHolder);
			} else {
				marcaViewHolder = (MarcaViewHolder) view.getTag();
			}

			Model_Marca marca = (Model_Marca) getItem(i);

			marcaViewHolder.titolMarca.setText(marca.getNommarca());
			Glide.with(context).load(marca.getImatgemarca()).into(marcaViewHolder.ivLogo);

			if(i == 0) {
				marcaViewHolder.vLinia.setVisibility(View.VISIBLE);
				marcaViewHolder.firstLetter.setVisibility(View.VISIBLE);
				marcaViewHolder.firstLetter.setText(marca.getNommarca().substring(0,1));
			} else if(!((Model_Marca)getItem(i-1)).getNommarca().substring(0,1).equals(marca.getNommarca().substring(0,1))) {
				marcaViewHolder.vLinia.setVisibility(View.VISIBLE);
				marcaViewHolder.firstLetter.setVisibility(View.VISIBLE);
				marcaViewHolder.firstLetter.setText(marca.getNommarca().substring(0,1));
			} else {
				marcaViewHolder.vLinia.setVisibility(View.INVISIBLE);
				marcaViewHolder.firstLetter.setVisibility(View.INVISIBLE);
				marcaViewHolder.firstLetter.setText("");
			}

			if(selectedItem == i) marcaViewHolder.radioButton.setChecked(true);
			else marcaViewHolder.radioButton.setChecked(false);

			return view;
		}

		private class MarcaViewHolder {
			SGBTextView titolMarca;
			AppCompatRadioButton radioButton;
			ImageView ivLogo;
			SGBTextView firstLetter;
			View vLinia;

			public MarcaViewHolder(View item) {
				titolMarca = (SGBTextView) item.findViewById(R.id.titolMarca);
				firstLetter = (SGBTextView) item.findViewById(R.id.firstLetter);
				radioButton = (AppCompatRadioButton) item.findViewById(R.id.radioButton);
				ivLogo = (ImageView) item.findViewById(R.id.ivLogo);
				vLinia = item.findViewById(R.id.vLinia);
			}
		}
	}
}