package com.sixtemia.gesbluedroid.datamanager.webservices.requests.basic;

import android.content.Context;

import com.sixtemia.gesbluedroid.global.PreferencesGesblue;

import pt.joaocruz04.lib.annotations.JSoapReqField;

/**
 * Created by rubengonzalez on 19/8/16.
 */

public class WSInternalConcessioUuid extends WSRequest {

	@JSoapReqField(order = 2, fieldName = "concessio")
	private long concessio;
	@JSoapReqField(order = 3, fieldName = "uuid")
	private String uuid;

	public WSInternalConcessioUuid() {
		super();
	}

	public WSInternalConcessioUuid(Context c) {
		super();
		concessio = PreferencesGesblue.getConcessio(c);
		uuid = PreferencesGesblue.getUuid(c);
	}

	public WSInternalConcessioUuid(Context c, long concessio, String uuid) {
		PreferencesGesblue.setConcessio(c, concessio);
		PreferencesGesblue.setUuid(c, uuid);
		this.concessio = concessio;
		this.uuid = uuid;
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}