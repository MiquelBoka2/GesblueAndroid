package com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class NouTerminalRequest {

	@JSoapReqField(order = 0, fieldName = "usuari")
	private String usuari;
	@JSoapReqField(order = 1, fieldName = "password")
	private String password;
	@JSoapReqField(order = 2, fieldName = "concessio")
	private long concessio;
	@JSoapReqField(order = 3, fieldName = "uuid")
	private String uuid;


	public NouTerminalRequest(String usuari, String password, long concessio, String uuid) {
		this.usuari = usuari;
		this.password = password;
		this.concessio = concessio;
		this.uuid = uuid;
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
}