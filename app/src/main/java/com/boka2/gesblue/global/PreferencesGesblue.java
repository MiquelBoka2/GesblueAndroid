package com.boka2.gesblue.global;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.boka2.sbaseobjects.tools.Preferences;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * Created by Boka2.
 */
public class PreferencesGesblue extends Preferences {

	private static final String BUIT = "";
	private static final int ZERO = 0;

	private static final String PREF_USER_LOGGED_ID = "userLoggedId";
	private static final String PREF_USER_TOKEN = "userToken";
	private static final String PREF_FIRST_EXECUTION = "first_execution";
	private static final String PREF_USER_NAME = "user_name";
	private static final String PREF_PASSWORD = "password";
	private static final String PREF_SDID = "sdid";
	private static final String PREF_ENABLED = "enabled";
	private static final String PREF_LOCALE = "locale";
	private static final String PREF_CONCESSIO = "concessio";
	private static final String PREF_UUID = "uuid";
	private static final String PREF_AGENT_ID = "agentId";
	private static final String PREF_CODI_AGENT = "codiAgent";
	private static final String PREF_TERMINAL = "terminal";
	private static final String PREF_SERVER_TIME = "serverTime";
	private static final String PREF_INCREMENTAL_TIME = "serverTime";
	private static final String PREF_CONCESSIO_STRING = "concessioString";
	private static final String PREF_COMPTADOR_DENUNCIA = "comptadorDenuncia";
	private static final String PREF_CONTROL = "digitControl";
	private static final String PREF_CODIBARRES_VISIBLE = "codiBarresVisible";
	private static final String PREF_TEXTPEU_VISIBLE = "textPeuVisible";
	private static final String PREF_TEXTPEU = "textPeu";
	private static final String PREF_IMPORTANULACIO = "importAnulacio";
	private static final String PREF_LOGOSMARQUES = "logosMarques";
	private static final String PREF_LOGOQR = "logoQr";
	private static final String PREF_ADRECAQR = "AdrecaQr";
	private static final String PREF_ICONESGENERICS = "iconesGenerics";
	private static final String PREF_IMPRESORA = "impresora";
	private static final String PREF_CODIBARRES_SERVICAIXA = "codiBarresServicaixa";
	private static final String PREF_VALORS_SERVICAIXA = "valorsServicaixa";
	private static final String PREF_TEXTANULACIO = "textAnulacio";
	private static final String PREF_CONTINGUT_TEXTANULACIO = "contingutTextAnulacio";
	private static final String PREF_COSTLIMITANULACIO = "costLimitAnulacio";
	private static final String PREF_DATAIMPORT_TIQUET = "dataImportTiquet";
	private static final String PREF_PRECEPTE_INFRINGIT = "precepteInfringit";
	private static final String PREF_LONGITUD_INFRACCIO = "longitudInfraccio";
	private static final String PREF_TIQUET_USUARI = "tiquetUsuari";
	private static final String PREF_LOGO = "logoTiquet";
	private static final String PREF_IMATGE_PEU = "imatgePeu";
	private static final String PREF_ADDRESS = "addresBluetoothPrinter";
	private static final String PREF_TEXTCAP = "textCap";
	private static final String PREF_EMISORA = "emisora";
	private static final String PREF_MOD = "mod";
	private static final String PREF_REFERENCIA = "referencia";
	private static final String PREF_IDENTIFICACIO = "identificacio";
	private static final String PREF_IMPDTE = "impdte";
	private static final String PREF_TIPUSVEHICLE = "tipus";
	private static final String PREF_MARCA = "marca";
	private static final String PREF_MODEL = "model";
	private static final String PREF_COLOR = "color";
	private static final String PREF_INFRACCIO = "infraccio";
	private static final String PREF_ZONA = "zona";
	private static final String PREF_CARRER = "carrer";
	private static final String PREF_NUMERO = "numero";
	private static final String PREF_FOTO1 = "foto1";
	private static final String PREF_FOTO2 = "foto2";
	private static final String PREF_FOTO3 = "foto3";
	private static final String PREF_FOTO4 = "foto4";

