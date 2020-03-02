package com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class ColorsRequest {

	@JSoapReqField(order = 0, fieldName = "fecha")
	private long fecha;

	public ColorsRequest(long fecha) {
		this.fecha = fecha;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
}