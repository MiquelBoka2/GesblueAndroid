package com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa;

import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class PosicioRequest {

	@JSoapReqField(order = 0, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 1, fieldName = "terminal")
	private long terminal;

	@JSoapReqField(order = 2, fieldName = "agent")
	private long agent;

	@JSoapReqField(order = 3, fieldName = "posicio")
	private String posicio;

	@JSoapReqField(order = 4, fieldName = "datahora")
	private long datahora;

	public PosicioRequest(long concessio, long terminal, long agent, String posicio, long datahora) {
		this.concessio = concessio;
		this.terminal = terminal;
		this.agent = agent;
		this.posicio = posicio;
		this.datahora = datahora;
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}

	public long getTerminal() {
		return terminal;
	}

	public void setTerminal(long terminal) {
		this.terminal = terminal;
	}

	public long getAgent() {
		return agent;
	}

	public void setAgent(long agent) {
		this.agent = agent;
	}

	public String getPosicio() {
		return posicio;
	}

	public void setPosicio(String posicio) {
		this.posicio = posicio;
	}

	public long getDatahora() {
		return datahora;
	}

	public void setDatahora(long datahora) {
		this.datahora = datahora;
	}
}