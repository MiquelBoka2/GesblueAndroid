package com.boka2.gesblue.model;

import com.google.gson.annotations.SerializedName;

import pt.joaocruz04.lib.annotations.JSoapResField;

/*
 * Created by Boka2.
 */

public class Tipus_Vehicle {

	@JSoapResField(name = "codi")
	@SerializedName("codi")
	private String codi;

	@JSoapResField(name = "nomcat")
	@SerializedName("nomcat")
	private String nomcat;

	@JSoapResField(name = "nomes")
	@SerializedName("nomes")
	private String nomes;

	@JSoapResField(name = "nomeng")
	@SerializedName("nomeng")
	private String nomeng;

	@JSoapResField(name = "nomfr")
	@SerializedName("nomfr")
	private String nomfr;

	@JSoapResField(name = "eliminar")
	@SerializedName("eliminar")
	private int eliminar;

	@JSoapResField(name = "defaultValue")
	@SerializedName("defaultValue")
	private int defaultValue;

	public String getCodi() {
		return codi;
	}
	public void setCodi(String codi) {
		this.codi = codi;
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

	public int getEliminar() {
		return eliminar;
	}
	public void setEliminar(int eliminar) {
		this.eliminar = eliminar;
	}
	public boolean isEliminar() {
		return isTrue(getEliminar());
	}

	public int getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
	}

	public static boolean isTrue(int v) {
		return v == 1;
	}

	public Tipus_Vehicle() {
	}

	public Tipus_Vehicle(String codi, String nomcat, String nomes, String nomeng, String nomfr, int eliminar, int defaultValue) {
		this.codi = codi;
		this.nomcat = nomcat;
		this.nomes = nomes;
		this.nomeng = nomeng;
		this.nomfr = nomfr;
		this.eliminar = eliminar;
		this.defaultValue = defaultValue;
	}
}