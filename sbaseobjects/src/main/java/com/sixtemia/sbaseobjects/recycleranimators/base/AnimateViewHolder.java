package com.sixtemia.sbaseobjects.recycleranimators.base;

import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by rubengonzalez on 24/5/16.
 */

public abstract class AnimateViewHolder extends RecyclerView.ViewHolder {
    public AnimateViewHolder(View itemView) {
        super(itemView);
    }

    public void preAnimateAddImpl() {
    }

    public void preAnimateRemoveImpl() {
    }

    public abstract void animateAddImpl(ViewPropertyAnimatorListener listener);

    public abstract void animateRemoveImpl(ViewPropertyAnimatorListener listener);
}