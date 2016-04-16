package com.borges.moises.materialtodolist.addnewtodoitem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.baseactivities.BaseActivity;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddNewTodoItemActivity extends BaseActivity {

    public AddNewTodoItemActivity() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected Fragment getFragment() {
        return AddNewTodoItemFragment.newFragment();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddNewTodoItemActivity.class);
    }
}
