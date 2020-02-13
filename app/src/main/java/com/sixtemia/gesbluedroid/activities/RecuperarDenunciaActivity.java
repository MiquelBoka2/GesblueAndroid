package com.sixtemia.gesbluedroid.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sixtemia.gesbluedroid.R;
import com.sixtemia.gesbluedroid.Sancio;
import com.sixtemia.gesbluedroid.adapters.DenunciaAdapter;
import com.sixtemia.gesbluedroid.datamanager.DatabaseAPI;
import com.sixtemia.gesbluedroid.datamanager.database.listeners.CustomButtonListener;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Carrer;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Color;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Denuncia;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Infraccio;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Marca;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Model;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_TipusVehicle;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_Zona;
import com.sixtemia.gesbluedroid.global.Utils;

import java.util.Collections;
import java.util.List;

public class RecuperarDenunciaActivity extends AppCompatActivity implements CustomButtonListener,SearchView.OnQueryTextListener {
    private static RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static View.OnClickListener myOnClickListener;
    private List<Model_Denuncia> denuncies;
    public Context mContext;
    private Button back_BTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_denuncia);

        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        back_BTN=this.findViewById(R.id.btn_Back);
        back_BTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();

            }
                                    }


        );

        List<Model_Denuncia> denunciesTemp = DatabaseAPI.getDenuncies(mContext);

        Collections.reverse(denunciesTemp);

        denuncies = denunciesTemp.subList(0,Math.min(denunciesTemp.size(),50));

        //Comprovem si tenim denuncies i en cas negatiu, mostrem un missatge informatiu.
        if (denuncies.size()<=0 || denuncies.isEmpty()){
            Utils.showCustomDatamanagerError(mContext, getString(R.string.noDenuncies));
        }

        // specify an adapter (see also next example)
        mAdapter = new DenunciaAdapter(this,denuncies,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**@Override
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
        Model_Denuncia denuncia = denuncies.get(position);
        Log.d("Element eleccionat:",""+position);
        Intent intent = new Intent(mContext, FormulariActivity.class);

        //ArrayList<Model_TipusVehicle> vehicles = DatabaseAPI.getTipusVehicles(mContext);
        Model_TipusVehicle tipusVehicle = DatabaseAPI.getTipusVehicle(mContext,String.valueOf((int)denuncia.getTipusvehicle()));
        Model_Marca marca = DatabaseAPI.getMarca(mContext,String.valueOf((int)denuncia.getMarca()));
        Model_Model model = DatabaseAPI.getModel(mContext,String.valueOf((int)denuncia.getModel()));
        Model_Color color = DatabaseAPI.getColor(mContext,String.valueOf((int)denuncia.getColor()));
        Model_Infraccio infraccio = DatabaseAPI.getInfraccio(mContext,String.valueOf((int)denuncia.getInfraccio()));
        Model_Carrer carrer = DatabaseAPI.getCarrer(mContext,String.valueOf((int)denuncia.getAdrecacarrer()));
        Model_Zona zona = DatabaseAPI.getZona(mContext,String.valueOf((int)denuncia.getZona()));
        Sancio mSancio = new Sancio(denuncia.getMatricula(),String.valueOf(denuncia.getAdrecanum()),tipusVehicle,marca,model,color,infraccio,carrer,zona);
        intent.putExtra(FormulariActivity.INTENT_SANCIO, mSancio);
        intent.putExtra(FormulariActivity.INTENT_NUM_DENUNCIA,denuncia.getCodidenuncia());
        intent.putExtra(FormulariActivity.INTENT_DATA_CREACIO,denuncia.getFechacreacio());





        intent.putExtra(FormulariActivity.INTENT_RECUPERADA,true);
        startActivity(intent);
    }


}
