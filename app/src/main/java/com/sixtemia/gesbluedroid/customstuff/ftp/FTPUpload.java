package com.sixtemia.gesbluedroid.customstuff.ftp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sixtemia.gesbluedroid.GesblueApplication;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by miquelviladomat on 12/9/17.
 */

public class FTPUpload extends AsyncTask<String,Void,String> {
    private Exception exception;

    public Context mContext;
    protected String doInBackground(String... urls) {

        FTPClient con = null;
        con = new FTPClient();

        try {
            con.connect("gesblue.com");

            if (con.login("gesblue", "Sjos0o6n")) {
                File path = new File("storage/emulated/0/Sixtemia/upload");

                if(path.exists()) {
                    String[] fileNames = path.list();
                    File[] files = path.listFiles();
                    for (File file : files) {
                        if  (file.isDirectory() == false)  {
                            // do what you want

                            /*String foto = fileNames[1];
                            if(file.equals("error")){
                                if(fileNames.length>2) {

                                    foto = fileNames[2];
                                }
                                else{
                                    return null;
                                }
                            }*/
                            String foto = file.getName();
                            String[] fotoParts = foto.split("-");
                            Log.d("EnviaFotos", "Foto " + file);
                            if(fotoParts.length>1) {


                                con.enterLocalPassiveMode(); // important!
                                con.setFileType(FTP.BINARY_FILE_TYPE);
                                String data = "storage/emulated/0/Sixtemia/upload/" + foto;

                                FileInputStream in = new FileInputStream(new File(data));
                                String fecha = fotoParts[0].substring(0, 8);
                                boolean directoryExists = con.changeWorkingDirectory("/web/admin/fotosdenuncies/" + fotoParts[1] + "/" + fecha);
                                if (!directoryExists) {
                                    con.makeDirectory("/web/admin/fotosdenuncies/" + fotoParts[1] + "/" + fecha);
                                }
                                boolean result = con.storeFile("/web/admin/fotosdenuncies/" + fotoParts[1] + "/" + fecha + "/" + fotoParts[2], in);
                                //boolean result = con.storeFile("test.jpg", in);

                                in.close();
                                if (result) {
                                    Log.v("upload result", "succeeded");
                                    File direct = new File("storage/emulated/0/Sixtemia/upload/done");

                                    if (!direct.exists()) {
                                        File wallpaperDirectory = new File("storage/emulated/0/Sixtemia/upload/done");
                                        wallpaperDirectory.mkdirs();
                                    }
                                    File from = new File("storage/emulated/0/Sixtemia/upload/" + foto);
                                    File to = new File("storage/emulated/0/Sixtemia/upload/done/" + fotoParts[2]);
                                    from.renameTo(to);
                                }




                            }
                            else {


                                if (con.login("gesblue", "Sjos0o6n")) {
                                    con.enterLocalPassiveMode(); // important!
                                    con.setFileType(FTP.BINARY_FILE_TYPE);
                                    String data = "storage/emulated/0/Sixtemia/upload/" + foto;

                                    FileInputStream in = new FileInputStream(new File(data));
                                    String fecha = fotoParts[0].substring(0, 8);
                                    long conc = PreferencesGesblue.getConcessio(GesblueApplication.getContext());

                                    boolean directoryExists0 = con.changeWorkingDirectory("/web/admin/fotosdenuncies/" + conc + "/" + fecha);
                                    if (!directoryExists0) {
                                        con.makeDirectory("/web/admin/fotosdenuncies/" + conc + "/" + fecha);
                                    }
                                    boolean directoryExists = con.changeWorkingDirectory("/web/admin/fotosdenuncies/" + conc + "/" + fecha + "/errors");
                                    if (!directoryExists) {
                                        con.makeDirectory("/web/admin/fotosdenuncies/" + conc + "/" + fecha + "/errors");
                                    }
                                    boolean result = con.storeFile("/web/admin/fotosdenuncies/" + conc + "/" + fecha + "/errors/" + fotoParts[0].substring(8, 18), in);
                                    //boolean result = con.storeFile("test.jpg", in);

                                    in.close();
                                    if (result) {
                                        Log.v("upload result", "succeeded");
                                        File direct = new File("storage/emulated/0/Sixtemia/upload/done");

                                        if (!direct.exists()) {
                                            File wallpaperDirectory = new File("storage/emulated/0/Sixtemia/upload/done");
                                            wallpaperDirectory.mkdirs();
                                        }
                                        File direct2 = new File("storage/emulated/0/Sixtemia/upload/error");
                                        if (!direct2.exists()) {
                                            File wallpaperDirectory2 = new File("storage/emulated/0/Sixtemia/upload/error");
                                            wallpaperDirectory2.mkdirs();
                                        }
                                        File from = new File("storage/emulated/0/Sixtemia/upload/" + foto);
                                        File to = new File("storage/emulated/0/Sixtemia/upload/error/" + foto);
                                        from.renameTo(to);
                                    }


                                } else {

                                    Log.d("EnviaFotos", "No conectat");
                                }
                            }
                        } else {

                            Log.d("EnviaFotos", "No conectat");
                        }


                        /*File direct = new File("storage/emulated/0/Sixtemia/upload/error");

                        if (!direct.exists()) {
                            File wallpaperDirectory = new File("storage/emulated/0/Sixtemia/upload/error");
                            wallpaperDirectory.mkdirs();
                        }
                        File from = new File("storage/emulated/0/Sixtemia/upload/" + foto);
                        File to = new File("storage/emulated/0/Sixtemia/upload/error/" + foto);
                        from.renameTo(to);*/
                    }


                }

            }
            con.logout();
            con.disconnect();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
