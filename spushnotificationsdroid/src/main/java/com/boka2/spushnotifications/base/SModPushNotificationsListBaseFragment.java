package com.boka2.spushnotifications.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boka2.sbaseobjects.SWebViewActivity;
import com.boka2.spushnotifications.R;
import com.boka2.spushnotifications.classes.PreferencesSPush;
import com.boka2.spushnotifications.datamanager.DataManagerSPushNotifications;
import com.boka2.spushnotifications.datamanager.SDataManagerListener;
import com.boka2.spushnotifications.db.DataBase;
import com.boka2.spushnotifications.model.SModPushNotification;
import com.boka2.spushnotifications.model.SModPushNotificationsListResult;
import com.boka2.spushnotifications.styles.SPushNotificationsStyleManager;

import java.util.ArrayList;

public abstract class SModPushNotificationsListBaseFragment extends Fragment {
	public static final String TAG = "NotifFrag";
	protected View view;

	protected ProgressBar pbLoading;
	protected View viewLoading;
	protected ListView listNotificacions;
	protected View viewNoResults;
	protected SwipeRefreshLayout swipeToRefreshLayout;

	protected boolean isLoadingData = false;

	protected DataManagerSPushNotifications dataManager;

	protected final int[] defaultSwipeColors = new int[] {
			R.color.smodpushnotifications_refresh_loading_color_1,
			R.color.smodpushnotifications_refresh_loading_color_2,
			R.color.smodpushnotifications_refresh_loading_color_3,
			R.color.smodpushnotifications_refresh_loading_color_4
	};

	BaseAdapter adapterLlistat;
	public ArrayList<SModPushNotification> arrayNotificacions;

	/*
	 * Torna la ID del layout del fragment
	 *
	 * Ha de tenir com a minim:
	 *  - ProgressBar de loading
	 *  - TextView de loading
	 *  - ListView
	 *  - TextView de "no hi ha resultats"
	 *  - SwipeRefreshLayout
	 *
	 * @return La ID del layout (R.layout.algunacosa)
	 */
	protected int getLayout() {
		return R.layout.fragment_push_notifications_list;
	}

