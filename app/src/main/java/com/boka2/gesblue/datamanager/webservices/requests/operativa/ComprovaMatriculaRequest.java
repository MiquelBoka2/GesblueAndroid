package com.boka2.gesblue.datamanager.webservices.requests.operativa;

import com.boka2.gesblue.global.Constants;

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
	@JSoapReqField(order = 4, fieldName = "adrecacarrer")
	private long adrecacarrer;
	@JSoapReqField(order = 5, fieldName = "zona")
	private long zona;


	public ComprovaMatriculaRequest(long concessio, String uuid, String matricula, long datahora, long adrecacarrer, long zona) {
		this.concessio = concessio;
		this.uuid = uuid;
		this.matricula = matricula;
		this.datahora = datahora;
		this.adrecacarrer = adrecacarrer;
		this.zona = zona;
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


	public long getAdrecacarrer() {
		return adrecacarrer;
	}

	public void setAdrecacarrer(long adrecacarrer) {
		this.adrecacarrer = adrecacarrer;
	}

	public long getZona() {
		return zona;
	}

	public void setZona(long zona) {
		this.zona = zona;
	}
}