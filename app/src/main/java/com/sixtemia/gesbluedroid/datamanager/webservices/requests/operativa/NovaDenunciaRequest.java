package com.sixtemia.gesbluedroid.datamanager.webservices.requests.operativa;

import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.OPERATIVA_NAMESPACE)
public class NovaDenunciaRequest {

	@JSoapReqField(order = 0, fieldName = "codidenuncia")
	private String codidenuncia;

	@JSoapReqField(order = 1, fieldName = "datahora")
	private long datahora;

	@JSoapReqField(order = 2, fieldName = "agent")
	private long agent;

	@JSoapReqField(order = 3, fieldName = "adrecacarrer")
	private long adrecacarrer;

	@JSoapReqField(order = 4, fieldName = "adrecanum")
	private String adrecanum;

	@JSoapReqField(order = 5, fieldName = "posicio")
	private String posicio;

	@JSoapReqField(order = 6, fieldName = "matricula")
	private String matricula;

	@JSoapReqField(order = 7, fieldName = "tipusvehicle")
	private long tipusvehicle;

	@JSoapReqField(order = 8, fieldName = "marca")
	private long marca;

	@JSoapReqField(order = 9, fieldName = "model")
	private long model;

	@JSoapReqField(order = 10, fieldName = "color")
	private long color;

	@JSoapReqField(order = 11, fieldName = "infraccio")
	private long infraccio;

	@JSoapReqField(order = 12, fieldName = "datatiquet")
	private long datatiquet;

	@JSoapReqField(order = 13, fieldName = "importtiquet")
	private String importtiquet;

	@JSoapReqField(order = 14, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 15, fieldName = "terminal")
	private long terminal;

	@JSoapReqField(order = 16, fieldName = "versioos")
	private String versioos;

	@JSoapReqField(order = 17, fieldName = "versioapp")
	private String versioapp;

	public NovaDenunciaRequest(String codidenuncia, long datahora, long agent, long adrecacarrer, String adrecanum, String posicio, String matricula, long tipusvehicle, long marca, long model, long color, long infraccio, long datatiquet, String importtiquet, long concessio, long terminal, String versioos, String versioapp) {
		this.codidenuncia = codidenuncia;
		this.datahora = datahora;
		this.agent = agent;
		this.adrecacarrer = adrecacarrer;
		this.adrecanum = adrecanum;
		this.posicio = posicio;
		this.matricula = matricula;
		this.tipusvehicle = tipusvehicle;
		this.marca = marca;
		this.model = model;
		this.color = color;
		this.infraccio = infraccio;
		this.datatiquet = datatiquet;
		this.importtiquet = importtiquet;
		this.concessio = concessio;
		this.terminal = terminal;
		this.versioos = versioos;
		this.versioapp = versioapp;
	}

	public String getCodidenuncia() {
		return codidenuncia;
	}

	public void setCodidenuncia(String codidenuncia) {
		this.codidenuncia = codidenuncia;
	}

	public long getDatahora() {
		return datahora;
	}

	public void setDatahora(long datahora) {
		this.datahora = datahora;
	}

	public long getAgent() {
		return agent;
	}

	public void setAgent(long agent) {
		this.agent = agent;
	}

	public long getAdrecacarrer() {
		return adrecacarrer;
	}

	public void setAdrecacarrer(long adrecacarrer) {
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

	public long getTipusvehicle() {
		return tipusvehicle;
	}

	public void setTipusvehicle(long tipusvehicle) {
		this.tipusvehicle = tipusvehicle;
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

	public long getInfraccio() {
		return infraccio;
	}

	public void setInfraccio(long infraccio) {
		this.infraccio = infraccio;
	}

	public long getDatatiquet() {
		return datatiquet;
	}

	public void setDatatiquet(long datatiquet) {
		this.datatiquet = datatiquet;
	}

	public String getImporttiquet() {
		return importtiquet;
	}

	public void setImporttiquet(String importtiquet) {
		this.importtiquet = importtiquet;
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}

	public long getTerminal() {
		return terminal;
	}

	public void setTerminal(long terminal) {
		this.terminal = terminal;
	}

	public String getVersioos() {
		return versioos;
	}

	public void setVersioos(String versioos) {
		this.versioos = versioos;
	}

	public String getVersioapp() {
		return versioapp;
	}

	public void setVersioapp(String versioapp) {
		this.versioapp = versioapp;
	}
}