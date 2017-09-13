package com.sixtemia.spushnotifications;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.sixtemia.spushnotifications.classes.PreferencesSPush;
import com.sixtemia.spushnotifications.objects.SPushFragmentActivity;
import com.sixtemia.spushnotifications.styles.SPushNotificationsStyleManager;


public class SModPushNotificationsListActivity extends SPushFragmentActivity {

	public SModPushNotificationsListFragment fragmentNotificacions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_notifications_list);

		/*try {
			DataBase.initialize(getBaseContext());
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		SPushNotificationsStyleManager.init(mContext);

		initContent();

		if(getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(R.string.smodpushnotifications_list_notifications_title);
		}

		applyStylesToActionBar();
	}

	@Override
	protected void onResume() {
		super.onResume();

		//applyStylesToActionBar();
	}

	private void initContent() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if (fragmentNotificacions == null) {
			fragmentNotificacions = SModPushNotificationsListFragment.newInstance();
		}

		ft.replace(R.id.content_frame_list_notifications, fragmentNotificacions);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_push_notifications_list, menu);
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

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		finish();
	}

}
