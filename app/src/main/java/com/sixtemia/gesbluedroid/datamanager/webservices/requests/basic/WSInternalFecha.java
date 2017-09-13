package com.sixtemia.gesbluedroid.datamanager.webservices.requests.basic;

/**
 * Created by rubengonzalez on 19/8/16.
 */

public class WSInternalFecha extends WSRequest {
	private long fecha;

	public WSInternalFecha(long fecha) {
		this.fecha = fecha;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
}