package com.sixtemia.gesbluedroid.datamanager.webservices.requests.dadesbasiques;

import android.os.Parcel;
import android.os.Parcelable;

import com.sixtemia.gesbluedroid.global.Constants;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class AgentsRequest implements Parcelable {
	@JSoapReqField(order = 0, fieldName = "concessio")
	private long concessio;

	@JSoapReqField(order = 1, fieldName = "fecha")
	private String fecha;


	public AgentsRequest(long concessio, String fecha) {
		this.concessio = concessio;
		this.fecha = fecha;
	}

	public long getConcessio() {
		return concessio;
	}

	public void setConcessio(long concessio) {
		this.concessio = concessio;
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

	protected AgentsRequest(Parcel in) {
		this.fecha = in.readString();
	}

	public static final Creator<AgentsRequest> CREATOR = new Creator<AgentsRequest>() {
		@Override
		public AgentsRequest createFromParcel(Parcel source) {
			return new AgentsRequest(source);
		}

		@Override
		public AgentsRequest[] newArray(int size) {
			return new AgentsRequest[size];
		}
	};
}