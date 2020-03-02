package com.boka2.spushnotifications.datamanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.boka2.spushnotifications.classes.SNameValuePair;
import com.boka2.sutils.classes.SNetworkUtils;
import com.boka2.sutils.classes.SUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe base per implementar Data Managers. * Crear una subclasse d'aquesta amb els m�todes especifics per cada App.
 * 
 * El constructor de la subclasse ha de fixar el parametre URL_BASE Tambe pot modificar els noms dels parametres OK, KO i tipus d'acci�.
 * 
 * La subclasse pot afegir parametres communs afeint Pairs a l'atribut commonParams. (veure m�tode initBaseCommonParams() per un exemple )
 * 
 * Cada tipus de crida ha de tenir el seu propi listener, no es pot compartir entre accions, ja que al acabar la connexi�
 * el datamanager notifica el listener, i si s'han fer crides diferents l'activity no sap a quina crida correspon.
 * 
 * @author alejandromartinez
 * 
 */
public class SDataManager {
	protected static String TAG = "SDataManager";
	private String PREFS_NAME;

	private final int TIMEOUT = 20000;

	private long CACHE_TIME_VALID = 1000 * 60 * 10; // 10 min
	private boolean CACHE = true;

	protected final String MULTIPART_PREFIX = "SDMultipart-";

	public String URL_BASE = "";
	protected String OK = "OK";
	protected String KO = "KO";

	protected Context mContext;

	protected List<SNameValuePair> commonParams;
	
	// http auth
	private static String user;
	private static String password;

	public enum HttpMethod {
		GET, POST, POST_MULTIPART;
	}

	/*
	 * CONSTRUCTOR
	 */
	public SDataManager(Context context) {
		this.mContext = context;
		PREFS_NAME = mContext.getPackageName();

		initBaseCommonParams();
	}

	/*
	 * GENERAL
	 */
	private void initBaseCommonParams() {
		commonParams = new ArrayList<SNameValuePair>();

		// subclass can do this
		// commonParams.add(new BasicNameValuePair("lang", "ca"));
	}
	
	/**
	 * Configure the future request to user the basic http authentification
	 * @param user
	 * @param password
	 */
	public static void configureHTTPAuthenticator(String user, String password) {
		SDataManager.user = user;
		SDataManager.password = password;
		/*es queda encallat :S
		 * Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				Log.d(TAG, "Setting default authentificator " + u + " " + p);
				return new PasswordAuthentication(u, p.toCharArray());
			}
		});*/
	}
	
	private String getQuery(List<SNameValuePair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (SNameValuePair pair : params)
	    {
			//multipart parameters will be encapsuled later
			if (!pair.getName().startsWith(MULTIPART_PREFIX)) {

				if (first)
					first = false;
				else
					result.append("&");

				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
				result.append("=");
				// result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
				result.append(pair.getValue());
			}
	    }

	    return result.toString();
	}

