package com.boka2.gesblue.datamanager.webservices.results.operativa;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class RecuperaComptadorDenunciaResponse {

	@JSoapResField(name = "resultat")
	@SerializedName("resultat")
	private long resultat;

	@JSoapResField(name = "comptador")
	@SerializedName("comptador")
	private String comptador;

	public RecuperaComptadorDenunciaResponse(long resultat, String comptador) {
		this.resultat = resultat;
		this.comptador = comptador;
	}

	public long getResultat() {
		return resultat;
	}

	public void setResultat(long resultat) {
		this.resultat = resultat;
	}

	public String getComptador() {
		return comptador;
	}

	public void setComptador(String comptador) {
		this.comptador = comptador;
	}
}