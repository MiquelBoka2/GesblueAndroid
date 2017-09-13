package com.sixtemia.sdatamanager.datamanager.parameters;

import android.text.TextUtils;

public class SNameValuePair {

    private String strName;
    private String strValue;

    public SNameValuePair(String _strName, String _strValue) {

        if(!TextUtils.isEmpty(_strName)) {
            strName = _strName;
        } else {
            strName = "";
        }

        if(!TextUtils.isEmpty(_strValue)) {
            strValue = _strValue;
        } else {
            strValue = "";
        }
    }

    public String getName() {
        return strName;
    }

    public String getValue() {
        return strValue;
    }

}
