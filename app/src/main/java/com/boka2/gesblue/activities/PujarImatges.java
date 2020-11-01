package com.boka2.gesblue.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.boka2.gesblue.R;
import com.boka2.gesblue.adapters.DenunciaAdapter;
import com.boka2.gesblue.adapters.PujarFotos_Adapter;
import com.boka2.gesblue.datamanager.webservices.DatamanagerAPI;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.PujaFotoRequest;
import com.boka2.gesblue.global.PreferencesGesblue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;

import java.io.File;
import java.nio.charset.StandardCharsets;

import pt.joaocruz04.lib.misc.JSoapCallback;

public class PujarImatges extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pujar_imatges);


        RecyclerView llistat= this.findViewById(R.id.recycler);


        File path = new File("storage/emulated/0/Boka2/upload/error");

        if(path.exists()) {
            String[] fileNames = path.list();
            final File[] files = path.listFiles();
            int i=0;

            // specify an adapter (see also next example)
            PujarFotos_Adapter mAdapter = new PujarFotos_Adapter(this,files);
            llistat.setAdapter(mAdapter);





        }









    }
}