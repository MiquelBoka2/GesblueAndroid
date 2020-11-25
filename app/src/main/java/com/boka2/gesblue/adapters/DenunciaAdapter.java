package com.boka2.gesblue.adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boka2.gesblue.R;
import com.boka2.gesblue.datamanager.database.listeners.CustomButtonListener;
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;

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

    //getting the context and denuncia list with constructor
    public DenunciaAdapter(Context mCtx, List<Model_Denuncia> denunciaList, CustomButtonListener customButtonListener) {
        this.mCtx = mCtx;
        this.denunciaList = denunciaList;
        this.customButtonListener = customButtonListener;
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
        Model_Denuncia denuncia = denunciaList.get(position);

        //binding the data with the viewholder views

        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date data=denuncia.getFechacreacio();
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
            holder.textViewEstat.setText(text);

        holder.cardViewDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customButtonListener!=null)
                {
                    customButtonListener.onButtonClickListener(position);
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
