package com.boka2.spushnotifications.model;

import com.google.gson.annotations.SerializedName;

public class SModPushRegisterResult {

	@SerializedName("result")
	public String strResult;
	
	@SerializedName("strMsg")
	public String strErrorMessage;
	
	@SerializedName("sdid")
	public String strSDID;
		
	public SModPushRegisterResult() {
		strResult = "";
		strErrorMessage = "";
		strSDID = "";
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

	public String getStrSDID() {
		return strSDID;
	}

	public void setStrSDID(String strSDID) {
		this.strSDID = strSDID;
	}

	
}
