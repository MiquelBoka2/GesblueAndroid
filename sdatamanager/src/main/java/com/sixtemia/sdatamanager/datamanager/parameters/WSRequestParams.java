package com.sixtemia.sdatamanager.datamanager.parameters;

import com.google.gson.Gson;
import com.sixtemia.sdatamanager.datamanager.SDataManager;
import com.sixtemia.sdatamanager.datamanager.listeners.SDataManagerListener;

import java.util.ArrayList;

public class WSRequestParams {

    private ArrayList<SNameValuePair> params;   //Paràmetres que es necessiten passar al webservice
    private String urlBase;                     //Url base a on es farà la petició del webservice
    private String urlWS;                       //Url que es concatena a la base per cridar un webservice concret
    private SDataManager.HttpMethod httpMethod; //GET, POST, POST_MULTIPART; Per default és POST
    private SDataManagerListener listener;      //Listener que ens retorna el resultat de l'asynctask.  (Visitor Pattern)
    private Gson gson;                          //Permet crear un Gson custom, per poder, per exemple, formatar els objectes Date d'una crida concreta.
    private String BasicWSClassName;            //Nom de la classe en la que es preveu mapejar la resposta

    /**
     * @param _params Array list de paràmetres que volem enviar amb la petició
     * @param _urlBase  URL base a on es farà la petició
     * @param _urlWS URL que es concatenarà a la _urlBase que identifica el webservice concret on volem fer la petició
     * @param _listener Listener que permet saber quan i com ha acabat la petició
     */
    public WSRequestParams(ArrayList<SNameValuePair> _params, String _urlBase, String _urlWS, String _basicWSClass, SDataManagerListener _listener) {
        setParams(_params);
        setUrlBase(_urlBase);
        setUrlWS(_urlWS);
        setListener(_listener);
        setBasicWSClass(_basicWSClass);
        setHttpMethod(SDataManager.HttpMethod.POST);
        setGson(null);
    }

    public WSRequestParams(ArrayList<SNameValuePair> _params, String _urlBase, String _urlWS, String _basicWSClass, SDataManager.HttpMethod _httpMethod, Gson _gson, SDataManagerListener _listener) {
        this(_params, _urlBase, _urlWS, _basicWSClass, _listener);
        setHttpMethod(_httpMethod);
        setGson(_gson);
    }

    public ArrayList<SNameValuePair> getParams() {
        return params;
    }

    public void setParams(ArrayList<SNameValuePair> params) {
        this.params = params;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public String getUrlWS() {
        return urlWS;
    }

    public void setUrlWS(String urlWS) {
        this.urlWS = urlWS;
    }

    public SDataManagerListener getListener() {
        return listener;
    }

    public void setListener(SDataManagerListener listener) {
        this.listener = listener;
    }

    public String getBasicWSClass() {
        return BasicWSClassName;
    }

    public void setBasicWSClass(String _basicWSClass) {
        BasicWSClassName = _basicWSClass;
    }

    public SDataManager.HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(SDataManager.HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
