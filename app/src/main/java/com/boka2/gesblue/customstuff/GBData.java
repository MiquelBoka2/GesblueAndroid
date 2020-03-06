package com.boka2.gesblue.customstuff;

import android.content.Context;

import com.boka2.sbaseobjects.tools.DataUtils;

import java.util.Date;

/*
 * Created by Boka2.
 */
public class GBData extends DataUtils {

	public GBData() {
		super();
	}

	public GBData(String data) {
		super();
		setData(data);
	}

	public GBData(Context c) {
		super(c);
	}

	public GBData(Context c, String sData) {
		super(c, sData);
	}

	public GBData(Context c, long lData) {
		super(c, lData);
	}

	public GBData(Context c, Date data) {
		super(c, data);
	}

	@Override
	protected String getDataFormatting() {
		return "dd/MM/yyyy";
	}

}