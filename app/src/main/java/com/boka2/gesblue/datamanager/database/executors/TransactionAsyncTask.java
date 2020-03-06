package com.boka2.gesblue.datamanager.database.executors;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.j256.ormlite.misc.TransactionManager;
import com.boka2.gesblue.datamanager.database.helpers.GeneralDBHelper;
import com.boka2.gesblue.datamanager.database.listeners.SDataBaseListener;
import com.boka2.gesblue.datamanager.database.results.BasicDBResult;
import com.boka2.sdatamanager.common.Constants;

import java.util.concurrent.ExecutorService;

/*
 * Created by Boka2.
 * La responsabilitat d'aquesta classe és d'executar la transacció de la base de dades, de gestionar
 * es possibles errors de la transacció i d'obrir i tancar el helper de manera centralitzada, única
 * i consistent.
 */
public class TransactionAsyncTask extends AsyncTask<OperationExecutorHelper, Void, BasicDBResult> {

    protected SDataBaseListener listener;   //Permet saber quan i com acabat el Asynctask
    protected Context context;              //Com s'han de fer operacions de la BDD es necessita el context
    protected BasicDBResult res;            //Conté el resultat de la crida. Es retorna al final de l'asynctask i al listener.
    /*
     * Creates an AsyncTask but doesn't execute it. You must call start after this constructor to execute it.
     * @param _listener
     * @param _context
     */
    public TransactionAsyncTask(SDataBaseListener _listener, Context _context) {
        listener = _listener;
        context = _context;
    }

    /*
     * Creates an AsynTask and executes it.
     * @param _listener
     * @param _config
     * @param _context
     */
    public TransactionAsyncTask(SDataBaseListener _listener, ExecutorService _executor, OperationExecutorHelper _config, Context _context) {
        listener = _listener;
        context = _context;
        start(_config, _executor);
    }

    public void start(OperationExecutorHelper _config, ExecutorService _executor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if(_executor==null) {
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, _config);
            } else {
                executeOnExecutor(_executor, _config);
            }
        } else {
            execute(_config);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        res = new BasicDBResult();
        if(listener!=null)listener.onPreExecute();
    }

    @Override
    protected BasicDBResult doInBackground(OperationExecutorHelper... params) {
        if(params.length>0) {
            final OperationExecutorHelper operationCaller = params[0];
            try {
                //Com el Helper es un singleton cada cop que es fa el getConnectionSource es crea una instància si és necessari
                //I es crea una nova connexió amb la base de dades. El caller té la informació que un vol que interactui amb la BDD
                res = TransactionManager.callInTransaction(GeneralDBHelper.getConnectionSource(context),operationCaller);
            }catch (Exception e) {
                res.setStrResult(BasicDBResult.KO);
                res.setStrMsg(e.getMessage());
                Log.d(Constants.LOG_TAG, res.getStrResult());
            }finally {
                //Tanca el helper.
                GeneralDBHelper.closeGeneralDBHelper();
            }
        } else {
            res.setStrResult(BasicDBResult.KO);
            res.setStrMsg("Missing OperationExecutorHelper into TransactionAsyncTask or ModelHelper!");
            Log.d(Constants.LOG_TAG, res.getStrResult());
        }
        return res;
    }

    @Override
    protected void onPostExecute(BasicDBResult params) {
        super.onPostExecute(params);
        if (!isCancelled()) {
            if (params!=null && params.getStrResult().equals(BasicDBResult.KO)) {
                error(params, listener);
                return;
            }
            completion(params, listener);
        }
    }

    public BasicDBResult getResult() {
        return res;
    }

    protected void completion(BasicDBResult result, SDataBaseListener listener) {
        if (listener!=null && listener instanceof SDataBaseListener) {
            listener.onCompletion(result);
        }
    }

    protected void error(BasicDBResult result, SDataBaseListener listener) {
        if (listener != null && listener instanceof SDataBaseListener) {
            listener.onError(result);
        }
    }

}
