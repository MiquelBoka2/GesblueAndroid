package com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa;

import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class RecuperaDenunciaRequest {

	@JSoapReqField(order = 0, fieldName = "matricula")
	private String matricula;

	@JSoapReqField(order = 1, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 2, fieldName = "agent")
	private long agent;

	@JSoapReqField(order = 3, fieldName = "datahora")
	private long datahora;

	public RecuperaDenunciaRequest(String matricula, long concessio, long agent, long datahora) {
		this.matricula = matricula;
		this.concessio = concessio;
		this.agent = agent;
		this.datahora = datahora;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}

	public long getAgent() {
		return agent;
	}

	public void setAgent(long agent) {
		this.agent = agent;
	}

	public long getDatahora() {
		return datahora;
	}

	public void setDatahora(long datahora) {
		this.datahora = datahora;
	}
}