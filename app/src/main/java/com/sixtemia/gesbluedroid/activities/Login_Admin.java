package com.sixtemia.gesbluedroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

public class Login_Admin extends AppCompatActivity {

    private Button btn_Conecta,hack;
    private Context LContext= this;
    private Boolean adm,NO_Concessio;
    private ConstraintLayout lay_AdminLogged,lay_ForLoginActivity,lay_AdminOpcions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login_admin);

        lay_AdminLogged=findViewById(R.id.lay_Admin_Logged);

        lay_ForLoginActivity=findViewById(R.id.lay_AdminLoginActivity);
        lay_AdminOpcions=findViewById(R.id.lay_AdminOpcions);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            adm=extras.getBoolean("adm");
            if(extras.get("concessio")!=null){
                NO_Concessio=extras.getBoolean("concessio",true);
            }


        }

        if(adm){

            AdminActivat();

        }
        else if(NO_Concessio!=null){

            FakeFragment();
        }
        else{

            Habitual_Activity();

        }





        btn_Conecta = (Button)findViewById(R.id.btn_conectar_admin);






        hack = (Button)findViewById(R.id.btn_Trampes);



        btn_Conecta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LContext,Opcions.class);
                intent.putExtra("adm",true);
                setResult(RESULT_OK, intent);
                finish();

            }
        });


        hack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LContext,Opcions.class);
                intent.putExtra("adm",true);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }


    //Mostra el layout que te el login fals per sota
    private void FakeFragment() {

        lay_AdminOpcions.setVisibility(View.GONE);
        lay_AdminLogged.setVisibility(View.GONE);
        lay_ForLoginActivity.setVisibility(View.VISIBLE);

        if (NO_Concessio){
            findViewById(R.id.textViewConcessio).setVisibility(View.VISIBLE);
            findViewById(R.id.editTextConcessio).setVisibility(View.VISIBLE);
            findViewById(R.id.viewSwitcherTancaConcessio).setVisibility(View.GONE);


        }
        else{
            findViewById(R.id.textViewConcessio).setVisibility(View.GONE);
            findViewById(R.id.editTextConcessio).setVisibility(View.GONE);
            findViewById(R.id.viewSwitcherTancaConcessio).setVisibility(View.VISIBLE);

        }



    }


    //Mostra el layout de quant vens d'opcions amb el Admin ja activat
    private void AdminActivat() {
        lay_AdminOpcions.setVisibility(View.GONE);
        lay_AdminLogged.setVisibility(View.VISIBLE);
        lay_ForLoginActivity.setVisibility(View.GONE);
    }


    //Mostra el layout de quant vens d'opcions sense admin
    private void Habitual_Activity() {
        lay_AdminOpcions.setVisibility(View.VISIBLE);
        lay_AdminLogged.setVisibility(View.GONE);
        lay_ForLoginActivity.setVisibility(View.GONE);

    }

}
