package com.sixtemia.gesbluedroid.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sixtemia.gesbluedroid.datamanager.database.Taules;

/**
 * Created by joelabello on 17/8/16.
 */
@DatabaseTable(tableName = Taules.TAULA_LLISTAABONATS)
public class Model_LlistaAbonats implements Parcelable {

	public static final String ID = "codillistaabonats";

	@DatabaseField(id = true, columnName = ID, columnDefinition = "NUMBER")
	private long codillistaabonats;

	@DatabaseField(columnName = "matricula", columnDefinition = "VARCHAR(100)")
	private String matricula;

	@DatabaseField(columnName = "datainici", columnDefinition = "VARCHAR(100)")
	private String datainici;

	@DatabaseField(columnName = "datafi", columnDefinition = "VARCHAR(100)")
	private String datafi;


	public Model_LlistaAbonats(long codillistaabonats, String matricula, String datainici, String datafi) {
		this.codillistaabonats = codillistaabonats;
		this.matricula = matricula;
		this.datainici = datainici;
		this.datafi = datafi;
	}

	public Model_LlistaAbonats() {
		this.codillistaabonats = -1;
		this.matricula = "";
		this.datainici = "";
		this.datafi = "";
	}

	public long getCodillistablanca() {
		return codillistaabonats;
	}

	public void setCodillistablanca(long codillistablanca) {
		this.codillistaabonats = codillistablanca;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getDatainici() {
		return datainici;
	}

	public void setDatainici(String datainici) {
		this.datainici = datainici;
	}

	public String getDatafi() {
		return datafi;
	}

	public void setDatafi(String datafi) {
		this.datafi = datafi;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.codillistaabonats);
		dest.writeString(this.matricula);
		dest.writeString(this.datainici);
		dest.writeString(this.datafi);
	}

	protected Model_LlistaAbonats(Parcel in) {
		this.codillistaabonats = in.readLong();
		this.matricula = in.readString();
		this.datainici = in.readString();
		this.datafi = in.readString();
	}

	public static final Creator<Model_LlistaAbonats> CREATOR = new Creator<Model_LlistaAbonats>() {
		@Override
		public Model_LlistaAbonats createFromParcel(Parcel source) {
			return new Model_LlistaAbonats(source);
		}

		@Override
		public Model_LlistaAbonats[] newArray(int size) {
			return new Model_LlistaAbonats[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Model_LlistaAbonats that = (Model_LlistaAbonats) o;

		if (codillistaabonats != that.codillistaabonats) return false;
		return matricula != null ? matricula.equals(that.matricula) : that.matricula == null;

	}
}