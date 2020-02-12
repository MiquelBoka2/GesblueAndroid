package com.sixtemia.gesbluedroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.databinding.ActivityLoginBinding;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques.LoginResponse;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.sbaseobjects.objects.SFragment;
import com.sixtemia.sbaseobjects.objects.SFragmentActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Method;

public class Opcions extends AppCompatActivity {

    private ActivityLoginBinding mBinding;
    private boolean refreshDades;
    LoginResponse responseManual;
    
    private ConstraintLayout Canviar_Concessio,Desconectat, Recarregar_Dades, Reimpressio, Idioma, Enviaments_Pendents,Admin,Extres,Base;
    private TextView txt_Versio;
    private Button btn_Confirmar;
    private Context oContext=this;
    private String estat="",adm="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        setContentView(R.layout.activity_opcions);

        Base=(ConstraintLayout) findViewById(R.id.lay_Base);

        txt_Versio=(TextView) findViewById(R.id.txt_Versio);


        Canviar_Concessio = (ConstraintLayout) findViewById(R.id.lay_canviarConcessio);
        Desconectat=(ConstraintLayout) findViewById(R.id.lay_Desconectat);
        Recarregar_Dades = (ConstraintLayout) findViewById(R.id.lay_RecarregarDades);
        Reimpressio = (ConstraintLayout) findViewById(R.id.lay_Reimpressio);
        Idioma = (ConstraintLayout) findViewById(R.id.lay_Idioma);
        Enviaments_Pendents = (ConstraintLayout) findViewById(R.id.lay_EnviamentsPendents);
        Admin = (ConstraintLayout) findViewById(R.id.lay_Admin);

        btn_Confirmar=(Button)findViewById(R.id.btn_Confirmar_Opcions);


        Canviar_Concessio.setVisibility(View.GONE);
        Desconectat.setVisibility(View.GONE);
        Recarregar_Dades.setVisibility(View.GONE);
        Reimpressio.setVisibility(View.GONE);
        Idioma.setVisibility(View.GONE);
        Enviaments_Pendents.setVisibility(View.GONE);
        Admin.setVisibility(View.GONE);

        //Versio
        {
            String menuTitle = "";
            try {
                PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                menuTitle = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            txt_Versio.setText("v."+menuTitle);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            estat = extras.getString("estat", "");
            adm=extras.getString("adm", adm);


        }

        if(estat.equals("main")){


            Desconectat.setVisibility(View.VISIBLE);
            Reimpressio.setVisibility(View.VISIBLE);
            Idioma.setVisibility(View.VISIBLE);
            Enviaments_Pendents.setVisibility(View.VISIBLE);
            Admin.setVisibility(View.VISIBLE);










        }



        else if(estat.equals("no_login_concessio")){


            Idioma.setVisibility(View.VISIBLE);





        }
        else if (estat.equals("login_concessio")){

            Canviar_Concessio.setVisibility(View.VISIBLE);
            Recarregar_Dades.setVisibility(View.VISIBLE);

            Idioma.setVisibility(View.VISIBLE);








        }




        Canviar_Concessio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                Intent intent = new Intent(oContext,LoginActivity.class);
                intent.putExtra("result","unlog");
                setResult(RESULT_OK, intent);
                finish();

            }
        });


        Recarregar_Dades.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(oContext,LoginActivity.class);
                intent.putExtra("result","refresh");
                setResult(RESULT_OK, intent);
                finish();

            }
        });


        Reimpressio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


        Idioma.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                Intent intent = new Intent(oContext,Idiomes.class);
                startActivity(intent);
            }
        });

        Enviaments_Pendents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });



        btn_Confirmar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(estat.equals("login_concessio")||estat.equals("no_login_concessio")){
                    Intent intent = new Intent(oContext,LoginActivity.class);
                    intent.putExtra("result","");
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    onBackPressed();
                }


            }
        });


    }




    @Override
    protected void onResume() {
        super.onResume();





    }
}