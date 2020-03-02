package com.boka2.gesblue.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.global.Constants;
import com.boka2.gesblue.model.Tipus_Vehicle;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class TipusVehiclesResponse {

	@JSoapResField(name = "tipusvehicles")
	@SerializedName("tipusvehicles")
	private ArrayList<Tipus_Vehicle> tipusvehicles;


	public TipusVehiclesResponse() {
		super();
	}

	public ArrayList<Tipus_Vehicle> getTipusvehicles() {
		return tipusvehicles;
	}

	public void setTipusvehicles(ArrayList<Tipus_Vehicle> tipusvehicles) {
		this.tipusvehicles = tipusvehicles;
	}

	public String getDefaultValue() {
		if(!tipusvehicles.isEmpty()) {
			for(Tipus_Vehicle v : tipusvehicles) {
				if(v.getDefaultValue() == 1) {
					return v.getCodi();
				}
			}
		}
		return "";
	}

	@Override
	public String toString() {
		return "TipusVehiclesResponse{" +
				"tipusvehicles=" + tipusvehicles +
				'}';
	}
}