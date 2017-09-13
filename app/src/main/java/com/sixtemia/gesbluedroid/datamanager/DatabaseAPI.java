package com.sixtemia.gesbluedroid.datamanager;

import android.content.Context;
import android.text.TextUtils;

import com.sixtemia.gesbluedroid.datamanager.database.executors.OperationExecutorHelper;
import com.sixtemia.gesbluedroid.datamanager.database.executors.TransactionAsyncTask;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.AccioPosicioHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.CarrerHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.ColorHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.DenunciaHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.InfraccioHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.MarcaHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.ModelHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.PosicioAgentHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.TerminalHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.TipusAnulacioHelper;
import com.sixtemia.gesbluedroid.datamanager.database.helpers.TipusVehicleHelper;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_AccioPosicio;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Carrer;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Color;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Denuncia;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Infraccio;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Marca;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Model;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_PosicioAgent;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Terminal;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_TipusAnulacio;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_TipusVehicle;
import com.sixtemia.gesbluedroid.datamanager.database.parameters.DBConfigParams;
import com.sixtemia.gesbluedroid.datamanager.database.results.BasicDBResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alejandroarangua on 7/7/16.
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

	/** AccioPosicio **/
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
	/** /AccioPosicio **/

	/** Carrer **/
	private static BasicDBResult _getCarrers(Context c) {
		return executeDatabaseOperation(c, new CarrerHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getCarrer(Context c, String id) {
		return executeDatabaseOperation(c, new CarrerHelper().getField(c, Model_Carrer.ID, id));
	}

	public static ArrayList<Model_Carrer> getCarrers(Context c) {
		return (ArrayList<Model_Carrer>) _getCarrers(c).getArray();
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
	/** /Carrer **/


	/** Color **/
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
	/** /Color **/


	/** Denuncia **/
	private static BasicDBResult _getDenuncies(Context c) {
		return executeDatabaseOperation(c, new DenunciaHelper().getAll(c));
	}

	public static ArrayList<Model_Denuncia> getDenuncies(Context c) {
		return (ArrayList<Model_Denuncia>) _getDenuncies(c).getArray();
	}
	private static BasicDBResult _getDenunciaPendent(Context c) {
		return executeDatabaseOperation(c, new DenunciaHelper().getField(c,"tipusanulacio","-1"));
	}
	public static Model_Denuncia getDenunciaPendent(Context c) {

		ArrayList<Model_Denuncia> array= (ArrayList<Model_Denuncia>) _getDenunciaPendent(c).getArray();
		if(array.size()>0){
			return array.get(0);
		}
		else{
			return null;
		}
	}

	public static BasicDBResult insertDenuncies(Context c, List<Model_Denuncia> list) {
		return executeDatabaseOperation(c, new DenunciaHelper().create(c, list));
	}

	public static void updateDenunciaPendent(Context c,String id){
		executeDatabaseOperation(c, new DenunciaHelper().update(c,"codidenuncia",id,"tipusanulacio",1));
	}

	public static BasicDBResult deleteAllDenuncies(Context c) {
		return executeDatabaseOperation(c, new DenunciaHelper().deleteAll(c));
	}
	/** /Denuncia **/


	/** Infraccio **/
	private static BasicDBResult _getInfraccions(Context c) {
		return executeDatabaseOperation(c, new InfraccioHelper().getAllGroupById(c));
	}

	private static BasicDBResult _getInfraccio(Context c, String id) {
		return executeDatabaseOperation(c, new InfraccioHelper().getField(c, Model_Infraccio.ID, id));
	}

	public static ArrayList<Model_Infraccio> getInfraccions(Context c) {
		return (ArrayList<Model_Infraccio>) _getInfraccions(c).getArray();
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
	/** /Infraccio **/


	/** Marca **/
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
	/** /Marca **/


	/** Model **/
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
	/** /Model **/


	/** PosicioAgent **/
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
	/** /PosicioAgent **/


	/** Terminal **/
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
	/** /Terminal **/


	/** TipusAnulacio **/
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
	/** /TipusAnulacio **/


	/** TipusVehicle **/
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
	/** /TipusVehicle **/
}