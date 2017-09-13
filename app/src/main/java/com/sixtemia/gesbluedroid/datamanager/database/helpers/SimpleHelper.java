package com.sixtemia.gesbluedroid.datamanager.database.helpers;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import com.sixtemia.gesbluedroid.datamanager.database.executors.OperationExecutorHelper;
import com.sixtemia.gesbluedroid.datamanager.database.results.BasicDBResult;
import com.sixtemia.gesbluedroid.datamanager.database.results.SimpleResult;
import com.sixtemia.gesbluedroid.datamanager.webservices.results.BasicGBResult;
import com.sixtemia.sdatamanager.datamanager.results.BasicWSResult;

import java.util.List;

import static com.sixtemia.gesbluedroid.datamanager.database.results.BasicDBResult.KO;

/**
 * Created by rubengonzalez on 29/8/16.
 */

public abstract class SimpleHelper {
	public static final String TAG = "SimpleHelper";
	public static final String ERROR_MSG = "Error in Operation Executor! Method:";

	protected class doCreate extends OperationExecutorHelper {
		private List<BasicGBResult> items;

		public doCreate(Context _context, List<BasicGBResult> items) {
			super(_context);
			this.items = items;
		}

		@Override
		public BasicDBResult processingMethod() {
			return create(DBhelper, items);
		}
	}

	protected class getAll extends OperationExecutorHelper {
		public getAll(Context _context) {
			super(_context);
		}

		@Override
		public BasicDBResult processingMethod() {
			return getAll(DBhelper);
		}
	}

	protected class getAllIn extends OperationExecutorHelper {
		private String field;
		private Object[] values;

		public getAllIn(Context _context, String field, Object[] values) {
			super(_context);
			this.field = field;
			this.values = values;
		}

		@Override
		public BasicDBResult processingMethod() {
			return getAllIn(DBhelper, field, values);
		}
	}

	protected class getField extends OperationExecutorHelper {
		private String field;
		private Object value;

		public getField(Context _context, String field, Object value) {
			super(_context);
			this.field = field;
			this.value = value;
		}

		@Override
		public BasicDBResult processingMethod() {
			return getField(DBhelper, field, value);
		}
	}

	protected class update extends OperationExecutorHelper {
		private String whereField;
		private String whereValue;
		private String field;
		private Object value;

		public update(Context _context, String whereField, String whereValue, String field, Object value) {
			super(_context);
			this.whereField = whereField;
			this.whereValue = whereValue;
			this.field = field;
			this.value = value;
		}

		@Override
		public BasicDBResult processingMethod() {
			return update(DBhelper, whereField, whereValue, field, value);
		}
	}

	protected class getFieldsOr extends OperationExecutorHelper {
		private String[] field;
		private Object[] value;

		public getFieldsOr(Context _context, String[] field, Object[] value) {
			super(_context);
			this.field = field;
			this.value = value;
		}

		@Override
		public BasicDBResult processingMethod() {
			return getFieldsOr(DBhelper, field, value);
		}
	}

	protected class getFields extends OperationExecutorHelper {
		private String[] field;
		private Object[] value;

		public getFields(Context _context, String[] field, Object[] value) {
			super(_context);
			this.field = field;
			this.value = value;
		}

		@Override
		public BasicDBResult processingMethod() {
			return getFields(DBhelper, field, value);
		}
	}

	protected class deleteAll extends OperationExecutorHelper {
		public deleteAll(Context _context) {
			super(_context);
		}

		@Override
		public BasicDBResult processingMethod() {
			clean(DBhelper);
			return null;
		}
	}

	protected class deleteWhere extends OperationExecutorHelper {
		private String[] field;
		private Object[] value;

		public deleteWhere(Context _context, String[] field, Object[] value) {
			super(_context);
			this.field = field;
			this.value = value;
		}

		public deleteWhere(Context _context, String field, Object value) {
			super(_context);
			this.field = new String[] { field };
			this.value = new Object[] { value };
		}

		@Override
		public BasicDBResult processingMethod() {
			return deleteWhere(DBhelper, field, value);
		}
	}

	protected class delete extends OperationExecutorHelper {
		private Object object;

		public delete(Context _context, Object object) {
			super(_context);
			this.object = object;
		}

		@Override
		public BasicDBResult processingMethod() {
			return delete(DBhelper, object);
		}
	}

