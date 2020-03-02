package com.boka2.spushnotifications.model;

import com.google.gson.annotations.SerializedName;

public class SModPushPendingNotificationsCountResult {

	@SerializedName("result")
	public String strResult;
	
	@SerializedName("strMsg")
	public String strErrorMessage;
	
	@SerializedName("total")
	public int intTotal;
		
	public SModPushPendingNotificationsCountResult() {
		strResult = "";
		strErrorMessage = "";
		intTotal = 0;
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

	public int getIntTotal() {
		return intTotal;
	}

	public void setIntTotal(int intTotal) {
		this.intTotal = intTotal;
	}
	
	
}
