package com.boka2.spushnotifications.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;

public class SModPushNotificationsListResult {

	@SerializedName("result")
	public String strResult;
	
	@SerializedName("strMsg")
	private String strErrorMessage;
	
	@SerializedName("maxCache")
	private int intMaxCache;
		
	@SerializedName("array")
	public SModPushNotification[] arrayNotificacions;
	
	
	public SModPushNotificationsListResult() {
		strResult = "";
		strErrorMessage = "";
		intMaxCache = 0;
		arrayNotificacions = new SModPushNotification[0];
	}

	public SModPushNotificationsListResult(String _strResult, String _strErrorMessage) {
		strResult = _strResult;
		strErrorMessage = _strErrorMessage;
		intMaxCache = 0;
		arrayNotificacions = new SModPushNotification[0];
	}

	
	public String getStrResult() {
		return strResult;
	}

	public void setStrResult(String strResult) {
		this.strResult = strResult;
	}

	public String getStrErrorMessage() {
		return strErrorMessage;
	}

	public void setStrErrorMessage(String strErrorMessage) {
		this.strErrorMessage = strErrorMessage;
	}

	public int getIntMaxCache() {
		return intMaxCache;
	}

	public void setIntMaxCache(int intMaxCache) {
		this.intMaxCache = intMaxCache;
	}

	public SModPushNotification[] getArrayNotificacions() {
		return arrayNotificacions;
	}

	public ArrayList<SModPushNotification> getArrayListNotificacions() {
		ArrayList<SModPushNotification> ret = new ArrayList<>();
		Collections.addAll(ret, arrayNotificacions);
		return ret;
	}

	public void setArrayNotificacions(SModPushNotification[] arrayNotificacions) {
		this.arrayNotificacions = arrayNotificacions;
	}

	public void setArrayNotificacions(ArrayList<SModPushNotification> arrayNotificacions) {
		this.arrayNotificacions = new SModPushNotification[arrayNotificacions.size()];
		for(int i = 0; i < arrayNotificacions.size(); i++) {
			this.arrayNotificacions[i] = arrayNotificacions.get(i);
		}
	}

}