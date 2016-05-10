package com.borges.moises.materialtodolist.todoitems;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;

import com.borges.moises.materialtodolist.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setupWindowAnimations();

        setupToolbar();
        initFragment();

    }

    private void initFragment() {
        Fragment fragment = TodoItemsFragment.newInstace();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout,fragment)
                .commit();
    }

    protected void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setExitTransition(slide);
        }
    }
}
