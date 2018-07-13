package com.sixtemia.gesbluedroid.datamanager.webservices.results.operativa;

import com.google.gson.annotations.SerializedName;
import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

/**
 * Created by rubengonzalez on 19/8/16.
 */

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class ComprovaMatriculaResponse {

	public static final int MATRICULA_CORRECTA = 0;
	public static final int MATRICULA_NO_CORRECTA = -1;
	public static final int MATRICULA_ERROR_COMPROVACIO = -2;

	@JSoapResField(name = "resultat")
	@SerializedName("resultat")
	private int resultat;

	@JSoapResField(name = "temps")
	@SerializedName("temps")
	private long temps;

	@JSoapResField(name = "tipus")
	@SerializedName("tipus")
	private long tipus;

	@JSoapResField(name = "marca")
	@SerializedName("marca")
	private long marca;

	@JSoapResField(name = "model")
	@SerializedName("model")
	private long model;

	@JSoapResField(name = "color")
	@SerializedName("color")
	private long color;

	public ComprovaMatriculaResponse() {
		this.resultat = 0;
		this.temps = 0;
		this.tipus = 0;
		this.marca = 0;
		this.model = 0;
		this.color = 0;
	}

	public ComprovaMatriculaResponse(int resultat,long temps, long tipus, long marca, long model, long color) {
		this.resultat = resultat;
		this.temps = temps;
		this.tipus = tipus;
		this.marca = marca;
		this.model = model;
		this.color = color;
	}

	public int getResultat() {
		return resultat;
	}

	public void setResultat(int resultat) {
		this.resultat = resultat;
	}

	public long getTemps() {
		return temps;
	}

	public void setTemps(long temps) {
		this.temps = temps;
	}

	public long getTipus() {
		return tipus;
	}

	public void setTipus(long tipus) {
		this.tipus = tipus;
	}

	public long getMarca() {
		return marca;
	}

	public void setMarca(long marca) {
		this.marca = marca;
	}

	public long getModel() {
		return model;
	}

	public void setModel(long model) {
		this.model = model;
	}

	public long getColor() {
		return color;
	}

	public void setColor(long color) {
		this.color = color;
	}
}