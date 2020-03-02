package com.boka2.spushnotifications.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;


@Table(name = "notificacions")
public class SModPushNotification extends Entity implements Parcelable {
	
	public static String DEFAULT_ORDER = "strDate DESC";
	
	@TableField(name = "nid", datatype = DATATYPE_TEXT)
	@SerializedName("nid")
	public String strId;
	
	@TableField(name = "strDesc", datatype = DATATYPE_TEXT)
	@SerializedName("strDesc")
	public String strDescripcio;
	
	@TableField(name = "strDate", datatype = DATATYPE_TEXT)
	@SerializedName("strDate")
	public String strDate;
	
	@TableField(name = "isNew", datatype = DATATYPE_BOOLEAN)
	public Boolean isNew;
	
	@TableField(name = "strCode", datatype = DATATYPE_TEXT)
	@SerializedName("strCode")
	public String strCode;

	@TableField(name = "strValue", datatype = DATATYPE_TEXT)
	@SerializedName("strValue")
	public String strValue;

	@TableField(name = "isRespost", datatype = DATATYPE_BOOLEAN)
	public boolean isRespost;

	// =================================
	// GETTERS & SETTERS
	// =================================
	public String getIntId() {
		return strId;
	}

	public void setIntId(String intId) {
		this.strId = intId;
	}

	public String getStrDescripcio() {
		return strDescripcio;
	}

	public void setStrDescripcio(String strDescripcio) {
		this.strDescripcio = strDescripcio;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}
	
	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
	
	public String getStrCode() {
		return strCode;
	}

	public void setStrCode(String strCode) {
		this.strCode = strCode;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	// =================================	

	public SModPushNotification() {
		strId = "";
		strDescripcio = "";
		strDate = "";
		isNew = true;
		strCode = "";
		strValue = "";
		isRespost = false;
	}
		
	public SModPushNotification(SModPushNotification _notificacio) {
		this.strId = _notificacio.strId;
		this.strDescripcio = _notificacio.strDescripcio;
		
		this.strDate = _notificacio.strDate;
		this.isNew = _notificacio.isNew;

		this.strCode = _notificacio.strCode;
		this.strValue = _notificacio.strValue;
	}
	
	
	public void modificar(SModPushNotification n) {

		strId = n.strId;
		strDescripcio = n.strDescripcio;
		strDate = n.strDate;
		isNew = n.isNew;
		strCode = n.strCode;
		strValue = n.strValue;
        isRespost = n.isRespost;

		setStatus(Entity.STATUS_UPDATED);
	}
		
	
	
	/*
	 * Parcelable(non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	
	@Override
	public int describeContents() {
		return 0;
	}
	

	public SModPushNotification(Parcel in) {
		this.strId = in.readString();
		this.strDescripcio = in.readString();
		this.strDate = in.readString();
		this.isNew = in.readByte() == 1;
		this.strCode = in.readString();
		this.strValue = in.readString();
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {	
		dest.writeString(strId);
		dest.writeString(strDescripcio);
		dest.writeString(strDate);
		dest.writeByte((byte) (isNew ? 1 : 0));
		dest.writeString(strCode);
		dest.writeString(strValue);
	}
	
	public static final Parcelable.Creator<SModPushNotification> CREATOR = new Parcelable.Creator<SModPushNotification>() {
        public SModPushNotification createFromParcel(Parcel in)
        {
            return new SModPushNotification(in);
        }
 
        public SModPushNotification[] newArray(int size)
        {
            return new SModPushNotification[size];
        }
    };

	@Override
	public String toString() {
		return "SModPushNotification{" +
				"strId='" + strId + '\'' +
				", strDescripcio='" + strDescripcio + '\'' +
				", strDate='" + strDate + '\'' +
				", isNew=" + isNew +
				", strCode='" + strCode + '\'' +
				", strValue='" + strValue + '\'' +
				", isRespost=" + isRespost +
				'}';
	}
}