package com.sixtemia.sutils.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Custom implementation of SeekBar to show the bar in vertical.
 * Implementation example: iCatFM
 * 
 * @author jaumebech
 * @version 1.0
 */
public class SVerticalSeekBar extends SeekBar {

	private OnSeekBarChangeListener myListener;
	public SVerticalSeekBar(Context context) {
	    super(context);
	}

	public SVerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	}

	public SVerticalSeekBar(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	    super.onSizeChanged(h, w, oldh, oldw);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(heightMeasureSpec, widthMeasureSpec);
	    setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
	    this.myListener = mListener;
	}

	protected void onDraw(Canvas c) {
	    c.rotate(-90);
	    c.translate(-getHeight(), 0);

	    super.onDraw(c);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (!isEnabled()) {
	        return false;
	    }

	    switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            if(myListener!=null) {
	                myListener.onStartTrackingTouch(this);
	            
	                int progress = getMax() - (int) (getMax() * event.getY() / getHeight());
	            
	                if(progress <= getMax() && progress >= 0) {
	            		setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
	    	            onSizeChanged(getWidth(), getHeight(), 0, 0);
	    	            myListener.onProgressChanged(this, getMax() - (int) (getMax() * event.getY() / getHeight()), true);
	            	}
	            }
	            
	            break;
	        case MotionEvent.ACTION_MOVE:
	        	
	        	int progress = getMax() - (int) (getMax() * event.getY() / getHeight());
            	
            	if(progress <= getMax() && progress >= 0) {
            		setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
    	            onSizeChanged(getWidth(), getHeight(), 0, 0);
    	            myListener.onProgressChanged(this, getMax() - (int) (getMax() * event.getY() / getHeight()), true);
            	}
            	
	            break;
	        case MotionEvent.ACTION_UP:
	            myListener.onStopTrackingTouch(this);
	            break;

	        case MotionEvent.ACTION_CANCEL:
	            break;
	    }
	    return true;
	}
}