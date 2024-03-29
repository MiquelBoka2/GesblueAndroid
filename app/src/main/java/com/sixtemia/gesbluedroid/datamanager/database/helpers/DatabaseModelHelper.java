package com.sixtemia.gesbluedroid.datamanager.database.helpers;

/**
 * Created by alejandroarangua on 20/5/16.
 * S'encarrega de crear les taules, la base de dades, té el nom de la base de dades i la carpeta on es vol desar
 * S'hauria d'abstraure el tema d'on escriu la BDD.
 * Falta optimitzacións per crear DAOs proceduralment.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_AccioPosicio;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Agent;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Carrer;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Color;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Denuncia;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Infraccio;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Log;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Marca;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Model;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_PosicioAgent;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Terminal;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_TipusAnulacio;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_TipusVehicle;
import com.sixtemia.sdatamanager.datamanager.results.BasicWSResult;
import com.sixtemia.sutils.classes.SSystemUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseModelHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "gesblue.db";
    private static final String DATABASE_FOLDER = "sdatamanagerdb";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 2;

    // the DAO object we use to access the SimpleData table
    private ConcurrentHashMap<Class,RuntimeExceptionDao<? extends BasicWSResult,String>> table_map;
    private List<? extends Class> table_classes = Arrays.asList(
		    Model_AccioPosicio.class,
		    Model_Agent.class,
		    Model_Carrer.class,
		    Model_Color.class,
		    Model_Denuncia.class,
		    Model_Infraccio.class,
			Model_Log.class,
		    Model_Marca.class,
		    Model_Model.class,
		    Model_PosicioAgent.class,
		    Model_Terminal.class,
		    Model_TipusAnulacio.class,
		    Model_TipusVehicle.class);

    public DatabaseModelHelper(Context context) {
        super(context, getDataBasePath(context), null, DATABASE_VERSION);
		table_map = new ConcurrentHashMap<>();
		for(Class c : table_classes) {
			table_map.put(c,getRuntimeExceptionDao(c));
		}
    }

    private static String getDataBasePath(Context context) {
	    try {
		    if (SSystemUtils.isDebugging(context)) {
			    File dir = context.getExternalCacheDir();
			    if(dir == null) {
				    dir = context.getFilesDir();
			    }
			    String databasePath = dir.getAbsolutePath() + File.separator + "Sixtemia" + File.separator
					    + DATABASE_FOLDER
					    + File.separator + DATABASE_NAME;
			    String folderPath = dir.getAbsolutePath() + File.separator + "Sixtemia" + File.separator
					    + DATABASE_FOLDER;
			    File dbFolder = new File(folderPath);
			    if (!dbFolder.exists()) {
				    dbFolder.mkdirs();
			    }
			    return databasePath;
		    }
	    } catch (Exception ex) {
		    Log.e("DBMHelper", "Error: " + ex.getLocalizedMessage(), ex);
	    }

        return DATABASE_NAME;
    }

    // DATABASE
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseModelHelper.class.getName(), "onCreate");
            createTables(connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseModelHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		if(oldVersion<2) {try {
			createTable(connectionSource,Model_Log.class);
		} catch (SQLException e) {
			Log.e(DatabaseModelHelper.class.getName(), "Can't create table", e);
			throw new RuntimeException(e);
		}
		}
    }

    public void createTables(ConnectionSource connectionSource) throws SQLException {
		for(Class model : table_map.keySet()) {
			TableUtils.createTable(connectionSource, model);
		}
    }
	public void createTable(ConnectionSource connectionSource,Class model) throws SQLException {

			TableUtils.createTable(connectionSource, model);

	}
    public void dropTables(ConnectionSource connectionSource) throws SQLException {
		for(Class model : table_map.keySet()) {
			TableUtils.dropTable(connectionSource, model,true);
		}
    }

	public RuntimeExceptionDao<? extends BasicWSResult, String> getModelDao(Class<? extends BasicWSResult> _modelClass) {
		return table_map.get(_modelClass);
	}

}