	private static final String PREF_TIPUSVEHICLE_DEFAULTVALUE = "tipusVehicleDefaultValue";
	private static final String PREF_MARCA_DEFAULTVALUE = "MarcaDefaultValue";
	private static final String PREF_MODEL_DEFAULTVALUE = "ModelDefaultValue";
	private static final String PREF_COLOR_DEFAULTVALUE = "ColorDefaultValue";
	private static final String PREF_INFRACCIO_DEFAULTVALUE = "InfraccioDefaultValue";
	private static final String PREF_ZONA_DEFAULTVALUE = "zonaDefaultValue";
	private static final String PREF_CARRER_DEFAULTVALUE = "carrerDefaultValue";
	private static final String PREF_ID_AGENT = "idAgent";
	private static final String PREF_DATA_SYNC = "dataSync";
	private static final String PREF_CODI_EXPORTADORA = "codiexportadora";
	private static final String PREF_CODI_TIPUS_BUTLLETA = "coditipusbutlleta";
	private static final String PREF_CODI_INSTITUCIO = "codiinstitucio";
	private static final String PREF_CODI_CONTROL = "codicontrol";


	private static final String PREF_CODI_CARRER = "codiCarrer";
	private static final String PREF_NOM_CARRER = "nomCarrer";
	private static final String PREF_CODI_ZONA = "codiZona";
	private static final String PREF_NOM_ZONA = "nomZona";
	private static final String PREF_FLASH = "flash";


	private static final String ESTAT_COMPROVACIO = "estatComprovacio";




	public static void saveEstatComprovacio(Context _context, int i) {


		put(_context, ESTAT_COMPROVACIO, i);
	}


	public static int getEstatComprovacio(Context _context) {

		return getInt(_context, ESTAT_COMPROVACIO, 0);
	}

	public static void savePrefCodiExportadora(Context _context, int i) {
		put(_context, PREF_CODI_EXPORTADORA, i);
	}

	public static int getPrefCodiExportadora(Context _context) {

		return getInt(_context, PREF_CODI_EXPORTADORA, 0);
	}

	public static void savePrefCodiTipusButlleta(Context _context, String s) {
		put(_context, PREF_CODI_TIPUS_BUTLLETA, s);
	}

	public static String getPrefCodiTipusButlleta(Context _context) {

		return getString(_context, PREF_CODI_TIPUS_BUTLLETA, BUIT);
	}

	public static void savPrefCodiInstitucio(Context _context, String s) {
		put(_context, PREF_CODI_INSTITUCIO, s);
	}

	public static String getPrefCodiInstitucio(Context _context) {

		return getString(_context, PREF_CODI_INSTITUCIO, BUIT);
	}

	public static void saveDataSync(Context _context, String s) {
		put(_context, PREF_DATA_SYNC, s);
	}

	public static String getDataSync(Context _context) {
		return getString(_context, PREF_DATA_SYNC, "0");
	}

	public static void saveCodiBarresVisible(Context _context, boolean b) {
		put(_context, PREF_CODIBARRES_VISIBLE, b);
	}

	public static boolean getCodiBarresVisible(Context _context) {
		return getBool(_context, PREF_CODIBARRES_VISIBLE, false);
	}

	public static void saveTextPeuVisible(Context _context, boolean b) {
		put(_context, PREF_TEXTPEU_VISIBLE, b);
	}

	public static boolean getTextPeuVisible(Context _context) {
		return getBool(_context, PREF_TEXTPEU_VISIBLE, false);
	}

	public static void saveTextPeu(Context _context, String s) {
		put(_context, PREF_TEXTPEU, s);
	}

	public static String getTextPeu(Context _context) {
		return getString(_context, PREF_TEXTPEU, BUIT);
	}

	public static void saveImportAnulacio(Context _context, boolean b) {
		put(_context, PREF_IMPORTANULACIO, b);
	}

	public static boolean getImportAnulacio(Context _context) {
		return getBool(_context, PREF_IMPORTANULACIO, false);
	}

	public static void saveLogosMarques(Context _context, boolean b) {
		put(_context, PREF_LOGOSMARQUES, b);
	}

	public static boolean getLogosMarques(Context _context) {
		return getBool(_context, PREF_LOGOSMARQUES, false);
	}

	public static void saveLogoQr(Context _context, boolean b) {
		put(_context, PREF_LOGOQR, b);
	}

	public static boolean getLogoQr(Context _context) {
		return getBool(_context, PREF_LOGOQR, false);
	}

