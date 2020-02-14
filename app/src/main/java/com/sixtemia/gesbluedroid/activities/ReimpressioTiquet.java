package com.sixtemia.gesbluedroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.Sancio;
import com.sixtemia.gesbluedroid.activities.passosformulari.Pas1TipusActivity;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Carrer;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;

import java.io.File;
import java.io.FileFilter;
import java.sql.Array;
import java.util.Arrays;
import java.util.Date;
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

    public static final String INTENT_RECUPERADA = "recuperada";
    public static final String INTENT_SANCIO = "sancio";
    public static final String INTENT_NUM_DENUNCIA = "numDenuncia";
    public static final String INTENT_DATA_CREACIO = "dataCreacio";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimpressio_denuncia);
        loadAll();
        getIntentAndFill();
        configSpiner();














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
}
