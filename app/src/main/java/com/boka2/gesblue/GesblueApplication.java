package com.boka2.gesblue;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import androidx.multidex.MultiDexApplication;
import android.util.Log;
import com.flurry.android.FlurryAgent;
import com.boka2.gesblue.activities.LoginActivity;
import com.boka2.gesblue.datamanager.DatabaseAPI;
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;
import com.boka2.gesblue.datamanager.database.model.Model_Log;
import com.boka2.gesblue.datamanager.webservices.DatamanagerAPI;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.NouLogRequest;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.NovaDenunciaRequest;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.PujaFotoRequest;
import com.boka2.gesblue.datamanager.webservices.results.operativa.NouLogResponse;
import com.boka2.gesblue.datamanager.webservices.results.operativa.NovaDenunciaResponse;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.global.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pt.joaocruz04.lib.misc.JSoapCallback;

import static com.boka2.sutils.classes.SSystemUtils.isDebugging;
import static pt.joaocruz04.lib.misc.JsoapError.PARSE_ERROR;

/*
 * Created by Boka2.
 */

public class GesblueApplication extends MultiDexApplication {

	private static GesblueApplication instance;
	private Handler handler = new Handler();
	public Context aContext = null;
	private int intentsEnviaDenuncia =0;
	static Context cont=null;

	public static boolean EnviamentDisponible=true;
	public static boolean DenunciaEnCurs=false;


