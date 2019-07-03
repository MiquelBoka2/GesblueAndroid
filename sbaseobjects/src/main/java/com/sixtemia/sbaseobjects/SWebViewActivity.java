package com.sixtemia.sbaseobjects;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;

import com.sixtemia.sbaseobjects.objects.SFragmentActivity;

public class SWebViewActivity extends SFragmentActivity implements SWebViewFragment.SWebViewFragmentListener {
    public static final String KEY_URL = "webview_url";
    public static final String KEY_CONFIG = "config";
    private SWebViewFragment mWebView;

    public static final String TAG = "SWebView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swebview);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(KEY_URL)) {
            String url = extras.getString(KEY_URL);
            SWebViewFragment.Config config = extras.getParcelable(KEY_CONFIG);
            if(config == null) {
                config = new SWebViewFragment.Config(mContext);
            }
            mWebView = SWebViewFragment.newInstance(url, config);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, mWebView);
            transaction.commit();
        } else {
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(!mWebView.goBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void shareUrl(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing url");
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(i, "Share url"));
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void close() {
        finish();
    }
}