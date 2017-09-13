package com.sixtemia.gesbluedroid.datamanager.webservices.parameters;

import com.sixtemia.sdatamanager.datamanager.listeners.SDataManagerListener;
import com.sixtemia.sdatamanager.datamanager.parameters.SNameValuePair;
import com.sixtemia.sdatamanager.datamanager.parameters.WSRequestParams;

import java.util.ArrayList;

/**
 * Created by rubengonzalez on 22/8/16.
 */

public class WSGBConfig extends WSRequestParams {
	public WSGBConfig(ArrayList<SNameValuePair> _params, String _urlBase, String _urlWS, String _basicWSClass, SDataManagerListener _listener) {
		super(_params, _urlBase, _urlWS, _basicWSClass, _listener);
	}
}