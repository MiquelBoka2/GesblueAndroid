package pt.joaocruz04.lib;

import android.text.TextUtils;
import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by joelabello on 9/9/16.
 */

public class SixtemiaTransport extends Transport {
	public SixtemiaTransport(String url) {
		super((Proxy)null, url);
	}

	public SixtemiaTransport(Proxy proxy, String url) {
		super(proxy, url);
	}

	public SixtemiaTransport(String url, int timeout) {
		super(url, timeout);
	}

	public SixtemiaTransport(Proxy proxy, String url, int timeout) {
		super(proxy, url, timeout);
	}

	public SixtemiaTransport(String url, int timeout, int contentLength) {
		super(url, timeout);
	}

	public SixtemiaTransport(Proxy proxy, String url, int timeout, int contentLength) {
		super(proxy, url, timeout);
	}

	public void call(String soapAction, SoapEnvelope envelope) throws HttpResponseException, IOException, XmlPullParserException {
		this.call(soapAction, envelope, (List)null);
	}

	public List call(String soapAction, SoapEnvelope envelope, List headers) throws HttpResponseException, IOException, XmlPullParserException {
		return this.call(soapAction, envelope, headers, (File)null);
	}

	public List call(String soapAction, SoapEnvelope envelope, List headers, File outputFile) throws HttpResponseException, IOException, XmlPullParserException {
		if(soapAction == null) {
			soapAction = "\"\"";
		}

		byte[] requestData = this.createRequestData(envelope, "UTF-8");
		this.requestDump = this.debug?new String(requestData):null;
		this.responseDump = null;
		ServiceConnection connection = this.getServiceConnection();
		connection.setRequestProperty("User-Agent", "ksoap2-android/2.6.0+");
		if(envelope.version != 120) {
			connection.setRequestProperty("SOAPAction", soapAction);
		}

		if(envelope.version == 120) {
			connection.setRequestProperty("Content-Type", "application/soap+xml;charset=utf-8");
		} else {
			connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
		}

		connection.setRequestProperty("Accept-Encoding", "gzip");
		connection.setRequestProperty("Content-Length", "" + requestData.length);
		connection.setFixedLengthStreamingMode(requestData.length);
		HeaderProperty is;
		if(headers != null) {
			for(int os = 0; os < headers.size(); ++os) {
				is = (HeaderProperty)headers.get(os);
				connection.setRequestProperty(is.getKey(), is.getValue());
			}
		}

		connection.setRequestMethod("POST");
		OutputStream var21 = connection.openOutputStream();
		var21.write(requestData, 0, requestData.length);
		var21.flush();
		var21.close();
		Object var20 = null;
		Object var22 = null;
		List retHeaders = null;
		Object buf = null;
		int contentLength = 8192;
		boolean gZippedContent = false;
		boolean xmlContent = false;
		int status = connection.getResponseCode();
		ArrayList<HeaderProperty> retHeaderWithoutVary = new ArrayList<>();

		try {
			retHeaders = connection.getResponseProperties();


			for(Object hp : retHeaders) {
				HeaderProperty hpro = (HeaderProperty)hp;
				if(hpro != null) {
					if(!TextUtils.isEmpty(hpro.getKey()) && !hpro.getKey().equalsIgnoreCase("Vary")) {
						retHeaderWithoutVary.add(hpro);
					}
				}
			}

			retHeaders.clear();

			for(HeaderProperty hp : retHeaderWithoutVary) {
				if(hp != null) {
					retHeaders.add(hp);
				}
			}

			for(Object o : retHeaders) {
				HeaderProperty hp = (HeaderProperty) o;
			}

			for(int e = 0; e < retHeaders.size(); ++e) {
				HeaderProperty hp = (HeaderProperty)retHeaders.get(e);
				if(null != hp.getKey()) {
					if(hp.getKey().equalsIgnoreCase("content-length") && hp.getValue() != null) {
						try {
							contentLength = Integer.parseInt(hp.getValue());
						} catch (NumberFormatException var18) {
							contentLength = 8192;
						}
					}
					if(hp.getKey().equalsIgnoreCase("Vary") && hp.getValue() != null) {
					}

					if(hp.getKey().equalsIgnoreCase("Content-Type") && hp.getValue().contains("xml")) {
						xmlContent = true;
					}

					if(hp.getKey().equalsIgnoreCase("Content-Encoding") && hp.getValue().equalsIgnoreCase("gzip")) {
						gZippedContent = true;
					}
				}
			}

			if(status != 200) {
				throw new HttpResponseException("HTTP request failed, HTTP status: " + status, status);
			}

			if(contentLength > 0) {
				if(gZippedContent) {
					var22 = this.getUnZippedInputStream(new BufferedInputStream(connection.openInputStream(), contentLength));
				} else {
					var22 = new BufferedInputStream(connection.openInputStream(), contentLength);
				}
			}
		} catch (IOException var19) {
			if(contentLength > 0) {
				if(gZippedContent && contentLength > 0) {
					var22 = this.getUnZippedInputStream(new BufferedInputStream(connection.getErrorStream(), contentLength));
				} else {
					var22 = new BufferedInputStream(connection.getErrorStream(), contentLength);
				}
			}

			if(var19 instanceof HttpResponseException && !xmlContent) {
				if(this.debug && var22 != null) {
					this.readDebug((InputStream)var22, contentLength, outputFile);
				}

				connection.disconnect();
				throw var19;
			}
		}

		if(this.debug) {
			var22 = this.readDebug((InputStream)var22, contentLength, outputFile);
		}

		this.parseResponse(envelope, (InputStream)var22);
		is = null;
		var21 = null;
		buf = null;
		connection.disconnect();
		connection = null;
		return retHeaders;
	}

	private InputStream readDebug(InputStream is, int contentLength, File outputFile) throws IOException {
		Object bos;
		if(outputFile != null) {
			bos = new FileOutputStream(outputFile);
		} else {
			bos = new ByteArrayOutputStream(contentLength > 0?contentLength:262144);
		}

		byte[] buf = new byte[256];
		int readTotal = 0;
		boolean enabled = true;

		while(true) {
			int rd = 0;
			try {
				if(enabled) {
					rd = is.read(buf, 0, buf.length);
				}
			} catch (Exception ex) {
				Log.e("SixtemiaTransport", "Error: " + ex.getMessage());
				rd = 0;
				enabled = false;
				for(int i = 0; i < buf.length; i++) {
					if(buf[i] == 0) {
						rd = i;
						break;
					}
				}
			}
//			int rd = is.read(buf, 0, contentLength - readTotal > 256 ? 256 : contentLength - readTotal);
			if(rd <= 0) {
				((OutputStream)bos).flush();
				if(bos instanceof ByteArrayOutputStream) {
					buf = ((ByteArrayOutputStream)bos).toByteArray();
				}

				bos = null;
				this.responseDump = new String(buf);
				is.close();
				return (InputStream)(outputFile != null?new FileInputStream(outputFile):new ByteArrayInputStream(buf));
			}

			readTotal += rd;

			((OutputStream)bos).write(buf, 0, rd);
			buf = new byte[256];
		}
	}

	private InputStream getUnZippedInputStream(InputStream inputStream) throws IOException {
		try {
			return (GZIPInputStream)inputStream;
		} catch (ClassCastException var3) {
			return new GZIPInputStream(inputStream);
		}
	}

	public ServiceConnection getServiceConnection() throws IOException {
		return new ServiceConnectionSE(this.proxy, this.url, this.timeout);
	}
}