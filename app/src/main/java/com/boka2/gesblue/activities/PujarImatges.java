package com.boka2.gesblue.activities;

import static android.os.Environment.DIRECTORY_DCIM;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boka2.gesblue.Boka2ols.BK_Utils;
import com.boka2.gesblue.Boka2ols.BK_Utils.BooVariable.ChangeListener;
import com.boka2.gesblue.Boka2ols.Kotlin_Utils;
import com.boka2.gesblue.Boka2ols.Kotlin_Utils.*;
import com.boka2.gesblue.R;
import com.boka2.gesblue.adapters.PujarFotos_Adapter;
import com.boka2.gesblue.datamanager.webservices.DatamanagerAPI;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.PujaFotoRequest;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.sbaseobjects.objects.SFragmentActivity;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

import pt.joaocruz04.lib.misc.JSoapCallback;

public class PujarImatges extends AppCompatActivity {

    public static RecyclerView llistat;
    public static ProgressBar loading;
    public static Context mContext;
    public static ProgressBar progress_all;
    public static TextView txt_upload,txt_progress,txt_correct_num,txt_error_num;
    public static Button btn_cancel,btn_send_all;
    public static ConstraintLayout lay_all;

    public static int count_ok=0;
    public static int count_error=0;
    public static int index=0;
    public static int total=0;
    public static boolean canceled=false;

    public static BK_Utils.BooVariable result_BOO;

