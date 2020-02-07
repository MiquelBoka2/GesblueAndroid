package com.sixtemia.gesbluedroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Opcions extends AppCompatActivity {

    private ConstraintLayout Canviar_Concessio, Recarregar_Dades, Reimpressio, Idioma, Enviaments_Pendents,Extres,Base;
    private Button btn_Confirmar;

    private String estat="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcions);

        Base=(ConstraintLayout) findViewById(R.id.lay_Base);


        Canviar_Concessio = (ConstraintLayout) findViewById(R.id.lay_canviarConcessio);
        Recarregar_Dades = (ConstraintLayout) findViewById(R.id.lay_RecarregarDades);
        Reimpressio = (ConstraintLayout) findViewById(R.id.lay_Reimpressio);
        Idioma = (ConstraintLayout) findViewById(R.id.lay_Idioma);
        Enviaments_Pendents = (ConstraintLayout) findViewById(R.id.lay_EnviamentsPendents);
        Extres=(ConstraintLayout) findViewById(R.id.lay_Extres);

        btn_Confirmar=(Button)findViewById(R.id.btn_Confirmar_Opcions);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            estat = extras.getString("estat", "");


        }

        if(estat.equals("login")){

            Canviar_Concessio.setVisibility(View.VISIBLE);
            Recarregar_Dades.setVisibility(View.VISIBLE);
            Reimpressio.setVisibility(View.VISIBLE);
            Idioma.setVisibility(View.VISIBLE);
            Enviaments_Pendents.setVisibility(View.VISIBLE);
            Extres.setVisibility(View.VISIBLE);









        }



        else if(estat.equals("no_login")){

            Canviar_Concessio.setVisibility(View.GONE);
            Recarregar_Dades.setVisibility(View.GONE);
            Reimpressio.setVisibility(View.GONE);
            Idioma.setVisibility(View.VISIBLE);
            Enviaments_Pendents.setVisibility(View.GONE);
            Extres.setVisibility(View.VISIBLE);








        }
        else if (estat.equals("main")){







        }

        else if (estat.equals("login")){
            Reimpressio.setVisibility(View.GONE);







        }

        else if (estat.equals("login")){








        }


        Canviar_Concessio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


        Recarregar_Dades.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


        Reimpressio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


        Idioma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        Enviaments_Pendents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        Extres.setOnClickListener(new View.OnClickListener() {


            int color =R.color.colorFons;
            @Override
            public void onClick(View v) {




            }
        });

        btn_Confirmar.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();





    }
}