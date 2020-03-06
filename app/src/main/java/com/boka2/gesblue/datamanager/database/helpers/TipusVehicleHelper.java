package com.boka2.gesblue.datamanager.database.helpers;

import android.content.Context;

import com.boka2.gesblue.datamanager.database.executors.OperationExecutorHelper;
import com.boka2.gesblue.datamanager.database.model.Model_TipusVehicle;
import com.boka2.gesblue.datamanager.database.results.BasicDBResult;

/*
 * Created by Boka2.
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