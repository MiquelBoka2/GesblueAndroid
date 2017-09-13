package com.sixtemia.gesbluedroid.datamanager.webservices.requests.basic;

import android.content.Context;

import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

/**
 * Created by rubengonzalez on 19/8/16.
 */

public class WSInternalFechaConcessio extends WSInternalFecha {
	private long concessio;

	public WSInternalFechaConcessio(Context c, long fecha) {
		super(fecha);
		concessio = PreferencesGesblue.getConcessio(c);
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}
}