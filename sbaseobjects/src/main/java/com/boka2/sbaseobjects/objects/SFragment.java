package com.boka2.sbaseobjects.objects;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

/**
 * Created by Boka2.
 */
public class SFragment extends Fragment {

    private Resources mResources;
    public Context mContext;
    private Toolbar mToolbar;
    protected boolean mAnimateNextEnter = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResources = getResources();
        mContext = getActivity();
    }

    public SFragment recreateFragment(FragmentManager mFragmentManager)
    {
        try {
            SavedState savedState = mFragmentManager.saveFragmentInstanceState(this);

            SFragment newInstance = getClass().newInstance();
            newInstance.setInitialSavedState(savedState);
            newInstance.setAnimateNext(isAnimateNext());

            return newInstance;
        }
        catch (Exception e) // InstantiationException, IllegalAccessException
        {
            throw new RuntimeException("Cannot reinstantiate fragment " + getClass().getSimpleName(), e);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setAnimateNext(false);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    protected void setToolbar(View v, int toolbar, int toolbarContainer, boolean withMargin) {
        mToolbar = (Toolbar) v.findViewById(toolbar);
        if(withMargin) {
            setStatusBarPadding(v.findViewById(toolbarContainer));
        }
    }

    protected void setToolbar(View v, int toolbar) {
        mToolbar = (Toolbar) v.findViewById(toolbar);
    }

    protected void setStatusBarPadding(View v) {
        if(v != null) {
            v.setPadding(0, getStatusBarHeight(), 0, 0);
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected void setMenu(int menu, Toolbar.OnMenuItemClickListener listener) {
        if(mToolbar != null) {
            if(mToolbar.getMenu() != null) {
                mToolbar.getMenu().clear();
            }
            mToolbar.inflateMenu(menu);
            mToolbar.setOnMenuItemClickListener(listener);
        }
    }

    protected void removeMenu() {
        if(mToolbar != null) {
            if(mToolbar.getMenu() != null) {
                mToolbar.getMenu().clear();
            }
        }
    }

    protected void removeMenu(Toolbar.OnMenuItemClickListener listener) {
        removeMenu();
        if(listener != null) {
            mToolbar.setOnMenuItemClickListener(listener);
        }
    }

    /**
     * Indica que si mostrarà o no la propera animació de entrada o sortida.
     * Per defecte sempre serà true.
     *
     * @param mAnimateNext
     */
    public void setAnimateNext(boolean mAnimateNext) {
        this.mAnimateNextEnter = mAnimateNext;
        DLog("BaseFragment", "Animate: " + mAnimateNext);
    }

    public boolean isAnimateNext() {
        return mAnimateNextEnter;
    }

    protected void DLog(String TAG, String what) {
        Context c = getContext();
        if(c != null && isDebugging(c)) {
            Log.i(TAG, what);
        }
    }

    private static boolean isDebugging(Context _context) {
        boolean debuggable = false;
        PackageManager pm = _context.getPackageManager();

        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(_context.getPackageName(), 0);
            debuggable = (appinfo.flags &= 2) != 0;
        } catch (PackageManager.NameNotFoundException var4) {
            ;
        }

        return debuggable;
    }

    protected void DLog(String what) {
        DLog(getClass().getSimpleName(), what);
    }

    protected void removeToolbarElevation() {
        if(mToolbar !=null && Build.VERSION.SDK_INT >= 21) {
            mToolbar.setElevation(0);
        }
    }

    private float pixelsToSp(float px)
    {
        float scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    protected float dimenToSp(int dimen) {
        return pixelsToSp(getResources().getDimension(dimen));
    }

    /**
     * Inicia una transició automàtica amb una duració de 100ms
     */
    protected void anima() {
        anima(getView());
    }

    /**
     * Inicia una transició automàtica amb la duració indicada
     * @param duration Duració de la animació
     */
    protected void anima(long duration) {
        anima(getView(), duration);
    }

    private void anima(ViewGroup g, long duration) {
        if(g != null) {
            Transition t = TransitionManager.getDefaultTransition();
            t.setDuration(duration);
            TransitionManager.beginDelayedTransition(g, t);
        }
    }

    protected void anima(View v) {
        if(v != null && v instanceof ViewGroup) {
            anima((ViewGroup) v, 100);
        }
    }

    private void anima(View v, long duration) {
        if(v != null && v instanceof ViewGroup) {
            anima((ViewGroup) v, duration);
        }
    }
}