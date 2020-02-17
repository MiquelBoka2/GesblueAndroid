package com.sixtemia.gesbluedroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.datecs.api.emsr.EMSR;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;
import com.datecs.api.rfid.RC663;
import com.sixtemia.gesbluedroid.PrintAsyncTask;
import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.Sancio;
import com.sixtemia.gesbluedroid.activities.passosformulari.Pas1TipusActivity;
import com.sixtemia.gesbluedroid.customstuff.dialogs.DeviceListActivity;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Carrer;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;
import com.sixtemia.gesbluedroid.network.PrinterServer;
import com.sixtemia.gesbluedroid.network.PrinterServerListener;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;
import static android.view.View.VISIBLE;

public class ReimpressioTiquet extends AppCompatActivity {

    private Sancio sancio;
    private String numDenuncia;
    private Date dataCreacio;
    private String foto1;
    private String foto2;
    private String foto3;
    private boolean img1IsActive = false;
    private boolean img2IsActive = false;
    private boolean img3IsActive = false;
    private String aux;
    private TextView txt_Matricula,txt_Tipus,txt_Marca,txt_Model,txt_Color,txt_Sancio,txt_Carrer,txt_Num;
    private Button btn_Imprimir;
    private ImageView img_Marca,img_Foto1,img_Foto2,img_Foto3;
    private Spinner spn_Idioma;
    private View view_Color;
    private boolean recuperada;
    private static boolean isLinked = false;
    private String dataInfraccio = "";
    private String numeroTiquet = "";
    private String idiomaImpressio="";
    private String idiomaAntic=Locale.getDefault().getLanguage();



    private Context RTContext=this;

    public static final String INTENT_RECUPERADA = "recuperada";
    public static final String INTENT_SANCIO = "sancio";
    public static final String INTENT_NUM_DENUNCIA = "numDenuncia";
    public static final String INTENT_DATA_CREACIO = "dataCreacio";
    public static final String KEY_DATA_INFRACCIO = "dataInfraccio";
    public static final String KEY_NUMERO_TIQUET = "numeroTiquet";
    private static final int REQUEST_MORE_OPTIONS = 1735;
    private static final int REQUEST_GET_DEVICE = 1734;
    private static RC663 mRC663;
    public static final String LOG_TAG = "Gesblue";

    private static ProtocolAdapter mProtocolAdapter;
    private static ProtocolAdapter.Channel mPrinterChannel;
    private static Printer mPrinter;
    private static EMSR mEMSR;
    private static PrinterServer mPrinterServer;
    private static BluetoothSocket mBtSocket;


    private ProgressDialog mDialog;

    private ProgressDialog getDialog( String title,  String message) {
        if(null == mDialog || !mDialog.isShowing()) {
            mDialog = new ProgressDialog(this);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setIndeterminate(true);
            mDialog.setCancelable(false);
            if(!isFinishing())mDialog.show();
        }
        if(null != title) {
            mDialog.setTitle(title);
        }
        if(null != message) {
            mDialog.setMessage(message);
        }
        return mDialog;
    }

    private ProgressDialog getDialog( String message) {
        return getDialog(null, message);
    }

    private ProgressDialog getDialog(int message) {
        return getDialog(getString(message));
    }

    private ProgressDialog getDialog(int title, int message) {
        return getDialog(getString(title), getString(message));
    }

