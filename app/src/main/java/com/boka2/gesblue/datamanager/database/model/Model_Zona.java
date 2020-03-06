package com.boka2.gesblue.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.boka2.gesblue.datamanager.database.Taules;

/*
 * Created by Boka2.
 */
@DatabaseTable(tableName = Taules.TAULA_ZONA)
public class Model_Zona implements Parcelable {

	public static final String ID = "codizona";

	@DatabaseField(id = true, columnName = ID, columnDefinition = "NUMBER")
	private long codizona;

	@DatabaseField(columnName = "nomzona", columnDefinition = "VARCHAR(100)")
	private String nomzona;


	public Model_Zona(long codizona, String nomzona) {
		this.codizona = codizona;
		this.nomzona = nomzona;
	}

	public Model_Zona() {
		this.codizona = -1;
		this.nomzona = "";
	}

	public long getCodizona() {
		return codizona;
	}

	public void setCodizona(long codizona) {
		this.codizona = codizona;
	}

	public String getNomzona() {
		return nomzona;
	}

	public void setNomzona(String nomzona) {
		this.nomzona = nomzona;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.codizona);
		dest.writeString(this.nomzona);
	}

	protected Model_Zona(Parcel in) {
		this.codizona = in.readLong();
		this.nomzona = in.readString();
	}

	public static final Creator<Model_Zona> CREATOR = new Creator<Model_Zona>() {
		@Override
		public Model_Zona createFromParcel(Parcel source) {
			return new Model_Zona(source);
		}

		@Override
		public Model_Zona[] newArray(int size) {
			return new Model_Zona[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Model_Zona that = (Model_Zona) o;

		if (codizona != that.codizona) return false;
		return nomzona != null ? nomzona.equals(that.nomzona) : that.nomzona == null;

	}
}