package com.sixtemia.spushnotificationsdroid.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class SModPushNotificationsListResult {

	@SerializedName("result")
	public String strResult;
	
	@SerializedName("strMsg")
	public String strErrorMessage;
	
	@SerializedName("maxCache")
	public int intMaxCache;
		
	@SerializedName("array")
	public ArrayList<SModPushNotification> arrayNotificacions;
	
	
	public SModPushNotificationsListResult() {
		strResult = "";
		strErrorMessage = "";
		intMaxCache = 0;
		arrayNotificacions = new ArrayList<SModPushNotification>();
	}

	public SModPushNotificationsListResult(String _strResult, String _strErrorMessage) {
		strResult = _strResult;
		strErrorMessage = _strErrorMessage;
		intMaxCache = 0;
		arrayNotificacions = new ArrayList<SModPushNotification>();
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
	
	public ArrayList<SModPushNotification> getArrayNotificacions() {
		return arrayNotificacions;
	}

	public void setArrayNotificacions(ArrayList<SModPushNotification> arrayNotificacions) {
		this.arrayNotificacions = arrayNotificacions;
	}	
	
}