	public OperationExecutorHelper deleteAll(Context c) {
		return new deleteAll(c);
	}

	public OperationExecutorHelper deleteWhere(Context c, String s, Object o) {
		return new deleteWhere(c, s, o);
	}

	public OperationExecutorHelper deleteWhere(Context c, String[] s, Object[] o) {
		return new deleteWhere(c, s, o);
	}

	public OperationExecutorHelper getAll(Context c) {
		return new getAll(c);
	}

	public OperationExecutorHelper create(Context c, List items) {
		return new doCreate(c, items);
	}

	public OperationExecutorHelper update(Context c, String whereField, String whereValue, String field, Object value) {
		return new update(c, whereField, whereValue, field, value);
	}

	public OperationExecutorHelper delete(Context c, Object value) {
		return new delete(c, value);
	}

	public OperationExecutorHelper getField(Context c, String field, Object value) {
		return new getField(c, field, value);
	}

	public OperationExecutorHelper getFields(Context c, String[] fields, Object[] values) {
		return new getFields(c, fields, values);
	}

	public OperationExecutorHelper getFieldsOr(Context c, String[] fields, Object[] values) {
		return new getFieldsOr(c, fields, values);
	}

	public OperationExecutorHelper getFieldsOr(Context c, String field, Object[] values) {
		String[] fields = new String[values.length];
		for(int i = 0; i < values.length; i++) {
			fields[i] = field;
		}
		return getFieldsOr(c, fields, values);
	}

	public abstract Class getModel();
	public BasicWSResult getResult(List<BasicGBResult> items) {
		return new SimpleResult(items);
	}
	public BasicWSResult getResult(int affectedItems) {
		return new SimpleResult(affectedItems);
	}

	public String getTag() {
		return getModel().getSimpleName() + "_Helper";
	}

	//Operacions

	protected BasicDBResult create(DatabaseModelHelper _DBhelper, List<BasicGBResult> items) {
		BasicDBResult dbResult = new BasicDBResult();
		boolean ok = true;

		try {
			if(items != null) {
				Log.i(getTag(), "Creating: " + items.size() + " items");
				for(Object item : items) {
					Dao.CreateOrUpdateStatus status = (_DBhelper.getModelDao(getModel())).createOrUpdate(item);
					if(!(status.isCreated() || status.isUpdated())) {
						ok = false;
						Log.e(getTag(), "Error creating: " + item);
					}
				}
				Log.i(getTag(), "Done");
			} else {
				Log.e(TAG, "No items");
				ok = false;
			}
			dbResult.setStrMsg(ok ? "" : ERROR_MSG + "create");
		} catch (Exception ex) {
			Log.e(getTag(), "Error: " + ex.getLocalizedMessage(), ex);
			ok = false;
			dbResult.setStrMsg(ex.getMessage());
		}

		dbResult.setStrResult(ok ? BasicDBResult.OK : KO);
		return dbResult;
	}

	public BasicDBResult getAllIn(DatabaseModelHelper _DBhelper, String field, Object[] values) {
		String[] fields = new String[values.length];
		for(int i = 0; i < fields.length; i++) {
			fields[i] = field;
		}
		return getFieldsOr(_DBhelper, fields, values);
	}

	public BasicDBResult getAll(DatabaseModelHelper _DBhelper) {
		BasicDBResult result = new BasicDBResult();

		try {
			List<BasicGBResult> items = _DBhelper.getModelDao(getModel()).queryForAll();
			result.setResultObject(getResult(items));
		} catch (Exception ex) {
			Log.e(TAG, ERROR_MSG + ex.getLocalizedMessage(), ex);
			result.setStrResult(KO);
			result.setStrMsg(ex.getMessage());
		}

		return result;
	}

	public BasicDBResult getAllGroupBy(DatabaseModelHelper _DBhelper, String column) {
		BasicDBResult result = new BasicDBResult();

		try {
			QueryBuilder builder = _DBhelper.getModelDao(getModel()).queryBuilder();
			builder.groupBy(column);
			List<BasicGBResult> items = builder.query();
			result.setResultObject(getResult(items));
		} catch (Exception ex) {
			Log.e(TAG, ERROR_MSG + ex.getLocalizedMessage(), ex);
			result.setStrResult(KO);
			result.setStrMsg(ex.getMessage());
		}

		return result;

	}

