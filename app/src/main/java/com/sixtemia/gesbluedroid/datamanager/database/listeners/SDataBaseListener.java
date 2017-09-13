package com.sixtemia.gesbluedroid.datamanager.database.listeners;

import com.sixtemia.gesbluedroid.datamanager.database.results.BasicDBResult;

public abstract class SDataBaseListener {
	public void onPreExecute() {}
	public void onError(BasicDBResult _result) {}
	public void onCompletion(BasicDBResult _result) {}
}

