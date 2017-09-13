package com.sixtemia.sdatamanager.datamanager.listeners;

import com.sixtemia.sdatamanager.datamanager.results.BasicWSResult;

public interface SDataManagerListener {
	void onError(BasicWSResult _result);
	void onCompletion(BasicWSResult _result);
}
