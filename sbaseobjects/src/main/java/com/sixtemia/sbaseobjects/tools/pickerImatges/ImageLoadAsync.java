/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.sixtemia.sbaseobjects.tools.pickerImatges;

import android.content.Context;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sixtemia.sbaseobjects.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ImageLoadAsync extends MediaAsync<String, String, String> {

	private ImageView mImageView;
	private Context mContext;
	private int mWidth;
	private int mHeight;
	private ImageView mImgPlay;
	private String mUrl;
	private ProgressBar mPbLoading;
	private VideoLoadAsync mVideoLoadAsync;
	private ArrayList<String> mNoAvailables;
	private boolean bCrop = true;

	public ImageLoadAsync(Context context, ImageView imageView, int width, ImageView imgPlay, ProgressBar pbLoading, VideoLoadAsync videoLoadAsync, ArrayList<String> noAvailables) {
		mImageView = imageView;
		mContext = context;
		mWidth = width;
		mHeight = width;
		mImgPlay = imgPlay;
		mPbLoading = pbLoading;
		mVideoLoadAsync = videoLoadAsync;
		mNoAvailables = noAvailables;
	}

	public ImageLoadAsync(Context context, ImageView imageView, int width, ImageView imgPlay, ProgressBar pbLoading, VideoLoadAsync videoLoadAsync, ArrayList<String> noAvailables, boolean _bCrop) {
		mImageView = imageView;
		mContext = context;
		mWidth = width;
		mHeight = width;
		mImgPlay = imgPlay;
		mPbLoading = pbLoading;
		mVideoLoadAsync = videoLoadAsync;
		mNoAvailables = noAvailables;
		bCrop = _bCrop;
	}

	public ImageLoadAsync(Context context, ImageView imageView, int width, int height, ImageView imgPlay, VideoLoadAsync videoLoadAsync, ArrayList<String> noAvailables) {
		mImageView = imageView;
		mContext = context;
		mWidth = width;
		mHeight = height;
		mImgPlay = imgPlay;
		mPbLoading = null;
		mVideoLoadAsync = videoLoadAsync;
		mNoAvailables = noAvailables;
	}

	@Override
	protected String doInBackground(String... params) {
		String url = params[0].toString();
		mUrl = url;

		return url;
	}

	@Override
	protected void onPostExecute(final String result) {
		if (bCrop) {
			Picasso.with(mContext).load(new File(result)).resize(mWidth, mHeight).centerCrop().into(mImageView, new Callback() {

				@Override
				public void onSuccess() {
					if (mPbLoading != null) {
						mPbLoading.setVisibility(View.GONE);
					}
				}

				@Override
				public void onError() {
					String extension = mUrl.substring(mUrl.lastIndexOf('.') + 1, mUrl.length());
					MimeTypeMap mime = MimeTypeMap.getSingleton();
					String type = mime.getMimeTypeFromExtension(extension);

					if (type != null && type.contains("video")) {
						mVideoLoadAsync.getVideoThumb(mImageView, mWidth, mHeight, result, mImgPlay, mPbLoading);
					} else if (mPbLoading != null) {
						mImageView.setImageResource(R.drawable.no_media);
						mPbLoading.setVisibility(View.GONE);
						mNoAvailables.add(mUrl);
					}
				}
			}); // .centerCrop().placeholder(R.drawable.loading)
		} else {
			Picasso.with(mContext).load(new File(result)).into(mImageView, new Callback() {

				@Override
				public void onSuccess() {
					if (mPbLoading != null) {
						mPbLoading.setVisibility(View.GONE);
					}
				}

				@Override
				public void onError() {
					String extension = mUrl.substring(mUrl.lastIndexOf('.') + 1, mUrl.length());
					MimeTypeMap mime = MimeTypeMap.getSingleton();
					String type = mime.getMimeTypeFromExtension(extension);

					if (type != null && type.contains("video")) {
						mVideoLoadAsync.getVideoThumb(mImageView, mWidth, mHeight, result, mImgPlay, mPbLoading);
					} else if (mPbLoading != null) {
						mImageView.setImageResource(R.drawable.no_media);
						mPbLoading.setVisibility(View.GONE);
						mNoAvailables.add(mUrl);
					}
				}
			});
		}
	}
}
