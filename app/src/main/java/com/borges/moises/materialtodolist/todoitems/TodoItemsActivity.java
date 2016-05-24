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
import com.borges.moises.materialtodolist.data.services.UserService;
import com.borges.moises.materialtodolist.login.LoginActivity;
import com.borges.moises.materialtodolist.menu.MenuMvp;
import com.borges.moises.materialtodolist.menu.MenuPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsActivity extends AppCompatActivity implements MenuMvp.View{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    MenuMvp.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_items);
        ButterKnife.bind(this);
        setupWindowAnimations();
        mPresenter = new MenuPresenter(new UserService(this));
        mPresenter.bindView(this);
        setupToolbar();
        initFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupDrawer();
        mPresenter.loadMenu();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    private void setupDrawer() {

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sign_in_menu:
                        mPresenter.openLoginOrCreateAccount();
                        break;
                    case R.id.sign_out_menu:
                        mPresenter.logout();
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

    @Override
    public void showLoginMenu() {
        mDrawerLayout.closeDrawers();
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.menu_drawer_no_logged_user);
    }

    @Override
    public void showLogoutMenu() {
        mDrawerLayout.closeDrawers();
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.menu_drawer_logged_user);
    }

    @Override
    public void openLogin() {
        LoginActivity.start(this);
    }

    @Override
    public void openCreateAccount() {
        CreateAccountActivity.start(this);
    }
}
