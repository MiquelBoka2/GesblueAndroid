package com.boka2.gesblue.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.boka2.gesblue.datamanager.database.Taules;

/*
 * Created by Boka2.
 */
@DatabaseTable(tableName = Taules.TAULA_AGENT)
public class Model_Agent implements Parcelable {

	public static final String ID = "id_agent";

	@DatabaseField(generatedId = true, columnName = "idagent_automatic")
	private long idagent_automatic;

	@DatabaseField(columnName = "codiagent", columnDefinition = "INTEGER")
	private long codiagent;

	@DatabaseField(columnName = "nomagent", columnDefinition = "VARCHAR(100)")
	private String nomagent;

	@DatabaseField(columnName = "cognomagent", columnDefinition = "VARCHAR(100)")
	private String cognomagent;

	@DatabaseField(columnName = "login", columnDefinition = "VARCHAR(100)")
	private String login;

	@DatabaseField(columnName = "password", columnDefinition = "VARCHAR(100)")
	private String password;

	@DatabaseField(columnName = "tipus", columnDefinition = "VARCHAR(100)")
	private String tipus;

	@DatabaseField(columnName = "identificador", columnDefinition = "VARCHAR(100)")
	private String identificador;

	public Model_Agent(long codiagent, String nomagent, String cognomagent, String login, String password, String tipus, String identificador) {
		this.codiagent = codiagent;
		this.nomagent = nomagent;
		this.cognomagent = cognomagent;
		this.login = login;
		this.password = password;
		this.tipus = tipus;
		this.identificador = identificador;
	}

	public Model_Agent() {
		this.codiagent = -1;
		this.nomagent = "";
		this.cognomagent = "";
		this.login = "";
		this.password = "";
		this.tipus = "";
		this.identificador = "";
	}

	public long getCodiagent() {
		return codiagent;
	}

	public void setCodiagent(long codiagent) {
		this.codiagent = codiagent;
	}

	public String getNomagent() {
		return nomagent;
	}

	public void setNomagent(String nomagent) {
		this.nomagent = nomagent;
	}

	public String getCognomagent() {
		return cognomagent;
	}

	public void setCognomagent(String cognomagent) {
		this.cognomagent = cognomagent;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTipus() {
		return tipus;
	}

	public void setTipus(String tipus) {
		this.tipus = tipus;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.codiagent);
		dest.writeString(this.nomagent);
		dest.writeString(this.cognomagent);
		dest.writeString(this.login);
		dest.writeString(this.password);
		dest.writeString(this.tipus);
		dest.writeString(this.identificador);
	}

	protected Model_Agent(Parcel in) {
		this.codiagent = in.readLong();
		this.nomagent = in.readString();
		this.cognomagent = in.readString();
		this.login = in.readString();
		this.password = in.readString();
		this.tipus = in.readString();
		this.identificador = in.readString();
	}

	public static final Parcelable.Creator<Model_Agent> CREATOR = new Parcelable.Creator<Model_Agent>() {
		@Override
		public Model_Agent createFromParcel(Parcel source) {
			return new Model_Agent(source);
		}

		@Override
		public Model_Agent[] newArray(int size) {
			return new Model_Agent[size];
		}
	};
}
