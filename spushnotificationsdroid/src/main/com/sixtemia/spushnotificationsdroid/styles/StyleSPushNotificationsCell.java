package com.boka2.spushnotificationsdroid.styles;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.boka2.sutils.classes.SUtils;

public class StyleSPushNotificationsCell {
	@SerializedName("strDescFontFamily")
	private String strDescFontFamily;

	@SerializedName("descFontSize")
	private float fontSizeDesc;

	@SerializedName("strDateFontFamily")
	private String strDateFontFamily;

	@SerializedName("dateFontSize")
	private float fontSizeDate;

	@SerializedName("separatorColor")
	private String strSeparatorColor;

	@SerializedName("stateNormal")
	private StyleSPushNotificationsState stateNormal;

	@SerializedName("stateUnread")
	private StyleSPushNotificationsState stateUnread;


	// Getters & setters
	public String getStrDescFontFamily() {
		if (strDescFontFamily == null) {
			strDescFontFamily = "";
		}
		return strDescFontFamily;
	}

	public void setStrDescFontFamily(String strDescFontFamily) {
		this.strDescFontFamily = strDescFontFamily;
	}

	public String getStrDateFontFamily() {
		if (strDateFontFamily == null) {
			strDateFontFamily = "";
		}
		return strDateFontFamily;
	}

	public void setStrDateFontFamily(String strDateFontFamily) {
		this.strDateFontFamily = strDateFontFamily;
	}

	public float getFontSizeDesc() {
		return fontSizeDesc;
	}

	public void setFontSizeDesc(float fontSizeDesc) {
		this.fontSizeDesc = fontSizeDesc;
	}

	public float getFontSizeDate() {
		return fontSizeDate;
	}

	public void setFontSizeDate(float fontSizeDate) {
		this.fontSizeDate = fontSizeDate;
	}

	public String getStrSeparatorColor() {
		if (strSeparatorColor == null) {
			strSeparatorColor = "";
		}
		return strSeparatorColor;
	}

	public void setStrSeparatorColor(String strSeparatorColor) {
		this.strSeparatorColor = strSeparatorColor;
	}

	public int getSeparatorColor(Context _mContext) {
		return SUtils.getColor(strSeparatorColor);
	}

	public StyleSPushNotificationsState getStateNormal() {
		if (stateNormal == null) {
			stateNormal = new StyleSPushNotificationsState();
		}

		return stateNormal;
	}

	public void setStateNormal(StyleSPushNotificationsState stateNormal) {
		this.stateNormal = stateNormal;
	}

	public StyleSPushNotificationsState getStateUnread() {
		if (stateUnread == null) {
			stateUnread = new StyleSPushNotificationsState();
		}

		return stateUnread;
	}

	public void setStateUnread(StyleSPushNotificationsState stateUnread) {
		this.stateUnread = stateUnread;
	}

	public StyleSPushNotificationsCell() {
		strDescFontFamily = "";
		fontSizeDesc = 0;
		strDateFontFamily = "";
		fontSizeDate = 0;
		strSeparatorColor = "";
		stateNormal = new StyleSPushNotificationsState();
		stateUnread = new StyleSPushNotificationsState();
	}

}
