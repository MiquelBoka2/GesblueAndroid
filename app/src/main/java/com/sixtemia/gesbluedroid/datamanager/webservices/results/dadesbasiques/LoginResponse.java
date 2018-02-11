package com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.basic.WSResult;
import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class LoginResponse extends WSResult {

	@JSoapResField(name = "resultat")
	@SerializedName("resultat")
	private long resultat;

	@JSoapResField(name = "agent")
	@SerializedName("agent")
	private long agent;

	@JSoapResField(name = "codiagent")
	@SerializedName("codiagent")
	private String codiagent;

	@JSoapResField(name = "agents")
	@SerializedName("agents")
	private int agents;

	@JSoapResField(name = "marques")
	@SerializedName("marques")
	private int marques;

	@JSoapResField(name = "models")
	@SerializedName("models")
	private int models;

	@JSoapResField(name = "colors")
	@SerializedName("colors")
	private int colors;

	@JSoapResField(name = "tipusvehicles")
	@SerializedName("tipusvehicles")
	private int tipusvehicles;

	@JSoapResField(name = "carrers")
	@SerializedName("carrers")
	private int carrers;

	@JSoapResField(name = "infraccions")
	@SerializedName("infraccions")
	private int infraccions;

	public LoginResponse() {
		resultat = 0;
		agent = 0;
		agents = 0;
		codiagent = "";
		marques = 0;
		models = 0;
		colors = 0;
		tipusvehicles = 0;
		carrers = 0;
		infraccions = 0;
	}

	public LoginResponse(long resultat, long agent, String codiagent, int agents,int marques, int models, int colors, int tipusvehicles, int carrers, int infraccions) {
		this.resultat = resultat;
		this.agent = agent;
		this.agents = agents;
		this.codiagent = codiagent;
		this.marques = marques;
		this.models = models;
		this.colors = colors;
		this.tipusvehicles = tipusvehicles;
		this.carrers = carrers;
		this.infraccions = infraccions;
	}

	public boolean showAgents() {
		return isTrue(getAgents());
	}
	public boolean showMarques() {
		return isTrue(getMarques());
	}

	public boolean showModels() {
		return isTrue(getModels());
	}

	public boolean showColors() {
		return isTrue(getColors());
	}

	public boolean showTipusVehicles() {
		return isTrue(getTipusvehicles());
	}

	public boolean showCarrers() {
		return isTrue(getCarrers());
	}

	public boolean showInfraccions() {
		return isTrue(getInfraccions());
	}

	public long getAgent() {
		return agent;
	}

	public void setAgent(long agent) {
		this.agent = agent;
	}

	public String getCodiagent() {
		return codiagent;
	}

	public void setCodiagent(String codiagent) {
		this.codiagent = codiagent;
	}

	public int getAgents() {
		return agents;
	}

	public void setAgents(int agents) {
		this.agents = agents;
	}

	public int getMarques() {
		return marques;
	}

	public void setMarques(int marques) {
		this.marques = marques;
	}

	public int getModels() {
		return models;
	}

	public void setModels(int models) {
		this.models = models;
	}

	public int getColors() {
		return colors;
	}

	public void setColors(int colors) {
		this.colors = colors;
	}

	public int getTipusvehicles() {
		return tipusvehicles;
	}

	public void setTipusvehicles(int tipusvehicles) {
		this.tipusvehicles = tipusvehicles;
	}

	public int getCarrers() {
		return carrers;
	}

	public void setCarrers(int carrers) {
		this.carrers = carrers;
	}

	public int getInfraccions() {
		return infraccions;
	}

	public void setInfraccions(int infraccions) {
		this.infraccions = infraccions;
	}

	public long getResultat() {
		return resultat;
	}

	public void setResultat(long resultat) {
		this.resultat = resultat;
	}

	@Override
	public String toString() {
		return "LoginResponse{" +
				"resultat='" + resultat + '\'' +
				", agent=" + agent +
				", codiagent='" + codiagent + '\'' +
				", marques=" + marques +
				", models=" + models +
				", colors=" + colors +
				", tipusvehicles=" + tipusvehicles +
				", carrers=" + carrers +
				", infraccions=" + infraccions +
				'}';
	}

	public static boolean isTrue(int v) {
		return v == 1;
	}
}