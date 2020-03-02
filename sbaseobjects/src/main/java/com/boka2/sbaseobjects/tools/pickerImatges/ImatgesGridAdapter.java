package com.boka2.sbaseobjects.tools.pickerImatges;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.boka2.sbaseobjects.R;

import java.util.ArrayList;
import java.util.List;

public class ImatgesGridAdapter extends ArrayAdapter<MediaModel> {

	private List<MediaModel> mGalleryModelList;
	LayoutInflater viewInflater;
	private Activity activity;

	private VideoLoadAsync videoLoad;
	private ArrayList<String> noAvailables;

	public ImatgesGridAdapter(Activity a, int resource, List<MediaModel> categories) {
		super(a, resource, categories);
		mGalleryModelList = categories;
		activity = a;
		viewInflater = LayoutInflater.from(a);

		videoLoad = new VideoLoadAsync();
		noAvailables = new ArrayList<String>();
	}

	public int getCount() {
		return mGalleryModelList.size();
	}

	@Override
	public MediaModel getItem(int position) {
		return mGalleryModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		ImageView imatge, imatgeMask;
		CheckedTextView checkBoxTextView;
		View viewSelected;
		ImageView video;
		ProgressBar pbLoading;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			convertView = viewInflater.inflate(R.layout.item_grid_imatges, parent, false);

			holder = new ViewHolder();
			holder.checkBoxTextView = (CheckedTextView) convertView.findViewById(R.id.checkTextViewFromMediaChooserGridItemRowView);
			holder.imatge = (ImageView) convertView.findViewById(R.id.imgImatge);
			holder.imatgeMask = (ImageView) convertView.findViewById(R.id.imgImatgeMask);
			holder.viewSelected = convertView.findViewById(R.id.viewSelected);
			holder.video = (ImageView) convertView.findViewById(R.id.imgVideo);
			holder.pbLoading = (ProgressBar) convertView.findViewById(R.id.pbLoading);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
			//			holder.imatge.setImageDrawable(null);
		}
		holder.imatge.setTag(mGalleryModelList.get(position).url);
		holder.pbLoading.setVisibility(View.VISIBLE);

		//		LayoutParams imageParams = (LayoutParams) holder.imatge.getLayoutParams();
		//		imageParams.width = mWidth / 4;
		//		imageParams.height = mWidth / 4;
		//
		//		holder.imatge.setLayoutParams(imageParams);
		//
		//		// set the status according to this Category item
		//
		//		ImageLoadAsync loadAsync = new ImageLoadAsync(mContext, holder.imatge, mWidth / 4);
		//		loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mGalleryModelList.get(position).url);
		//
		//		holder.checkBoxTextView.setChecked(mGalleryModelList.get(position).status);
		//		return convertView;

		// Fixem el height de la imatge
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int px = dm.widthPixels;
		int numColumnes = 4;

		//		String url = mGalleryModelList.get(position).url;
		//		String extension = url.substring(url.lastIndexOf('.') + 1, url.length());
		//		MimeTypeMap mime = MimeTypeMap.getSingleton();
		//		String type = mime.getMimeTypeFromExtension(extension);

		holder.video.setVisibility(View.GONE);

		if (!videoLoad.contains(mGalleryModelList.get(position).url) && !noAvailables.contains(mGalleryModelList.get(position).url)) {
			ImageLoadAsync loadAsync = new ImageLoadAsync(activity, holder.imatge, px / numColumnes, holder.video, holder.pbLoading, videoLoad, noAvailables);
			loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mGalleryModelList.get(position).url);
		} else if (videoLoad.contains(mGalleryModelList.get(position).url)) {
			videoLoad.getVideoThumb(holder.imatge, px / numColumnes, px / numColumnes, mGalleryModelList.get(position).url, holder.video, holder.pbLoading);
		} else if (noAvailables.contains(mGalleryModelList.get(position).url)) {
			holder.imatge.setImageResource(R.drawable.no_media);
		}

		//		if (type != null && type.contains("video")) {
		//			holder.video.setVisibility(View.VISIBLE);
		//			videoLoad.getVideoThumb(holder.imatge, px / numColumnes, px / numColumnes, mGalleryModelList.get(position).url);
		//		}

		holder.imatge.getLayoutParams().height = px / numColumnes;
		
		holder.imatgeMask.getLayoutParams().width = px / numColumnes;
		holder.imatgeMask.getLayoutParams().height = px / numColumnes;
		
		holder.viewSelected.getLayoutParams().width = px / numColumnes;
		holder.viewSelected.getLayoutParams().height = px / numColumnes;

		holder.checkBoxTextView.setChecked(mGalleryModelList.get(position).status);
		holder.imatge.setScaleType(ScaleType.CENTER_CROP);
		if (mGalleryModelList.get(position).status) {
			holder.imatgeMask.setVisibility(View.VISIBLE);
			holder.viewSelected.setVisibility(View.VISIBLE);
		} else {
			holder.imatgeMask.setVisibility(View.GONE);
			holder.viewSelected.setVisibility(View.GONE);
		}

		return convertView;
	}

	public void deseleccionaOtros() {
		for (int i = 0; i < mGalleryModelList.size(); i++) {
			if (mGalleryModelList.get(i).status) {
				mGalleryModelList.get(i).status = false;
			}
		}
	}
}
