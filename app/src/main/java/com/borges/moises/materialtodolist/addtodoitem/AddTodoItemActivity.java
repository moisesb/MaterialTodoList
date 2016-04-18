package com.borges.moises.materialtodolist.addtodoitem;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.baseactivities.BaseActivity;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddTodoItemActivity extends BaseActivity {

    public AddTodoItemActivity() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected Fragment getFragment() {
        return AddTodoItemFragment.newFragment();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddTodoItemActivity.class);
    }
}
