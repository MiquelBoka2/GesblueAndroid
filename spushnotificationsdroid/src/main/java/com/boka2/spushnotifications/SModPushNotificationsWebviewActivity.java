package com.boka2.spushnotifications;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boka2.spushnotifications.objects.SPushFragmentActivity;
import com.boka2.spushnotifications.styles.SPushNotificationsStyleManager;

public class SModPushNotificationsWebviewActivity extends SPushFragmentActivity {

	public static final String BUNDLE_WEBVIEW_URL = "webview_url";

	private ProgressBar pbLoading;
	private TextView txtNoResults;
	private WebView webviewContent;

	public String strUrl;
	public boolean error = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_notifications_webview);

		Bundle b = this.getIntent().getExtras();
		if (b != null && b.containsKey(BUNDLE_WEBVIEW_URL)) {
			strUrl = b.getString(BUNDLE_WEBVIEW_URL);
		} else {
			strUrl = "";
		}

		SPushNotificationsStyleManager.init(mContext);

		initControls();
		initContent();
		initActionBar();
	}

	private void initActionBar() {
		if(getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			applyStylesToActionBar();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finishWebview();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	protected void applyStylesToActionBar() {
		// Apliquem la typo
		actionbarStyle(SPushNotificationsStyleManager.Current(mContext).getStyle().getNavigationStyle().getStrFontFamily(),
				SPushNotificationsStyleManager.Current(mContext).getStyle().getNavigationStyle().getTitleColor(), SPushNotificationsStyleManager
						.Current(mContext).getStyle().getNavigationStyle().getFontSize());
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(SPushNotificationsStyleManager.Current(mContext).getStyle().getNavigationStyle().getBackgroundColor()));
	}


	private void finishWebview() {
		Intent intent = new Intent(mContext, SModPushNotificationsListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

		finish();
	}

	private void initControls() {
	//	pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

	//	txtNoResults = (TextView) findViewById(R.id.txtNoResults);

	//	webviewContent = (WebView) findViewById(R.id.webviewContent);

		webviewContent.setWebViewClient(new CustomWebViewClient());
		webviewContent.setWebChromeClient(new CustomWebChromeClient());
		webviewContent.getSettings().setJavaScriptEnabled(true);

		webviewContent.getSettings().setLoadWithOverviewMode(true);
		webviewContent.getSettings().setUseWideViewPort(true);

		webviewContent.getSettings().setAppCacheEnabled(true);
		webviewContent.setVisibility(View.GONE);

		//txtNoResults.setCustomFontByFontName(SPushNotificationsStyleManager.Current(mContext).getStyle().getStrFontFamily(), mContext);
		txtNoResults.setTextSize(SPushNotificationsStyleManager.Current(mContext).getStyle().getFontSize());
		txtNoResults.setTextColor(SPushNotificationsStyleManager.Current(mContext).getStyle().getFontColor());

	//	((RelativeLayout) findViewById(R.id.layout_content)).setBackgroundColor(SPushNotificationsStyleManager.Current(mContext).getStyle().getBackgroundColor());
	}

	private void initContent() {
		webviewContent.loadUrl(strUrl);
	}

	private class CustomWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);

			error = true;
			pbLoading.setVisibility(ProgressBar.GONE);
			webviewContent.setVisibility(View.GONE);
		}

	}

	private class CustomWebChromeClient extends WebChromeClient {

		public void onProgressChanged(WebView view, int progress) {
			if (progress < 100 && pbLoading.getVisibility() == ProgressBar.GONE) {
				pbLoading.setVisibility(ProgressBar.VISIBLE);
			}

			pbLoading.setProgress(progress);

			if (progress == 100) {

				if (!error) {
					pbLoading.setVisibility(ProgressBar.GONE);
					webviewContent.setVisibility(View.VISIBLE);
					txtNoResults.setVisibility(ProgressBar.GONE);
				} else {
					pbLoading.setVisibility(ProgressBar.GONE);
					webviewContent.setVisibility(View.GONE);
					txtNoResults.setVisibility(ProgressBar.VISIBLE);
				}

			}
		}

	}

	public void backPressed() {
		if (webviewContent.canGoBack()) {
			webviewContent.goBack();
		} else {
			finishWebview();
		}
	}

}
