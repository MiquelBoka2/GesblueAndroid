package com.boka2.gesblue.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.boka2.gesblue.datamanager.database.Taules;

/**
 * Created by Boka2.
 */
@DatabaseTable(tableName = Taules.TAULA_TIPUSVEHICLE)
public class Model_TipusVehicle implements Parcelable {

	public static final String ID = "coditipusvehicle";

	@DatabaseField(id = true, columnName = ID, columnDefinition = "VARCHAR(100)")
	private long coditipusvehicle;

	@DatabaseField(columnName = "nomtipusvehiclecat", columnDefinition = "VARCHAR(100)")
	private String nomtipusvehiclecat;

	@DatabaseField(columnName = "nomtipusvehiclees", columnDefinition = "VARCHAR(100)")
	private String nomtipusvehiclees;

	@DatabaseField(columnName = "nomtipusvehicleeng", columnDefinition = "VARCHAR(100)")
	private String nomtipusvehicleeng;

	@DatabaseField(columnName = "nomtipusvehiclefr", columnDefinition = "VARCHAR(100)")
	private String nomtipusvehiclefr;


	public Model_TipusVehicle(long coditipusvehicle, String nomtipusvehiclecat, String nomtipusvehiclees, String nomtipusvehicleeng, String nomtipusvehiclefr) {
		this.coditipusvehicle = coditipusvehicle;
		this.nomtipusvehiclecat = nomtipusvehiclecat;
		this.nomtipusvehiclees = nomtipusvehiclees;
		this.nomtipusvehicleeng = nomtipusvehicleeng;
		this.nomtipusvehiclefr = nomtipusvehiclefr;
	}

	public Model_TipusVehicle() {
		this.coditipusvehicle = -1;
		this.nomtipusvehiclecat = "";
		this.nomtipusvehiclees = "";
		this.nomtipusvehicleeng = "";
		this.nomtipusvehiclefr = "";
	}

	public String getNomtipusvehiclefr() {
		return nomtipusvehiclefr;
	}

	public void setNomtipusvehiclefr(String nomtipusvehiclefr) {
		this.nomtipusvehiclefr = nomtipusvehiclefr;
	}

	public long getCoditipusvehicle() {
		return coditipusvehicle;
	}

	public void setCoditipusvehicle(long coditipusvehicle) {
		this.coditipusvehicle = coditipusvehicle;
	}

	public String getNomtipusvehiclecat() {
		return nomtipusvehiclecat;
	}

	public void setNomtipusvehiclecat(String nomtipusvehiclecat) {
		this.nomtipusvehiclecat = nomtipusvehiclecat;
	}

	public String getNomtipusvehiclees() {
		return nomtipusvehiclees;
	}

	public void setNomtipusvehiclees(String nomtipusvehiclees) {
		this.nomtipusvehiclees = nomtipusvehiclees;
	}

	public String getNomtipusvehicleeng() {
		return nomtipusvehicleeng;
	}

	public void setNomtipusvehicleeng(String nomtipusvehicleeng) {
		this.nomtipusvehicleeng = nomtipusvehicleeng;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.coditipusvehicle);
		dest.writeString(this.nomtipusvehiclecat);
		dest.writeString(this.nomtipusvehiclees);
		dest.writeString(this.nomtipusvehicleeng);
		dest.writeString(this.nomtipusvehiclefr);
	}

	protected Model_TipusVehicle(Parcel in) {
		this.coditipusvehicle = in.readLong();
		this.nomtipusvehiclecat = in.readString();
		this.nomtipusvehiclees = in.readString();
		this.nomtipusvehicleeng = in.readString();
		this.nomtipusvehiclefr = in.readString();
	}

	public static final Parcelable.Creator<Model_TipusVehicle> CREATOR = new Parcelable.Creator<Model_TipusVehicle>() {
		@Override
		public Model_TipusVehicle createFromParcel(Parcel source) {
			return new Model_TipusVehicle(source);
		}

		@Override
		public Model_TipusVehicle[] newArray(int size) {
			return new Model_TipusVehicle[size];
		}
	};

	@Override
	public boolean equals(Object o) {

		Model_TipusVehicle that = (Model_TipusVehicle) o;
		return (this.coditipusvehicle == that.coditipusvehicle &&
				this.nomtipusvehiclecat.equals(that.nomtipusvehiclecat) &&
				this.nomtipusvehiclees.equals(that.nomtipusvehiclees) &&
				this.nomtipusvehicleeng.equals(that.nomtipusvehicleeng) &&
				this.nomtipusvehiclefr.equals(that.nomtipusvehiclefr));
	}
}