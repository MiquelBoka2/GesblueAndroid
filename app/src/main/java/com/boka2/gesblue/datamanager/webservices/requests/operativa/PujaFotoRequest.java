package com.boka2.gesblue.datamanager.webservices.requests.operativa;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class PujaFotoRequest {

	@JSoapReqField(order = 0, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 1, fieldName = "file")
	private String file;

	@JSoapReqField(order = 2, fieldName = "location")
	private String location;



	public PujaFotoRequest(long concessio, String file, String location) {
		this.concessio = concessio;
		this.file = file;
		this.location = location;
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}