	public static void saveAdrecaQr(Context _context, String b) {
		put(_context, PREF_ADRECAQR, b);
	}

	public static String getAdrecaQr(Context _context) {
		return getString(_context, PREF_ADRECAQR, BUIT);
	}

	public static void saveTextCap(Context _context, String b) {
		put(_context, PREF_TEXTCAP, b);
	}

	public static String getTextCap(Context _context) {
		return getString(_context, PREF_TEXTCAP, BUIT);
	}

	public static void saveAdressBluetoothPrinter(Context _context, String b) {
		put(_context, PREF_ADDRESS, b);
	}

	public static String getAddressBluetoothPrinter(Context _context) {
		return getString(_context, PREF_ADDRESS, BUIT);
	}

	public static void saveIconesGenerics(Context _context, boolean b) {
		put(_context, PREF_ICONESGENERICS, b);
	}

	public static boolean getIconesGenerics(Context _context) {
		return getBool(_context, PREF_ICONESGENERICS, false);
	}

	public static void saveImpresora(Context _context, boolean b) {
		put(_context, PREF_IMPRESORA, b);
	}

	public static boolean getImpresora(Context _context) {
		return getBool(_context, PREF_IMPRESORA, false);
	}

	public static void saveCodiBarresServiCaixa(Context _context, boolean b) {
		put(_context, PREF_CODIBARRES_SERVICAIXA, b);
	}

	public static boolean getCodiBarresServiCaixa(Context _context) {
		return getBool(_context, PREF_CODIBARRES_SERVICAIXA, false);
	}

	public static void saveLogo(Context _context, String b) {
		put(_context, PREF_LOGO, b);
	}

	public static String getLogo(Context _context) {
		return getString(_context, PREF_LOGO, BUIT);
	}

	public static void saveImatgePeu(Context _context, String b) {
		put(_context, PREF_IMATGE_PEU, b);
	}

	public static String getImatgePeu(Context _context) {
		return getString(_context, PREF_IMATGE_PEU, BUIT);
	}

	public static void saveValorsServiCaixa(Context _context, String b) {
		put(_context, PREF_VALORS_SERVICAIXA, b);
	}

	public static String getValorsServiCaixa(Context _context) {
		return getString(_context, PREF_VALORS_SERVICAIXA, BUIT);
	}

	public static void saveTextAnulacio(Context _context, boolean b) {
		put(_context, PREF_TEXTANULACIO, b);
	}

	public static boolean getTextAnulacio(Context _context) {
		return getBool(_context, PREF_TEXTANULACIO, false);
	}

	public static void saveContingutTextAnulacio(Context _context, String b) {
		put(_context, PREF_CONTINGUT_TEXTANULACIO, b);
	}

	public static String getContingutTextAnulacio(Context _context) {
		return getString(_context, PREF_CONTINGUT_TEXTANULACIO, BUIT);
	}

	public static void saveCostLimitAnulacio(Context _context, boolean b) {
		put(_context, PREF_COSTLIMITANULACIO, b);
	}

	public static boolean getCostLimitAnulacio(Context _context) {
		return getBool(_context, PREF_COSTLIMITANULACIO, false);
	}

	public static void saveDataImportTiquet(Context _context, boolean b) {
		put(_context, PREF_DATAIMPORT_TIQUET, b);
	}

	public static boolean getDataImportTiquet(Context _context) {
		return getBool(_context, PREF_DATAIMPORT_TIQUET, false);
	}

	public static void savePrecepteInfringit(Context _context, String b) {
		put(_context, PREF_PRECEPTE_INFRINGIT, b);
	}

	public static String getPrecepteInfringit(Context _context) {
		return getString(_context, PREF_PRECEPTE_INFRINGIT, BUIT);
	}

	public static void saveLongitudInfraccio(Context _context, long b) {
		put(_context, PREF_LONGITUD_INFRACCIO, b);
	}

	public static long getLongitudInfraccio(Context _context) {
		return getLong(_context, PREF_LONGITUD_INFRACCIO, ZERO);
	}

	public static void saveTiquetUsuari(Context _context, boolean b) {
		put(_context, PREF_TIQUET_USUARI, b);
	}

	public static boolean getTiquetUsuari(Context _context) {
		return getBool(_context, PREF_TIQUET_USUARI, false);
	}

