package com.borges.moises.materialtodolist.createaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.borges.moises.materialtodolist.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateAccountActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

        addFragment();
    }

    private void addFragment() {
        Fragment fragment = CreateAccountFragment.newInstace();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame_layout,fragment)
                .commit();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context,CreateAccountActivity.class);
        context.startActivity(intent);
    }
}
