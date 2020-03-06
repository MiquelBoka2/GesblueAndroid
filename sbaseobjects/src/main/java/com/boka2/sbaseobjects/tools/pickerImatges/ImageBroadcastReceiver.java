package com.boka2.sbaseobjects.tools.pickerImatges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.boka2.sbaseobjects.tools.ImageTools;

import java.io.File;
import java.util.ArrayList;

/*
 * Created by Boka2.
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