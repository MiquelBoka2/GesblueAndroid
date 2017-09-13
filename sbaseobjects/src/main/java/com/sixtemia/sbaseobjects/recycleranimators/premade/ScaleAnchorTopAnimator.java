package com.sixtemia.sbaseobjects.recycleranimators.premade;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Interpolator;

import com.sixtemia.sbaseobjects.recycleranimators.BaseItemAnimator;

/**
 * Created by rubengonzalez on 24/5/16.
 */

public class ScaleAnchorTopAnimator extends BaseItemAnimator {
    //https://github.com/wasabeef/recyclerview-animators/tree/master/animators/src/main/java/jp/wasabeef/recyclerview/animators

    public ScaleAnchorTopAnimator() {
        setAddDuration(170);
        setRemoveDuration(250);
    }

    public ScaleAnchorTopAnimator(int addDuration, int removeDuration) {
        setAddDuration(addDuration);
        setRemoveDuration(removeDuration);
    }

    public ScaleAnchorTopAnimator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override protected void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {
        // @TODO https://code.google.com/p/android/issues/detail?id=80863
        //        ViewCompat.setPivotY(holder.itemView, 0);
        holder.itemView.setPivotY(0);
    }

    @Override protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .scaleX(0)
                .scaleY(0)
                .setDuration(getRemoveDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultRemoveVpaListener(holder))
                .setStartDelay(getRemoveDelay(holder))
                .start();
    }

    @Override protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        // @TODO https://code.google.com/p/android/issues/detail?id=80863
        //        ViewCompat.setPivotY(holder.itemView, 0);
        holder.itemView.setPivotY(0);
        ViewCompat.setScaleX(holder.itemView, 0);
        ViewCompat.setScaleY(holder.itemView, 0);
    }

    @Override protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .scaleX(1)
                .scaleY(1)
                .setDuration(getAddDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultAddVpaListener(holder))
                .setStartDelay(getAddDelay(holder))
                .start();
    }
}