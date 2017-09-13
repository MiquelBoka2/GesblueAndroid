package com.sixtemia.gesbluedroid.datamanager.database.helpers;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.support.ConnectionSource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by alejandroarangua on 25/5/16.
 * La funcionalitat d'aquesta classe és d'encapsular les connexions al a bdd de forma concurrent,
 * i els cops que s'obre i es tanca de forma centralitzada i única.
 * Aquesta classe NO exclueix que l'usuari hagi d'obrir i tancar el helper cada cop que es fa una
 * transacció. Aquesta responsabilitat es delegarà a l'asynctask que faci servir aquesta classe.
 */
public class GeneralDBHelper {

    //Te control threadsafe del recompte de processos que executen tasques amb el helper obert.
    //Si es posa a false el control s'ha de fer manual. Es a dir, no es comptabilitza, i només
    //es tanca la instància quan realment no hi ha ningú fent-la servir.
    private static final boolean automatic = true;
    private static final boolean debug = true;

    private static AtomicInteger mOpenCounter = new AtomicInteger();
    private static GeneralDBHelper _inst;
    private DatabaseModelHelper db_helper;
    private ConnectionSource connection;

    /**
     * @param _context Hauria de ser el context de l'App, perquè la instància del Helper estarà oberta per a totes les transaccións
     *                 durant totes les pantalles de l'App.
     *                 COMPTE! NO FER SERVIR!! AQUEST CONSTRUCTOR HAURIA DE SER PRIVAT, PERÒ EL ORMLITE PETA SI NO ÉS PUBLIC. LA IDEA ÉS
     *                 QUE S'OBTINGUIN INSTÀNCIES VIA EL getDBHelperInstance en comptes de amb el constructor.
     */
    public GeneralDBHelper(Context _context) {}

    private static synchronized GeneralDBHelper getDBHelperInstance(Context _context) {
        if(_inst==null) {
            _inst = new GeneralDBHelper(_context);
            _inst.db_helper = OpenHelperManager.getHelper(_context, DatabaseModelHelper.class);
        }
        return _inst;
    }

    public static synchronized ConnectionSource getConnectionSource(Context _context) {
        if(automatic) {
            if (mOpenCounter.incrementAndGet() == 1) {
                if(debug) {Log.d("DB COUNT Open=", String.valueOf(mOpenCounter.get()));}
                GeneralDBHelper.getDBHelperInstance(_context).setConnection(
                        GeneralDBHelper.getDBHelperInstance(_context).getDb_helper().getConnectionSource()
                );
            }
        }
        return GeneralDBHelper.getDBHelperInstance(_context).getConnection();
    }

    public static DatabaseModelHelper getDb_helper(Context _context) {
        return GeneralDBHelper.getDBHelperInstance(_context).getDb_helper();
    }

    public DatabaseModelHelper getDb_helper() {
        return db_helper;
    }
    /**
     * S'hauria de cridar només quan es tanca l'app, o s'està segur de que no es tornaran a fer crides a la BDD.
     * Idelament en el onDestroy de l'activity.
     * Després de molta recerca
     */
    public static synchronized void closeGeneralDBHelper() {
        if(debug) {Log.d("DB COUNT Close=", String.valueOf(mOpenCounter.get()));}
        if(automatic) {
            if (mOpenCounter.decrementAndGet() == 0) {
                if(debug) {Log.d("DB COUNT Closed!=", String.valueOf(mOpenCounter.get()));}
//                OpenHelperManager.releaseHelper();
                _inst = null;
            }
        } else {
//            OpenHelperManager.releaseHelper();
        }
    }

    private ConnectionSource getConnection() {
        return connection;
    }

    private void setConnection(ConnectionSource connection) {
        this.connection = connection;
    }
}
