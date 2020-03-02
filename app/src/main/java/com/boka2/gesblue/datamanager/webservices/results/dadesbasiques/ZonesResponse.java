package com.boka2.gesblue.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.datamanager.webservices.results.basic.WSResult;
import com.boka2.gesblue.global.Constants;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class ZonesResponse extends WSResult {

	public static class Zones {

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
			return "Zones{" +
					"codi=" + codi +
					", nom='" + nom + '\'' +
					", eliminar=" + eliminar +
					'}';
		}

		public Zones() {

		}

		public boolean isDefaultValue() {
			return defaultValue == 1;
		}

	}

	@JSoapResField(name = "zones")
	@SerializedName("zones")
	private ArrayList<Zones> zones;

	public ZonesResponse() {
		super();
	}

	public ArrayList<Zones> getZones() {
		return zones;
	}

	public void setZones(ArrayList<Zones> zones) {
		this.zones = zones;
	}

	public String getDefaultValue() {
		if(!zones.isEmpty()) {
			for(Zones c : zones) {
				if(c.isDefaultValue()) {
					return c.getCodi();
				}
			}
		}
		return "";
	}

	@Override
	public String toString() {
		return "ZonesResponse{" +
				"zones=" + zones +
				'}';
	}
}