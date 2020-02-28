package com.sixtemia.gesbluedroid.datamanager.webservices;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.AgentsRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.CarrersRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.ColorsRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.InfraccionsRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.LlistaAbonatsRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.LlistaBlancaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.LoginRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.MarquesRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.ModelsRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.NouTerminalRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.RecuperaDataRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.TipusVehiclesRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.ZonesRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.ComprovaMatriculaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.EstablirComptadorDenunciaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.NouLogRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.NovaDenunciaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.PosicioRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.PujaFotoRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.RecuperaComptadorDenunciaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.RecuperaDenunciaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.AgentsResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.CarrersResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.ColorsResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.InfraccionsResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.LlistaAbonatsResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.LlistaBlancaResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.LoginResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.MarquesResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.ModelsResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.NouTerminalResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.TipusVehiclesResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.ZonesResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.ComprovaMatriculaResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.EstablirComptadorDenunciaResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.NouLogResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.NovaDenunciaResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.PosicioResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.PujaFotoResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.RecuperaComptadorDenunciaResponse;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.RecuperaDenunciaResponse;
import com.sixtemia.gesbluedroid.global.Constants;

import java.lang.reflect.Type;

import pt.joaocruz04.lib.SOAPManager;
import pt.joaocruz04.lib.misc.JSoapCallback;

public class DatamanagerAPI {

