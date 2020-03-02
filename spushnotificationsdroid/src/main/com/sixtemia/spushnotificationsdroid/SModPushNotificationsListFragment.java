package com.boka2.spushnotificationsdroid;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.Fragment;
import androidx.core.widget.SwipeRefreshLayout;
import androidx.core.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.boka2.spushnotificationsdroid.adapters.NotificationListItemAdapter;
import com.boka2.spushnotificationsdroid.classes.GCMIntentService;
import com.boka2.spushnotificationsdroid.classes.PreferencesSPush;
import com.boka2.spushnotificationsdroid.datamanager.DataManagerSPushNotifications;
import com.boka2.spushnotificationsdroid.datamanager.SDataManagerListener;
import com.boka2.spushnotificationsdroid.db.DataBase;
import com.boka2.spushnotificationsdroid.model.SModPushNotification;
import com.boka2.spushnotificationsdroid.model.SModPushNotificationsListResult;
import com.boka2.spushnotificationsdroid.objects.TextViewCustomFont;
import com.boka2.spushnotificationsdroid.styles.SPushNotificationsStyleManager;

public class SModPushNotificationsListFragment extends Fragment {

	private View view;

	private ProgressBar pbLoading;
	private TextViewCustomFont txtLoading;
	private ListView listNotificacions;
	private TextViewCustomFont txtNoResults;
	private SwipeRefreshLayout swipeToRefreshLayout;

	private boolean isLoadingData = false;

	private DataManagerSPushNotifications dataManager;

	NotificationListItemAdapter adapterLlistat;
	public ArrayList<SModPushNotification> arrayNotificacions;

	public static SModPushNotificationsListFragment newInstance() {
		SModPushNotificationsListFragment fragment = new SModPushNotificationsListFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			DataBase.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}

		dataManager = new DataManagerSPushNotifications(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_push_notifications_list, container, false);

		initControls();

		getPushNotifications();