	protected ProgressBar getPbLoading(View view) {
		return (ProgressBar) view.findViewById(R.id.pbLoading);
	}
	protected View getLoading(View view) {
		return view.findViewById(R.id.txtLoading);
	}
	protected View getNoResults(View view) {
		return view.findViewById(R.id.txtNoResults);
	}
	protected ListView getListNotificacions(View view) {
		return (ListView) view.findViewById(R.id.listNotificacions);
	}
	protected SwipeRefreshLayout getSwipeRefreshLayout(View view) {
		return (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
	}

	/*
	 * Es crida quan s'estan descarregant les dades.
	 * Cal amagar les vistes que toquin i mostrar el pbLoading i viewLoading.
	 *
	 * @return true si s'ha fet tot el procés o false si cal que el fragment
	 *         faci les crides originals.
	 */
	protected boolean isLoading() {
		return false;
	}

	/*
	 * Es crida quan s'han descarregat les dades.
	 * Cal mostrar les vistes que toquin i amagar el pbLoading i viewLoading.
	 *
	 * @return true si s'ha fet tot el procés o false si cal que el fragment
	 *         faci les crides originals.
	 */
	protected boolean loadingDone() {
		return false;
	}

	/*
	 * Es crida quan s'han descarregat les dades i no hi ha resultats.
	 * Cal mostrar les vistes que toquin i amagar el pbLoading i viewLoading.
	 *
	 * @return true si s'ha fet tot el procés o false si cal que el fragment
	 *         faci les crides originals.
	 */
	protected boolean loadingDoneNoData() {
		return false;
	}

	/*
	 * Crea i torna un adapter amb les notificacions.
	 *
	 * @param _arrayNotificacions L'array amb les notificacions
	 * @return L'adapter ja creat i preparat
	 */
	protected BaseAdapter getAdapter(ArrayList<SModPushNotification> _arrayNotificacions) {
		return getDefaultAdapter(_arrayNotificacions);
	}

	/*
	 * Tracta (o no) l'event del on click a una notificació.
	 *
	 * @param notif La notificació que s'ha clickat.
	 * @return Torna true si ja s'ha tractat o false si no.
	 */
	protected abstract boolean clickNotificacio(SModPushNotification notif);

	/*
	 * Agafa els colors del loading del SwipeRefresh.
	 * Es pot tornar defaultSwipeColors per el comportament per defecte
	 *
	 * @return
	 */
	protected abstract int[] getSwipeRefreshColors();

	protected NotificationListItemBaseAdapter getDefaultAdapter(ArrayList<SModPushNotification> _arrayNotificacions) {
		return new NotificationListItemBaseAdapter(getActivity(), _arrayNotificacions);
	}

	public SModPushNotificationsListBaseFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			DataBase.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DataManagerSPushNotifications getDataManager() {
		if(dataManager == null) {
			dataManager = new DataManagerSPushNotifications(getActivity().getApplicationContext());
		}
		return dataManager;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(getLayout(), container, false);

		initControls();

		getPushNotifications();

		return view;
	}

	/*
	 * Inicialitza i configura els controls.
	 * Si es fa override, sempre cridar primer a super() per tenir tots els objectes carregats
	 */
	protected void initControls() {
		listNotificacions = getListNotificacions(view);

		pbLoading = getPbLoading(view);
		viewLoading = getLoading(view);

		if(viewLoading instanceof TextView) {
			((TextView) viewLoading).setText(Html.fromHtml(getResources().getString(R.string.smodpushnotifications_loading_notifications)));
		}
		//viewLoading.setTextSize(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getFontSize());
		//viewLoading.setTextColor(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getFontColor());

		viewNoResults = getNoResults(view);

		//viewNoResults.setTextSize(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getFontSize());
		//viewNoResults.setTextColor(SPushNotificationsStyleManager.Current(getActivity()).getStyle().getFontColor());
		viewNoResults.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshLlistat();
			}
		});

		swipeToRefreshLayout = getSwipeRefreshLayout(view);
		swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				refreshLlistat();
			}
		});

		swipeToRefreshLayout.setColorSchemeResources(getSwipeRefreshColors());

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

	protected void getPushNotifications() {
		if(!isLoading()) {
			listNotificacions.setVisibility(View.GONE);
			pbLoading.setVisibility(View.VISIBLE);
			viewLoading.setVisibility(View.VISIBLE);
		}

		if (!isLoadingData) {
			isLoadingData = true;

			String strDataUltimaNotificacio = DataBase.Context.NotificacionsSet.getNewestNotificationDate();

			if (strDataUltimaNotificacio.equals("")) {
				strDataUltimaNotificacio = PreferencesSPush.getAppInstallationDate(getActivity().getApplicationContext());
			}

			//strDataUltimaNotificacio = "20130220074804"; TEST

			String strAppTokenID = PreferencesSPush.getAppTokenID(getActivity()); //getResources().getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);

			getDataManager().getLlistatNotificacions(strDataUltimaNotificacio, strAppTokenID, true, new SDataManagerListener() {

				@Override
				public void onCompletion(Object result) {
					SModPushNotificationsListResult response = (SModPushNotificationsListResult) result;
					boolean hasData = response != null && response.getArrayListNotificacions().size() > 0;
					boolean loadingDone = true;
					boolean noDataDone = true;

					if (hasData) {
						loadingDone = loadingDone();
						updateDadesLlistat(response.getArrayListNotificacions());
					} else {
						noDataDone = loadingDoneNoData();
						if(!noDataDone) {
							viewNoResults.setVisibility(View.VISIBLE);
						}
					}

					if(!loadingDone || !noDataDone) {
						pbLoading.setVisibility(View.GONE);
						viewLoading.setVisibility(View.GONE);
					}
					isLoadingData = false;
				}

				@Override
				public void onError(Object result) {
					try {
						Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.smodpushnotifications_notificacions_download_error), Toast.LENGTH_SHORT).show();
					} catch (Exception ex) {
						Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
					}
					if(!loadingDoneNoData()) {
						pbLoading.setVisibility(View.GONE);
						viewLoading.setVisibility(View.GONE);

						viewNoResults.setVisibility(View.VISIBLE);
					}
					isLoadingData = false;
				}

			});
		}
	}

	protected void updateDadesLlistat(ArrayList<SModPushNotification> _arrayNotificacions) {

		arrayNotificacions = _arrayNotificacions;

		adapterLlistat = getAdapter(_arrayNotificacions);

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
			viewNoResults.setVisibility(View.VISIBLE);
		} else {
			viewNoResults.setVisibility(View.GONE);
		}

	}

	protected void actionSelectNotificacio(int _position) {
		SModPushNotification notif = arrayNotificacions.get(_position);

		if(!clickNotificacio(notif)) {
			if (!TextUtils.isEmpty(notif.getStrCode()) && !TextUtils.isEmpty(notif.getStrValue())) {
				if (notif.getStrCode().equals(GCMIntentServiceBase.EXTRA_PUSH_WEBVIEW_URL)) {
					// Si el codi és del tipus webview. Obrirem el webview amb la url corresponent
					Intent intent = new Intent(getActivity(), SWebViewActivity.class);
					intent.putExtra(SWebViewActivity.KEY_URL, notif.getStrValue());
					startActivity(intent);
				}
			}
		}
	}

	public void refreshLlistat() {
		if (!isLoadingData) {
			swipeToRefreshLayout.setRefreshing(true);
			isLoadingData = true;

			String strDataUltimaNotificacio = DataBase.Context.NotificacionsSet.getNewestNotificationDate();

			if (strDataUltimaNotificacio.equals("")) {
				strDataUltimaNotificacio = PreferencesSPush.getAppInstallationDate(getActivity().getApplicationContext());
			}

			//strDataUltimaNotificacio = "20130220074804"; TEST

			String strAppTokenID = PreferencesSPush.getAppTokenID(getActivity()); //getResources().getString(R.string.SMODPUSHNOTIFICATIONS_APP_TOKEN_ID);

			getDataManager().getLlistatNotificacions(/*DataBase.Context.NotificacionsSet.getNewestNotificationID(),*/strDataUltimaNotificacio,
					strAppTokenID, false, new SDataManagerListener() {

						@Override
						public void onCompletion(Object result) {
							SModPushNotificationsListResult response = (SModPushNotificationsListResult) result;

							if (response != null) {

								// Nom�s actualitzem si el total de notificacions ha canviat
								if (arrayNotificacions != null && response.getArrayNotificacions() != null
										&& arrayNotificacions.size() != response.getArrayListNotificacions().size()) {

									arrayNotificacions.clear();
									//updateDadesLlistat(response.getArrayNotificacions());
								} else {
									try {
										Toast.makeText(getActivity(), R.string.modpushnotifications_refresh_no_changes, Toast.LENGTH_SHORT).show();
									} catch (Exception ex) {
										Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
									}
								}

								updateDadesLlistat(response.getArrayListNotificacions());

							}

							isLoadingData = false;
							swipeToRefreshLayout.setRefreshing(false);

						}

						@Override
						public void onError(Object result) {
							try {
								Context c = getActivity();
								if(c != null) {
									Toast.makeText(c, getResources().getString(R.string.smodpushnotifications_notificacions_download_error), Toast.LENGTH_SHORT).show();
								}
							} catch (Exception ex) {
								Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
							}
							isLoadingData = false;
							swipeToRefreshLayout.setRefreshing(false);
						}

					});

		}
	}
}