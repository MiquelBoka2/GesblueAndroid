package com.boka2.gesblue.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.boka2.gesblue.Boka2ols.BK_Utils;
import com.boka2.gesblue.R;
import com.boka2.gesblue.databinding.ActivityLoginBinding;
import com.boka2.gesblue.datamanager.DatabaseAPI;
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;
import com.boka2.gesblue.datamanager.webservices.DatamanagerAPI;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.NovaDenunciaRequest;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.PujaFotoRequest;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.LoginResponse;
import com.boka2.gesblue.datamanager.webservices.results.operativa.NovaDenunciaResponse;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.global.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

import pt.joaocruz04.lib.misc.JSoapCallback;

import static com.boka2.gesblue.GesblueApplication.DenunciaEnCurs;
import static com.boka2.gesblue.GesblueApplication.EnviamentDisponible;
import static pt.joaocruz04.lib.misc.JsoapError.PARSE_ERROR;

public class Opcions extends AppCompatActivity {

    private ActivityLoginBinding mBinding;
    private boolean refreshDades;
    LoginResponse responseManual;
    
    private ConstraintLayout Logout_Refrescar_Dades,Canviar_Concessio,Desconectat, Recarregar_Dades, Reimpressio, Idioma, Enviaments_Pendents,Pujar_Fotos,Admin,E_UUID,E_TimeOut,Extres,Base,Mode_Offline,Capçalera;
    private TextView txt_Versio,txtNumDenuncies;
    private Button btn_Confirmar;
    private Context oContext=this;
    private String estat="";
    private Boolean adm=false;
    private ImageView  img_Lock,img_Unlock,save_uuid,save_timeout,imgCercleContador;
    private EditText edt_uuid,edit_timeout;
    private Switch Switch_Offline;

    private int RequestCode=0002;



