package com.boka2.gesblue.customstuff.ftp;

/**
 * Created by Boka2.
 */
public abstract class FTPListener {
	/*private Handler mHandler;

	public FTPListener() {
		mHandler = new Handler(Looper.getMainLooper());
	}

	void connected() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onConnectionSuccess();
			}
		});

		GBFTP.startUpload();
	}

	void connectionError(final Exception ex) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onConnectionError(ex);
			}
		});
	}

	void uploadError(final GBFileUpload file, final Exception ex) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onUploadError(file, ex);
			}
		});
	}

	void uploadStart(final int count) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onUploadStart(count);
			}
		});
	}

	void fileUploaded(final int done, final GBFileUpload file, final boolean success) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onFileUploaded(done, file, success);
			}
		});
	}

	void uploadComplete() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onUploadComplete();
			}
		});
	}

	public abstract void onConnectionSuccess();

	public abstract void onUploadError(GBFileUpload file, Exception ex);

	public abstract void onUploadStart(int count);

	public abstract void onFileUploaded(int done, GBFileUpload file, boolean success);

	public abstract void onUploadComplete();

	public abstract void onConnectionError(Exception ex);*/
}