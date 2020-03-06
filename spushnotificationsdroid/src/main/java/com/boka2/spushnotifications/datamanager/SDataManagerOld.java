package com.boka2.spushnotifications.datamanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.boka2.sutils.classes.SNetworkUtils;
import com.boka2.sutils.classes.SUtils;

import org.apache.http.NameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Classe base per implementar Data Managers. * Crear una subclasse d'aquesta amb els m�todes especifics per cada App.
 * 
 * El constructor de la subclasse ha de fixar el parametre URL_BASE Tambe pot modificar els noms dels parametres OK, KO i tipus d'acci�.
 * 
 * La subclasse pot afegir parametres communs afeint Pairs a l'atribut commonParams. (veure m�tode initBaseCommonParams() per un exemple )
 * 
 * @author alejandromartinez
 * 
 */
public class SDataManagerOld {
	protected static String TAG = "SDataManager";
	private String PREFS_NAME;

	private long CACHE_TIME_VALID = 1000 * 60 * 0;
	private boolean CACHE = true;

	public String URL_BASE = "";
	protected String PARAM_ACCIO = "accio";
	protected String OK = "OK";
	protected String KO = "KO";

	protected Context mContext;

	protected List<NameValuePair> commonParams;

	// http auth
	private static String user;
	private static String password;

	public enum HttpMethod {
		GET, POST;
	}

	/*
	 * CONSTRUCTOR
	 */
	public SDataManagerOld(Context context) {
		this.mContext = context;
		PREFS_NAME = mContext.getPackageName();

		initBaseCommonParams();
	}

	/*
	 * GENERAL
	 */
	private void initBaseCommonParams() {
		commonParams = new ArrayList<NameValuePair>();

		// subclass can do this
		// commonParams.add(new BasicNameValuePair("lang", "ca"));
	}

