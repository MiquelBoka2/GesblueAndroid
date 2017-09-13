package com.sixtemia.spushnotifications.db;

import android.content.Context;

import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.sixtemia.spushnotifications.model.SModPushNotification;
import com.sixtemia.sutils.classes.SSystemUtils;

import java.io.File;

/*
 * CLASSE NECESSARIA PER UTILITZAR ADA DATAFRAMEWORK (framework base de dades)
 */

public class DataContext extends ObjectContext {

	final static String DATABASE_FOLDER = "Sixtemia";
	final static String TARGET_PROJECT_NAME = "spush";
	final static String DATABASE_NAME = "spushnotifications.db";
	final static int DATABASE_VERSION = 2;

	/**************************************************/
	/* OBJECTSETS DEFINITION */
	/**************************************************/
	public NotificationsObjectSet NotificacionsSet;
		
	
	public static Context mContext;

	/**************************************************/
	/* CONSTRUCTORS */
	/**************************************************/

	public DataContext(Context pContext) {
		// Set a custom DataBase path and version.
		super(pContext, String.format("%s%s", getDataBaseFolder(pContext), DATABASE_NAME), DATABASE_VERSION);
		// super(pContext, DATABASE_NAME, DATABASE_VERSION);

		// Initialize the ObjectContext
		initializeContext();
	}

	private static String getDataBaseFolder(Context context) {
		String folderPath = "";
		if (SSystemUtils.isDebugging(context)) {
			String databasePath = context.getExternalCacheDir().getAbsolutePath() + File.separator + DATABASE_FOLDER + File.separator
					+ TARGET_PROJECT_NAME
					+ File.separator + DATABASE_NAME;

			folderPath = context.getExternalCacheDir().getAbsolutePath() + File.separator + DATABASE_FOLDER + File.separator
					+ TARGET_PROJECT_NAME;
			File dbFolder = new File(folderPath);
			if (!dbFolder.exists()) {
				dbFolder.mkdirs();
			}
			return databasePath;
		} else {
			return DATABASE_NAME;
		}
	}

//	private static String getDataBaseFolder() {
//		String folderPath = "";
//		if (SSystemUtils.isDebugging(mContext)) {
//			try {
//				folderPath = String.format(DATABASE_FOLDER, Environment.getExternalStorageDirectory().getAbsolutePath());
//				File dbFolder = new File(folderPath);
//				if (!dbFolder.exists()) {
//					dbFolder.mkdirs();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return folderPath;
//	}

	/**************************************************/
	/* EVENTS */
	/**************************************************/
	/*
	 * @Override protected void onPopulate(SQLiteDatabase pDatabase) { }
	 * 
	 * @Override protected void onError(Exception pException) { ExceptionsHelper.manage(getContext(), pException); }
	 */

	/**************************************************/
	/* PRIVATE METHOS */
	/**************************************************/

	private void initializeContext() {
		try {

			// Enable DataBase Transactions to be used by the Save process.
			// this.setUseTransactions(true);

			// Enable the creation of DataBase table indexes.
			// this.setUseTableIndexes(true);

			// Enable LazyLoading capabilities.
			// // this.useLazyLoading(true);

			// Set a custom encryption algorithm.
			// this.setEncryptionAlgorithm("AES");

			// Set a custom encryption master pass phrase.
			// this.setMasterEncryptionKey("com.mobandme.ada.examples.advanced");

			// Initialize ObjectSets instances.
			initializeObjectSets();

		} catch (Exception e) {
			// ExceptionsHelper.manage(e);
		}

	}

	private void initializeObjectSets() throws AdaFrameworkException {
		NotificacionsSet = new NotificationsObjectSet(this);
				
		NotificacionsSet.fill(SModPushNotification.DEFAULT_ORDER);
	}

}
