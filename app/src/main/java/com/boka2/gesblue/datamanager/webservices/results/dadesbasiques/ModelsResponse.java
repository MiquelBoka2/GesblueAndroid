package com.boka2.gesblue.datamanager.webservices.results.dadesbasiques;

import com.google.gson.annotations.SerializedName;
import com.boka2.gesblue.datamanager.webservices.results.basic.WSResult;
import com.boka2.gesblue.global.Constants;
import com.boka2.gesblue.model.Models;

import java.util.ArrayList;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;

@JSoapClass(namespace = Constants.DADESBA_NAMESPACE)
public class ModelsResponse extends WSResult {

	@JSoapResField(name = "models")
	@SerializedName("models")
	private ArrayList<Models> models;

	public ModelsResponse() {
		super();
	}

	public ArrayList<Models> getModels() {
		return models;
	}

	public void setModels(ArrayList<Models> models) {
		this.models = models;
	}

	@Override
	public String toString() {
		return "ModelsResponse{" +
				"models=" + models +
				'}';
	}

	public String getDefaultValue() {
		if(!models.isEmpty()) {
			for(Models m : models) {
				if(m.isDefaultValue()) {
					return m.getCodi();
				}
			}
		}
		return "";
	}
}