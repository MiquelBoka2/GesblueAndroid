package com.boka2.gesblue.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.BoringLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boka2.gesblue.GesblueApplication;
import com.boka2.gesblue.R;
import com.boka2.gesblue.customstuff.GesblueFragmentActivity;
import com.boka2.gesblue.databinding.ActivityLoginBinding;
import com.boka2.gesblue.datamanager.DatabaseAPI;
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
import com.boka2.gesblue.datamanager.database.model.Model_TipusVehicle;
import com.boka2.gesblue.datamanager.database.model.Model_Zona;
import com.boka2.gesblue.datamanager.database.results.BasicDBResult;
import com.boka2.gesblue.datamanager.webservices.DatamanagerAPI;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.AgentsRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.CarrersRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.ColorsRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.InfraccionsRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.LlistaAbonatsRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.LlistaBlancaRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.LoginRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.MarquesRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.ModelsRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.NouTerminalRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.TipusVehiclesRequest;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.ZonesRequest;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.EstablirComptadorDenunciaRequest;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.NovaDenunciaRequest;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.PujaFotoRequest;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.RecuperaComptadorDenunciaRequest;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.AgentsResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.CarrersResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.ColorsResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.InfraccionsResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.LlistaAbonatsResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.LlistaBlancaResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.LoginResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.MarquesResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.ModelsResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.NouTerminalResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.TipusVehiclesResponse;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.ZonesResponse;
import com.boka2.gesblue.datamanager.webservices.results.operativa.EstablirComptadorDenunciaResponse;
import com.boka2.gesblue.datamanager.webservices.results.operativa.NovaDenunciaResponse;
import com.boka2.gesblue.datamanager.webservices.results.operativa.RecuperaComptadorDenunciaResponse;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.global.Utils;
import com.boka2.gesblue.model.Models;
import com.boka2.gesblue.model.Tipus_Vehicle;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import pt.joaocruz04.lib.misc.JSoapCallback;


import static com.boka2.gesblue.GesblueApplication.EnviamentDisponible;
import static pt.joaocruz04.lib.misc.JsoapError.PARSE_ERROR;

public class LoginActivity extends GesblueFragmentActivity {

	private ActivityLoginBinding mBinding;


	private boolean isNoLoginConcessio = true;
	private boolean everythingIsOk = true;
	private Boolean adm=false;
	private String concessio = "",concessio_txt;
	private String initialDate = "0";



	private String estat="no_login_concessio";
	private ImageView opciones;
	private TextView localitzacio;

	private int RequestCode=4321;





	private LoginResponse response;

	private boolean refreshDades = false;



	private Menu menu;


	private ProgressDialog progress, liniar_progress;
	private List<Model_Denuncia> denunciesPendents=null;
	private int intentsEnviaDenuncia =0;
	private boolean escapador=true;




