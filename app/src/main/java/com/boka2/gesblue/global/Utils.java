package com.boka2.gesblue.global;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.boka2.gesblue.R;
import com.boka2.gesblue.datamanager.webservices.DatamanagerAPI;
import com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques.RecuperaDataRequest;
import com.boka2.gesblue.datamanager.webservices.results.dadesbasiques.RecuperaDataResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import pt.joaocruz04.lib.misc.JSoapCallback;
import pt.joaocruz04.lib.misc.JsoapError;

import static android.text.TextUtils.isEmpty;
import static com.boka2.gesblue.global.PreferencesGesblue.getControl;
import static com.boka2.gesblue.global.PreferencesGesblue.getPrefCodiExportadora;
import static com.boka2.gesblue.global.PreferencesGesblue.getPrefCodiInstitucio;
import static com.boka2.gesblue.global.PreferencesGesblue.getPrefCodiTipusButlleta;
import static com.boka2.sbaseobjects.tools.ImageTools.getFileAfterResize;

/**
 * Created by Boka2.
 */



public class Utils {



    static public final int REQUEST_IMAGE_CAPTURE_1= 10;
    static public final int REQUEST_IMAGE_CAPTURE_2= 20;
    static public final int REQUEST_IMAGE_CAPTURE_3= 30;
    static public final int REQUEST_IMAGE_CAPTURE_4= 40;


    public static Activity current_Activity;
    private static String cp = "sup3rS3xy";

    public static Calendar getCurrentTime(Context _context) {
        long current = System.currentTimeMillis();
        current = current + PreferencesGesblue.getIncrementalTime(_context);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current);

