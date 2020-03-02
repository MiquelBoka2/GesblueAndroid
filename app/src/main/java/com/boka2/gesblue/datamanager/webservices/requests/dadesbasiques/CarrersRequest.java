package com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class CarrersRequest {

	@JSoapReqField(order = 0, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 1, fieldName = "fecha")
	private long fecha;

	public CarrersRequest(long concessio, long fecha) {
		this.concessio = concessio;
		this.fecha = fecha;
	}

	public Long getConcessio() {
		return concessio;
	}

	public void setConcessio(Long concessio) {
		this.concessio = concessio;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
}