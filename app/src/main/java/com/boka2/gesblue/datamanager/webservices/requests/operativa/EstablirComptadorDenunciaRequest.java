package com.boka2.gesblue.datamanager.webservices.requests.operativa;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class EstablirComptadorDenunciaRequest {

	@JSoapReqField(order = 0, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 1, fieldName = "terminal")
	private String terminal;

	@JSoapReqField(order = 2, fieldName = "agent")
	private String agent;

	@JSoapReqField(order = 3, fieldName = "comptador")
	private long comptador;

	public EstablirComptadorDenunciaRequest(long concessio, String terminal, String agent, long comptador) {
		this.concessio = concessio;
		this.terminal = terminal;
		this.agent = agent;
		this.comptador = comptador;
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public long getComptador() {
		return comptador;
	}

	public void setComptador(long comptador) {
		this.comptador = comptador;
	}
}