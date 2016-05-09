package com.borges.moises.materialtodolist.baseui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.borges.moises.materialtodolist.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 11/04/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        setupToolbar();
        replaceFrameLayoutWithFragment();
    }

    protected void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void replaceFrameLayoutWithFragment() {
        final Fragment fragment = getFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
    }

    protected abstract int getLayoutId();

    protected abstract Fragment getFragment();

}
