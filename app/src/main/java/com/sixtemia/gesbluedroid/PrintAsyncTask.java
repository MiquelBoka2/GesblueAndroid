package com.sixtemia.gesbluedroid;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

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
import java.util.Date;

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
	private Date dataCreacio;

	public interface PrintListener {
		void onFinish(Exception ex, boolean isFirstTime, boolean printed);
		void onError();
	}


	public PrintAsyncTask(Printer _printer, Context _context, Sancio _sancio, String _numeroButlleta, Date _fecha, boolean _isFirstTime, PrintListener _listener) {
		mPrinter = _printer;
		mContext = _context;
		sancio = _sancio;
		numeroButlleta = _numeroButlleta;
		isFirstTime = _isFirstTime;
		mListener = _listener;
		dataCreacio = _fecha;
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

			if (PreferencesGesblue.getConcessio(mContext) == 4) {//Banyoles
				ticketConfiguration.setImatgePeu(Picasso.with(mContext).load(PreferencesGesblue.getImatgePeu(mContext)).get());
			}
			try {
				if (sancio.getModelMarca() != null && !TextUtils.isEmpty(sancio.getModelMarca().getImatgemarca()))
					ticketConfiguration = ticketConfiguration.setLogoCotxe(Picasso.with(mContext).load(sancio.getModelMarca().getImatgemarca()).get());
			} catch (IOException e) {
				e.printStackTrace();
				e.printStackTrace();
			}
			ticketConfiguration = ticketConfiguration.setVehicle(Utils.languageMultiplexer(sancio.getModelTipusVehicle().getNomtipusvehiclees(), sancio.getModelTipusVehicle().getNomtipusvehiclecat()))
					.setMarcaModel(sancio.getModelMarca().getImatgemarca(), sancio.getModelMarca().getNommarca() + " " + sancio.getModelModel().getNommodel())
					.setColor(Utils.languageMultiplexer(sancio.getModelColor().getNomcolores(), sancio.getModelColor().getNomcolorcat()))
					.setLlocInfraccio(sancio.getModelCarrer().getNomcarrer() + " " + sancio.getNumero())
					.setPrecepteInfringit(sancio.getModelInfraccio().getPrecepte())
					.setFetDenunciat(sancio.getModelInfraccio().getNom())
					.setImport(Float.parseFloat(sancio.getModelInfraccio().getImporte()))
					.setDte(Float.parseFloat(sancio.getModelInfraccio().getImporte()) / 2)
					.setAgent(Long.toString(PreferencesGesblue.getCodiAgent(mContext)));

			ticketConfiguration.setDataCreacio(dataCreacio);

			if (PreferencesGesblue.getCodiBarresServiCaixa(mContext)) {
				ticketConfiguration.setCodiBarresServiCaixa(generarServiCaixa());
				ticketConfiguration
						.setEmissora(getEmisora(mContext))
						.setMod(PreferencesGesblue.getMod(mContext))
						.setReferencia(PreferencesGesblue.getReferencia(mContext))
						.setIdentificacio(PreferencesGesblue.getIdentificacio(mContext))
						.setImpDte(Float.parseFloat(PreferencesGesblue.getImpDte(mContext)) / 100);
			}
			Calendar calendar = Calendar.getInstance();
			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();

			calendar.setTime((Date) dataCreacio);
			calendar.add(Calendar.MONTH, 1); //Com van de 0 a 11, hi afegim 1 per tenir el correcte.
			calendar.add(Calendar.DATE, getDiesDescompteFromValorsServicaixa());


			if (PreferencesGesblue.getConcessio(mContext) == 4){//Banyoles
				calendar1.setTime((Date) dataCreacio);
				//calendar1.add(Calendar.MONTH, 1); //Com van de 0 a 11, hi afegim 1 per tenir el correcte.
				if(Integer.parseInt(sancio.getModelInfraccio().getTempsanulacio())==120) {
					calendar1.add(Calendar.MINUTE, Integer.parseInt(sancio.getModelInfraccio().getTempsanulacio()));
				}else{
					calendar1.set(Calendar.SECOND, 59);
					calendar1.set(Calendar.MINUTE, 59);
					calendar1.set(Calendar.HOUR_OF_DAY, 23);
				}



				calendar2.setTime((Date) dataCreacio);
				//calendar2.add(Calendar.MONTH, 1); //Com van de 0 a 11, hi afegim 1 per tenir el correcte.
				calendar2.set(Calendar.SECOND, 59);
				calendar2.set(Calendar.MINUTE, 59);
				calendar2.set(Calendar.HOUR_OF_DAY, 23);
				//calendar2.set(Calendar.DATE, getDiesDescompteFromValorsServicaixa(());
			}
			else if(PreferencesGesblue.getConcessio(mContext) == 25){//Estartit
				calendar1.setTime((Date) dataCreacio);
				//calendar1.add(Calendar.MONTH, 1); //Com van de 0 a 11, hi afegim 1 per tenir el correcte.
                calendar1.add(Calendar.MINUTE, Integer.parseInt(sancio.getModelInfraccio().getTempsanulacio()));


				calendar2.setTime((Date) dataCreacio);
				calendar2.add(Calendar.MINUTE, 1440);
				//calendar2.set(Calendar.DATE, getDiesDescompteFromValorsServicaixa(());
			}
			else{
				calendar1.setTime((Date) dataCreacio);
				//calendar1.add(Calendar.MONTH, 1); //Com van de 0 a 11, hi afegim 1 per tenir el correcte.
				calendar1.add(Calendar.MINUTE, Integer.parseInt(sancio.getModelInfraccio().getTempsanulacio()));

			}


			ticketConfiguration
					.setDataLimitPagament(calendar)
					.setDataAnulacioArray(new DataAnulacio[]{
											new DataAnulacio(
												Float.parseFloat(sancio.getModelInfraccio().getAnulacio()),
													calendar1),
											new DataAnulacio(
												Float.parseFloat(sancio.getModelInfraccio().getAnulacio2()),
													calendar2)});

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
		String referencia = numeroButlleta.substring(numeroButlleta.length()-10, numeroButlleta.length());
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
		Calendar calendar = Calendar.getInstance();

		calendar.setTime((Date)dataCreacio);
		//calendar.add(Calendar.MONTH, 1); //Com van de 0 a 11, hi afegim 1 per tenir el correcte.
		calendar.add(Calendar.DATE, getDiesDescompteFromValorsServicaixa());
		String limit = String.valueOf(convertToJulian(calendar));
		while(limit.length() < 3) {
			limit = 0 + limit;
		}
		identificacion += limit;
		PreferencesGesblue.setIdentificacio(mContext, identificacion);

		float importe = Float.parseFloat(sancio.getModelInfraccio().getImporte());

		long calculcontrol0 = (Long.parseLong(emisora)*76);
		long cc = Long.parseLong(numeroButlleta.substring(numeroButlleta.length()-10, numeroButlleta.length()));
		long codicurt = cc * 9;
		calculcontrol0 = calculcontrol0 + codicurt;
		float ic = importe/2*100;
		long fide = Long.parseLong(identificacion);
		long ide = (fide+(long)ic-1)*55;
		calculcontrol0 = calculcontrol0 + (long)ide;

		double calculcontrol = (double)calculcontrol0/97;

		double f2 = calculcontrol % 1;
		int i = (int)(f2 * 100);
		Log.d("numberD",calculcontrol0+" - "+i);
		/*String numberD = String.valueOf(calculcontrol);
		Log.d("numberD",numberD.length()+" - "+numberD.indexOf(".")+" - "+(numberD.length()-(numberD.indexOf(".") + 1)));
		if(numberD.indexOf ( "." )>-1) {
			numberD = numberD.substring(numberD.indexOf(".") + 1, numberD.indexOf ( "." )+Math.min(2,numberD.length()-(numberD.indexOf(".") + 1)));
		}
		else{
			numberD = "0";
		}
		Log.d("numberD2: ", numberD);*/
		String control = String.valueOf(99-i);
		//String[] control_parts=control.split(".");
		//control = control_parts[0];
		while(control.length() < 2) {
			control = 0 + control;
		}
		code.append(control);

		PreferencesGesblue.setPrefCodiControl(mContext,control);


		code.append(PreferencesGesblue.getIdentificacio(mContext));

		//Importe (cents)
		PreferencesGesblue.setImpDte(mContext,  String.valueOf(Float.parseFloat(sancio.getModelInfraccio().getImporte())/2*100));

		String imp=String.format("%.02f", Float.parseFloat(sancio.getModelInfraccio().getImporte())/2);

		code.append(fill8Digits(imp));
		//Dígito de Paridad (1)
		code.append("0");

		return code.toString();
	}

	private int getLastDigitDataLimit() {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime((Date)dataCreacio);
		//calendar.add(Calendar.MONTH, 1); //Com van de 0 a 11, hi afegim 1 per tenir el correcte.
		calendar.add(Calendar.DATE, getDiesDescompteFromValorsServicaixa());
		int year = calendar.get(Calendar.YEAR);

		return year % 10;
	}

	public static int convertToJulian(Calendar calendar) {
		/*int year = calendar.get(Calendar.YEAR);
		String syear = String.format("%04d",year).substring(2);
		int century = Integer.parseInt(String.valueOf(((year / 100)+1)).substring(1));
		int julian = Integer.parseInt(String.format("%d%s%03d",century,syear,calendar.get(Calendar.DAY_OF_YEAR)));
		return julian%100;*/
		return calendar.get(Calendar.DAY_OF_YEAR);
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
	private int getDiesDescompteFromValorsServicaixa() {

		String[] cadenaServicaixa = PreferencesGesblue.getValorsServiCaixa(mContext).split("/");
		if(cadenaServicaixa.length > 2) {
			return Integer.parseInt(cadenaServicaixa[2]);
		}
		return 20;
	}


	private int getEjercicioDevengo() {
		Calendar c = Calendar.getInstance();
		c.setTime((Date)dataCreacio);
		int any = c.get(Calendar.YEAR);
		return (any % 100);
	}

	protected void onProgressUpdate(String... progress) { }

	@Override
	protected void onPostExecute(Boolean printed) {

		mListener.onFinish(exception, isFirstTime, printed);
	}

	private String fill8Digits(String d) {
		String aRetornar = "";
		if(d.contains(",")) {
			String[] splitted = d.split("\\,");
			aRetornar = splitted[0];
			aRetornar+= splitted[1];
		}

		while(aRetornar.length() < 8) {
			aRetornar = 0 + aRetornar;
		}
		return aRetornar;
	}
}