package com.boka2.gesblue.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.boka2.gesblue.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created by Boka2.
 */
;

public class PujarFotos_Adapter extends RecyclerView.Adapter<PujarFotos_Adapter.PujaFotoViewHolder>  {

    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the denuncias in a list
    private File[] Files;

    //getting the context and denuncia list with constructor
    public PujarFotos_Adapter(Context mCtx, File[] Files) {
        this.mCtx = mCtx;
        this.Files = Files;
    }

    @Override
    public PujaFotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_recycler_pujar_imatge, parent,false);
        return new PujaFotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PujaFotoViewHolder holder, final int position) {
        //getting the denuncia of the specified position
        File foto = Files[position];

        //binding the data with the viewholder views
        Bitmap bitmap = BitmapFactory.decodeFile(foto.getPath());
        holder.img_preview.setImageBitmap(bitmap);

        holder.text_nom.setText(foto.getName());



    }


    @Override
    public int getItemCount() {
        return Files.length;
    }


    class PujaFotoViewHolder extends RecyclerView.ViewHolder {

        TextView text_nom;
        ImageView img_preview;
        public PujaFotoViewHolder(View itemView) {
            super(itemView);
            text_nom = (TextView) itemView.findViewById(R.id.txt_name);
            img_preview = (ImageView) itemView.findViewById(R.id.img_preview);
            //itemView.setOnClickListener(this);
        }
        /*@Override
        public void onClick(View view) {
            Log.d("Miquel", "onClick " + getPosition());
        }*/
    }
}
