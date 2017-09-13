package com.sixtemia.gesbluedroid.customstuff.ftp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creat per rubengonzalez al 10/10/16.
 */
public class GBFileUpload implements Parcelable {
	private String localPath;
	private String concessio;
	private String numDenuncia;

	public static final Parcelable.Creator<GBFileUpload> CREATOR = new Parcelable.Creator<GBFileUpload>() {
		@Override
		public GBFileUpload createFromParcel(Parcel source) {
			return new GBFileUpload(source);
		}

		@Override
		public GBFileUpload[] newArray(int size) {
			return new GBFileUpload[size];
		}
	};

	public GBFileUpload(String localPath, String concessio, String numDenuncia) {
		this.localPath = localPath;
		this.concessio = concessio;
		this.numDenuncia = numDenuncia;
	}

	public GBFileUpload(String localPath, long concessio, String numDenuncia) {
		this.localPath = localPath;
		this.concessio = Long.toString(concessio);
		this.numDenuncia = numDenuncia;
	}

	private GBFileUpload(Parcel in) {
		this.localPath = in.readString();
		this.concessio = in.readString();
		this.numDenuncia = in.readString();
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getConcessio() {
		return concessio;
	}

	public void setConcessio(String concessio) {
		this.concessio = concessio;
	}

	String getNumDenuncia() {
		return numDenuncia;
	}

	public void setNumDenuncia(String numDenuncia) {
		this.numDenuncia = numDenuncia;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.localPath);
		dest.writeString(this.concessio);
		dest.writeString(this.numDenuncia);
	}
}