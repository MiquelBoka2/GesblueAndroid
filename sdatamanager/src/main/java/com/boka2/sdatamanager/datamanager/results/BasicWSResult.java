package com.boka2.sdatamanager.datamanager.results;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.boka2.sdatamanager.datamanager.DataManager;

/**
 * Created by Boka2.
 */
public class BasicWSResult {

    public final static String OK = "OK";
    public final static String KO = "KO";

    @SerializedName("result")
    protected String strResult;

    @SerializedName("strMsg")
    protected String strMsg;

    //Serveix per diferenciar d'on procedeix el missatge d'error, si de local, o de remot, i quin és l'error segons el valor que tingui.
    //Si ve de remot val 0, altrament adopta valors enters positius.
    protected int intSource;

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
    public int getIntSource() {
        return intSource;
    }
    public void setIntSource(int intSource) {
        this.intSource = intSource;
    }

    public BasicWSResult() {
        setStrResult(DataManager.OK);
        setStrMsg("");
        intSource = 0;
    }

    public BasicWSResult(String _strResult) {
        this();
        setStrResult(_strResult);
    }

    public BasicWSResult(String _strMsg, String _strResult) {
        this(_strResult);
        setStrMsg(_strMsg);
    }

    public boolean isOk() {
        return !TextUtils.isEmpty(strResult) && strResult.equalsIgnoreCase(DataManager.OK);
    }

    /**
     * @return True if the message is from remote. False if is a local error (for example no connection).
     */
    public boolean isWSMsg() {
        return intSource < 1;
    }

}