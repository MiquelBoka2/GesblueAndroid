package com.sixtemia.gesbluedroid.datamanager.database.helpers;

import android.content.Context;

import com.sixtemia.gesbluedroid.datamanager.database.executors.OperationExecutorHelper;
import com.sixtemia.gesbluedroid.datamanager.database.model.Model_TipusVehicle;
import com.sixtemia.gesbluedroid.datamanager.database.results.BasicDBResult;

/**
 * Created by rubengonzalez on 8/9/16.
 */

public class TipusVehicleHelper extends SimpleHelper {
	@Override
	public Class getModel() {
		return Model_TipusVehicle.class;
	}

	public OperationExecutorHelper getAllGroupById(Context c) {
		return new getAllGroupById(c);
	}

	public class getAllGroupById extends OperationExecutorHelper {
		public getAllGroupById(Context _context) {
			super(_context);
		}

		@Override
		public BasicDBResult processingMethod() {
			return getAllGroupBy(DBhelper, "coditipusvehicle");
		}
	}
}