	//-- Dades basiques
	public static AsyncTask crida_NouTerminal(NouTerminalRequest _nouTerminalRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_NOUTERMINAL_METHOD, Constants.DADESBA_NOUTERMINAL_SOAPACTION, _nouTerminalRequest, NouTerminalResponse.class, _listener);
	}
	public static AsyncTask crida_Login(LoginRequest _loginRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_LOGIN_METHOD, Constants.DADESBA_LOGIN_SOAPACTION, _loginRequest, LoginResponse.class, _listener);
	}
	public static AsyncTask crida_TipusVehicles(TipusVehiclesRequest _tipusVehiclesRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_TIPUSVEHICLE_METHOD, Constants.DADESBA_TIPUSVEHICLES_SOAPACTION, _tipusVehiclesRequest, TipusVehiclesResponse.class, _listener);
	}
	public static AsyncTask crida_Agents(AgentsRequest _agentsRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_AGENTS_METHOD, Constants.DADESBA_AGENTS_SOAPACTION, _agentsRequest, AgentsResponse.class, _listener);
	}
	public static AsyncTask crida_Marques(MarquesRequest _marquesRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_MARQUES_METHOD, Constants.DADESBA_MARQUES_SOAPACTION, _marquesRequest, MarquesResponse.class, _listener);
	}
	public static AsyncTask crida_Models(ModelsRequest _modelsRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_MODELS_METHOD, Constants.DADESBA_MODELS_SOAPACTION, _modelsRequest, ModelsResponse.class, _listener);
	}
	public static AsyncTask crida_Colors(ColorsRequest _colorsRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_COLORS_METHOD, Constants.DADESBA_COLORS_SOAPACTION, _colorsRequest, ColorsResponse.class, _listener);
	}
	public static AsyncTask crida_Zones(ZonesRequest _zonesRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_ZONES_METHOD, Constants.DADESBA_ZONES_SOAPACTION, _zonesRequest, ZonesResponse.class, _listener);
	}
	public static AsyncTask crida_Carrers(CarrersRequest _carrersRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_CARRERS_METHOD, Constants.DADESBA_CARRERS_SOAPACTION, _carrersRequest, CarrersResponse.class, _listener);
	}
	public static AsyncTask crida_Infraccions(InfraccionsRequest _infraccionsRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_INFRACCIONS_METHOD, Constants.DADESBA_INFRACCIONS_SOAPACTION, _infraccionsRequest, InfraccionsResponse.class, _listener);
	}
	public static AsyncTask crida_LlistaBlanca(LlistaBlancaRequest _llistablancaRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_LLISTABLANCA_METHOD, Constants.DADESBA_LLISTABLANCA_SOAPACTION, _llistablancaRequest, LlistaBlancaResponse.class, _listener);
	}
    public static AsyncTask crida_LlistaAbonats(LlistaAbonatsRequest _llistaabonatsRequest, JSoapCallback _listener) {
        return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_LLISTAABONATS_METHOD, Constants.DADESBA_LLISTAABONATS_SOAPACTION, _llistaabonatsRequest, LlistaAbonatsResponse.class, _listener);
    }
	public static AsyncTask crida_RecuperaData(RecuperaDataRequest _recuperaDataRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.DADESBA_NAMESPACE, Constants.DADESBA_URL, Constants.DADESBA_RECUPERADATA_METHOD, Constants.DADESBA_RECUPERADATA_SOAPACTION, _recuperaDataRequest, String.class, _listener);
	}

	//-- Operativa
	public static AsyncTask crida_ComprovaMatricula(ComprovaMatriculaRequest _comprovaMatriculaRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.OPERATIVA_NAMESPACE, Constants.OPERATIVA_URL, Constants.OPERATIVA_COMPROVAMATRICULA_METHOD, Constants.OPERATIVA_COMPROVAMATRICULA_SOAPACTION, _comprovaMatriculaRequest, ComprovaMatriculaResponse.class, _listener);
	}
	public static AsyncTask crida_NovaDenuncia(NovaDenunciaRequest _novaDenunciaRequest, JSoapCallback _listener) {
		AsyncTask resposta=SOAPManager.get(Constants.OPERATIVA_NAMESPACE, Constants.OPERATIVA_URL, Constants.OPERATIVA_NOVADENUNCIA_METHOD, Constants.OPERATIVA_NOVADENUNCIA_SOAPACTION, _novaDenunciaRequest, NovaDenunciaResponse.class, _listener);
		return resposta;
	}
	public static AsyncTask crida_NouLog(NouLogRequest _nouLogRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.OPERATIVA_NAMESPACE, Constants.OPERATIVA_URL, Constants.OPERATIVA_NOULOG_METHOD, Constants.OPERATIVA_NOULOG_SOAPACTION, _nouLogRequest, NouLogResponse.class, _listener);
	}

	public static AsyncTask crida_PujaFoto(PujaFotoRequest _pujaFotoRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.OPERATIVA_NAMESPACE, Constants.OPERATIVA_URL, Constants.OPERATIVA_PUJAFOTO_METHOD, Constants.OPERATIVA_PUJAFOTO_SOAPACTION, _pujaFotoRequest, PujaFotoResponse.class, _listener);
	}
	public static AsyncTask crida_Posicio(PosicioRequest _posicioRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.OPERATIVA_NAMESPACE, Constants.OPERATIVA_URL, Constants.OPERATIVA_POSICIO_METHOD, Constants.OPERATIVA_POSICIO_SOAPACTION, _posicioRequest, PosicioResponse.class, _listener);
	}
	public static AsyncTask crida_RecuperaDenuncia(RecuperaDenunciaRequest _recuperaDenunciaRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.OPERATIVA_NAMESPACE, Constants.OPERATIVA_URL, Constants.OPERATIVA_RECUPERADENUNCIA_METHOD, Constants.OPERATIVA_RECUPERADENUNCIA_SOAPACTION, _recuperaDenunciaRequest, RecuperaDenunciaResponse.class, _listener);
	}
	public static AsyncTask crida_EstablirComptadorDenuncia(EstablirComptadorDenunciaRequest _establirComptadorDenunciaRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.OPERATIVA_NAMESPACE, Constants.OPERATIVA_URL, Constants.OPERATIVA_ESTABLIRCOMPTADORDENUNCIA_METHOD, Constants.OPERATIVA_ESTABLIRCOMPTADORDENUNCIA_SOAPACTION, _establirComptadorDenunciaRequest, EstablirComptadorDenunciaResponse.class, _listener);
	}
	public static AsyncTask crida_RecuperaComptadorDenuncia(RecuperaComptadorDenunciaRequest _recuperaComptadorDenunciaRequest, JSoapCallback _listener) {
		return SOAPManager.get(Constants.OPERATIVA_NAMESPACE, Constants.OPERATIVA_URL, Constants.OPERATIVA_RECUPERACOMPTADORDENUNCIA_METHOD, Constants.OPERATIVA_RECUPERACOMPTADORDENUNCIA_SOAPACTION, _recuperaComptadorDenunciaRequest, RecuperaComptadorDenunciaResponse.class, _listener);
	}

	public static <T> T parseJson(String _json, Class<T> classOfT) throws MalformedJsonException {
		Gson gson = new Gson();
		Log.e("GSON","Vaig a parsejar " + classOfT.getCanonicalName());
		T response = gson.fromJson(_json, (Type) classOfT);
		Log.e("La response datamanager",""+response);
		return response;
	}

}