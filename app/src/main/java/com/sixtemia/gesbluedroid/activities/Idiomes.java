package com.sixtemia.gesbluedroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Configuration;
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
    String locale;


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


        // (locale.equals("")) {
            locale = Locale.getDefault().getLanguage();
        //}

        if(locale.equals("ca")){
            rb_Cat.setChecked(true);
        }
        else if(locale.equals("es")){
            rb_Esp.setChecked(true);
        }
        else if(locale.equals("Extra0")){
            rb_Extra0.setChecked(true);
        }
        else if(locale.equals("Extra1")){
            rb_Extra1.setChecked(true);
        }
        else if(locale.equals("Extra2")){
            rb_Extra2.setChecked(true);
        }
        else{
            rb_Cat.setChecked(true);
        }


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

        }}



        lay_Cat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rb_Cat.setChecked(true);
                rb_Esp.setChecked(false);
                rb_Extra0.setChecked(false);
                rb_Extra1.setChecked(false);
                rb_Extra2.setChecked(false);


                //editor.putString("locale","ca");
                //editor.apply();


                setLocale("ca");
                recreate();
            }
        });


        lay_Esp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rb_Cat.setChecked(false);
                rb_Esp.setChecked(true);
                rb_Extra0.setChecked(false);
                rb_Extra1.setChecked(false);
                rb_Extra2.setChecked(false);


                setLocale("es");
                recreate();
            }
        });

        lay_Extra0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rb_Cat.setChecked(false);
                rb_Esp.setChecked(false);
                rb_Extra0.setChecked(true);
                rb_Extra1.setChecked(false);
                rb_Extra2.setChecked(false);



            }
        });

        lay_Extra1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rb_Cat.setChecked(false);
                rb_Esp.setChecked(false);
                rb_Extra0.setChecked(false);
                rb_Extra1.setChecked(true);
                rb_Extra2.setChecked(false);

            }
        });

        lay_Extra2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                onBackPressed();

            }
        });

    }


    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, Idiomes.class);
        finish();
        startActivity(refresh);
    }

}
