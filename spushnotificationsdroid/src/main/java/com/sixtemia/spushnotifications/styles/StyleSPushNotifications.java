package com.sixtemia.spushnotifications.styles;

import com.google.gson.annotations.SerializedName;
import com.sixtemia.sutils.classes.SUtils;

public class StyleSPushNotifications {

	@SerializedName("backgroundColor")
	private String strBackgroundColor;

	@SerializedName("strFontFamily")
	private String strFontFamily;

	@SerializedName("fontSize")
	private float fontSize;

	@SerializedName("fontColor")
	private String strFontColor;

	@SerializedName("loadingColor")
	private String strLoadingColor;

	@SerializedName("navigation")
	private StyleSPushNotificationsNavigation navigationStyle;

	@SerializedName("cell")
	private StyleSPushNotificationsCell cellStyle;

	// Getter & setters
	public String getStrBackgroundColor() {
		if (strBackgroundColor == null) {
			strBackgroundColor = "";
		}
		return strBackgroundColor;
	}

	public int getBackgroundColor() {
		return SUtils.getColor(strBackgroundColor);
	}


	public void setStrBackgroundColor(String strBackgroundColor) {
		this.strBackgroundColor = strBackgroundColor;
	}

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

	public String getStrFontColor() {
		if (strFontColor == null) {
			strFontColor = "";
		}
		return strFontColor;
	}

	public void setStrFontColor(String strFontColor) {
		this.strFontColor = strFontColor;
	}

	public int getFontColor() {
		return SUtils.getColor(strFontColor);
	}

	public String getStrLoadingColor() {
		if (strLoadingColor == null) {
			strLoadingColor = "";
		}
		return strLoadingColor;
	}

	public void setStrLoadingColor(String strLoadingColor) {
		this.strLoadingColor = strLoadingColor;
	}

	public int getLoadingColor() {
		return SUtils.getColor(strLoadingColor);
	}

	public StyleSPushNotificationsNavigation getNavigationStyle() {
		if (navigationStyle == null) {
			navigationStyle = new StyleSPushNotificationsNavigation();
		}

		return navigationStyle;
	}

	public void setNavigationStyle(StyleSPushNotificationsNavigation navigationStyle) {
		this.navigationStyle = navigationStyle;
	}

	public StyleSPushNotificationsCell getCellStyle() {
		if (cellStyle == null) {
			cellStyle = new StyleSPushNotificationsCell();
		}

		return cellStyle;
	}

	public void setCellStyle(StyleSPushNotificationsCell cellStyle) {
		this.cellStyle = cellStyle;
	}

	public StyleSPushNotifications() {
		strBackgroundColor = "";
		strFontFamily = "";
		fontSize = 0;
		strFontColor = "";
		strLoadingColor = "";
		navigationStyle = new StyleSPushNotificationsNavigation();
		cellStyle = new StyleSPushNotificationsCell();
	}

}