package com.boka2.sdatamanager.datamanager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.boka2.sdatamanager.R;
import com.boka2.sdatamanager.common.Constants;
import com.boka2.sdatamanager.datamanager.listeners.ConnectionListener;
import com.boka2.sdatamanager.datamanager.listeners.SDataManagerListener;
import com.boka2.sdatamanager.datamanager.parameters.WSRequestParams;
import com.boka2.sdatamanager.datamanager.results.BasicWSResult;
import com.boka2.sutils.classes.SNetworkUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// =========================================
// WebService AsyncTasck Pseudo-Factory Pattern
// =========================================
public class DataManager extends SDataManager {

	public DataManager(Context _context) {
		super(_context);
	}

	//Tota la configuració i objectes necessaris per a la crida s'encapsulen en WSRequestParams (Command Pattern)
	public WebserviceAsyncTask getWSResponse(WSRequestParams _config, ExecutorService _executor) {
		return getWSResponse(_config, _executor, null);
	}

	//Tota la configuració i objectes necessaris per a la crida s'encapsulen en WSRequestParams (Command Pattern)
	public WebserviceAsyncTask getWSResponse(WSRequestParams _config, ExecutorService _executor, @Nullable ConnectionListener _listener) {
		WebserviceAsyncTask taskGetUniversityList;
		taskGetUniversityList = new WebserviceAsyncTask(_listener);
		if (taskGetUniversityList != null) {
			taskGetUniversityList.cancel(true);
		}
		taskGetUniversityList = new WebserviceAsyncTask(_listener);
		ExecutorService executor = _executor;
		if(executor==null) {
			executor = Executors.newSingleThreadExecutor();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			taskGetUniversityList.executeOnExecutor(executor, _config);
		} else {
			taskGetUniversityList.execute(_config);
		}
		return taskGetUniversityList;
	}

	public class WebserviceAsyncTask extends AsyncTask<WSRequestParams, Void, BasicWSResult> {
		private WSRequestParams config = null;
		private boolean error = false;
		private BasicWSResult result;
		private ConnectionListener mListener;

		public WebserviceAsyncTask(@Nullable ConnectionListener mListener) {
			this.mListener = mListener;
		}

		@Override
		protected BasicWSResult doInBackground(WSRequestParams... params) {
			BasicWSResult res = null;
			String errorMsg = "";
			int intSrc = 0;
			if(params.length>0 && params[0]!=null) {
				config = params[0];
				try {
					if (SNetworkUtils.isInternetConnectionAvailable(mContext)) {
						String json = getJSON(config.getParams(), config.getUrlBase(), config.getUrlWS(), config.getHttpMethod(), mListener);
						if (!TextUtils.isEmpty(json)) {
							res = parseJSONResult(json, config.getBasicWSClass(), config.getGson());
						} else {
							error = true;
							intSrc = WSConstants.ERROR_JSON_PARSING;
							errorMsg = mContext.getString(R.string.error_json_parsing);
						}
					} else {
						error = true;
						intSrc = WSConstants.ERROR_NO_CONNECTION;
						errorMsg = mContext.getString(R.string.error_no_connection_available);
					}
				} catch (Exception e) {
					error = true;
					intSrc = WSConstants.ERROR_HANDLED_EXCEPTION;
					errorMsg = e.getMessage();
				}
			}
			if(res==null || error) {
				res = instantiateBasicResult(config.getBasicWSClass());
				res.setStrMsg(errorMsg);
				res.setStrResult(KO);
				res.setIntSource(intSrc);
				Log.d(Constants.LOG_TAG, errorMsg);
			}
			if(res!=null && res.getStrResult().equalsIgnoreCase(KO)) {
				error = true;
			}
			result = res;
			return res;
		}

		@Override
		protected void onPostExecute(BasicWSResult _result) {
			super.onPostExecute(_result);
			if (!isCancelled()) {
				if (error) {
					error(_result, config.getListener());
					return;
				}
				completion(_result, config.getListener());
			}
		}

		public BasicWSResult getResult() {
			return result;
		}
	}

	protected void completion(BasicWSResult result, SDataManagerListener listener) {
		if (listener instanceof SDataManagerListener) {
			listener.onCompletion(result);
		}
	}

	protected void error(BasicWSResult result, SDataManagerListener listener) {
		if (listener instanceof SDataManagerListener) {
			listener.onError(result);
		}
	}

	private BasicWSResult parseJSONResult(String _json, String _model, Gson _gson) {
		BasicWSResult result = instantiateBasicResult(_model);
		String errorMsg="";
		try {
			Gson gson = _gson;
			if(gson==null)gson = new Gson();
			result = gson.fromJson(_json, result.getClass());
		}catch (Exception e) {
			e.printStackTrace();
			errorMsg = e.getMessage();
			result = null;
		}
		if(result==null) {
			result = instantiateBasicResult(_model);
			result.setStrResult(KO);
			result.setStrMsg(errorMsg);
			result.setIntSource(WSConstants.ERROR_JSON_PARSING);
		}
		return result;
	}

	private BasicWSResult instantiateBasicResult(String _modelClassName) {
		Class<?> class_to_instanciate = null;
		Object result = null;
		boolean error = false;
		String errorMsg = "";
		try {
			//Creem una instància per desar el resultat de la classe _model
			class_to_instanciate = Class.forName(_modelClassName);
			Constructor<?> constructor = class_to_instanciate.getConstructor();
			result = constructor.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			error = true;errorMsg =e.getMessage();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			error = true;errorMsg =e.getMessage();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			error = true;errorMsg =e.getMessage();
		} catch (InstantiationException e) {
			e.printStackTrace();
			error = true;errorMsg =e.getMessage();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			error = true;errorMsg =e.getMessage();
		}
		BasicWSResult return_result = (BasicWSResult)result;
		if(error) {
			if(return_result==null)return_result = new BasicWSResult();
			return_result.setStrResult(KO);
			return_result.setStrMsg(errorMsg);
		}
		return return_result;
	}

}
