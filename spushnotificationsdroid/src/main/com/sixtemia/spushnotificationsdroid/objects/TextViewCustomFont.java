package com.boka2.spushnotificationsdroid.objects;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.boka2.spushnotificationsdroid.objects.FontManager;
import com.boka2.spushnotificationsdroid.classes.ConstantsSPush;

public class TextViewCustomFont extends TextView {


	private static final String TAG = "TextViewCustomFontSPush";

	/**
	 * VERSIÓ SIMPLIFICADA DE L'OBJECTE TEXTVIEWCUSTOMFONT. PER VEURE LA VERSIÓ COMPLETA ES POT ANAR AL PROJECTE BASE DEL TOPPO
	 */
	//	public static final String FONT_TYPE_EXTRA_LIGHT = "ExtraLight";
	//	public static final String FONT_TYPE_LIGHT = "Light";
	//	public static final String FONT_TYPE_REGULAR = "Regular";
	//	public static final String FONT_TYPE_MEDIUM = "Medium";
	//	public static final String FONT_TYPE_SEMI_BOLD = "SemiBold";
	//	public static final String FONT_TYPE_BOLD = "Bold";
	//	public static final String FONT_TYPE_EXTRA_BOLD = "ExtraBold";

	public TextViewCustomFont(Context context) {
		super(context);
	}

	public TextViewCustomFont(Context context, AttributeSet attrs) {
		super(context, attrs);
		//		setCustomFont(context, attrs);
	}

	public TextViewCustomFont(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//		setCustomFont(context, attrs);
	}

	//	private void setCustomFont(Context ctx, AttributeSet attrs) {
	//		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewCustomFont);
	//		String customFont = a.getString(R.styleable.TextViewCustomFont_customFont);
	//		setCustomFont(ctx, customFont);
	//		a.recycle();
	//	}

	//	public boolean setCustomFont(Context ctx, String asset) {
	//		Typeface tf = null;
	//
	//		try {
	//			if (asset.equals(FONT_TYPE_BOLD)) {
	//				tf = ((ToppoApplication) ctx.getApplicationContext()).getFontBold();
	//			} else if (asset.equals(FONT_TYPE_EXTRA_BOLD)) {
	//				tf = ((ToppoApplication) ctx.getApplicationContext()).getFontExtraBold();
	//			} else if (asset.equals(FONT_TYPE_EXTRA_LIGHT)) {
	//				tf = ((ToppoApplication) ctx.getApplicationContext()).getFontExtraLight();
	//			} else if (asset.equals(FONT_TYPE_LIGHT)) {
	//				tf = ((ToppoApplication) ctx.getApplicationContext()).getFontLight();
	//			} else if (asset.equals(FONT_TYPE_MEDIUM)) {
	//				tf = ((ToppoApplication) ctx.getApplicationContext()).getFontMedium();
	//			} else if (asset.equals(FONT_TYPE_REGULAR)) {
	//				tf = ((ToppoApplication) ctx.getApplicationContext()).getFontRegular();
	//			} else if (asset.equals(FONT_TYPE_SEMI_BOLD)) {
	//				tf = ((ToppoApplication) ctx.getApplicationContext()).getFontSemiBold();
	//			}
	//
	//			if (tf != null) {
	//				setTypeface(tf);
	//			}
	//
	//		} catch (Exception e) {
	//			Log.e(TAG, "Could not get typeface: " + e.getMessage());
	//			return false;
	//		}
	//
	//		return true;
	//	}

	public void setCustomFontByFontName(String _strFontName, Context _context) {

		try {
			if (!_strFontName.contains(ConstantsSPush.DEFAULT_FONT_NAME_1) && !_strFontName.contains(ConstantsSPush.DEFAULT_FONT_NAME_2)) {
//				Typeface tf = Typeface.createFromAsset(_context.getAssets(), SPushTypefaceSpan.getTypefaceExtension(_context, _strFontName));
				Typeface tf = FontManager.getTypeFace(_context, _strFontName);
				if (tf != null) {
					setTypeface(tf);
				}
			} else if (_strFontName.contains("Bold")) {
				setTypeface(Typeface.DEFAULT_BOLD);
			}
		} catch (Exception e) {
			Log.e(TAG, "Could not get typeface: " + e.getMessage());
		}

	}

}