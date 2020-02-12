package com.sixtemia.gesbluedroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.IntentCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.sixtemia.gesbluedroid.R;


import java.util.Locale;

import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Idiomes extends AppCompatActivity {

    private ConstraintLayout lay_Cat,lay_Esp,lay_Extra0,lay_Extra1,lay_Extra2;
    private RadioButton rb_Cat,rb_Esp,rb_Extra0,rb_Extra1,rb_Extra2;
    private Button btn_Confirmar;
    boolean click=false;
    Context iContext=this;
    String locale,localeAntic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idiomes);

        lay_Cat=(ConstraintLayout) findViewById(R.id.lay_idioma_Cat);
        lay_Esp=(ConstraintLayout) findViewById(R.id.lay_idioma_Esp);
        lay_Extra0=(ConstraintLayout) findViewById(R.id.lay_idioma_Extra0);
        lay_Extra1=(ConstraintLayout) findViewById(R.id.lay_idioma_Extra1);
        lay_Extra2=(ConstraintLayout) findViewById(R.id.lay_idioma_Extra2);

        rb_Cat=(RadioButton)findViewById(R.id.rb_Cat);
        rb_Esp=(RadioButton)findViewById(R.id.rb_Esp);
        rb_Extra0=(RadioButton)findViewById(R.id.rb_Extra0);
        rb_Extra1=(RadioButton)findViewById(R.id.rb_Extra1);
        rb_Extra2=(RadioButton)findViewById(R.id.rb_Extra2);

        btn_Confirmar=(Button) findViewById(R.id.btn_Confirmar_Idiomes);


        localeAntic = Locale.getDefault().getLanguage();


        locale=Locale.getDefault().getLanguage();

        //Inicialitza els radioButtons
        {
        if (locale.equals("ca")){
            rb_Cat.setChecked(true);
        }
        else if (locale.equals("es")){
            rb_Esp.setChecked(true);
        }
        else if (locale.equals("extra0")){
            rb_Extra0.setChecked(true);
        }
        else if (locale.equals("extra1")){
            rb_Extra1.setChecked(true);
        }
        else if (locale.equals("extra2")){
            rb_Extra2.setChecked(true);

        }
        else{
            rb_Cat.setChecked(true);
        }}



        lay_Cat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click=true;
                rb_Cat.setChecked(true);
                rb_Esp.setChecked(false);
                rb_Extra0.setChecked(false);
                rb_Extra1.setChecked(false);
                rb_Extra2.setChecked(false);



                //editor.putString("locale","ca");
                //editor.apply();


                locale="ca";

            }
        });


        lay_Esp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click=true;
                rb_Cat.setChecked(false);
                rb_Esp.setChecked(true);
                rb_Extra0.setChecked(false);
                rb_Extra1.setChecked(false);
                rb_Extra2.setChecked(false);


                locale="es";

            }
        });

        lay_Extra0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click=true;
                rb_Cat.setChecked(false);
                rb_Esp.setChecked(false);
                rb_Extra0.setChecked(true);
                rb_Extra1.setChecked(false);
                rb_Extra2.setChecked(false);



            }
        });

        lay_Extra1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click=true;
                rb_Cat.setChecked(false);
                rb_Esp.setChecked(false);
                rb_Extra0.setChecked(false);
                rb_Extra1.setChecked(true);
                rb_Extra2.setChecked(false);

            }
        });

        lay_Extra2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                click=true;
                rb_Cat.setChecked(false);
                rb_Esp.setChecked(false);
                rb_Extra0.setChecked(false);
                rb_Extra1.setChecked(false);
                rb_Extra2.setChecked(true);


            }
        });

        rb_Cat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lay_Cat.setOnClickListener(this);

            }
        });


        rb_Esp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lay_Esp.setOnClickListener(this);

            }
        });

        rb_Extra0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lay_Extra0.setOnClickListener(this);

            }
        });
        rb_Extra1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lay_Extra1.setOnClickListener(this);

            }
        });
        rb_Extra2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lay_Extra2.setOnClickListener(this);

            }
        });







        btn_Confirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(click){

                    CanviarIdioma(locale);


                    MissatgeAlerte();


                }
                else{

                    onBackPressed();
                }


            }
        });

    }
    public void CanviarIdioma(String nouIdioma) {

        Locale myLocale = new Locale(nouIdioma);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    public void Reiniciar() {

        Intent refresh = new Intent(this, SplashActivity.class);
        finish();
        startActivity(refresh);
    }

    public void MissatgeAlerte(){

         AlertDialog builder = new AlertDialog.Builder(iContext)
                .setTitle(R.string.MistageAvis)
                .setMessage(R.string.MistageReinici)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.SI, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        Reiniciar();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CanviarIdioma(localeAntic);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert).create();

        builder.show();
        builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.btn_color_negatiu));
        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.btn_color_positiu));





    }
}
