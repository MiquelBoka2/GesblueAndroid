package com.boka2.sutils.classes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;

/*
 * Commonly used methods
 * 
 * @author jaumebech
 * @version 1.0
 */

public class SUtils {

	/*
	 * Dials a phone number
	 * @param _context The context the method is being called.
	 * @param _strTelefon Phone number to dial
	 */
	public static void showDial(Context _context, String _strTelefon) {

		if (!_strTelefon.equals("")) {
			try {
				_context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _strTelefon)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	/*
	 * Opens a url on the phone's browser 
	 * @param _context The context the method is being called.
	 * @param _strUrl Url to be opened
	 */
	public static void showWeb(Context _context, String _strUrl) {
		if (!_strUrl.equals("")) {
			if (!_strUrl.startsWith("http://") && !_strUrl.startsWith("https://")) {
				_strUrl = "http://" + _strUrl;
			}
			try {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(_strUrl));
				_context.startActivity(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Opens Google Maps app and shows a direction
	 * @param _context The context the method is being called.
	 * @param _strDireccio Adress to show in Google Maps
	 */
	public static void showMap(Context _context, String _strDireccio) {

		if (!_strDireccio.equals("")) {
			try {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + _strDireccio));
				_context.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * Shows a selector of apps installed on the phone who can send an email, an passes the content of the mail to the selected app 
	 * @param _context The context the method is being called.
	 * @param _strArrayDestinataris Array of recipients of the mail
	 * @param _strArrayDestinatarisCC Array of recipients in copy of the mail
	 * @param _strArrayDestinatarisCCO Array of recipients in hidden copy of the mail
	 * @param _strSubject Subject of the mail
	 * @param _strText Text of the mail
	 */
	public static void showMail(Context _context, ArrayList<String> _strArrayDestinataris, ArrayList<String> _strArrayDestinatarisCC,
			ArrayList<String> _strArrayDestinatarisCCO, String _strSubject, String _strText) {

		if (_strArrayDestinataris != null && _strArrayDestinataris.size() > 0) {
			try {
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				/*String aEmailList[] = { "user@fakehost.com", "user2@fakehost.com" };
				String aEmailCCList[] = { "user3@fakehost.com", "user4@fakehost.com" };
				String aEmailBCCList[] = { "user5@fakehost.com" };
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
				emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
				emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My subject");*/

				String[] aEmailList = new String[_strArrayDestinataris.size()];
				_strArrayDestinataris.toArray(aEmailList);
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);

				if (_strArrayDestinatarisCC != null && _strArrayDestinatarisCC.size() > 0) {
					String[] aEmailListCC = new String[_strArrayDestinataris.size()];
					_strArrayDestinatarisCC.toArray(aEmailListCC);
					emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailListCC);
				}

				if (_strArrayDestinatarisCCO != null && _strArrayDestinatarisCCO.size() > 0) {
					String[] aEmailListCCO = new String[_strArrayDestinataris.size()];
					_strArrayDestinatarisCCO.toArray(aEmailListCCO);
					emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailListCCO);
				}

				if (!_strSubject.equals("")) {
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, _strSubject);
				}

				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, _strText);
				_context.startActivity(emailIntent);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * Opens Media player to show video
	 * @param _context The context the method is being called.
	 * @param _strVideoUrl Url of the video to play
	 */
	public static void showVideoUrl(Context _context, String _strVideoUrl) {
		try {
			_context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(_strVideoUrl)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Converts pixels to dps
	 * @param _context The context the method is being called.
	 * @param _numPixels Number of pixels
	 * @return The number of dps equivalent to the number of pixels passed
	 */
	public static int convertPixelsToDps(Context _context, int _numPixels) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _numPixels, _context.getResources().getDisplayMetrics());
	}

	/*
	 * Converts dps to pixels
	 * @param _context The context the method is being called.
	 * @param _numDps Number of dps
	 * @return The number of pixels equivalent to the number of dps passed
	 */
	public static int convertDpsToPixels(Context _context, int _numDps) {
		return (int) ((_numDps * _context.getResources().getDisplayMetrics().density) + 0.5);
	}

	/*
	 * Shows an alertdialog configured accordingly to the parameters passed. 
	 * @param _context The context the method is being called.
	 * @param _strTitle The title of the dialog. Can be ""
	 * @param _strText The text of the dialog. Can't be ""
	 * @param _strPositiveButtonText The text for the Positive button. If the text is equals to "" the Positive button will be hidden. 
	 * @param _strNegativeButtonText The text for the Negative button. If the text is equals to "" the Negative button will be hidden. 
	 * @param _strNeutralButtonText The text for the Neutral button. If the text is equals to "" the Neutral button will be hidden. 
	 * @param _onClickListener The listener for the buttons of the dialog. Can be null.
	 * @param _icon Icon to show in the dialog. Can be null if no icon is needed.
	 */
	public static void showAlertDialog(Context _context, String _strTitle, String _strText, String _strPositiveButtonText,
			String _strNegativeButtonText, String _strNeutralButtonText, OnClickListener _onClickListener, Drawable _icon) {

		AlertDialog alertDialog = new AlertDialog.Builder(_context).create();

		if (!_strTitle.equals("")) {
			alertDialog.setTitle(_strTitle);
		}

		alertDialog.setMessage(_strText);

		if (!_strPositiveButtonText.equals("")) {
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, _strPositiveButtonText, _onClickListener);
		}

		if (!_strNegativeButtonText.equals("")) {
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, _strNegativeButtonText, _onClickListener);
		}

		if (!_strNeutralButtonText.equals("")) {
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, _strNeutralButtonText, _onClickListener);
		}

		if (_icon != null) {
			alertDialog.setIcon(_icon);
		}

		alertDialog.show();
	}

	/*
	 * Shows an alertdialog configured accordingly to the parameters passed. 
	 * @param _context The context the method is being called.
	 * @param _strTitle The title of the dialog. Can be ""
	 * @param _strText The text of the dialog. Can't be ""
	 * @param _strPositiveButtonText The text for the Positive button. If the text is equals to "" the Positive button will be hidden. 
	 * @param _strNegativeButtonText The text for the Negative button. If the text is equals to "" the Negative button will be hidden. 
	 * @param _strNeutralButtonText The text for the Neutral button. If the text is equals to "" the Neutral button will be hidden. 
	 * @param _onClickListener The listener for the buttons of the dialog. Can be null.
	 * @param _icon Icon to show in the dialog. Can be null if no icon is needed.
	 * @param _cancelable boolean to allow to cancel the dialog touching outside and with back button.
	 */
	public static void showAlertDialog(Context _context, String _strTitle, String _strText, String _strPositiveButtonText,
			String _strNegativeButtonText, String _strNeutralButtonText, OnClickListener _onClickListener, Drawable _icon, boolean _cancelable) {

		AlertDialog alertDialog = new AlertDialog.Builder(_context).create();

		if (!_strTitle.equals("")) {
			alertDialog.setTitle(_strTitle);
		}

		alertDialog.setMessage(_strText);

		if (!_strPositiveButtonText.equals("")) {
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, _strPositiveButtonText, _onClickListener);
		}

		if (!_strNegativeButtonText.equals("")) {
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, _strNegativeButtonText, _onClickListener);
		}

