package com.boka2.gesblue.datamanager.webservices.requests.basic;

import android.content.Context;

import com.boka2.gesblue.global.PreferencesGesblue;

/**
 * Created by Boka2.
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