    /*
    public void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
        }
    }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pujar_imatges);
        canceled=false;
        result_BOO=new BK_Utils.BooVariable();
        result_BOO.setBoo(false);
        result_BOO.setContext(this);

        count_ok=0;
        count_error=0;
        index=0;
        total=0;

        mContext=this;
        llistat= this.findViewById(R.id.recycler);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q) {
            Companion.AskPermi.Companion.askForPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_MEDIA_LOCATION,

                    }, new Kotlin_Utils.Companion.AskPermi.Companion.PermissionListener() {
                        @Override
                        public void onPermissionsGranted() {
                            Log.d("PujarImatges", "PermissionsGranted");

                        }

                        @Override
                        public void onPermissionsDenied() {
                            Log.d("PujarImatges", "PermissionsDenied");
                            finish();
                        }
                    });
        }
        else{
            Companion.AskPermi.Companion.askForPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    }, new Kotlin_Utils.Companion.AskPermi.Companion.PermissionListener() {
                        @Override
                        public void onPermissionsGranted() {
                            Log.d("PujarImatges", "PermissionsGranted");

                        }

                        @Override
                        public void onPermissionsDenied() {
                            Log.d("PujarImatges", "PermissionsDenied");
                            finish();
                        }
                    });
        }


        //askForPermissions();

        //region UPLOAD ALL
        lay_all=this.findViewById(R.id.lay_all);
        progress_all=this.findViewById(R.id.progress_all);
        txt_upload=this.findViewById(R.id.txt_upload);
        txt_progress=this.findViewById(R.id.txt_progress);
        txt_correct_num=this.findViewById(R.id.txt_correct_num);
        txt_error_num=this.findViewById(R.id.txt_error_num);
        btn_cancel=this.findViewById(R.id.btn_cancel);
        btn_send_all=this.findViewById(R.id.btn_send_all);

        lay_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canceled=true;
                txt_upload.setText(String.format("%s", mContext.getString(R.string.canceled)));
            }
        });








        //endregion

        loading=this.findViewById(R.id.loading_bar);
        loading.setVisibility(View.GONE);
        loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        File path = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), "/Boka2/upload/error");
        if(path.exists()) {
            String[] fileNames = path.list();
            final File[] files = path.listFiles();
            int i=0;


            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            llistat.setLayoutManager(mLayoutManager);


            // specify an adapter (see also next example)
            PujarFotos_Adapter mAdapter = new PujarFotos_Adapter(this,files);
            llistat.setAdapter(mAdapter);


            if(files.length>0){
                btn_send_all.setVisibility(View.VISIBLE);

                btn_send_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txt_progress.setText(String.format("%s / %d",index, total));
                        progress_all.setMax(total);
                        progress_all.setProgress(index);
                        txt_correct_num.setText(String.format("%s %d", mContext.getString(R.string.correct_upload), count_ok));
                        txt_error_num.setText(String.format("%s %d", mContext.getString(R.string.upload_error), count_error));
                        canceled=false;
                        lay_all.setVisibility(View.VISIBLE);
                        total=files.length;
                        index=0;
                        result_BOO.setBoo(true);


                    }
                });


                result_BOO.setListener(new ChangeListener() {
                    @Override
                    public void onChange () {
                        if ( result_BOO.getBoo() ) {
                            result_BOO.setBoo(false);

                            if(index<total&&!canceled){
                                txt_progress.setText(String.format("%s / %d",index, total));
                                progress_all.setProgress(index);
                                progress_all.setMax(total);
                                txt_correct_num.setText(String.format("%s %d", mContext.getString(R.string.correct_upload), count_ok));
                                txt_error_num.setText(String.format("%s %d", mContext.getString(R.string.upload_error), count_error));
                                PujarFoto(files[index],mContext);
                            }
                            else{
                                lay_all.setVisibility(View.GONE);
                                count_ok=0;
                                count_error=0;
                                index=0;
                                total=0;
                                Refresh();
                            }

                        }
                    }
                });
            }
            else{
                btn_send_all.setVisibility(View.GONE);
            }
        }



    }

    public static void Refresh(){



        File path = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), "/Boka2/upload/error");
        if(path.exists()) {
            String[] fileNames = path.list();
            final File[] files = path.listFiles();
            int i=0;


            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            llistat.setLayoutManager(mLayoutManager);


            // specify an adapter (see also next example)
            PujarFotos_Adapter mAdapter = new PujarFotos_Adapter(mContext,files);
            llistat.setAdapter(mAdapter);

            StopLoading();

            if(files.length>0){
                btn_send_all.setVisibility(View.VISIBLE);

                btn_send_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        txt_progress.setText(String.format("%s / %d",index, total));
                        progress_all.setMax(total);
                        progress_all.setProgress(index);
                        txt_correct_num.setText(String.format("%s %d", mContext.getString(R.string.correct_upload), count_ok));
                        txt_error_num.setText(String.format("%s %d", mContext.getString(R.string.upload_error), count_error));
                        canceled=false;
                        lay_all.setVisibility(View.VISIBLE);
                        total=files.length;
                        index=0;
                        result_BOO.setBoo(true);


                    }
                });


                result_BOO.setListener(new ChangeListener() {
                    @Override
                    public void onChange () {
                        if ( result_BOO.getBoo() ) {
                            result_BOO.setBoo(false);

                            if(index<total&&!canceled){
                                txt_progress.setText(String.format("%s / %d",index, total));
                                progress_all.setProgress(index);
                                progress_all.setMax(total);
                                txt_correct_num.setText(String.format("%s %d", mContext.getString(R.string.correct_upload), count_ok));
                                txt_error_num.setText(String.format("%s %d", mContext.getString(R.string.upload_error), count_error));
                                PujarFoto(files[index],mContext);
                            }
                            else{
                                lay_all.setVisibility(View.GONE);
                                count_ok=0;
                                count_error=0;
                                index=0;
                                total=0;
                                Refresh();
                            }

                        }
                    }
                });
            }
            else{
                btn_send_all.setVisibility(View.GONE);
            }
        }
        else{
            StopLoading();
        }

    }

    public static void StartLoading(){
        loading.setVisibility(View.VISIBLE);
    }

    public static void StopLoading(){
        loading.setVisibility(View.GONE);
    }


    public static void Message(Integer Result){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        if(Result==0){
            alertDialogBuilder.setTitle(mContext.getString(R.string.atencio))
                    .setMessage(mContext.getString(R.string.sendFoto_failed_title))
                    .setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
        else if(Result==1){
            alertDialogBuilder.setTitle(mContext.getString(R.string.atencio))
                    .setMessage(mContext.getString(R.string.sendFoto_succesfull_title))
                    .setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }


    }

    private static void PujarFoto(final File file, Context aContext){
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

                            File direct = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), "/Boka2/upload/done");

                            if (!direct.exists()) {
                                File wallpaperDirectory =new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), "/Boka2/upload/done");
                                wallpaperDirectory.mkdirs();
                                Log.d("Formulari_create"," upload/done created");
                            }

                            File from =  new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), "/Boka2/upload/error/" + file.getName());
                            File to = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), "/Boka2/upload/done/" + file.getName());
                            boolean success =false;
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                                try {
                                    success=Files.move(from.toPath(), to.toPath()).toFile().exists();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                success = from.renameTo(to);
                            }

                            Log.d("Formulari_move","  success="+success+"\nfrom.exists()="+ from.exists()+"\nto.canWrite()="+ to.canWrite()+"\nto.canRead="+ to.canRead()+"\nfrom.canRead="+ from.canRead());
                            Log.d("Formulari", "Ok PujaFoto: "+file.getName()+" new place: "+to.getAbsolutePath());
                            count_ok++;
                            index++;
                            result_BOO.setBoo(true);
                        }

                        @Override
                        public void onError(int error) {
                            Log.e("Formulari", "Error PujaFoto: " + error);
                            count_error++;
                            index++;
                            result_BOO.setBoo(true);

                        }
                    });

        }
        catch (Exception e) {
            e.printStackTrace();
            count_error++;
            index++;
            result_BOO.setBoo(true);
        }
    }


}