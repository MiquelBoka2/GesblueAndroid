package com.boka2.sbaseobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.boka2.sbaseobjects.objects.SFragment;
import com.boka2.sbaseobjects.views.SProgressBar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.animation.AnimationUtils.loadAnimation;

public class SWebViewFragment extends SFragment {

	private static final String KEY_URL = "url";

	protected SWebViewFragmentListener mListener;
	protected WebView webview;
	protected View layoutWebView;
	protected SProgressBar pbWebProgress;
	protected TextView txtWebTitle;
	protected TextView txtWebUrl;
	protected ImageView btnClose;
	protected String url;

	protected int backgroundResource;
	protected int backgroundColor;
	protected int textColor;
	protected int progressBarColor;
	protected int backImage;
	protected int statusbarColor;
	protected boolean showUrl = true;
	protected boolean showMenu = true;
	protected String forcedTitle;

	public SWebViewFragment() {
	}

	public static class Config implements Parcelable {
		public int bgRes;
		public int bgCol;
		public int txtCol;
		public int pbCol;
		public int backRes;
		public int statusbarColor;
		public boolean showUrl = true;
		public boolean showMenu = true;
		public String title;

		/*
		 * Cal cridar un dels setters després del constructor!
		 *
		 * @param txtCol Color del texte
		 * @param pbCol Color del ProgressBar
		 */
		public Config(int txtCol, int pbCol) {
			this.txtCol = txtCol;
			this.pbCol = pbCol;
		}

		/*
		 * Inicialitza tot lo necessari amb els colors de la app
		 * @param c
		 */
		public Config(Context c) {
			this.txtCol = ContextCompat.getColor(c, R.color.colorAccent);
			this.pbCol = ContextCompat.getColor(c, R.color.colorPrimary);
			this.bgCol = ContextCompat.getColor(c, R.color.colorPrimary);
			this.statusbarColor = ContextCompat.getColor(c, R.color.colorPrimary);
		}

		public Config setTxtCol(int txtCol) {
			this.txtCol = txtCol;
			return this;
		}

		public Config setTitle(String title) {
			this.title = title;
			return this;
		}

		public Config setBackgroundResource(int bgRes) {
			this.bgRes = bgRes;
			return this;
		}

		public Config setBackgroundColor(int bgCol) {
			this.bgCol = bgCol;
			return this;
		}

		public Config setBackImage(int backRes) {
			this.backRes = backRes;
			return this;
		}

		public Config setStatusbarColor(int statusbarColor) {
			this.statusbarColor = statusbarColor;
			return this;
		}

		public Config setShowUrl(boolean showUrl) {
			this.showUrl = showUrl;
			return this;
		}

		public Config setShowMenu(boolean showMenu) {
			this.showMenu = showMenu;
			return this;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(this.bgRes);
			dest.writeInt(this.bgCol);
			dest.writeInt(this.txtCol);
			dest.writeInt(this.pbCol);
			dest.writeInt(this.backRes);
			dest.writeInt(this.statusbarColor);
			dest.writeByte(this.showUrl ? (byte) 1 : (byte) 0);
			dest.writeByte(this.showMenu ? (byte) 1 : (byte) 0);
			dest.writeString(this.title);
		}

		protected Config(Parcel in) {
			this.bgRes = in.readInt();
			this.bgCol = in.readInt();
			this.txtCol = in.readInt();
			this.pbCol = in.readInt();
			this.backRes = in.readInt();
			this.statusbarColor = in.readInt();
			this.showUrl = in.readByte() != 0;
			this.showMenu = in.readByte() != 0;
			this.title = in.readString();
		}

		public static final Creator<Config> CREATOR = new Creator<Config>() {
			@Override
			public Config createFromParcel(Parcel source) {
				return new Config(source);
			}

			@Override
			public Config[] newArray(int size) {
				return new Config[size];
			}
		};
	}

