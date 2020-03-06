package com.boka2.gesblue.datamanager.webservices.results.basic;

import com.google.gson.annotations.SerializedName;

/*
 * Created by Boka2.
 */

public class WSInternalTranslatableObject extends WSInternalObject {

	@SerializedName("nomcat")
	private String nomcat;

	@SerializedName("nomes")
	private String nomes;

	@SerializedName("nomeng")
	private String nomeng;

	@SerializedName("nomfr")
	private String nomfr;

	public WSInternalTranslatableObject() {
		super();
	}

	public String getNomcat() {
		return nomcat;
	}

	public void setNomcat(String nomcat) {
		this.nomcat = nomcat;
	}

	public String getNomes() {
		return nomes;
	}

	public void setNomes(String nomes) {
		this.nomes = nomes;
	}

	public String getNomeng() {
		return nomeng;
	}

	public void setNomeng(String nomeng) {
		this.nomeng = nomeng;
	}

	public String getNomfr() {
		return nomfr;
	}

	public void setNomfr(String nomfr) {
		this.nomfr = nomfr;
	}
}