    private void dismissDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null != mDialog) {
                    try {
                        mDialog.dismiss();
                    } catch (Exception ex) {
                        Log.e("ERROR",ex.toString());
                    }
                }
                mDialog = null;
            }
        });
    }

    protected void DLog(String what) {
        Log.i(LOG_TAG, what);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimpressio_denuncia);
        loadAll();
        getIntentAndFill();
        configSpiner();

        btn_Imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Utils.showAlertSelectedLanguage(RTContext, R.string.atencio, R.string.confirmacio,spn_Idioma.getSelectedItem().toString(), R.string.butlletaImpresaOk_imprimir, R.string.butlletaImpresaOk_enviar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   print();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            }, false);

            }
        });

















    }

    private void loadAll() {
        txt_Matricula=this.findViewById(R.id.txt_Matricula);
        txt_Tipus=this.findViewById(R.id.txt_Tipus);
        txt_Marca=this.findViewById(R.id.txt_Marca);
        txt_Model=this.findViewById(R.id.txt_Model);
        txt_Color=this.findViewById(R.id.txt_Color);
        txt_Sancio=this.findViewById(R.id.txt_Sancio);
        txt_Carrer=this.findViewById(R.id.txt_Carrer);
        txt_Num=this.findViewById(R.id.txt_Num);

        btn_Imprimir=this.findViewById(R.id.btn_Imprimir);

        img_Marca=this.findViewById(R.id.img_Marca);
        img_Foto1=this.findViewById(R.id.img_Foto1);
        img_Foto2=this.findViewById(R.id.img_Foto2);
        img_Foto3=this.findViewById(R.id.img_Foto3);

        spn_Idioma=this.findViewById(R.id.spn_idioma);

        view_Color=this.findViewById(R.id.view_Color);











    }


    private void getIntentAndFill() {
        Intent intent = getIntent();



        sancio = intent.getParcelableExtra(INTENT_SANCIO);

        if(intent.getBooleanExtra(INTENT_RECUPERADA,false)) {
            recuperada = true;
        }
        if(!isEmpty(intent.getStringExtra(INTENT_NUM_DENUNCIA))) {
            numDenuncia = intent.getStringExtra(INTENT_NUM_DENUNCIA);
        }
        if(recuperada==true){
            dataCreacio =(Date)intent.getSerializableExtra(INTENT_DATA_CREACIO);
        }
        else{

            Model_Carrer carrer = DatabaseAPI.getCarrer(this, String.valueOf(PreferencesGesblue.getCodiCarrer(this)));

            sancio.setModelCarrer(carrer);
        }

        /**MATRICULA**/
        if(!isEmpty(sancio.getMatricula())) {
            txt_Matricula.setText(sancio.getMatricula());
        } else {
            finish();
        }

        /**TIPUS**/
        if(sancio.getModelTipusVehicle() != null) {


            aux = txt_Tipus.getText().toString();

            if(isEmpty(aux)) {
                txt_Tipus.setText(Utils.languageMultiplexer(sancio.getModelTipusVehicle().getNomtipusvehiclees(), sancio.getModelTipusVehicle().getNomtipusvehiclecat()));
                txt_Marca.setEnabled(true);
            } else {
                if(!(aux.equals(Utils.languageMultiplexer(sancio.getModelTipusVehicle().getNomtipusvehiclees(), sancio.getModelTipusVehicle().getNomtipusvehiclecat())))) {
                    txt_Tipus.setText(Utils.languageMultiplexer(sancio.getModelTipusVehicle().getNomtipusvehiclees(), sancio.getModelTipusVehicle().getNomtipusvehiclecat()));

                    txt_Tipus.setText("");
                    txt_Tipus.setEnabled(true);
                    img_Marca.setImageResource(0);
                    sancio.setModelMarca(null);

                    txt_Model.setText("");
                    txt_Model.setEnabled(false);
                    sancio.setModelModel(null);
                }
            }
        }


        /**MARCA**/

        if(txt_Marca.isEnabled()) {
            if(sancio.getModelMarca() != null) {

                aux = txt_Marca.getText().toString();

                if(isEmpty(aux)) {
                    Glide.with(this).load(sancio.getModelMarca().getImatgemarca()).into(img_Marca);
                    txt_Marca.setText(sancio.getModelMarca().getNommarca());
                    txt_Model.setEnabled(true);
                } else {
                    if(!(aux.equals(sancio.getModelMarca().getNommarca()))) {
                        Glide.with(this).load(sancio.getModelMarca().getImatgemarca()).into(img_Marca);
                        txt_Marca.setText(sancio.getModelMarca().getNommarca());
                        txt_Model.setText("");
                        txt_Model.setEnabled(true);
                        sancio.setModelModel(null);

                    }
                }
            }
        }





        /**MODEL**/
        if(txt_Model.isEnabled()) {
            if(sancio.getModelModel() != null) {

                String aux = txt_Model.getText().toString();

                if(isEmpty(aux)) {
                    txt_Model.setText(sancio.getModelModel().getNommodel());
                } else {
                    if(!(aux.equals(sancio.getModelModel().getNommodel()))) {
                        txt_Model.setText(sancio.getModelModel().getNommodel());

                    }
                }
            }
        }

        /**COLOR**/
        if(sancio.getModelColor() != null) {
           txt_Color.setText(Utils.languageMultiplexer(sancio.getModelColor().getNomcolores(), sancio.getModelColor().getNomcolorcat()));
            view_Color.setBackgroundColor(Color.parseColor("#" + sancio.getModelColor().getHexcolor()));
        }


        /**SANCIO**/
        if(txt_Sancio.isEnabled()) {
            if(sancio.getModelInfraccio() != null) {
                txt_Sancio.setText(sancio.getModelInfraccio().getNom());
            }
        }

        /**CARRER**/
        if(txt_Carrer.isEnabled()) {
            if (sancio.getModelCarrer() != null) {
                txt_Carrer.setText(sancio.getModelCarrer().getNomcarrer());
            }
        }

       /**NUM**/
        if(txt_Num.isEnabled()) {
            if(!isEmpty(sancio.getNumero())) {
                txt_Num.setText(sancio.getNumero());
            }
        }



        /** IMG***/
        File f = new File("storage/emulated/0/Sixtemia/upload/temp");
        if (f.exists() && f.isDirectory()){
            final Pattern p = Pattern.compile(".*-"+numDenuncia+"1.jpg"); // I know I really have a stupid mistake on the regex;

            File[] flists = f.listFiles(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    Log.e("Matched?:","-"+file);
                     return p.matcher(file.getName()).matches();
                }
            });
            if(flists.length>0){
                File f1 = flists[0];

                Log.e("Ruta foto1:",f1.getName());

                pinta("storage/emulated/0/Sixtemia/upload/temp/"+f1.getName(),img_Foto1);
                img1IsActive = true;
                foto1 = "storage/emulated/0/Sixtemia/upload/temp/"+f1.getName();
            }

            final Pattern p2 = Pattern.compile(".*-"+numDenuncia+"2.jpg"); // I know I really have a stupid mistake on the regex;

            File[] flists2 = f.listFiles(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    return p2.matcher(file.getName()).matches();
                }
            });
            if(flists2.length>0){
                File f2 = flists2[0];

                Log.e("Ruta foto2:",f2.getName());


                pinta("storage/emulated/0/Sixtemia/upload/temp/"+f2.getName(), img_Foto2);
                img2IsActive = true;
                foto2 = "storage/emulated/0/Sixtemia/upload/temp/"+f2.getName();
            }


            final Pattern p3 = Pattern.compile(".*-"+numDenuncia+"3.jpg"); // I know I really have a stupid mistake on the regex;

            File[] flists3 = f.listFiles(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    return p3.matcher(file.getName()).matches();
                }
            });
            if(flists3.length>0){
                File f3 = flists3[0];


                Log.e("Ruta foto3:",f3.getName());


                pinta("storage/emulated/0/Sixtemia/upload/temp/"+f3.getName(), img_Foto3);
                img3IsActive = true;
                foto3 = "storage/emulated/0/Sixtemia/upload/temp/"+f3.getName();
            }

        }






    }

    private void configSpiner(){
        if (spn_Idioma != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_idioma_custom,getResources().getStringArray(R.array.Idiomes));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_Idioma.setAdapter(arrayAdapter);


            spn_Idioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                    if (position==1){
                        idiomaImpressio="es";
                    }
                    else if (position==2){
                        idiomaImpressio="en";
                    }
                    else if (position==3){
                        idiomaImpressio="fr";
                    }
                    else if (position==4){
                        idiomaImpressio="de";
                    }
                    else{
                        idiomaImpressio="ca";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

        }
    }

    private void pinta(String path, ImageView imgView) {
        if(!isEmpty(path)) {
            Glide.with(this)
                    .load(path)
                    .asBitmap()
                    .into(new BitmapImageViewTarget(imgView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            //Play with bitmap
                            super.setResource(resource);
                        }
                    });



            imgView.setVisibility(VISIBLE);
        }
    }

    private void print() {
        if(isLinked) {
            printFinal();
        } else {
            getDialog(R.string.msg_connecting);
            link();
        }
    }

    private void printFinal() {
        if(PreferencesGesblue.getTiquetUsuari(RTContext) || PreferencesGesblue.getDataImportTiquet(RTContext)) {
            Intent intent = new Intent(RTContext, MoreInfoToPrintActivity.class);
            intent.putExtra(MoreInfoToPrintActivity.KEY_TIQUET,PreferencesGesblue.getTiquetUsuari(RTContext));
            intent.putExtra(MoreInfoToPrintActivity.KEY_DATAIMPORT,PreferencesGesblue.getDataImportTiquet(RTContext));
            intent.putExtra(INTENT_SANCIO,sancio);
            startActivityForResult(intent, REQUEST_MORE_OPTIONS);
        } else {
            try{
                runOnUiThread(new Runnable() {
                    public void run() {
                        getDialog(R.string.dialogImprimint);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            CanviarIdioma(idiomaImpressio);

            PrintAsyncTask p = new PrintAsyncTask(mPrinter, RTContext, sancio, numDenuncia, dataCreacio, false, new PrintAsyncTask.PrintListener() {
                @Override
                public void onFinish(Exception ex, boolean isFirstTime) {
                    if(null == ex) {
                        dismissDialog();
                        Utils.showCustomDialog(RTContext, R.string.atencio, R.string.butlletaImpresaOk, R.string.butlletaImpresaOk_imprimir, R.string.butlletaImpresaOk_enviar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                printFinal();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CanviarIdioma(idiomaAntic);


                                Intent intent = new Intent(RTContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        }, false);


                    }
                    else {
                        Log.e("ERROR", ex.toString());
                        if (isFirstTime) {
                            //printFinal();
                        } else {
                            Utils.showCustomDialog(RTContext, R.string.atencio, R.string.printerConnection_failedConnect, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismissDialog();
                                }
                            });
                        }
                    }
                }


                @Override
                public void onError() {
                    link();
                }
            });
            p.execute();
        }
    }

    private void link() {
        if(!isEmpty(PreferencesGesblue.getAddressBluetoothPrinter(RTContext))) {
            establishBluetoothConnection(PreferencesGesblue.getAddressBluetoothPrinter(RTContext));

        } else {
            waitForConnection();
        }
    }

    private void establishBluetoothConnection(final String address) {
        getDialog(R.string.title_please_wait, R.string.msg_connecting);

        closeActiveConnection();

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(LOG_TAG, "Connecting to " + address + "...");

                btAdapter.cancelDiscovery();

                try {
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    BluetoothDevice btDevice = btAdapter.getRemoteDevice(address);

                    InputStream in;
                    OutputStream out;

                    try {
                        BluetoothSocket btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                        btSocket.connect();

                        mBtSocket = btSocket;
                        in = mBtSocket.getInputStream();
                        out = mBtSocket.getOutputStream();
                    } catch (final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(btAdapter.getState()==BluetoothAdapter.STATE_OFF) {
                                    Utils.showCustomDialog(RTContext, R.string.printerConnection_failedTitle, R.string.printerConnection_failedEngegaBluetooth);
                                } else {
                                    Utils.showCustomDialog(RTContext, R.string.printerConnection_failedTitle, R.string.printerConnection_failedConnect);
                                    PreferencesGesblue.saveAdressBluetoothPrinter(RTContext, "");
                                }
                                DLog("FAILED to connect: " + e.getMessage());
                                dismissDialog();
//								waitForConnection();
                            }
                        });
                        return;
                    }

                    try {
                        initPrinter(in, out);
                    } catch (final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(btAdapter.getState()==BluetoothAdapter.STATE_ON) {
                                    Utils.showCustomDialog(RTContext, R.string.printerConnection_failedTitle, R.string.printerConnection_failedInitialize);
                                }
                                DLog("FAILED to initiallize: " + e.getMessage());
                            }
                        });
                    }
                } finally {
                    DLog("Tancant dialeg a establishBluetoothConnection");
                    dismissDialog();

                    printFinal();
                }
            }
        });
        t.start();
    }


    private void showErrorImpresora(int code) {
        switch(code) {
            case 0:
                Utils.showCustomDialog(RTContext, R.string.errorAtencio, R.string.errorTemperatura);
                break;
            case 1:
                Utils.showCustomDialog(RTContext, R.string.errorAtencio, R.string.errorPaper);
                break;
            case 2:
                Utils.showCustomDialog(RTContext, R.string.errorAtencio, R.string.errorBateria);
                break;
        }
    }


    private synchronized void waitForConnection() {
//		closeActiveConnection();

        // Show dialog to select a Bluetooth device.
        Intent intent = new Intent(this, DeviceListActivity.class);
        intent.putExtra(DeviceListActivity.KEY_VALID, true);
        startActivityForResult(intent, REQUEST_GET_DEVICE);

        // Start server to listen for network connection.
        try {
            mPrinterServer = new PrinterServer(new PrinterServerListener() {
                @Override
                public void onConnect(Socket socket) {
                    Log.i(LOG_TAG, "Accept connection from " + socket.getRemoteSocketAddress().toString());

                    // Close Bluetooth selection dialog
                    finishActivity(REQUEST_GET_DEVICE);

                    try {
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                        initPrinter(in, out);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RTContext, "FAILED to initialize: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        waitForConnection();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initPrinter(InputStream inputStream, OutputStream outputStream) throws IOException {
        // Check if printer is into protocol mode. Ones the object is created it can not be released
        // without closing base streams.
        mProtocolAdapter = new ProtocolAdapter(inputStream, outputStream);
        if (mProtocolAdapter.isProtocolEnabled()) {
            Log.i(LOG_TAG, "Protocol mode is enabled");

            isLinked = true;


            // Into protocol mode we can callbacks to receive printer notifications
            mProtocolAdapter.setPrinterListener(new ProtocolAdapter.PrinterListener() {
                @Override
                public void onThermalHeadStateChanged(boolean overheated) {
                    if (overheated) {
                        Log.i(LOG_TAG, "Thermal head is overheated");
                        showErrorImpresora(0);
                    }
                }

                @Override
                public void onPaperStateChanged(boolean hasPaper) {
                    if (hasPaper) {
                        Log.i(LOG_TAG, "Event: Paper out");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showErrorImpresora(1);
                            }
                        });
                    }
                }

                @Override
                public void onBatteryStateChanged(boolean lowBattery) {
                    if (lowBattery) {
                        Log.i(LOG_TAG, "Low battery");
                        showErrorImpresora(2);
                    }
                }
            });
            // Get printer instance
            mPrinterChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
            mPrinter = new com.datecs.api.printer.Printer(mPrinterChannel.getInputStream(), mPrinterChannel.getOutputStream());
            isLinked = true;

        } else {
            Log.i(LOG_TAG, "Protocol mode is disabled");

            // Protocol mode it not enables, so we should use the row streams.
            mPrinter = new com.datecs.api.printer.Printer(mProtocolAdapter.getRawInputStream(), mProtocolAdapter.getRawOutputStream());
        }

        mPrinter.setConnectionListener(new com.datecs.api.printer.Printer.ConnectionListener() {
            @Override
            public void onDisconnect() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RTContext, getString(R.string.printer_disconnected), Toast.LENGTH_SHORT).show();
                        isLinked = false;
                        if (!isFinishing()) {
                            waitForConnection();
                        }
                    }
                });
            }
        });

    }

    private synchronized void closeActiveConnection() {
        closePrinterConnection();
        closeBluetoothConnection();
        closePrinterServer();
    }

    private synchronized void closeBluetoothConnection() {
        // Close Bluetooth connection
        BluetoothSocket s = mBtSocket;
        mBtSocket = null;
        if (s != null) {
            Log.i(LOG_TAG, "Close Bluetooth socket");
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void closePrinterServer() {
        // Close network server
        PrinterServer ps = mPrinterServer;
        mPrinterServer = null;
        if (ps != null) {
            Log.i(LOG_TAG, "Close Network server");
            try {
                ps.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void closePrinterConnection() {
        if (mRC663 != null) {
            try {
                mRC663.disable();
            } catch (IOException e) {
                Log.e("ERROR",e.toString());
            }

            mRC663.close();
        }

        if (mEMSR != null) {
            mEMSR.close();
        }

        if (mPrinter != null) {
            mPrinter.close();
        }

        if (mProtocolAdapter != null) {
            mProtocolAdapter.close();
        }
    }

    public void CanviarIdioma(String nouIdioma) {

        Locale myLocale = new Locale(nouIdioma);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        PreferencesGesblue.setLocale(RTContext,nouIdioma);
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_MORE_OPTIONS:
                    sancio = (Sancio) data.getExtras().get(INTENT_SANCIO);
                    dataInfraccio = (String) data.getExtras().get(KEY_DATA_INFRACCIO);
                    numeroTiquet = (String) data.getExtras().get(KEY_NUMERO_TIQUET);
                    break;

                case REQUEST_GET_DEVICE:
                    String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
                        PreferencesGesblue.saveAdressBluetoothPrinter(RTContext, address);
                        establishBluetoothConnection(address);
                    }
                    break;
                default:
                    break;
            }
        } else if(resultCode == RESULT_CANCELED || resultCode == 1) {
            switch (requestCode) {
                case REQUEST_GET_DEVICE:
                    dismissDialog();
                    break;
            }
        }
    }
}
