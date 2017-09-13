package com.sixtemia.gesbluedroid.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sixtemia.gesbluedroid.datamanager.database.Taules;

import java.util.Date;

/**
 * Created by joelabello on 17/8/16.
 */
@DatabaseTable(tableName = Taules.TAULA_MARCA)
public class Model_Marca implements Parcelable {

	public static final String ID = "codimarca";

	@DatabaseField(id = true, columnName = ID, columnDefinition = "VARCHAR(100)")
	private String codimarca;

	@DatabaseField(columnName = "nommarca", columnDefinition = "VARCHAR(100)")
	private String nommarca;

	@DatabaseField(columnName = "imatgemarca", columnDefinition = "VARCHAR(100)")
	private String imatgemarca;

	@DatabaseField(columnName = "fechaactualitzacio", columnDefinition = "DATETIME")
	private Date fechaactualitzacio;

	public Model_Marca(String codimarca, Date fechaactualitzacio, String nommarca, String imatgemarca) {
		this.codimarca = codimarca;
		this.nommarca = nommarca;
		this.imatgemarca = imatgemarca;
		this.fechaactualitzacio = fechaactualitzacio;
	}

	public Model_Marca() {
		this.codimarca = "";
		this.nommarca = "";
		this.imatgemarca = "";
		this.fechaactualitzacio = new Date();
	}

	public String getCodimarca() {
		return codimarca;
	}

	public void setCodimarca(String codimarca) {
		this.codimarca = codimarca;
	}

	public String getNommarca() {
		return nommarca;
	}

	public void setNommarca(String nommarca) {
		this.nommarca = nommarca;
	}

	public String getImatgemarca() {
		return imatgemarca;
	}

	public void setImatgemarca(String imatgemarca) {
		this.imatgemarca = imatgemarca;
	}

	public Date getFechaactualitzacio() {
		return fechaactualitzacio;
	}

	public void setFechaactualitzacio(Date fechaactualitzacio) {
		this.fechaactualitzacio = fechaactualitzacio;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.codimarca);
		dest.writeString(this.nommarca);
		dest.writeString(this.imatgemarca);
		dest.writeLong(this.fechaactualitzacio != null ? this.fechaactualitzacio.getTime() : -1);
	}

	protected Model_Marca(Parcel in) {
		this.codimarca = in.readString();
		this.nommarca = in.readString();
		this.imatgemarca = in.readString();
		long tmpFechaactualitzacio = in.readLong();
		this.fechaactualitzacio = tmpFechaactualitzacio == -1 ? null : new Date(tmpFechaactualitzacio);
	}

	public static final Parcelable.Creator<Model_Marca> CREATOR = new Parcelable.Creator<Model_Marca>() {
		@Override
		public Model_Marca createFromParcel(Parcel source) {
			return new Model_Marca(source);
		}

		@Override
		public Model_Marca[] newArray(int size) {
			return new Model_Marca[size];
		}
	};

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Model_Marca that = (Model_Marca) o;

		if (codimarca != null ? !codimarca.equals(that.codimarca) : that.codimarca != null)
			return false;
		if (nommarca != null ? !nommarca.equals(that.nommarca) : that.nommarca != null)
			return false;
		if (imatgemarca != null ? !imatgemarca.equals(that.imatgemarca) : that.imatgemarca != null)
			return false;
		return fechaactualitzacio != null ? fechaactualitzacio.equals(that.fechaactualitzacio) : that.fechaactualitzacio == null;

	}
}