package com.boka2.sdatamanager.datamanager.listeners;

import com.boka2.sdatamanager.datamanager.results.BasicWSResult;

public interface SDataManagerListener {
	void onError(BasicWSResult _result);
	void onCompletion(BasicWSResult _result);
}
