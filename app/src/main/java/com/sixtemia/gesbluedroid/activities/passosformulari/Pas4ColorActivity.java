package com.sixtemia.gesbluedroid.activities.passosformulari;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.Sancio;
import com.sixtemia.gesbluedroid.activities.FormulariActivity;
import com.sixtemia.gesbluedroid.customstuff.GesblueFragmentActivity;
import com.sixtemia.gesbluedroid.databinding.ActivityPas4ColorBinding;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Color;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Pas4ColorActivity extends GesblueFragmentActivity {

	private ActivityPas4ColorBinding mBinding;
	private Sancio mSancio;
	private Model_Color mSelected;
	private Pas4ColorActivity.ColorAdapter mAdapter;
	private boolean primerCop;

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pas4_color);
		setupVisibleToolbar(mBinding.toolbar);

		if(getIntent().getExtras() != null) {
			mSancio = getIntent().getExtras().getParcelable(FormulariActivity.INTENT_SANCIO);
			primerCop = getIntent().getExtras().getBoolean(FormulariActivity.KEY_FORMULARI_PRIMER_COP, true);
		}

		final ArrayList<Model_Color> arrayAux = DatabaseAPI.getColors(mContext);
		if(mAdapter == null) {
			mAdapter = new Pas4ColorActivity.ColorAdapter(mContext, arrayAux);
		}
		mBinding.gridView.setAdapter(mAdapter);

		if(mSancio != null && mSancio.getModelColor() != null) {
			mSelected = mSancio.getModelColor();
			int index = -1;
			for(int i = 0; i< arrayAux.size(); i++) {
				if(arrayAux.get(i).equals(mSelected)) {
					index = i;
				}
			}

			mAdapter.setSelectedItem(index);
			mAdapter.notifyDataSetChanged();
		}

		mBinding.gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Model_Color newSelected = arrayAux.get(position);

				if(mSelected!= null && newSelected != mSelected) {
					mSelected = newSelected;
					mAdapter.setSelectedItem(position);
					mAdapter.notifyDataSetChanged();
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
			String id = PreferencesGesblue.getColorDefaultValue(mContext);
			if(!TextUtils.isEmpty(id)) {
				int index = -1;
				for(int i = 0; i< arrayAux.size(); i++) {
					if(arrayAux.get(i).getCodicolor().equals(id)) {
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
					Intent intent = new Intent(mContext, Pas5InfraccioActivity.class);
					if(mSelected == null) {
						Utils.showDialogNoPotsPassar(mContext);
					} else {
						mSancio.setModelColor(mSelected);
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
					mSancio.setModelColor(mSelected);
					getIntent().putExtra(FormulariActivity.KEY_FORMULARI_CONFIRMAR, mSancio);
					setResult(RESULT_OK, getIntent());
					finish();
				}
			});
		}
	}

	private class ColorAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Model_Color> colorsArrayList;
		private LayoutInflater inflater;

		private int selectedItem = -1;

		public ColorAdapter(Context context, ArrayList<Model_Color> colorsArrayList) {
			this.context = context;
			this.colorsArrayList = colorsArrayList;
			inflater = LayoutInflater.from(this.context);
		}

		public void setSelectedItem(int i) {
			selectedItem = i;
			PreferencesGesblue.setFormulariColor(mContext, colorsArrayList.get(i).getCodicolor());
		}

		public Model_Color getSelected() {
			if(selectedItem != -1) {
				return colorsArrayList.get(selectedItem);
			}
			return null;
		}

		@Override
		public int getCount() {
			return colorsArrayList.size();
		}

		@Override
		public Model_Color getItem(int i) {
			return colorsArrayList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ColorViewHolder colorViewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.item_color, viewGroup, false);
				colorViewHolder = new ColorViewHolder(view);
				view.setTag(colorViewHolder);
			} else {
				colorViewHolder = (ColorViewHolder) view.getTag();
			}

			Model_Color color = getItem(i);

			if(selectedItem == i) {
				colorViewHolder.selected.setVisibility(VISIBLE);
				try {
					colorViewHolder.colorView2.setBackgroundColor(Color.parseColor("#" + color.getHexcolor()));
				} catch (Exception ex) {
					colorViewHolder.colorView2.setBackgroundColor(Color.WHITE);
					ELog(ex);
				}
			} else {
				colorViewHolder.selected.setVisibility(GONE);
			}

			colorViewHolder.tvColor.setText(Utils.languageMultiplexer(color.getNomcolores(), color.getNomcolorcat()));
			try {
				colorViewHolder.colorView.setBackgroundColor(Color.parseColor("#" + color.getHexcolor()));
			} catch (Exception ex) {
				colorViewHolder.colorView.setBackgroundColor(Color.WHITE);
				ELog(ex);
			}

			return view;
		}

		private class ColorViewHolder {
			TextView tvColor;
			View colorView;
			RelativeLayout selected;
			RelativeLayout colorView2;


			public ColorViewHolder(View item) {
				tvColor = (TextView) item.findViewById(R.id.tvColor);
				colorView = (View) item.findViewById(R.id.colorView);

				selected = (RelativeLayout) item.findViewById(R.id.selected);
				colorView2 = (RelativeLayout) item.findViewById(R.id.colorView2);
			}
		}
	}
}
