package com.boka2.gesblue.global;

import android.content.Context;
import android.os.Bundle;

public class Firebase_Constants {

    public static final String MODO_OFFLINE="offline_mode";
    public static final String DEVICE_UUID = "device_UUID";
    public static final String CONCESSIO = "concessio";

    //<editor-fold desc="EVENTS LOGIN">

    public static final String USERNAME = "username";
    public static final String LAST_USARNAME = "last_username";


    public static final String E_OBERTURA_NOU_TERMINAL = "obertura_nou_terminal";
    public static final String E_OBERTURA_NOU_LOGIN = "obertura_nou_login";

    public static final String E_REGISTRE_NOU_TERMINAL = "registre_nou_terminal";
    public static final String E_REGISTRE_LOGIN = "registre_nou_login";
    //</editor-fold>


    //<editor-fold desc="EVENTS MAIN">
    public static final String USER_ID = "user_id";


    public static final String E_COMPRVACIO = "comprovacio";


    public static void BASIC_INFO(Bundle bundle, Context mContext){
        bundle.putString(DEVICE_UUID, PreferencesGesblue.getPrefEternUUID(mContext));
        bundle.putBoolean(MODO_OFFLINE, PreferencesGesblue.getOffline(mContext));
    }
}
