package com.sixtemia.gesbluedroid.model;

import com.google.gson.annotations.SerializedName;

import pt.joaocruz04.lib.annotations.JSoapResField;

/**
 * Created by joelabello on 8/9/16.
 */

public class Models {

	@JSoapResField(name = "codi")
	@SerializedName("codi")
	private String codi;

	@JSoapResField(name = "nom")
	@SerializedName("nom")
	private String nom;

	@JSoapResField(name = "marca")
	@SerializedName("marca")
	private String marca;

	@JSoapResField(name = "tipusvehicle")
	@SerializedName("tipusvehicle")
	private String tipusvehicle;

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

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getTipusvehicle() {
		return tipusvehicle;
	}
	public void setTipusvehicle(String tipusvehicle) {
		this.tipusvehicle = tipusvehicle;
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

	public static boolean isTrue(int v) {
		return v == 1;
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Models() {
	}

	public Models(String codi, String nom, String marca, String tipusvehicle, int eliminar) {
		this.codi = codi;
		this.nom = nom;
		this.marca = marca;
		this.tipusvehicle = tipusvehicle;
		this.eliminar = eliminar;
	}

	@Override
	public String toString() {
		return "Models{" +
				"codi=" + codi +
				", nom='" + nom + '\'' +
				", marca='" + marca + '\'' +
				", tipusvehicle='" + tipusvehicle + '\'' +
				", eliminar=" + eliminar +
				'}';
	}

	public boolean isDefaultValue() {
		return defaultValue == 1;
	}
}