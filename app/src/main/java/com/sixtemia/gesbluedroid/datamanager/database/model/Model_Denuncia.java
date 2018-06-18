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
@DatabaseTable(tableName = Taules.TAULA_DENUNCIA)
public class Model_Denuncia implements Parcelable {

	@DatabaseField(generatedId = true, columnName = "codidenunciaauto")
	private long codidenunciaauto;

	@DatabaseField(columnName = "codidenuncia", columnDefinition = "VARCHAR(100)")
	private String codidenuncia;

	@DatabaseField(columnName = "fechacreacio", columnDefinition = "DATETIME")
	private Date fechacreacio;

	@DatabaseField(columnName = "agent", columnDefinition = "BIGINT")
	private double agent;

	@DatabaseField(columnName = "adrecacarrer", columnDefinition = "BIGINT")
	private double adrecacarrer;

	@DatabaseField(columnName = "adrecanum", columnDefinition = "BIGINT")
	private double adrecanum;

	@DatabaseField(columnName = "posicio", columnDefinition = "VARCHAR(100)")
	private String posicio;

	@DatabaseField(columnName = "matricula", columnDefinition = "VARCHAR(100)")
	private String matricula;

	@DatabaseField(columnName = "tipusvehicle", columnDefinition = "BIGINT")
	private double tipusvehicle;

	@DatabaseField(columnName = "marca", columnDefinition = "BIGINT")
	private long marca;

	@DatabaseField(columnName = "model", columnDefinition = "BIGINT")
	private double model;

	@DatabaseField(columnName = "color", columnDefinition = "BIGINT")
	private double color;

	@DatabaseField(columnName = "infraccio", columnDefinition = "BIGINT")
	private double infraccio;

	@DatabaseField(columnName = "terminal", columnDefinition = "BIGINT")
	private double terminal;

	@DatabaseField(columnName = "foto1", columnDefinition = "VARCHAR(100)")
	private String foto1;

	@DatabaseField(columnName = "foto2", columnDefinition = "VARCHAR(100)")
	private String foto2;

	@DatabaseField(columnName = "foto3", columnDefinition = "VARCHAR(100)")
	private String foto3;

	@DatabaseField(columnName = "foto4", columnDefinition = "VARCHAR(100)")
	private String foto4;

	@DatabaseField(columnName = "fechaanulacio", columnDefinition = "DATETIME")
	private Date fechaanulacio;

	@DatabaseField(columnName = "agentanulacio", columnDefinition = "BIGINT")
	private double agentanulacio;

	@DatabaseField(columnName = "tipusanulacio", columnDefinition = "BIGINT")
	private double tipusanulacio;

	@DatabaseField(columnName = "zona", columnDefinition = "BIGINT")
	private double zona;

	public Model_Denuncia(long codidenunciaauto, String codidenuncia, Date fechacreacio, double agent, double adrecacarrer, double adrecanum, String posicio, String matricula, double tipusvehicle, long marca, double model, double color, double infraccio, double terminal, String foto1, String foto2, String foto3, String foto4, Date fechaanulacio, double agentanulacio, double tipusanulacio, double zona) {
		this.codidenunciaauto = codidenunciaauto;
		this.codidenuncia = codidenuncia;
		this.fechacreacio = fechacreacio;
		this.agent = agent;
		this.adrecacarrer = adrecacarrer;
		this.adrecanum = adrecanum;
		this.posicio = posicio;
		this.matricula = matricula;
		this.tipusvehicle = tipusvehicle;
		this.marca = marca;
		this.model = model;
		this.color = color;
		this.infraccio = infraccio;
		this.terminal = terminal;
		this.foto1 = foto1;
		this.foto2 = foto2;
		this.foto3 = foto3;
		this.foto4 = foto4;
		this.fechaanulacio = fechaanulacio;
		this.agentanulacio = agentanulacio;
		this.tipusanulacio = tipusanulacio;
		this.zona = zona;
	}


	public Model_Denuncia() {
		this.codidenunciaauto = -1;
		this.codidenuncia = "";
		this.fechacreacio = new Date();
		this.agent = -1;
		this.adrecacarrer = -1;
		this.adrecanum = -1;
		this.posicio = "";
		this.matricula = "";
		this.tipusvehicle = -1;
		this.marca = -1;
		this.model = -1;
		this.color = -1;
		this.infraccio = -1;
		this.terminal = -1;
		this.foto1 = "";
		this.foto2 = "";
		this.foto3 = "";
		this.foto4 = "";
		this.fechaanulacio = new Date();
		this.agentanulacio = -1;
		this.tipusanulacio = -1;
		this.zona = -1;
	}

	public long getCodidenunciaauto() {
		return codidenunciaauto;
	}

	public void setCodidenunciaauto(long codidenunciaauto) {
		this.codidenunciaauto = codidenunciaauto;
	}

	public String getCodidenuncia() {
		return codidenuncia;
	}

	public void setCodidenuncia(String codidenuncia) {
		this.codidenuncia = codidenuncia;
	}

	public Date getFechacreacio() {
		return fechacreacio;
	}

	public void setFechacreacio(Date fechacreacio) {
		this.fechacreacio = fechacreacio;
	}