	public static void saveComptadorDenuncia(Context _context, int _comptadorDenuncia) {
		put(_context, PREF_COMPTADOR_DENUNCIA, _comptadorDenuncia);
	}

	public static int getComptadorDenuncia(Context _context) {
		int comptador = getInt(_context, PREF_COMPTADOR_DENUNCIA, 1);
		int comptadorFinal = comptador;
		//put(_context, PREF_COMPTADOR_DENUNCIA, comptadorFinal);
		return comptadorFinal;
	}

	public static void saveIdAgent(Context _context, long _idAgent) {
		put(_context, PREF_ID_AGENT, _idAgent);
	}

	public static long getIdAgent(Context _context) {
		return getLong(_context, PREF_ID_AGENT, ZERO);
	}

	public static void logout(Context _context) {
		logout(_context, false);
	}

	public static void logout(Context _context, boolean removeUsername) {
		remove(_context, PREF_USER_LOGGED_ID);
		remove(_context, PREF_USER_TOKEN);
		if (removeUsername) {
			remove(_context, PREF_USER_NAME);
		}
		remove(_context, PREF_PASSWORD);
	}

	public static String getUserName(Context _context) {
		return getString(_context, PREF_USER_NAME, BUIT);
	}

	public static void setUserName(Context _context, String _name) {
		put(_context, PREF_USER_NAME, _name);
	}

	public static String getPassword(Context _context) {
		return getString(_context, PREF_PASSWORD, BUIT);
	}

	public static void setPassword(Context _context, String _name) {
		put(_context, PREF_PASSWORD, _name);
	}

	public static void setSDID(Context _context, String sdid) {
		put(_context, PREF_SDID, sdid);
	}

	public static String getSDID(Context _context) {
		return getString(_context, PREF_SDID, BUIT);
	}

	public static void setEnabled(Context _context, boolean enabled) {
		put(_context, PREF_ENABLED, enabled);
	}

	public static boolean getEnabled(Context _context) {
		return getBool(_context, PREF_ENABLED, true);
	}

	public static void setLocale(Context _context, String locale) {
		put(_context, PREF_LOCALE, locale);
	}

	public static String getLocale(Context _context, String defaultLocale) {
		return getString(_context, PREF_LOCALE, defaultLocale);
	}

	public static void setConcessio(Context _context, long concessio) {
		put(_context, PREF_CONCESSIO, concessio);
	}

	public static long getConcessio(Context _context) {
		return getLong(_context, PREF_CONCESSIO, -1);
	}

	public static void setUuid(Context _context, String uuid) {
		put(_context, PREF_UUID, uuid);
	}

	public static String getUuid(Context _context) {
		return getString(_context, PREF_UUID, BUIT);
	}

	public static String getAgentId(Context _context) {
		return getString(_context, PREF_AGENT_ID, BUIT);
	}

	public static void setAgentId(Context _context, String agentId) {
		put(_context, PREF_AGENT_ID, agentId);
	}

	public static long getCodiAgent(Context _context) {
		return getLong(_context, PREF_CODI_AGENT, ZERO);
	}

	public static void setCodiAgent(Context _context, long codiAgent) {
		put(_context, PREF_CODI_AGENT, codiAgent);
	}

	public static void setTerminal(Context _context, String terminal) {
		put(_context, PREF_TERMINAL, terminal);
	}

	public static String getTerminal(Context _context) {
		return getString(_context, PREF_TERMINAL, BUIT);
	}

	public static long getIncrementalTime(Context _context) {
		return getLong(_context, PREF_INCREMENTAL_TIME, ZERO);
	}

	public static void setIncrementalTime(Context _context, long _dataServer) throws ParseException {
		long horaActual = System.currentTimeMillis();
		long horaServer = Utils.convertServerTimeToMillis(_dataServer);

		put(_context, PREF_INCREMENTAL_TIME, (horaActual - horaServer));
	}

	public static void setConcessioString(Context _context, String concessio) {
		put(_context, PREF_CONCESSIO_STRING, concessio);
	}

	public static String getConcessioString(Context _context) {
		return getString(_context, PREF_CONCESSIO_STRING, BUIT);
	}

