package com.sixtemia.gesbluedroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.sixtemia.gesbluedroid.activities.LoginActivity;
import com.sixtemia.gesbluedroid.customstuff.ftp.FTPListener;
import com.sixtemia.gesbluedroid.customstuff.ftp.FTPUpload;
import com.sixtemia.gesbluedroid.customstuff.ftp.GBFTP;
import com.sixtemia.gesbluedroid.customstuff.ftp.GBFileUpload;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Denuncia;
import com.sixtemia.gesbluedroid.datamanager.webservices.DatamanagerAPI;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa.NovaDenunciaRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa.NovaDenunciaResponse;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.joaocruz04.lib.misc.JSoapCallback;

import static android.text.TextUtils.isEmpty;
import static com.sixtemia.sutils.classes.SSystemUtils.isDebugging;
import static pt.joaocruz04.lib.misc.JsoapError.PARSE_ERROR;

/**
 * Creat per rubengonzalez al 20/2/17.
 */

public class GesblueApplication extends MultiDexApplication {
	private Handler handler = new Handler();
	private Context aContext = null;
	@Override
	public void onCreate() {
		super.onCreate();
		aContext = getApplicationContext();
		getApplicationContext();
		FlurryAgent.init(this, getString(isDebugging(this) ? R.string.flurryApiKeyDebug : R.string.flurryApiKey));

		handler.postDelayed(runnable, 5000);

	}
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
      /* do what you need to do */
			enviaDenuncia();
			new FTPUpload().execute();
      /* and here comes the "trick" */
			handler.postDelayed(this, 10000);
		}
	};

	private void enviaDenuncia(){
		final Model_Denuncia denuncia = DatabaseAPI.getDenunciaPendent(aContext);
		SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyyMMddhhmmss");
		if(denuncia!=null){

			Log.d("Enviant denuncia",""+denuncia.getCodidenuncia());
			NovaDenunciaRequest ndr = new NovaDenunciaRequest(
					denuncia.getCodidenuncia(),
					Long.parseLong(simpleDate.format(denuncia.getFechacreacio())),
					(long) denuncia.getAgent(),              //-- ID D'AGENT
					(long) denuncia.getAdrecacarrer(),                //-- CARRER
					String.valueOf(denuncia.getAdrecanum()),                                     //-- NUMERO CARRER
					"",                                                     //-- TODO COORDENADES?
					denuncia.getMatricula(),                                  //-- MATRICULA
					(long) denuncia.getTipusvehicle(),    //-- CODI TIPUS VEHICLE
					(long) denuncia.getMarca(),  //-- CODI MARCA
					(long) denuncia.getModel(),  //-- CODI MODEL
					(long) denuncia.getColor(),  //-- CODI COLOR
					(long) denuncia.getInfraccio(),                   //-- MATRICULA
					0,             //-- HORA ACTUAL
					"",                //-- IMPORT
					PreferencesGesblue.getConcessio(aContext),              //-- CONCESSIO
					Long.parseLong(PreferencesGesblue.getTerminal(aContext)),//-- TERMINAL ID
					Utils.getAndroidVersion(),                              //-- SO VERSION
					Utils.getAppVersion(aContext));                         //-- APP VERSION



			DatamanagerAPI.crida_NovaDenuncia(ndr,
					new JSoapCallback() {
						@Override
						public void onSuccess(String result) {
							Intent intent = new Intent(aContext, LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

							final NovaDenunciaResponse response;
							try {
								response = DatamanagerAPI.parseJson(result, NovaDenunciaResponse.class);
							} catch (Exception ex) {
								Log.e("",""+ex);
								onError(PARSE_ERROR);
								return;
							}

							switch((int) response.getResultat()) {
								case -1:
									Utils.showCustomDialog(aContext, R.string.atencio, R.string.errorEnDades);
									break;
								case -2:
								case -3:
									PreferencesGesblue.logout(aContext);
									startActivity(intent);
									break;
								default:
									//denunciaSent = true;
									//sendPhotos();
									DatabaseAPI.updateDenunciaPendent(aContext,denuncia.getCodidenuncia());
									return; //Return aqui per no tancar el ProgressDialog
							}

						}

						@Override
						public void onError(int error) {
							Log.e("Formulari", "Error NovaDenuncia: " + error);

						}
					}
			);
		}

	}

}