	public double getAgent() {
		return agent;
	}

	public void setAgent(double agent) {
		this.agent = agent;
	}

	public double getAdrecacarrer() {
		return adrecacarrer;
	}

	public void setAdrecacarrer(double adrecacarrer) {
		this.adrecacarrer = adrecacarrer;
	}

	public double getAdrecanum() {
		return adrecanum;
	}

	public void setAdrecanum(double adrecanum) {
		this.adrecanum = adrecanum;
	}

	public String getPosicio() {
		return posicio;
	}

	public void setPosicio(String posicio) {
		this.posicio = posicio;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public double getTipusvehicle() {
		return tipusvehicle;
	}

	public void setTipusvehicle(double tipusvehicle) {
		this.tipusvehicle = tipusvehicle;
	}

	public long getMarca() {
		return marca;
	}

	public void setMarca(long marca) {
		this.marca = marca;
	}

	public double getModel() {
		return model;
	}

	public void setModel(double model) {
		this.model = model;
	}

	public double getColor() {
		return color;
	}

	public void setColor(double color) {
		this.color = color;
	}

	public double getInfraccio() {
		return infraccio;
	}

	public void setInfraccio(double infraccio) {
		this.infraccio = infraccio;
	}

	public double getTerminal() {
		return terminal;
	}

	public void setTerminal(double terminal) {
		this.terminal = terminal;
	}

	public String getFoto1() {
		return foto1;
	}

	public void setFoto1(String foto1) {
		this.foto1 = foto1;
	}

	public String getFoto2() {
		return foto2;
	}

	public void setFoto2(String foto2) {
		this.foto2 = foto2;
	}

	public String getFoto3() {
		return foto3;
	}

	public void setFoto3(String foto3) {
		this.foto3 = foto3;
	}

	public String getFoto4() {
		return foto4;
	}

	public void setFoto4(String foto4) {
		this.foto4 = foto4;
	}

	public Date getFechaanulacio() {
		return fechaanulacio;
	}

	public void setFechaanulacio(Date fechaanulacio) {
		this.fechaanulacio = fechaanulacio;
	}

	public double getAgentanulacio() {
		return agentanulacio;
	}

	public void setAgentanulacio(double agentanulacio) {
		this.agentanulacio = agentanulacio;
	}

	public double getTipusanulacio() {
		return tipusanulacio;
	}

	public void setTipusanulacio(double tipusanulacio) {
		this.tipusanulacio = tipusanulacio;
	}


	public double getZona() {
		return zona;
	}

	public void setZona(double zona) {
		this.zona = zona;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.codidenunciaauto);
		dest.writeString(this.codidenuncia);
		dest.writeLong(this.fechacreacio != null ? this.fechacreacio.getTime() : -1);
		dest.writeDouble(this.agent);
		dest.writeDouble(this.adrecacarrer);
		dest.writeDouble(this.adrecanum);
		dest.writeString(this.posicio);
		dest.writeString(this.matricula);
		dest.writeDouble(this.tipusvehicle);
		dest.writeLong(this.marca);
		dest.writeDouble(this.model);
		dest.writeDouble(this.color);
		dest.writeDouble(this.infraccio);
		dest.writeDouble(this.terminal);
		dest.writeString(this.foto1);
		dest.writeString(this.foto2);
		dest.writeString(this.foto3);
		dest.writeString(this.foto4);
		dest.writeLong(this.fechaanulacio != null ? this.fechaanulacio.getTime() : -1);
		dest.writeDouble(this.agentanulacio);
		dest.writeDouble(this.tipusanulacio);
		dest.writeDouble(this.zona);
	}

	protected Model_Denuncia(Parcel in) {
		this.codidenunciaauto = in.readLong();
		this.codidenuncia = in.readString();
		long tmpFechacreacio = in.readLong();
		this.fechacreacio = tmpFechacreacio == -1 ? null : new Date(tmpFechacreacio);
		this.agent = in.readDouble();
		this.adrecacarrer = in.readDouble();
		this.adrecanum = in.readDouble();
		this.posicio = in.readString();
		this.matricula = in.readString();
		this.tipusvehicle = in.readDouble();
		this.marca = in.readLong();
		this.model = in.readDouble();
		this.color = in.readDouble();
		this.infraccio = in.readDouble();
		this.terminal = in.readDouble();
		this.foto1 = in.readString();
		this.foto2 = in.readString();
		this.foto3 = in.readString();
		this.foto4 = in.readString();
		long tmpFechaanulacio = in.readLong();
		this.fechaanulacio = tmpFechaanulacio == -1 ? null : new Date(tmpFechaanulacio);
		this.agentanulacio = in.readDouble();
		this.tipusanulacio = in.readDouble();
		this.zona = in.readDouble();
	}

	public static final Creator<Model_Denuncia> CREATOR = new Creator<Model_Denuncia>() {
		@Override
		public Model_Denuncia createFromParcel(Parcel source) {
			return new Model_Denuncia(source);
		}

		@Override
		public Model_Denuncia[] newArray(int size) {
			return new Model_Denuncia[size];
		}
	};
}