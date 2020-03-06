package com.boka2.gesblue.datamanager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.boka2.gesblue.datamanager.database.executors.OperationExecutorHelper;
import com.boka2.gesblue.datamanager.database.executors.TransactionAsyncTask;
import com.boka2.gesblue.datamanager.database.helpers.AccioPosicioHelper;
import com.boka2.gesblue.datamanager.database.helpers.AgentHelper;
import com.boka2.gesblue.datamanager.database.helpers.CarrerHelper;
import com.boka2.gesblue.datamanager.database.helpers.ColorHelper;
import com.boka2.gesblue.datamanager.database.helpers.DenunciaHelper;
import com.boka2.gesblue.datamanager.database.helpers.InfraccioHelper;
import com.boka2.gesblue.datamanager.database.helpers.LlistaAbonatsHelper;
import com.boka2.gesblue.datamanager.database.helpers.LlistaBlancaHelper;
import com.boka2.gesblue.datamanager.database.helpers.LogHelper;
import com.boka2.gesblue.datamanager.database.helpers.MarcaHelper;
import com.boka2.gesblue.datamanager.database.helpers.ModelHelper;
import com.boka2.gesblue.datamanager.database.helpers.PosicioAgentHelper;
import com.boka2.gesblue.datamanager.database.helpers.TerminalHelper;
import com.boka2.gesblue.datamanager.database.helpers.TipusAnulacioHelper;
import com.boka2.gesblue.datamanager.database.helpers.TipusVehicleHelper;
import com.boka2.gesblue.datamanager.database.helpers.ZonaHelper;
import com.boka2.gesblue.datamanager.database.model.Model_AccioPosicio;
import com.boka2.gesblue.datamanager.database.model.Model_Agent;
import com.boka2.gesblue.datamanager.database.model.Model_Carrer;
import com.boka2.gesblue.datamanager.database.model.Model_Color;
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;
import com.boka2.gesblue.datamanager.database.model.Model_Infraccio;
import com.boka2.gesblue.datamanager.database.model.Model_LlistaAbonats;
import com.boka2.gesblue.datamanager.database.model.Model_LlistaBlanca;
import com.boka2.gesblue.datamanager.database.model.Model_Log;
import com.boka2.gesblue.datamanager.database.model.Model_Marca;
import com.boka2.gesblue.datamanager.database.model.Model_Model;
import com.boka2.gesblue.datamanager.database.model.Model_PosicioAgent;
import com.boka2.gesblue.datamanager.database.model.Model_Terminal;
import com.boka2.gesblue.datamanager.database.model.Model_TipusAnulacio;
import com.boka2.gesblue.datamanager.database.model.Model_TipusVehicle;
import com.boka2.gesblue.datamanager.database.model.Model_Zona;
import com.boka2.gesblue.datamanager.database.parameters.DBConfigParams;
import com.boka2.gesblue.datamanager.database.results.BasicDBResult;
import com.boka2.gesblue.global.PreferencesGesblue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Created by Boka2.
 * TOTES LES CRIDES D'AQUESTA API HAN DE SER SINCRONES (ELS MÈTODES NO RETORNEN RES FINS A ACABAR PERQUÈ CADA MÈTODE ÉS UNA OPERACIÓ A LA BASE DE DADES)
 * NOTA: UNA OPERACIÓ DE BASE DE DADES S'ENCAPSULA EN EL HELPER, I POT FER MOLTES TRANSACCIÓNS. PER TANT, SI ES VOLEN FER MOLTES TRANSACCIONS EN UNA
 * MATEIXA OPERACIÓ, S'HA DE CREAR UNA NOVA OPERACIÓ AMB LES MÚLTIPLES TRANSACCIONS EN EL HELPER QUE SIGUI, I EXECUTAR-LA DES D'AQUÍ.
 */
public class DatabaseAPI {
	public static final String TAG = "DBAPI";

	private static BasicDBResult executeDatabaseOperation(Context _context, OperationExecutorHelper _operationHelper) {
		BasicDBResult result = new BasicDBResult();
		TransactionAsyncTask rh = new TransactionAsyncTask(null, _context);
		boolean terminated = false;
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			rh.start(_operationHelper, executor);
			executor.shutdown();
			terminated = executor.awaitTermination(DBConfigParams.timeOut, DBConfigParams.units);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(terminated) { //Ha acabat per haver executat la tasca, no per time out
			result = rh.getResult();
		} else {
			result.setStrResult(BasicDBResult.KO);
			result.setStrMsg(BasicDBResult.error_timeout);
		}
		return result;
	}