	public static SWebViewFragment newInstance(String _url, Config config) {
		SWebViewFragment fragment = new SWebViewFragment();
		if(_url != null && !_url.contains("http") && !_url.contains("https")) {
			_url = "http://" + _url;
		}
		fragment.url = _url;
		fragment.textColor = config.txtCol;
		if(config.bgRes != 0) {
			fragment.backgroundResource = config.bgRes;
		} else if(config.bgCol != 0) {
			fragment.backgroundColor = config.bgCol;
		} else {
			fragment.backgroundColor = 0;
		}
		fragment.progressBarColor = config.pbCol;
		fragment.backImage = config.backRes;
		fragment.statusbarColor = config.statusbarColor;
		fragment.showUrl = config.showUrl;
		fragment.showMenu = config.showMenu;
		fragment.forcedTitle = config.title;
		return fragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(outState!=null) {
			outState.putString(KEY_URL, url);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null) {
			url = savedInstanceState.getString(KEY_URL);
		}
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		DLog("Animation enabled: " + mAnimateNextEnter);
		if (!mAnimateNextEnter) {
			return super.onCreateAnimation(transit, enter, nextAnim);
		}
		if(enter) {
			return loadAnimation(getContext(), android.R.anim.fade_in);
		} else {
			return loadAnimation(getContext(), android.R.anim.fade_out);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =  inflater.inflate(R.layout.swebview_fragment, container, false);
		webview = (WebView) v.findViewById(R.id.webview);
		layoutWebView = v.findViewById(R.id.layoutWebView);
		pbWebProgress = (SProgressBar) v.findViewById(R.id.pbWebProgress);
		txtWebTitle = (TextView) v.findViewById(R.id.txtWebTitle);
		if(forcedTitle != null) {
			txtWebTitle.setText(forcedTitle);
			txtWebTitle.setVisibility(VISIBLE);
		}
		txtWebUrl = (TextView) v.findViewById(R.id.txtWebUrl);
		txtWebUrl.setVisibility(showUrl ? VISIBLE : GONE);
		btnClose = (ImageView) v.findViewById(R.id.btnClose);

		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.close();
			}
		});

		if(textColor == 0) {
			textColor = ContextCompat.getColor(mContext, R.color.colorAccent);
		}
		if(backgroundColor == 0 && backgroundResource == 0) {
			backgroundColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
		}

		txtWebTitle.setTextColor(textColor);
		txtWebUrl.setTextColor(textColor);
		if(progressBarColor != 0) {
			pbWebProgress.setProgressColor(progressBarColor);
		}
		if(backImage != 0) {
			btnClose.setImageResource(backImage);
		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getActivity().getWindow().setStatusBarColor(statusbarColor);
		}

		setToolbar(v, R.id.toolbar);
		if(backgroundResource != 0) {
			getToolbar().setBackgroundResource(backgroundResource);
		} else {
			getToolbar().setBackgroundColor(backgroundColor);
		}
		if(showMenu) {
			setMenu(R.menu.swebview, new Toolbar.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					final int OPEN = R.id.menu_open;
					final int SHARE = R.id.menu_share;
					if (item.getItemId() == OPEN) {
						mListener.openUrl(webview.getUrl());
					} else if (item.getItemId() == SHARE) {
						mListener.shareUrl(webview.getUrl());
					}
					return true;
				}
			});
		}

		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				pbWebProgress.setVisibility(newProgress == 0 || newProgress == 100 ? GONE : VISIBLE);
				pbWebProgress.setProgress(newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if(forcedTitle == null) {
					if (!TextUtils.isEmpty(title)) {
						DLog("ReceivedTitle: " + title);
						try {
							anima(250);
							txtWebTitle.setVisibility(VISIBLE);
							txtWebTitle.setText(title);
							txtWebUrl.setTextSize(dimenToSp(R.dimen.webview_url));
						} catch (Exception ex) {
							Log.e(SWebViewFragment.class.getSimpleName(), "Error setting title: " + ex.getLocalizedMessage(), ex);
						}
					}
				}
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			long animating = 0;
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				try {
					DLog("PageStarted: " + url);
					//Si hi ha una redirecció, les animacions es criden massa ràpid,
					//aquí controlarem el temps de animació i quan es va cridar per
					//última vegada per evitar problemes de concurrència
					long animateTime = 100;
					if (System.currentTimeMillis() - animating >= animateTime) {
						anima(animateTime);
						animating = System.currentTimeMillis();
					} else {
						DLog("PageStarted noAnim");
					}
					if(forcedTitle == null) txtWebTitle.setVisibility(GONE);
					txtWebUrl.setText(url);
					txtWebUrl.setTextSize(dimenToSp(R.dimen.webview_url_big));
				} catch (Exception ex) {
					Log.e(SWebViewFragment.class.getSimpleName(), "Error setting url: " + ex.getLocalizedMessage(), ex);
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pbWebProgress.setVisibility(GONE);
			}
		});

		webview.getSettings().setJavaScriptEnabled(true);

		if(webview.getUrl()==null) {
			webview.loadUrl(url);
		}
		return v;
	}

	public boolean goBack() {
		if(webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return false;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof SWebViewFragmentListener) {
			mListener = (SWebViewFragmentListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnInformeResultFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface SWebViewFragmentListener {
		void openUrl(String url);
		void shareUrl(String url);
		void close();
	}
}