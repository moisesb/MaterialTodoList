package com.borges.moises.materialtodolist.todoitems;

import android.support.v4.app.Fragment;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.baseactivities.BaseActivity;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment_and_fab;
    }

    @Override
    protected Fragment getFragment() {
        return TodoItemsFragment.newInstace();
    }
}
