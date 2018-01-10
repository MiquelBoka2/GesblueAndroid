package com.sixtemia.gesbluedroid.global;

import com.sixtemia.gesbluedroid.datamanager.webservices.WSConstantsGB;

/**
 * Created by joelabello on 12/8/16.
 */
public class Constants {

    public static final String GENERAL_LOG_TAG = "GesblueDroid";
    public static final String LOG_TAG = "GesblueDroid";
    public static final String LOG_TAG_ERR = ".GesblueDroidError";

    // ==================================
    // TOKEN AND OTHERS
    // ==================================
    //public static final String APP_TOKEN = "tempAppToken!";

    // ==================================
    // FLURRY
    // ==================================
    public static final String FLURRY_API_KEY = "YFHGJHY3B5YNMMK58Z3N";
    public static final String FLURRY_API_KEY_DEBUG = "TO DO";

    // ==================================
    // MAP
    // ==================================
    public static final int COMFORTABLE_ZOOM_LEVEL = 14;

    // ==================================
    // DATAMANAGER
    // ==================================
    public static final String DADESBA_URL = "http://www.gesblue.com/app/v2/dadesbasiques.php";
    public static final String DADESBA_NAMESPACE = WSConstantsGB.NAMESPACE_DADES_BASIQUES;


    public static final String OPERATIVA_URL = "http://www.gesblue.com/app/v2/operativa.php";
    public static final String OPERATIVA_NAMESPACE = WSConstantsGB.NAMESPACE_OPERATIVA;

    public static final String HASH = "#";

    //-- METHODS
        //- Dades basiques
        public static final String DADESBA_NOUTERMINAL_METHOD = "NouTerminalRequest";
        public static final String DADESBA_LOGIN_METHOD = "LoginRequest";
        public static final String DADESBA_TIPUSVEHICLE_METHOD = "TipusVehiclesRequest";
        public static final String DADESBA_MARQUES_METHOD = "MarquesRequest";
        public static final String DADESBA_MODELS_METHOD = "ModelsRequest";
        public static final String DADESBA_COLORS_METHOD = "ColorsRequest";
        public static final String DADESBA_RECUPERADATA_METHOD = "RecuperaDataRequest";
        public static final String DADESBA_INFRACCIONS_METHOD = "InfraccionsRequest";
        public static final String DADESBA_CARRERS_METHOD = "CarrersRequest";
        //- Operativa
        public static final String OPERATIVA_COMPROVAMATRICULA_METHOD = "ComprovaMatricula";
        public static final String OPERATIVA_NOVADENUNCIA_METHOD = "NovaDenuncia";
        public static final String OPERATIVA_NOULOG_METHOD = "NouLog";
        public static final String OPERATIVA_PUJAFOTO_METHOD = "PujaFoto";
        public static final String OPERATIVA_POSICIO_METHOD = "Posicio";
        public static final String OPERATIVA_RECUPERADENUNCIA_METHOD = "RecuperaDenuncia";
        public static final String OPERATIVA_ESTABLIRCOMPTADORDENUNCIA_METHOD = "EstablirComptadorDenuncia";
        public static final String OPERATIVA_RECUPERACOMPTADORDENUNCIA_METHOD = "RecuperaComptadorDenuncia";

    //--SOAP ACTIONS
        //- Dades basiques
        public static final String DADESBA_NOUTERMINAL_SOAPACTION = DADESBA_NAMESPACE + HASH + WSConstantsGB.REQUEST_NOUTERMINAL;
        public static final String DADESBA_LOGIN_SOAPACTION = DADESBA_NAMESPACE + HASH + WSConstantsGB.REQUEST_LOGIN;
        public static final String DADESBA_TIPUSVEHICLES_SOAPACTION = DADESBA_NAMESPACE + HASH + WSConstantsGB.REQUEST_TIPUSVEHICLES;
        public static final String DADESBA_MARQUES_SOAPACTION = DADESBA_NAMESPACE + HASH + WSConstantsGB.REQUEST_MARQUES;
        public static final String DADESBA_MODELS_SOAPACTION = DADESBA_NAMESPACE + HASH + WSConstantsGB.REQUEST_MODELS;
        public static final String DADESBA_COLORS_SOAPACTION = DADESBA_NAMESPACE + HASH + WSConstantsGB.REQUEST_COLORS;
        public static final String DADESBA_RECUPERADATA_SOAPACTION = DADESBA_NAMESPACE + HASH + WSConstantsGB.REQUEST_RECUPERADATA;
        public static final String DADESBA_INFRACCIONS_SOAPACTION = DADESBA_NAMESPACE + HASH + WSConstantsGB.REQUEST_INFRACCIONS;
        public static final String DADESBA_CARRERS_SOAPACTION = DADESBA_NAMESPACE + HASH + WSConstantsGB.REQUEST_CARRERS;
        //- Operativa
        public static final String OPERATIVA_COMPROVAMATRICULA_SOAPACTION = OPERATIVA_NAMESPACE + HASH + WSConstantsGB.REQUEST_COMPROVAMATRICULA;
        public static final String OPERATIVA_NOVADENUNCIA_SOAPACTION = OPERATIVA_NAMESPACE + HASH + WSConstantsGB.REQUEST_NOVADENUNCIA;
        public static final String OPERATIVA_NOULOG_SOAPACTION = OPERATIVA_NAMESPACE + HASH + WSConstantsGB.REQUEST_NOULOG;

    public static final String OPERATIVA_PUJAFOTO_SOAPACTION = OPERATIVA_NAMESPACE + HASH + WSConstantsGB.REQUEST_PUJAFOTO;
        public static final String OPERATIVA_POSICIO_SOAPACTION = OPERATIVA_NAMESPACE + HASH + WSConstantsGB.REQUEST_POSICIO;
        public static final String OPERATIVA_RECUPERADENUNCIA_SOAPACTION = OPERATIVA_NAMESPACE + HASH + WSConstantsGB.REQUEST_RECUPERADENUNCIA;
        public static final String OPERATIVA_ESTABLIRCOMPTADORDENUNCIA_SOAPACTION = OPERATIVA_NAMESPACE + HASH + WSConstantsGB.REQUEST_ESTABLIRCOMPTADORDENUNCIA;
        public static final String OPERATIVA_RECUPERACOMPTADORDENUNCIA_SOAPACTION = OPERATIVA_NAMESPACE + HASH + WSConstantsGB.REQUEST_RECUPERACOMPTADORDENUNCIA;

    public static final String LANG_ES = "ES";
    public static final String LANG_CA = "CA";
    public static final String LANG_FR = "FR";
    public static final String LANG_EN = "EN";
}