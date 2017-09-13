package com.sixtemia.gesbluedroid.customstuff.ftp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by miquelviladomat on 12/9/17.
 */

public class FTPUpload extends AsyncTask<String,Void,String> {
    private Exception exception;

    protected String doInBackground(String... urls) {
        File path = new File("storage/emulated/0/Sixtemia/upload");
        if(path.exists()) {
            String[] fileNames = path.list();
            if (fileNames.length > 0) {
                String foto = fileNames[0];
                String[] fotoParts = foto.split("-");
                Log.d("EnviaFotos", "Foto " + foto);

                FTPClient con = null;

                try
                {
                    con = new FTPClient();
                    con.connect("gesblue.com");

                    if (con.login("gesblue", "Sjos0o6n"))
                    {
                        con.enterLocalPassiveMode(); // important!
                        con.setFileType(FTP.BINARY_FILE_TYPE);
                        String data = "storage/emulated/0/Sixtemia/upload/"+foto;

                        FileInputStream in = new FileInputStream(new File(data));
                        String fecha = fotoParts[0].substring(0,8);
                        boolean result = con.storeFile("web/admin/fotosdenuncies/"+fotoParts[1]+"/"+fecha+"/"+fotoParts[2], in);
                        //boolean result = con.storeFile("test.jpg", in);

                        in.close();
                        if (result) Log.v("upload result", "succeeded");
                        con.logout();
                        con.disconnect();

                        File direct = new File("storage/emulated/0/Sixtemia/upload/done");

                        if (!direct.exists()) {
                            File wallpaperDirectory = new File("storage/emulated/0/Sixtemia/upload/done/");
                            wallpaperDirectory.mkdirs();
                        }
                        File from = new File("storage/emulated/0/Sixtemia/upload/"+foto);
                        File to = new File("storage/emulated/0/Sixtemia/upload/done/"+fotoParts[2]);
                        from.renameTo(to);

                        return "ok";
                    }
                    else{

                        Log.d("EnviaFotos", "No conectat");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        return null;
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
