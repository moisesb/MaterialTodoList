package com.borges.moises.materialtodolist.data.model;

import com.borges.moises.materialtodolist.R;

/**
 * Created by Moisés on 11/04/2016.
 */
public enum Priority {
    NORMAL(R.string.priority_normal), HIGH(R.string.priority_high);

    private final int mResId;

    Priority(int resId) {
        mResId = resId;
    }

    public int stringResId() {
        return mResId;
    }
}
