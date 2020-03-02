package com.boka2.gesblue.datamanager.webservices.requests.operativa;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class RecuperaComptadorDenunciaRequest {

	@JSoapReqField(order = 0, fieldName = "concessio")
	private String concessio;

	@JSoapReqField(order = 1, fieldName = "terminal")
	private String terminal;

	@JSoapReqField(order = 2, fieldName = "agent")
	private String agent;

	public RecuperaComptadorDenunciaRequest(String concessio, String terminal, String agent) {
		this.concessio = concessio;
		this.terminal = terminal;
		this.agent = agent;
	}

	public String getConcessio() {
		return concessio;
	}

	public void setConcessio(String concessio) {
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
}