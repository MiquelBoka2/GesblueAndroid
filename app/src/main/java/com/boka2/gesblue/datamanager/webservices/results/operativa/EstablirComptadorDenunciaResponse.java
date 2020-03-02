package com.boka2.gesblue.datamanager.webservices.results.operativa;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class EstablirComptadorDenunciaResponse {

	@JSoapResField(name = "resultat")
	@SerializedName("resultat")
	private long resultat;

	public EstablirComptadorDenunciaResponse(long resultat) {
		this.resultat = resultat;
	}

	public long getResultat() {
		return resultat;
	}

	public void setResultat(long resultat) {
		this.resultat = resultat;
	}
}