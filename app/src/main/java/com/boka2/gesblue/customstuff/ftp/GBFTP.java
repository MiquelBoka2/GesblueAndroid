package com.boka2.gesblue.customstuff.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.util.List;

/*
 * Created by Boka2.
 */
public class GBFTP {
	private static final String TAG = "GesBlueFTP";
	private static FTPClient mClient;
	private static FTPListener mListener;
	private static List<GBFileUpload> mUploadList;
	private static String gbf1;
	private static int gbf2;
	private static String gbf3;
	private static String gbf4;
	private static String gbf5;
	private static String gbf6;

	/*
	 * Inicialitza el client FTP (connecta i fa login si cal).
	 *
	 * Si la inicialització acaba estant connectat, es comença la pujada de arxius automàticament.
	 *
	 * @param context Contexte de l'activity
	 * @param toUpload Llistat de arxius a pujar
	 * @param listener Listener dels possibles events
	 */
/*	public static void init(@NonNull List<GBFileUpload> toUpload, @NonNull FTPListener listener) {
		mListener = listener;
		if(mUploadList == null) {
			mUploadList = new ArrayList<>();
		} else {
			mUploadList.clear();
		}
		mUploadList.addAll(toUpload);

		gbf1 = "gesblue.com";
		gbf2 = 21;
		gbf3 = "gesblue";
		gbf4 = "web/admin/fotosdenuncies/";
		gbf5 = "%1$s/%2$s";
		gbf6 = "%1$s/%2$s/%3$s.jpg";

		new Thread(new Runnable() {
			@Override
			public void run() {
				connect();
			}
		}).start();
	}

	public static void close() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mClient.logout();
					mClient.disconnect();
					mClient = null;
					mListener = null;
				} catch (Exception ex) {
					Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
				}
			}
		}).start();
	}

	private static String getHost() {
		return gbf1;
	}

	private static int getPort() {
		return gbf2;
	}

	private static String getUsername() {
		return gbf3;
	}

	private static String getPath() {
		return String.format("%s%s", gbf4, gbf5);
	}

	private static String getParentPath() {
		return gbf4 + "%s";
	}

	private static String getFilePath() {
		return String.format("%s%s", gbf4, gbf6);
	}

	private static void connect() {
		if(mClient == null) {
			mClient = new FTPClient();
		}
		try {
			if (!mClient.isConnected()) {
				mClient.connect(getHost(), getPort());

				if(FTPReply.isPositiveCompletion(mClient.getReplyCode())) {
					login();
				} else {
					mListener.connectionError(new Exception("Connection error. Wrong address?"));
				}
			} else {
				mListener.connected();
			}
		} catch (Exception ex) {
			mListener.connectionError(ex);
		}
	}

	private static void login() throws IOException {
		//String e = Utils.e(context, "", true);
		String p = "Sjos0o6n";
		boolean status = mClient.login(getUsername(), p);
		mClient.setFileType(BINARY_FILE_TYPE);
		mClient.enterLocalPassiveMode();

		if(status) {
			mListener.connected();
		} else {
			mListener.connectionError(new Exception("Connection error. Wrong pwd?"));
		}
	}


	private static void initDirs(String concessio) {
		try {
			mClient.makeDirectory("/web");
			mClient.makeDirectory("/web/admin");
			mClient.makeDirectory("/web/admin/fotosdenuncies");
			mClient.makeDirectory("/web/admin/fotosdenuncies/" + concessio);
		} catch (Exception ex) {
			Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
		}
	}

	static void startUpload() {
		DataUtils ftpData = new DataUtils() {
			@Override
			protected String getDataFormatting() {
				return "yyyyMMdd";
			}
		};

		for(GBFileUpload file : mUploadList) {
			initDirs(file.getConcessio());
		}

		int max = mUploadList.size();
		mListener.uploadStart(max);
		for(int i = 0; i < max; i++) {
			boolean ok = false;
			GBFileUpload file = mUploadList.get(i);
			BufferedInputStream bis = null;
			try {
				FileInputStream fis = new FileInputStream(file.getLocalPath());
				bis = new BufferedInputStream(fis);
			} catch (Exception e) {
				mListener.uploadError(file, e);
			} finally {
				String concessio = file.getConcessio();
				String denuncia = file.getNumDenuncia();

				String uploadPath = String.format(getPath(), concessio, ftpData.getStringData());
				String uploadFilePath = String.format(getFilePath(), concessio, ftpData.getStringData(), denuncia);

				try {
					Log.i(TAG, "Uploading: " + uploadFilePath);
					try {
						FTPFile[] list = mClient.listDirectories(String.format(getParentPath(), concessio));
						boolean found = false;
						int dirI = 0;
						while(!found && dirI < list.length) {
							found = list[dirI].getName().contains(ftpData.getStringData());
							dirI++;
						}
						if(!found) {
							ok = mClient.makeDirectory(uploadPath);
							if(!ok) {
								mListener.uploadError(file, new Exception("Create folder error: " + mClient.getReplyString()));
							}
						} else {
							ok = true;
						}
					} catch (Exception ex) {
						Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
					}
					if(ok) {
						ok = mClient.storeFile(uploadFilePath, bis);
						if (!ok) {
							mListener.uploadError(file, new Exception("Upload error: " + mClient.getReplyString()));
						}
					}
				} catch (IOException e) {
					mListener.uploadError(file, e);
				}
			}
			mListener.fileUploaded(i + 1, file, ok);
			if(!ok) {
				mListener.uploadComplete();
				return;
			}
		}
		mListener.uploadComplete();
	}*/
}