	@Override
	public void onCreate() {
		super.onCreate();
		cont=getApplicationContext();
		aContext = getApplicationContext();
		getApplicationContext();
		FlurryAgent.init(this, getString(isDebugging(this) ? R.string.flurryApiKeyDebug : R.string.flurryApiKey));

		if ( isExternalStorageWritable() ) {

			File appDirectory = new File( Environment.getExternalStorageDirectory() + "/MyPersonalAppFolder" );
			File logDirectory = new File( appDirectory + "/gesblue_log" );
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
			Date date = new Date();
			File logFile = new File( logDirectory, "logcat-" + dateFormat.format(date) + ".txt" );

			// create app folder
			if ( !appDirectory.exists() ) {
				appDirectory.mkdir();
			}

			// create log folder
			if ( !logDirectory.exists() ) {
				logDirectory.mkdir();
			}

			// clear the previous logcat and then write the new one to the file
			try {
				Process process = Runtime.getRuntime().exec("logcat -c");
				process = Runtime.getRuntime().exec("logcat -f " + logFile);
			} catch ( IOException e ) {
				e.printStackTrace();
			}

		} else {
			// not accessible
		}

		handler.postDelayed(runnable, 5000);

	}
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
			return true;
		}
		return false;
	}

	public static Context getContext(){
		//return instance;
		return cont;
	}
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
      /* do what you need to do */
			ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

			if(!PreferencesGesblue.getOffline(getApplicationContext())) {
				if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
						connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
					//we are connected to a network

					//Condicional de si algu esta enviant
					if (EnviamentDisponible) {
						EnviamentDisponible = false;
						enviaDenuncia();
						//enviaLog();
						//new FTPUpload().execute();
						pujaFoto();

						//SI HI HA UNA DENUNCIA EN CURS ES DEIXA EN FALSE PER EVITAR ENVIAMENTS
						if (!DenunciaEnCurs) {
							EnviamentDisponible = true;
						}

					}
					if (!DenunciaEnCurs) {
						EnviamentDisponible = true;
					}
				}
			}

			Log.e("Estat EnviamentDispon",EnviamentDisponible+"");
			Log.e("Estat DenunciaEnCurs:",DenunciaEnCurs+"");
      /* and here comes the "trick" */
			handler.postDelayed(this, 20000);
		}
	};

	private void enviaDenuncia(){
		Model_Denuncia denuncia;
		denuncia = DatabaseAPI.getDenunciaPendentEnviar(aContext);
		if(denuncia!=null){
			intentsEnviaDenuncia++;
			final Model_Denuncia den = denuncia;
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");

			Log.d("Enviant denuncia", "" + denuncia.getCodidenuncia());
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
					(long) denuncia.getEstatcomprovacio(),             //-- HORA ACTUAL
					"",                //-- IMPORT
					(long)denuncia.getConcessio(),              //-- CONCESSIO
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
								Log.e("", "" + ex);
								onError(PARSE_ERROR);
								return;
							}

							switch ((int) response.getResultat()) {
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
									DatabaseAPI.updateADenunciaEnviada(aContext, den.getCodidenuncia());
									if(intentsEnviaDenuncia<5) {
										enviaDenuncia();
									}else{
										intentsEnviaDenuncia=0;
										return;
									}

							}



						}

						@Override
						public void onError(int error) {
							Log.e("Formulari", "Error NovaDenuncia: " + error);

						}
					}
			);
		}
		else{
			intentsEnviaDenuncia=0;
			return;
		}

	}




	private void pujaFoto(){

		File path = new File("storage/emulated/0/Boka2/upload");

		if(path.exists()) {
			String[] fileNames = path.list();
			final File[] files = path.listFiles();
			int i=0;

			if(files!=null) {
				for (File file : files) {
					final File f = file;
					if (file.isDirectory() == false) {
						i++;
						try {
							byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
							String str_encoded = new String(encoded, StandardCharsets.US_ASCII);


							PujaFotoRequest pjr = new PujaFotoRequest(
									PreferencesGesblue.getConcessio(aContext),
									str_encoded,
									file.getName()
							);
							DatamanagerAPI.crida_PujaFoto(pjr,
									new JSoapCallback() {
										@Override
										public void onSuccess(String result) {
											File direct = new File("storage/emulated/0/Boka2/upload/done");

											if (!direct.exists()) {
												File wallpaperDirectory = new File("storage/emulated/0/Boka2/upload/done");
												wallpaperDirectory.mkdirs();
											}
											File from = new File("storage/emulated/0/Boka2/upload/" + f.getName());
											File to = new File("storage/emulated/0/Boka2/upload/done/" + f.getName());
											from.renameTo(to);

										}

										@Override
										public void onError(int error) {
											Log.e("Formulari", "Error PujaFoto: " + error);
											File direct = new File("storage/emulated/0/Boka2/upload/error");

											if (!direct.exists()) {
												File wallpaperDirectory = new File("storage/emulated/0/Boka2/upload/error");
												wallpaperDirectory.mkdirs();
											}
											File from = new File("storage/emulated/0/Boka2/upload/" + f.getName());
											File to = new File("storage/emulated/0/Boka2/upload/error/" + f.getName());
											from.renameTo(to);

										}
									});

						} catch (Exception e) {

							e.printStackTrace();
						}
						if (i > 5) {
							return;
						}

					}
				}
			}
		}

		else{
			return;
		}

	}
	private void enviaLog(){
		final Model_Log model_log = DatabaseAPI.getLogPendent(aContext);
		SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyyMMddhhmmss");
		if(model_log!=null){

			Log.d("Enviant log",""+model_log.getID());
			NouLogRequest ndr = new NouLogRequest(
					model_log.getCodiacciolog(),
					0,//Long.parseLong(model_log.getFechalog()),
					(long) model_log.getIdagent(),              //-- ID D'AGENT
					Long.parseLong(PreferencesGesblue.getTerminal(aContext)),            //-- TERMINAL ID
					(long) Long.parseLong(model_log.getConcessiolog()),                //-- CONCESSIO
					"",                                                     //-- TODO COORDENADES?
					Utils.getAppVersion(aContext),                        //-- APP VERSION
					model_log.getInfo());                                  //-- INFO



			DatamanagerAPI.crida_NouLog(ndr,
					new JSoapCallback() {
						@Override
						public void onSuccess(String result) {
							Intent intent = new Intent(aContext, LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

							final NouLogResponse response;
							try {
								response = DatamanagerAPI.parseJson(result, NouLogResponse.class);
							} catch (Exception ex) {
								Log.e("",""+ex);
								onError(PARSE_ERROR);
								return;
							}

							switch((int) response.getResultat()) {
								case -1:

								case -2:
								case -3:

									break;
								default:
									//denunciaSent = true;
									//sendPhotos();
									DatabaseAPI.updateLogPendent(aContext,model_log.getID());
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