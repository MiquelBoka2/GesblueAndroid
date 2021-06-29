package com.boka2.gesblue.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.boka2.gesblue.R;
import com.boka2.gesblue.adapters.DenunciaAdapter;
import com.boka2.gesblue.adapters.PujarFotos_Adapter;
import com.boka2.gesblue.datamanager.webservices.DatamanagerAPI;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.PujaFotoRequest;
import com.boka2.gesblue.global.PreferencesGesblue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;

import java.io.File;
import java.nio.charset.StandardCharsets;

import pt.joaocruz04.lib.misc.JSoapCallback;

public class PujarImatges extends AppCompatActivity {

    public static RecyclerView llistat;
    public static ProgressBar loading;
    public static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pujar_imatges);

        mContext=this;
        llistat= this.findViewById(R.id.recycler);
        loading=this.findViewById(R.id.loading_bar);
        loading.setVisibility(View.GONE);
        loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/error");;
        if(path.exists()) {
            String[] fileNames = path.list();
            final File[] files = path.listFiles();
            int i=0;


            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            llistat.setLayoutManager(mLayoutManager);


            // specify an adapter (see also next example)
            PujarFotos_Adapter mAdapter = new PujarFotos_Adapter(this,files);
            llistat.setAdapter(mAdapter);
        }



    }

    public static void Refresh(){



        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Boka2/upload/error");;
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
}