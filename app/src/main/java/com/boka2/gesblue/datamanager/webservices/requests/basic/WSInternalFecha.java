package com.boka2.gesblue.datamanager.webservices.requests.basic;

/**
 * Created by Boka2.
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