package com.boka2.gesblue.datamanager.database.helpers;

import android.content.Context;

import com.boka2.gesblue.GesblueApplication;
import com.boka2.gesblue.datamanager.database.executors.OperationExecutorHelper;
import com.boka2.gesblue.datamanager.database.model.Model_Carrer;
import com.boka2.gesblue.datamanager.database.results.BasicDBResult;
import com.boka2.gesblue.global.PreferencesGesblue;

/**
 * Created by Boka2.
 */

public class CarrerHelper extends SimpleHelper {
	@Override
	public Class getModel() {
		return Model_Carrer.class;
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
			return getAllGroupBy(DBhelper, "codicarrer");
		}
	}

	public class getAllFromZone extends OperationExecutorHelper {
		public getAllFromZone(Context _context) {
			super(_context);
		}

		@Override
		public BasicDBResult processingMethod() {
			Context _context = GesblueApplication.getContext();
			return getField(DBhelper,"zona", PreferencesGesblue.getCodiZona(_context));
		}
	}
}