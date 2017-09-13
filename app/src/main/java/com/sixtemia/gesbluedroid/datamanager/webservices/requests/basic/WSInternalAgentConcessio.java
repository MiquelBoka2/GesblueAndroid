package com.sixtemia.gesbluedroid.datamanager.webservices.requests.basic;

import android.content.Context;

import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

/**
 * Created by rubengonzalez on 19/8/16.
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