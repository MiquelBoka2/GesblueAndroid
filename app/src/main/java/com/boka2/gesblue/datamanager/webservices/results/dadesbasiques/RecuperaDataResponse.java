package com.boka2.gesblue.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class RecuperaDataResponse {

	@JSoapResField(name = "resultat")
	@SerializedName("resultat")
	private long resultat;

	public RecuperaDataResponse() {
		super();
	}

	public RecuperaDataResponse(long resultat) {
		this.resultat = resultat;
	}

	public long getFecha() {
		return resultat;
	}

	public void setFecha(long fecha) {
		this.resultat = fecha;
	}

	@Override
	public String toString() {
		return "RecuperaDataResponse{" +
				"resultat=" + resultat +
				'}';
	}
}