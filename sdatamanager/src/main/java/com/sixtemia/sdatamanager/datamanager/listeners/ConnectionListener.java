package com.sixtemia.sdatamanager.datamanager.listeners;

import android.content.Context;
import android.os.Handler;

/**
 * Created by rubengonzalez on 5/9/16.
 */

public abstract class ConnectionListener {
	private Handler mHandler;
	private int mLength;

	public ConnectionListener(Context c) {
		mHandler = new Handler(c.getMainLooper());
	}

	public void connected(final int ln) {
		mLength = ln;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onConnected(ln);
			}
		});
	}

	public void progress(final int pg) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onProgress(pg);
			}
		});
	}

	public void finished() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onFinished();
			}
		});
	}

	protected abstract void onConnected(int length);
	protected abstract void onProgress(int progress);
	protected abstract void onFinished();

	public int getLength() {
		return mLength;
	}
}