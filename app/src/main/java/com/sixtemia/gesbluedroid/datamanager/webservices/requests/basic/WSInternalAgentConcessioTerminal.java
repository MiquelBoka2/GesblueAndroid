package com.sixtemia.gesbluedroid.datamanager.webservices.requests.basic;

import android.content.Context;

import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

/**
 * Created by rubengonzalez on 19/8/16.
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