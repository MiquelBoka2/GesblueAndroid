package com.boka2.sbaseobjects.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.boka2.sbaseobjects.tools.pickerImatges.ImageBroadcastReceiver;
import com.boka2.sbaseobjects.tools.pickerImatges.SelectImatgesActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.boka2.sbaseobjects.tools.pickerImatges.SelectImatgesActivity.EXTRA_MAX_NUM_FILES;
import static com.boka2.sbaseobjects.tools.pickerImatges.SelectImatgesActivity.EXTRA_MULTIFILE_ENABLE;
import static com.boka2.sbaseobjects.tools.pickerImatges.SelectImatgesActivity.EXTRA_PATH_SELECTED_FILES;

/*
 * Created by Boka2.
 */
public class ImageTools {
    public static final String TAG = "ImageTools";
    public static final String IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER = "imageChosen";
    private static final int PHOTO_MAX_MEASURE = 800;
    private static final int REQUEST_OPEN_CAMERA = 2;

    private static String mCurrentPhotoPath;
    private static final String TEMP_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Boka2" + File.separator + "upload" + File.separator;

    public static abstract class ZipListener {
        public abstract void onZipComplete(String path);
        public abstract void onZipError(Exception ex);
        public abstract void onZipEmpty();
    }

    public static abstract class CameraResultListener {
        public abstract void onPhotoReceived(@NonNull File file, @NonNull String uri, @Nullable File compressedPhoto);
        public abstract void onError(Exception ex);
    }

    /*
     * Redimensiona, converteix a jpg i comprimeix l'array de imatges.
     * @param c Contexte vàlid per trobar la carpeta de la app
     * @param imatgeFiles ArrayList amb els File de les imatges a comprimir
     * @param _listener
     */
    public static void zipFiles(@NonNull final Context c, final ArrayList<File> imatgeFiles, @NonNull final ZipListener _listener) {
        if(imatgeFiles == null || imatgeFiles.size() == 0) {
            _listener.onZipEmpty();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File dir = c.getExternalCacheDir();
                        if (dir != null && !dir.exists()) {
                            Log.i(TAG, "Creant carpeta: " + dir.mkdirs());
                        }
                        final String path = dir.toString() + "/eventupload.zip";
                        File zip = new File(path);
                        if (zip.exists()) {
                            zip.delete();
                        }
                        if (imatgeFiles.size() > 0) {
                            String[] files = new String[imatgeFiles.size()];
                            for (int i = 0; i < files.length; i++) {
                                File tmp = getFileAfterResize(imatgeFiles.get(i));
                                files[i] = tmp.getAbsolutePath();
                            }
                            Compress co = new Compress(files, path);
                            co.zip();
                            _listener.onZipComplete(path);
                        } else {
                            _listener.onZipEmpty();
                        }
                    } catch (Exception ex) {
                        _listener.onZipError(ex);
                    }
                }
            }).start();
        }
    }

    public static File getFileAfterResize(File file) {
        try {
            Log.i(TAG, "Resizing: " + file);
            String _strPath = file.getAbsolutePath();
            int inWidth;
            int inHeight;
            InputStream in = new FileInputStream(_strPath);
            String filename = _strPath.substring(_strPath.lastIndexOf("/") + 1);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(_strPath);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / PHOTO_MAX_MEASURE, inHeight / PHOTO_MAX_MEASURE);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, PHOTO_MAX_MEASURE, PHOTO_MAX_MEASURE);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

            // save image
            try {
                File dir = new File(TEMP_FILE_PATH);
                if(!dir.exists()) {
                    dir.mkdirs();
                }
                String fname = filename.replace(".png", ".jpg");
                FileOutputStream out = new FileOutputStream(TEMP_FILE_PATH + fname);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                Log.i(TAG, "Resized: " + fname);
                return new File(TEMP_FILE_PATH + fname);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
                return file;
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            return file;
        }
    }

    /*
     * Obre una galeria i rep N imatges
     * @param mContext Un contexte vàlid
     * @param imageBroadcastReceiver Un ImageBroadcastReceiver per rebre les imatges
     * @param imatgeUris Les Uris de les imatges ja seleccionades
     * @param multifile Indica si s'ha de permetre escollir més de una
     * @param maxNumImatges Número màxim de imatges que es poden seleccionar. S'ignora si multifile és false
     */
    public static void actionOpenGaleria(@NonNull Context mContext, @NonNull ImageBroadcastReceiver imageBroadcastReceiver, @Nullable ArrayList<String> imatgeUris, boolean multifile, int maxNumImatges) {
        // Rep el retorn de la selecció d'imatges
        IntentFilter imageIntentFilter = new IntentFilter(IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        mContext.registerReceiver(imageBroadcastReceiver, imageIntentFilter);

        Intent intent = new Intent(mContext, SelectImatgesActivity.class);

        if (imatgeUris != null) {
            ArrayList<String> tmp = new ArrayList<>();
            for(String s : imatgeUris) {
                tmp.add(s.replace("file://", ""));
            }
            intent.putStringArrayListExtra(EXTRA_PATH_SELECTED_FILES, tmp);
        }
        intent.putExtra(EXTRA_MULTIFILE_ENABLE, multifile ? 1 : 0);
        intent.putExtra(EXTRA_MAX_NUM_FILES, multifile ? maxNumImatges : 1);

        mContext.startActivity(intent);
    }

    /*
     * Crida a la càmera per demanar una foto.
     * Cal cridar a checkCameraActivityResult al onActivityResult de la Activity que cridi a aquesta funció.
     * @param a
     */
    public static void actionOpenCamera(Activity a) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(a.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                a.startActivityForResult(takePictureIntent, REQUEST_OPEN_CAMERA);
            }
        }
    }

    /*
     * Comprova si s'ha tornat una imatge.
     * @param a La activity actual
     * @param requestCode El requestCode del onActivityResult
     * @param resultCode El resultCode del onActivityResult
     * @param _listener Un listener que rebrà la imatge o la exception
     * @return True si el result era per nosaltres, false si cal continuar buscant per una altra banda
     */
    public static boolean checkCameraActivityResult(Activity a, int requestCode, int resultCode, boolean compressImage, CameraResultListener _listener) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_OPEN_CAMERA) {
            try {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                a.sendBroadcast(mediaScanIntent);

                File photo = new File(mCurrentPhotoPath);
                String mImage = "file://" + photo.toString();
                File compressedPhoto = compressImage ? getFileAfterResize(photo) : null;

                _listener.onPhotoReceived(photo, mImage, compressedPhoto);
            } catch (Exception ex) {
                _listener.onError(ex);
            }
            return true;
        }
        return false;
    }

    private static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}