    private ProgressDialog progress, liniar_progress;
    private List<Model_Denuncia> denunciesPendents;
    private int intentsEnviaDenuncia =0;
    private boolean escapador=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_opcions);
        DenunciaEnCurs=false;
        EnviamentDisponible=true;
        setContentView(R.layout.activity_opcions);

        /*DECLAREM LES PARTS**/{

            Base = (ConstraintLayout) findViewById(R.id.lay_Base);

            txt_Versio = (TextView) findViewById(R.id.txt_Versio);

            Capçalera= (ConstraintLayout) findViewById(R.id.Capçalera);

            imgCercleContador= (ImageView) findViewById(R.id.imgCercleContador);

            Logout_Refrescar_Dades = (ConstraintLayout) findViewById(R.id.lay_Logout_Refrescar_Dades);
            Canviar_Concessio = (ConstraintLayout) findViewById(R.id.lay_canviarConcessio);
            Desconectat = (ConstraintLayout) findViewById(R.id.lay_Desconectat);
            Recarregar_Dades = (ConstraintLayout) findViewById(R.id.lay_RecarregarDades);
            Reimpressio = (ConstraintLayout) findViewById(R.id.lay_Reimpressio);
            Idioma = (ConstraintLayout) findViewById(R.id.lay_Idioma);
            Enviaments_Pendents = (ConstraintLayout) findViewById(R.id.lay_EnviamentsPendents);
            Mode_Offline= (ConstraintLayout) findViewById(R.id.lay_offline);

            E_UUID = (ConstraintLayout) findViewById(R.id.lay_edit_uuid);
            E_TimeOut = (ConstraintLayout) findViewById(R.id.lay_edit_timeout);
            Pujar_Fotos=(ConstraintLayout) findViewById(R.id.lay_PujarFotos);
            Admin = (ConstraintLayout) findViewById(R.id.lay_Admin);

            Switch_Offline=(Switch)findViewById(R.id.sw_offline);

            save_uuid=(ImageView) findViewById(R.id.img_save_uuid);
            edt_uuid=(EditText) findViewById(R.id.edt_uuid);

            save_timeout=(ImageView) findViewById(R.id.img_save_timeout);
            edit_timeout=(EditText) findViewById(R.id.edt_timeout);

            img_Lock = (ImageView) findViewById(R.id.img_Admin_close);
            img_Unlock = (ImageView) findViewById(R.id.img_Admin_open);
            btn_Confirmar = (Button) findViewById(R.id.btn_Confirmar_Opcions);

            txtNumDenuncies = (TextView) findViewById(R.id.txt_NumDenuncies);


            Canviar_Concessio.setVisibility(View.GONE);
            Desconectat.setVisibility(View.GONE);
            Recarregar_Dades.setVisibility(View.GONE);
            Reimpressio.setVisibility(View.GONE);
            Idioma.setVisibility(View.GONE);
            Enviaments_Pendents.setVisibility(View.GONE);
            Mode_Offline.setVisibility(View.GONE);
            E_UUID.setVisibility(View.GONE);
            E_TimeOut.setVisibility(View.GONE);
            Pujar_Fotos.setVisibility(View.GONE);
            Admin.setVisibility(View.GONE);
        }

        /*TEXT DE LA VERSIO*/{
            String menuTitle = "";
            try {
                PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                menuTitle = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            txt_Versio.setText("v."+menuTitle);
        }

        /*EStAT DEL OFFLINE*/{
            Boolean offline= PreferencesGesblue.getOffline(oContext);
            Switch_Offline.setChecked(offline);
        }



        /*RECUPAREM DADES D'ON PROVENIM**/{
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                estat = extras.getString("estat", "");
                adm=extras.getBoolean("adm");

            }

        }



        /*PROPIETATS DE LA BARRA DE PROGRES*/{
        liniar_progress = new ProgressDialog(this);
        liniar_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        liniar_progress.setCancelable(false);}


        //Obtenir si hi ha denuncies Pendents
        ContadorDenuncies(true);



        /*ESTATS DE PROCEDENCIA**/{
            if(estat.equals("main")){

                Canviar_Concessio.setVisibility(View.GONE);
                Recarregar_Dades.setVisibility(View.GONE);
                Pujar_Fotos.setVisibility(View.GONE);
                E_UUID.setVisibility(View.GONE);
                E_TimeOut.setVisibility(View.GONE);

                Logout_Refrescar_Dades.setVisibility(View.VISIBLE);
                Mode_Offline.setVisibility(View.VISIBLE);
                Desconectat.setVisibility(View.VISIBLE);
                Reimpressio.setVisibility(View.VISIBLE);
                Idioma.setVisibility(View.VISIBLE);
                //Comprovem si tenim denuncies pendents i en cas negatiu amagem la opcio.
                if(denunciesPendents==null || denunciesPendents.size()<=0 || denunciesPendents.isEmpty()){

                    Enviaments_Pendents.setVisibility(View.GONE);
                }
                else{

                    Enviaments_Pendents.setVisibility(View.VISIBLE);
                    imgCercleContador.setVisibility(View.VISIBLE);
                    txtNumDenuncies.setVisibility(View.VISIBLE);

                    txtNumDenuncies.setText(denunciesPendents.size()+"");

                }

                Admin.setVisibility(View.VISIBLE);

            }
            else if(estat.equals("no_login_concessio")){


                Idioma.setVisibility(View.VISIBLE);
                Admin.setVisibility(View.VISIBLE);
                Mode_Offline.setVisibility(View.VISIBLE);


                Canviar_Concessio.setVisibility(View.GONE);
                Recarregar_Dades.setVisibility(View.GONE);
                Desconectat.setVisibility(View.GONE);
                Reimpressio.setVisibility(View.GONE);
                Enviaments_Pendents.setVisibility(View.GONE);
                Logout_Refrescar_Dades.setVisibility(View.GONE);


            }
            else if (estat.equals("login_concessio")){

                Canviar_Concessio.setVisibility(View.VISIBLE);
                Recarregar_Dades.setVisibility(View.VISIBLE);
                Admin.setVisibility(View.VISIBLE);
                Idioma.setVisibility(View.VISIBLE);
                Mode_Offline.setVisibility(View.VISIBLE);


                Desconectat.setVisibility(View.GONE);
                Reimpressio.setVisibility(View.GONE);
                Enviaments_Pendents.setVisibility(View.GONE);
                Pujar_Fotos.setVisibility(View.GONE);
                E_UUID.setVisibility(View.GONE);
                E_TimeOut.setVisibility(View.GONE);
                Logout_Refrescar_Dades.setVisibility(View.GONE);





            }
        }


        /*LISTENERS**/{
            Desconectat.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Utils.showCustomDialog(oContext, R.string.atencio, R.string.deslog, R.string.SI, R.string.NO, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PreferencesGesblue.setUserName(oContext, "");
                            PreferencesGesblue.setPassword(oContext, "");
                            PreferencesGesblue.setConcessioString(oContext, "");
                            Intent refresh = new Intent(oContext, LoginActivity.class);
                            finish();
                            startActivity(refresh);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }, false);


                }
            });


            Logout_Refrescar_Dades.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(oContext, LoginActivity.class);
                    intent.putExtra("result", "logout_refresh");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    setResult(RESULT_OK, intent);
                    startActivity(intent);
                    finish();
                }
            });

            Canviar_Concessio.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(oContext, LoginActivity.class);
                    intent.putExtra("result", "unlog");
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });


            Recarregar_Dades.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(oContext, LoginActivity.class);
                    intent.putExtra("result", "refresh");
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });


            Reimpressio.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(oContext, RecuperarDenunciaActivity.class);
                    intent.putExtra("adm", adm);
                    startActivity(intent);
                }
            });

            Pujar_Fotos.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(oContext, PujarImatges.class);
                    intent.putExtra("adm", adm);
                    startActivity(intent);
                }
            });


            Idioma.setOnClickListener(new View.OnClickListener() {


                public void onClick(View v) {

                    Intent intent = new Intent(oContext, Idiomes.class);
                    startActivity(intent);
                }
            });

            Enviaments_Pendents.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (Enviaments_Pendents.isEnabled()) {

                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            //we are connected to a network
                            if (denunciesPendents.size() > 0) {
                                liniar_progress.setMax(denunciesPendents.size());

                                liniar_progress.show();
                                if (EnviamentDisponible) {
                                    EnviamentDisponible = false;
                                    EnviarDenuncies();

                                } else {
                                    //aguanta el main fins que s'ha enviat la anterior
                                    while (!EnviamentDisponible) {

                                        Log.d("ESPERANT","disponibilitat");
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

                                liniar_progress.dismiss();
                                Enviaments_Pendents.setVisibility(View.GONE);
                            }


                        } else {
                            Toast noConexio =
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.sense_conexio), Toast.LENGTH_SHORT);

                            noConexio.show();
                        }
                    }


                }
            });

            Admin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (adm){
                        adm=false;
                        if (estat.equals("main"))
                        {
                        Intent intent = new Intent(oContext, MainActivity.class);
                        intent.putExtra("result", "");
                        intent.putExtra("adm", adm);
                        setResult(RESULT_OK, intent);
                        Toast adminOff =
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.admin_off), Toast.LENGTH_SHORT);

                        adminOff.show();
                        finish();
                        }
                        else if (estat.equals("login_concessio") || estat.equals("no_login_concessio")) {
                            Intent intent = new Intent(oContext, LoginActivity.class);
                            intent.putExtra("result", "");
                            intent.putExtra("adm", adm);
                            setResult(RESULT_OK, intent);
                            Toast adminOff =
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.admin_off), Toast.LENGTH_SHORT);

                            adminOff.show();
                            finish();

                        }

                    }
                    else{
                        Intent intent = new Intent(oContext, Login_Admin.class);
                        intent.putExtra("adm", adm);
                        startActivityForResult(intent, RequestCode);
                    }

                }
            });


            btn_Confirmar.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    if (estat.equals("login_concessio") || estat.equals("no_login_concessio")) {
                        Intent intent = new Intent(oContext, LoginActivity.class);
                        intent.putExtra("result", "");
                        intent.putExtra("adm", adm);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else if (estat.equals("main")) {
                        Intent intent = new Intent(oContext, MainActivity.class);
                        intent.putExtra("adm", adm);
                        setResult(RESULT_OK, intent);
                        finish();
                    }


                }
            });


            Switch_Offline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    PreferencesGesblue.saveOffline(oContext,b);
                    checkEstat(adm,PreferencesGesblue.getOffline(oContext));
                }
            });


            save_uuid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        String uuid=edt_uuid.getText().toString();
                        if(!uuid.equals("null") && !uuid.equals("")){
                            if(!uuid.contains("=")&&!uuid.contains("?")&&!uuid.contains("/")&&!uuid.contains("$")){
                                BK_Utils.SetUUIDToFile(oContext,uuid);
                                PreferencesGesblue.savePrefEternUUID(oContext,uuid);
                                Toast.makeText(oContext,R.string.uuid_canviat,Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(oContext,R.string.uuid_no_caracter_espacials,Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(oContext,R.string.uuid_no_caracter_espacials,Toast.LENGTH_LONG).show();
                        }

                }
            });







            save_timeout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String text=edit_timeout.getText().toString();
                    Integer timeout=Integer.parseInt(text);
                    if(!timeout.equals(0)&&timeout>0){
                        PreferencesGesblue.saveTimeOut(oContext,timeout);
                        Toast.makeText(oContext,R.string.timeout_canviat,Toast.LENGTH_LONG).show();


                    }
                    else{
                        Toast.makeText(oContext,R.string.timeout_error,Toast.LENGTH_LONG).show();
                    }

                }
            });
        }


    }
    /* Actualitza el UI en funcio de ADMIN**/
    private void checkEstat(Boolean adm,Boolean offline) {

        if (adm){
            img_Lock.setVisibility(View.GONE);
            img_Unlock.setVisibility(View.VISIBLE);
            Pujar_Fotos.setVisibility(View.VISIBLE);
            E_UUID.setVisibility(View.VISIBLE);
            E_TimeOut.setVisibility(View.VISIBLE);
        }
        else{
            img_Lock.setVisibility(View.VISIBLE);
            img_Unlock.setVisibility(View.GONE);
            Pujar_Fotos.setVisibility(View.GONE);
            E_UUID.setVisibility(View.GONE);
            E_TimeOut.setVisibility(View.GONE);
        }

        if(offline){
            Capçalera.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.vermellKO));
        }
        else{
            Capçalera.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.barra_superior));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkEstat(adm,PreferencesGesblue.getOffline(oContext));







    }
    /*EL RETORN**/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Boolean result=false;
        // check that it is the SecondActivity with an OK result
        if (requestCode == RequestCode) {
            if (resultCode == RESULT_OK) {


                if (data.getExtras().getBoolean("adm") != false) {
                    result=data.getExtras().getBoolean("adm");
                    if (result) {
                        adm=result;
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "Admin Activat", Toast.LENGTH_SHORT);

                        toast1.show();

                    }
                }
            }
        }

        checkEstat(adm,PreferencesGesblue.getOffline(oContext));



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
                        Long.parseLong(PreferencesGesblue.getTerminal(oContext)),//-- TERMINAL ID
                        Utils.getAndroidVersion(),                              //-- SO VERSION
                        Utils.getAppVersion(oContext));                         //-- APP VERSION


                DatamanagerAPI.crida_NovaDenuncia(ndr,
                        new JSoapCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Intent intent = new Intent(oContext, LoginActivity.class);
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
                                        Utils.showCustomDialog(oContext, R.string.atencio, R.string.errorEnDades);
                                        break;
                                    case -2:
                                    case -3:
                                        PreferencesGesblue.logout(oContext);
                                        startActivity(intent);
                                        break;
                                    default:
                                        //denunciaSent = true;
                                        //sendPhotos();
                                        DatabaseAPI.updateADenunciaEnviada(oContext, den.getCodidenuncia());
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

            File path =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload");

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
                                        PreferencesGesblue.getConcessio(oContext),
                                        str_encoded,
                                        file.getName()
                                );
                                DatamanagerAPI.crida_PujaFoto(pjr,
                                        new JSoapCallback() {
                                            @Override
                                            public void onSuccess(String result) {
                                                File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/done");


                                                if (!direct.exists()) {
                                                    File wallpaperDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/done");
                                                    wallpaperDirectory.mkdirs();
                                                }
                                                File from = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/"+ f.getName());
                                                File to =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/done/"+ f.getName());
                                                from.renameTo(to);

                                            }

                                            @Override
                                            public void onError(int error) {
                                                Log.e("Formulari", "Error PujaFoto: " + error);
                                                File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/error");

                                                if (!direct.exists()) {
                                                    File wallpaperDirectory =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/error");
                                                    wallpaperDirectory.mkdirs();
                                                }
                                                File from = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/"+ f.getName());
                                                File to = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/error/"+f.getName());
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


        private void EnviarDenuncies () {


            //Crea un nou fil
            Runnable runnable=new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < denunciesPendents.size(); i++) {
                        try {
                            enviaDenunciaConcreta(denunciesPendents.get(i));
                            pujaFoto();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        liniar_progress.setProgress(i);
                    }
                    denunciesPendents = null;
                    EnviamentDisponible = true;


                }
            });
            AsyncTask.execute(runnable);



        }



    /*RECUPERA LES DENUNCIES PENDENTS**/
    private void ContadorDenuncies(boolean recarregardades) {

        //Consegueix les denuncies i adpta l'interfaz a les dades
        if(recarregardades){
            List<Model_Denuncia> denunciesPendentsTemp = DatabaseAPI.getDenunciesPendentsEnviar(oContext);
            if(denunciesPendentsTemp!=null) {


                denunciesPendents = denunciesPendentsTemp.subList(0, denunciesPendentsTemp.size());
                txtNumDenuncies.setText(denunciesPendents.size()+"");
            }
            else{
                denunciesPendents=null;
            }
        }



    }













}