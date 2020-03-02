package com.boka2.spushnotificationsdroid.db;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.boka2.spushnotificationsdroid.model.SModPushNotification;
import com.boka2.sutils.classes.SSystemUtils;

/*
 * CLASSE NECESSARIA PER UTILITZAR ADA DATAFRAMEWORK (framework base de dades)
 */

public class DataContext extends ObjectContext {

	final static String DATABASE_FOLDER = "%s/Boka2/";
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
		super(pContext, String.format("%s%s", getDataBaseFolder(), DATABASE_NAME), DATABASE_VERSION);
		// super(pContext, DATABASE_NAME, DATABASE_VERSION);

		// Initialize the ObjectContext
		initializeContext();
	}

	private static String getDataBaseFolder() {
		String folderPath = "";
		if (SSystemUtils.isDebugging(mContext)) {
			try {

				folderPath = String.format(DATABASE_FOLDER, Environment.getExternalStorageDirectory().getAbsolutePath());

				File dbFolder = new File(folderPath);
				if (!dbFolder.exists()) {
					dbFolder.mkdirs();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return folderPath;
	}

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
