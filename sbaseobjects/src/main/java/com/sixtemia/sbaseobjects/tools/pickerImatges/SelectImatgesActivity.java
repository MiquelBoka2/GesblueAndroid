package com.sixtemia.sbaseobjects.tools.pickerImatges;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.sixtemia.sbaseobjects.R;
import com.sixtemia.sbaseobjects.objects.SFragmentActivity;

import java.util.List;

import static com.sixtemia.sbaseobjects.tools.ImageTools.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER;

public class SelectImatgesActivity extends SFragmentActivity implements SelectImatgesFragment.OnImageSelectedListener {

	public static final String EXTRA_PATH_SELECTED_FILES = "path_selected_files";
	public static final String EXTRA_MULTIFILE_ENABLE = "enable_multifile_selection";
	public static final String EXTRA_MAX_NUM_FILES = "max_num_files";
	//	public static String BUNDLE_CATEGORIA_TITOL = "bundle_categoria_titol";
	private boolean bMultiFile = false;
	private int intMaxNumFiles;

	public SelectImatgesFragment fragmentImatges;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_imatges);

		Bundle b = this.getIntent().getExtras();

		List<String> listFilePath = null;

		if (b != null && b.containsKey(EXTRA_PATH_SELECTED_FILES)) {
			listFilePath = b.getStringArrayList(EXTRA_PATH_SELECTED_FILES);
		}

		if (b != null && b.containsKey(EXTRA_MULTIFILE_ENABLE)) {
			bMultiFile = (b.getInt(EXTRA_MULTIFILE_ENABLE) == 1);
		}

		if (b != null && b.containsKey(EXTRA_MAX_NUM_FILES)) {
			intMaxNumFiles = b.getInt(EXTRA_MAX_NUM_FILES);
		} else {
			intMaxNumFiles = -1;
		}

		initContent(listFilePath);
		initToolbar();
	}

	private void initToolbar() {
//		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
		if(getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//			getSupportActionBar().setHomeAsUpIndicator(R.drawable.navbar_back);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select_imatges, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			return true;
		} else if (itemId == R.id.action_attach) {
			if (fragmentImatges != null && fragmentImatges.getSelectedImageList().size() > 0) {
				Intent imageIntent = new Intent();
				imageIntent.setAction(IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
				imageIntent.putStringArrayListExtra("list", fragmentImatges.getSelectedImageList());
				sendBroadcast(imageIntent);

				finish();
				return true;
			}
			return false;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void initContent(List<String> _listFilePath) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if (fragmentImatges == null) {
			//fragmentImatges = new SelectImatgesFragment();
			fragmentImatges = SelectImatgesFragment.newInstance(_listFilePath, bMultiFile, intMaxNumFiles);
		}

		ft.replace(R.id.content_frame_imatges, fragmentImatges);
		ft.commit();
	}

	private void initActionBar() {
		//		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onImageSelected(int count) {
	}
}