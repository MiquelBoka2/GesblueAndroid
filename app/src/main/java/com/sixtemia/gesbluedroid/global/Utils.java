package com.sixtemia.gesbluedroid.global;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;

import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.datamanager.webservices.DatamanagerAPI;
import com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques.RecuperaDataRequest;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.RecuperaDataResponse;

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

/**
 * Created by gerardmartin on 12/9/16.
 */

public class Utils {

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
                .setPositiveButton("aaaaaaa", _listenerPositive)
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
}
