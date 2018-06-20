package com.sixtemia.gesbluedroid.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sixtemia.gesbluedroid.datamanager.database.Taules;

/**
 * Created by joelabello on 17/8/16.
 */
@DatabaseTable(tableName = Taules.TAULA_LLISTABLANCA)
public class Model_LlistaBlanca implements Parcelable {

	public static final String ID = "codillistablanca";

	@DatabaseField(id = true, columnName = ID, columnDefinition = "NUMBER")
	private long codillistablanca;

	@DatabaseField(columnName = "matricula", columnDefinition = "VARCHAR(100)")
	private String matricula;


	public Model_LlistaBlanca(long codillistablanca, String matricula) {
		this.codillistablanca = codillistablanca;
		this.matricula = matricula;
	}

	public Model_LlistaBlanca() {
		this.codillistablanca = -1;
		this.matricula = "";
	}

	public long getCodillistablanca() {
		return codillistablanca;
	}

	public void setCodillistablanca(long codillistablanca) {
		this.codillistablanca = codillistablanca;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.codillistablanca);
		dest.writeString(this.matricula);
	}

	protected Model_LlistaBlanca(Parcel in) {
		this.codillistablanca = in.readLong();
		this.matricula = in.readString();
	}

	public static final Creator<Model_LlistaBlanca> CREATOR = new Creator<Model_LlistaBlanca>() {
		@Override
		public Model_LlistaBlanca createFromParcel(Parcel source) {
			return new Model_LlistaBlanca(source);
		}

		@Override
		public Model_LlistaBlanca[] newArray(int size) {
			return new Model_LlistaBlanca[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Model_LlistaBlanca that = (Model_LlistaBlanca) o;

		if (codillistablanca != that.codillistablanca) return false;
		return matricula != null ? matricula.equals(that.matricula) : that.matricula == null;

	}
}