	/**
	 * @param _params
	 *            Parametres per la petici� POST
	 * @param _urlPath
	 *            Path per afegir al final de la URL_BASE o null si no n'hi ha
	 * @param _method
	 *            GET o POST
	 * @return La resposta de realitzar la connexi� POST amb el servidor per defecte.
	 */
	public String getResponse(List<SNameValuePair> _params,String _urlBase, String _urlPath, HttpMethod _method) {
		String strUrl = _urlBase;
		if (_urlPath != null && _urlBase!=null) {
			strUrl = _urlBase + _urlPath;
		}
		String paramString = "";
		try {
			paramString = getQuery(_params);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpURLConnection connection = null;
		InputStream in = null;
		int http_status;

		if (_method == HttpMethod.GET) {
			if (!strUrl.endsWith("?")) {
				strUrl += "?";
			}
			strUrl += paramString;

			try {
				URL url = new URL(strUrl);

				connection = (HttpURLConnection) url.openConnection();
				connection.setReadTimeout(TIMEOUT);
				connection.setConnectTimeout(TIMEOUT);

				in = connection.getInputStream();

				http_status = connection.getResponseCode();
				if (http_status / 100 != 2) {
					// redirects, server errors, lions and tigers and bears! Oh my!
					//Log.e(TAG, "HTTP STATUS ERROR " + http_status);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (_method == HttpMethod.POST || _method == HttpMethod.POST_MULTIPART) { // POST

			try {
				URL url = new URL(strUrl);

				connection = (HttpURLConnection) url.openConnection(); // this does no network IO.

				// tells HUC that you're going to POST; still no IO.
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setReadTimeout(TIMEOUT);
				connection.setConnectTimeout(TIMEOUT);

				//used when sending multipart params...
				String lineEnd = "\r\n";
				String twoHyphens = "--";
				String boundary = "********";
				if (_method == HttpMethod.POST_MULTIPART) {
					connection.setRequestProperty("Connection", "Keep-Alive");
					connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				}

				if (SDataManager.user != null && SDataManager.password != null) {
					// autentificacio
					String auth = SDataManager.user + ":" + SDataManager.password;
					//Log.d(TAG, "Auth: " + auth);
					connection.setRequestProperty("Authorization", "basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP));
				}

				// TO ADD PARAMS WHEN THERE ARE MULTIPART VALUES
				DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
				if (_method == HttpMethod.POST_MULTIPART) {

					//to get all parameters of multipart type
					ArrayList<SNameValuePair> arrayMultipartParams = getMultipartParameters(_params);

					//to add all the string params: accio, telf, etc
					String[] posts = paramString.split("&");
					int max = posts.length;
					for (int i = 0; i < max; i++) {
						dos.writeBytes(twoHyphens + boundary + lineEnd);
						//Log.d(TAG, twoHyphens + boundary + lineEnd);
						String[] kv = posts[i].split("=");
						dos.writeBytes("Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
						//Log.d(TAG, "Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
						dos.writeBytes("Content-Type: text/plain" + lineEnd);
						//Log.d(TAG, "Content-Type: text/plain"+lineEnd);
						dos.writeBytes(lineEnd);
						//Log.d(TAG, lineEnd);
						dos.writeBytes(kv[1]);
						//Log.d(TAG,kv[1]);
						dos.writeBytes(lineEnd);
						//Log.d(TAG, lineEnd);
					}

					//to add multipart params values
					for (SNameValuePair nvp : arrayMultipartParams) {

						dos.writeBytes(twoHyphens + boundary + lineEnd);
						//Log.d(TAG, twoHyphens + boundary + lineEnd);

						String nameParam = nvp.getName();
						nameParam = nameParam.replace(MULTIPART_PREFIX, "");//el nom del par�metre que espera el server �s sense el prefix
						String valueParam = nvp.getValue();

						String extension = nvp.getValue().substring(nvp.getValue().lastIndexOf(".") + 1, nvp.getValue().length());

						//dos.writeBytes(twoHyphens + boundary + lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\"" + nameParam + "\";filename=\"" + "file." + extension + "\""
								+ lineEnd);
						//Log.d(TAG, "Content-Disposition: form-data; name=\""+nameParam+"\";filename=\"" + "file.png" + "\"" + lineEnd);

						// JAUME: Test per comprovar si es poden pujar imatges com qualsevol altre document. Si aix� funciona no caldr� fer res m�s.
						// Si no funciona haurem de muntar un sistema per diferenciar imatges i altres fitxers
						//dos.writeBytes("Content-Type: image/jpeg" + lineEnd);
						//Log.d(TAG, "Content-Type: image/jpeg" + lineEnd);
						dos.writeBytes("Content-Type: multipart/form-data" + lineEnd);
						//Log.d(TAG, "Content-Type: multipart/form-data" + lineEnd);

						dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
						//Log.d(TAG, "Content-Transfer-Encoding: binary" + lineEnd);

						dos.writeBytes(lineEnd);
						//Log.d(TAG, lineEnd);

						FileInputStream fileInputStream = new FileInputStream(valueParam);

						int bytesAvailable = fileInputStream.available();
						int maxBufferSize = 1 * 1024 * 1024;
						int bufferSize = Math.min(bytesAvailable, maxBufferSize);
						byte[] buffer = new byte[bufferSize];

						// read file and write it into form...
						int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
						while (bytesRead > 0) {
							dos.write(buffer, 0, bufferSize);
							bytesAvailable = fileInputStream.available();
							bufferSize = Math.min(bytesAvailable, maxBufferSize);
							bytesRead = fileInputStream.read(buffer, 0, bufferSize);
							/*
							 * dos.writeBytes(lineEnd); dos.writeBytes(twoHyphens + boundary
							 * + twoHyphens + lineEnd);
							 */
						}

						//Log.d(TAG, "IMATGE");

						// send multipart form data necesssary after file data...
						dos.writeBytes(lineEnd);
						//Log.d(TAG, lineEnd);

						dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
						//Log.d(TAG, twoHyphens + boundary + twoHyphens + lineEnd);

						fileInputStream.close();

					}

					// TO ADD PARAMS WHEN THERE ARE NO MULTIPART VALUES
				} else {

					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dos, "UTF-8"));
					//Log.d(TAG, "---->"+paramString);
					writer.write(paramString);
					writer.close();
				}


				if (connection != null) {
					try {
						http_status = connection.getResponseCode();
						if (http_status / 100 != 2) {
							// redirects, server errors, lions and tigers and bears! Oh my!
							//Log.e(TAG, "HTTP STATUS ERROR " + http_status);
						}
					}catch(SocketTimeoutException | FileNotFoundException e) {
						Log.e(TAG, "Error:" + e.getMessage());
					}
					in = connection.getInputStream();
				}
				dos.flush();
				dos.close();

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
			//Log.e(TAG, "ERROR: HttpMethod no conocido. Debe usar HttpMethod.GET o HttpMethod.POST");
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
			//Log.d(TAG, str);
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
	
	
	public ArrayList<SNameValuePair> getMultipartParameters(List<SNameValuePair> params) {

		ArrayList<SNameValuePair> arrayToReturn = new ArrayList<SNameValuePair>();

		for (SNameValuePair nvp : params) {
			String name = nvp.getName();
			if (name.startsWith(MULTIPART_PREFIX)) {
				arrayToReturn.add(nvp);
			}
		}

		return arrayToReturn;
	}
	

		/**
	 * @param params
	 *            Parametres per la petici� POST
	 * @param method
	 *            GET o POST
	 * @return Un string amb la resposta al realitzar la connexi� amb el servidor.
	 */
	protected String getJSON(List<SNameValuePair> params, HttpMethod method) {
		return getJSON(params,URL_BASE,null,method);
	}

	/**
	 * @param params
	 *            Parametres per la petici� POST
	 * @param method
	 *            GET o POST
	 * @return Un string amb la resposta al realitzar la connexi� amb el servidor.
	 */
	protected String getJSON(List<SNameValuePair> params, String _urlBase, String urlPath, HttpMethod method) {
		return getJSON(params, _urlBase, urlPath, method, true);
	}

	/**
	 * @param params
	 *            Parametres per la petici�
	 * @param urlPath
	 *            Path per afegir al final de la URL_BASE o null si no n'hi ha
	 * @param method
	 *            GET o POST
	 * @return Un string amb la resposta al realitzar la connexi� amb el servidor.
	 */
	protected String getJSON(List<SNameValuePair> params, String _urlBase, String urlPath, HttpMethod method, boolean _useCache) {
		try {
			String str = "";
			String key = createUrlForCache(params, urlPath);

			if (SNetworkUtils.isInternetConnectionAvailable(mContext) && (!isCacheValid(key) || !_useCache)) {
				str = getResponse(params, _urlBase, urlPath, method);

				// Save to cache
				if (CACHE) {
					saveCache(key, str);
					saveCachedTime(key, new Date().getTime());
				}

			} else if (CACHE) {
				// Read from cache
				str = readCache(key);
				if (str != null) {
					//Log.d(TAG, "Hi ha cache!" + str);
				} else {
					//Log.d(TAG, "No hi ha cache!");
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

		if (CACHE) {
			if (cacheKey != null) {
				removeCache(cacheKey);
			}
		}

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
	
	/**
	 * 
	 * @param params
	 * @param urlPath
	 * @return Crea un string format per la url i els parameters que es pot fer servir per identificar la petici�.
	 */
	public String createUrlForCache(List<SNameValuePair> params, String urlPath) {
		String res = URL_BASE;
		if (urlPath != null) {
			res += urlPath;
		}
		if (!res.endsWith("?")) {
			res += "?";
		}
		for (SNameValuePair nameValuePair : params) {
			res += nameValuePair.getName() + "=" + nameValuePair.getValue() + "&";
		}
		res = SUtils.getMD5(res);
		return res;
	}

	/**
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
			//Log.d(TAG, "File saved " + outputFile);
			writer.close();
		} catch (IOException e) {
			//Log.e(TAG, e.getMessage(), e);
		}
	}
	
	private void removeCache(String key) {
		File dir = baseCacheDir();
		String fileName = key;
		
		File file = new File(dir, fileName);
		file.delete();
		
		removeCachedTime(key);
	}

	private String readCache(String key) {
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
			//Log.e(TAG, e.getMessage(), e);
		}
		return res;
	}
}