	/*
	 * Configure the future request to user the basic http authentification
	 * @param user
	 * @param password
	 */
	public static void configureHTTPAuthenticator(String user, String password) {
		SDataManagerOld.user = user;
		SDataManagerOld.password = password;
		/*es queda encallat :S
		 * Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				Log.d(TAG, "Setting default authentificator " + u + " " + p);
				return new PasswordAuthentication(u, p.toCharArray());
			}
		});*/
	}

	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	/*
	 * @param params
	 *            Parametres per la petici� POST
	 * @param urlPath
	 *            Path per afegir al final de la URL_BASE o null si no n'hi ha
	 * @param method
	 *            GET o POST
	 * @return La resposta de realitzar la connexi� POST amb el servidor per defecte.
	 */
	private String getResponse(List<NameValuePair> params, String urlPath, HttpMethod method) {
		String strUrl = URL_BASE;
		if (urlPath != null) {
			strUrl = strUrl + urlPath;
		}
		String paramString = "";
		try {
			paramString = getQuery(params);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpURLConnection connection = null;
		InputStream in = null;
		int http_status;

		if (method == HttpMethod.GET) {
			if (!strUrl.endsWith("?")) {
				strUrl += "?";
			}
			strUrl += paramString;

			try {
				URL url = new URL(strUrl);

				connection = (HttpURLConnection) url.openConnection();
				in = connection.getInputStream();

				http_status = connection.getResponseCode();
				if (http_status / 100 != 2) {
					// redirects, server errors, lions and tigers and bears! Oh my!
					Log.e(TAG, "HTTP STATUS ERROR " + http_status);
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (method == HttpMethod.POST) { // POST

			try {
				URL url = new URL(strUrl);

				connection = (HttpURLConnection) url.openConnection(); // this does no network IO.

				// tells HUC that you're going to POST; still no IO.
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				//connection.setFixedLengthStreamingMode(payload.length); // still no IO

				if (SDataManagerOld.user != null && SDataManagerOld.password != null) {
					// autentificacio
					String auth = SDataManagerOld.user + ":" + SDataManagerOld.password;
					Log.d(TAG, "Auth: " + auth);
					connection.setRequestProperty("Authorization", "basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP));
				}

				OutputStream out;
				out = connection.getOutputStream();

				// now we can send the body
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
				writer.write(paramString);
				writer.close();
				out.close();

				http_status = connection.getResponseCode();
				if (http_status / 100 != 2) {
					// redirects, server errors, lions and tigers and bears! Oh my!
					Log.e(TAG, "HTTP STATUS ERROR " + http_status);
				}

				in = connection.getInputStream();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			/*
			 * HttpPost post = new HttpPost(strUrl); try { post.setEntity(new UrlEncodedFormEntity(params)); } catch (UnsupportedEncodingException e)
			 * { e.printStackTrace(); } request = post;
			 */
		} else {
			Log.e(TAG, "ERROR: HttpMethod no conocido. Debe usar HttpMethod.GET o HttpMethod.POST");
		}

		// Read the response
		String str = "";
		InputStream stream = new BufferedInputStream(in);
		try {
			// Convert response to string
			/*
			 * boolean debug = false; if (stream.markSupported() && debug) { stream.mark(100); // ???
			 */
			BufferedReader r = new BufferedReader(new InputStreamReader(stream));
			StringBuilder out = new StringBuilder();
			String line;

			while ((line = r.readLine()) != null) {
				out.append(line);
			}
			str = out.toString();
			Log.d(TAG, str);
			/*
			 * stream.reset(); }
			 */

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}

		return str;
	}

	/*
	 * @param params
	 *            Parametres per la petici� POST
	 * @param strUrlBase
	 *            Url base
	 * @param urlPath
	 *            Path per afegir al final de la URL_BASE o null si no n'hi ha
	 * @param method
	 *            GET o POST
	 * @return La resposta de realitzar la connexi� POST amb el servidor per defecte.
	 */
	protected String getResponse(List<NameValuePair> params, String strUrlBase, String urlPath, HttpMethod method) {
		String strUrl = strUrlBase;
		if (urlPath != null) {
			strUrl = strUrl + urlPath;
		}
		String paramString = "";
		try {
			paramString = getQuery(params);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpURLConnection connection = null;
		InputStream in = null;
		int http_status;

		if (method == HttpMethod.GET) {
			if (!strUrl.endsWith("?")) {
				strUrl += "?";
			}
			strUrl += paramString;

			try {
				URL url = new URL(strUrl);

				connection = (HttpURLConnection) url.openConnection();
				in = connection.getInputStream();

				http_status = connection.getResponseCode();
				if (http_status / 100 != 2) {
					// redirects, server errors, lions and tigers and bears! Oh my!
					Log.e(TAG, "HTTP STATUS ERROR " + http_status);
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (method == HttpMethod.POST) { // POST

			try {
				URL url = new URL(strUrl);

				connection = (HttpURLConnection) url.openConnection(); // this does no network IO.

				// tells HUC that you're going to POST; still no IO.
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				//connection.setFixedLengthStreamingMode(payload.length); // still no IO

				if (SDataManagerOld.user != null && SDataManagerOld.password != null) {
					// autentificacio
					String auth = SDataManagerOld.user + ":" + SDataManagerOld.password;
					Log.d(TAG, "Auth: " + auth);
					connection.setRequestProperty("Authorization", "basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP));
				}

				OutputStream out;
				out = connection.getOutputStream();

				// now we can send the body
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
				writer.write(paramString);
				writer.close();
				out.close();

				http_status = connection.getResponseCode();
				if (http_status / 100 != 2) {
					// redirects, server errors, lions and tigers and bears! Oh my!
					Log.e(TAG, "HTTP STATUS ERROR " + http_status);
				}

				in = connection.getInputStream();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			/*
			 * HttpPost post = new HttpPost(strUrl); try { post.setEntity(new UrlEncodedFormEntity(params)); } catch (UnsupportedEncodingException e)
			 * { e.printStackTrace(); } request = post;
			 */
		} else {
			Log.e(TAG, "ERROR: HttpMethod no conocido. Debe usar HttpMethod.GET o HttpMethod.POST");
		}

		// Read the response
		String str = "";
		InputStream stream = new BufferedInputStream(in);
		try {
			// Convert response to string
			/*
			 * boolean debug = false; if (stream.markSupported() && debug) { stream.mark(100); // ???
			 */
			BufferedReader r = new BufferedReader(new InputStreamReader(stream));
			StringBuilder out = new StringBuilder();
			String line;

			while ((line = r.readLine()) != null) {
				out.append(line);
			}
			str = out.toString();
			Log.d(TAG, str);
			/*
			 * stream.reset(); }
			 */

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}

		return str;
	}

	/*
	 * @param params
	 *            Parametres per la petici� POST
	 * @param method
	 *            GET o POST
	 * @return Un string amb la resposta al realitzar la connexi� amb el servidor.
	 */
	protected String getJSON(List<NameValuePair> params, HttpMethod method) {
		return getJSON(params, null, method);
	}

	protected String getJSON(List<NameValuePair> params, String urlPath, HttpMethod method) {
		return getJSON(params, urlPath, method, true);
	}

	/*
	 * @param params
	 *            Parametres per la petici�
	 * @param urlPath
	 *            Path per afegir al final de la URL_BASE o null si no n'hi ha
	 * @param method
	 *            GET o POST
	 * @return Un string amb la resposta al realitzar la connexi� amb el servidor.
	 */
	protected String getJSON(List<NameValuePair> params, String urlPath, HttpMethod method, boolean _useCache) {
		try {
			String str = "";
			String key = createUrlForCache(params, urlPath);

			if (SNetworkUtils.isInternetConnectionAvailable(mContext) && !isCacheValid(key)) {

				str = getResponse(params, urlPath, method);

				// Save to cache
				if (CACHE) {
					saveCache(key, str);
					saveCachedTime(key, new Date().getTime());
				}

			} else if (CACHE) {
				// Read from cache
				str = readCache(key);
				if (str != null) {
					Log.d(TAG, "Hi ha cache!" + str);
				} else {
					Log.d(TAG, "No hi ha cache!");
				}
			}

			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void completion(Object result, SDataManagerListener listener) {
		if (listener instanceof SDataManagerListener) {
			listener.onCompletion(result);
		}
	}

	protected void error(Object result, SDataManagerListener listener, String cacheKey) {

		String key = cacheKey;
		removeCache(key);

		if (listener instanceof SDataManagerListener) {
			listener.onError(result);
		}
	}

	public void cancelAll() {
		/*
		 * if (taskCategoriesPrincipals != null) { taskCategoriesPrincipals.cancel(true); }
		 */
	}

	// ////////////////////////////////////////////
	// CACHE
	// ///////////////////////////////////////////
	private boolean isCacheValid(String key) {
		if (!CACHE)
			return false;

		long timestamp = readCachedTime(key);
		long current = new Date().getTime();
		long diff = (current - timestamp);
		boolean res = diff <= CACHE_TIME_VALID;
		return res;
	}

	private File baseCacheDir() {
		return mContext.getCacheDir();
	}

	private void saveCachedTime(String key, long timestamp) {
		SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, timestamp);
		editor.commit();
	}

	private void removeCachedTime(String key) {
		SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
	}

	private long readCachedTime(String key) {
		SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
		long timestamp = settings.getLong(key, 0);
		return timestamp;
	}

	/*
	 * 
	 * @param params
	 * @param urlPath
	 * @return Crea un string format per la url i els parameters que es pot fer servir per identificar la petici�.
	 */
	protected String createUrlForCache(List<NameValuePair> params, String urlPath) {
		String res = URL_BASE;
		if (urlPath != null) {
			res += urlPath;
		}
		if (!res.endsWith("?")) {
			res += "?";
		}
		for (NameValuePair nameValuePair : params) {
			res += nameValuePair.getName() + "=" + nameValuePair.getValue() + "&";
		}
		res = SUtils.getMD5(res);
		return res;
	}

	/*
	 * Guarda a la cache. Actualment es guarda un fitxer a la memoria externa del dispositiu.
	 * 
	 * @param key
	 *            - clau que es fa servir per gaurdar al cache
	 * @param value
	 *            - valor que es gaurda en cache
	 */
	private void saveCache(String key, String value) {
		File outDir = baseCacheDir();
		String fileName = key;
		Writer writer;

		if (!outDir.isDirectory()) {
			outDir.mkdir();
		}
		try {
			if (!outDir.isDirectory()) {
				throw new IOException("Unable to create directory. Maybe the SD card is mounted?");
			}
			File outputFile = new File(outDir, fileName);
			writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write(value);
			Log.d(TAG, "File saved " + outputFile);
			writer.close();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void removeCache(String key) {
		File dir = baseCacheDir();
		String fileName = key;

		File file = new File(dir, fileName);
		file.delete();

		removeCachedTime(key);
	}

	protected String readCache(String key) {
		String res = null;
		File inDir = baseCacheDir();
		String fileName = key;

		try {
			if (!inDir.isDirectory()) {
				throw new IOException("Unable to read directory. Maybe the SD card is mounted?");
			}
			File inputFile = new File(inDir, fileName);
			FileInputStream fis = new FileInputStream(inputFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffreader = new BufferedReader(isr);
			String readString = buffreader.readLine();
			String content = "";
			while (readString != null) {
				content = content + readString;
				readString = buffreader.readLine();
			}
			isr.close();

			if (!content.equalsIgnoreCase("")) {
				res = content;
			}

		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return res;
	}
}
