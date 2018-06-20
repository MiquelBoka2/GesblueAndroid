package com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques;

import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class LlistaBlancaRequest {

	@JSoapReqField(order = 0, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 1, fieldName = "fecha")
	private String fecha;

	public LlistaBlancaRequest(long concessio, String fecha) {
		this.concessio = concessio;
		this.fecha = fecha;
	}

	public Long getConcessio() {
		return concessio;
	}

	public void setConcessio(Long concessio) {
		this.concessio = concessio;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}