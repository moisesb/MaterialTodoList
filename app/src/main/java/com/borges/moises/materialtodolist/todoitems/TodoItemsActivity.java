package com.borges.moises.materialtodolist.todoitems;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.createaccount.CreateAccountActivity;
import com.borges.moises.materialtodolist.data.model.User;
import com.borges.moises.materialtodolist.data.services.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_items);
        ButterKnife.bind(this);
        setupWindowAnimations();

        setupToolbar();
        initFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupDrawer();
    }

    private void setupDrawer() {
        showMenuOptions(SessionManager.getInstance().getSignedInUser());

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sign_in_menu:
                        openSignIn();
                        break;
                    case R.id.sign_out_menu:
                        signOut();
                        break;
                    default:
                        throw new IllegalStateException("Not implemented");
                }

                mDrawerLayout.closeDrawers();
                return true;

            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.openDrawer, R.string.closeDrawer) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void showMenuOptions(User user) {
        mNavigationView.getMenu().clear();
        if (user != null) {
            mNavigationView.inflateMenu(R.menu.menu_drawer_logged_user);
        }else {
            mNavigationView.inflateMenu(R.menu.menu_drawer_no_logged_user);
        }
    }

    private void signOut() {
        final SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.logout();
        mDrawerLayout.closeDrawers();
        showMenuOptions(sessionManager.getSignedInUser());
    }

    private void openSignIn() {
        CreateAccountActivity.start(this);
    }

    private void initFragment() {
        Fragment contentFragment = TodoItemsFragment.newInstace();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout,contentFragment)
                .commit();
    }

    protected void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setExitTransition(slide);
        }
    }
}
