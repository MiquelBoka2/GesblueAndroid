package com.sixtemia.gesbluedroid;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.datecs.api.printer.Printer;
import com.sixtemia.gesbluedroid.global.PreferencesGesblue;
import com.sixtemia.gesbluedroid.global.Utils;
import com.sixtemia.gesbluedroid.ticketstyle.DataAnulacio;
import com.sixtemia.gesbluedroid.ticketstyle.TicketConfiguration;
import com.sixtemia.gesbluedroid.ticketstyle.TicketPrinter;
import com.sixtemia.sutils.classes.SSystemUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

import static com.sixtemia.gesbluedroid.activities.FormulariActivity.createCalendar;
import static com.sixtemia.gesbluedroid.global.PreferencesGesblue.getEmisora;

/**
 * Creat per joelabello al 4/10/16.
 */
public class PrintAsyncTask extends AsyncTask<String, String, Boolean> {
	private Printer mPrinter;
	private Context mContext;
	private Sancio sancio;
	private String numeroButlleta;
	private Boolean isFirstTime;
	private PrintListener mListener;
	private Exception exception;

	public interface PrintListener {
		void onFinish(Exception ex, boolean isFirstTime);
		void onError();
	}


	public PrintAsyncTask(Printer _printer, Context _context, Sancio _sancio, String _numeroButlleta,boolean _isFirstTime, PrintListener _listener) {
		mPrinter = _printer;
		mContext = _context;
		sancio = _sancio;
		numeroButlleta = _numeroButlleta;
		isFirstTime = _isFirstTime;
		mListener = _listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... url) {

		try {
			TicketPrinter ticketPrinter = new TicketPrinter(mPrinter, mContext);

			TicketConfiguration ticketConfiguration = new TicketConfiguration();

			ticketConfiguration.setLogo(Picasso.with(mContext).load(PreferencesGesblue.getLogo(mContext)).get())
					.setButlleta(numeroButlleta)
					.setTextCap(PreferencesGesblue.getTextCap(mContext))
					.setData(createCalendar(0))
					.setMatricula(sancio.getMatricula());
					try{
						if(sancio.getModelMarca() != null && !TextUtils.isEmpty(sancio.getModelMarca().getImatgemarca())) ticketConfiguration = ticketConfiguration.setLogoCotxe(Picasso.with(mContext).load(sancio.getModelMarca().getImatgemarca()).get());
					} catch(IOException e) {
						e.printStackTrace();
					}
					ticketConfiguration = ticketConfiguration.setVehicle(Utils.languageMultiplexer(sancio.getModelTipusVehicle().getNomtipusvehiclees(), sancio.getModelTipusVehicle().getNomtipusvehiclecat()))
					.setMarcaModel(sancio.getModelMarca().getImatgemarca(), sancio.getModelMarca().getNommarca() + " " + sancio.getModelModel().getNommodel())
					.setColor(Utils.languageMultiplexer(sancio.getModelColor().getNomcolores(), sancio.getModelColor().getNomcolorcat()))
					.setLlocInfraccio(sancio.getModelCarrer().getNomcarrer() + " " + sancio.getNumero())
					.setPrecepteInfringit(sancio.getModelInfraccio().getPrecepte())
					.setFetDenunciat(sancio.getModelInfraccio().getNom())
					.setImport(Float.parseFloat(sancio.getModelInfraccio().getImporte()))
					.setDte(Float.parseFloat(sancio.getModelInfraccio().getImporte())/2)
					.setAgent(Long.toString(PreferencesGesblue.getCodiAgent(mContext)));

					if(PreferencesGesblue.getCodiBarresServiCaixa(mContext)) {
						ticketConfiguration.setCodiBarresServiCaixa(generarServiCaixa());
						ticketConfiguration
                            .setEmissora(getEmisora(mContext))
                            .setMod(PreferencesGesblue.getMod(mContext))
                            .setReferencia(PreferencesGesblue.getReferencia(mContext))
                            .setIdentificacio(PreferencesGesblue.getIdentificacio(mContext))
                            .setImpDte(Float.parseFloat(PreferencesGesblue.getImpDte(mContext))/100);
					}
					ticketConfiguration
					.setDataLimitPagament(createCalendar(20))
					.setDataAnulacioArray(new DataAnulacio[]{
											new DataAnulacio(
												Float.parseFloat(sancio.getModelInfraccio().getAnulacio()),
												createCalendar(Integer.parseInt(sancio.getModelInfraccio().getTempsanulacio()))),
											new DataAnulacio(
												Float.parseFloat(sancio.getModelInfraccio().getAnulacio2()),
												createCalendar(Integer.parseInt(sancio.getModelInfraccio().getTempsanulacio2())))});

					if(PreferencesGesblue.getCodiBarresVisible(mContext)) {
						ticketConfiguration.setCodiBarres(numeroButlleta);
					}
					ticketConfiguration
					.setTextAnulacio(mContext.getString(SSystemUtils.isDebugging(mContext) ? R.string.textAnulacioP : R.string.textAnulacio))
					.setTextPeu(PreferencesGesblue.getTextPeu(mContext))
					.setQr(PreferencesGesblue.getAdrecaQr(mContext));

					Boolean b= PreferencesGesblue.getTextPeuVisible(mContext);

			return ticketPrinter.print(mContext, ticketConfiguration);
		} catch (IOException e) {
			e.printStackTrace();
			exception = e;
		}

		return null;

	}

