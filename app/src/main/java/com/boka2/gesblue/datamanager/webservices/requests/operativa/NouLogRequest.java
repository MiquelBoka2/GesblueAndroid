package com.boka2.gesblue.datamanager.webservices.requests.operativa;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class NouLogRequest {

	@JSoapReqField(order = 0, fieldName = "accio")
	private long accio;

	@JSoapReqField(order = 1, fieldName = "fecha")
	private long fecha;

	@JSoapReqField(order = 2, fieldName = "agent")
	private long agent;

	@JSoapReqField(order = 3, fieldName = "terminal")
	private long terminal;

	@JSoapReqField(order = 4, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 5, fieldName = "posicio")
	private String posicio;

	@JSoapReqField(order = 6, fieldName = "versioapp")
	private String versioapp;

	@JSoapReqField(order = 7, fieldName = "info")
	private String info;


	public NouLogRequest(long accio, long fecha, long agent, long terminal, long concessio, String posicio, String versioapp, String info) {
		this.accio = accio;
		this.fecha = fecha;
		this.agent = agent;
		this.terminal = terminal;
		this.concessio = concessio;
		this.posicio = posicio;
		this.versioapp = versioapp;
		this.info = info;
	}

	public long getAccio() {
		return accio;
	}

	public void setAccio(long accio) {
		this.accio = accio;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}

	public long getAgent() {
		return agent;
	}

	public void setAgent(long agent) {
		this.agent = agent;
	}

	public long getTerminal() {
		return terminal;
	}

	public void setTerminal(long terminal) {
		this.terminal = terminal;
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}

	public String getPosicio() {
		return posicio;
	}

	public void setPosicio(String posicio) {
		this.posicio = posicio;
	}

	public String getVersioapp() {
		return versioapp;
	}

	public void setVersioapp(String versioapp) {
		this.versioapp = versioapp;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}