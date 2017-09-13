package com.sixtemia.spushnotifications.base;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sixtemia.sbaseobjects.views.STextView;
import com.sixtemia.spushnotifications.R;
import com.sixtemia.spushnotifications.model.SModPushNotification;
import com.sixtemia.sutils.classes.SDateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class NotificationListItemBaseAdapter extends BaseAdapter {
	private static final String TAG = "MissatgeAdapter";

	protected ArrayList<SModPushNotification> notificacions;
	protected Activity activity;


	public NotificationListItemBaseAdapter(Activity a, ArrayList<SModPushNotification> _notificacions) {
		super();
		this.notificacions = _notificacions;
		activity = a;
	}

	@Override
	public int getCount() {
		return notificacions.size();
	}

	@Override
	public SModPushNotification getItem(int position) {
		return notificacions.get(position);
	}

	@Override
	public long getItemId(int position) {
		try {
			return Long.parseLong(getItem(position).getIntId());
		} catch (Exception ex) {
			Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
			return 0;
		}
	}

	public class ViewHolder{
		public STextView txtDescription;
		public STextView txtDate;
		public View layoutBaseNotificacio;
		public View layoutInfoNotificacio;
		public View viewCellSeparator;
		public View viewNewMessageIndicator;
		public View caretRight;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder holder;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.smodpushnotifications_item_notification_list_base, parent, false);
			holder = new ViewHolder();

			holder.txtDescription = (STextView) v.findViewById(R.id.txtDescription);
			holder.txtDate = (STextView) v.findViewById(R.id.txtDate);
			holder.layoutBaseNotificacio = v.findViewById(R.id.layoutBaseNotificacio);
			holder.layoutInfoNotificacio = v.findViewById(R.id.layoutInfoNotificacio);
			holder.viewCellSeparator = v.findViewById(R.id.viewCellSeparator);
			holder.viewNewMessageIndicator = v.findViewById(R.id.viewNewMessageIndicator);
			v.setTag(holder);
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

			if("webview".equals(notif.strCode)) {
				holder.txtDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.sixtemia.spushnotifications.R.drawable.icon_attach, 0);
			} else {
				holder.txtDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}

			holder.viewNewMessageIndicator.setVisibility(notif.isNew ? VISIBLE : GONE);
			holder.layoutInfoNotificacio.setBackgroundResource(notif.isNew ? R.drawable.notification_read : R.drawable.notification_unread);
		}
/*
*
{
	"result": "OK",
	"maxCache": 200,
	"count": 4,
	"array": [{
		"nid": "5783217e5833131234869067",
		"strDesc": "L'usuari marc et recomana assistir a: CDLC Houseclass (every thursday) 444",
		"strDate": "20160714083022",
		"strCode": "recomment",
		"strValue": "{\"id\":\"5783217e5833131234869067\"}"
	}, {
		"nid": "578731241234132d2c869068",
		"strDesc": "L'usuari marc et recomana assistir a: CDLC Houseclass (every thursday) 555",
		"strDate": "20160714083023",
		"strCode": "recommend",
		"strValue": "{\"id\":\"578731241234132d2c869068\"}"
	}, {
		"nid": "5723417e535642345c869068",
		"strDesc": "Obro una web",
		"strDate": "20160715083022",
		"strCode": "webview",
		"strValue": "http://www.sixtemia.com"
	}, {
		"nid": "5712347e5833132d2c869090",
		"strDesc": "L'usuari marc et recomana assistir a: CDLC Houseclass (every thursday) 777",
		"strDate": "20160716083023",
		"strCode": "recommend",
		"strValue": "{\"id\":\"5712347e5833132d2c869090\"}"
	}]
}
* */

		return v;
	}

	public static String getNotificacioDataFormatejada(Date date) {

		String strResultat = "";

		if (date != null) {

			DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale(Locale.getDefault().getLanguage(), Locale
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