	private String generarServiCaixa() {
		StringBuilder code = new StringBuilder("");

		//Identificador de Aplicación
		code.append("90");

		//Tipo de Formato
		code.append("521");

		//Emisora
		String emisora = getEmisoraFromValorsServicaixa();
		PreferencesGesblue.setEmisora(mContext, emisora);
		code.append(getEmisora(mContext));

		//TODO S'HA DE MOSTRAR UN 'MOD' A LA CAIXA DE PAGAMENT BANCARI. D'ON SURT?
		PreferencesGesblue.setMod(mContext, "2");

		//Referencia TODO CANVIAR PER UN DINÀMIC! Identificación Documento(10)/control(2)
		String referencia = "003160083555";
		PreferencesGesblue.setReferencia(mContext, referencia);
		code.append(PreferencesGesblue.getReferencia(mContext));

		//Identificación TODO (10)
			String identificacion = "";
			//Discriminante del Periodo (1)
			identificacion += "1";
			//Tributo (Según Anexo 6 del Cuaderno 60)
			identificacion += getTributoFromValorsServicaixa();
			//Ejercicio de devengo
			identificacion += getEjercicioDevengo();
			//Año de la fecha límite (último dígito)
			identificacion += getLastDigitDataLimit();
			//Fecha juliana límite de pago //TODO CANVIAR PER UN DINÀMIC!
			identificacion += convertToJulian(createCalendar(20));
		PreferencesGesblue.setIdentificacio(mContext, identificacion);
		code.append(PreferencesGesblue.getIdentificacio(mContext));

		//Importe (cents)
		PreferencesGesblue.setImpDte(mContext, sancio.getModelInfraccio().getImporte());
		code.append(fill8Digits(sancio.getModelInfraccio().getImporte()));

		//Dígito de Paridad (1)
		code.append("0");

		return code.toString();
	}

	private int getLastDigitDataLimit() {

		int year = createCalendar(20).get(Calendar.YEAR);

		return year % 10;
	}

	public static int convertToJulian(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		String syear = String.format("%04d",year).substring(2);
		int century = Integer.parseInt(String.valueOf(((year / 100)+1)).substring(1));
		int julian = Integer.parseInt(String.format("%d%s%03d",century,syear,calendar.get(Calendar.DAY_OF_YEAR)));
		return julian%100;
	}

	private String getEmisoraFromValorsServicaixa() {

		String[] cadenaServicaixa = PreferencesGesblue.getValorsServiCaixa(mContext).split("/");
		if(cadenaServicaixa.length > 1) {
			return cadenaServicaixa[1];
		}
		return "";
	}

	private String getTributoFromValorsServicaixa() {

		String[] cadenaServicaixa = PreferencesGesblue.getValorsServiCaixa(mContext).split("/");
		if(!TextUtils.isEmpty(cadenaServicaixa[0])) {
			return cadenaServicaixa[0];
		}
		return "";
	}

	private int getEjercicioDevengo() {
		Calendar c = Calendar.getInstance();
		int any = c.get(Calendar.YEAR);
		return (any % 100);
	}

	protected void onProgressUpdate(String... progress) { }

	@Override
	protected void onPostExecute(Boolean printed) {
		mListener.onFinish(exception, isFirstTime);
	}

	private String fill8Digits(String d) {
		String aRetornar = "";
		if(d.contains(".")) {
			String[] splitted = d.split("\\.");
			aRetornar = splitted[0];
			aRetornar+= splitted[1];
		}

		while(aRetornar.length() < 8) {
			aRetornar = 0 + aRetornar;
		}
		return aRetornar;
	}
}