	/* AccioPosicio **/
	private static BasicDBResult _getAccioPosicions(Context c) {
		return executeDatabaseOperation(c, new AccioPosicioHelper().getAll(c));
	}

	public static ArrayList<Model_AccioPosicio> getAccioPosicions(Context c) {
		return (ArrayList<Model_AccioPosicio>) _getAccioPosicions(c).getArray();
	}

//	public static BasicDBResult insertNouTerminal(Context c, Model_Terminal list) {
//		return executeDatabaseOperation(c, new AccioPosicioHelper().create(c, list));
//	}

	public static BasicDBResult insertAccioPosicions(Context c, List<Model_AccioPosicio> list) {
		return executeDatabaseOperation(c, new AccioPosicioHelper().create(c, list));
	}

	public static BasicDBResult deleteAllAccioPosicions(Context c) {
		return executeDatabaseOperation(c, new AccioPosicioHelper().deleteAll(c));
	}
	/* /AccioPosicio **/


	/* Zona **/
	private static BasicDBResult _getZones(Context c) {
		return executeDatabaseOperation(c, new ZonaHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getZona(Context c, String id) {
		return executeDatabaseOperation(c, new ZonaHelper().getField(c, Model_Zona.ID, id));
	}

	public static ArrayList<Model_Zona> getZones(Context c) {
		return (ArrayList<Model_Zona>) _getZones(c).getArray();
	}

	public static Model_Zona getZona(Context c, String id) {
		return  _getZona(c, id).getFirst();
	}

	public static BasicDBResult insertZones(Context c, List<Model_Zona> list) {
		return executeDatabaseOperation(c, new ZonaHelper().create(c, list));
	}

	public static BasicDBResult deleteAllZones(Context c) {
		return executeDatabaseOperation(c, new ZonaHelper().deleteAll(c));
	}

	public static BasicDBResult deleteZona(Context c, String codi) {
		return executeDatabaseOperation(c, new ZonaHelper().deleteWhere(c, "codizona", codi));
	}
	/* /Zona **/

	/* Carrer **/
	private static BasicDBResult _getCarrers(Context c) {
		return executeDatabaseOperation(c, new CarrerHelper().getAllGroupById(c));
	}
	private static BasicDBResult _getCarrersZona(Context c) {

		Log.d("Codizona get",""+PreferencesGesblue.getCodiZona(c));
		return executeDatabaseOperation(c, new CarrerHelper().getField(c, "zona", PreferencesGesblue.getCodiZona(c)));
		//return executeDatabaseOperation(c, new CarrerHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getCarrer(Context c, String id) {
		return executeDatabaseOperation(c, new CarrerHelper().getField(c, Model_Carrer.ID, id));
	}

	public static ArrayList<Model_Carrer> getCarrers(Context c) {
		return (ArrayList<Model_Carrer>) _getCarrers(c).getArray();
	}

	public static ArrayList<Model_Carrer> getCarrersZona(Context c) {
		return (ArrayList<Model_Carrer>) _getCarrersZona(c).getArray();
	}
	public static Model_Carrer getCarrer(Context c, String id) {
		return  _getCarrer(c, id).getFirst();
	}

	public static BasicDBResult insertCarrers(Context c, List<Model_Carrer> list) {
		return executeDatabaseOperation(c, new CarrerHelper().create(c, list));
	}

	public static BasicDBResult deleteAllCarrers(Context c) {
		return executeDatabaseOperation(c, new CarrerHelper().deleteAll(c));
	}

	public static BasicDBResult deleteCarrer(Context c, String codi) {
		return executeDatabaseOperation(c, new CarrerHelper().deleteWhere(c, "codicarrer", codi));
	}
	/* /Carrer **/


	/* Color **/
	private static BasicDBResult _getColors(Context c) {
		return executeDatabaseOperation(c, new ColorHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getColor(Context c, String id) {
		return executeDatabaseOperation(c, new ColorHelper().getField(c, Model_Color.ID, id));
	}

	public static ArrayList<Model_Color> getColors(Context c) {
		return (ArrayList<Model_Color>) _getColors(c).getArray();
	}

	public static Model_Color getColor(Context c, String id) {
		return _getColor(c, id).getFirst();
	}

	public static BasicDBResult insertColors(Context c, List<Model_Color> list) {
		return executeDatabaseOperation(c, new ColorHelper().create(c, list));
	}

	public static BasicDBResult deleteAllColors(Context c) {
		return executeDatabaseOperation(c, new ColorHelper().deleteAll(c));
	}

	public static BasicDBResult deleteColor(Context c, String color) {
		return executeDatabaseOperation(c, new ColorHelper().deleteWhere(c, "codicolor", color));
	}
	/* /Color **/


	/* Denuncia **/
	private static BasicDBResult _getDenuncies(Context c) {
		return executeDatabaseOperation(c, new DenunciaHelper().getAll(c));
	}

	public static ArrayList<Model_Denuncia> getDenuncies(Context c) {
		return (ArrayList<Model_Denuncia>) _getDenuncies(c).getArray();
	}
	private static BasicDBResult _getDenunciaPendentEnviar(Context c) {
		return executeDatabaseOperation(c, new DenunciaHelper().getField(c,"tipusanulacio","0"));
	}

	private static BasicDBResult _getDenunciaPendentImprimir(Context c) {
		return executeDatabaseOperation(c, new DenunciaHelper().getField(c,"tipusanulacio","-1"));
	}

	public static ArrayList<Model_Denuncia> getDenunciesPendentsImprmir(Context c) {

		ArrayList<Model_Denuncia> array= (ArrayList<Model_Denuncia>) _getDenunciaPendentImprimir(c).getArray();
		if(array !=null && array.size()>0){
			return array;
		}
		else{
			return null;
		}
	}



	public static Model_Denuncia getDenunciaPendentEnviar(Context c) {

		ArrayList<Model_Denuncia> array= (ArrayList<Model_Denuncia>) _getDenunciaPendentEnviar(c).getArray();
		if(array !=null && array.size()>0){
			return array.get(0);
		}
		else{
			return null;
		}
	}

	public static ArrayList<Model_Denuncia> getDenunciesPendentsEnviar(Context c) {

		ArrayList<Model_Denuncia> array= (ArrayList<Model_Denuncia>) _getDenunciaPendentEnviar(c).getArray();
		if(array !=null && array.size()>0){
			return array;
		}
		else{
			return null;
		}
	}


	public static BasicDBResult insertDenuncies(Context c, List<Model_Denuncia> list) {
		return executeDatabaseOperation(c, new DenunciaHelper().create(c, list));
	}

	public static void updateADenunciaEnviada(Context c, String id){
		executeDatabaseOperation(c, new DenunciaHelper().update(c,"codidenuncia",id,"tipusanulacio",1));
	}

	public static void updateADenunciaImpresa(Context c, String id){
		executeDatabaseOperation(c, new DenunciaHelper().update(c,"codidenuncia",id,"tipusanulacio",0));
	}


	public static BasicDBResult deleteAllDenuncies(Context c) {
		return executeDatabaseOperation(c, new DenunciaHelper().deleteAll(c));
	}
	/* /Denuncia **/


	/* Infraccio **/
	private static BasicDBResult _getInfraccions(Context c) {
		return executeDatabaseOperation(c, new InfraccioHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getInfraccionsZona(Context c) {
		return executeDatabaseOperation(c, new InfraccioHelper().getField(c, "zona", PreferencesGesblue.getCodiZona(c)));

		//return executeDatabaseOperation(c, new InfraccioHelper().getAllGroupById(c));
	}
	private static BasicDBResult _getInfraccio(Context c, String id) {
		return executeDatabaseOperation(c, new InfraccioHelper().getField(c, Model_Infraccio.ID, id));
	}

	public static ArrayList<Model_Infraccio> getInfraccions(Context c) {
		return (ArrayList<Model_Infraccio>) _getInfraccions(c).getArray();
	}

	public static ArrayList<Model_Infraccio> getInfraccionsZona(Context c) {
		return (ArrayList<Model_Infraccio>) _getInfraccionsZona(c).getArray();
	}
	public static Model_Infraccio getInfraccio(Context c, String id) {
		return _getInfraccio(c, id).getFirst();
	}

	public static BasicDBResult insertInfraccions(Context c, List<Model_Infraccio> list) {
		return executeDatabaseOperation(c, new InfraccioHelper().create(c, list));
	}

	public static BasicDBResult deleteAllInfraccions(Context c) {
		return executeDatabaseOperation(c, new InfraccioHelper().deleteAll(c));
	}

	public static BasicDBResult deleteInfraccio(Context c, long codi) {
		return executeDatabaseOperation(c, new InfraccioHelper().deleteWhere(c, "codiinfraccio", codi));
	}
	/* /Infraccio **/

	/* LlistaBlanca **/
	private static BasicDBResult _getLlistaBlanca(Context c) {
		return executeDatabaseOperation(c, new LlistaBlancaHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getLlistaBlanca(Context c, String id) {
		return executeDatabaseOperation(c, new LlistaBlancaHelper().getField(c, Model_LlistaBlanca.ID, id));
	}

	public static ArrayList<Model_LlistaBlanca> getLlistaBlanca(Context c) {
		return (ArrayList<Model_LlistaBlanca>) _getLlistaBlanca(c).getArray();
	}


	public static Model_LlistaBlanca getLlistaBlanca(Context c, String id) {
		return _getLlistaBlanca(c, id).getFirst();
	}

    private static BasicDBResult _findLlistaBlanca(Context c, String matricula) {
        String[] fields =  {"matricula"};
        Object[] values = {matricula};
        return executeDatabaseOperation(c, new LlistaBlancaHelper().getFields(c, fields, values));
    }


    public static Model_LlistaBlanca findLlistaBlanca(Context c, String matricula) {
        return _findLlistaBlanca(c, matricula).getFirst();
    }

	public static BasicDBResult inserLlistaBlanca(Context c, List<Model_LlistaBlanca> list) {
		return executeDatabaseOperation(c, new LlistaBlancaHelper().create(c, list));
	}

	public static BasicDBResult deleteAllLlistaBlanca(Context c) {
		return executeDatabaseOperation(c, new LlistaBlancaHelper().deleteAll(c));
	}

	public static BasicDBResult deleteLlistaBlanca(Context c, long codi) {
		return executeDatabaseOperation(c, new LlistaBlancaHelper().deleteWhere(c, "codillistablanca", codi));
	}
	/* /LlistaBlanca **/

    /* LlistaAbonats **/
    private static BasicDBResult _getLlistaAbonats(Context c) {
        return executeDatabaseOperation(c, new LlistaAbonatsHelper().getAllGroupById(c));
    }

    private static BasicDBResult _getLlistaAbonats(Context c, String id) {
        return executeDatabaseOperation(c, new LlistaAbonatsHelper().getField(c, Model_LlistaAbonats.ID, id));
    }

    public static ArrayList<Model_LlistaAbonats> getLlistaAbonats(Context c) {
        return (ArrayList<Model_LlistaAbonats>) _getLlistaAbonats(c).getArray();
    }


    public static Model_LlistaAbonats getLlistaAbonats(Context c, String id) {
        return _getLlistaAbonats(c, id).getFirst();
    }

    private static BasicDBResult _findLlistaAbonats(Context c, String matricula) {
        String[] fields =  {"matricula"};
        Object[] values = {matricula};
        return executeDatabaseOperation(c, new LlistaAbonatsHelper().getFields(c, fields, values));
    }


    public static Model_LlistaAbonats findLlistaAbonats(Context c, String matricula) {
        return _findLlistaAbonats(c, matricula).getFirst();
    }

    public static BasicDBResult insertLlistaAbonats(Context c, List<Model_LlistaAbonats> list) {
        return executeDatabaseOperation(c, new LlistaAbonatsHelper().create(c, list));
    }

    public static BasicDBResult deleteAllLlistaAbonats(Context c) {
        return executeDatabaseOperation(c, new LlistaAbonatsHelper().deleteAll(c));
    }

    public static BasicDBResult deleteLlistaAbonats(Context c, long codi) {
        return executeDatabaseOperation(c, new LlistaAbonatsHelper().deleteWhere(c, "codillistaabonats", codi));
    }
    /* /LlistaAbonats **/

	/* Log **/
	private static BasicDBResult _getLogs(Context c) {
		return executeDatabaseOperation(c, new LogHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getLog(Context c, String id) {
		return executeDatabaseOperation(c, new LogHelper().getField(c, Model_Log.ID, id));
	}

	public static ArrayList<Model_Log> getLogs(Context c) {
		return (ArrayList<Model_Log>) _getLogs(c).getArray();
	}

	public static Model_Log getLog(Context c, String id) {
		return _getLog(c, id).getFirst();
	}
	private static BasicDBResult _getLogPendent(Context c) {
		return executeDatabaseOperation(c, new LogHelper().getField(c,"enviat","0"));
	}
	public static Model_Log getLogPendent(Context c) {

		ArrayList<Model_Log> array= (ArrayList<Model_Log>) _getLogPendent(c).getArray();
		if(array.size()>0){
			return array.get(0);
		}
		else{
			return null;
		}
	}
	public static BasicDBResult insertLogs(Context c, List<Model_Log> list) {
		return executeDatabaseOperation(c, new LogHelper().create(c, list));
	}
	public static void updateLogPendent(Context c,String id){
		executeDatabaseOperation(c, new LogHelper().update(c,"codilog",id,"enviat",1));
	}
	public static BasicDBResult deleteAllLogs(Context c) {
		return executeDatabaseOperation(c, new LogHelper().deleteAll(c));
	}

	public static BasicDBResult deleteLog(Context c, String log) {
		return executeDatabaseOperation(c, new LogHelper().deleteWhere(c, "codilog", log));
	}
	/* /Log **/


	/* Agent **/
	private static BasicDBResult _getAgents(Context c) {
		return executeDatabaseOperation(c, new AgentHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getAgent(Context c, String id) {
		return executeDatabaseOperation(c, new AgentHelper().getField(c, Model_Agent.ID, id));
	}
	private static BasicDBResult _findAgent(Context c, String login, String password) {
		String[] fields =  {"login","password"};
		Object[] values = {login,password};
		return executeDatabaseOperation(c, new AgentHelper().getFields(c, fields, values));
	}

	public static ArrayList<Model_Agent> getAgents(Context c) {
		return (ArrayList<Model_Agent>) _getAgents(c).getArray();
	}

	public static Model_Agent getAgent(Context c, String id) {
		return _getAgent(c, id).getFirst();
	}

	public static Model_Agent findAgent(Context c, String login,String password) {
		return _findAgent(c, login, password).getFirst();
	}

	public static BasicDBResult insertAgents(Context c, List<Model_Agent> list) {
		return executeDatabaseOperation(c, new AgentHelper().create(c, list));
	}

	public static BasicDBResult deleteAllAgents(Context c) {
		return executeDatabaseOperation(c, new AgentHelper().deleteAll(c));
	}

	public static BasicDBResult deleteAgent(Context c, String codi) {
		return executeDatabaseOperation(c, new AgentHelper().deleteWhere(c, "codiagent", codi));
	}
	/* /Agent **/

	/* Marca **/
	private static BasicDBResult _getMarques(Context c) {
		return executeDatabaseOperation(c, new MarcaHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getMarca(Context c, String id) {
		return executeDatabaseOperation(c, new MarcaHelper().getField(c, Model_Marca.ID, id));
	}

	public static ArrayList<Model_Marca> getMarques(Context c) {
		return (ArrayList<Model_Marca>) _getMarques(c).getArray();
	}

	public static Model_Marca getMarca(Context c, String id) {
		return _getMarca(c, id).getFirst();
	}

	public static BasicDBResult insertMarques(Context c, List<Model_Marca> list) {
		return executeDatabaseOperation(c, new MarcaHelper().create(c, list));
	}

	public static BasicDBResult deleteAllMarques(Context c) {
		return executeDatabaseOperation(c, new MarcaHelper().deleteAll(c));
	}

	public static BasicDBResult deleteMarca(Context c, String codi) {
		return executeDatabaseOperation(c, new MarcaHelper().deleteWhere(c, "codimarca", codi));
	}
	/* /Marca **/


	/* Model **/
	private static BasicDBResult _getModels(Context c) {
		return executeDatabaseOperation(c, new ModelHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getModel(Context c, String id) {
		return executeDatabaseOperation(c, new ModelHelper().getField(c, Model_Model.ID, id));
	}

	public static ArrayList<Model_Model> getModelsByMarca(Context c, int idMarca) {
		ArrayList<Model_Model> arrayModels = _getModels(c).getArray();
		ArrayList<Model_Model> arrayModelsAux = new ArrayList<>();
		for(Model_Model m : arrayModels) {

			int value = TextUtils.isEmpty(m.getMarca()) ? 0 : Integer.parseInt(m.getMarca());

			if((TextUtils.isEmpty(m.getMarca()) ? 0 : Integer.parseInt(m.getMarca())) == idMarca) {
				arrayModelsAux.add(m);
			}
		}
		return arrayModelsAux;
	}

	public static Model_Model getModel(Context c, String id) {
		return _getModel(c, id).getFirst();
	}

	public static BasicDBResult insertModels(Context c, List<Model_Model> list) {
		return executeDatabaseOperation(c, new ModelHelper().create(c, list));
	}

	public static BasicDBResult deleteAllModels(Context c) {
		return executeDatabaseOperation(c, new ModelHelper().deleteAll(c));
	}

	public static BasicDBResult deleteModel(Context c, String codi) {
		return executeDatabaseOperation(c, new ModelHelper().deleteWhere(c, "codimodel", codi));
	}
	/* /Model **/


	/* PosicioAgent **/
	private static BasicDBResult _getPosicionsAgent(Context c) {
		return executeDatabaseOperation(c, new PosicioAgentHelper().getAll(c));
	}

	public static ArrayList<Model_PosicioAgent> getPosicionsAgent(Context c) {
		return (ArrayList<Model_PosicioAgent>) _getPosicionsAgent(c).getArray();
	}

	public static BasicDBResult insertPosicionsAgent(Context c, List<Model_PosicioAgent> list) {
		return executeDatabaseOperation(c, new PosicioAgentHelper().create(c, list));
	}

	public static BasicDBResult deleteAllPosicionsAgent(Context c) {
		return executeDatabaseOperation(c, new PosicioAgentHelper().deleteAll(c));
	}
	/* /PosicioAgent **/


	/* Terminal **/
	private static BasicDBResult _getTerminals(Context c) {
		return executeDatabaseOperation(c, new TerminalHelper().getAll(c));
	}

	public static ArrayList<Model_Terminal> getTerminals(Context c) {
		return (ArrayList<Model_Terminal>) _getTerminals(c).getArray();
	}

	public static BasicDBResult insertTerminals(Context c, List<Model_Terminal> list) {
		return executeDatabaseOperation(c, new TerminalHelper().create(c, list));
	}

	public static BasicDBResult deleteAllTerminals(Context c) {
		return executeDatabaseOperation(c, new TerminalHelper().deleteAll(c));
	}
	/* /Terminal **/


	/* TipusAnulacio **/
	private static BasicDBResult _getTipusAnulacions(Context c) {
		return executeDatabaseOperation(c, new TipusAnulacioHelper().getAll(c));
	}

	public static ArrayList<Model_TipusAnulacio> getTipusAnulacions(Context c) {
		return (ArrayList<Model_TipusAnulacio>) _getTipusAnulacions(c).getArray();
	}

	public static BasicDBResult insertTipusAnulacions(Context c, List<Model_TipusAnulacio> list) {
		return executeDatabaseOperation(c, new TipusAnulacioHelper().create(c, list));
	}

	public static BasicDBResult deleteAllTipusAnulacions(Context c) {
		return executeDatabaseOperation(c, new TipusAnulacioHelper().deleteAll(c));
	}
	/* /TipusAnulacio **/


	/* TipusVehicle **/
	private static BasicDBResult _getTipusVehicles(Context c) {
		return executeDatabaseOperation(c, new TipusVehicleHelper().getAllGroupById(c));
	}
	private static BasicDBResult _getTipusVehicle(Context c, String id) {
		return executeDatabaseOperation(c, new TipusVehicleHelper().getField(c, Model_TipusVehicle.ID, id));
	}

	public static ArrayList<Model_TipusVehicle> getTipusVehicles(Context c) {
		return (ArrayList<Model_TipusVehicle>) _getTipusVehicles(c).getArray();
	}

	public static Model_TipusVehicle getTipusVehicle(Context c, String id) {
		return (Model_TipusVehicle) _getTipusVehicle(c, id).getFirst();
	}

	public static BasicDBResult insertTipusVehicles(Context c, List<Model_TipusVehicle> list) {
		return executeDatabaseOperation(c, new TipusVehicleHelper().create(c, list));
	}

	public static BasicDBResult deleteAllTipusVehicles(Context c) {
		return executeDatabaseOperation(c, new TipusVehicleHelper().deleteAll(c));
	}

	public static BasicDBResult deleteTipusVehicle(Context c, long codi) {
		return executeDatabaseOperation(c, new TipusVehicleHelper().deleteWhere(c, Model_TipusVehicle.ID, codi));
	}
	/* /TipusVehicle **/
}