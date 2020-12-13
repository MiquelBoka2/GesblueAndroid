package com.boka2.gesblue.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.boka2.gesblue.R;
import com.boka2.gesblue.activities.PujarImatges;
import com.boka2.gesblue.datamanager.webservices.DatamanagerAPI;
import com.boka2.gesblue.datamanager.webservices.requests.operativa.PujaFotoRequest;
import com.boka2.gesblue.global.PreferencesGesblue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created by Boka2.
 */
;import pt.joaocruz04.lib.misc.JSoapCallback;

public class PujarFotos_Adapter extends RecyclerView.Adapter<PujarFotos_Adapter.PujaFotoViewHolder>  {

    private Context mCtx;
    private File[] Files;

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
        final File foto = Files[position];


        String[] valors=foto.getName().split("-");
        if(valors.length>2) {
            String nom=valors[2].substring(0,(valors[2].length()-4));

            holder.text_nom.setText(nom.substring(0,nom.length()-1));
            holder.text_numfoto.setText(nom.substring(nom.length()-1));


            SimpleDateFormat dateFormatter =new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat dateFormatterResultant =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date data= dateFormatter.parse(valors[0]);

                holder.text_data.setText(dateFormatterResultant.format(data));
            } catch (ParseException e) {
                holder.text_data.setText(valors[0]);
            }


            /**
            Bitmap bitmap = BitmapFactory.decodeFile(foto.getPath());
            holder.img_preview.setImageBitmap(bitmap);
             **/



            holder.lay_base.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PujarFoto(foto,mCtx);

                }
            });
        }
        else{
            holder.text_numfoto.setText("Te un format no corresponent");
            holder.text_data.setVisibility(View.GONE);
        }





    }


    @Override
    public int getItemCount() {
        return Files.length;
    }


    class PujaFotoViewHolder extends RecyclerView.ViewHolder {

        TextView text_nom,text_data,text_numfoto;
        //ImageView img_preview;
        ConstraintLayout lay_base;
        public PujaFotoViewHolder(View itemView) {
            super(itemView);
            text_nom = (TextView) itemView.findViewById(R.id.txt_numdenucnia);
            text_data=(TextView) itemView.findViewById(R.id.txt_data);
            text_numfoto=(TextView) itemView.findViewById(R.id.txt_numfoto);
            //img_preview = (ImageView) itemView.findViewById(R.id.img_preview);
            lay_base=(ConstraintLayout) itemView.findViewById(R.id.lay_base);

        }
    }



    private void PujarFoto(final File file, Context aContext){
        PujarImatges.StartLoading();
        try {
            byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
            String str_encoded = new String(encoded, StandardCharsets.US_ASCII);


            PujaFotoRequest pjr = new PujaFotoRequest(
                    PreferencesGesblue.getConcessio(aContext),
                    str_encoded,
                    file.getName()
            );
            DatamanagerAPI.crida_PujaFoto(pjr,
                    new JSoapCallback() {
                        @Override
                        public void onSuccess(String result) {
                            File direct = new File("storage/emulated/0/Boka2/upload/done");

                            if (!direct.exists()) {
                                File wallpaperDirectory = new File("storage/emulated/0/Boka2/upload/done");
                                wallpaperDirectory.mkdirs();
                            }
                            File from = new File("storage/emulated/0/Boka2/upload/error/" + file.getName());
                            File to = new File("storage/emulated/0/Boka2/upload/done/" + file.getName());
                            from.renameTo(to);


                            PujarImatges.Refresh();
                            PujarImatges.Message(1);
                        }

                        @Override
                        public void onError(int error) {
                            Log.e("Formulari", "Error PujaFoto: " + error);
                            PujarImatges.StopLoading();
                            PujarImatges.Message(0);

                        }
                    });

        }
        catch (Exception e) {
            PujarImatges.StopLoading();
            PujarImatges.Message(0);
            e.printStackTrace();
        }
    }
}
