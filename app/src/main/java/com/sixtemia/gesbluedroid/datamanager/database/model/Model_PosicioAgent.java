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
@DatabaseTable(tableName = Taules.TAULA_POSICIOAGENT)
public class Model_PosicioAgent implements Parcelable {

	@DatabaseField(generatedId = true, columnName = "codiposicio")
	private long codiposicio;

	@DatabaseField(columnName = "agent", columnDefinition = "BIGINT")
	private long agent;

	@DatabaseField(columnName = "terminal", columnDefinition = "BIGINT")
	private long terminal;

	@DatabaseField(columnName = "coordenadas", columnDefinition = "VARCHAR(100)")
	private String coordenadas;

	@DatabaseField(columnName = "fechaposicio", columnDefinition = "DATETIME")
	private Date fechaposicio;

	@DatabaseField(columnName = "accioposicio", columnDefinition = "BIGINT")
	private long accioposicio;

	public Model_PosicioAgent() {
		this.codiposicio = -1;
		this.agent = -1;
		this.terminal = -1;
		this.coordenadas = "";
		this.fechaposicio = new Date();
		this.accioposicio = -1;
	}

	public Model_PosicioAgent(long codiposicio, long agent, long terminal, String coordenadas, Date fechaposicio, long accioposicio) {
		this.codiposicio = codiposicio;
		this.agent = agent;
		this.terminal = terminal;
		this.coordenadas = coordenadas;
		this.fechaposicio = fechaposicio;
		this.accioposicio = accioposicio;
	}

	public long getCodiposicio() {
		return codiposicio;
	}

	public void setCodiposicio(long codiposicio) {
		this.codiposicio = codiposicio;
	}

	public long getAgent() {
		return agent;
	}

	public void setAgent(long agent) {
		this.agent = agent;
	}

	public long getTerminal() {
		return terminal;
	}

	public void setTerminal(long terminal) {
		this.terminal = terminal;
	}

	public String getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}

	public Date getFechaposicio() {
		return fechaposicio;
	}

	public void setFechaposicio(Date fechaposicio) {
		this.fechaposicio = fechaposicio;
	}

	public long getAccioposicio() {
		return accioposicio;
	}

	public void setAccioposicio(long accioposicio) {
		this.accioposicio = accioposicio;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.codiposicio);
		dest.writeLong(this.agent);
		dest.writeLong(this.terminal);
		dest.writeString(this.coordenadas);
		dest.writeLong(this.fechaposicio != null ? this.fechaposicio.getTime() : -1);
		dest.writeLong(this.accioposicio);
	}

	protected Model_PosicioAgent(Parcel in) {
		this.codiposicio = in.readLong();
		this.agent = in.readLong();
		this.terminal = in.readLong();
		this.coordenadas = in.readString();
		long tmpFechaposicio = in.readLong();
		this.fechaposicio = tmpFechaposicio == -1 ? null : new Date(tmpFechaposicio);
		this.accioposicio = in.readLong();
	}

	public static final Parcelable.Creator<Model_PosicioAgent> CREATOR = new Parcelable.Creator<Model_PosicioAgent>() {
		@Override
		public Model_PosicioAgent createFromParcel(Parcel source) {
			return new Model_PosicioAgent(source);
		}

		@Override
		public Model_PosicioAgent[] newArray(int size) {
			return new Model_PosicioAgent[size];
		}
	};
}