	private boolean antirepetidor=true;
	private boolean antirepetidorDADES=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppTheme);




		super.onCreate(savedInstanceState);

		askForPermissions(new String[]{
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.CAMERA
		}, new PermissionListener() {
			@Override
			public void onPermissionsGranted() {
			}

			@Override
			public void onPermissionsDenied() {
				finish();
			}
		});



		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
		setupVisibleToolbar(mBinding.toolbar);
		opciones=mBinding.toolbar.icOpciones;



		/*EL LISTENER DE LES OPCIONS*/{
			opciones.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(mContext, Opcions.class);
					intent.putExtra("estat", estat);
					intent.putExtra("adm", adm);
					intent.putExtra("concessio", isNoLoginConcessio);
					startActivityForResult(intent, RequestCode);
				}
			});
		}

		/*RECUPAREM DADES D'ON PROVENIM**/{
			Bundle extras = getIntent().getExtras();


			if (extras != null) {
				isNoLoginConcessio = extras.getBoolean("isNoLoginConcessio", true);
				adm = extras.getBoolean("adm");

			}
			checkAdmin(adm);
		}



		mBinding.editTextPassword.setTypeface(mBinding.editTextUsuari.getTypeface());

		Utils.getSyncDate(mContext);

		/*PROPIETATS DE LA BARRA DE PROGRES**/
		{
			progress = new ProgressDialog(this);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setIndeterminate(true);
			progress.setCancelable(false);

			liniar_progress = new ProgressDialog(this);
			liniar_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

			liniar_progress.setCancelable(false);
		}


		/* DEFINIM LA CONCESSIO SI EN HI HA*/{
			final String concessioString = PreferencesGesblue.getConcessioString(this);
			isNoLoginConcessio = TextUtils.isEmpty(concessioString);

			concessio = Long.toString(PreferencesGesblue.getConcessio(mContext));

			localitzacio = mBinding.toolbar.txtLocalitzacioEstat;
			//Log.e("Er login de la conzezió", isNoLoginConcessio + "-----");
		}

		/* MODIFIQUEM EL LAYOUT EN FUNCIO DE SI HI HA CONCCESIO O NO*/{
			if(!isNoLoginConcessio) {
				estat="login_concessio";
				mBinding.textViewConcessio.setVisibility(View.GONE);
				mBinding.editTextConcessio.setVisibility(View.GONE);
				concessio_txt = PreferencesGesblue.getConcessioString(mContext);
				localitzacio=mBinding.toolbar.txtLocalitzacioEstat;
				localitzacio.setText(concessio_txt);

			}
			else{
				localitzacio.setText(getString(R.string.no_concessio));

			}
		}



		//POSEM EL USUARI QUE TENIM GUARDAT
		mBinding.editTextUsuari.setText(PreferencesGesblue.getUserName(mContext));






		//Obtenir si hi ha denuncies Pendents
		ContadorDenuncies(true);


		/*LISTENER DE BUTO ACCEPTA**/{
			mBinding.buttonAccepta.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					amagarTeclat();

					String username = mBinding.editTextUsuari.getText().toString();
					String password = mBinding.editTextPassword.getText().toString();
					concessio = mBinding.editTextConcessio.getText().toString();
					if (TextUtils.isEmpty(concessio))
						concessio = Long.toString(PreferencesGesblue.getConcessio(mContext));


					//Log.e("Concesio antes", concessio);

					PreferencesGesblue.setUserName(mContext, username);
					PreferencesGesblue.setPassword(mContext, password);
					PreferencesGesblue.setConcessio(mContext, Long.parseLong(concessio));

					//Log.e("Concesio despues", concessio);
					//Log.e("isNoLoginConcessio", "" + isNoLoginConcessio);

					if (username.equals("") || password.equals("") || (concessio.equals("") && isNoLoginConcessio)) {
						Utils.showFaltenDadesError(mContext);
					} else {
						enableEditTexts(false);
						antirepetidorDADES=true;

						if (isNoLoginConcessio) {
							cridaNouTerminal(username, password, Long.parseLong(concessio), "0");

							//Log.d("Login -1", "Vivo en una piña");
						} else {
							cridaNouTerminal(username, password, Long.parseLong(concessio), "0");

							//Log.d("Login -2", "Fallo Aquí");
							cridaLogin(mBinding.editTextUsuari.getText().toString(), mBinding.editTextPassword.getText().toString(), TextUtils.isEmpty(concessio) ? 0 : Long.parseLong(concessio), PreferencesGesblue.getDataSync(mContext));
						}
					}
				}
			});
		}

		/*LISTENER DE BUTO ENVIA DENUNCIES PENDENTS**/{
			mBinding.btnEnviaPendents.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network

                        if (denunciesPendents!=null) {
							if (denunciesPendents.size() > 0) {


								liniar_progress.setMax(denunciesPendents.size());

								liniar_progress.show();
								if (EnviamentDisponible) {
									EnviamentDisponible = false;
									EnviarDenuncies();

								} else {
									//aguanta el main fins que s'ha enviat la anterior
									while (EnviamentDisponible == false) {


									}
									//Tornem a mirar les dades
									ContadorDenuncies(true);

									//Bloquejem els demes enviaments
									EnviamentDisponible = false;


									if (denunciesPendents!=null) {
										if (denunciesPendents.size() > 0) {
											EnviarDenuncies();
										}
									}
								}

								//Mante el Main activity viu mentres s'envies les denuncies
								while (escapador) {


								}
								liniar_progress.dismiss();
								EnviamentDisponible = true;
								ContadorDenuncies(false);
							}
						}

                        else {
                            AlertDialog.Builder builder= new AlertDialog.Builder(mContext)
                                    .setTitle(getResources().getString(R.string.no_denuncies_a_enviar))

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            // Continue with delete operation
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setIcon(android.R.drawable.ic_dialog_alert);

							AlertDialog alert = builder.create();
							alert.show();
							Button ybutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
							ybutton.setTextColor(Color.BLACK);

                        }



                    } else {
                        mBinding.btnEnviaPendents.setBackgroundColor(getResources().getColor(R.color.vermellKO));
                        mBinding.layNoConexio.setVisibility(View.VISIBLE);

                    }


				}
			});
		}

		/*LISTENERTS I OBJECTES DE DESCARTAR LA NOTIFICACIO QUE SURT AL NO TENIR INTERTNET*/{
			View.OnClickListener descarta_inferior = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mBinding.layNoConexio.setVisibility(View.GONE);
					mBinding.btnEnviaPendents.setBackgroundColor(getResources().getColor(R.color.btn_activat));
				}
			};

			mBinding.layNoConexio.setOnClickListener(descarta_inferior);
			mBinding.txtNoConxio.setOnClickListener(descarta_inferior);
			mBinding.imgCreu.setOnClickListener(descarta_inferior);
		}

		/*LISTENER DE BUTO ESBORRA TOT**/{
			mBinding.btnEsborraTot.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!mBinding.btnEnviaPendents.isEnabled()) {

						if (adm) {

							CustomDialogClass cdd = new CustomDialogClass(LoginActivity.this);
							cdd.show();
						} else {
							Intent intent = new Intent(mContext, Login_Admin.class);
							intent.putExtra("concessio", isNoLoginConcessio);
							intent.putExtra("accio", "Esborra_APP");
							startActivityForResult(intent, RequestCode);
						}
					}
					else {
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
								.setTitle(getResources().getString(R.string.hi_ha_denuncies_pendents))


								.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
										// Continue with delete operation
									}
								})

								// A null listener allows the button to dismiss the dialog and take no further action.
								.setIcon(android.R.drawable.ic_dialog_alert);

						AlertDialog alert = builder.create();
						alert.show();
						Button ybutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
						ybutton.setTextColor(Color.BLACK);
					}

				}


			});
			/*ESBORAR FORÇAT (admin=true/contraseña="bol<a>2.$udo"/concessio=DiaIMes)**/
			mBinding.btnEsborraTot.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {

					if(adm) {
						Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
						int mes = (calendar.get(Calendar.MONTH) + 1);
						int dia = calendar.get(Calendar.DAY_OF_MONTH);
						if (mBinding.editTextPassword.getText().toString().equals("bol<a>2.$udo") && mBinding.editTextConcessio.getText().toString().equals(String.valueOf(dia) + String.valueOf(mes))) {
							CustomDialogClass cdd = new CustomDialogClass(LoginActivity.this);
							cdd.show();
						}

					}
					return false;
				}
			});
		}

		/*LISTENER DE NETEJA DADES**/{
			mBinding.btnNetejaConcessions.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!mBinding.btnEnviaPendents.isEnabled()){



						if(adm){

							DatabaseAPI.deleteAllAgents(mContext);
							DatabaseAPI.deleteAllMarques(mContext);
							DatabaseAPI.deleteAllModels(mContext);
							DatabaseAPI.deleteAllTipusVehicles(mContext);
							DatabaseAPI.deleteAllTipusAnulacions(mContext);
							DatabaseAPI.deleteAllCarrers(mContext);
							DatabaseAPI.deleteAllInfraccions(mContext);
							DatabaseAPI.deleteAllZones(mContext);
							DatabaseAPI.deleteAllLlistaBlanca(mContext);

							PreferencesGesblue.setConcessioString(mContext,"");
							PreferencesGesblue.setConcessio(mContext,0);
							PreferencesGesblue.saveDataSync(mContext,"0");

							isNoLoginConcessio = true;

							mBinding.textViewConcessio.setVisibility(View.VISIBLE);
							mBinding.editTextConcessio.setVisibility(View.VISIBLE);
							localitzacio.setText(getString(R.string.no_concessio));






						}
						else{
							Intent intent = new Intent(mContext, Login_Admin.class);
							intent.putExtra("concessio", isNoLoginConcessio);
							intent.putExtra("accio", "");
							startActivityForResult(intent, RequestCode);

						}
					}
					else{

						AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
								.setTitle(getResources().getString(R.string.hi_ha_denuncies_pendents))


								.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
										// Continue with delete operation
									}
								})

								// A null listener allows the button to dismiss the dialog and take no further action.
								.setIcon(android.R.drawable.ic_dialog_alert);

						AlertDialog alert = builder.create();
						alert.show();
						Button ybutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
						ybutton.setTextColor(Color.BLACK);

					}

				}
			});





		}
	}


	/*FUNCIONAMENT DEL LA FUNCIO ENVIAR DENUNCIES PENDENTS**/
		private void enviaDenunciaConcreta ( final Model_Denuncia denuncia){

			if (denuncia != null) {
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
						Long.parseLong(PreferencesGesblue.getTerminal(mContext)),//-- TERMINAL ID
						Utils.getAndroidVersion(),                              //-- SO VERSION
						Utils.getAppVersion(mContext));                         //-- APP VERSION


				DatamanagerAPI.crida_NovaDenuncia(ndr,
						new JSoapCallback() {
							@Override
							public void onSuccess(String result) {
								Intent intent = new Intent(mContext, LoginActivity.class);
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
										Utils.showCustomDialog(mContext, R.string.atencio, R.string.errorEnDades);
										break;
									case -2:
									case -3:
										PreferencesGesblue.logout(mContext);
										startActivity(intent);
										break;
									default:
										//denunciaSent = true;
										//sendPhotos();
										DatabaseAPI.updateADenunciaEnviada(mContext, den.getCodidenuncia());
										if (intentsEnviaDenuncia < 5) {
											enviaDenunciaConcreta(denuncia);
										} else {
											intentsEnviaDenuncia = 0;
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
			} else {
				intentsEnviaDenuncia = 0;
				return;
			}

		}

		private void pujaFoto () {

			File path = new File("storage/emulated/0/Boka2/upload");

			if (path.exists()) {
				String[] fileNames = path.list();
				final File[] files = path.listFiles();
				int i = 0;

				if (files != null) {
					for (File file : files) {
						final File f = file;
						if (file.isDirectory() == false) {
							i++;
							try {
								byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
								String str_encoded = new String(encoded, StandardCharsets.US_ASCII);


								PujaFotoRequest pjr = new PujaFotoRequest(
										PreferencesGesblue.getConcessio(mContext),
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
			} else {
				return;
			}

		}

		/*AQUESTA FUNCIO CREA EL FIL*/
		private void EnviarDenuncies () {
			//Crea un nou fil
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < denunciesPendents.size(); i++) {
						enviaDenunciaConcreta(denunciesPendents.get(i));
						pujaFoto();


						liniar_progress.setProgress(i);
					}
					denunciesPendents = null;
					escapador = false;
				}
			}).start();

		}



	/*RECUPERA LES DENUNCIES PENDENTS I ACTUALITZA EL UI**/
	private void ContadorDenuncies(boolean recarregardades) {

		//Consegueix les denuncies i adpta l'interfaz a les dades
		if(recarregardades){
			List<Model_Denuncia> denunciesPendentsTemp = DatabaseAPI.getDenunciesPendentsEnviar(mContext);
			if(denunciesPendentsTemp!=null) {


				denunciesPendents = denunciesPendentsTemp.subList(0, denunciesPendentsTemp.size());
			}
			else{
				denunciesPendents=null;
			}
		}


		//Comprovem si tenim denuncies i en cas negatiu, mostrem un missatge informatiu.

		if (denunciesPendents==null || denunciesPendents.size()<=0 || denunciesPendents.isEmpty()){
			mBinding.imgCercleContador.setVisibility(View.GONE);
			mBinding.txtNumDenuncies.setVisibility(View.GONE);

			//btnEnviaPendents
			mBinding.btnEnviaPendents.setBackgroundColor(getResources().getColor(R.color.btn_desectivat));
			mBinding.btnEnviaPendents.setEnabled(false);



			if(adm){
				//btnNetejaConcessio
				mBinding.btnNetejaConcessions.setBackgroundColor(getResources().getColor(R.color.btn_activat));


				//btnEsborraTot
				mBinding.btnEsborraTot.setBackgroundColor(getResources().getColor(R.color.btn_activat));


			}
			else {
				//btnNetejaConcessio
				mBinding.btnNetejaConcessions.setBackgroundColor(getResources().getColor(R.color.btn_desectivat));


				//btnEsborraTot
				mBinding.btnEsborraTot.setBackgroundColor(getResources().getColor(R.color.btn_desectivat));


			}
		}
		else{
			mBinding.imgCercleContador.setVisibility(View.VISIBLE);
			mBinding.txtNumDenuncies.setVisibility(View.VISIBLE);

			mBinding.txtNumDenuncies.setText(denunciesPendents.size()+"");

			//btnEnviaPendents
			mBinding.btnEnviaPendents.setBackgroundColor(getResources().getColor(R.color.btn_activat));



			//btnNetejaConcessio
			mBinding.btnNetejaConcessions.setBackgroundColor(getResources().getColor(R.color.btn_desectivat));


			//btnEsborraTot
			mBinding.btnEsborraTot.setBackgroundColor(getResources().getColor(R.color.btn_desectivat));


		}

	}


	/* Actualitza el UI en funcio de ADMIN**/
	private void checkAdmin(Boolean adm) {

		if (adm){
			mBinding.toolbar.imgUnlock.setVisibility(View.VISIBLE);
			mBinding.toolbar.toolbarBackground.setBackgroundColor(getResources().getColor(R.color.admin));
			mBinding.toolbar.txtLocalitzacioEstat.setBackgroundColor(getResources().getColor(R.color.admin));
		}
		else{
			mBinding.toolbar.imgUnlock.setVisibility(View.GONE);
			mBinding.toolbar.toolbarBackground.setBackgroundColor(getResources().getColor(R.color.barra_estat));
			mBinding.toolbar.txtLocalitzacioEstat.setBackgroundColor(getResources().getColor(R.color.barra_estat));
		}
	}


	@Override
	protected void onResume() {

		super.onResume();
		checkAdmin(adm);


	}

	private void showLoadingAnimButton(boolean show) {
		boolean showingLoading = mBinding.viewSwitcherLoginAnim.getCurrentView() != mBinding.buttonAccepta;

		if(show && !showingLoading) {
			mBinding.viewSwitcherLoginAnim.showNext();
		}else if(showingLoading) {
			mBinding.viewSwitcherLoginAnim.showNext();
		}
	}

	private void enableEditTexts(boolean enable) {
		mBinding.editTextUsuari.setEnabled(enable);
		mBinding.editTextPassword.setEnabled(enable);
		mBinding.editTextConcessio.setEnabled(enable);
	}

	private void cridaNouTerminal(final String username, final String password, final long concessio, final String _data) {
		showLoadingAnimButton(true);

		DatamanagerAPI.crida_NouTerminal(new NouTerminalRequest(username, password, concessio, Utils.getDeviceId(mContext)), new JSoapCallback() {
			@Override
			public void onSuccess(String result) {
				final NouTerminalResponse response;

				//Log.e("Er rezurtao---------->", result);

				try {
					response = DatamanagerAPI.parseJson(result, NouTerminalResponse.class);
				} catch (Exception ex) {
					ELog(ex);
					onError(PARSE_ERROR);
					return;
				}

				PreferencesGesblue.setConcessioString(mContext, response.getTextcap());
				PreferencesGesblue.setConcessio(mContext, concessio);

				showLoadingAnimButton(false);
				enableEditTexts(true);

				//Log.e("Er otro rezurtao------>",""+response.getResultat());

				switch((int) response.getResultat()) {
					case 0:
						guardarDadesAlPreferences(response, username);
//                        cridaLogin(username, password, concessio, _data);
						sincronitzarTot(null, concessio, _data);
						break;
					case -1: //Error de login
						Utils.showCustomDatamanagerError(mContext, getString(R.string.errorLogin));
						enableEditTexts(true);
						everythingIsOk = false;
						if(progress != null && progress.isShowing()) progress.dismiss();
						break;
					case -2: //Terminal pendent
						Utils.showCustomDatamanagerError(mContext, getString(R.string.terminalPendent));
						enableEditTexts(true);
						everythingIsOk = false;
						if(progress != null && progress.isShowing()) progress.dismiss();
						break;
					case -3: //Terminal denegat
						Utils.showCustomDatamanagerError(mContext, getString(R.string.terminalDenegat));
						enableEditTexts(true);
						everythingIsOk = false;
						if(progress != null && progress.isShowing()) progress.dismiss();
						break;
					case -4: //Concessio no valida
						Utils.showCustomDatamanagerError(mContext, getString(R.string.concessioNoValida));
						enableEditTexts(true);
						everythingIsOk = false;
						if(progress != null && progress.isShowing()) progress.dismiss();
						break;
					default:
						Utils.showCustomDatamanagerError(mContext, getString(R.string.otherError));
						enableEditTexts(true);
						everythingIsOk = false;
						if(progress != null && progress.isShowing()) progress.dismiss();
				}

			}

			@Override
			public void onError(final int error) {

				showLoadingAnimButton(false);
				enableEditTexts(true);

				Utils.showDatamanagerError(mContext, error);
				everythingIsOk = false;

			}
		});
	}
	private void guardarDadesAlPreferences(NouTerminalResponse nt, String username) {
		PreferencesGesblue.setTerminal(mContext, Long.toString(nt.getTerminal()));
		PreferencesGesblue.saveLogo(mContext, nt.getLogo());
		PreferencesGesblue.saveImatgePeu(mContext, nt.getImatgepeu());
		PreferencesGesblue.saveTextCap(mContext, nt.getTextcap());
		PreferencesGesblue.setAgentId(mContext, username);
		PreferencesGesblue.saveCodiBarresVisible(mContext, nt.hasCodiBarresVisible());
		PreferencesGesblue.saveTextPeuVisible(mContext, nt.hasTextPeuVisible());
		PreferencesGesblue.saveTextPeu(mContext, nt.hasTextPeu());
		PreferencesGesblue.saveImportAnulacio(mContext, nt.hasImportAnulacio());
		PreferencesGesblue.saveLogosMarques(mContext, nt.hasLogosMarques());
		PreferencesGesblue.saveLogoQr(mContext, nt.hasLogoQr());
		PreferencesGesblue.saveAdrecaQr(mContext, nt.getAdrecaqr());
		PreferencesGesblue.saveIconesGenerics(mContext, nt.hasIconesGenerics());
		PreferencesGesblue.saveImpresora(mContext, nt.isImpresoraAmple());
		PreferencesGesblue.saveCodiBarresServiCaixa(mContext, nt.hasCodiBarresServicaixa());
		PreferencesGesblue.saveValorsServiCaixa(mContext, nt.getValorservicaixa());
		PreferencesGesblue.saveTextAnulacio(mContext, nt.hasTextAnulacio());
		PreferencesGesblue.saveContingutTextAnulacio(mContext, nt.getContinguttextanulacio());
		PreferencesGesblue.saveCostLimitAnulacio(mContext, nt.hasCostLimitAnulacio());
		PreferencesGesblue.saveDataImportTiquet(mContext, nt.hasDataImportTiquet());
		PreferencesGesblue.savePrecepteInfringit(mContext, nt.getPrecepteinfringit());
		PreferencesGesblue.saveLongitudInfraccio(mContext, nt.getLongitudinfraccio());
		PreferencesGesblue.saveTiquetUsuari(mContext, nt.hasTiquetUsuari());
		PreferencesGesblue.savePrefCodiExportadora(mContext, nt.hasCodiExportadora());
		PreferencesGesblue.savePrefCodiTipusButlleta(mContext, nt.hasCodiTipusButlleta());
		PreferencesGesblue.savPrefCodiInstitucio(mContext, nt.hasCodiInstitucio());




	}
	private void cridaLogin(final String username, final String password, final long concessio, @Nullable final String _data) {
		//Log.e("Login -0",concessio+"------");

		showLoadingAnimButton(true);
		//mContext=null;
		Long data =  Long.parseLong(PreferencesGesblue.getDataSync(mContext));

		//Log.e("Login 0",concessio+"------"+data);
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(mContext.CONNECTIVITY_SERVICE);
		if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			//we are connected to a network
					//Log.e("Login 1","xXx");


			DatamanagerAPI.crida_Login(new LoginRequest(username, password, concessio, Utils.getDeviceId(mContext), Utils.getAndroidVersion(), Utils.getAppVersion(mContext), data), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {

					try {
						response = DatamanagerAPI.parseJson(result, LoginResponse.class);

					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					//Log.d("Login 2","xxX");
					showLoadingAnimButton(false);
					enableEditTexts(true);

					switch((int) response.getResultat()) {
						case 0:

							PreferencesGesblue.setCodiAgent(mContext, Long.parseLong(response.getCodiagent()));
							PreferencesGesblue.saveIdAgent(mContext, response.getAgent());


							/*Model_Log model_log = new Model_Log();
							model_log.setFechalog("");
							model_log.setConcessiolog(String.valueOf(concessio));
							model_log.setIdagent(Integer.parseInt(response.getCodiagent()));
							model_log.setCodiacciolog(1);//Login
							model_log.setVersioapp(Utils.getAppVersion(mContext));
							model_log.setInfo("");
							model_log.setEnviat(0);

							final ArrayList<Model_Log> arrayLogs = new ArrayList<Model_Log>();

							arrayLogs.add(model_log);

							DatabaseAPI.insertLogs(mContext,arrayLogs);

							DatabaseAPI.insertLogs(mContext,arrayLogs);

							DatabaseAPI.insertLogs(mContext,arrayLogs);

							final ArrayList<Model_Log> listLogs = DatabaseAPI.getLogs(mContext);

							Log.d("Num logs locals",""+listLogs.size());*/



							if(isNoLoginConcessio) {

								//LOG SENSE CONCESSIO PREVIA


								Intent intent = new Intent(mContext, MainActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("adm",adm);
								startActivity(intent);
								finish();



							}
							else {


								String d = PreferencesGesblue.getDataSync(mContext);
								sincronitzarTot(response, concessio, d);
							}

							break;
						case -1: //Error de login
							Utils.showCustomDatamanagerError(mContext, getString(R.string.errorLogin));
							enableEditTexts(true);
							if(progress != null && progress.isShowing()) progress.dismiss();
							break;
						case -2: //Terminal pendent
							Utils.showCustomDatamanagerError(mContext, getString(R.string.terminalPendent));
							enableEditTexts(true);
							if(progress != null && progress.isShowing()) progress.dismiss();
							break;
						case -3: //Terminal denegat
							Utils.showCustomDatamanagerError(mContext, getString(R.string.terminalDenegat));
							enableEditTexts(true);
							if(progress != null && progress.isShowing()) progress.dismiss();
							break;
						default:
							Utils.showCustomDatamanagerError(mContext, getString(R.string.otherError));
							enableEditTexts(true);
							if(progress != null && progress.isShowing()) progress.dismiss();
					}
				}

				@Override
				public void onError(final int error) {
					showLoadingAnimButton(false);
					enableEditTexts(true);
					if(isRunning()) {
						try {
							Utils.showDatamanagerError(mContext, error);
						} catch (Exception ex) {
							ELog(ex);
						}
					}
				}
			});
		}
		else{
			//Not Connected
			Model_Agent agent = DatabaseAPI.findAgent(mContext,username,password);
			if(agent==null){
				Utils.showCustomDatamanagerError(mContext, getString(R.string.errorLogin));
				enableEditTexts(true);
				progress.dismiss();
				showLoadingAnimButton(false);
			}
			else{
				PreferencesGesblue.setCodiAgent(mContext, Long.parseLong(agent.getIdentificador()));
				PreferencesGesblue.saveIdAgent(mContext, agent.getCodiagent());


				Model_Log model_log = new Model_Log();
				model_log.setFechalog("");
				model_log.setConcessiolog(String.valueOf(concessio));
				model_log.setIdagent((int)agent.getCodiagent());
				model_log.setCodiacciolog(1);//Login
				model_log.setVersioapp(Utils.getAppVersion(mContext));
				model_log.setInfo("");
				model_log.setEnviat(0);

				final ArrayList<Model_Log> arrayLogs = new ArrayList<Model_Log>();

				arrayLogs.add(model_log);

				DatabaseAPI.insertLogs(mContext,arrayLogs);

				DatabaseAPI.insertLogs(mContext,arrayLogs);

				DatabaseAPI.insertLogs(mContext,arrayLogs);

				final ArrayList<Model_Log> listLogs = DatabaseAPI.getLogs(mContext);

				//Log.e("Num logs locals",""+listLogs.size());

				Intent intent = new Intent(mContext, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("adm",adm);
				startActivity(intent);
			}
		}

	}
	private void sincronitzarTot(LoginResponse loginResponse, long concessio, String _data) {
		if(antirepetidorDADES) {
			antirepetidorDADES=false;
			DatabaseAPI.deleteAllAgents(mContext);
			DatabaseAPI.deleteAllMarques(mContext);
			DatabaseAPI.deleteAllModels(mContext);
			DatabaseAPI.deleteAllTipusVehicles(mContext);
			DatabaseAPI.deleteAllTipusAnulacions(mContext);
			DatabaseAPI.deleteAllCarrers(mContext);
			DatabaseAPI.deleteAllInfraccions(mContext);
			DatabaseAPI.deleteAllZones(mContext);
			DatabaseAPI.deleteAllLlistaBlanca(mContext);

			sincronitzarAgents(loginResponse, concessio, _data);

			if(isRunning()) {

				try {
					progress.show();

				} catch (Exception bte) {

					ELog(bte);
					//Tenir un progress d'aquesta manera és molt mala idea i només dona problemes
					//TODO: Tenir *UN* progress per a cada funció, si és que el progress de veritat cal
				}
			}
		}


	}
	private void sincronitzarAgents(final LoginResponse loginResponse, final long concessio, final String _data)
	{
		if(loginResponse == null || loginResponse.showAgents()) {
			progress.setMessage(getString(R.string.actualitzantAgents));
			Log.i("Actualitzant","AGENTS");
			String data = PreferencesGesblue.getDataSync(mContext);
			if(!TextUtils.isEmpty(_data)) data = _data;
			DatamanagerAPI.crida_Agents(new AgentsRequest(concessio, data), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					//Log.d("Resultat agents:",result);
					AgentsResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, AgentsResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					LinkedList<Model_Agent> list = new LinkedList<>();
					for (AgentsResponse.Agent agent : response.getAgents()) {
						list.add(new Model_Agent(Long.parseLong(agent.getCodi()),"","",agent.getLogin(),agent.getPassword(),"",agent.getCodiagent()));
					}

					DatabaseAPI.insertAgents(mContext, list);

					sincronitzarMarques(loginResponse, concessio, _data);

				}

				@Override
				public void onError(int error) {
					Utils.showDatamanagerError(mContext, error);
					everythingIsOk = false;
					sincronitzarMarques(loginResponse, concessio, _data);
				}
			});
		} else {
			sincronitzarMarques(loginResponse, concessio, _data);
		}
	}
	private void sincronitzarMarques(final LoginResponse loginResponse, final long concessio, final String _data) {
		if(loginResponse == null || loginResponse.showMarques()) {
			progress.setMessage(getString(R.string.actualitzantMarques));

			Log.i("Actualitzant","Marques");
			String data = PreferencesGesblue.getDataSync(mContext);
			if(!TextUtils.isEmpty(_data)) data = _data;
			DatamanagerAPI.crida_Marques(new MarquesRequest(data), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					MarquesResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, MarquesResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					LinkedList<Model_Marca> list = new LinkedList<>();
					PreferencesGesblue.setMarcaDefaultValue(mContext, response.getDefaultValue());
					for (MarquesResponse.Marca marca : response.getMarques()) {
						if (marca.isEliminar()) {
							DatabaseAPI.deleteMarca(mContext, marca.getCodi());
						} else {
							list.add(new Model_Marca(marca.getCodi(), new Date(), marca.getNom(), marca.getLogo()));
						}
					}

					DatabaseAPI.insertMarques(mContext, list);

					sincronitzarModels(loginResponse, concessio, _data);

				}

				@Override
				public void onError(int error) {
					Utils.showDatamanagerError(mContext, error);
					everythingIsOk = false;
					sincronitzarModels(loginResponse, concessio, _data);
				}
			});
		} else {
			sincronitzarModels(loginResponse, concessio, _data);
		}
	}
	private void sincronitzarModels(final LoginResponse loginResponse, final long concessio, final String _data) {
		if(loginResponse == null || loginResponse.showColors()) {
			progress.setMessage(getString(R.string.actualitzantModels));

			Log.i("Actualitzant","Models");
			String data = PreferencesGesblue.getDataSync(mContext);
			if(!TextUtils.isEmpty(_data)) data = _data;
			DatamanagerAPI.crida_Models(new ModelsRequest(data), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					ModelsResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, ModelsResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					LinkedList<Model_Model> list = new LinkedList<>();
					PreferencesGesblue.setModelDefaultValue(mContext, response.getDefaultValue());
					for(Models model : response.getModels()) {
						if(model.isEliminar()) {
							DatabaseAPI.deleteColor(mContext, model.getCodi());
						} else {
							list.add(new Model_Model(model.getCodi(), model.getMarca(), model.getNom()));
						}
					}
					DatabaseAPI.insertModels(mContext, list);
					sincronitzarColors(loginResponse, concessio, _data);
				}

				@Override
				public void onError(int error) {
					sincronitzarColors(loginResponse, concessio, _data);
					Utils.showDatamanagerError(mContext, error);
					everythingIsOk = false;
				}
			});
		} else {
			sincronitzarColors(loginResponse, concessio, _data);
		}
	}
	private void sincronitzarColors(final LoginResponse loginResponse, final long concessio, final String _data) {
		if(loginResponse == null || loginResponse.showModels()) {
			progress.setMessage(getString(R.string.actualitzantColors));

			Log.i("Actualitzant","Colors");
			String data = PreferencesGesblue.getDataSync(mContext);
			if(!TextUtils.isEmpty(_data)) data = _data;
			DatamanagerAPI.crida_Colors(new ColorsRequest(Long.parseLong(data)), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					ColorsResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, ColorsResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}
					LinkedList<Model_Color> list = new LinkedList<>();
					PreferencesGesblue.setColorDefaultValue(mContext, response.getDefaultValue());
					for(ColorsResponse.Colors color : response.getColors()) {
						if(color.isEliminar()) {
							DatabaseAPI.deleteModel(mContext, color.getCodi());
						} else {
							list.add(new Model_Color(color.getCodi(), color.getNomcat(), color.getNomes(), color.getNomeng(), color.getRgb()));
						}
					}
					DatabaseAPI.insertColors(mContext, list);
					sincronitzarTipusVehicles(loginResponse, concessio, _data);
				}

				@Override
				public void onError(int error) {
					sincronitzarTipusVehicles(loginResponse, concessio, _data);
					Utils.showDatamanagerError(mContext, error);
					everythingIsOk = false;
				}
			});
		} else {
			sincronitzarTipusVehicles(loginResponse, concessio, _data);
		}
	}
	private void sincronitzarTipusVehicles(final LoginResponse loginResponse, final long concessio, final String _data) {
		if(loginResponse == null || loginResponse.showTipusVehicles()) {
			progress.setMessage(getString(R.string.actualitzantTipusVehicles));

			Log.i("Actualitzant","Tipus Vehicles");

			String data = PreferencesGesblue.getDataSync(mContext);
			if(!TextUtils.isEmpty(_data)) data = _data;
			DatamanagerAPI.crida_TipusVehicles(new TipusVehiclesRequest(data), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					TipusVehiclesResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, TipusVehiclesResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					LinkedList<Model_TipusVehicle> list = new LinkedList<>();
					PreferencesGesblue.setTipusVehicleDefaultValue(mContext, response.getDefaultValue());
					for(Tipus_Vehicle tipus_vehicle : response.getTipusvehicles()) {
						if(tipus_vehicle.isEliminar()) {
							DatabaseAPI.deleteTipusVehicle(mContext, TextUtils.isEmpty(tipus_vehicle.getCodi()) ? 0 : Long.parseLong(tipus_vehicle.getCodi()));
						} else {
							list.add(new Model_TipusVehicle((TextUtils.isEmpty(tipus_vehicle.getCodi()) ? 0 : Long.parseLong(tipus_vehicle.getCodi())), tipus_vehicle.getNomcat(), tipus_vehicle.getNomes(), tipus_vehicle.getNomeng(), tipus_vehicle.getNomfr()));
						}
					}
					DatabaseAPI.insertTipusVehicles(mContext, list);
					sincronitzarZones(loginResponse, concessio, _data);
				}

				@Override
				public void onError(int error) {
					sincronitzarZones(loginResponse, concessio, _data);
					Utils.showDatamanagerError(mContext, error);
					everythingIsOk = false;
				}
			});
		} else {
			sincronitzarZones(loginResponse, concessio, _data);
		}
	}
	private void sincronitzarZones(final LoginResponse loginResponse, final long concessio, final String _data) {
		if(loginResponse == null || loginResponse.showZones()) {
			progress.setMessage(getString(R.string.actualitzantZones));

			Log.i("Actualitzant","Zones");
			String data = PreferencesGesblue.getDataSync(mContext);
			if(!TextUtils.isEmpty(_data)) data = _data;
			DatamanagerAPI.crida_Zones(new ZonesRequest(concessio, Long.parseLong(data)), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					ZonesResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, ZonesResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					LinkedList<Model_Zona> list = new LinkedList<>();
					PreferencesGesblue.setZonaDefaultValue(mContext, response.getDefaultValue());
					for(ZonesResponse.Zones zona : response.getZones()) {
						if(zona.isEliminar()) {
							DatabaseAPI.deleteZona(mContext, zona.getCodi());
						} else {
							list.add(new Model_Zona(TextUtils.isEmpty(zona.getCodi()) ? 0 : Long.parseLong(zona.getCodi()), zona.getNom()));
						}
					}
					BasicDBResult dbResult = DatabaseAPI.insertZones(mContext, list);
					if(!dbResult.isOk()) {
						DLog(dbResult.getStrMsg());
					}
					sincronitzarCarrers(loginResponse, concessio, _data);
				}

				@Override
				public void onError(int error) {
					Utils.showDatamanagerError(mContext, error);
					everythingIsOk = false;
					sincronitzarCarrers(loginResponse, concessio, _data);
				}
			});
		} else {
			sincronitzarCarrers(loginResponse, concessio, _data);
		}
	}
	private void sincronitzarCarrers(final LoginResponse loginResponse, final long concessio, final String _data) {
		if(loginResponse == null || loginResponse.showCarrers()) {
			progress.setMessage(getString(R.string.actualitzantCarrers));

			Log.i("Actualitzant","Carrers");
			String data = PreferencesGesblue.getDataSync(mContext);
			if(!TextUtils.isEmpty(_data)) data = _data;
			DatamanagerAPI.crida_Carrers(new CarrersRequest(concessio, Long.parseLong(data)), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					CarrersResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, CarrersResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					LinkedList<Model_Carrer> list = new LinkedList<>();
					PreferencesGesblue.setCarrerDefaultValue(mContext, response.getDefaultValue());
					for(CarrersResponse.Carrers carrer : response.getCarrers()) {
						if(carrer.isEliminar()) {
							DatabaseAPI.deleteCarrer(mContext, carrer.getCodi());
						} else {
							list.add(new Model_Carrer(TextUtils.isEmpty(carrer.getCodi()) ? 0 : Long.parseLong(carrer.getCodi()), carrer.getNom(),carrer.getZona()));
						}
					}
					BasicDBResult dbResult = DatabaseAPI.insertCarrers(mContext, list);
					if(!dbResult.isOk()) {
						DLog(dbResult.getStrMsg());
					}
					sincronitzarInfraccions(loginResponse, concessio, _data);
				}

				@Override
				public void onError(int error) {
					Utils.showDatamanagerError(mContext, error);
					everythingIsOk = false;
					sincronitzarInfraccions(loginResponse, concessio, _data);
				}
			});
		} else {
			sincronitzarInfraccions(loginResponse, concessio, _data);
		}
	}
	private void sincronitzarInfraccions(final LoginResponse loginResponse, final long concessio, final String _data) {
		if(loginResponse == null || loginResponse.showInfraccions()) {
			progress.setMessage(getString(R.string.actualitzantInfraccions));

			Log.i("Actualitzant","Infraccions");
			String data = PreferencesGesblue.getDataSync(mContext);
			if(!TextUtils.isEmpty(_data)) data = _data;
			DatamanagerAPI.crida_Infraccions(new InfraccionsRequest(concessio, data), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					InfraccionsResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, InfraccionsResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					LinkedList<Model_Infraccio> list = new LinkedList<>();
					PreferencesGesblue.setInfraccioDefaultValue(mContext, response.getDefaultValue());
					for(InfraccionsResponse.Infraccions infraccio : response.getInfraccions()) {
						if(infraccio.isEliminar()) {
							DatabaseAPI.deleteInfraccio(mContext, Long.parseLong(infraccio.getCodi()));
						} else {
							list.add(new Model_Infraccio(TextUtils.isEmpty(infraccio.getCodi()) ? 0 : Long.parseLong(infraccio.getCodi()), infraccio.getNom(), infraccio.getImporte(), infraccio.getAnulacio(), infraccio.getTempsanulacio(), infraccio.getAnulacio2(), infraccio.getTempsanulacio2(), infraccio.getPrecepte(),infraccio.getZona()));
						}
					}
					DatabaseAPI.insertInfraccions(mContext, list);
					sincronitzarLlistaBlanca(loginResponse, concessio, _data);
				}

				@Override
				public void onError(int error) {
					Utils.showDatamanagerError(mContext, error);
					everythingIsOk = false;
					sincronitzarLlistaBlanca(loginResponse, concessio, _data);
					mBinding.viewSwitcherLoginAnim.showNext();
				}
			});
		} else {
			sincronitzarLlistaBlanca(loginResponse, concessio, _data);
		}
	}
	private void sincronitzarLlistaBlanca(final LoginResponse loginResponse, final long concessio, final String _data) {

			progress.setMessage(getString(R.string.actualitzantLlista));

			Log.i("Actualitzant","Llista Blanca");
			String data = PreferencesGesblue.getDataSync(mContext);
			if(!TextUtils.isEmpty(_data)) data = _data;
			DatamanagerAPI.crida_LlistaBlanca(new LlistaBlancaRequest(concessio, data), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					LlistaBlancaResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, LlistaBlancaResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					LinkedList<Model_LlistaBlanca> list = new LinkedList<>();
					for(LlistaBlancaResponse.LlistaBlanca llistablanca : response.getLlistablanca()) {
						if(llistablanca.isEliminar()) {
							DatabaseAPI.deleteLlistaBlanca(mContext, Long.parseLong(llistablanca.getCodi()));
						} else {
							list.add(new Model_LlistaBlanca(TextUtils.isEmpty(llistablanca.getCodi()) ? 0 : Long.parseLong(llistablanca.getCodi()), llistablanca.getMatricula()));
						}
					}
					DatabaseAPI.inserLlistaBlanca(mContext, list);
                    sincronitzarLlistaAbonats(loginResponse, concessio, _data);
				}

				@Override
				public void onError(int error) {
					Utils.showDatamanagerError(mContext, error);
					everythingIsOk = false;
                    sincronitzarLlistaAbonats(loginResponse, concessio, _data);
					mBinding.viewSwitcherLoginAnim.showNext();
				}
			});
	}
    private void sincronitzarLlistaAbonats(final LoginResponse loginResponse, final long concessio, final String _data) {

        progress.setMessage(getString(R.string.actualitzantLlista));

		Log.i("Actualitzant","Abonats");
        String data = PreferencesGesblue.getDataSync(mContext);
        if(!TextUtils.isEmpty(_data)) data = _data;
        DatamanagerAPI.crida_LlistaAbonats(new LlistaAbonatsRequest(concessio, data), new JSoapCallback() {
            @Override
            public void onSuccess(String result) {
                LlistaAbonatsResponse response;
                try {
                    response = DatamanagerAPI.parseJson(result, LlistaAbonatsResponse.class);
                } catch (Exception ex) {
                    ELog(ex);
                    onError(PARSE_ERROR);
                    return;
                }

                LinkedList<Model_LlistaAbonats> list = new LinkedList<>();
                for(LlistaAbonatsResponse.LlistaAbonats llistaabonats : response.getLlistaabonats()) {
                    if(llistaabonats.isEliminar()) {
                        DatabaseAPI.deleteLlistaAbonats(mContext, Long.parseLong(llistaabonats.getCodi()));
                    } else {
                        list.add(new Model_LlistaAbonats(TextUtils.isEmpty(llistaabonats.getCodi()) ? 0 : Long.parseLong(llistaabonats.getCodi()), llistaabonats.getMatricula(),llistaabonats.getDatainici(),llistaabonats.getDatafi()));
                    }
                }
                DatabaseAPI.insertLlistaAbonats(mContext, list);
                sincronitzarComptadorDenuncia();
            }

            @Override
            public void onError(int error) {
                Utils.showDatamanagerError(mContext, error);
                everythingIsOk = false;
                sincronitzarComptadorDenuncia();
                mBinding.viewSwitcherLoginAnim.showNext();
            }
        });
    }
	private void sincronitzarComptadorDenuncia() {
		progress.setMessage(getString(R.string.actualitzant));

			Log.d("ppp",""+concessio+ "- "+PreferencesGesblue.getTerminal(mContext)+" - "+PreferencesGesblue.getAgentId(mContext));
			DatamanagerAPI.crida_RecuperaComptadorDenuncia(new RecuperaComptadorDenunciaRequest(concessio, PreferencesGesblue.getTerminal(mContext), PreferencesGesblue.getAgentId(mContext)), new JSoapCallback() {
				@Override
				public void onSuccess(String result) {
					if(progress != null && progress.isShowing()) progress.dismiss();
					DLog("Entro al onSuccess de crida_RecuperaComptadorDenuncia");
					RecuperaComptadorDenunciaResponse response;
					try {
						response = DatamanagerAPI.parseJson(result, RecuperaComptadorDenunciaResponse.class);
					} catch (Exception ex) {
						ELog(ex);
						onError(PARSE_ERROR);
						return;
					}

					int comptadorLocal = PreferencesGesblue.getComptadorDenuncia(mContext);
					int comptadorServer = TextUtils.isEmpty(response.getComptador()) ? 0 : Integer.parseInt(response.getComptador());

					DLog("comptadorLocal: "+comptadorLocal);
					DLog("comptadorServer: "+comptadorServer);
					if(comptadorServer > comptadorLocal) {
						PreferencesGesblue.saveComptadorDenuncia(mContext, comptadorServer);
						if(isNoLoginConcessio) {
							cridaLogin(PreferencesGesblue.getUserName(mContext), PreferencesGesblue.getPassword(mContext), Long.parseLong(concessio), initialDate);
						} else {
							if(!refreshDades) {

								Intent intent = new Intent(mContext, MainActivity.class);

								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
							else{
								refreshDades = false;
							}
						}
					} else {
						crida_establirComptadorDenuncia();
					}
					DLog("comptador final: "+PreferencesGesblue.getComptadorDenuncia(mContext));
					DLog("Surto del onSuccess de crida_RecuperaComptadorDenuncia");

				}

				@Override
				public void onError(int error) {
					if(progress != null && progress.isShowing()) progress.dismiss();
					DLog("Entro al error de crida_RecuperaComptadorDenuncia");
					crida_establirComptadorDenuncia();
					showLoadingAnimButton(false);
					enableEditTexts(true);

					Utils.showDatamanagerError(mContext, error);
					DLog("Surto del onError de crida_RecuperaComptadorDenuncia");

				}
			});

	}
	private void crida_establirComptadorDenuncia() {
		PreferencesGesblue.saveDataSync(mContext,Utils.getCurrentTimeStringShort(mContext));
		Log.d("DataSync",""+PreferencesGesblue.getDataSync(mContext));
		Log.d("PARAMS",""+Long.parseLong(concessio)+" - "+PreferencesGesblue.getTerminal(mContext)+" - "+PreferencesGesblue.getAgentId(mContext)+" - "+PreferencesGesblue.getComptadorDenuncia(mContext));
		DatamanagerAPI.crida_EstablirComptadorDenuncia(new EstablirComptadorDenunciaRequest(Long.parseLong(concessio), PreferencesGesblue.getTerminal(mContext), PreferencesGesblue.getAgentId(mContext), PreferencesGesblue.getComptadorDenuncia(mContext)), new JSoapCallback() {
			@Override
			public void onSuccess(String result) {
				DLog("Entro al onSuccess de crida_establirComptadorDenuncia");
				EstablirComptadorDenunciaResponse responseComptadorDenuncia;
				try {
					responseComptadorDenuncia = DatamanagerAPI.parseJson(result, EstablirComptadorDenunciaResponse.class);
					if(responseComptadorDenuncia.getResultat()>0){
						PreferencesGesblue.saveComptadorDenuncia(mContext, (int)responseComptadorDenuncia.getResultat());
					}

				} catch (Exception ex) {
					ELog(ex);
					onError(PARSE_ERROR);
					return;
				}
				//PreferencesGesblue.saveComptadorDenuncia(mContext, (int)response.getResultat());
				DLog("saveComptadorDenuncia: "+(int)responseComptadorDenuncia.getResultat());

				DLog("Surto del onSuccess de crida_establirComptadorDenuncia");



				if(progress != null && progress.isShowing()) progress.dismiss();
				if(!refreshDades) {
					if (isNoLoginConcessio) {

						cridaLogin(PreferencesGesblue.getUserName(mContext), PreferencesGesblue.getPassword(mContext), Long.parseLong(concessio), initialDate);
					} else {
						if(response.getResultat()==0) {

							if (antirepetidor) {
								antirepetidor = false;
								Intent intent = new Intent(mContext, MainActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("adm", adm);
								startActivity(intent);
								finish();
							}

						}
					}
				}
				else{
					refreshDades = false;
				}
			}

			@Override
			public void onError(int error) {
				if(progress != null && progress.isShowing()) progress.dismiss();
				DLog("Entro al onError de crida_establirComptadorDenuncia");
				showLoadingAnimButton(false);
				enableEditTexts(true);
				if(isNoLoginConcessio) {
					cridaLogin(PreferencesGesblue.getUserName(mContext), PreferencesGesblue.getPassword(mContext), Long.parseLong(concessio), initialDate);
				} else {

					Intent intent = new Intent(mContext, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}


//                Utils.showDatamanagerError(mContext, error);
				DLog("Surto del onError de crida_establirComptadorDenuncia");
			}
		});
	}



	/*EL RETORN**/
   	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String result="";
		adm=false;
		setRunning(true);
		// check that it is the SecondActivity with an OK result
		if (requestCode == RequestCode) {
			if (resultCode == RESULT_OK) {

				if (data.getExtras().getBoolean("adm")) {
					adm = data.getExtras().getBoolean("adm");
				}
				checkAdmin(adm);

				if (data.getExtras().getString("result") != null) {



					result=data.getExtras().getString("result");



					if (result.equals("refresh")) {


						refreshDades = true;
						DatabaseAPI.deleteAllAgents(mContext);
						DatabaseAPI.deleteAllMarques(mContext);
						DatabaseAPI.deleteAllModels(mContext);
						DatabaseAPI.deleteAllTipusVehicles(mContext);
						DatabaseAPI.deleteAllTipusAnulacions(mContext);
						DatabaseAPI.deleteAllCarrers(mContext);
						DatabaseAPI.deleteAllInfraccions(mContext);
						DatabaseAPI.deleteAllZones(mContext);
						DatabaseAPI.deleteAllLlistaBlanca(mContext);
						LoginResponse responseManual = new LoginResponse(0,1,"1",1,1,1,1,1,1,1,1);
						sincronitzarTot(responseManual,Long.parseLong(concessio),"0");






					}
					else if(result.equals("unlog")){


						PreferencesGesblue.setConcessioString(mContext,"");
						PreferencesGesblue.setConcessio(mContext,0);
						PreferencesGesblue.saveDataSync(mContext,"0");

						mBinding.textViewConcessio.setVisibility(View.VISIBLE);
						mBinding.editTextConcessio.setVisibility(View.VISIBLE);
						localitzacio.setText(getString(R.string.no_concessio));

						isNoLoginConcessio = true;

						DatabaseAPI.deleteAllAgents(mContext);
						DatabaseAPI.deleteAllMarques(mContext);
						DatabaseAPI.deleteAllModels(mContext);
						DatabaseAPI.deleteAllTipusVehicles(mContext);
						DatabaseAPI.deleteAllTipusAnulacions(mContext);
						DatabaseAPI.deleteAllCarrers(mContext);
						DatabaseAPI.deleteAllInfraccions(mContext);
						DatabaseAPI.deleteAllZones(mContext);
						DatabaseAPI.deleteAllLlistaBlanca(mContext);
					}
				}




			}

		}
		ContadorDenuncies(false);







	}

	/*ES BORRA EL PAKAGE**/
		private void clearAppData() {
			try {
				// clearing app data
				if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {




					((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!


				} else {



					String packageName = getApplicationContext().getPackageName();
					Runtime runtime = Runtime.getRuntime();
					runtime.exec("pm clear "+packageName);


				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private class CustomDialogClass extends Dialog implements
				android.view.View.OnClickListener {

			public Activity c;
			public Dialog d;
			public Button yes, no;
			public Boolean result=false;

			public CustomDialogClass(Activity a) {
				super(a);
				// TODO Auto-generated constructor stub
				this.c = a;
			}

			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				setContentView(R.layout.custom_dialog_delete_all);
				yes = (Button) findViewById(R.id.btn_yes);
				no = (Button) findViewById(R.id.btn_no);
				yes.setOnClickListener(this);
				no.setOnClickListener(this);

			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.btn_yes:
						DatabaseAPI.deleteAllDenuncies(mContext);
						PreferencesGesblue.clearFormulari(mContext);
						//File path = new File("storage/emulated/0/Sixtemia");
						File path = new File("storage/emulated/0/Boka2");
						BorratRecursiu(path);
						path = new File("storage/emulated/0/Sixtemia");
						BorratRecursiu(path);
						clearAppData();
						break;
					case R.id.btn_no:
						result=false;
						dismiss();
						break;
					default:
						result=false;
						break;
				}
				dismiss();
			}
		}


		void BorratRecursiu(File fileOrDirectory) {
			if (fileOrDirectory.isDirectory())
				for (File child : fileOrDirectory.listFiles())
					BorratRecursiu(child);

			fileOrDirectory.delete();
		}


}
