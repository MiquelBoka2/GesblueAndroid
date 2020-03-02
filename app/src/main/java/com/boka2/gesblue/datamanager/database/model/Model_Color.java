package com.boka2.gesblue.datamanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.boka2.gesblue.datamanager.database.Taules;

/**
 * Created by Boka2.
 */
@DatabaseTable(tableName = Taules.TAULA_COLOR)
public class Model_Color implements Parcelable {

	public static final String ID = "codicolor";

	@DatabaseField(columnName = ID, id = true, columnDefinition = "VARCHAR(100)")
	private String codicolor;

	@DatabaseField(columnName = "nomcolorcat", columnDefinition = "VARCHAR(100)")
	private String nomcolorcat;

	@DatabaseField(columnName = "nomcolores", columnDefinition = "VARCHAR(100)")
	private String nomcolores;

	@DatabaseField(columnName = "nomcoloreng", columnDefinition = "VARCHAR(100)")
	private String nomcoloreng;

	@DatabaseField(columnName = "hexcolor", columnDefinition = "VARCHAR(100)")
	private String hexcolor;

	public Model_Color(String codicolor, String nomcolorcat, String nomcolores, String nomcoloreng, String hexcolor) {
		this.codicolor = codicolor;
		this.nomcolorcat = nomcolorcat;
		this.nomcolores = nomcolores;
		this.nomcoloreng = nomcoloreng;
		this.hexcolor = hexcolor;
	}

	public Model_Color() {
		this.codicolor = "";
		this.nomcolorcat = "";
		this.nomcolores = "";
		this.nomcoloreng = "";
		this.hexcolor = "";
	}

	public String getCodicolor() {
		return codicolor;
	}

	public void setCodicolor(String codicolor) {
		this.codicolor = codicolor;
	}

	public String getNomcolorcat() {
		return nomcolorcat;
	}

	public void setNomcolorcat(String nomcolorcat) {
		this.nomcolorcat = nomcolorcat;
	}

	public String getNomcolores() {
		return nomcolores;
	}

	public void setNomcolores(String nomcolores) {
		this.nomcolores = nomcolores;
	}

	public String getNomcoloreng() {
		return nomcoloreng;
	}

	public void setNomcoloreng(String nomcoloreng) {
		this.nomcoloreng = nomcoloreng;
	}

	public String getHexcolor() {
		if(TextUtils.isEmpty(hexcolor)) {
			return "ffffff";
		}
		return hexcolor;
	}

	public void setHexcolor(String hexcolor) {
		this.hexcolor = hexcolor;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.codicolor);
		dest.writeString(this.nomcolorcat);
		dest.writeString(this.nomcolores);
		dest.writeString(this.nomcoloreng);
		dest.writeString(this.hexcolor);
	}

	protected Model_Color(Parcel in) {
		this.codicolor = in.readString();
		this.nomcolorcat = in.readString();
		this.nomcolores = in.readString();
		this.nomcoloreng = in.readString();
		this.hexcolor = in.readString();
	}

	public static final Parcelable.Creator<Model_Color> CREATOR = new Parcelable.Creator<Model_Color>() {
		@Override
		public Model_Color createFromParcel(Parcel source) {
			return new Model_Color(source);
		}

		@Override
		public Model_Color[] newArray(int size) {
			return new Model_Color[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Model_Color that = (Model_Color) o;

		if (codicolor != null ? !codicolor.equals(that.codicolor) : that.codicolor != null)
			return false;
		if (nomcolorcat != null ? !nomcolorcat.equals(that.nomcolorcat) : that.nomcolorcat != null)
			return false;
		if (nomcolores != null ? !nomcolores.equals(that.nomcolores) : that.nomcolores != null)
			return false;
		if (nomcoloreng != null ? !nomcoloreng.equals(that.nomcoloreng) : that.nomcoloreng != null)
			return false;
		return hexcolor != null ? hexcolor.equals(that.hexcolor) : that.hexcolor == null;

	}
}