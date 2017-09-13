package com.sixtemia.gesbluedroid.datamanager.database.results;

import com.sixtemia.gesbluedroid.datamanager.webservices.results.BasicGBResult;
import com.sixtemia.sdatamanager.datamanager.results.BasicWSResult;

import java.util.List;

/**
 * Created by rubengonzalez on 29/8/16.
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