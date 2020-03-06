package com.boka2.sdatamanager.datamanager;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.boka2.sdatamanager.datamanager.listeners.ConnectionListener;
import com.boka2.sdatamanager.datamanager.parameters.SNameValuePair;
import com.boka2.sdatamanager.datamanager.security.SSecurityManager;
import com.boka2.sutils.classes.SNetworkUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SDataManager {
	protected static String LOG_TAG = "SDataManager";

	public int TIMEOUT = 30000;

	public static final String MULTIPART_PREFIX = "SDMultipart-";

	public String URL_BASE = "";
	public final static String OK = "OK";
	public final static String KO = "KO";

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
		initBaseCommonParams();
	}

	/*
	 * GENERAL
	 */
	private void initBaseCommonParams() {
		commonParams = new ArrayList<SNameValuePair>();
	}

	/*
	 * @param params Llista de SNameValuePair que es volen concatenar
	 * @return retorna un string en UTF-8 amb la llista de SNameValuePair que s'hagi passat.
	 * @throws UnsupportedEncodingException
	 */
	private String getQuery(List<SNameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (SNameValuePair pair : params) {
			//multipart parameters will be encapsuled later
			if (!pair.getName().startsWith(MULTIPART_PREFIX)) {
				if (first) {
					first = false;
				} else {
					result.append("&");
				}
				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
				result.append("=");
				// result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
				result.append(pair.getValue());
			}
		}
		return result.toString();
	}

	private InputStream httpsGetWithAuthorizationNoCertif(String _strUrl, String _paramString, @Nullable ConnectionListener _listener) {
		try {
			String url = _strUrl;
			url = url.concat(_paramString);
			return SSecurityManager.getHttpsGetUrlInputStreamNoCertificatedAuthenticated(url, user, password, "GET", TIMEOUT, _listener);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private HttpsURLConnection httpsPostWithAuthorizationNoCertif(String _strUrl, String _paramString) {
		try {
			return SSecurityManager.getHttpPostUrlConnectionWithoutCertificate(_strUrl, _paramString, user, password,TIMEOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * @param _params
	 *		Parametres per la petici� POST
	 * @param _urlPath
	 * 		Path per afegir al final de la URL_BASE o null si no n'hi ha
	 * @param _method
	 * 		GET o POST
	 * @return La resposta de realitzar la connexi� POST amb el servidor per defecte.
	 */
	private String getResponse(List<SNameValuePair> _params, String _urlBase, String _urlPath, HttpMethod _method, @Nullable ConnectionListener _listener) {
		String strUrl = _urlBase;
		if (!TextUtils.isEmpty(_urlPath)) {
			strUrl = strUrl + _urlPath;
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
			in = httpsGetWithAuthorizationNoCertif(strUrl, paramString, _listener);
		} else if (_method == HttpMethod.POST) {
			try {
				//El paramString serà el value del primer paràmetre.
				paramString = _params.get(0).getValue();

				URL url = new URL(strUrl);
				connection = null;
				// connexi� normal http
				if (!strUrl.contains("https")) {
					connection = (HttpURLConnection) url.openConnection(); // this does no network IO.
					// connexi� https
				} else {
					// Load CAs from an InputStream
					// (could be from a resource or ByteArrayInputStream or ...)
					connection = SSecurityManager.getHttpUrlConnectionCertificated(mContext, "mobile.phsystems.es.cer", "X.509", strUrl);
				}

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
				} else {
					connection.setRequestProperty("Content-Type", "application/json");
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
						dos.writeBytes("Content-Disposition: form-data; name=\"" + nameParam + "\";filename=\"" + "file." + extension + "\"" + lineEnd);
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

				http_status = ((HttpURLConnection) connection).getResponseCode();
				if (http_status / 100 != 2) {
					// redirects, server errors, lions and tigers and bears! Oh my!
					//Log.e(TAG, "HTTP STATUS ERROR " + http_status);
				}

				if(_listener != null) {
					Log.i("SDataManager", "Connected");
					_listener.connected(connection.getContentLength());
				}

				in = connection.getInputStream();

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

			if(_listener == null || _listener.getLength() <= 0) {
				while ((line = r.readLine()) != null) {
					out.append(line);
				}
			} else {
				char[] buf = new char[1024];
				int len = 0;
				long last = System.currentTimeMillis();
				do
				{
					len = r.read(buf);
					if(len > 0) {
						out.append(String.valueOf(buf).substring(0, len));
						if(System.currentTimeMillis() - last > 16) {
							Log.i("SDataManager", "Progress: " + out.length() + "[+" + len + "]");
							_listener.progress(out.length());
							last = System.currentTimeMillis();
						}
					}
				} while(len > 0);
			}
			str = out.toString();
			//Log.d(TAG, str);
			/*
			 * stream.reset(); }
			 */

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(connection!=null) {
				((HttpURLConnection) connection).disconnect();
			}
		}

		if(_listener != null) {
			_listener.finished();
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

	/*
	 * @param params
	 * 		Parametres per la petici� POST
	 * @param method
	 * 		GET o POST
	 * @return Un string amb la resposta al realitzar la connexi� amb el servidor.
	 */
	protected String getJSON(ArrayList<SNameValuePair> params, HttpMethod method) {
		return getJSON(params, null, method);
	}

	/*
	 * @param params
	 * 		Parametres per la petici� POST
	 * @param method
	 * 		GET o POST
	 * @return Un string amb la resposta al realitzar la connexi� amb el servidor.
	 */
	protected String getJSON(ArrayList<SNameValuePair> params, String urlPath, HttpMethod method) {
		return getJSON(params, urlPath, method);
	}

	/*
	 * @param params
	 * 		Parametres per la petici�
	 * @param urlPath
	 * 		Path per afegir al final de la URL_BASE o null si no n'hi ha
	 * @param method
	 * 		GET o POST
	 * @return Un string amb la resposta al realitzar la connexi� amb el servidor.
	 */
	protected String getJSON(ArrayList<SNameValuePair> params, String _urlBasePath, String urlPath, HttpMethod method, @Nullable ConnectionListener _listener) {
		try {
			String str = "";
			if (SNetworkUtils.isInternetConnectionAvailable(mContext)) {
				str = getResponse(params, _urlBasePath, urlPath, method, _listener);
			}
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
