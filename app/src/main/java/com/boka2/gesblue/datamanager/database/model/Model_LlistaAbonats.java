package com.boka2.gesblue.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.boka2.gesblue.datamanager.database.Taules;

/*
 * Created by Boka2.
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


	@DatabaseField(columnName = "zona1", columnDefinition = "NUMBER")
	private long zona1;

	@DatabaseField(columnName = "zona2", columnDefinition = "NUMBER")
	private long zona2;

	@DatabaseField(columnName = "zona3", columnDefinition = "NUMBER")
	private long zona3;

	@DatabaseField(columnName = "zona4", columnDefinition = "NUMBER")
	private long zona4;

	@DatabaseField(columnName = "zona5", columnDefinition = "NUMBER")
	private long zona5;



	public Model_LlistaAbonats(long codillistaabonats, String matricula, String datainici, String datafi,long zona1,long zona2,long zona3,long zona4,long zona5) {
		this.codillistaabonats = codillistaabonats;
		this.matricula = matricula;
		this.datainici = datainici;
		this.datafi = datafi;
		this.zona1=zona1;
		this.zona2=zona2;
		this.zona3=zona3;
		this.zona4=zona4;
		this.zona5=zona5;
	}

	public Model_LlistaAbonats() {
		this.codillistaabonats = -1;
		this.matricula = "";
		this.datainici = "";
		this.datafi = "";
		this.zona1=0;
		this.zona2=0;
		this.zona3=0;
		this.zona4=0;
		this.zona5=0;
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

	public long getZona1() {
		return zona1;
	}

	public void setZona1(long zona1) {
		this.zona1 = zona1;
	}

	public long getZona2() {
		return zona2;
	}

	public void setZona2(long zona2) {
		this.zona2 = zona2;
	}

	public long getZona3() {
		return zona3;
	}

	public void setZona3(long zona3) {
		this.zona3 = zona3;
	}

	public long getZona4() {
		return zona4;
	}

	public void setZona4(long zona4) {
		this.zona4 = zona4;
	}

	public long getZona5() {
		return zona5;
	}

	public void setZona5(long zona5) {
		this.zona5 = zona5;
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
		dest.writeLong(this.zona1);
		dest.writeLong(this.zona2);
		dest.writeLong(this.zona3);
		dest.writeLong(this.zona4);
		dest.writeLong(this.zona5);
	}

	protected Model_LlistaAbonats(Parcel in) {
		this.codillistaabonats = in.readLong();
		this.matricula = in.readString();
		this.datainici = in.readString();
		this.datafi = in.readString();
		this.zona1 = in.readLong();
		this.zona2 = in.readLong();
		this.zona3 = in.readLong();
		this.zona4 = in.readLong();
		this.zona5 = in.readLong();
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