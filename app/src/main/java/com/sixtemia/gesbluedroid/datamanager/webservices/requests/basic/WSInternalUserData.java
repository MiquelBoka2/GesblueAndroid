package com.sixtemia.gesbluedroid.datamanager.webservices.requests.basic;

import android.content.Context;

import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

import pt.joaocruz04.lib.annotations.JSoapReqField;

/**
 * Created by rubengonzalez on 19/8/16.
 */

public class WSInternalUserData extends WSInternalConcessioUuid {

	@JSoapReqField(order = 0, fieldName = "usuari")
	private String usuari;
	@JSoapReqField(order = 1, fieldName = "password")
	private String password;

	public WSInternalUserData() {
		super();
	}

	public WSInternalUserData(Context c) {
		super(c);
		usuari = PreferencesGesblue.getUserName(c);
		password = PreferencesGesblue.getPassword(c);
	}

	public WSInternalUserData(Context c, long concessio, String uuid) {
		super(c, concessio, uuid);
		usuari = PreferencesGesblue.getUserName(c);
		password = PreferencesGesblue.getPassword(c);
	}

	public String getUsuari() {
		return usuari;
	}

	public void setUsuari(String usuari) {
		this.usuari = usuari;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}