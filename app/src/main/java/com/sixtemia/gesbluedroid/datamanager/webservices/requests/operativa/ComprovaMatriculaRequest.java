package com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa;

import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class ComprovaMatriculaRequest {

	@JSoapReqField(order = 0, fieldName = "concessio")
	private long concessio;
	@JSoapReqField(order = 1, fieldName = "uuid")
	private String uuid;
	@JSoapReqField(order = 2, fieldName = "matricula")
	private String matricula;
	@JSoapReqField(order = 3, fieldName = "datahora")
	private long datahora;

	public ComprovaMatriculaRequest(long concessio, String uuid, String matricula, long datahora) {
		this.concessio = concessio;
		this.uuid = uuid;
		this.matricula = matricula;
		this.datahora = datahora;
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public long getDatahora() {
		return datahora;
	}

	public void setDatahora(long datahora) {
		this.datahora = datahora;
	}
}