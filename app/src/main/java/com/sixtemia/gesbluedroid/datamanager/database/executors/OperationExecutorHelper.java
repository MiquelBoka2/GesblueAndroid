package com.sixtemia.gesbluedroid.datamanager.database.executors;

import android.content.Context;

import com.sixtemia.gesbluedroid.datamanager.database.helpers.DatabaseModelHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.GeneralDBHelper;
import com.sixtemia.gesbluedroid.datamanager.database.results.BasicDBResult;

import java.util.concurrent.Callable;

/**
 * Created by alejandroarangua on 26/5/16.
 */
public abstract class OperationExecutorHelper implements Callable<BasicDBResult> {

    protected DatabaseModelHelper DBhelper;

    public OperationExecutorHelper(Context _context) {
        DBhelper = GeneralDBHelper.getDb_helper(_context);
    }

    @Override
    public BasicDBResult call() throws Exception { return processingMethod();}

    /**
     * S'ha de implementar aquest mètode per cada tipus d'objecte que vulgui interactuar amb la BDD
     * Per anar bé i ser consistent, mai hauria de retornar null. Així ens estalviem de mirar tota la
     * estona si és o no null.
     **/
    public abstract BasicDBResult processingMethod();

}
