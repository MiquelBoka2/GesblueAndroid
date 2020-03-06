package com.boka2.gesblue.datamanager.webservices.requests.basic;

import android.content.Context;

import com.boka2.gesblue.global.PreferencesGesblue;

/*
 * Created by Boka2.
 */

public class WSInternalAgentConcessioTerminal extends WSInternalAgentConcessio {
	private String terminal;

	public WSInternalAgentConcessioTerminal(Context c) {
		super(c);
		terminal = PreferencesGesblue.getTerminal(c);
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
}