        return calendar;
    }

    protected static long convertServerTimeToMillis(long _serverTime) throws ParseException {
        Date oldDate = new Date();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setLenient(false);


            oldDate = formatter.parse(Long.toString(_serverTime));
            return oldDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return oldDate.getTime();
    }

    public static String getCurrentTimeString(Context context) {
        Calendar calendar = getCurrentTime(context);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        StringBuilder s = new StringBuilder(14);

        s.append(year);

        if(month < 10) s.append("0");
        s.append(month);

        if(day < 10 ) s.append("0");
        s.append(day);

        if(hours < 10) s.append("0");
        s.append(hours);

        if(minutes < 10 ) s.append("0");
        s.append(minutes);

        if(seconds < 10) s.append("0");
        s.append(seconds);

        return s.toString();
    }
    public static String getCurrentTimeStringShort(Context context) {
        Calendar calendar = getCurrentTime(context);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        StringBuilder s = new StringBuilder(14);

        s.append(year);

        if(month < 10) s.append("0");
        s.append(month);

        if(day < 10 ) s.append("0");
        s.append(day);



        return s.toString();
    }
    public static long getCurrentTimeLong(Context context) {
        return Long.parseLong(getCurrentTimeString(context));
    }

    public static String getAppVersion(Context context) {
        try{
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAndroidVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return tManager.getDeviceId();
    }

    public static void showCustomDatamanagerError(Context context, String error) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(context.getString(R.string.atencio))
                .setMessage(error)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    public static void getSyncDate(final Context _context) {
        DatamanagerAPI.crida_RecuperaData(new RecuperaDataRequest(), new JSoapCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    RecuperaDataResponse response = DatamanagerAPI.parseJson(result, RecuperaDataResponse.class);

                    PreferencesGesblue.setIncrementalTime(_context, response.getFecha());
                } catch (Exception ex) {
                    Log.e("GesBlue", "Error: " + ex.getLocalizedMessage(), ex);
                }
            }

            @Override
            public void onError(int error) {

            }
        });
    }

    public static String removeAccents(String text) {
        return text == null ? null : Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static void showDatamanagerError(Context context, int error) {
        String strError = null;

        switch(error) {
            case JsoapError.PARSE_ERROR:
                strError = context.getString(R.string.otherError);
                break;
            case JsoapError.NETWORK_ERROR:
                strError = context.getString(R.string.networkError);
                break;
            default:
                strError = context.getString(R.string.otherError);
                break;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(context.getString(R.string.atencio))
                .setMessage(strError)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    public static void showFaltenDadesError(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(context.getString(R.string.atencio))
                .setMessage(context.getString(R.string.campsObligatoris))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    public static void showDialogNoPotsPassar(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(context.getString(R.string.atencio))
                .setMessage(context.getString(R.string.youShallNotPass))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    public static void showCustomDialog(Context context, int title, int body) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(context.getString(title))
                .setMessage(context.getString(body))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        if(context instanceof Activity) {
            if(!((Activity) context).isFinishing()) {
                alert.show();
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.BLACK);
            }
        }
    }

    public static void showCustomDialog(Context context, int title, int body, DialogInterface.OnClickListener _listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(context.getString(title))
                .setMessage(context.getString(body))
                .setPositiveButton(context.getString(R.string.ok), _listener);

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    public static void showCustomDialog(Context context, int title, int body, DialogInterface.OnClickListener _listener, boolean cancelable) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(context.getString(title))
                .setMessage(context.getString(body))
                .setPositiveButton(context.getString(R.string.ok), _listener);
        alertDialogBuilder.setCancelable(cancelable);
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    public static void showCustomDialog(Context context, int title, int body, int positive, int negative, DialogInterface.OnClickListener _listenerPositive, DialogInterface.OnClickListener _listenerNegative, boolean cancelable) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


        alertDialogBuilder.setTitle(title == -1 ? "" : context.getString(title))
                .setMessage(context.getString(body))
                .setPositiveButton(context.getString(positive), _listenerPositive)
                .setNegativeButton(context.getString(negative), _listenerNegative)
                .setCancelable(cancelable);
        AlertDialog alert = alertDialogBuilder.create();


        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    public static void showAlertSelectedLanguage(Context context, int title, int body,String idioma, int positive, int negative, DialogInterface.OnClickListener _listenerPositive, DialogInterface.OnClickListener _listenerNegative, boolean cancelable) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


        alertDialogBuilder.setTitle(title == -1 ? "" : context.getString(title))
                .setMessage(context.getString(body)+" "+idioma)
                .setPositiveButton(context.getString(positive), _listenerPositive)
                .setNegativeButton(context.getString(negative), _listenerNegative)
                .setCancelable(cancelable);
        AlertDialog alert = alertDialogBuilder.create();


        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }


    public static String languageMultiplexer(String es, String ca) {

        String str = (Locale.getDefault().getLanguage().equalsIgnoreCase(Constants.LANG_CA) ? ca : es);
        if(str.isEmpty() && !ca.isEmpty()) {
            str = ca;
        }
        return str;

    }

    public static String e(Context mContext, String value, boolean en) {
        if(en) {
            String v = mContext.getString(R.string.gbf_3) + mContext.getString(R.string.cela_3) + "0" + mContext.getString(R.string.cela_1);
            try {
                DESKeySpec ks = new DESKeySpec(cp.getBytes("UTF8"));
                SecretKey k = SecretKeyFactory.getInstance("DES").generateSecret(ks);

                byte[] ct = v.getBytes("UTF8");
                // Cipher is not thread safe
                Cipher c = Cipher.getInstance("DES");
                c.init(Cipher.ENCRYPT_MODE, k);

                return Base64.encodeToString(c.doFinal(ct), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return v;
        } else {
            try {
                DESKeySpec ks = new DESKeySpec(cp.getBytes("UTF8"));
                SecretKey k = SecretKeyFactory.getInstance("DES").generateSecret(ks);

                byte[] epb = Base64.decode(value, Base64.DEFAULT);
                // cipher is not thread safe
                Cipher c = Cipher.getInstance("DES");
                c.init(Cipher.DECRYPT_MODE, k);
                byte[] dvb = (c.doFinal(epb));

                String dv = new String(dvb);
                return dv;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }
    }

    public static void showCustomDialog2(Context context, int title, int body, int positive, DialogInterface.OnClickListener _listenerPositive, DialogInterface.OnClickListener _listenerNegative, boolean cancelable) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


        alertDialogBuilder.setTitle(title == -1 ? "" : context.getString(title))
                .setMessage(context.getString(body))
                .setPositiveButton(context.getString(positive), _listenerPositive)
                .setCancelable(cancelable);
        AlertDialog alert = alertDialogBuilder.create();


        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }


    public static String generateCodiButlleta(Context mContext) {
        String numeroTiquet="";
        if(!isEmpty(numeroTiquet)) {
            Log.d("Camera","No empty");
            return numeroTiquet;
        } else {
            long codiAgent = PreferencesGesblue.getCodiAgent(mContext);
            String terminal = PreferencesGesblue.getTerminal(mContext);

            int comptadorDenuncia = PreferencesGesblue.getComptadorDenuncia(mContext.getApplicationContext())+1;
            //PreferencesGesblue.saveComptadorDenuncia(mContext, comptadorDenuncia);

            int control = getControl(mContext);

            Log.d("comptadorDenuncia: ",comptadorDenuncia+"");
            int codiexportadora = getPrefCodiExportadora(mContext);
            String coditipusbutlleta = getPrefCodiTipusButlleta(mContext);
            String codiinstitucio = getPrefCodiInstitucio(mContext);
            StringBuilder sb = new StringBuilder();
            //sb.append("1");
            int padding = 0;
            switch(codiexportadora) {
                case 1://Consell Comarcal de la Selva
                    sb.append(coditipusbutlleta);
                    sb.append(codiinstitucio);
                    if (terminal.length() < 2) {
                        sb.append("0");
                    }
                    sb.append(terminal);
                    padding = 5 - String.valueOf(comptadorDenuncia).length();
                    for (int i = 0; i < padding; i++) {
                        sb.append("0");
                    }
                    sb.append(comptadorDenuncia);
                    break;
                case 2://Consell Comarcal del Baix Empordà
                    sb.append(coditipusbutlleta);
                    sb.append(codiinstitucio);
                    if (terminal.length() < 2) {
                        sb.append("0");
                    }
                    sb.append(terminal);
                    padding = 6 - String.valueOf(comptadorDenuncia).length();
                    for (int i = 0; i < padding; i++) {
                        sb.append("0");
                    }
                    sb.append(comptadorDenuncia);
                    break;
                case 3://Xaloc
                    sb.append(coditipusbutlleta);
                    sb.append(codiinstitucio);
                    if (terminal.length() < 2) {
                        sb.append("0");
                    }
                    sb.append(terminal);
                    padding = 5 - String.valueOf(comptadorDenuncia).length();
                    for (int i = 0; i < padding; i++) {
                        sb.append("0");
                    }
                    sb.append(comptadorDenuncia);
                    break;
                case 4://Somintec
                    sb.append(coditipusbutlleta);
                    sb.append(codiinstitucio);
                    if (terminal.length() < 2) {
                        sb.append("0");
                    }
                    sb.append(terminal);
                    padding = 5 - String.valueOf(comptadorDenuncia).length();
                    for (int i = 0; i < padding; i++) {
                        sb.append("0");
                    }
                    sb.append(comptadorDenuncia);
                    break;
                case 5://Policia Local de Calonge
                    sb.append(coditipusbutlleta);
                    sb.append(codiinstitucio);
                    if (terminal.length() < 2) {
                        sb.append("0");
                    }
                    sb.append(terminal);
                    padding = 5 - String.valueOf(comptadorDenuncia).length();
                    for (int i = 0; i < padding; i++) {
                        sb.append("0");
                    }
                    sb.append(comptadorDenuncia);
                    break;
                case 6://Consell Comarcal de la Selva
                    sb.append(coditipusbutlleta);
                    sb.append(codiinstitucio);
                    if (terminal.length() < 2) {
                        sb.append("0");
                    }
                    sb.append(terminal);
                    padding = 5 - String.valueOf(comptadorDenuncia).length();
                    for (int i = 0; i < padding; i++) {
                        sb.append("0");
                    }
                    sb.append(comptadorDenuncia);
                    break;
            }
            numeroTiquet = sb.toString();

            return numeroTiquet;
        }
    }

    public static String savePicture(Bitmap FotoBitmap, Context mContext, String position) {
            String concessio = Long.toString(PreferencesGesblue.getConcessio(mContext));
            String numDenuncia = generateCodiButlleta(mContext);
            while(numDenuncia == ""){
                numDenuncia = generateCodiButlleta(mContext);
                Log.d("Camera","Error calculant Codi Butlleta");
            }
            Log.d ("Camera",numDenuncia);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentDateString = dateFormat.format(new Date());
            File direct = new File("storage/emulated/0/Boka2/upload/temp");

            if (!direct.exists()) {
                File wallpaperDirectory = new File("storage/emulated/0/Boka2/upload/temp");
                wallpaperDirectory.mkdirs();
            }
            File file = new File(direct, currentDateString  + "-" + concessio + "-" + numDenuncia + position + ".jpg");
            //OutputStream os = null;
            //String error = getString(R.string.foto_error_guardar_desconocido);

            try (FileOutputStream out = new FileOutputStream(file)) {
                FotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.toString();


            /**try {
                os = new FileOutputStream(file);
                os.write(data);
                os.close();
            } catch (IOException e) {
                Log.w(getTag(), "Cannot write to " + file, e);
                error = e.getLocalizedMessage();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        // Ignore
                        error = e.getLocalizedMessage();
                    }
                }
            }
            if(file.exists()) {
                file = getFileAfterResize(file);
                confirmIntent(file.getAbsolutePath());
            } else {
                Toast.makeText(mContext, getString(R.string.foto_error_guardar, error), Toast.LENGTH_SHORT).show();
            }**/
        }







}
