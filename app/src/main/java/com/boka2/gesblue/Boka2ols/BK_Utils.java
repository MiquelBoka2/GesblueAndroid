package com.boka2.gesblue.Boka2ols;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.boka2.gesblue.global.PreferencesGesblue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class BK_Utils {

    private static String FileName="UUID.txt";

    public static String GetUUIDFormFile(Context mContext){
        String result=null;


        File root =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "/Boka2");;

        if(root.exists()) {
            String[] fileNames = root.list();
            File[] files = root.listFiles();
            for (File file : files) {
                if (file.isDirectory() == false) {

                    String fileName = file.getName();
                    if(fileName.equals(FileName)){
                        final File arxiu= new File(root, fileName);
                        if(arxiu.canRead()){
                            StringBuilder stringBuilder = new StringBuilder();
                            try (BufferedReader reader = new BufferedReader(new FileReader(arxiu))) {
                                String line = reader.readLine();
                                while (line != null) {
                                    stringBuilder.append(line);
                                    line = reader.readLine();
                                }
                            } catch (IOException e) {
                                // Error occurred when opening raw file for reading.
                            } finally {
                                String contents = stringBuilder.toString();

                                if (!contents.equals("") && !contents.equals("null")){
                                    result=contents;
                                    Log.e("UUID found in file:",contents);
                                }
                            }

                        }
                    }
                    else{
                      Log.e("ERROR","UUID.txt");
                    }

                }
            }
        }



        if(result==null){
            File old_dir = new File("storage/emulated/0/Boka2/");

            if(old_dir.exists()){
                String[] fileNames = old_dir.list();
                File[] files = old_dir.listFiles();
                for (File file : files) {
                    if (file.isDirectory() == false) {

                        String fileName = file.getName();
                        if(fileName.equals(FileName)){
                            final File arxiu= new File(old_dir, fileName);
                            if(arxiu.canRead()){
                                StringBuilder stringBuilder = new StringBuilder();
                                try (BufferedReader reader = new BufferedReader(new FileReader(arxiu))) {
                                    String line = reader.readLine();
                                    while (line != null) {
                                        stringBuilder.append(line);
                                        line = reader.readLine();
                                    }
                                } catch (IOException e) {
                                    // Error occurred when opening raw file for reading.
                                } finally {
                                    String contents = stringBuilder.toString();

                                    if (!contents.equals("") && !contents.equals("null")){
                                        result=contents;
                                        Log.e("UUID found in file:",contents);
                                    }
                                }

                            }
                        }
                        else{
                            Log.e("ERROR","UUID.txt");
                        }

                    }
                }
            }


            if(result!=null){
                SetUUIDToFile(mContext,result);
            }
        }
        return result;
    }

    public static void SetUUIDToFile(Context mContext, String uuid){

        try {

            //TODO: UPDATE NEW SYSTEM
            File dir =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "/Boka2");;
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(dir ,FileName);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            FileOutputStream out = new FileOutputStream(f);
            out.write(uuid.getBytes(StandardCharsets.UTF_8));
            out.close();


            //TODO: OLD SISTEM
            /**
            String rootPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+"/Boka2/";
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            File f_old = new File(rootPath + FileName);
            if (f_old.exists()) {
                f_old.delete();
            }

            f_old.createNewFile();

            FileOutputStream out_old = new FileOutputStream(f_old);
            out_old.write(uuid.getBytes(StandardCharsets.UTF_8));
            out_old.close();
             **/
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class BooVariable {

        public Boolean getBoo() {
            return boo;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public ChangeListener getListener() {
            return listener;
        }

        public void setListener(ChangeListener listener) {
            this.listener = listener;
        }

        Boolean boo = false;
        Context context=null;
        ChangeListener listener = null;

        public void setBoo(Boolean boo) {
            this.boo = boo;
            if (listener != null){listener.onChange(); }
        }

        public interface ChangeListener {
            void onChange();
        }

    }

    public static void setLocale(Context context,String nouIdioma) {

        Locale myLocale = new Locale(nouIdioma);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        PreferencesGesblue.setLocale(context,nouIdioma);
        res.updateConfiguration(conf, dm);
    }

}
