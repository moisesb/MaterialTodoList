package com.borges.moises.materialtodolist.addtodoitem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.borges.moises.materialtodolist.R;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddTodoItemActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.activity_title_text_view)
    TextView mTitleTextView;

    @BindString(R.string.add_new_task)
    String mTitle;

    public AddTodoItemActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);
        ButterKnife.bind(this);

        setupToolbar();
        initFragment();
    }

    private void initFragment() {
        Fragment fragment = AddTodoItemFragment.newFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout,fragment)
                .commit();
    }

    protected void setupToolbar() {
        setSupportActionBar(mToolbar);
        mTitleTextView.setText(mTitle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, AddTodoItemActivity.class));
    }
}
