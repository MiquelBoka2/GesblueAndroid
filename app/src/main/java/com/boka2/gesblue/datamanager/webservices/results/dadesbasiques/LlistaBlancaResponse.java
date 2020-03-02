package com.boka2.gesblue.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.datamanager.webservices.results.basic.WSResult;
import com.boka2.gesblue.global.Constants;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class LlistaBlancaResponse extends WSResult {

	public static class LlistaBlanca {

		@JSoapResField(name = "codi")
		@SerializedName("codi")
		private String codi;

		@JSoapResField(name = "matricula")
		@SerializedName("matricula")
		private String matricula;

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
					", eliminar=" + eliminar +
					'}';
		}

		public LlistaBlanca() {

		}


	}

	@JSoapResField(name = "llistablanca")
	@SerializedName("llistablanca")
	private ArrayList<LlistaBlanca> llistablanca;

	public LlistaBlancaResponse() {
		super();
	}

	public ArrayList<LlistaBlanca> getLlistablanca() {
		return llistablanca;
	}

	public void setLlistablanca(ArrayList<LlistaBlanca> llistablanca) {
		this.llistablanca = llistablanca;
	}



	@Override
	public String toString() {
		return "LlistaBlancaResponse{" +
				"llistablanca=" + llistablanca +
				'}';
	}
}