package com.sixtemia.gesbluedroid.customstuff;

import android.content.Context;

import com.sixtemia.sbaseobjects.tools.DataUtils;

import java.util.Date;

/**
 * Created by rubengonzalez on 13/9/16.
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