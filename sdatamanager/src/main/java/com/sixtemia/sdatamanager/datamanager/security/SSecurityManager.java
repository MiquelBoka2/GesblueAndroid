package com.sixtemia.sdatamanager.datamanager.security;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.sixtemia.sdatamanager.datamanager.listeners.ConnectionListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by alejandroarangua on 22/4/16.
 */
public class SSecurityManager {

	public static final String LOG_TAG = "SSecurityManager";

	/**
	 * @param _algorithm Una de les constants de TrustManagerFactory
	 * @param _keystore El keystore que es vol fer servir per inicialitzar el manager
	 * @return retorna el trustfactory creat i inicialitzat, o null
	 */
	public static TrustManagerFactory getTrustManagerFactory(String _algorithm, KeyStore _keystore) {
		// Create a TrustManager that trusts the CAs in our KeyStore
		String tmfAlgorithm = _algorithm;
		TrustManagerFactory tmf = null;
		try {
			tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			tmf.init(_keystore);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return tmf;
	}

	/**
	 * @param _tmf TrustManagerFactory per inicialitzar el SSContext
	 * @param _protocol Protocol (Exemple: "TLS"...)
	 * @return retorna el SSLContext inicialitzat o null.
	 */
	public static SSLContext getSSLContext(TrustManagerFactory _tmf, String _protocol) {
		// Create an SSLContext that uses our TrustManager
		SSLContext context = null;
		try {
			context = SSLContext.getInstance(_protocol);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			context.init(null, _tmf.getTrustManagers(), null);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return context;
	}

	/**
	 * @param _alias Alias del certificat
	 * @param _cert Certificat
	 * @return retorna el keyStore inicialitzat o null.
	 */
	public static KeyStore getKeyStore(String _alias, Certificate _cert) {
		// Create a KeyStore containing our trusted CAs
		String keyStoreType = KeyStore.getDefaultType();
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance(keyStoreType);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		try {
			try {
				keyStore.load(null, null);
			} catch (CertificateException|IOException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			keyStore.setCertificateEntry(_alias, _cert);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return keyStore;
	}

	public static HttpsURLConnection getHttpPostUrlConnectionWithoutCertificate(String _strUrl, String _strParams, String _username, String _pass, int _timeOut) throws IOException {
		HttpsURLConnection connection = null;
		try {
			URL url = new URL(_strUrl);
			connection = (HttpsURLConnection) url.openConnection();

			//Create the SSL connection
			try {
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, getTrustAllCertsManager(), new SecureRandom());
				connection = (HttpsURLConnection) url.openConnection();
				connection.setHostnameVerifier(DO_NOT_VERIFY);
				connection.setDefaultSSLSocketFactory(context.getSocketFactory());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!TextUtils.isEmpty(_username) && !TextUtils.isEmpty(_pass)) {
				// autentificacio
				String auth = _username + ":" + _pass;
				connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP));
			}
			connection.setRequestProperty("Content-Length", String.valueOf(_strParams.getBytes().length));
			connection.setFixedLengthStreamingMode(_strParams.getBytes().length);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
			connection.setRequestProperty("Accept-Language", "es;q=1");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Accept","*/*");
			connection.setUseCaches(false);
			connection.setReadTimeout(_timeOut);
			connection.setConnectTimeout(_timeOut);
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, e.getMessage(),e);
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage(),e);
		}
		return connection;
	}

	/**
	 * @param _context Es fa servir per poder accedir als assets i obtenir el certificat
	 * @param _certificate  nom del certificat
	 * @param _certificateType  Tipus del certificat (exemple: "X.509")
	 * @param _strUrl Url a la que s'ha de connectar.
	 * @return retorna una connexió HttpURLConnection inicialitzada o null.
	 * @throws IOException
	 */
	public static HttpURLConnection getHttpUrlConnectionCertificated(Context _context, String _certificate, String _certificateType, String _strUrl) throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL(_strUrl);
		// Load CAs from an InputStream
		// (could be from a resource or ByteArrayInputStream or ...)
		CertificateFactory cf = null;
		try {
			cf = CertificateFactory.getInstance(_certificateType);
		} catch (CertificateException e) {
			e.printStackTrace();
		}

		Certificate ca = null;
		InputStream caInput = caInput = new BufferedInputStream(_context.getAssets().open(_certificate));
		try {
			try {
				ca = cf.generateCertificate(caInput);
			} catch (CertificateException e1) {
				e1.printStackTrace();
			}
			// Create a KeyStore containing our trusted CAs
			KeyStore keyStore = SSecurityManager.getKeyStore("ca", ca);

			// Create a TrustManager that trusts the CAs in our KeyStore
			TrustManagerFactory tmf = SSecurityManager.getTrustManagerFactory(TrustManagerFactory.getDefaultAlgorithm(), keyStore);

			// Create an SSLContext that uses our TrustManager
			SSLContext context = SSecurityManager.getSSLContext(tmf,"TLS");

			// Tell the URLConnection to use a SocketFactory from our SSLContext
			//URL url = new URL("https://certs.cac.washington.edu/CAtest/");
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.setSSLSocketFactory(context.getSocketFactory());
			connection = urlConnection;
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			caInput.close();
		}
		return connection;
	}

	public static HttpsURLConnection getHttpsGetUrlConnectionNoCertificatedAuthenticated(String _strUrl, String _username, String _pass, String _method, int _timeOut) throws IOException {
		HttpsURLConnection connection = null;
		InputStream in = null;
		try {
			URL url = new URL(_strUrl);
			connection = (HttpsURLConnection) url.openConnection();

			//Create the SSL connection
			try {
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, getTrustAllCertsManager(), new SecureRandom());
				connection = (HttpsURLConnection) url.openConnection();
				connection.setHostnameVerifier(DO_NOT_VERIFY);
				connection.setDefaultSSLSocketFactory(context.getSocketFactory());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//Use needed Authorization
			if (!TextUtils.isEmpty(_username) && !TextUtils.isEmpty(_pass)) {
				// autentificacio
				String auth = _username + ":" + _pass;
				connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP));
			}
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "ca;q=1");
			connection.setRequestProperty("Accept","*/*");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			connection.setReadTimeout(_timeOut);
			connection.setConnectTimeout(_timeOut);
			connection.setRequestMethod(_method);
			connection.setDoInput(true);
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, e.getMessage(),e);
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage(),e);
		}
		return connection;
	}

	/**
	 * @param _strUrl Url a la que s'ha de connectar.
	 * @return retorna una connexió HttpURLConnection inicialitzada o null.
	 * @throws IOException
	 */
	public static InputStream getHttpsGetUrlInputStreamNoCertificatedAuthenticated(String _strUrl, String _username, String _pass, String _method, int _timeOut, @Nullable ConnectionListener _listener) throws IOException {
		HttpsURLConnection connection = getHttpsGetUrlConnectionNoCertificatedAuthenticated(_strUrl,_username,_pass,_method,_timeOut);
		connection.connect();
		InputStream in = null;
		try{
			connection.connect();
			in = connection.getInputStream();
			int http_status = connection.getResponseCode();
			if (http_status / 100 != 2) {
				// redirects, server errors, lions and tigers and bears! Oh my!
				//Log.e(TAG, "HTTP STATUS ERROR " + http_status);
			}
			if(_listener != null) {
				_listener.connected(connection.getContentLength());
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, e.getMessage(),e);
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage(),e);
		}
		return in;
	}

	/**
	 * Trust every server - dont check for any certificate
	 */
	public static TrustManager[] getTrustAllCertsManager() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trustAllCerts;
	}

	// always verify the host - dont check for certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};


}
