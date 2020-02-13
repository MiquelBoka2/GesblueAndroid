package com.sixtemia.gesbluedroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sixtemia.gesbluedroid.R;

public class Login_Admin extends AppCompatActivity {

    private Button btn_Conecta,hack;
    private Context LContext= this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);


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



}
