package com.sixtemia.gesbluedroid.datamanager.database.results;

import android.util.Log;

import com.sixtemia.sdatamanager.datamanager.results.BasicWSResult;

/**
 * Created by alejandroarangua on 25/5/16.
 */
public class BasicDBResult {
    public static final String TAG = "BasicDBResult";
    public static final String KO = "KO";
    public static final String OK = "OK";
    public static final String error_timeout = "timeout";

    public BasicWSResult resultObject;

    private String strResult;
    private String strMsg;

    public BasicDBResult() {
        setStrMsg("");
        setStrResult(OK);
        resultObject = new BasicWSResult();
    }

    public BasicDBResult(String strResult, String strMsg) {
        this.strResult = strResult;
        this.strMsg = strMsg;
    }

    public String getStrResult() {
        return strResult;
    }

    public void setStrResult(String strResult) {
        this.strResult = strResult;
    }

    public String getStrMsg() {
        return strMsg;
    }

    public void setStrMsg(String strMsg) {
        this.strMsg = strMsg;
    }

    public BasicWSResult getResultObject() {
        return resultObject;
    }

    public <T> T getResult() {
        try {
            return (T) getResultObject();
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
            return null;
        }
    }

    public <T> T getFirst() {
        try {
            SimpleResult sr = getResult();
            return (T) sr.getItems().get(0);
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
            return null;
        }
    }

    public <T> T getArray() {
        try {
            SimpleResult sr = getResult();
            return (T) sr.getItems();
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
            return null;
        }
    }

    public void setResultObject(BasicWSResult _resultObject) {
        this.resultObject = _resultObject;
    }

    /**
     * Concatena dos resultats. Si un dels dos es KO el KO preval.
     * @param _result
     * @return
     */
    public BasicDBResult concat(BasicDBResult _result) {
        strResult = (_result.equals(OK) && strResult.equals(OK)) ? OK : KO;
        if(strMsg.isEmpty()) {
            strMsg = _result.getStrMsg();
        }
        return this;
    }

    public boolean isOk() {
        return OK.equals(getStrResult());
    }

    @Override
    public String toString() {
        return "BasicDBResult{" +
                "resultObject=" + resultObject +
                ", strResult='" + strResult + '\'' +
                ", strMsg='" + strMsg + '\'' +
                '}';
    }
}