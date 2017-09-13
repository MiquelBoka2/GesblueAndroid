package com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa;

import com.google.gson.annotations.SerializedName;
import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

/**
 * Created by rubengonzalez on 19/8/16.
 */

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class NovaDenunciaResponse {

	@JSoapResField(name = "resultat")
	@SerializedName("resultat")
	private long resultat;

	public NovaDenunciaResponse(long resultat) {
		this.resultat = resultat;
	}

	public long getResultat() {
		return resultat;
	}

	public void setResultat(long resultat) {
		this.resultat = resultat;
	}
}