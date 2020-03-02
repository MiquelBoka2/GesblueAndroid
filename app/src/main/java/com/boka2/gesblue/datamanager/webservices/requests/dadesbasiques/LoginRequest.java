package com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class LoginRequest {

	@JSoapReqField(order = 0, fieldName = "usuari")
	private String usuari;

	@JSoapReqField(order = 1, fieldName = "password")
	private String password;

	@JSoapReqField(order = 2, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 3, fieldName = "uuid")
	private String uuid;

	@JSoapReqField(order = 4, fieldName = "versioos")
	private String versioos;

	@JSoapReqField(order = 5, fieldName = "versioapp")
	private String versioapp;

	@JSoapReqField(order = 6, fieldName = "fecha")
	private long fecha;

	public LoginRequest(String usuari, String password, long concessio, String uuid, String versioos, String versioapp, long fecha) {
		this.usuari = usuari;
		this.password = password;
		this.concessio = concessio;
		this.uuid = uuid;
		this.versioos = versioos;
		this.versioapp = versioapp;
		this.fecha = fecha;
	}

	public LoginRequest() {
	}

	public String getUsuari() {
		return usuari;
	}

	public void setUsuari(String usuari) {
		this.usuari = usuari;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getVersioos() {
		return versioos;
	}

	public void setVersioos(String versioos) {
		this.versioos = versioos;
	}

	public String getVersioapp() {
		return versioapp;
	}

	public void setVersioapp(String versioapp) {
		this.versioapp = versioapp;
	}

	public long getFecha() {
		return fecha;
	}

	public void setFecha(long fecha) {
		this.fecha = fecha;
	}

}