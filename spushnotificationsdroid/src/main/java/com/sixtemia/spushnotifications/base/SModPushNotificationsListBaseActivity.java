package com.sixtemia.spushnotifications.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.sixtemia.spushnotifications.R;
import com.sixtemia.spushnotifications.classes.PreferencesSPush;
import com.sixtemia.spushnotifications.objects.SPushFragmentActivity;
import com.sixtemia.spushnotifications.styles.SPushNotificationsStyleManager;


public abstract class SModPushNotificationsListBaseActivity extends SPushFragmentActivity {

	public SModPushNotificationsListBaseFragment fragmentNotificacions;
	
	/**
	 * Torna la ID del layout a fer servir.
	 * Ha de tenir com a minim un FrameLayout
	 * 
	 * @return La ID del layout (R.layout.elquesigui)
	 */
	protected int getLayout() {
		return R.layout.activity_push_notifications_list;
	}
	
	/**
	 * Torna la ID del FrameLayout a on posarem el fragment.
	 * 
	 * @return La ID del FrameLayout (R.id.framelayoutid)
	 */
	protected int getFrameLayout() {
		return R.id.content_frame_list_notifications;
	}
	
	/**
	 * Instancia un fragment que heredi de SModPushNotificationsListBaseFragment
	 * per fer-la servir a la activity.
	 * 
	 * @return El fragment personalitzat o SModPushNotificationsListBaseFragment.newInstance();
	 */
	protected abstract SModPushNotificationsListBaseFragment newFragmentInstance();
	
	/**
	 * Torna la ID del menu a mostrar
	 * 
	 * @return La ID del menu (R.menu.pushmenu) o 0 per no mostrar-ne cap
	 */
	protected int getMenu() {
		return 0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayout());

		SPushNotificationsStyleManager.init(mContext);

		initContent();

		if(getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.smodpushnotifications_list_notifications_title);

			applyStylesToActionBar();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void initContent() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if (fragmentNotificacions == null) {
			fragmentNotificacions = newFragmentInstance();
		}

		ft.replace(getFrameLayout(), fragmentNotificacions);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int menuId = getMenu();
		if(menuId != 0) {
			getMenuInflater().inflate(menuId, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finishNotificationsList();
			return true;
		} else if (itemId == R.id.action_refresh) {
			if (fragmentNotificacions != null) {
				fragmentNotificacions.refreshLlistat();
			}
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		finishNotificationsList();
	}

	private void finishNotificationsList() {
		try {

			String strClassReturn = PreferencesSPush.getReturnClassPackage(mContext);

			if (!TextUtils.isEmpty(strClassReturn)) {
				Class returnClass = Class.forName(strClassReturn);

				Intent returnIntent = new Intent(mContext, returnClass);
				returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(returnIntent);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finish();
	}
}