	public static int getControl(Context _context) {
		int control = getInt(_context, PREF_CONTROL, ZERO);
		int controlFinal = (control < 1 || control > 6) ? 1 : control + 1;
		put(_context, PREF_CONTROL, controlFinal);
		return controlFinal;
	}

	public static void clearFormulari(Context _context) {
		remove(_context, PREF_TIPUSVEHICLE);
		remove(_context, PREF_MARCA);
		remove(_context, PREF_MODEL);
		remove(_context, PREF_COLOR);
		remove(_context, PREF_INFRACCIO);
		remove(_context, PREF_CARRER);
		remove(_context, PREF_NUMERO);
	}

	public static void setFormulariTipus(Context _context, String id) {
		put(_context, PREF_TIPUSVEHICLE, id);
	}

	public static String getFormulariTipus(Context _context) {
		return getString(_context, PREF_TIPUSVEHICLE, BUIT);
	}

	public static void setFormulariMarca(Context _context, String id) {
		put(_context, PREF_MARCA, id);
	}

	public static String getFormulariMarca(Context _context) {
		return getString(_context, PREF_MARCA, BUIT);
	}

	public static void setFormulariModel(Context _context, String id) {
		put(_context, PREF_MODEL, id);
	}

	public static String getFormulariModel(Context _context) {
		return getString(_context, PREF_MODEL, BUIT);
	}

	public static void setFormulariColor(Context _context, String id) {
		put(_context, PREF_COLOR, id);
	}

	public static String getFormulariColor(Context _context) {
		return getString(_context, PREF_COLOR, BUIT);
	}

	public static void setFormulariInfraccio(Context _context, String id) {
		put(_context, PREF_INFRACCIO, id);
	}

	public static String getFormulariInfraccio(Context _context) {
		return getString(_context, PREF_INFRACCIO, BUIT);
	}

	public static void setFormulariZona(Context _context, String id) {
		put(_context, PREF_ZONA, id);
	}

	public static String getFormulariZona(Context _context) {
		return getString(_context, PREF_ZONA, BUIT);
	}

	public static void setFormulariCarrer(Context _context, String id) {
		put(_context, PREF_CARRER, id);
	}

	public static String getFormulariCarrer(Context _context) {
		return getString(_context, PREF_CARRER, BUIT);
	}

	public static void setFormulariNumero(Context _context, String id) {
		put(_context, PREF_NUMERO, id);
	}

	public static String getFormulariNumero(Context _context) {
		return getString(_context, PREF_NUMERO, BUIT);
	}

	public static void setEmisora(Context _context, String _emisora) {
		put(_context, PREF_EMISORA, _emisora);
	}

	public static String getEmisora(Context _context) {
		return getString(_context, PREF_EMISORA, BUIT);
	}

	public static void setMod(Context _context, String _mod) {
		put(_context, PREF_MOD, _mod);
	}

	public static String getMod(Context _context) {
		return getString(_context, PREF_MOD, BUIT);
	}

	public static void setReferencia(Context _context, String _referencia) {
		put(_context, PREF_REFERENCIA, _referencia);
	}

	public static String getReferencia(Context _context) {
		return getString(_context, PREF_REFERENCIA, BUIT);
	}

	public static void setIdentificacio(Context _context, String _identificacio) {
		put(_context, PREF_IDENTIFICACIO, _identificacio);
	}

	public static String getIdentificacio(Context _context) {
		return getString(_context, PREF_IDENTIFICACIO, BUIT);
	}

	public static void setImpDte(Context _context, String _impdte) {
		put(_context, PREF_IMPDTE, _impdte);
	}

	public static String getImpDte(Context _context) {
		return getString(_context, PREF_IMPDTE, BUIT);
	}

	public static void setTipusVehicleDefaultValue(Context _context, String _idTipusVehicleDefaultValue) {
		put(_context, PREF_TIPUSVEHICLE_DEFAULTVALUE, _idTipusVehicleDefaultValue);
	}

	public static String getTipusVehicleDefaultValue(Context _context) {
		return getString(_context, PREF_TIPUSVEHICLE_DEFAULTVALUE, BUIT);
	}

	public static void setMarcaDefaultValue(Context _context, String _idMarcaDefaultValue) {
		put(_context, PREF_MARCA_DEFAULTVALUE, _idMarcaDefaultValue);
	}

