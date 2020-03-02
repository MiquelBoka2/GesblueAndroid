package com.boka2.gesblue.datamanager.webservices.requests.dadesbasiques;

import android.os.Parcel;
import android.os.Parcelable;

import com.boka2.gesblue.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class MarquesRequest implements Parcelable {

	@JSoapReqField(order = 0, fieldName = "fecha")
	private String fecha;

	public MarquesRequest(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.fecha);
	}

	protected MarquesRequest(Parcel in) {
		this.fecha = in.readString();
	}

	public static final Parcelable.Creator<MarquesRequest> CREATOR = new Parcelable.Creator<MarquesRequest>() {
		@Override
		public MarquesRequest createFromParcel(Parcel source) {
			return new MarquesRequest(source);
		}

		@Override
		public MarquesRequest[] newArray(int size) {
			return new MarquesRequest[size];
		}
	};
}