package com.boka2.spushnotifications.styles;

import com.google.gson.annotations.SerializedName;
import com.boka2.sutils.classes.SUtils;

public class StyleSPushNotificationsNavigation {
	@SerializedName("strFontFamily")
	private String strFontFamily;

	@SerializedName("titleFontSize")
	private float fontSize;

	@SerializedName("buttonColor")
	private String strButtonColor;

	@SerializedName("titleColor")
	private String strTitleColor;

	@SerializedName("backgroundColor")
	private String strBackgroundColor;

	// Getters & setters
	public String getStrFontFamily() {
		if (strFontFamily == null) {
			strFontFamily = "";
		}
		return strFontFamily;
	}

	public void setStrFontFamily(String strFontFamily) {
		this.strFontFamily = strFontFamily;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public String getStrButtonColor() {
		if (strButtonColor == null) {
			strButtonColor = "";
		}
		return strButtonColor;
	}

	public void setStrButtonColor(String strButtonColor) {
		this.strButtonColor = strButtonColor;
	}

	public String getStrTitleColor() {
		if (strTitleColor == null) {
			strTitleColor = "";
		}
		return strTitleColor;
	}

	public void setStrTitleColor(String strTitleColor) {
		this.strTitleColor = strTitleColor;
	}

	public int getTitleColor() {
		return SUtils.getColor(strTitleColor);
	}

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

	public StyleSPushNotificationsNavigation() {
		strFontFamily = "";
		fontSize = 0;
		strButtonColor = "";
		strTitleColor = "";
		strBackgroundColor = "";
	}

}
