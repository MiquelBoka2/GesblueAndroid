package com.sixtemia.gesbluedroid.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sixtemia.gesbluedroid.datamanager.database.Taules;

/**
 * Created by joelabello on 17/8/16.
 */
@DatabaseTable(tableName = Taules.TAULA_INFRACCIO)
public class Model_Infraccio implements Parcelable {

	public static final String ID = "codi";

	@DatabaseField(id = true, columnName = ID, columnDefinition = "NUMBER")
	private long codi;

	@DatabaseField(columnName = "nom", columnDefinition = "VARCHAR(100)")
	private String nom;

	@DatabaseField(columnName = "importe", columnDefinition = "VARCHAR(100)")
	private String importe;

	@DatabaseField(columnName = "anulacio", columnDefinition = "VARCHAR(100)")
	private String anulacio;

	@DatabaseField(columnName = "tempsanulacio", columnDefinition = "VARCHAR(100)")
	private String tempsanulacio;

	@DatabaseField(columnName = "anulacio2", columnDefinition = "VARCHAR(100)")
	private String anulacio2;

	@DatabaseField(columnName = "tempsanulacio2", columnDefinition = "VARCHAR(100)")
	private String tempsanulacio2;

	@DatabaseField(columnName = "precepte", columnDefinition = "VARCHAR(100)")
	private String precepte;

	@DatabaseField(columnName = "zona", columnDefinition = "NUMBER")
	private long zona;

	public Model_Infraccio() {
		this.codi = 0;
		this.nom = "";
		this.importe = "";
		this.anulacio = "";
		this.tempsanulacio = "";
		this.anulacio = "";
		this.anulacio2 = "";
		this.tempsanulacio2 = "";
		this.precepte = "";
		this.zona = 0;
	}

	public Model_Infraccio(long codi, String nom, String importe, String anulacio, String tempsanulacio, String anulacio2, String tempsanulacio2, String precepte, long zona) {
		this.codi = codi;
		this.nom = nom;
		this.importe = importe;
		this.anulacio = anulacio;
		this.tempsanulacio = tempsanulacio;
		this.anulacio = anulacio;
		this.anulacio2 = anulacio2;
		this.tempsanulacio2 = tempsanulacio2;
		this.precepte = precepte;
		this.zona = zona;
	}

	public long getCodi() {
		return codi;
	}

	public void setCodi(long codi) {
		this.codi = codi;
	}

	public String getNom() {
		return nom;
	}

	public String getImporte() {
		return importe;
	}

	public String getAnulacio() {
		return anulacio;
	}

	public String getTempsanulacio() {
		if(TextUtils.isEmpty(tempsanulacio)) {
			return "-1";
		}
		return tempsanulacio;
	}

	public String getAnulacio2() {
		return anulacio2;
	}

	public String getTempsanulacio2() {
		if(TextUtils.isEmpty(tempsanulacio2)) {
			return "-1";
		}
		return tempsanulacio2;
	}

	public String getPrecepte() {
		return precepte;
	}

	public void setImport(String _importe) {
		this.importe = _importe;
	}

	public long getZona() {
		return zona;
	}

	public void setZona(long zona) {
		this.zona = zona;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.codi);
		dest.writeString(this.nom);
		dest.writeString(this.importe);
		dest.writeString(this.anulacio);
		dest.writeString(this.tempsanulacio);
		dest.writeString(this.anulacio2);
		dest.writeString(this.tempsanulacio2);
		dest.writeString(this.precepte);
		dest.writeLong(this.zona);
	}
	protected Model_Infraccio(Parcel in) {
		this.codi = in.readLong();
		this.nom = in.readString();
		this.importe = in.readString();
		this.anulacio = in.readString();
		this.tempsanulacio = in.readString();
		this.anulacio2 = in.readString();
		this.tempsanulacio2 = in.readString();
		this.precepte = in.readString();
		this.zona = in.readLong();
	}
	public static final Parcelable.Creator<Model_Infraccio> CREATOR = new Parcelable.Creator<Model_Infraccio>() {
		@Override
		public Model_Infraccio createFromParcel(Parcel source) {
			return new Model_Infraccio(source);
		}

		@Override
		public Model_Infraccio[] newArray(int size) {
			return new Model_Infraccio[size];
		}
	};
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Model_Infraccio that = (Model_Infraccio) o;

		if (codi != that.codi) return false;
		if (nom != null ? !nom.equals(that.nom) : that.nom != null) return false;
		if (importe != null ? !importe.equals(that.importe) : that.importe != null) return false;
		if (anulacio != null ? !anulacio.equals(that.anulacio) : that.anulacio != null)
			return false;
		if (tempsanulacio != null ? !tempsanulacio.equals(that.tempsanulacio) : that.tempsanulacio != null)
			return false;
		if (anulacio2 != null ? !anulacio2.equals(that.anulacio2) : that.anulacio2 != null)
			return false;
		if (tempsanulacio2 != null ? !tempsanulacio2.equals(that.tempsanulacio2) : that.tempsanulacio2 != null)
			return false;
		return precepte != null ? precepte.equals(that.precepte) : that.precepte == null;

	}
}