		if (!_strNeutralButtonText.equals("")) {
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, _strNeutralButtonText, _onClickListener);
		}

		if (_icon != null) {
			alertDialog.setIcon(_icon);
		}

		alertDialog.setCancelable(_cancelable);
		alertDialog.setCanceledOnTouchOutside(_cancelable);

		alertDialog.show();
	}

	/*
	 * Returns the original string formatted in lowercase except the first character
	 * @param _strValue String to be formatted
	 * @return String in lower case except the first character
	 */
	public static String getStringLowerCaseWithFirstMajus(String _strValue) {

		String strResultat = _strValue;

		strResultat = strResultat.toLowerCase();

		if (strResultat.length() > 1)
			strResultat = strResultat.substring(0, 1).toUpperCase() + strResultat.substring(1);

		return strResultat;
	}

	/*
	 * Get the MD5 value of a string
	 * @param _strValue 
	 * @return MD5 value
	 */
	public static String getMD5(String _strValue) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(_strValue.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return _strValue;
	}

	/*
	 * Encrypts the string using SHA1
	 * @param _strToHash String to be encrypted 
	 * @return SHA1 value
	 */
	public static String getSHA1Hash(String _strToHash) {
		String hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = _strToHash.getBytes("UTF-8");
			digest.update(bytes, 0, bytes.length);
			bytes = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02X", b));
			}
			hash = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hash;
	}

	/*
	 * Function to get Progress percentage
	 * 
	 * @param _current
	 * @param _total
	 * 
	 * @return Percentage
	 * */
	public int getProgressPercentage(long _current, long _total) {
		Double percentage = (double) 0;

		long currentSeconds = (int) (_current / 1000);
		long totalSeconds = (int) (_total / 1000);

		// calculating percentage
		percentage = (((double) currentSeconds) / totalSeconds) * 100;

		// return percentage
		return percentage.intValue();
	}

	/*
	 * Function to change progress to timer
	 * 
	 * @param progress
	 * @param totalDuration 
	 * @returns current duration in milliseconds
	 * */
	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double) progress) / 100) * totalDuration);

		// return current duration in milliseconds
		return currentDuration * 1000;
	}

	/*
	 * Function to get a color using a colorCode
	 * @param _strColorCode Color code in HEX format
	 * @return
	 */
	public static int getColor(String _strColorCode) {
		String strColor = _strColorCode;

		if (!strColor.contains("#")) {
			strColor = String.format("#%s", strColor);
		}

		int intColorResultat = 0;

		try {
			intColorResultat = Color.parseColor(strColor);
		} catch (Exception e) {
			// Ha fallat la conversió del color. 
		}

		return intColorResultat;
	}

	/*
	 * Function to get a color using a colorCode, and a default color
	 * 
	 * @param _strColorCode Color code in HEX format
	 * @param _intColorNameDefault Default resource color used in case of an error in transformation of _strColorCode. (R.color.colorName) 
	 * @param _mContext
	 * @return
	 */
	public static int getColorWithDefault(String _strColorCode, int _intColorNameDefault, Context _mContext) {
		String strColor = _strColorCode;

		if (!strColor.contains("#")) {
			strColor = String.format("#%s", strColor);
		}

		int intColorResultat = 0;

		try {
			intColorResultat = Color.parseColor(strColor);
		} catch (Exception e) {
			// Ha fallat la conversió del color. Hi posem el color per defecte.
			intColorResultat = _mContext.getResources().getColor(_intColorNameDefault);
		}

		return intColorResultat;
	}

}
