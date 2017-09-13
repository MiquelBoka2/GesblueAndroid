package com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.basic.WSResult;
import com.sixtemia.gesbluedroid.global.Constants;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class CarrersResponse extends WSResult {

	public static class Carrers {

		@JSoapResField(name = "codi")
		@SerializedName("codi")
		private String codi;

		@JSoapResField(name = "nom")
		@SerializedName("nom")
		private String nom;

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

		@Override
		public String toString() {
			return "Carrers{" +
					"codi=" + codi +
					", nom='" + nom + '\'' +
					", eliminar=" + eliminar +
					'}';
		}

		public Carrers() {

		}

		public boolean isDefaultValue() {
			return defaultValue == 1;
		}

	}

	@JSoapResField(name = "carrers")
	@SerializedName("carrers")
	private ArrayList<Carrers> carrers;

	public CarrersResponse() {
		super();
	}

	public ArrayList<Carrers> getCarrers() {
		return carrers;
	}

	public void setCarrers(ArrayList<Carrers> carrers) {
		this.carrers = carrers;
	}

	public String getDefaultValue() {
		if(!carrers.isEmpty()) {
			for(Carrers c : carrers) {
				if(c.isDefaultValue()) {
					return c.getCodi();
				}
			}
		}
		return "";
	}

	@Override
	public String toString() {
		return "CarrersResponse{" +
				"carrers=" + carrers +
				'}';
	}
}