package com.boka2.sbaseobjects.tools;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Boka2.
 */
public class Compress {
    private static final int BUFFER = 2048;

    private String[] _files;
    private String _zipFile;

    public Compress(String[] files, String zipFile) {
        _files = files;
        _zipFile = zipFile;
    }

    public void zip() {
        zip("jpg");
    }

    public void zip(String defaultExtension) {
        try  {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(_zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for(String file : _files) {
                Log.v("Compress", "Adding: " + file);
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                String filename = file.substring(file.lastIndexOf("/") + 1);
                if(!filename.contains(".")) {
                    filename += "." + defaultExtension;
                }
                ZipEntry entry = new ZipEntry(filename);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}