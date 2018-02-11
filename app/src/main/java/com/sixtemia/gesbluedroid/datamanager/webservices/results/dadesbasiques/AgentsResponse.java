package com.sixtemia.gesbluedroid.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.basic.WSResult;
import com.sixtemia.gesbluedroid.global.Constants;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class AgentsResponse extends WSResult {

	public static class Agent {

		@JSoapResField(name = "codi")
		@SerializedName("codi")
		private String codi;

		@JSoapResField(name = "login")
		@SerializedName("login")
		private String login;


		@JSoapResField(name = "password")
		@SerializedName("password")
		private String password;

		@JSoapResField(name = "codiagent")
		@SerializedName("codiagent")
		private String codiagent;

		public String getCodi() {
			return codi;
		}

		public void setCodi(String codi) {
			this.codi = codi;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getCodiagent() {
			return codiagent;
		}

		public void setCodiagent(String codiagent) {
			this.codiagent = codiagent;
		}

		public Agent() {

		}

		@Override
		public String toString() {
			return "Agent{" +
					"codi=" + codi +
					", login='" + login + '\'' +
					", password='" + password + '\'' +
					", codiagent='" + codiagent +
					'}';
		}


	}

	@JSoapResField(name = "agents")
	@SerializedName("agents")
	private ArrayList<Agent> agents;

	public AgentsResponse() {
		super();
	}

	public ArrayList<Agent> getAgents() {
		return agents;
	}

	public void setMarques(ArrayList<Agent> marques) {
		this.agents = agents;
	}



	@Override
	public String toString() {
		return "AgentsResponse{" +
				"agents=" + agents +
				'}';
	}
}