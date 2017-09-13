package com.sixtemia.spushnotificationsdroid.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.sixtemia.spushnotificationsdroid.R;
import com.sixtemia.spushnotificationsdroid.model.SModPushNotification;
import com.sixtemia.spushnotificationsdroid.objects.TextViewCustomFont;
import com.sixtemia.spushnotificationsdroid.styles.SPushNotificationsStyleManager;
import com.sixtemia.sutils.classes.SDateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class NotificationListItemAdapter extends ArrayAdapter<SModPushNotification>{

	public ArrayList<SModPushNotification> notificacions;
	private Activity activity;
	
	
	public NotificationListItemAdapter(Activity a, int cellResourceId, ArrayList<SModPushNotification> _notificacions) {
		super(a, cellResourceId, _notificacions);
				
		this.notificacions = _notificacions;
		
		activity = a;
	}
		
	public static class ViewHolder{
		public TextViewCustomFont txtDescription;
		public TextViewCustomFont txtDate;
		public RelativeLayout layoutBaseNotificacio;
		public RelativeLayout layoutInfoNotificacio;
		public View viewCellSeparator;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
				
		View v = convertView;
		ViewHolder holder;
						
		if (v == null) {		
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.smodpushnotifications_item_notification_list, null);
			holder = new ViewHolder();
			
			holder.txtDescription = (TextViewCustomFont) v.findViewById(R.id.txtDescription);
			holder.txtDate = (TextViewCustomFont) v.findViewById(R.id.txtDate);
			holder.layoutBaseNotificacio = (RelativeLayout)v.findViewById(R.id.layoutBaseNotificacio);
			holder.layoutInfoNotificacio = (RelativeLayout)v.findViewById(R.id.layoutInfoNotificacio);
			holder.viewCellSeparator = (View) v.findViewById(R.id.viewCellSeparator);
			v.setTag(holder);
			
			holder.txtDescription.setCustomFontByFontName(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle()
					.getStrDescFontFamily(), activity);
			holder.txtDescription.setTextSize(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle().getFontSizeDesc());

			holder.txtDate.setCustomFontByFontName(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle().getStrDateFontFamily(),
					activity);
			holder.txtDate.setTextSize(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle().getFontSizeDate());

			holder.viewCellSeparator.setBackgroundColor(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle()
					.getSeparatorColor(activity));
		}
		else{
			holder=(ViewHolder)v.getTag();
		}
		
		
		final SModPushNotification notif = notificacions.get(position);
				
		if (notif != null) {				
			
			holder.txtDescription.setText(notif.getStrDescripcio());		

			try {
				Date data = SDateUtils.getDateUTC(notif.getStrDate(), "yyyyMMddHHmmss");

				holder.txtDate.setText(getNotificacioDataFormatejada(data));
				
			} catch (Exception e) {
				holder.txtDate.setText("");
			}
			

			if(notif.isNew) {
				holder.layoutBaseNotificacio.setBackgroundColor(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle()
						.getStateUnread().getUnreadBarColor(activity));
				holder.layoutInfoNotificacio.setBackgroundColor(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle()
						.getStateUnread().getBackgroundColor());

				holder.txtDescription.setTextColor(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle().getStateUnread()
						.getFontColorDesc());
				holder.txtDate.setTextColor(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle().getStateUnread()
						.getFontColorDate());
			}
			else{
				holder.layoutBaseNotificacio.setBackgroundColor(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle()
						.getStateNormal().getBackgroundColor());
				holder.layoutInfoNotificacio.setBackgroundColor(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle()
						.getStateNormal().getBackgroundColor());


				holder.txtDescription.setTextColor(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle().getStateNormal()
						.getFontColorDesc());
				holder.txtDate.setTextColor(SPushNotificationsStyleManager.Current(activity).getStyle().getCellStyle().getStateNormal()
						.getFontColorDate());
			}
			
		}			
		
		return v;
	}
	

	private String getDiaNum(String _strDate) {
		// _strDate in format aaaammdd
		String strResultat = "";
		
		if(!_strDate.equals("") && _strDate.length()>=8) {
			strResultat = _strDate.substring(6, 8);
		}
		
		return strResultat;
	}
	

	private String getDiaText(String _strDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date fechaActual = null;
		
		try {
		fechaActual = df.parse(_strDate);
		
		} catch (Exception e) {
		//System.err.println("No se ha podido parsear la fecha.");
		//e.printStackTrace();
		}
		
		GregorianCalendar fechaCalendario = new GregorianCalendar();
		fechaCalendario.setTime(fechaActual);
		fechaCalendario.setFirstDayOfWeek(Calendar.MONDAY);
		int diaSemana = fechaCalendario.get(Calendar.DAY_OF_WEEK);
		
		return getNomDiaSetmana(diaSemana);
	}
		
	
	private String getNomDiaSetmana(int intNumSetmana) {
		switch (intNumSetmana) {
		case 2:
			return activity.getResources().getString(R.string.dilluns);
			
		case 3:
			return activity.getResources().getString(R.string.dimarts);
			
		case 4:
			return activity.getResources().getString(R.string.dimecres);
			
		case 5:
			return activity.getResources().getString(R.string.dijous);
			
		case 6:
			return activity.getResources().getString(R.string.divendres);
			
		case 7:
			return activity.getResources().getString(R.string.dissabte);
			
		case 1:
			return activity.getResources().getString(R.string.diumenge);

		default:
			break;
		}
		
		return "";
	}

	
	public static String getNotificacioDataFormatejada(Date date) {

		String strResultat = "";

		if (date != null) {

			DateFormat outputFormat = new SimpleDateFormat("EEEE dd MMMM, yyyy HH:mm", new Locale(Locale.getDefault().getLanguage(), Locale
					.getDefault()
					.getCountry()));
			//Date parsed;
			try {
				strResultat = outputFormat.format(date);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		return strResultat;
	}

}
