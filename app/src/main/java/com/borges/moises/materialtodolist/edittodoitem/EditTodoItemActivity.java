package com.borges.moises.materialtodolist.edittodoitem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.widget.TextView;

import com.borges.moises.materialtodolist.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 24/04/2016.
 */
public class EditTodoItemActivity extends AppCompatActivity {

    public static final String ARG_TODO_ITEM_ID = "com.borges.moises.materialtodolist.todoitemdetails.todoItemId";
    public static final int REQUEST_CODE = 2;
    public static final int EDITED_RESULT_CODE = 200;
    public static final int DELETED_RESULT_CODE = 300;
    public static final String TODO_ITEM_ID = "todo_item_id";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_title_text_view)
    TextView mTitleTextView;

    @BindString(R.string.edit_task)
    String mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);
        ButterKnife.bind(this);
        setupWindowAnimations();

        setupToolbar();
        initFragment();
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);
        }
    }


    private void initFragment() {
        final long todoItemId = getIntent().getLongExtra(ARG_TODO_ITEM_ID,-1);

        if (todoItemId < 0) {
            throw new IllegalArgumentException("todo item id invalid: " + todoItemId);
        }

        Fragment fragment = EditTodoItemFragment.newInstance(todoItemId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
    }

    protected void setupToolbar() {
        setSupportActionBar(mToolbar);
        mTitleTextView.setText(mTitle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void start(Activity activity, long todoItemId) {
        Intent intent = new Intent(activity, EditTodoItemActivity.class);
        intent.putExtra(ARG_TODO_ITEM_ID, todoItemId);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }
}
