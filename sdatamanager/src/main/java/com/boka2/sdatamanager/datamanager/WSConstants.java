package com.boka2.sdatamanager.datamanager;

/**
 * Created by Boka2.
 */
public class WSConstants {

    // ==================================
    // URLs i Constants de WS
    // ==================================
    public static final String WS_VERSION = "1.0";

    // =========================================
    // CONSTANTS D'ERRORS INTERNS (DEFAULT QUAN NO ESTÀ DEFINIT VOL DIR QUE STRMSG DE BASICWSRESULT ARRIBA DE WS, ÉS 0 PER CONVENCIÓ)
    // =========================================
    public static final int ERROR_JSON_PARSING = 1;         //Aquest cas l'error pot ser tant nostre (per parsejar) com del WS per estar tornant realment una resposta buida.
    public static final int ERROR_NO_CONNECTION = 2;
    public static final int ERROR_HANDLED_EXCEPTION = 3;

    // ==================================================================================
    // GENERAL WS PARAMETERS
    // ==================================================================================
    public static final String WS_PARM_LANG = "lang";
    public static final String WS_PARM_VER = "ver";
}

//For fast copy paste:
//public static final String WS_ = "";
