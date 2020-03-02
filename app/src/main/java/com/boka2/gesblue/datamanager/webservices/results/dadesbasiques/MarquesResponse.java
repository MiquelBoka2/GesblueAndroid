package com.boka2.gesblue.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.datamanager.webservices.results.basic.WSResult;
import com.boka2.gesblue.global.Constants;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class MarquesResponse extends WSResult {

	public static class Marca {

		@JSoapResField(name = "codi")
		@SerializedName("codi")
		private String codi;

		@JSoapResField(name = "nom")
		@SerializedName("nom")
		private String nom;

		@JSoapResField(name = "logo")
		@SerializedName("logo")
		private String logo;

		@JSoapResField(name = "logoimpresio")
		@SerializedName("logoimpresio")
		private String logoimpresio;

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

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getLogoimpresio() {
			return logoimpresio;
		}

		public void setLogoimpresio(String logoimpresio) {
			this.logoimpresio = logoimpresio;
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

		public Marca() {

		}

		@Override
		public String toString() {
			return "Marca{" +
					"codi=" + codi +
					", nom='" + nom + '\'' +
					", logo='" + logo + '\'' +
					", logoimpresio='" + logoimpresio + '\'' +
					", eliminar=" + eliminar +
					'}';
		}

		public boolean isDefaultValue() {
			return defaultValue == 1;
		}
	}

	@JSoapResField(name = "marques")
	@SerializedName("marques")
	private ArrayList<Marca> marques;

	public MarquesResponse() {
		super();
	}

	public ArrayList<Marca> getMarques() {
		return marques;
	}

	public void setMarques(ArrayList<Marca> marques) {
		this.marques = marques;
	}

	public String getDefaultValue() {
		String id = "";
		for(Marca m : marques) {
			if(m.isDefaultValue()) {
				id = m.getCodi();
			}
		}
		return id;
	}

	@Override
	public String toString() {
		return "MarquesResponse{" +
				"marques=" + marques +
				'}';
	}
}