package com.boka2.gesblue.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.boka2.gesblue.datamanager.database.Taules;

/*
 * Created by Boka2.
 */
@DatabaseTable(tableName = Taules.TAULA_ACCIOPOSICIO)
public class Model_AccioPosicio implements Parcelable {

	@DatabaseField(generatedId = true, columnName = "codiaccioposicio")
	private long codiaccioposicio;

	@DatabaseField(columnName = "descripcioaccioposicio", columnDefinition = "VARCHAR(100)")
	private String descripcioaccioposicio;

	public Model_AccioPosicio(long codiaccioposicio, String descripcioaccioposicio) {
		this.codiaccioposicio = codiaccioposicio;
		this.descripcioaccioposicio = descripcioaccioposicio;
	}

	public Model_AccioPosicio() {
		this.codiaccioposicio = -1;
		this.descripcioaccioposicio = "";
	}

	public long getCodiaccioposicio() {
		return codiaccioposicio;
	}

	public void setCodiaccioposicio(long codiaccioposicio) {
		this.codiaccioposicio = codiaccioposicio;
	}

	public String getDescripcioaccioposicio() {
		return descripcioaccioposicio;
	}

	public void setDescripcioaccioposicio(String descripcioaccioposicio) {
		this.descripcioaccioposicio = descripcioaccioposicio;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.codiaccioposicio);
		dest.writeString(this.descripcioaccioposicio);
	}

	protected Model_AccioPosicio(Parcel in) {
		this.codiaccioposicio = in.readLong();
		this.descripcioaccioposicio = in.readString();
	}

	public static final Parcelable.Creator<Model_AccioPosicio> CREATOR = new Parcelable.Creator<Model_AccioPosicio>() {
		@Override
		public Model_AccioPosicio createFromParcel(Parcel source) {
			return new Model_AccioPosicio(source);
		}

		@Override
		public Model_AccioPosicio[] newArray(int size) {
			return new Model_AccioPosicio[size];
		}
	};
}