package com.boka2.gesblue.datamanager.webservices.requests.basic;

import android.content.Context;

import com.boka2.gesblue.global.PreferencesGesblue;

/**
 * Created by Boka2.
 */

public class WSInternalAgentConcessio extends WSRequest {
	private long concessio;
	private String agent;

	public WSInternalAgentConcessio() {
		super();
	}

	public WSInternalAgentConcessio(Context c) {
		super();
		concessio = PreferencesGesblue.getConcessio(c);
		agent = PreferencesGesblue.getAgentId(c);
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
}