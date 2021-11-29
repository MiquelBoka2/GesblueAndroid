package com.boka2.gesblue.adapters;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.boka2.gesblue.R;
import com.boka2.gesblue.datamanager.database.listeners.CustomButtonListener;
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;
import com.boka2.gesblue.global.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * Created by Boka2.
 */
;

public class DenunciaAdapter extends RecyclerView.Adapter<DenunciaAdapter.DenunciaViewHolder>{

    //this context we will use to inflate the layout
    private Context mCtx;



    private CustomButtonListener customButtonListener;






    //we are storing all the denuncias in a list
    private List<Model_Denuncia> denunciaList;
    private List<Model_Denuncia> denuncies;

    //getting the context and denuncia list with constructor
    public DenunciaAdapter(Context mCtx, List<Model_Denuncia> denunciaList, List<Model_Denuncia> denuncies, CustomButtonListener customButtonListener) {
        this.mCtx = mCtx;
        this.denunciaList = denunciaList;
        this.customButtonListener = customButtonListener;
        this.denuncies=denuncies;
    }

    @Override
    public DenunciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_denuncia, parent,false);
        return new DenunciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DenunciaViewHolder holder, final int position) {
        //getting the denuncia of the specified position
        final Model_Denuncia denuncia = denunciaList.get(position);

        //binding the data with the viewholder views

        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        final Date data=denuncia.getFechacreacio();
        if(data!=null) {
            holder.textViewFecha.setText(simpleDate.format(denuncia.getFechacreacio()));
        }else{
            holder.textViewFecha.setText("#ERROR DATA");
        }
        holder.textViewMatricula.setText(denuncia.getMatricula());
        holder.textViewNumDenuncia.setText(String.valueOf(denuncia.getCodidenuncia()));
        String text=String.valueOf(denuncia.getTipusanulacio());


        if (String.valueOf(denuncia.getTipusanulacio()).equals("-1.0")){
            text=mCtx.getResources().getString(R.string.pendent_Imprimir);
            holder.textViewEstat.setTextColor(mCtx.getResources().getColor(R.color.vermellKO));
        }
        else if(String.valueOf(denuncia.getTipusanulacio()).equals("0.0")){
            text=mCtx.getResources().getString(R.string.pendent_Enviar);
            holder.textViewEstat.setTextColor(mCtx.getResources().getColor(R.color.grocFosc));

        }
        else if(String.valueOf(denuncia.getTipusanulacio()).equals("1.0")){
            text=mCtx.getResources().getString(R.string.Enviada);

            holder.textViewEstat.setTextColor(mCtx.getResources().getColor(R.color.verdOK));
        }
        else if(String.valueOf(denuncia.getTipusanulacio()).equals("-2.0")){
            text=mCtx.getResources().getString(R.string.denuncia_fallida);

            holder.textViewEstat.setTextColor(mCtx.getResources().getColor(R.color.vermellKO));
        }
            holder.textViewEstat.setText(text);

        holder.cardViewDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customButtonListener!=null)
                {
                    if(data!=null) {
                        customButtonListener.onButtonClickListener(denuncies.indexOf(denuncia));
                    }
                    else{

                    }
                }
            }
        });



    }


    @Override
    public int getItemCount() {
        return denunciaList.size();
    }


    class DenunciaViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFecha, textViewMatricula, textViewNumDenuncia,textViewEstat;
        CardView cardViewDenuncia;
        public DenunciaViewHolder(View itemView) {
            super(itemView);
            textViewFecha = (TextView) itemView.findViewById(R.id.textViewFecha);
            textViewMatricula = (TextView) itemView.findViewById(R.id.textViewMatricula);
            textViewNumDenuncia = (TextView) itemView.findViewById(R.id.textViewNumDenuncia);
            textViewEstat= (TextView) itemView.findViewById(R.id.textViewEstat);
            cardViewDenuncia = (CardView) itemView.findViewById(R.id.cardViewDenuncia);
            //itemView.setOnClickListener(this);
        }
        /*@Override
        public void onClick(View view) {
            Log.d("Miquel", "onClick " + getPosition());
        }*/
    }
}
