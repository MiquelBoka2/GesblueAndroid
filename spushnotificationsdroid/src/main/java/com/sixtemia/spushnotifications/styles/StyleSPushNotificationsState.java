package com.sixtemia.spushnotifications.styles;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.sixtemia.sutils.classes.SUtils;

public class StyleSPushNotificationsState {

	@SerializedName("backgroundColor")
	private String strBackgroundColor;

	@SerializedName("descColor")
	private String strFontColorDesc;

	@SerializedName("dateColor")
	private String strFontColorDate;

	@SerializedName("unreadBarColor")
	private String strUnreadBarColor;

	// Getters & setters

	public String getStrBackgroundColor() {
		if (strBackgroundColor == null) {
			strBackgroundColor = "";
		}
		return strBackgroundColor;
	}

	public void setStrBackgroundColor(String strBackgroundColor) {
		this.strBackgroundColor = strBackgroundColor;
	}

	public int getBackgroundColor() {
		return SUtils.getColor(strBackgroundColor);
	}

	public String getStrFontColorDesc() {
		if (strFontColorDesc == null) {
			strFontColorDesc = "";
		}
		return strFontColorDesc;
	}

	public void setStrFontColorDesc(String strFontColorDesc) {
		this.strFontColorDesc = strFontColorDesc;
	}

	public int getFontColorDesc() {
		return SUtils.getColor(strFontColorDesc);
	}

	public String getStrFontColorDate() {
		if (strFontColorDate == null) {
			strFontColorDate = "";
		}
		return strFontColorDesc;
	}

	public void setStrFontColorDate(String strFontColorDate) {
		this.strFontColorDate = strFontColorDate;
	}

	public int getFontColorDate() {
		return SUtils.getColor(strFontColorDate);
	}

	public String getStrUnreadBarColor() {
		if (strUnreadBarColor == null) {
			strUnreadBarColor = "";
		}
		return strUnreadBarColor;
	}

	public void setStrUnreadBarColor(String strUnreadBarColor) {
		this.strUnreadBarColor = strUnreadBarColor;
	}

	public int getUnreadBarColor(Context _mContext) {
		return SUtils.getColor(strUnreadBarColor);
	}


	public StyleSPushNotificationsState() {
		strFontColorDate = "";
		strFontColorDesc = "";
		strUnreadBarColor = "";
		strBackgroundColor = "";
	}

}
