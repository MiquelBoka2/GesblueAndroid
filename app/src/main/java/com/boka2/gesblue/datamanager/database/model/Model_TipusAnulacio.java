package com.boka2.gesblue.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.boka2.gesblue.datamanager.database.Taules;

import java.util.Date;

/*
 * Created by Boka2.
 */
@DatabaseTable(tableName = Taules.TAULA_TIPUSANULACIO)
public class Model_TipusAnulacio implements Parcelable {

	@DatabaseField(generatedId = true, columnName = "coditipusanulacio")
	private long coditipusanulacio;

	@DatabaseField(columnName = "descripciotipusanulacio", columnDefinition = "VARCHAR(100)")
	private String descripciotipusanulacio;

	@DatabaseField(columnName = "fechaactualitzacio", columnDefinition = "DATETIME")
	private Date fechaactualitzacio;

	public Model_TipusAnulacio(long coditipusanulacio, String descripciotipusanulacio, Date fechaactualitzacio) {
		this.coditipusanulacio = coditipusanulacio;
		this.descripciotipusanulacio = descripciotipusanulacio;
		this.fechaactualitzacio = fechaactualitzacio;
	}

	public Model_TipusAnulacio() {
		this.coditipusanulacio = -1;
		this.descripciotipusanulacio = "";
		this.fechaactualitzacio = new Date();
	}

	public long getCoditipusanulacio() {
		return coditipusanulacio;
	}

	public void setCoditipusanulacio(long coditipusanulacio) {
		this.coditipusanulacio = coditipusanulacio;
	}

	public String getDescripciotipusanulacio() {
		return descripciotipusanulacio;
	}

	public void setDescripciotipusanulacio(String descripciotipusanulacio) {
		this.descripciotipusanulacio = descripciotipusanulacio;
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
		dest.writeLong(this.coditipusanulacio);
		dest.writeString(this.descripciotipusanulacio);
		dest.writeLong(this.fechaactualitzacio != null ? this.fechaactualitzacio.getTime() : -1);
	}

	protected Model_TipusAnulacio(Parcel in) {
		this.coditipusanulacio = in.readLong();
		this.descripciotipusanulacio = in.readString();
		long tmpFechaactualitzacio = in.readLong();
		this.fechaactualitzacio = tmpFechaactualitzacio == -1 ? null : new Date(tmpFechaactualitzacio);
	}

	public static final Parcelable.Creator<Model_TipusAnulacio> CREATOR = new Parcelable.Creator<Model_TipusAnulacio>() {
		@Override
		public Model_TipusAnulacio createFromParcel(Parcel source) {
			return new Model_TipusAnulacio(source);
		}

		@Override
		public Model_TipusAnulacio[] newArray(int size) {
			return new Model_TipusAnulacio[size];
		}
	};
}