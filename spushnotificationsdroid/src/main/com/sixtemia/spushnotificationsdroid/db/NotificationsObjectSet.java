package com.boka2.spushnotificationsdroid.db;

import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.ObjectSet;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.boka2.spushnotificationsdroid.model.SModPushNotification;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class NotificationsObjectSet extends ObjectSet<SModPushNotification> {

	public NotificationsObjectSet(ObjectContext pContext) throws AdaFrameworkException {
		super(SModPushNotification.class, pContext);
	}
	
	public void updateWithNotificacions(List<SModPushNotification> _notificacions) {
		for (SModPushNotification notif : _notificacions) {
			SModPushNotification ndb = getNotificacioById(notif.intId);
			if (ndb != null) {
				notif.modificar(ndb);
			} else {
				add(notif);
			}
		}
		try {
			save();
		} catch (AdaFrameworkException e) {
			e.printStackTrace();
		}
	}
	
	public SModPushNotification getNotificacioById(int id) {
		for (SModPushNotification notif : this) {
			if (notif.intId == id) {
				return notif;
			}
		}
		return null;
	}
	
	public void saveNewNotificacio(SModPushNotification _notificacio) {
		add(_notificacio);
		try {
			save();
		} catch (AdaFrameworkException e) {
			e.printStackTrace();
		}
	}
	
	public void updateNotificacio(SModPushNotification _existingNotif) {

		/*if (this.size() <= 0) {
			Log.e("coyupi", "No todos");
			return;
		}*/

		SModPushNotification ndb = getNotificacioById(_existingNotif.intId);
		if (ndb != null) {
			ndb.modificar(_existingNotif);
		} else {
			add(_existingNotif);
		}
		
		try {
			save();
		} catch (AdaFrameworkException e) {
			e.printStackTrace();
		}

	}
		
	
	public ArrayList<SModPushNotification> getAllNotificacionsFromBDD() {
		ArrayList<SModPushNotification> arrayResultat = new ArrayList<SModPushNotification>();
		
		try {
			fill(SModPushNotification.DEFAULT_ORDER);
			
		} catch (AdaFrameworkException e) {
			e.printStackTrace();
		}
		
		if (this!=null) {
			for (int i = 0; i < this.size(); i++) {
				SModPushNotification notif = this.get(i);
				arrayResultat.add(notif);
			}
		}
		
		return arrayResultat;
	}
	
		
	public void markAllNotificationsAsRead() {
		try {
			fill();
			
			for (int i = 0; i < size(); i++) {
				SModPushNotification notif = get(i);
				
				// Si est� caducat i no est� a l'array de ids v�lids. L'eliminem
				if(notif.isNew) {
					notif.isNew = false;
					notif.setStatus(com.mobandme.ada.Entity.STATUS_UPDATED);
				}
			
			}
			
			save();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void deleteOldNotifications(int intMaxCache) {
		try {
			fill(SModPushNotification.DEFAULT_ORDER);
			
			if(intMaxCache < size()) {
				// Hem d'eliminar notificacions velles
				for (int i = intMaxCache; i < size(); i++) {
					SModPushNotification notif = get(i);
					notif.setStatus(com.mobandme.ada.Entity.STATUS_DELETED);				
				}
				
			}	
			
			save();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	} 
	
	
	public int getNewestNotificationID() {
		try {
			fill(SModPushNotification.DEFAULT_ORDER);
			if(size()>0) {
				return get(0).getIntId();
			}
			else{
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}

	public String getNewestNotificationDate() {
		try {
			fill(SModPushNotification.DEFAULT_ORDER);
			
			if(size()>0) {
				return get(0).getStrDate();
			}
			else{
				return "";
			}
									
		} catch (Exception e) {
			return "";
		}
	} 
	
	
}
