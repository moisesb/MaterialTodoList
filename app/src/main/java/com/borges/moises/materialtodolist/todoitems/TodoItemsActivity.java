package com.borges.moises.materialtodolist.todoitems;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Slide;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.baseui.BaseActivity;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();

    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setExitTransition(slide);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment_and_fab;
    }

    @Override
    protected Fragment getFragment() {
        return TodoItemsFragment.newInstace();
    }
}
