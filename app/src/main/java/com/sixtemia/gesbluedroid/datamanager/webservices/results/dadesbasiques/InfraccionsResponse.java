package com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.basic.WSResult;
import com.sixtemia.gesbluedroid.global.Constants;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class InfraccionsResponse extends WSResult {

	public static class Infraccions {

		@JSoapResField(name = "codi")
		@SerializedName("codi")
		private String codi;

		@JSoapResField(name = "nom")
		@SerializedName("nom")
		private String nom;

		@JSoapResField(name = "import")
		@SerializedName("import")
		private String importe;

		@JSoapResField(name = "anulacio")
		@SerializedName("anulacio")
		private String anulacio;

		@JSoapResField(name = "tempsanulacio")
		@SerializedName("tempsanulacio")
		private String tempsanulacio;

		@JSoapResField(name = "anulacio2")
		@SerializedName("anulacio2")
		private String anulacio2;

		@JSoapResField(name = "tempsanulacio2")
		@SerializedName("tempsanulacio2")
		private String tempsanulacio2;

		@JSoapResField(name = "precepte")
		@SerializedName("precepte")
		private String precepte;

		@JSoapResField(name = "zona")
		@SerializedName("zona")
		private Long zona;

		@JSoapResField(name = "eliminar")
		@SerializedName("eliminar")
		private int eliminar;

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

		public String getImporte() {
			return importe;
		}

		public void setImporte(String importe) {
			this.importe = importe;
		}

		public String getAnulacio() {
			return anulacio;
		}

		public void setAnulacio(String anulacio) {
			this.anulacio = anulacio;
		}

		public String getTempsanulacio() {
			return tempsanulacio;
		}

		public void setTempsanulacio(String tempsanulacio) {
			this.tempsanulacio = tempsanulacio;
		}

		public String getAnulacio2() {
			return anulacio2;
		}

		public void setAnulacio2(String anulacio2) {
			this.anulacio2 = anulacio2;
		}

		public String getTempsanulacio2() {
			return tempsanulacio2;
		}

		public void setTempsanulacio2(String tempsanulacio2) {
			this.tempsanulacio2 = tempsanulacio2;
		}

		public String getPrecepte() {
			return precepte;
		}

		public void setPrecepte(String precepte) {
			this.precepte = precepte;
		}

		public Long getZona() {
			return zona;
		}
		public void setZona(String zona) {
			this.nom = zona;
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


		public Infraccions() {

		}

		@Override
		public String toString() {
			return "Infraccions{" +
					"codi=" + codi +
					", nom='" + nom + '\'' +
					", importe='" + importe + '\'' +
					", anulacio='" + anulacio + '\'' +
					", tempsanulacio='" + tempsanulacio + '\'' +
					", anulacio2='" + anulacio2 + '\'' +
					", tempsanulacio2='" + tempsanulacio2 + '\'' +
					", precepte='" + precepte + '\'' +
					", zona='" + zona + '\'' +
					", eliminar=" + eliminar +
					'}';
		}
	}

	@JSoapResField(name = "infraccions")
	@SerializedName("infraccions")
	private ArrayList<Infraccions> infraccions;

	@JSoapResField(name = "defaultValue")
	@SerializedName("defaultValue")
	private String defaultValue;

	public InfraccionsResponse() {
		super();
	}

	public ArrayList<Infraccions> getInfraccions() {
		return infraccions;
	}

	public void setInfraccions(ArrayList<Infraccions> infraccions) {
		this.infraccions = infraccions;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return "InfraccionsResponse{" +
				"infraccions=" + infraccions +
				", defaultValue='" + defaultValue + '\'' +
				'}';
	}
}