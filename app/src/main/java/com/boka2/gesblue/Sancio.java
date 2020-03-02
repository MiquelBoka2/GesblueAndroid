package com.boka2.gesblue;

import android.os.Parcel;
import android.os.Parcelable;

import com.boka2.gesblue.datamanager.database.model.Model_Carrer;
import com.boka2.gesblue.datamanager.database.model.Model_Color;
import com.boka2.gesblue.datamanager.database.model.Model_Infraccio;
import com.boka2.gesblue.datamanager.database.model.Model_Marca;
import com.boka2.gesblue.datamanager.database.model.Model_Model;
import com.boka2.gesblue.datamanager.database.model.Model_TipusVehicle;
import com.boka2.gesblue.datamanager.database.model.Model_Zona;

public class Sancio implements Parcelable {

    private String matricula;
    private String numero;
    private Model_TipusVehicle modelTipusVehicle;
    private Model_Marca modelMarca;
    private Model_Model modelModel;
    private Model_Color modelColor;
    private Model_Infraccio modelInfraccio;
    private Model_Zona modelZona;
    private Model_Carrer modelCarrer;

    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Model_TipusVehicle getModelTipusVehicle() {
        return modelTipusVehicle;
    }
    public void setModelTipusVehicle(Model_TipusVehicle modelTipusVehicle) {
        this.modelTipusVehicle = modelTipusVehicle;
    }

    public Model_Marca getModelMarca() {
        return modelMarca;
    }
    public void setModelMarca(Model_Marca modelMarca) {
        this.modelMarca = modelMarca;
    }

    public Model_Model getModelModel() {
        return modelModel;
    }
    public void setModelModel(Model_Model modelModel) {
        this.modelModel = modelModel;
    }

    public Model_Color getModelColor() {
        return modelColor;
    }
    public void setModelColor(Model_Color modelColor) {
        this.modelColor = modelColor;
    }

    public Model_Infraccio getModelInfraccio() {
        return modelInfraccio;
    }
    public void setModelInfraccio(Model_Infraccio modelInfraccio) {
        this.modelInfraccio = modelInfraccio;
    }

    public Model_Carrer getModelCarrer() {
        return modelCarrer;
    }
    public void setModelCarrer(Model_Carrer modelCarrer) {
        this.modelCarrer = modelCarrer;
    }


    public Model_Zona getModelZona() {
        return modelZona;
    }
    public void setModelZona(Model_Zona modelZona) {
        this.modelZona = modelZona;
    }

    public Sancio() {
    }
    public Sancio(String matricula, String _numero, Model_TipusVehicle modelTipusVehicle, Model_Marca modelMarca, Model_Model modelModel, Model_Color modelColor, Model_Infraccio modelInfraccio, Model_Carrer modelCarrer, Model_Zona modelZona) {
        this.matricula = matricula;
        this.numero = _numero;
        this.modelTipusVehicle = modelTipusVehicle;
        this.modelMarca = modelMarca;
        this.modelModel = modelModel;
        this.modelColor = modelColor;
        this.modelInfraccio = modelInfraccio;
        this.modelZona = modelZona;
        this.modelCarrer = modelCarrer;
    }

    public void setImportInfraccio(String _importe) {
        this.getModelInfraccio().setImport(_importe);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.matricula);
        dest.writeString(this.numero);
        dest.writeParcelable(this.modelTipusVehicle, flags);
        dest.writeParcelable(this.modelMarca, flags);
        dest.writeParcelable(this.modelModel, flags);
        dest.writeParcelable(this.modelColor, flags);
        dest.writeParcelable(this.modelInfraccio, flags);
        dest.writeParcelable(this.modelZona, flags);
        dest.writeParcelable(this.modelCarrer, flags);
    }
    protected Sancio(Parcel in) {
        this.matricula = in.readString();
        this.numero = in.readString();
        this.modelTipusVehicle = in.readParcelable(Model_TipusVehicle.class.getClassLoader());
        this.modelMarca = in.readParcelable(Model_Marca.class.getClassLoader());
        this.modelModel = in.readParcelable(Model_Model.class.getClassLoader());
        this.modelColor = in.readParcelable(Model_Color.class.getClassLoader());
        this.modelInfraccio = in.readParcelable(Model_Infraccio.class.getClassLoader());
        this.modelZona = in.readParcelable(Model_Zona.class.getClassLoader());
        this.modelCarrer = in.readParcelable(Model_Carrer.class.getClassLoader());
    }
    public static final Creator<Sancio> CREATOR = new Creator<Sancio>() {
        @Override
        public Sancio createFromParcel(Parcel source) {
            return new Sancio(source);
        }

        @Override
        public Sancio[] newArray(int size) {
            return new Sancio[size];
        }
    };
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sancio sancio = (Sancio) o;

        if (matricula != null ? !matricula.equals(sancio.matricula) : sancio.matricula != null)
            return false;
        if (numero != null ? !numero.equals(sancio.numero) : sancio.numero != null) return false;
        if (modelTipusVehicle != null ? !modelTipusVehicle.equals(sancio.modelTipusVehicle) : sancio.modelTipusVehicle != null)
            return false;
        if (modelMarca != null ? !modelMarca.equals(sancio.modelMarca) : sancio.modelMarca != null)
            return false;
        if (modelModel != null ? !modelModel.equals(sancio.modelModel) : sancio.modelModel != null)
            return false;
        if (modelColor != null ? !modelColor.equals(sancio.modelColor) : sancio.modelColor != null)
            return false;
        if (modelInfraccio != null ? !modelInfraccio.equals(sancio.modelInfraccio) : sancio.modelInfraccio != null)
            return false;

        if (modelZona != null ? modelZona.equals(sancio.modelZona) : sancio.modelZona != null)
            return false;

        return modelCarrer != null ? modelCarrer.equals(sancio.modelCarrer) : sancio.modelCarrer == null;

    }
}
