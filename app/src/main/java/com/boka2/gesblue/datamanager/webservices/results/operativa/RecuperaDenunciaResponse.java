package com.boka2.gesblue.datamanager.webservices.results.operativa;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.global.Constants;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class RecuperaDenunciaResponse {

	public static class Denuncies {

		@JSoapResField(name = "codidenuncia")
		@SerializedName("codidenuncia")
		private long codidenuncia;

		@JSoapResField(name = "datahora")
		@SerializedName("datahora")
		private String datahora;

		@JSoapResField(name = "agent")
		@SerializedName("agent")
		private String agent;

		@JSoapResField(name = "adrecacarrer")
		@SerializedName("adrecacarrer")
		private String adrecacarrer;

		@JSoapResField(name = "adrecanum")
		@SerializedName("adrecanum")
		private String adrecanum;

		@JSoapResField(name = "posicio")
		@SerializedName("posicio")
		private String posicio;

		@JSoapResField(name = "matricula")
		@SerializedName("matricula")
		private String matricula;

		@JSoapResField(name = "tipusvehicle")
		@SerializedName("tipusvehicle")
		private String tipusvehicle;

		@JSoapResField(name = "marca")
		@SerializedName("marca")
		private String marca;

		@JSoapResField(name = "model")
		@SerializedName("model")
		private String model;

		@JSoapResField(name = "color")
		@SerializedName("color")
		private String color;

		@JSoapResField(name = "infraccio")
		@SerializedName("infraccio")
		private String infraccio;

		@JSoapResField(name = "datatiquet")
		@SerializedName("datatiquet")
		private String datatiquet;

		@JSoapResField(name = "importiquet")
		@SerializedName("importiquet")
		private String importiquet;

		public long getCodidenuncia() {
			return codidenuncia;
		}

		public void setCodidenuncia(long codidenuncia) {
			this.codidenuncia = codidenuncia;
		}

		public String getDatahora() {
			return datahora;
		}

		public void setDatahora(String datahora) {
			this.datahora = datahora;
		}

		public String getAgent() {
			return agent;
		}

		public void setAgent(String agent) {
			this.agent = agent;
		}

		public String getAdrecacarrer() {
			return adrecacarrer;
		}

		public void setAdrecacarrer(String adrecacarrer) {
			this.adrecacarrer = adrecacarrer;
		}

		public String getAdrecanum() {
			return adrecanum;
		}

		public void setAdrecanum(String adrecanum) {
			this.adrecanum = adrecanum;
		}

		public String getPosicio() {
			return posicio;
		}

		public void setPosicio(String posicio) {
			this.posicio = posicio;
		}

		public String getMatricula() {
			return matricula;
		}

		public void setMatricula(String matricula) {
			this.matricula = matricula;
		}

		public String getTipusvehicle() {
			return tipusvehicle;
		}

		public void setTipusvehicle(String tipusvehicle) {
			this.tipusvehicle = tipusvehicle;
		}

		public String getMarca() {
			return marca;
		}

		public void setMarca(String marca) {
			this.marca = marca;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getInfraccio() {
			return infraccio;
		}

		public void setInfraccio(String infraccio) {
			this.infraccio = infraccio;
		}

		public String getDatatiquet() {
			return datatiquet;
		}

		public void setDatatiquet(String datatiquet) {
			this.datatiquet = datatiquet;
		}

		public String getImportiquet() {
			return importiquet;
		}

		public void setImportiquet(String importiquet) {
			this.importiquet = importiquet;
		}

		public Denuncies() {

		}

		@Override
		public String toString() {
			return "denuncies{" +
					"codidenuncia=" + codidenuncia +
					", datahora='" + datahora + '\'' +
					", agent='" + agent + '\'' +
					", adrecacarrer='" + adrecacarrer + '\'' +
					", adrecanum='" + adrecanum + '\'' +
					", posicio='" + posicio + '\'' +
					", matricula='" + matricula + '\'' +
					", tipusvehicle='" + tipusvehicle + '\'' +
					", marca='" + marca + '\'' +
					", model='" + model + '\'' +
					", color='" + color + '\'' +
					", infraccio='" + infraccio + '\'' +
					", datatiquet='" + datatiquet + '\'' +
					", importiquet='" + importiquet + '\'' +
					'}';
		}
	}

	@JSoapResField(name = "denuncies")
	@SerializedName("denuncies")
	private ArrayList<RecuperaDenunciaResponse.Denuncies> denuncies;

	public RecuperaDenunciaResponse() {
		super();
	}

	public ArrayList<RecuperaDenunciaResponse.Denuncies> getDenuncies() {
		return denuncies;
	}

	public void setDenuncies(ArrayList<RecuperaDenunciaResponse.Denuncies> denuncies) {
		this.denuncies = denuncies;
	}

	@Override
	public String toString() {
		return "RecuperaDenunciaResponse{" +
				"denuncies=" + denuncies +
				'}';
	}
}