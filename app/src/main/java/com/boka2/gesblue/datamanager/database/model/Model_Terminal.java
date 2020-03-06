package com.boka2.gesblue.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.boka2.gesblue.datamanager.database.Taules;

/*
 * Created by Boka2.
 */
@DatabaseTable(tableName = Taules.TAULA_TERMINAL)
public class Model_Terminal implements Parcelable {

	@DatabaseField(generatedId = true, columnName = "coditerminal")
	private long coditerminal;

	@DatabaseField(columnName = "descripcioterminal", columnDefinition = "VARCHAR(100)")
	private String descripcioterminal;

	@DatabaseField(columnName = "uuidterminal", columnDefinition = "VARCHAR(100)")
	private String uuidterminal;

	@DatabaseField(columnName = "marca", columnDefinition = "VARCHAR(100)")
	private String marca;

	@DatabaseField(columnName = "model", columnDefinition = "VARCHAR(100)")
	private String model;

	@DatabaseField(columnName = "sistemaoperatiu", columnDefinition = "VARCHAR(100)")
	private String sistemaoperatiu;

	@DatabaseField(columnName = "versiosistema", columnDefinition = "VARCHAR(100)")
	private String versiosistema;

	@DatabaseField(columnName = "estat", columnDefinition = "VARCHAR(100)")
	private String estat;

	public Model_Terminal(long coditerminal, String descripcioterminal, String uuidterminal, String marca, String model, String sistemaoperatiu, String versiosistema, String estat) {
		this.coditerminal = coditerminal;
		this.descripcioterminal = descripcioterminal;
		this.uuidterminal = uuidterminal;
		this.marca = marca;
		this.model = model;
		this.sistemaoperatiu = sistemaoperatiu;
		this.versiosistema = versiosistema;
		this.estat = estat;
	}

	public Model_Terminal() {
		this.coditerminal = -1;
		this.descripcioterminal = "";
		this.uuidterminal = "";
		this.marca = "";
		this.model = "";
		this.sistemaoperatiu = "";
		this.versiosistema = "";
		this.estat = "";
	}

	public long getCoditerminal() {
		return coditerminal;
	}

	public void setCoditerminal(long coditerminal) {
		this.coditerminal = coditerminal;
	}

	public String getDescripcioterminal() {
		return descripcioterminal;
	}

	public void setDescripcioterminal(String descripcioterminal) {
		this.descripcioterminal = descripcioterminal;
	}

	public String getUuidterminal() {
		return uuidterminal;
	}

	public void setUuidterminal(String uuidterminal) {
		this.uuidterminal = uuidterminal;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSistemaoperatiu() {
		return sistemaoperatiu;
	}

	public void setSistemaoperatiu(String sistemaoperatiu) {
		this.sistemaoperatiu = sistemaoperatiu;
	}

	public String getVersiosistema() {
		return versiosistema;
	}

	public void setVersiosistema(String versiosistema) {
		this.versiosistema = versiosistema;
	}

	public String getEstat() {
		return estat;
	}

	public void setEstat(String estat) {
		this.estat = estat;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.coditerminal);
		dest.writeString(this.descripcioterminal);
		dest.writeString(this.uuidterminal);
		dest.writeString(this.marca);
		dest.writeString(this.model);
		dest.writeString(this.sistemaoperatiu);
		dest.writeString(this.versiosistema);
		dest.writeString(this.estat);
	}

	protected Model_Terminal(Parcel in) {
		this.coditerminal = in.readLong();
		this.descripcioterminal = in.readString();
		this.uuidterminal = in.readString();
		this.marca = in.readString();
		this.model = in.readString();
		this.sistemaoperatiu = in.readString();
		this.versiosistema = in.readString();
		this.estat = in.readString();
	}

	public static final Parcelable.Creator<Model_Terminal> CREATOR = new Parcelable.Creator<Model_Terminal>() {
		@Override
		public Model_Terminal createFromParcel(Parcel source) {
			return new Model_Terminal(source);
		}

		@Override
		public Model_Terminal[] newArray(int size) {
			return new Model_Terminal[size];
		}
	};
}