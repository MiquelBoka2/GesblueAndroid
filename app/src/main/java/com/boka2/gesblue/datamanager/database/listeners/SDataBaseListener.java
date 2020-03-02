package com.boka2.gesblue.datamanager.database.listeners;

import com.boka2.gesblue.datamanager.database.results.BasicDBResult;

public abstract class SDataBaseListener {
	public void onPreExecute() {}
	public void onError(BasicDBResult _result) {}
	public void onCompletion(BasicDBResult _result) {}
}

