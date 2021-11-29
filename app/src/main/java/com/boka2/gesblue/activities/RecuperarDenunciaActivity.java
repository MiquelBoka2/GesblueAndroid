package com.boka2.gesblue.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.boka2.gesblue.Boka2ols.Kotlin_Utils;
import com.boka2.gesblue.R;
import com.boka2.gesblue.Sancio;
import com.boka2.gesblue.adapters.DenunciaAdapter;
import com.boka2.gesblue.adapters.Holder_denuncia_adapter;
import com.boka2.gesblue.datamanager.DatabaseAPI;
import com.boka2.gesblue.datamanager.database.listeners.CustomButtonListener;
import com.boka2.gesblue.datamanager.database.model.Model_Agent;
import com.boka2.gesblue.datamanager.database.model.Model_Carrer;
import com.boka2.gesblue.datamanager.database.model.Model_Color;
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;
import com.boka2.gesblue.datamanager.database.model.Model_Infraccio;
import com.boka2.gesblue.datamanager.database.model.Model_Marca;
import com.boka2.gesblue.datamanager.database.model.Model_Model;
import com.boka2.gesblue.datamanager.database.model.Model_TipusVehicle;
import com.boka2.gesblue.datamanager.database.model.Model_Zona;
import com.boka2.gesblue.global.PreferencesGesblue;
import com.boka2.gesblue.global.Utils;
import com.boka2.gesblue.model.Model_Group_Denuncies;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RecuperarDenunciaActivity extends AppCompatActivity implements CustomButtonListener,SearchView.OnQueryTextListener {
    private static RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static View.OnClickListener myOnClickListener;
    private List<Model_Denuncia> denuncies_Personals;
    private  List<Model_Group_Denuncies> llistat_agrupat;
    public Context mContext;
    private Button back_BTN;
    private ImageButton btn_hide_or_show;
    private EditText edt_Mat;
    private ConstraintLayout lay_filtre_matricula;
    public final static int INTENT_RETORN=1547;

    private Boolean adm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_denuncia);
        mContext = this;

        /*RECUPAREM DADES D'ON PROVENIM**/{
            Bundle extras = getIntent().getExtras();

            if (extras != null) {

                adm=extras.getBoolean("adm");

            }

        }

        back_BTN=this.findViewById(R.id.btn_Back);
        back_BTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("adm",adm);
                startActivity(intent);
                finish();

            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        lay_filtre_matricula= (ConstraintLayout) findViewById(R.id.lay_filtre_matricula);
        edt_Mat= (EditText) findViewById(R.id.edt_Mat);
        btn_hide_or_show= (ImageButton) findViewById(R.id.btn_hide_or_show);




        List<Model_Denuncia> denunciesTemp = DatabaseAPI.getDenuncies(mContext);
        denuncies_Personals = DatabaseAPI.getDenuncies(mContext);
        denuncies_Personals.clear();

        final CustomButtonListener list=this;
        if(!adm){/*NORMAL: TOTES LES SEVES DE LA CONCESSIO*/
            for(int i=0;i<denunciesTemp.size();i++){
                String Agentid= PreferencesGesblue.getAgentId(mContext);
                Long IDAgent= PreferencesGesblue.getIdAgent(mContext);
                Long CodiAgent= PreferencesGesblue.getCodiAgent(mContext);
                double agent_Denuncia=denunciesTemp.get(i).getAgent();
                if(denunciesTemp.get(i).getAgent()== PreferencesGesblue.getIdAgent(mContext) && denunciesTemp.get(i).getConcessio()==PreferencesGesblue.getConcessio(mContext)){
                    denuncies_Personals.add(denunciesTemp.get(i));
                }
            }



        }
        else{/*ADMIN: TOTES LA DE LA CONCESSIO*/
            for(int i=0;i<denunciesTemp.size();i++){
                String Agentid= PreferencesGesblue.getAgentId(mContext);
                Long IDAgent= PreferencesGesblue.getIdAgent(mContext);
                Long CodiAgent= PreferencesGesblue.getCodiAgent(mContext);
                double agent_Denuncia=denunciesTemp.get(i).getAgent();
                if(denunciesTemp.get(i).getConcessio()==PreferencesGesblue.getConcessio(mContext)){
                    denuncies_Personals.add(denunciesTemp.get(i));
                }
            }
        }
        Collections.reverse(denuncies_Personals);
        llistat_agrupat= Kotlin_Utils.Companion.Denuncies_Filter(denuncies_Personals,"");
        //Comprovem si tenim denuncies i en cas negatiu, mostrem un missatge informatiu.
        if ( denuncies_Personals.isEmpty() || denuncies_Personals.size()<=0){
            Utils.showCustomDatamanagerError(mContext, getString(R.string.noDenuncies));
        }else {
            mAdapter = new Holder_denuncia_adapter(this, llistat_agrupat, denuncies_Personals,list,true);
            mRecyclerView.setAdapter(mAdapter);
        }


        btn_hide_or_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lay_filtre_matricula.getVisibility()==View.VISIBLE){
                    btn_hide_or_show.setImageResource(R.drawable.ic_eye_open);
                    lay_filtre_matricula.setVisibility(View.GONE);
                }
                else{
                    btn_hide_or_show.setImageResource(R.drawable.ic_eye_off);
                    lay_filtre_matricula.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_Mat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                llistat_agrupat = Kotlin_Utils.Companion.Denuncies_Filter(denuncies_Personals, s.toString().toUpperCase());
                //Comprovem si tenim denuncies i en cas negatiu, mostrem un missatge informatiu.
                mAdapter = new Holder_denuncia_adapter(mContext, llistat_agrupat, denuncies_Personals,list,false);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }**/

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filter logic
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public void onButtonClickListener(int position) {
        Model_Denuncia denuncia = denuncies_Personals.get(position);
        Log.d("Element eleccionat:",""+position);
        Intent intent = new Intent(mContext, ReimpressioTiquet.class);

        //ArrayList<Model_TipusVehicle> vehicles = DatabaseAPI.getTipusVehicles(mContext);
        Model_TipusVehicle tipusVehicle = DatabaseAPI.getTipusVehicle(mContext,String.valueOf((int)denuncia.getTipusvehicle()));
        Model_Marca marca = DatabaseAPI.getMarca(mContext,String.valueOf((int)denuncia.getMarca()));
        Model_Model model = DatabaseAPI.getModel(mContext,String.valueOf((int)denuncia.getModel()));
        Model_Color color = DatabaseAPI.getColor(mContext,String.valueOf((int)denuncia.getColor()));
        Model_Infraccio infraccio = DatabaseAPI.getInfraccio(mContext,String.valueOf((int)denuncia.getInfraccio()));
        Model_Carrer carrer = DatabaseAPI.getCarrer(mContext,String.valueOf((int)denuncia.getAdrecacarrer()));
        Model_Zona zona = DatabaseAPI.getZona(mContext,String.valueOf((int)denuncia.getZona()));
        Sancio mSancio =new Sancio();
        if(denuncia.getAdrecanum()==0)
        {
            mSancio = new Sancio(denuncia.getMatricula(),"",tipusVehicle,marca,model,color,infraccio,carrer,zona);
        }
        else{
            mSancio = new Sancio(denuncia.getMatricula(),String.valueOf(((int)denuncia.getAdrecanum())),tipusVehicle,marca,model,color,infraccio,carrer,zona);
        }
        intent.putExtra(ReimpressioTiquet.INTENT_SANCIO, mSancio);
        intent.putExtra(ReimpressioTiquet.INTENT_NUM_DENUNCIA,denuncia.getCodidenuncia());
        intent.putExtra(ReimpressioTiquet.INTENT_DATA_CREACIO,denuncia.getFechacreacio());
        intent.putExtra(ReimpressioTiquet.INTENT_DENUNCIA, denuncia);





        intent.putExtra(ReimpressioTiquet.INTENT_RECUPERADA,true);
        startActivityForResult(intent,INTENT_RETORN);
    }

    /*EL RETORN**/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        recreate();
    }


}
