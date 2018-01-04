
package com.sixtemia.gesbluedroid.datamanager.database.model;

        import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sixtemia.gesbluedroid.datamanager.database.Taules;


@DatabaseTable(tableName = Taules.TAULA_LOG)
public class Model_Log implements Parcelable {

    public static final String ID = "codilog";

    @DatabaseField(generatedId = true,allowGeneratedIdInsert=true,columnName = ID)
    private Integer codilog;

    @DatabaseField(columnName = "fecha", columnDefinition = "VARCHAR(100)")
    private String fechalog;

    @DatabaseField(columnName = "concessio", columnDefinition = "VARCHAR(100)")
    private String concessiolog;

    @DatabaseField(columnName = "idagent", columnDefinition = "INTEGER")
    private Integer idagent;

    @DatabaseField(columnName = "codi_accio_log", columnDefinition = "INTENGER")
    private Integer codiacciolog;

    @DatabaseField(columnName = "versio_app", columnDefinition = "VARCHAR(100)")
    private String versioapp;

    @DatabaseField(columnName = "info", columnDefinition = "VARCHAR(1000)")
    private String info;

    @DatabaseField(columnName = "enviat", columnDefinition = "INTEGER")
    private Integer enviat;

    public Model_Log( String fechalog, String concessiolog, Integer idagent, Integer codiacciolog, String versioapp, String info, Integer enviat) {
        this.fechalog = fechalog;
        this.concessiolog = concessiolog;
        this.idagent = idagent;
        this.codiacciolog = codiacciolog;
        this.versioapp = versioapp;
        this.info = info;
        this.enviat = enviat;
    }

    public Model_Log() {
        this.fechalog = "";
        this.concessiolog = "";
        this.idagent = 0;
        this.codiacciolog = 0;
        this.versioapp = "";
        this.info = "";
        this.enviat = 0;
    }

    public String getID() {
        return ID;
    }

    public String getFechalog() {
        return fechalog;
    }

    public void setFechalog(String fechalog) {
        this.fechalog = fechalog;
    }

    public String getConcessiolog() {
        return concessiolog;
    }

    public void setConcessiolog(String concessiolog) {
        this.concessiolog = concessiolog;
    }

    public Integer getIdagent() {
        return idagent;
    }

    public void setIdagent(Integer idagent) {
        this.idagent = idagent;
    }

    public Integer getCodiacciolog() {
        return codiacciolog;
    }

    public void setCodiacciolog(Integer codiacciolog) {
        this.codiacciolog = codiacciolog;
    }

    public String getVersioapp() {
        return versioapp;
    }

    public void setVersioapp(String versioapp) {
        this.versioapp = versioapp;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getEnviat() {
        return enviat;
    }

    public void setEnviat(Integer enviat) {
        this.enviat = enviat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fechalog);
        dest.writeString(this.concessiolog);
        dest.writeInt(this.idagent);
        dest.writeInt(this.codiacciolog);
        dest.writeString(this.versioapp);
        dest.writeString(this.info);
        dest.writeInt(this.enviat);
    }

    protected Model_Log(Parcel in) {
        this.fechalog = in.readString();
        this.concessiolog = in.readString();
        this.idagent = in.readInt();
        this.codiacciolog = in.readInt();
        this.versioapp = in.readString();
        this.info = in.readString();
        this.enviat = in.readInt();
    }

    public static final Parcelable.Creator<Model_Log> CREATOR = new Parcelable.Creator<Model_Log>() {
        @Override
        public Model_Log createFromParcel(Parcel source) {
            return new Model_Log(source);
        }

        @Override
        public Model_Log[] newArray(int size) {
            return new Model_Log[size];
        }
    };
/*
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model_Log that = (Model_Log) o;

        if (codimarca != null ? !codimarca.equals(that.codimarca) : that.codimarca != null)
            return false;
        if (nommarca != null ? !nommarca.equals(that.nommarca) : that.nommarca != null)
            return false;
        if (imatgemarca != null ? !imatgemarca.equals(that.imatgemarca) : that.imatgemarca != null)
            return false;
        return fechaactualitzacio != null ? fechaactualitzacio.equals(that.fechaactualitzacio) : that.fechaactualitzacio == null;

    }*/
}