package com.boka2.sbaseobjects.tools.pickerImatges;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.boka2.sbaseobjects.R;
import com.boka2.sbaseobjects.objects.SFragment;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SelectImatgesFragment extends SFragment {
	private ArrayList<String> mSelectedItems = new ArrayList<String>();
	private ArrayList<MediaModel> mGalleryModelList;
	private GridView mImageGridView;
	private View mView;
	private OnImageSelectedListener mCallback;
	private ImatgesGridAdapter mImageAdapter;
	private Cursor mImageCursor;
	private List<String> listFilePathSelectedInici; // Només serveix per saber les imatges seleccionades a l'inici. Després no s'actualitza mai més
	private boolean bMultiFile;
	private int intMaxSelectedImages;

	public static SelectImatgesFragment newInstance(List<String> _listFilePath, boolean _bMultiFile, int _intMaxSelectedImages) {
		SelectImatgesFragment fragment = new SelectImatgesFragment();

		fragment.listFilePathSelectedInici = _listFilePath;
		fragment.bMultiFile = _bMultiFile;
		fragment.intMaxSelectedImages = _intMaxSelectedImages;
		return fragment;
	}

	// Container Activity must implement this interface
	public interface OnImageSelectedListener {
		public void onImageSelected(int count);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnImageSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnImageSelectedListener");
		}
	}

	public SelectImatgesFragment() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (mView == null) {
			mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);

			mImageGridView = (GridView) mView.findViewById(R.id.gridViewFromMediaChooser);

			initPhoneImages();

		} else {
			((ViewGroup) mView.getParent()).removeView(mView);
			if (mImageAdapter == null || mImageAdapter.getCount() == 0) {
				Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
			}
		}

		return mView;
	}

	private void initPhoneImages() {
		try {
			final String orderBy = MediaStore.Files.FileColumns.DATE_ADDED;
			final String[] columns = { MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns._ID };

			String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE /*+ " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
																																+ MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO*/;

			mImageCursor = getActivity().getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, orderBy + " DESC");

			setAdapter(mImageCursor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setAdapter(Cursor imagecursor) {

		if (imagecursor.getCount() > 0) {

			mGalleryModelList = new ArrayList<MediaModel>();

			for (int i = 0; i < imagecursor.getCount(); i++) {
				imagecursor.moveToPosition(i);
				int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

				String strUriPath = imagecursor.getString(dataColumnIndex).toString();
				boolean status = false;

				if (listFilePathSelectedInici != null) {
					// Tenim imatges previament seleccionades
					for (int j = 0; j < listFilePathSelectedInici.size(); j++) {
						if (strUriPath.equals(listFilePathSelectedInici.get(j))) {
							status = true;
							mSelectedItems.add(strUriPath);
							j = listFilePathSelectedInici.size();
						}
					}
				}

				MediaModel galleryModel = new MediaModel(strUriPath, status);
				mGalleryModelList.add(galleryModel);
			}

			mImageAdapter = new ImatgesGridAdapter(getActivity(), 0, mGalleryModelList);
			mImageGridView.setAdapter(mImageAdapter);
		} else {
			Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
		}

		mImageGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
/*
				ImatgesGridAdapter adapter = (ImatgesGridAdapter) parent.getAdapter();
				MediaModel galleryModel = (MediaModel) adapter.getItem(position);
				File file = new File(galleryModel.url);
				String extension = galleryModel.url.substring(galleryModel.url.lastIndexOf('.') + 1, galleryModel.url.length());
				MimeTypeMap mime = MimeTypeMap.getSingleton();
				String type = mime.getMimeTypeFromExtension(extension);
				Intent intent;
				if (type == null || !type.contains("video")) {
					intent = new Intent(mContext, VerFotoActivity.class);
					intent.putExtra(VerFotoActivity.PATH_IMAGEN, file.getAbsolutePath());
//					intent.setDataAndType(Uri.fromFile(file), "image/*");
				} else {
					intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(file), "video/*");
				}

				startActivity(intent);
				*/
				return true;
			}
		});

		mImageGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// update the mStatus of each category in the adapter
				ImatgesGridAdapter adapter = (ImatgesGridAdapter) parent.getAdapter();
				MediaModel galleryModel = (MediaModel) adapter.getItem(position);

				if (!galleryModel.status) {
					if (!bMultiFile) {
						adapter.deseleccionaOtros();
						adapter.notifyDataSetChanged();
						if (mSelectedItems.size() > 0)
							mSelectedItems.remove(0);

					}

				}

				if (intMaxSelectedImages == -1 || galleryModel.status || mSelectedItems.size() < intMaxSelectedImages) {
					// inverse the status
					galleryModel.status = !galleryModel.status;

					adapter.notifyDataSetChanged();

					if (galleryModel.status) {
						if (!bMultiFile && mSelectedItems.size() > 0) {
							mSelectedItems.remove(mSelectedItems.get(0).trim());
						}
						mSelectedItems.add(galleryModel.url.toString());
						//MediaChooserConstants.SELECTED_MEDIA_COUNT ++;

					} else {
						mSelectedItems.remove(galleryModel.url.toString().trim());
						//MediaChooserConstants.SELECTED_MEDIA_COUNT --;
					}

					if (mCallback != null) {
						mCallback.onImageSelected(mSelectedItems.size());
						Intent intent = new Intent();
						intent.putStringArrayListExtra("list", mSelectedItems);
						getActivity().setResult(Activity.RESULT_OK, intent);
					}
				} else {
					//TODO: Mostrar un missatge?
//					UlifeUtils.showAlertDialogCustom(mContext, getString(R.string.sinistre_error_num_imatges), "",
//                            getString(R.string.btn_aceptar), null, null, true, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
				}


			}
		});
	}

	public ArrayList<String> getSelectedImageList() {
		return mSelectedItems;
	}

	public void addItem(String item) {
		if (mImageAdapter != null) {
			MediaModel model = new MediaModel(item, false);
			mGalleryModelList.add(0, model);
			mImageAdapter.notifyDataSetChanged();
		} else {
			initPhoneImages();
		}
	}
}
