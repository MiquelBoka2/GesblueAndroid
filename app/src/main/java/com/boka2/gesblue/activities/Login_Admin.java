package com.boka2.gesblue.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.boka2.gesblue.R;

public class Login_Admin extends AppCompatActivity {

    private Button hack;
    private Context LContext= this;
    private Boolean adm,NO_Concessio;
    private ConstraintLayout lay_AdminLogged,lay_FakeFons,lay_AdminLoginActivity;
    private TextView txt_Accio;
    private TextView btn_Accepta,btn_Anula,tbox_DadesIncorrectes;
    private String Tipus_Accio;

    private EditText txt_pass,txt_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login_admin);

        lay_AdminLogged=findViewById(R.id.lay_Admin_Logged);
        lay_AdminLoginActivity=findViewById(R.id.lay_AdminLoginActivity);

        lay_FakeFons=findViewById(R.id.fake_fons);

        txt_Accio=findViewById(R.id.txt_Accio);

        btn_Accepta=findViewById(R.id.txt_ButoAccepta);
        btn_Anula=findViewById(R.id.txt_ButoAnula);


        txt_pass=findViewById(R.id.txt_pass);
        txt_user=findViewById(R.id.txt_user);

        tbox_DadesIncorrectes=(TextView)findViewById(R.id.tbox_Incorecte);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            adm=extras.getBoolean("adm");
            if(extras.get("concessio")!=null){
                NO_Concessio=extras.getBoolean("concessio",true);
                Tipus_Accio=extras.getString("accio");
            }


        }


        /*TRIA EL DISENY**/
        if(adm){

            AdminActivat();

        }
        else if(NO_Concessio!=null){

            FakeFragment();
            if(Tipus_Accio.equals("Esborra_APP")){
                txt_Accio.setText(R.string.accio_eliminar_tot);
            }
            else{
                txt_Accio.setText(R.string.accio_esborrar_concessions);
            }

        }
        else{

            Habitual_Activity();

        }


















        btn_Accepta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_pass.getText().toString().equals("mifasTIC.2020p@ss") & txt_user.getText().toString().equals("mifas")){
                    Intent intent = new Intent(LContext,Opcions.class);
                    intent.putExtra("adm",true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    tbox_DadesIncorrectes.setVisibility(View.VISIBLE);


                }
            }
        });



        btn_Anula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LContext,Opcions.class);
                intent.putExtra("adm",false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    //Mostra el layout que te el login fals per sota
    private void FakeFragment() {


        lay_AdminLogged.setVisibility(View.GONE);
        lay_FakeFons.setVisibility(View.VISIBLE);
        txt_Accio.setVisibility(View.VISIBLE);

        if (NO_Concessio){

            findViewById(R.id.lay_Concessio).setVisibility(View.VISIBLE);
            findViewById(R.id.lay_Login).setVisibility(View.GONE);


        }
        else{

            findViewById(R.id.lay_Concessio).setVisibility(View.GONE);
            findViewById(R.id.lay_Login).setVisibility(View.VISIBLE);

        }



    }


    //Mostra el layout de quant vens d'opcions amb el Admin ja activat
    private void AdminActivat() {
        lay_AdminLoginActivity.setVisibility(View.GONE);
        lay_AdminLogged.setVisibility(View.VISIBLE);

    }


    //Mostra el layout de quant vens d'opcions sense admin
    private void Habitual_Activity() {
        lay_FakeFons.setVisibility(View.GONE);
        lay_AdminLogged.setVisibility(View.GONE);
        txt_Accio.setVisibility(View.GONE);


    }

}
