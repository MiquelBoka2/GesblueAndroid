package com.sixtemia.gesbluedroid.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sixtemia.gesbluedroid.datamanager.database.Taules;

/**
 * Created by joelabello on 17/8/16.
 */
@DatabaseTable(tableName = Taules.TAULA_CARRER)
public class  Model_Carrer implements Parcelable {

	public static final String ID = "codicarrer";

	@DatabaseField(id = true, columnName = ID, columnDefinition = "NUMBER")
	private long codicarrer;

	@DatabaseField(columnName = "nomcarrer", columnDefinition = "VARCHAR(100)")
	private String nomcarrer;


	public Model_Carrer(long codicarrer, String nomcarrer) {
		this.codicarrer = codicarrer;
		this.nomcarrer = nomcarrer;
	}

	public Model_Carrer() {
		this.codicarrer = -1;
		this.nomcarrer = "";
	}

	public long getCodicarrer() {
		return codicarrer;
	}

	public void setCodicarrer(long codicarrer) {
		this.codicarrer = codicarrer;
	}

	public String getNomcarrer() {
		return nomcarrer;
	}

	public void setNomcarrer(String nomcarrer) {
		this.nomcarrer = nomcarrer;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.codicarrer);
		dest.writeString(this.nomcarrer);
	}

	protected Model_Carrer(Parcel in) {
		this.codicarrer = in.readLong();
		this.nomcarrer = in.readString();
	}

	public static final Parcelable.Creator<Model_Carrer> CREATOR = new Parcelable.Creator<Model_Carrer>() {
		@Override
		public Model_Carrer createFromParcel(Parcel source) {
			return new Model_Carrer(source);
		}

		@Override
		public Model_Carrer[] newArray(int size) {
			return new Model_Carrer[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Model_Carrer that = (Model_Carrer) o;

		if (codicarrer != that.codicarrer) return false;
		return nomcarrer != null ? nomcarrer.equals(that.nomcarrer) : that.nomcarrer == null;

	}
}