	public BasicDBResult update(DatabaseModelHelper _DBhelper, String whereField, Object whereValue, String field, Object value) {
		BasicDBResult result = new BasicDBResult();

		try {
			Class c = getModel();
			UpdateBuilder<BasicGBResult, Integer> builder = _DBhelper.getModelDao(getModel()).updateBuilder();
			builder.where().eq(whereField, whereValue);
			builder.updateColumnValue(field, value);
			int updated = builder.update();
			result.setResultObject(getResult(updated));
		} catch (Exception ex) {
			Log.e(TAG, ERROR_MSG + ex.getLocalizedMessage(), ex);
			result.setStrResult(KO);
			result.setStrMsg(ex.getMessage());
		}

		return result;
	}

	public BasicDBResult getField(DatabaseModelHelper _DBhelper, String field, Object value) {
		BasicDBResult result = new BasicDBResult();

		try {
			List<BasicGBResult> items = _DBhelper.getModelDao(getModel()).queryForEq(field, value);
			result.setResultObject(getResult(items));
		} catch (Exception ex) {
			Log.e(TAG, ERROR_MSG + ex.getLocalizedMessage(), ex);
			result.setStrResult(KO);
			result.setStrMsg(ex.getMessage());
		}

		return result;
	}

	public BasicDBResult delete(DatabaseModelHelper _DBhelper, Object value) {
		BasicDBResult result = new BasicDBResult();

		try {
			int affected = _DBhelper.getModelDao(getModel()).delete(value);
			result.setResultObject(getResult(affected));
		} catch (Exception ex) {
			Log.e(TAG, ERROR_MSG + ex.getLocalizedMessage(), ex);
			result.setStrResult(KO);
			result.setStrMsg(ex.getMessage());
		}

		return result;
	}

	public BasicDBResult deleteWhere(DatabaseModelHelper dBHelper, String[] fields, Object[] values) {
		BasicDBResult result = new BasicDBResult();

		try {
			DeleteBuilder builder = dBHelper.getModelDao(getModel()).deleteBuilder();
			Where where = builder.where();
			for(int i = 0; i < fields.length; i++) {
				if(i > 0) {
					where.and();
				}
				where.eq(fields[i], values[i]);
			}
			int rows = builder.delete();
			result.setResultObject(getResult(rows));
		} catch (Exception ex) {
			Log.e(TAG, ERROR_MSG + ex.getLocalizedMessage(), ex);
			result.setStrResult(KO);
			result.setStrMsg(ex.getMessage());
		}

		return result;
	}

	public BasicDBResult getFields(DatabaseModelHelper _DBhelper, String[] fields, Object[] values) {
		BasicDBResult result = new BasicDBResult();

		try {
			QueryBuilder builder = _DBhelper.getModelDao(getModel()).queryBuilder();
			Where where = builder.where();
			for(int i = 0; i < fields.length; i++) {
				if(i > 0) {
					where.and();
				}
				where.eq(fields[i], values[i]);
			}
			List<BasicGBResult> items = where.query();
			result.setResultObject(getResult(items));
		} catch (Exception ex) {
			Log.e(TAG, ERROR_MSG + ex.getLocalizedMessage(), ex);
			result.setStrResult(KO);
			result.setStrMsg(ex.getMessage());
		}

		return result;
	}

	public BasicDBResult getFieldsOr(DatabaseModelHelper _DBhelper, String[] fields, Object[] values) {
		BasicDBResult result = new BasicDBResult();

		try {
			QueryBuilder builder = _DBhelper.getModelDao(getModel()).queryBuilder();
			Where where = builder.where();
			for(int i = 0; i < fields.length; i++) {
				if(i > 0) {
					where.or();
				}
				where.eq(fields[i], values[i]);
			}
			List<BasicGBResult> items = where.query();
			result.setResultObject(getResult(items));
		} catch (Exception ex) {
			Log.e(TAG, ERROR_MSG + ex.getLocalizedMessage(), ex);
			result.setStrResult(KO);
			result.setStrMsg(ex.getMessage());
		}

		return result;
	}

	public void clean(DatabaseModelHelper _DBhelper) {
		try {
			TableUtils.clearTable(_DBhelper.getConnectionSource(), getModel());
		} catch (Exception ex) {
			Log.e(TAG, ERROR_MSG + ex.getLocalizedMessage(), ex);
		}
	}
}