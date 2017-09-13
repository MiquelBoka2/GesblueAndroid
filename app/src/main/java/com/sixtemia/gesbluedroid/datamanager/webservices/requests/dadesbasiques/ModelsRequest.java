package com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques;

import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class ModelsRequest {

	@JSoapReqField(order = 0, fieldName = "fecha")
	private String fecha;

	public ModelsRequest(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}