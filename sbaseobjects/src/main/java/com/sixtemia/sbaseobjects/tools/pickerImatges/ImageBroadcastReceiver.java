package com.sixtemia.sbaseobjects.tools.pickerImatges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sixtemia.sbaseobjects.tools.ImageTools;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rubengonzalez on 13/7/16.
 */

public class ImageBroadcastReceiver extends BroadcastReceiver {
    private ImagesReceivedListener _listener;
    private boolean _compress;

    public ImageBroadcastReceiver(@NonNull ImagesReceivedListener _listener, boolean compressImages) {
        this._listener = _listener;
        this._compress = compressImages;
    }

    public static abstract class ImagesReceivedListener {
        public abstract void onImagesReceived(@NonNull ArrayList<File> files, @NonNull ArrayList<String> uris, @Nullable ArrayList<File> compressedFiles);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> tmp = intent.getStringArrayListExtra("list");

        ArrayList<String> imatgeUris = new ArrayList<>();
        ArrayList<File> imatgeFiles = new ArrayList<>();
        ArrayList<File> imatgeFilesCompressed = new ArrayList<>();

        for(String s : tmp) {
            File photo = new File(s);
            String mImage = "file://" + photo.toString();

            imatgeFiles.add(photo);
            imatgeUris.add(mImage);
            if(_compress) {
                imatgeFilesCompressed.add(ImageTools.getFileAfterResize(photo));
            }
        }

        _listener.onImagesReceived(imatgeFiles, imatgeUris, imatgeFilesCompressed);

        context.unregisterReceiver(this);
    }
}