		return view;
	}

	private void initControls() {
		listNotificacions = (ListView) view.findViewById(R.id.listNotificacions);

		pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
		txtLoading = (TextViewCustomFont) view.findViewById(R.id.txtLoading);

		txtLoading.setCustomFontByFontName(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getStrFontFamily(), getActivity());
		txtLoading.setText(Html.fromHtml(getResources().getString(R.string.smodpushnotifications_loading_notifications)));
		txtLoading.setTextSize(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getFontSize());
		txtLoading.setTextColor(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getFontColor());

		txtNoResults = (TextViewCustomFont) view.findViewById(R.id.txtNoResults);

		txtNoResults.setCustomFontByFontName(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getStrFontFamily(), getActivity());
		txtNoResults.setTextSize(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getFontSize());
		txtNoResults.setTextColor(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getFontColor());

		swipeToRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refreshLlistat();
			}
		});

		swipeToRefreshLayout.setColorScheme(R.color.smodpushnotifications_refresh_loading_color_1,
				R.color.smodpushnotifications_refresh_loading_color_2, R.color.smodpushnotifications_refresh_loading_color_3,
				R.color.smodpushnotifications_refresh_loading_color_4);

		swipeToRefreshLayout.setBackgroundColor(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getBackgroundColor());

		// Necessari perquè l'scroll del listview funcioni dins el swipetorefresh 
		listNotificacions.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int topRowVerticalPosition = (listNotificacions == null || listNotificacions.getChildCount() == 0) ? 0 : listNotificacions
						.getChildAt(0).getTop();
				swipeToRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
			}
		});
	}

	private void getPushNotifications() {
		listNotificacions.setVisibility(View.GONE);
		pbLoading.setVisibility(View.VISIBLE);
		txtLoading.setVisibility(View.VISIBLE);

		if (dataManager == null) {
			dataManager = new DataManagerSPushNotifications(getActivity().getApplicationContext());
		}

		if (!isLoadingData) {
			isLoadingData = true;

			String strDataUltimaNotificacio = DataBase.Context.NotificacionsSet.getNewestNotificationDate();

			if (strDataUltimaNotificacio.equals("")) {
				strDataUltimaNotificacio = PreferencesSPush.getAppInstallationDate(getActivity().getApplicationContext());
			}

			//strDataUltimaNotificacio = "20130220074804"; TEST

			String strAppTokenID = PreferencesSPush.getAppTokenID(getActivity()); //getResources().getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);

			dataManager.getLlistatNotificacions(/*DataBase.Context.NotificacionsSet.getNewestNotificationID(),*/strDataUltimaNotificacio,
					strAppTokenID, true, new SDataManagerListener() {

						@Override
						public void onCompletion(Object result) {
							SModPushNotificationsListResult response = (SModPushNotificationsListResult) result;

							if (response != null) {
								updateDadesLlistat(response.getArrayNotificacions());
							} else {
								txtNoResults.setVisibility(View.VISIBLE);
							}

							isLoadingData = false;
							pbLoading.setVisibility(View.GONE);
							txtLoading.setVisibility(View.GONE);
						}

						@Override
						public void onError(Object result) {
							Toast.makeText(getActivity().getApplicationContext(),
									getResources().getString(R.string.smodpushnotifications_notificacions_download_error), Toast.LENGTH_SHORT).show();
							isLoadingData = false;
							pbLoading.setVisibility(View.GONE);
							txtLoading.setVisibility(View.GONE);

							txtNoResults.setVisibility(View.VISIBLE);
						}

					});

		}
	}

	protected void updateDadesLlistat(ArrayList<SModPushNotification> _arrayNotificacions) {

		arrayNotificacions = _arrayNotificacions;

		adapterLlistat = new NotificationListItemAdapter(getActivity(), R.layout.smodpushnotifications_item_notification_list, arrayNotificacions);

		listNotificacions.setAdapter(adapterLlistat);
		listNotificacions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				actionSelectNotificacio(position);
			}
		});
		//llistatTodo.onRefreshComplete(SUtils.getStringDataActualPullToRefresh(getActivity()));

		//listNotificacions.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.notifications_fade_in));
		listNotificacions.setVisibility(View.VISIBLE);

		//listNotificacions.onRefreshComplete(SUtils.getStringDataActual(getBaseContext()));

		if (_arrayNotificacions.size() == 0) {
			txtNoResults.setVisibility(View.VISIBLE);
		} else {
			txtNoResults.setVisibility(View.GONE);
		}

	}

	protected void actionSelectNotificacio(int _position) {
		SModPushNotification notif = arrayNotificacions.get(_position);

		if (!TextUtils.isEmpty(notif.getStrCode()) && !TextUtils.isEmpty(notif.getStrValue())) {
			if (notif.getStrCode().equals(GCMIntentService.EXTRA_PUSH_WEBVIEW_URL)) {
				// Si el codi és del tipus webview. Obrirem el webview amb la url corresponent
				Intent intent = new Intent(getActivity(), SModPushNotificationsWebviewActivity.class);
				intent.putExtra(SModPushNotificationsWebviewActivity.BUNDLE_WEBVIEW_URL, notif.getStrValue());
				startActivity(intent);
			}
		}

	}

	public void refreshLlistat() {
		if (!isLoadingData) {
			swipeToRefreshLayout.setRefreshing(true);
			isLoadingData = true;

			if (dataManager == null) {
				dataManager = new DataManagerSPushNotifications(getActivity().getApplicationContext());
			}

			String strDataUltimaNotificacio = DataBase.Context.NotificacionsSet.getNewestNotificationDate();

			if (strDataUltimaNotificacio.equals("")) {
				strDataUltimaNotificacio = PreferencesSPush.getAppInstallationDate(getActivity().getApplicationContext());
			}

			//strDataUltimaNotificacio = "20130220074804"; TEST

			String strAppTokenID = PreferencesSPush.getAppTokenID(getActivity()); //getResources().getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);

			dataManager.getLlistatNotificacions(/*DataBase.Context.NotificacionsSet.getNewestNotificationID(),*/strDataUltimaNotificacio,
					strAppTokenID, false, new SDataManagerListener() {

						@Override
						public void onCompletion(Object result) {
							SModPushNotificationsListResult response = (SModPushNotificationsListResult) result;

							if (response != null) {

								// Nom�s actualitzem si el total de notificacions ha canviat
								if (arrayNotificacions != null && response.getArrayNotificacions() != null
										&& arrayNotificacions.size() != response.getArrayNotificacions().size()) {

									arrayNotificacions.clear();
									//updateDadesLlistat(response.getArrayNotificacions());
								} else {
									Toast.makeText(getActivity(), R.string.modpushnotifications_refresh_no_changes, Toast.LENGTH_SHORT).show();
								}

								updateDadesLlistat(response.getArrayNotificacions());

							}

							isLoadingData = false;
							swipeToRefreshLayout.setRefreshing(false);

						}

						@Override
						public void onError(Object result) {
							Toast.makeText(getActivity().getApplicationContext(),
									getResources().getString(R.string.smodpushnotifications_notificacions_download_error), Toast.LENGTH_SHORT).show();

							isLoadingData = false;
							swipeToRefreshLayout.setRefreshing(false);
						}

					});

		}
	}

}