	public static String getMarcaDefaultValue(Context _context) {
		return getString(_context, PREF_MARCA_DEFAULTVALUE, BUIT);
	}

	public static void setModelDefaultValue(Context _context, String _idModelDefaultValue) {
		put(_context, PREF_MODEL_DEFAULTVALUE, _idModelDefaultValue);
	}

	public static String getModelDefaultValue(Context _context) {
		return getString(_context, PREF_MODEL_DEFAULTVALUE, BUIT);
	}

	public static void setColorDefaultValue(Context _context, String _idColorDefaultValue) {
		put(_context, PREF_COLOR_DEFAULTVALUE, _idColorDefaultValue);
	}

	public static String getColorDefaultValue(Context _context) {
		return getString(_context, PREF_COLOR_DEFAULTVALUE, BUIT);
	}

	public static void setInfraccioDefaultValue(Context _context, String _idInfraccioDefaultValue) {
		put(_context, PREF_INFRACCIO_DEFAULTVALUE, _idInfraccioDefaultValue);
	}

	public static String getInfraccioDefaultValue(Context _context) {
		return getString(_context, PREF_INFRACCIO_DEFAULTVALUE, BUIT);
	}

	public static void setZonaDefaultValue(Context _context, String _idZonaDefaultValue) {
		put(_context, PREF_ZONA_DEFAULTVALUE, _idZonaDefaultValue);
	}

	public static String getZonaDefaultValue(Context _context) {
		return getString(_context, PREF_ZONA_DEFAULTVALUE, BUIT);
	}

	public static void setCarrerDefaultValue(Context _context, String _idCarrerDefaultValue) {
		put(_context, PREF_CARRER_DEFAULTVALUE, _idCarrerDefaultValue);
	}

	public static String getCarrerDefaultValue(Context _context) {
		return getString(_context, PREF_CARRER_DEFAULTVALUE, BUIT);
	}

	public static void setCodiCarrer(Context _context, long idCarrer) {
		put(_context, PREF_CODI_CARRER, idCarrer);
	}

	public static long getCodiCarrer(Context _context) {
		return getLong(_context, PREF_CODI_CARRER, ZERO);
	}

	public static void setNomCarrer(Context _context, String nomCarrer) {
		put(_context, PREF_NOM_CARRER, nomCarrer);
	}

	public static String getNomCarrer(Context _context) {
		return getString(_context, PREF_NOM_CARRER, BUIT);
	}

	public static void setCodiZona(Context _context, long idZona) {
		put(_context, PREF_CODI_ZONA, idZona);
	}

	public static long getCodiZona(Context _context) {
		return getLong(_context, PREF_CODI_ZONA, ZERO);
	}

	public static void setNomZona(Context _context, String nomZona) {
		put(_context, PREF_NOM_ZONA, nomZona);
	}

	public static String getNomZona(Context _context) {
		return getString(_context, PREF_NOM_ZONA, BUIT);
	}

	public static void setPrefCodiControl(Context _context, String codiControl) {
		put(_context, PREF_CODI_CONTROL, codiControl);
	}

	public static String getPrefCodiControl(Context _context) {
		return getString(_context, PREF_CODI_CONTROL, BUIT);
	}

	public static void setPrefFlash(Context _context, int idFlash) {
		put(_context, PREF_FLASH, idFlash);
	}

	public static int getPrefFlash(Context _context) {
		return getInt(_context, PREF_FLASH, ZERO);
	}


	public static void setFoto1(Context _context, String foto1) {
		put(_context, PREF_FOTO1, foto1);


	}

	public static void setFoto2(Context _context, String foto2) {
		put(_context, PREF_FOTO2, foto2);

	}

	public static void setFoto3(Context _context, String foto3) {
		put(_context, PREF_FOTO3, foto3);

	}

	public static void setFoto4(Context _context, String foto4) {
		put(_context, PREF_FOTO4, foto4);

	}

	public static String getFoto1(Context _context) {
		return getString(_context, PREF_FOTO1, BUIT);

	}

	public static String getFoto2(Context _context) {
		return getString(_context, PREF_FOTO2, BUIT);

	}

	public static String getFoto3(Context _context) {
		return getString(_context, PREF_FOTO3, BUIT);

	}

	public static String getFoto4(Context _context) {
		return getString(_context, PREF_FOTO4, BUIT);


	}
}