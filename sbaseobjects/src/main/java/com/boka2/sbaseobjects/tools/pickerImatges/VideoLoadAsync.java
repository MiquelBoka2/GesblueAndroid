package com.boka2.sbaseobjects.tools.pickerImatges;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.HashMap;

public class VideoLoadAsync {

	private HashMap<String, Bitmap> hashImatges;

	public VideoLoadAsync() {
		hashImatges = new HashMap<String, Bitmap>();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void getVideoThumb(ImageView _imgThumb, int _intWidth,
			int _intHeight, String _url, ImageView _imgPlay, ProgressBar _pbLoading) {

		GetThumbAsyncTask taskGetThumb = new GetThumbAsyncTask();
		taskGetThumb.url = _url;
		taskGetThumb.imgThumb = _imgThumb;
		taskGetThumb.intWidth = _intWidth;
		taskGetThumb.intHeight = _intHeight;
		taskGetThumb.imgPlay = _imgPlay;
		taskGetThumb.pbLoading = _pbLoading;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			taskGetThumb.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			taskGetThumb.execute();
		}
	}

	private class GetThumbAsyncTask extends AsyncTask<Void, Void, String> {

		public ImageView imgThumb;
		public int intWidth;
		public int intHeight;
		public String url;
		public ImageView imgPlay;
		public ProgressBar pbLoading;

		@Override
		protected String doInBackground(Void... params) {
			if (!contains(url)) {
				Bitmap bm = ThumbnailUtils.extractThumbnail(ThumbnailUtils
						.createVideoThumbnail(url, Thumbnails.MINI_KIND),
						intWidth, intHeight);
				if (bm != null) {
					hashImatges.put(url, bm);
				}
			}
			return url;
		}

		@Override
		protected void onPostExecute(String result) {
			imgPlay.setVisibility(View.VISIBLE);
			Bitmap bitmap = hashImatges.get(result);
			if (bitmap != null && imgThumb.getTag().equals(result)) {
				imgThumb.setImageBitmap(bitmap);
			}
			if (pbLoading != null) {
				pbLoading.setVisibility(View.GONE);
			}
		}

	}
	
	public boolean contains(String url) {

		if (!hashImatges.containsKey(url)) {
			return false;
		} else {
			return true;
		}
	}

}
