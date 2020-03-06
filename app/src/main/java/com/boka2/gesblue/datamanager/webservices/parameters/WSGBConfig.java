package com.boka2.gesblue.datamanager.webservices.parameters;

import com.boka2.sdatamanager.datamanager.listeners.SDataManagerListener;
import com.boka2.sdatamanager.datamanager.parameters.SNameValuePair;
import com.boka2.sdatamanager.datamanager.parameters.WSRequestParams;

import java.util.ArrayList;

/*
 * Created by Boka2.
 */

public class WSGBConfig extends WSRequestParams {
	public WSGBConfig(ArrayList<SNameValuePair> _params, String _urlBase, String _urlWS, String _basicWSClass, SDataManagerListener _listener) {
		super(_params, _urlBase, _urlWS, _basicWSClass, _listener);
	}
}