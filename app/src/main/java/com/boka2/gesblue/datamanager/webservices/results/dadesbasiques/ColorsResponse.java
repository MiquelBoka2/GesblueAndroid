package com.boka2.gesblue.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.datamanager.webservices.results.basic.WSResult;
import com.boka2.gesblue.global.Constants;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class ColorsResponse extends WSResult {

	public static class Colors {

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

		@JSoapResField(name = "rgb")
		@SerializedName("rgb")
		private String rgb;

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

		public String getRgb() {
			return rgb;
		}

		public void setRgb(String rgb) {
			this.rgb = rgb;
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

		public Colors() {

		}

		public Colors(String codi, String nomcat, String nomes, String nomeng, String nomfr, String rgb, int eliminar) {
			this.codi = codi;
			this.nomcat = nomcat;
			this.nomes = nomes;
			this.nomeng = nomeng;
			this.nomfr = nomfr;
			this.rgb = rgb;
			this.eliminar = eliminar;
		}

		@Override
		public String toString() {
			return "Colors{" +
					"codi=" + codi +
					", nomcat='" + nomcat + '\'' +
					", nomes='" + nomes + '\'' +
					", nomeng='" + nomeng + '\'' +
					", nomfr='" + nomfr + '\'' +
					", rgb='" + rgb + '\'' +
					", eliminar=" + eliminar +
					'}';
		}

		public boolean isDefaultValue() {
			return defaultValue == 1;
		}
	}

	@JSoapResField(name = "colors")
	@SerializedName("colors")
	private ArrayList<Colors> colors;

	public ColorsResponse() {
		super();
	}

	public ArrayList<Colors> getColors() {
		return colors;
	}

	public void setColors(ArrayList<Colors> colors) {
		this.colors = colors;
	}

	@Override
	public String toString() {
		return "ColorsResponse{" +
				"colors=" + colors +
				'}';
	}

	public String getDefaultValue() {
		if(!colors.isEmpty()) {
			for(Colors c : colors) {
				if(c.isDefaultValue()) {
					return c.getCodi();
				}
			}
		}
		return "";
	}
}