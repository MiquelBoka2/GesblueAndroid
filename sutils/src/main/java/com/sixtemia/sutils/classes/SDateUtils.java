package com.sixtemia.sutils.classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Commonly used methods related to Dates.
 * 
 * @author jaumebech
 * @version 1.0
 */
public class SDateUtils {

	/**
	 * Return a string date in specified format.
	 * 
	 * @param _milliSeconds Date in milliseconds
	 * @param _strDateFormat Date format 
	 * @return String representing date in specified format
	 */
	public static String getDate(long _milliSeconds, String _strDateFormat) {
		// Create a DateFormatter object for displaying date in specified format.
		DateFormat formatter = new SimpleDateFormat(_strDateFormat);

		// Create a calendar object that will convert the date and time value in milliseconds to date. 
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(_milliSeconds);
		return formatter.format(calendar.getTime());
	}

	/**
	 * Converts a String into a date
	 * @param _strData String representing a date
	 * @param _dateFormatInput Format of the input string
	 * @return A date object.
	 */
	public static Date getDate(String _strData, String _dateFormatInput) {

		Date dateResultat;

		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat(_dateFormatInput);

			//TimeZone gmtTime = Calendar.getInstance().getTimeZone(); //TimeZone.getTimeZone("GMT");
			//inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			dateResultat = inputFormat.parse(_strData);

			/*DateFormat outputFormat;
			if (SUtils.dateIsToday(date)) {
				outputFormat = new SimpleDateFormat("HH:mm");
			} else {
				outputFormat = new SimpleDateFormat("dd/MM/yy");
			}

			strResultat = outputFormat.format(date);*/

		} catch (Exception e) {
			e.printStackTrace();
			dateResultat = null;
		}

		return dateResultat;
	}

	/**
	 * Converts a String into an equivalent UTC date 
	 * @param _strData String representing a date
	 * @param _dateFormatInput Format of the input string
	 * @return A UTC date object
	 */
	public static Date getDateUTC(String _strData, String _dateFormatInput) {

		Date dateResultat;

		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat(_dateFormatInput);

			//TimeZone gmtTime = Calendar.getInstance().getTimeZone(); //TimeZone.getTimeZone("GMT");
			inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			dateResultat = inputFormat.parse(_strData);

			/*DateFormat outputFormat;
			if (SUtils.dateIsToday(date)) {
				outputFormat = new SimpleDateFormat("HH:mm");
			} else {
				outputFormat = new SimpleDateFormat("dd/MM/yy");
			}

			strResultat = outputFormat.format(date);*/

		} catch (Exception e) {
			e.printStackTrace();
			dateResultat = new Date();
		}

		return dateResultat;
	}

	/**
	 * Return a string date in specified format and UTC
	 * @param _milliSeconds Date in milliseconds
	 * @param _strDateFormat Date format
	 * @return String representing date in specified format and UTC
	 */
	public static String getDateUTC(long _milliSeconds, String _strDateFormat) {
		// Create a DateFormatter object for displaying date in specified format.
		SimpleDateFormat formatter = new SimpleDateFormat(_strDateFormat);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Create a calendar object that will convert the date and time value in milliseconds to date. 
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(_milliSeconds);
		return formatter.format(calendar.getTime());
	}

	/**
	 * Checks if a date is today
	 * 
	 * @param _date the date to check if it's today
	 * @return true if date is today. False if not
	 */
	public static boolean dateIsToday(Date _date) {
		// THE DAY
		Calendar d = Calendar.getInstance();
		d.setTime(_date);
		int dDay = d.get(Calendar.DAY_OF_YEAR);
		int dYear = d.get(Calendar.YEAR);

		// TODAY
		Calendar n = Calendar.getInstance();
		int nDay = n.get(Calendar.DAY_OF_YEAR);
		int nYear = n.get(Calendar.YEAR);

		return dDay == nDay && dYear == nYear;
	}

	/**
	 * Function to convert milliseconds time to Timer Format Hours:Minutes:Seconds
	 * 
	 * @param _milliseconds Number of milliseconds to format
	 * @return Time Format Hours:Minutes:Seconds
	 */

	public static String milliSecondsToHoursMinutesSeconds(long _milliseconds) {
		String finalTimerString = "";
		String minutesString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int) (_milliseconds / (1000 * 60 * 60));
		int minutes = (int) (_milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((_milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		// Add hours if there
		if (hours > 0) {
			finalTimerString = hours + ":";
		}

		// Prepending 0 to minutes if it is one digit
		if (minutes < 10) {
			minutesString = "0" + minutes;
		} else {
			minutesString = "" + minutes;
		}

		// Prepending 0 to seconds if it is one digit
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutesString + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}

	/**
	 * Function to convert milliseconds time to
	 * Timer Format  Hours:Minutes:Seconds
	 * 
	 * @param _milliseconds Number of milliseconds to format
	 * @return Time Format Hours:Minutes:Seconds
	 * */
	public static String milliSecondsToTimer(long _milliseconds) {
		String finalTimerString = "";
		String minutesString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int) (_milliseconds / (1000 * 60 * 60));
		int minutes = (int) (_milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((_milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		// Add hours if there
		if (hours > 0) {
			finalTimerString = hours + ":";
		}

		// Prepending 0 to minutes if it is one digit
		if (minutes < 10) {
			minutesString = "0" + minutes;
		} else {
			minutesString = "" + minutes;
		}

		// Prepending 0 to seconds if it is one digit
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutesString + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}

}
