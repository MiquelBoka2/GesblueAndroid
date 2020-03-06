package com.boka2.gesblue.datamanager.database.results;

import com.boka2.gesblue.datamanager.webservices.results.BasicGBResult;
import com.boka2.sdatamanager.datamanager.results.BasicWSResult;

import java.util.List;

/*
 * Created by Boka2.
 */

public class SimpleResult extends BasicWSResult {
	private List<BasicGBResult> items;
	private int affectedItems;

	public SimpleResult(List<BasicGBResult> items) {
		super();
		this.items = items;
		this.affectedItems = 0;
	}

	public SimpleResult(int affectedItems) {
		this.affectedItems = affectedItems;
	}

	public List<BasicGBResult> getItems() {
		return items;
	}

	public int getAffectedItems() {
		return affectedItems;
	}
}