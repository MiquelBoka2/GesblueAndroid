package com.sixtemia.sutils.classes;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Commonly used methods related to Files.
 * 
 * @author jaumebech
 * @version 1.0
 */
public class SFileUtils {

	/**
	 * 
	 * @param _strPathLocalFile
	 * @return
	 */
	public static File getLocalFile(String _strPathLocalFile) {

		File result = new File(_strPathLocalFile);

		return result;
	}

	/**
	 * Gets the path of a file using an uri
	 * @param _context The context the method is being called. 
	 * @param _uri The uri of the file.
	 * @return Real path of the file.
	 */
	public static String getRealPathFromURI(Context _context, Uri _uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(_context, _uri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * Gets the file name of a file specified by a Uri
	 * @param _context The context the method is being called. 
	 * @param _uri The uri of the file.
	 * @return The file name.
	 */
	public static String getFileNameByUri(Context _context, Uri _uri) {
		String fileName = "unknown";// default fileName
		Uri filePathUri = _uri;
		if (_uri.getScheme().toString().compareTo("content") == 0) {
			Cursor cursor = _context.getContentResolver().query(_uri, null, null, null, null);
			if (cursor.moveToFirst()) {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);// Instead of "MediaStore.Images.Media.DATA" can be used
																								// "_data"
				filePathUri = Uri.parse(cursor.getString(column_index));
				fileName = filePathUri.getLastPathSegment().toString();
			}
		} else if (_uri.getScheme().compareTo("file") == 0) {
			fileName = filePathUri.getLastPathSegment().toString();
		} else {
			fileName = fileName + "_" + filePathUri.getLastPathSegment().toString();
		}
		return fileName;
	}

	/**
	 * Translates a number of bytes to a human readable format
	 * @param bytes Number of bytes to be translated
	 * @return Formatted string
	 */
	public static String humanReadableByteCount(long bytes) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		if (bytes > 1024) {
			double kilo = bytes / 1024;
			if (kilo > 1024) {
				double megas = kilo / 1024;
				return df.format(megas) + " MB";
			}
			return df.format(kilo) + " kB";
		}
		return df.format(bytes) + " B";
	}

	/**
	 * Reduce the size of a specified image
	 * @param _context The context the method is being called. 
	 * @param _strFilePath Path of the image to be compressed
	 * @param _intCompressionPercentage % of compression desired.
	 */
	public static void compressImage(Context _context, String _strFilePath, int _intCompressionPercentage) {
		Bitmap bitmapOriginal = BitmapFactory.decodeFile(_strFilePath);

		File file = new File(_strFilePath);

		try {
			FileOutputStream out = new FileOutputStream(_strFilePath);
			bitmapOriginal.compress(Bitmap.CompressFormat.JPEG, _intCompressionPercentage, out);

			out.flush();
			out.close();

			MediaStore.Images.Media.insertImage(_context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		bitmapOriginal.recycle();
	}

}
