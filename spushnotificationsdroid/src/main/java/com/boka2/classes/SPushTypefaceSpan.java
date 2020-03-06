package com.boka2.classes;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.collection.LruCache;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SPushTypefaceSpan extends MetricAffectingSpan {
	/* An <code>LruCache</code> for previously loaded typefaces. */
	private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

	private Typeface mTypeface;
	private int intColor;
	private float textSize;
	private boolean colorAndSizeChanged;

	/*
	 * Load the {@link Typeface} and apply to a {@link Spannable}.
	 */
	public SPushTypefaceSpan(Context context, String typefaceName) {
		mTypeface = sTypefaceCache.get(typefaceName);

		if (mTypeface == null) {
			//mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), String.format("fonts/%s", typefaceName));
			mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), typefaceName);

			// Cache the loaded Typeface
			sTypefaceCache.put(typefaceName, mTypeface);
		}

		intColor = -1;
		textSize = -1;
		colorAndSizeChanged = false;
	}

	public SPushTypefaceSpan(Context context, String typefaceName, int _textColor, float _textSize) {
		mTypeface = sTypefaceCache.get(typefaceName);
		if (mTypeface == null) {
			//mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), String.format("fonts/%s", typefaceName));
			mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), typefaceName);

			// Cache the loaded Typeface
			sTypefaceCache.put(typefaceName, mTypeface);
		}

		intColor = _textColor;
		textSize = getDPFromPixels(context, _textSize);
		colorAndSizeChanged = true;
	}

	private float getDPFromPixels(Context _context, float _pixels) {
		// Get the screen's density scale
		final float scale = _context.getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (float) (_pixels * scale /*+ 0.5f*/);
	}

	@Override
	public void updateMeasureState(TextPaint p) {
		p.setTypeface(mTypeface);

		if (colorAndSizeChanged) {
			p.setColor(intColor);
			p.setTextSize(textSize);
		}

		// Note: This flag is required for proper typeface rendering
		p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		tp.setTypeface(mTypeface);

		if (colorAndSizeChanged) {
			tp.setColor(intColor);
			tp.setTextSize(textSize);
		}

		// Note: This flag is required for proper typeface rendering
		tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
	}
	
	public static String getTypefaceExtension(Context _context, String _strFontName) {
		String strAsset = "";
		try {
			List<String> assets = Arrays.asList(_context.getResources()
					.getAssets().list("fonts"));
			if (assets.contains(_strFontName + ".ttf")) {
				strAsset = "fonts/" + _strFontName + ".ttf";
			} else if (assets.contains(_strFontName + ".otf")) {
				strAsset = "fonts/" + _strFontName + ".otf";
			}
		} catch (IOException e) {
		}
		return strAsset;
	}
}
