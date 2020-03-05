package com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class TipusVehiclesRequest {

	@JSoapReqField(order = 0, fieldName = "fecha")
	private String fecha;

	public TipusVehiclesRequest(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}