package com.boka2.gesblue.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.datamanager.webservices.results.basic.WSResult;
import com.boka2.gesblue.global.Constants;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class LlistaAbonatsResponse extends WSResult {

	public static class LlistaAbonats {

		@JSoapResField(name = "codi")
		@SerializedName("codi")
		private String codi;

		@JSoapResField(name = "matricula")
		@SerializedName("matricula")
		private String matricula;

		@JSoapResField(name = "datainici")
		@SerializedName("datainici")
		private String datainici;

		@JSoapResField(name = "datafi")
		@SerializedName("datafi")
		private String datafi;

		@JSoapResField(name = "zona1")
		@SerializedName("zona1")
		private String zona1;


		@JSoapResField(name = "zona2")
		@SerializedName("zona2")
		private String zona2;


		@JSoapResField(name = "zona3")
		@SerializedName("zona3")
		private String zona3;


		@JSoapResField(name = "zona4")
		@SerializedName("zona4")
		private String zona4;

		@JSoapResField(name = "zona5")
		@SerializedName("zona5")
		private String zona5;

		@JSoapResField(name = "eliminar")
		@SerializedName("eliminar")
		private int eliminar;


		public String getCodi() {
			return codi;
		}
		public void setCodi(String codi) {
			this.codi = codi;
		}

		public String getMatricula() {
			return matricula;
		}
		public void setMatricula(String matricula) {
			this.matricula = matricula;
		}

		public String getDatainici() {
			return datainici;
		}

		public void setDatainici(String datainici) {
			this.datainici = datainici;
		}

		public String getDatafi() {
			return datafi;
		}

		public void setDatafi(String datafi) {
			this.datafi = datafi;
		}

		public String getZona1() {
			return zona1;
		}

		public void setZona1(String zona1) {
			this.zona1 = zona1;
		}

		public String getZona2() {
			return zona2;
		}

		public void setZona2(String zona2) {
			this.zona2 = zona2;
		}

		public String getZona3() {
			return zona3;
		}

		public void setZona3(String zona3) {
			this.zona3 = zona3;
		}

		public String getZona4() {
			return zona4;
		}

		public void setZona4(String zona4) {
			this.zona4 = zona4;
		}

		public String getZona5() {
			return zona5;
		}

		public void setZona5(String zona5) {
			this.zona5 = zona5;
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


		@Override
		public String toString() {
			return "Zones{" +
					"codi=" + codi +
					", matricula='" + matricula + '\'' +
					", datainici='" + datainici + '\'' +
					", datafi='" + datafi + '\'' +
					", zona1='" + zona1 + '\'' +
					", zona2='" + zona2 + '\'' +
					", zona3='" + zona3 + '\'' +
					", zona4='" + zona4 + '\'' +
					", zona5='" + zona5 + '\'' +
					", eliminar=" + eliminar +
					'}';
		}

		public LlistaAbonats() {

		}


	}

	@JSoapResField(name = "llistaabonats")
	@SerializedName("llistaabonats")
	private ArrayList<LlistaAbonats> llistaabonats;

	public LlistaAbonatsResponse() {
		super();
	}

	public ArrayList<LlistaAbonats> getLlistaabonats() {
		return llistaabonats;
	}

	public void setLlistaabonats(ArrayList<LlistaAbonats> llistaabonats) {
		this.llistaabonats = llistaabonats;
	}



	@Override
	public String toString() {
		return "LlistaAbonatsResponse{" +
				"llistaabonats=" + llistaabonats +
				'}';
	}
}