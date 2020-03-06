package com.boka2.gesblue.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.boka2.gesblue.datamanager.database.Taules;

/*
 * Created by Boka2.
 */
@DatabaseTable(tableName = Taules.TAULA_MODEL)
public class Model_Model implements Parcelable {

	public static final String ID = "codimodel";

	@DatabaseField(id = true, columnName = ID, columnDefinition = "VARCHAR(100)")
	private String codimodel;

	@DatabaseField(columnName = "marca", columnDefinition = "VARCHAR(100)")
	private String marca;

	@DatabaseField(columnName = "nommodel", columnDefinition = "VARCHAR(100)")
	private String nommodel;

	public Model_Model(String codimodel, String marca, String nommodel) {
		this.codimodel = codimodel;
		this.marca = marca;
		this.nommodel = nommodel;
	}

	public Model_Model() {
		this.codimodel = "";
		this.marca = "";
		this.nommodel = "";
	}

	public String getCodimodel() {
		return codimodel;
	}

	public void setCodimodel(String codimodel) {
		this.codimodel = codimodel;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNommodel() {
		return nommodel;
	}

	public void setNommodel(String nommodel) {
		this.nommodel = nommodel;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.codimodel);
		dest.writeString(this.marca);
		dest.writeString(this.nommodel);
	}

	protected Model_Model(Parcel in) {
		this.codimodel = in.readString();
		this.marca = in.readString();
		this.nommodel = in.readString();
	}

	public static final Parcelable.Creator<Model_Model> CREATOR = new Parcelable.Creator<Model_Model>() {
		@Override
		public Model_Model createFromParcel(Parcel source) {
			return new Model_Model(source);
		}

		@Override
		public Model_Model[] newArray(int size) {
			return new Model_Model[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Model_Model that = (Model_Model) o;

		if (codimodel != null ? !codimodel.equals(that.codimodel) : that.codimodel != null)
			return false;
		if (marca != null ? !marca.equals(that.marca) : that.marca != null) return false;
		return nommodel != null ? nommodel.equals(that.nommodel) : that.nommodel == null;

	}

}