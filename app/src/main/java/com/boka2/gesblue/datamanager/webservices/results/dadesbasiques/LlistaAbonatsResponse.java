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