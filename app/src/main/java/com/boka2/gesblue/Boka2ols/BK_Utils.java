package com.boka2.gesblue.Boka2ols;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.boka2.gesblue.global.PreferencesGesblue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BK_Utils {

    private static String FileName="UUID.txt";

    public static String GetUUIDFormFile(Context mContext){
        File root = new File("storage/emulated/0/Boka2/");
        String result=null;
        if(root.exists()) {
            String[] fileNames = root.list();
            File[] files = root.listFiles();
            for (File file : files) {
                if (file.isDirectory() == false) {

                    String fileName = file.getName();
                    if(fileName==FileName){
                        final File arxiu= new File(root, fileName);
                        if(arxiu.canRead()){
                            FileInputStream fis = null;
                            try {
                                fis = mContext.openFileInput(fileName);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            InputStreamReader inputStreamReader =
                                    new InputStreamReader(fis, StandardCharsets.UTF_8);
                            StringBuilder stringBuilder = new StringBuilder();
                            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
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

                }
            }
        }
        return result;
    }

    public static void SetUUIDToFile(Context mContext, String uuid){

        try {
            String rootPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+"/Boka2/";
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            File f = new File(rootPath + FileName);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            FileOutputStream out = new FileOutputStream(f);
            out.write(uuid.getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
