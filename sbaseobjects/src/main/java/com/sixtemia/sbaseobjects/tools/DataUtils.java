package com.sixtemia.sbaseobjects.tools;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.Log;

import com.sixtemia.sbaseobjects.objects.SFragmentActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rubengonzalez on 13/9/16.
 */
public abstract class DataUtils {
	public static final String TAG = "DataUtils";

	protected Context mContext;
	private Locale mLocale;
	private String sData;
	private long lData;
	private Date data;
	private Calendar calendar;

	public DataUtils(Context c) {
		this(c, System.currentTimeMillis());
	}

	public DataUtils() {
		mLocale = Locale.getDefault();
		calendar = Calendar.getInstance();
		lData = System.currentTimeMillis();
		fromLong();
	}

	public DataUtils(Context c, String sData) {
		calendar = Calendar.getInstance();
		mLocale = SFragmentActivity.getSelectedLocale(c);
		this.sData = sData;
		fromString();
	}

	public DataUtils(Context c, long lData) {
		calendar = Calendar.getInstance();
		this.lData = lData;
		mLocale = SFragmentActivity.getSelectedLocale(c);
		fromLong();
	}

	public DataUtils(Context c, Date data) {
		calendar = Calendar.getInstance();
		this.data = data;
		mLocale = SFragmentActivity.getSelectedLocale(c);
		fromDate();
	}

	private void fromString() {
		try {
			SimpleDateFormat sdf = getSdf();
			data = sdf.parse(sData);
			lData = data.getTime();
			calendar.setTime(data);
		} catch (Exception ex) {
			Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
		}
	}

	private void fromLong() {
		try {
			SimpleDateFormat sdf = getSdf();
			data = new Date(lData);
			sData = sdf.format(data);
			calendar.setTime(data);
		} catch (Exception ex) {
			Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
		}
	}

	private void fromDate() {
		try {
			SimpleDateFormat sdf = getSdf();
			lData = data.getTime();
			sData = sdf.format(data);
			calendar.setTime(data);
		} catch (Exception ex) {
			Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
		}
	}

	protected SimpleDateFormat getSdf() {
		return new SimpleDateFormat(getDataFormatting(), mLocale);
	}

	public String getStringData() {
		return sData;
	}

	public String getStringData(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, mLocale);
		return sdf.format(data);
	}

	public long getLongData() {
		return lData;
	}

	public Date getData() {
		return data;
	}

	public Date getData(String s) {
		setData(s);
		return getData();
	}

	public void setData(String sData) {
		this.sData = sData;
		fromString();
	}

	public void setData(long lData) {
		this.lData = lData;
		fromLong();
	}

	public void setData(Date data) {
		this.data = data;
		fromDate();
	}

	/**
	 * Assigna una data amb dia, mes i any
	 * @param day      El dia
	 * @param month    El mes, 0-11
	 * @param year     L'any
	 */
	public void setData(int day, int month, int year) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		setData(c.getTime());
	}

	public int getDia() {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * El mes es 0 index! 0-11
	 * @return
	 */
	public int getMes() {
		return calendar.get(Calendar.MONTH);
	}

	public int getAny() {
		return calendar.get(Calendar.YEAR);
	}

	protected abstract String getDataFormatting();

	public boolean between(@Nullable Date from, @Nullable Date to) {
		if(from == null && to == null) {
			return true;
		} else if(from == null) {
			return getData().before(to);
		} else if(to == null) {
			return getData().after(from);
		} else return getData().before(to) && getData().after(from);
	}

	public Context getContext() {
		return mContext;
	}

	public boolean after(Date startDate) {
		return getData().after(startDate);
	}

	public boolean before(Date endDate) {
		return getData().before(endDate);
	}
}