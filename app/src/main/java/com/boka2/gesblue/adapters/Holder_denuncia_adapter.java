package com.boka2.gesblue.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boka2.gesblue.R;
import com.boka2.gesblue.datamanager.database.listeners.CustomButtonListener;
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia;
import com.boka2.gesblue.model.Model_Group_Denuncies;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * Created by Boka2.
 */
;

public class Holder_denuncia_adapter extends RecyclerView.Adapter<Holder_denuncia_adapter.HolderDenunciaViewHolder>{

    //this context we will use to inflate the layout
    private Context mCtx;



    private CustomButtonListener customButtonListener;






    //we are storing all the denuncias in a list
    private List<Model_Group_Denuncies> denunciaList;
    private List<Model_Denuncia> denuncies;
    private Boolean auto_show;

    //getting the context and denuncia list with constructor
    public Holder_denuncia_adapter(Context mCtx, List<Model_Group_Denuncies> denunciaList, List<Model_Denuncia> denuncies, CustomButtonListener customButtonListener, Boolean auto_show) {
        this.mCtx = mCtx;
        this.denunciaList = denunciaList;
        this.customButtonListener = customButtonListener;
        this.denuncies=denuncies;
        this.auto_show=auto_show;
    }

    @Override
    public HolderDenunciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_group_deniuncies, parent,false);
        return new HolderDenunciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HolderDenunciaViewHolder holder, final int position) {
        //getting the denuncia of the specified position
        final Model_Group_Denuncies denuncia = denunciaList.get(position);

        //binding the data with the viewholder views

        holder.txt_date.setText(denuncia.getDate());
        holder.txt_total.setText(mCtx.getString(R.string.total)+" "+denuncia.getLlistat().size());
        holder.btn_hide_or_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.lay_list.getVisibility()==View.VISIBLE){
                    holder.btn_hide_or_show.setImageResource(R.drawable.ic_eye_open);
                    holder.lay_list.setVisibility(View.GONE);
                }
                else{
                    holder.btn_hide_or_show.setImageResource(R.drawable.ic_eye_off);
                    holder.lay_list.setVisibility(View.VISIBLE);

                    holder.recycler_denuncies.setLayoutManager(new LinearLayoutManager(mCtx));
                    DenunciaAdapter mAdapter = new DenunciaAdapter(mCtx,denuncia.getLlistat(),denuncies,customButtonListener);
                    holder.recycler_denuncies.setAdapter(mAdapter);
                }
            }
        });

        holder.txt_PI.setText(mCtx.getString(R.string.min_Pendents_Imprimir)+": "+denuncia.getPendents_imprimir());
        holder.txt_PE.setText(mCtx.getString(R.string.min_Pendents_Enviar)+": "+denuncia.getPendents_enviar());
        holder.txt_Env.setText(mCtx.getString(R.string.min_Enviades)+": "+denuncia.getEnviades());
        holder.txt_EF.setText(mCtx.getString(R.string.min_Fallides)+": "+denuncia.getFallides());

        if(position==0&& auto_show){
            holder.recycler_denuncies.setLayoutManager(new LinearLayoutManager(mCtx));
            DenunciaAdapter mAdapter = new DenunciaAdapter(mCtx,denuncia.getLlistat(),denuncies,customButtonListener);
            holder.recycler_denuncies.setAdapter(mAdapter);
            holder.lay_list.setVisibility(View.VISIBLE);

        }
        else{
            holder.lay_list.setVisibility(View.GONE);
        }



    }


    @Override
    public int getItemCount() {
        return denunciaList.size();
    }


    class HolderDenunciaViewHolder extends RecyclerView.ViewHolder {

        TextView txt_date, txt_total, txt_PI,txt_PE,txt_Env,txt_EF;
        ImageButton btn_hide_or_show;
        ConstraintLayout lay_list;
        RecyclerView recycler_denuncies;
        public HolderDenunciaViewHolder(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_total = (TextView) itemView.findViewById(R.id.txt_total);

            txt_PI = (TextView) itemView.findViewById(R.id.txt_PI);
            txt_PE = (TextView) itemView.findViewById(R.id.txt_PE);
            txt_Env = (TextView) itemView.findViewById(R.id.txt_Env);
            txt_EF = (TextView) itemView.findViewById(R.id.txt_EF);

            btn_hide_or_show = (ImageButton) itemView.findViewById(R.id.btn_hide_or_show);
            lay_list= (ConstraintLayout) itemView.findViewById(R.id.lay_list);
            recycler_denuncies = (RecyclerView) itemView.findViewById(R.id.recycler_denuncies);
        }
        /*@Override
        public void onClick(View view) {
            Log.d("Miquel", "onClick " + getPosition());
        }*/
    }
}
