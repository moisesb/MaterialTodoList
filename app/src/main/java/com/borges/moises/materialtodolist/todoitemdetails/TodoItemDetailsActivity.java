package com.borges.moises.materialtodolist.todoitemdetails;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.baseactivities.BaseActivity;

/**
 * Created by Moisés on 24/04/2016.
 */
public class TodoItemDetailsActivity extends BaseActivity {

    public static final String ARG_TODO_ITEM_ID = "com.borges.moises.materialtodolist.todoitemdetails.todoItemId";

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected Fragment getFragment() {
        long todoItemId = getIntent().getLongExtra(ARG_TODO_ITEM_ID, -1);
        return TodoItemDetailsFragment.newFragment(todoItemId);
    }

    public static void start(Context context, long todoItemId) {
        Intent intent = new Intent(context,TodoItemDetailsActivity.class);
        intent.putExtra(ARG_TODO_ITEM_ID, todoItemId);
        context.startActivity(intent);
    }
}
